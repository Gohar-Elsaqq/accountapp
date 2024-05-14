package com.example.demo.vaildation;

import com.example.demo.configuration.ApartmentValidationException;
import com.example.demo.dao.ApartmentJpaDOA;
import com.example.demo.dto.ApartmentDto;
import com.example.demo.entity.Apartment;
import com.example.demo.enums.Status;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.extern.apachecommons.CommonsLog;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;
@Component
@CommonsLog
public class ApartmentValidation {
    @Autowired
    private ApartmentJpaDOA apartmentDOA;
    private final ModelMapper modelMapper = new ModelMapper();
    public void validateApartmentForSave(ApartmentDto apartmentDto) throws Exception {
        log.info("Validation for save model");
        validateApartmentDto(apartmentDto);
        String apartmentCode = apartmentDto.getApartmentCode();
        checkIfApartmentExists(apartmentCode);
    }
    public void validateApartmentDto(ApartmentDto apartmentDto) throws Exception {
        if(apartmentDto ==null ){
            throw new ApartmentValidationException("Invalid apartment");
        }
        String apartmentCode = apartmentDto.getApartmentCode();
        if (apartmentCode == null || apartmentCode.isEmpty()) {
            throw new ApartmentValidationException("Invalid or empty apartment code");
        }
        String locationApartment = apartmentDto.getLocationApartment();
        if (locationApartment == null || locationApartment.isEmpty()) {
            throw new ApartmentValidationException("Invalid or empty  location Apartment");
        }
    }
    public void checkIfApartmentExists(String apartmentCode) throws ApartmentValidationException {
        if (apartmentDOA.findByApartmentCode(apartmentCode).isPresent()) {
            throw new ApartmentValidationException("An apartment with this code already exists");
        }
    }
    public Apartment mapDtoToEntity(ApartmentDto apartmentDto) {
        return modelMapper.map(apartmentDto, Apartment.class);
    }
    public void setApartmentDefaults(Apartment apartment) {
        apartment.setStatus(String.valueOf(Status.ACT));
    }
    public Apartment validateApartmentCode(String apartmentCode) throws ValidationException {
        if (apartmentCode == null || apartmentCode.trim().isEmpty()) {
            throw new ValidationException("Invalid or empty apartment code");
        }
        Optional<Apartment> optionalApartment = apartmentDOA.findByApartmentCodeAndStatus(apartmentCode, String.valueOf(Status.ACT));
        Apartment apartment;
        if (optionalApartment.isPresent()) {
            apartment = optionalApartment.get();
            return apartment;
        } else {
            throw new ValidationException("Apartment with code : > " + apartmentCode + " < : not found");
        }
    }
    public void  delete(String apartmentCode) throws Exception {
        if (apartmentCode == null) {
            throw new Exception("Invalid apartment code");
        }
        Apartment existingApartment = apartmentDOA.findByApartmentCode(apartmentCode)
                .orElseThrow(() -> new ApartmentValidationException("not found "));
        if (existingApartment.getStatus().equals(String.valueOf(Status.SUSPEND))) {
            throw new ApartmentValidationException("apartment is already delete: " + apartmentCode);
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
                    double totalCost = apartment.getPurchaseApartment() + newExpenses;
                    apartment.setTotalCost(totalCost);
                    apartmentDOA.save(apartment);
                }
            log.info("Successfully updated expenses for Apartment: " + apartmentCode);
        } catch (Exception e) {
            throw new ApartmentValidationException("An error occurred while updating expenses: " + e.getMessage());
        }
    }
    public void performCalculationsAndSave(String apartmentCode, double newPurchaseApartment, double newAmountApartmentSale) throws ApartmentValidationException {
        try {
            log.info("Calculations and save");
            Optional<Apartment> optionalApartment = apartmentDOA.findByApartmentCode(apartmentCode);
            Apartment existingApartment = optionalApartment.orElseThrow(() -> new ApartmentValidationException("Apartment not found for code: " + apartmentCode));
            double purchaseAmount = (newPurchaseApartment != 0) ? newPurchaseApartment :(existingApartment.getPurchaseApartment());
            existingApartment.setPurchaseApartment(purchaseAmount);
            double expenses = existingApartment.getExpenses();
            existingApartment.setAmountApartmentSale(newAmountApartmentSale);
            if (purchaseAmount != 0 && expenses != 0) {
                double totalCost = purchaseAmount + expenses;
                existingApartment.setTotalCost(totalCost);
                if (newAmountApartmentSale != 0) {
                    double netOfApartment = newAmountApartmentSale - totalCost;
                    existingApartment.setNetOfApartment(netOfApartment);
                }
                apartmentDOA.save(existingApartment);
                log.info("Successfully performed calculations and updated values for Apartment: " + existingApartment.getApartmentCode());
            }
        } catch (Exception e) {
            throw new ApartmentValidationException("An error occurred while performing calculations and updating values: " + e.getMessage());
        }
    }
}
