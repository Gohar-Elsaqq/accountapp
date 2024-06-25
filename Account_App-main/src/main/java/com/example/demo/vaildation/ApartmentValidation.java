package com.example.demo.vaildation;

import com.example.demo.configuration.ApartmentValidationException;
import com.example.demo.dao.ApartmentDOA;
import com.example.demo.dto.ApartmentDto;
import com.example.demo.dto.FinalProfitDTO;
import com.example.demo.entity.Apartment;
import com.example.demo.entity.Contributor;
import com.example.demo.enums.Status;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.extern.apachecommons.CommonsLog;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Optional;
@Component
@CommonsLog
public class ApartmentValidation {
    @Autowired
    private ApartmentDOA apartmentDOA;
    private final ModelMapper modelMapper = new ModelMapper();

    public void validateApartmentForSave(ApartmentDto apartmentDto) throws Exception {
        log.info("Validation for save model");
        validateApartmentDto(apartmentDto);
    }

    public void validateApartmentDto(ApartmentDto apartmentDto) throws Exception {
        if (apartmentDto == null) {
            throw new ApartmentValidationException("Invalid apartment", "");
        }
        String apartmentCode = apartmentDto.getApartmentCode();
        if (apartmentCode == null || apartmentCode.isEmpty()) {
            throw new ApartmentValidationException("Invalid or empty apartment code", "");
        }
        String locationApartment = apartmentDto.getLocationApartment();
        if (locationApartment == null || locationApartment.isEmpty()) {
            throw new ApartmentValidationException("Invalid or empty  location Apartment", "");
        }
        String purchaseApartment = String.valueOf(apartmentDto.getPurchaseApartment());
        if (purchaseApartment.isEmpty()) {
            throw new ApartmentValidationException("Invalid or empty  purchaseApartment", "");
        }
    }
    public Apartment mapDtoToEntity(ApartmentDto apartmentDto) {

        return modelMapper.map(apartmentDto, Apartment.class);
    }
    public void setApartmentDefaults(Apartment apartment) {
        apartment.setStatus(String.valueOf(Status.ACT));
    }

    public ApartmentDto validateApartmentCode(String apartmentCode) throws ValidationException {
        if (apartmentCode == null || apartmentCode.trim().isEmpty()) {
            throw new ValidationException("Invalid or empty apartment code");
        }
        Optional<Apartment> optionalApartment = apartmentDOA.findByApartmentCodeAndStatus(apartmentCode, String.valueOf(Status.ACT));
        if (optionalApartment.isPresent()) {
            return ApartmentDto.mapToApartmentDto(optionalApartment.get());
        } else {
            throw new ValidationException("Apartment with code : > " + apartmentCode + " < : not found");
        }
    }
    public void delete(String apartmentCode) throws Exception {
        if (apartmentCode == null) {
            throw new Exception("Invalid apartment code");
        }
        Apartment existingApartment = apartmentDOA.findByApartmentCode(apartmentCode)
                .orElseThrow(() -> new ApartmentValidationException("not found ", ""));
        if (existingApartment.getStatus().equals(String.valueOf(Status.SUSPEND))) {
            throw new ApartmentValidationException("apartment is already delete: " + apartmentCode, "");
        }
        existingApartment.setStatus(String.valueOf(Status.SUSPEND));
        apartmentDOA.save(existingApartment);
    }
    @Transactional
    public void updateExpensesForApartment(String apartmentCode, double newExpenses) throws ApartmentValidationException {
        try {
            log.info("start updating");
            Optional<Apartment> optionalApartment = apartmentDOA.findByApartmentCode(apartmentCode);
            if (optionalApartment.isPresent()) {
                Apartment apartment = optionalApartment.get();
                apartment.setExpenses(newExpenses);
                double totalInvestedAmount = 0;
                for (Contributor contributor : apartment.getContributor()) {
                    totalInvestedAmount += contributor.getAmountInvested();
                }

                for (Contributor contributor : apartment.getContributor()) {
                    double sharePercentage = contributor.getAmountInvested() / totalInvestedAmount;
                    double expensesForContributor = newExpenses * sharePercentage;
                    contributor.setShareholdersExpenses(expensesForContributor);
                }
                double totalSaleAmount = apartment.getNetOfApartment();
                for (Contributor contributor : apartment.getContributor()) {
                    double sharePercentage = contributor.getAmountInvested() / totalInvestedAmount;
                    double profitForContributor = totalSaleAmount * sharePercentage;
                    contributor.setShareholdersProfits(profitForContributor);
                    double endData =contributor.getShareholdersProfits()-contributor.getShareholdersExpenses() ;
                    contributor.setEndData(endData);
                }
                apartmentDOA.save(apartment);
            }
            log.info("Successfully updated expenses and profits for Apartment: " + apartmentCode);
        } catch (Exception e) {
            throw new ApartmentValidationException("An error occurred while updating expenses and profits: " + e.getMessage(), "");
        }
    }


    public void performCalculationsAndSave(String apartmentCode, FinalProfitDTO finalProfitDTO) throws ApartmentValidationException {
        try {
            log.info("Calculations and save");
            Optional<Apartment> optionalApartment = apartmentDOA.findByApartmentCode(apartmentCode);
            Apartment existingApartment = optionalApartment.orElseThrow(() -> new ApartmentValidationException("Apartment not found for code: " + apartmentCode, ""));
            existingApartment.setAmountApartmentSale(finalProfitDTO.getAmountApartmentSale());
                    if (finalProfitDTO.getAmountApartmentSale() != 0) {

                        double netOfApartment = finalProfitDTO.getAmountApartmentSale() - existingApartment.getTotalCost();;
                        existingApartment.setNetOfApartment(netOfApartment);
                    }
                    existingApartment.setSaleDate(LocalDateTime.now());
                    apartmentDOA.save(existingApartment);
                    log.info("Successfully performed calculations and updated values for Apartment: " + existingApartment.getApartmentCode());
        }catch(Exception e){
            throw new ApartmentValidationException("An error occurred while performing calculations and updating values: " + e.getMessage(), "");
        }
    }
}

