package com.example.demo.vaildation;

import com.example.demo.dao.DetailsApartmentDAO;
import com.example.demo.entity.Apartment;
import com.example.demo.entity.DetailsApartment;
import com.example.demo.services.ApartmentService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
@CommonsLog
public class ApartmentsTotalAmountCalculator {
    @Autowired
    private DetailsApartmentDAO detailsApartmentDAO;
    @Autowired
    private ApartmentValidation apartmentValidation;

    public void sumAmount(DetailsApartment detailsApartment) throws Exception {
        try {
             Double amount = detailsApartment.getAmount();
            String apartmentCode = detailsApartment.getApartment().getApartmentCode();
            Double lastTotalAmount = getLastTotalAmount(apartmentCode);
            String currentTotalAmount = (lastTotalAmount != null && lastTotalAmount > 0.0)
                    ? String.valueOf(lastTotalAmount)
                    : "0000000000.00";
            String newTotalAmount = String.valueOf(Double.parseDouble(currentTotalAmount) + amount);
            detailsApartment.setTotalcost(Double.valueOf(newTotalAmount));
            apartmentValidation.updateExpensesForApartment(apartmentCode, Double.valueOf(newTotalAmount));
        } catch (NumberFormatException e) {
            throw new Exception("Error parsing 'amount' value. Please provide a valid numeric value.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error in sumAmount", e);
        }
    }
private Double getLastTotalAmount(String apartmentCode) {
    Double lastTotalAmount = detailsApartmentDAO.findMaxTotalCostByApartmentCode(apartmentCode);
        return (lastTotalAmount != null) ? (lastTotalAmount) :0000000000.00;
    }
}

