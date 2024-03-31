package com.example.demo.vaildation;

import com.example.demo.configuration.ApartmentValidationException;
import com.example.demo.configuration.BaseService;
import com.example.demo.configuration.Utility;
import com.example.demo.dao.ApartmentJpaDOA;
import com.example.demo.dto.ApartmentDto;
import com.example.demo.entity.Apartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class ApartmentUpdater {
    @Autowired
    private ApartmentJpaDOA apartmentDOA;

    public void updateApartments(ApartmentDto apartmentDto) throws Exception {
        try {
            if (apartmentDto.getApartmentCode() == null || apartmentDto.getApartmentCode().isEmpty()) {
                throw new Exception("Invalid apartment code");
            }
            Optional<Apartment> optionalApartment = apartmentDOA.findByApartmentCode(apartmentDto.getApartmentCode());
            if (optionalApartment.isPresent()) {
                Apartment existingApartment = optionalApartment.get();
                existingApartment.setApartmentCode(apartmentDto.getApartmentCode());
                existingApartment.setComments(apartmentDto.getComments());
                existingApartment.setLocationApartment(apartmentDto.getLocationApartment());
                existingApartment.setPurchaseApartment(apartmentDto.getPurchaseApartment());
                existingApartment.setExpenses(apartmentDto.getExpenses());
                existingApartment.setAmountApartmentSale(apartmentDto.getAmountApartmentSale());
                apartmentDOA.save(existingApartment);
//                Utility.getGson().toJson(new Utility("apartment update", BaseService.SUCCESS));
            } else {
                throw new ApartmentValidationException("Invalid apartment code ");
            }
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }
}