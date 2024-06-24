package com.example.demo.services;

import com.example.demo.configuration.ApartmentValidationException;
import com.example.demo.configuration.BaseService;
import com.example.demo.dao.ApartmentDOA;
import com.example.demo.dto.ApartmentDto;
import com.example.demo.dto.ContributorDto;
import com.example.demo.dto.FinalProfitDTO;
import com.example.demo.entity.Apartment;
import com.example.demo.entity.Contributor;
import com.example.demo.vaildation.ApartmentValidation;
import com.example.demo.vaildation.ApartmentsTotalAmountCalculator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@CommonsLog
public class ApartmentService extends BaseService {
    @Autowired
    private ApartmentDOA apartmentDOA;
    @Autowired
    private ApartmentValidation apartmentValidation;
    @Autowired
    private ApartmentsTotalAmountCalculator apartmentsTotalAmountCalculator;
    @Transactional
    public String saveOrUpdateApartment(ApartmentDto apartmentDto) throws Exception {
        log.info("Start saveOrUpdateApartment >>>>: create or update Apartment >>>>");
        try {
            // Validate the incoming data
            apartmentValidation.validateApartmentForSave(apartmentDto);

            // Check if apartment exists
            Optional<Apartment> existingApartmentOpt = apartmentDOA.findByApartmentCode(apartmentDto.getApartmentCode());

            Apartment apartmentToSave;

            if (existingApartmentOpt.isPresent()) {
                // Apartment exists, update it
                Apartment existingApartment = existingApartmentOpt.get();
                log.info("Updating existing apartment with code: " + apartmentDto.getApartmentCode());
                // Update apartment data
                if (apartmentDto.getApartmentCodeNew() != null && !apartmentDto.getApartmentCodeNew().isEmpty()) {
                    log.info("Apartment code updated to: " + apartmentDto.getApartmentCodeNew());
                    existingApartment.setApartmentCode(apartmentDto.getApartmentCodeNew());
                }
                existingApartment.setLocationApartment(apartmentDto.getLocationApartment());
                existingApartment.setPurchaseApartment(apartmentDto.getPurchaseApartment());
                existingApartment.setExpenses(apartmentDto.getExpenses());
                if(apartmentDto.getPurchaseApartment()!=0 && apartmentDto.getExpenses()!=0){
                    double sum= apartmentDto.getPurchaseApartment() + apartmentDto.getExpenses();
                    existingApartment.setTotalCost(sum);
                }
                existingApartment.setAmountApartmentSale(apartmentDto.getAmountApartmentSale());
                if(apartmentDto.getAmountApartmentSale()!=0){
                    double sumEnd=existingApartment.getAmountApartmentSale() - existingApartment.getTotalCost();
                    existingApartment.setNetOfApartment(sumEnd);
                }
                existingApartment.setComments(apartmentDto.getComments());
                // Update existing contributors
                List<Contributor> updatedContributors = apartmentDto.getContributors().stream()
                        .map(contributorDto -> updateExistingContributor(contributorDto, existingApartment.getContributor(), existingApartment))
                        .collect(Collectors.toList());
                apartmentsTotalAmountCalculator.sumAmount(apartmentDto.getApartmentCode());
                existingApartment.setContributor(updatedContributors);
                // Use existing apartment to save
                apartmentToSave = existingApartment;
            } else {
                // Apartment does not exist, create new one
                log.info("Creating new apartment with code: " + apartmentDto.getApartmentCode());
                Apartment newApartment = apartmentValidation.mapDtoToEntity(apartmentDto);
                apartmentValidation.setApartmentDefaults(newApartment);
                // Add new contributors
                if (apartmentDto.getContributors() != null && !apartmentDto.getContributors().isEmpty()) {
                    List<Contributor> newContributors = apartmentDto.getContributors().stream()
                            .map(contributorDto -> ContributorDto.mapToContributor(contributorDto, newApartment))
                            .collect(Collectors.toList());
                    newApartment.setContributor(newContributors);
                }
                // Use new apartment to save
                apartmentToSave = newApartment;
            }
            // Save the apartment (whether new or updated)
            apartmentDOA.save(apartmentToSave);
            log.info("Apartment saved or updated successfully");
            return "Apartment saved or updated successfully";
        } catch (Exception e) {
            throw new Exception("Error occurred while saving or updating apartment: " + e.getMessage());
        }
    }
    private Contributor updateExistingContributor(ContributorDto contributorDto, List<Contributor> existingContributors, Apartment apartment) {

        Optional<Contributor> existingContributorOpt = existingContributors.stream()
                .filter(contributor -> contributor.getName().equals(contributorDto.getName()))
                .findFirst();
        if (existingContributorOpt.isPresent()) {
            Contributor existingContributor = existingContributorOpt.get();
            existingContributor.setAmountInvested(contributorDto.getAmountInvested());
            return existingContributor;
        } else {
            return ContributorDto.mapToContributor(contributorDto, apartment);
        }
    }

    public ApartmentDto getApartment(String apartmentCode) throws Exception {
        log.info("Start getApartment >>>>: get Apartment by code  and status :)> ");
        try {
            return apartmentValidation.validateApartmentCode(apartmentCode);
        } catch (Exception e) {
            throw new ApartmentValidationException(e.getMessage(), "");
        }
    }
    public Apartment getByCode(String apartmentCode) {
        return apartmentDOA.findByApartmentCode(apartmentCode)
                .orElseThrow(() -> new EntityNotFoundException("Apartment not found with code: " + apartmentCode));
    }


    public List<String>  getNameApartmentCode() throws Exception {
        log.info("Start getApartment >>>>: get Apartment by code  and status :)> ");
        try {
        return apartmentDOA.findAllActiveApartmentsName();
        } catch (Exception e) {
            throw new ApartmentValidationException(e.getMessage(), "");
        }
    }

    public List<ApartmentDto> findAllApartments() throws Exception {
        try {
            log.info("start findAllApartments : > ");
            List<Apartment> apartments = apartmentDOA.findAllActiveApartments();

            if (apartments.isEmpty()) {
                throw new Exception("No apartments found.");
            }

            return apartments.stream()
                    .map(ApartmentDto::mapToApartmentDto)
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            throw new Exception("There is an error in retrieving the data. Please try again", exception);
        }
    }

    public void deleteApartment(String apartmentCode) throws Exception {
        try {
            log.info("start deleteApartment : > " + apartmentCode);
            apartmentValidation.delete(apartmentCode);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }
public void finalProfit(String apartmentCode, FinalProfitDTO finalProfitDTO) throws Exception {
    try {
        log.info("Start finalProfit for Apartment: " + apartmentCode);
        apartmentValidation.performCalculationsAndSave(apartmentCode,finalProfitDTO);
    } catch (Exception exception) {
        throw new Exception(exception.getMessage());
    }
}
}
