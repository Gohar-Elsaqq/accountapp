package com.example.demo.vaildation;

import com.example.demo.dao.ApartmentDOA;
import com.example.demo.dao.DetailsApartmentDAO;
import com.example.demo.entity.Apartment;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@CommonsLog
public class ApartmentsTotalAmountCalculator {
    @Autowired
    private ApartmentDOA apartmentJpaDOA;
    @Autowired
    private DetailsApartmentDAO detailsApartmentDAO;
    @Autowired
    private ApartmentValidation apartmentValidation;


    public void sumAmount(String apartmentCode ) throws Exception {
        try {
            if (apartmentCode != null && !apartmentCode.isEmpty()) {
                Optional<Apartment> apartmentOptional = apartmentJpaDOA.findByApartmentCode(apartmentCode);

                if (apartmentOptional.isPresent()) {
                    Apartment apartment = apartmentOptional.get();
                    int apartmentId = apartment.getId();

                    double newExpenses = getLastTotalAmount(apartmentId);
                    apartmentValidation.updateExpensesForApartment(apartmentCode, newExpenses);
                    log.info("Successfully updated expenses for Apartment: " + apartmentCode);
                } else {
                    throw new IllegalArgumentException("Apartment with code " + apartmentCode + " not found.");
                }
            }

        } catch (Exception e) {
            throw new Exception("Error in sumAmount", e);
        }
    }



    public double getLastTotalAmount(int apartmentId) {
        return detailsApartmentDAO.getLastTotalAmount(apartmentId);
    }
}


