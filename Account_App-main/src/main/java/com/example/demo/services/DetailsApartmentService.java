package com.example.demo.services;

import com.example.demo.configuration.ApartmentValidationException;
import com.example.demo.configuration.BaseService;
import com.example.demo.configuration.Utility;
import com.example.demo.dao.ApartmentDOA;
import com.example.demo.dao.DetailsApartmentDAO;
import com.example.demo.dto.DetailsApartmentDto;
import com.example.demo.dto.DetailsApartmentDtoSql;
import com.example.demo.entity.Apartment;
import com.example.demo.entity.DetailsApartment;
import com.example.demo.entity.SystemType;
import com.example.demo.vaildation.ApartmentValidation;
import com.example.demo.vaildation.ApartmentsTotalAmountCalculator;
import com.example.demo.vaildation.DetailsApartmentValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@CommonsLog
public class DetailsApartmentService extends BaseService {

    @Autowired
    private DetailsApartmentDAO detailsApartmentDAO;
    @Autowired
    private DetailsApartmentValidator detailsApartmentValidator;
    @Autowired
    private ApartmentsTotalAmountCalculator apartmentsTotalAmountCalculator;
    @Autowired
    private ApartmentDOA apartmentDOA;
    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private SystemTypeService systemTypeService;
    @Autowired
    private ApartmentValidation apartmentValidation;

    public void save(DetailsApartmentDto detailsApartmentDto) throws Exception {
        try {
            log.info("enter with save");
            Apartment apartmentId = apartmentService.getByCode(detailsApartmentDto.getApartmentCode());
            SystemType lookupTypeId = systemTypeService.getLookupTypeCode(detailsApartmentDto.getLookupType());
            if (apartmentId == null && lookupTypeId == null ) {
                throw new EntityNotFoundException("Apartment and SystemType not found : " + detailsApartmentDto.getApartmentCode() + detailsApartmentDto.getLookupType());
            }
            DetailsApartment detailsApartment= detailsApartmentValidator.mapDtoToEntity(detailsApartmentDto);
            detailsApartment.setApartment(apartmentId);
            detailsApartment.setSystemType(lookupTypeId);
            detailsApartmentValidator.validateBooleanFields(detailsApartmentDto);
            double sum= detailsApartment.getAmount()+detailsApartment.getApartment().getExpenses()+detailsApartment.getApartment().getPurchaseApartment();
            detailsApartment.getApartment().setTotalCost(sum);
            detailsApartmentDAO.save(detailsApartment);
            apartmentsTotalAmountCalculator.sumAmount(detailsApartmentDto.getApartmentCode());
            Utility.getGson().toJson(new Utility("Details Apartment", SUCCESS));
        } catch (Exception exception) {
            log.info("invalided save");
            throw new ApartmentValidationException(exception.getMessage(),"invalided save");
        }
    }
    public List<DetailsApartmentDtoSql> getDetails(String apartmentCode) throws ValidationException {
        if (apartmentCode == null || apartmentCode.trim().isEmpty()) {
            throw new ValidationException("Invalid or empty apartment code");
        }
            return detailsApartmentDAO.findAllByApartmentCode(apartmentCode);
        }

    public void delete(int id) throws Exception {
        try {
            log.info("Start delete Apartment");
            Optional<DetailsApartment> optionalApartment = detailsApartmentDAO.findById(id);

            if (optionalApartment.isPresent()) {
                DetailsApartment apartmentDetails = optionalApartment.get();
                Apartment apartment = apartmentDetails.getApartment();
                if (apartment != null) {
                    double newExpenses = apartment.getExpenses() - apartmentDetails.getAmount();

                    if (newExpenses <=0) {
                        apartment.setExpenses(0);
                    }else {
                        apartment.setExpenses(newExpenses);
                    }
                    double total = apartment.getTotalCost()- apartmentDetails.getAmount();
                    apartment.setTotalCost(total);
//                    apartmentDOA.save(apartment);
                    apartmentValidation.updateExpensesForApartment(apartment.getApartmentCode(), newExpenses);
                    detailsApartmentDAO.deleteById(id);

                }
            }
        } catch (Exception e) {
            throw new Exception("An error occurred while deleting the apartment: " + e.getMessage());
        }
    }
}