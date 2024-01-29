package com.example.demo.services;

import com.example.demo.configuration.ApartmentValidationException;
import com.example.demo.configuration.BaseService;
import com.example.demo.configuration.Utility;
import com.example.demo.dao.DetailsApartmentDAO;
import com.example.demo.entity.ActApartmentsView;
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

    public void save(DetailsApartment detailsApartment, String apartmentCode, String lookupType) throws Exception {
        try {
            log.info("enter with save");
            detailsApartmentValidator.validateInputs(apartmentCode, lookupType, detailsApartment);
            apartmentsTotalAmountCalculator.sumAmount(detailsApartment);
            detailsApartmentDAO.save(detailsApartment);
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

    public void delete (int id) throws Exception {
        try {
            log.info("Start delete Apartment");
            detailsApartmentValidator.delete(id);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        }
    }
}