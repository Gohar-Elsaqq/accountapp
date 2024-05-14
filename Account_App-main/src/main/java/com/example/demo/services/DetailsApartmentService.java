package com.example.demo.services;

import com.example.demo.configuration.ApartmentValidationException;
import com.example.demo.configuration.BaseService;
import com.example.demo.configuration.Utility;
import com.example.demo.dao.ApartmentJpaDOA;
import com.example.demo.dao.DetailsApartmentDAO;
import com.example.demo.entity.ActApartmentsView;
import com.example.demo.entity.Apartment;
import com.example.demo.entity.DetailsApartment;
import com.example.demo.vaildation.ApartmentsTotalAmountCalculator;
import com.example.demo.vaildation.DetailsApartmentValidator;
import jakarta.validation.ValidationException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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
    private ApartmentJpaDOA apartmentDOA;

    public void save(DetailsApartment detailsApartment, String apartmentCode, String lookupType) throws Exception {
        try {
            log.info("enter with save");
            detailsApartmentValidator.validateInputs(apartmentCode, lookupType, detailsApartment);
            detailsApartmentDAO.save(detailsApartment);
            apartmentsTotalAmountCalculator.sumAmount(apartmentCode);
            Utility.getGson().toJson(new Utility("Details Apartment", SUCCESS));
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }
    public List<ActApartmentsView> getDetails(String apartmentCode) throws ValidationException {
        try {
            return detailsApartmentValidator.viewList(apartmentCode);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public void delete(int id) throws Exception {
        try {
            log.info("Start delete Apartment");
            Optional<DetailsApartment> optionalApartment = detailsApartmentDAO.findById(id);

            if (optionalApartment.isPresent()) {
                DetailsApartment apartmentDetails = optionalApartment.get();
                Apartment apartment = apartmentDetails.getApartment();
                if (apartment != null) {
                    double amount = apartmentDetails.getAmount();
                    double newExpenses =apartment.getExpenses() - amount;
                    apartment.setExpenses(newExpenses);
                    apartmentDOA.save(apartment);
                } else {
                    log.warn("Apartment is null for DetailsApartment with id " + id);
                }
            } else {
                log.warn("DetailsApartment with id " + id + " not found");
            }
            detailsApartmentValidator.delete(id);

        } catch (Exception e) {
            throw new Exception("An error occurred while deleting the apartment: " + e.getMessage());
        }
    }

    }