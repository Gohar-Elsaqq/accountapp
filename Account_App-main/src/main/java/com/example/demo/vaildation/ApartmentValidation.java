package com.example.demo.vaildation;

import com.example.demo.configuration.ApartmentValidationException;
import com.example.demo.dao.ApartmentJpaDOA;
import com.example.demo.dao.DetailsApartmentDAO;
import com.example.demo.dto.ApartmentDto;
import com.example.demo.entity.Apartment;
import com.example.demo.enums.Status;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.extern.apachecommons.CommonsLog;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Component
@CommonsLog
public class ApartmentValidation {
    @Autowired
    private ApartmentJpaDOA apartmentDOA;
    @Autowired
    private DetailsApartmentDAO detailsApartmentRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    public void validateApartmentForSave(ApartmentDto apartmentDto) throws Exception {
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

        double purchaseApartment = apartmentDto.getPurchaseApartment();
        if (purchaseApartment <= 0) {
            throw new ApartmentValidationException("Invalid or empty purchase Apartment");
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
    /**
     * Sets default values for the given {@link Apartment} object.
     * This method initializes the purchase date to the current date,
     * sets the status to "ACT" (Active), and sets another property
     * (e.g., propertyName) to a specific default value.
     *
     * @param apartment The {@link Apartment} object for which default values will be set.
     */
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
    public void updateExpensesForApartment(String apartmentCode, Double newExpenses) throws ApartmentValidationException {
        try {
            // find by id Apartment
            Optional<Apartment> optionalApartment = apartmentDOA.findByApartmentCode(apartmentCode);
            if (optionalApartment.isPresent()) {
                Apartment apartment = optionalApartment.get();
                apartment.setExpenses(newExpenses);
                double totalCost = apartment.getPurchaseApartment() + newExpenses;
                apartment.setTotalcost(totalCost);
                apartmentDOA.save(apartment);
                log.info("Successfully updated expenses for Apartment: " + apartmentCode);
            } else {
                throw new ApartmentValidationException("Apartment not found for code: " + apartmentCode);
            }
        } catch (Exception e) {
            throw new ApartmentValidationException("An error occurred while updating expenses: " + e.getMessage());
        }
    }
    public void performCalculationsAndSave(String apartmentCode, Double newPurchaseApartment, Double newAmountApartmentSale) throws ApartmentValidationException {
        try {
            Optional<Apartment> optionalApartment = apartmentDOA.findByApartmentCode(apartmentCode);
            Apartment existingApartment = optionalApartment
                    .orElseThrow(() -> new ApartmentValidationException("Apartment not found for code: " + apartmentCode));

            // This sentence means "Updating purchaseAmount based on the value sent or the stored value.
            double purchaseAmount = (newPurchaseApartment != null) ? newPurchaseApartment : existingApartment.getPurchaseApartment();
            //update apratment
            existingApartment.setPurchaseApartment(purchaseAmount);
            Double expenses = existingApartment.getExpenses();
            existingApartment.setTotalcost(null);
            existingApartment.setAmountApartmentSale(newAmountApartmentSale);
            existingApartment.setNetOfApartment(null);
            if (existingApartment.getPurchaseApartment() != null && existingApartment.getExpenses() != null) {
                double totalCost = purchaseAmount + expenses;
                existingApartment.setTotalcost(totalCost);
                if (newAmountApartmentSale != null) {
                    double netOfApartment = newAmountApartmentSale - totalCost;
                    existingApartment.setNetOfApartment(netOfApartment);
                }
                apartmentDOA.save(existingApartment);
                log.info("Successfully performed calculations and updated values for Apartment: " + existingApartment.getApartmentCode());
            } else {
                throw new ApartmentValidationException("One or more required values are null for Apartment: " + apartmentCode);
            }
        } catch (Exception e) {
            throw new ApartmentValidationException("An error occurred while performing calculations and updating values: " + e.getMessage());
        }
    }


}
