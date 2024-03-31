package com.example.demo.services;

import com.example.demo.configuration.ApartmentValidationException;
import com.example.demo.configuration.BaseService;
import com.example.demo.configuration.Utility;
import com.example.demo.dao.ApartmentJpaDOA;
import com.example.demo.dto.ApartmentDto;
import com.example.demo.entity.Apartment;
import com.example.demo.vaildation.ApartmentUpdater;
import com.example.demo.vaildation.ApartmentValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Optional;

@Service
@CommonsLog
public class ApartmentService extends BaseService {
    @Autowired
    private ApartmentJpaDOA apartmentDOA;
    @Autowired
    private ApartmentValidation apartmentValidation;
    @Autowired
    private ApartmentUpdater apartmentUpdater;
    public String save(ApartmentDto apartmentDto) throws Exception {
        log.info("Start save >>>>: create Apartment >>>>");
        try {
            apartmentValidation.validateApartmentForSave(apartmentDto);
            Apartment apartment = apartmentValidation.mapDtoToEntity(apartmentDto);
            apartmentValidation.setApartmentDefaults(apartment);
            log.info("this is save apartment");
            apartmentDOA.save(apartment);
            return "this is save apartment";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public void updateApartments(@RequestBody ApartmentDto apartmentDto)throws Exception {
        try {
            log.info("Enter api to update Apartments ");
            apartmentUpdater.updateApartments(apartmentDto);
            log.info("this is save apartment");
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }
    public Apartment getApartment(String apartmentCode) throws Exception {
        log.info("Start getApartment >>>>: get Apartment by code  and status :)> ");
        try {
        return   apartmentValidation.validateApartmentCode(apartmentCode);
        } catch (Exception e) {
            throw new ApartmentValidationException(e.getMessage());
        }
    }
    public List<Apartment> findAllApartment() throws Exception {
        try {
            log.info("start findAllApartment : > ");
            return apartmentDOA.findAllActiveApartments();
        } catch (Exception exception) {
            throw new Exception("There is an error in retrieving the data. Please try again");
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
public void finalProfit(String apartmentCode, Double newPurchaseApartment, Double newAmountApartmentSale) throws Exception {
    try {
        log.info("Start finalProfit for Apartment: " + apartmentCode);
        apartmentValidation.performCalculationsAndSave(apartmentCode,newPurchaseApartment,newAmountApartmentSale);
    } catch (Exception exception) {
        throw new Exception(exception.getMessage());
    }
}
}
