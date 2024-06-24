package com.example.demo.controller;

import com.example.demo.configuration.BaseService;
import com.example.demo.configuration.Utility;
import com.example.demo.configurationController.BaseController;
import com.example.demo.dto.DetailsApartmentDto;
import com.example.demo.dto.DetailsApartmentDtoSql;
import com.example.demo.services.DetailsApartmentService;
import jakarta.validation.ValidationException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@CommonsLog
@RestController
public class DetailsApartmentController extends BaseController {
    @Autowired
    private DetailsApartmentService detailsApartmentService;
    @PostMapping(value = "/detailsApartment/save")
    public ResponseEntity<?> saveDetailsApartment(@RequestBody DetailsApartmentDto detailsApartmentDto){
        log.info("Start saveDetailsApartment add >: ");
        try {
            log.info("Please enter saveDetailsApartment " );
             detailsApartmentService.save(detailsApartmentDto);
            return success(new Utility("Details Apartment", BaseService.SUCCESS));
        } catch (Exception exception) {
            return wrapException(exception,exception.getMessage());
        }
    }
    @GetMapping(value = "/detailsApartment/searchApartment")
    public  List<DetailsApartmentDtoSql> searchOneApartment(@RequestParam("apartmentCode") String apartmentCode)throws Exception {
        try {
            List<DetailsApartmentDtoSql> apartmentsViews = detailsApartmentService.getDetails(apartmentCode);
            if (apartmentsViews.isEmpty()) {
               throw new Exception("No apartments found for the provided code.");
            }
            return apartmentsViews;
        } catch (ValidationException e) {
           throw new Exception(e.getMessage());
        }
    }
    @DeleteMapping(value="/detailsApartment/delete/{id}")
    public ResponseEntity<?>  delete(@PathVariable int id) throws Exception {
        try {
            log.info("start delete  details Apartment : >> -- ");
            detailsApartmentService.delete(id);
            return success(new Utility("details Apartment Delete", BaseService.SUCCESS));
        }catch (Exception exception){
            return wrapException(exception,exception.getMessage());
        }
    }
}

