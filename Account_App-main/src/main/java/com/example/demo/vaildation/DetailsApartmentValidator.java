package com.example.demo.vaildation;

import com.example.demo.dao.ActApartmentsViewDOA;
import com.example.demo.dao.DetailsApartmentDAO;
import com.example.demo.entity.ActApartmentsView;
import com.example.demo.entity.Apartment;
import com.example.demo.entity.DetailsApartment;
import com.example.demo.entity.SystemType;
import com.example.demo.services.ApartmentService;
import com.example.demo.services.SystemTypeService;
import jakarta.validation.ValidationException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@CommonsLog
public class DetailsApartmentValidator {
    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private SystemTypeService systemTypeService;
    @Autowired
    private ActApartmentsViewDOA actApartmentsViewDOA;
    @Autowired
    private ApartmentValidation apartmentValidation;
    @Autowired
    private DetailsApartmentDAO detailsApartmentDAO;

    public void validateInputs(String apartmentCode, String lookupType, DetailsApartment detailsApartment) throws Exception {
        Apartment apartment = apartmentService.getApartment(apartmentCode);
        detailsApartment.setApartment(apartment);
        validateNullOrEmpty(apartmentCode, "Apartment code must not be null or empty");
        SystemType type = systemTypeService.searchLookupType(lookupType);
        detailsApartment.setSystemType(type);
        validateNullOrEmpty(lookupType, "Lookup type must not be null or empty");
        validateBooleanFields(detailsApartment);
    }

    private void validateNullOrEmpty(String value, String errorMessage) throws IllegalArgumentException {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateBooleanFields(DetailsApartment detailsApartment) throws Exception {
        boolean atLeastOneTrue = detailsApartment.getEstablishing() || detailsApartment.getFinishing();
        boolean bothEstablishingAndFinishing = detailsApartment.getEstablishing() && detailsApartment.getFinishing();

        if (!atLeastOneTrue) {
            throw new Exception("At least one boolean field must be true");
        } else if (bothEstablishingAndFinishing) {
            throw new Exception("Please select only one of Establishing or Finishing");
        }
    }




    public List<ActApartmentsView> viewList(String apartmentCode) throws ValidationException {
        try {
            if (apartmentCode == null) {
                throw new ValidationException("Apartment code must not be null or empty");
            }
            apartmentValidation.validateApartmentCode(apartmentCode);
            return actApartmentsViewDOA.findByApartmentCode(apartmentCode);
        } catch (ValidationException e) {
            throw new ValidationException("This code does not exist in the system");
        }
    }
    public void delete(int id) throws Exception {
        try {
            Optional<DetailsApartment> optionalDetailsApartment = detailsApartmentDAO.findById(id);

            if (optionalDetailsApartment.isPresent()) {
                detailsApartmentDAO.deleteById(id);
            } else {
                throw new Exception("Record with Details Apartment " + id + " not found.");
            }
        } catch (Exception e) {
            throw new Exception("Details Apartment not found");
        }
    }
}