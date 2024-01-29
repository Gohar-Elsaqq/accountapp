package com.example.demo.controller;

import com.example.demo.configuration.BaseService;
import com.example.demo.configuration.Utility;
import com.example.demo.configurationController.BaseController;
import com.example.demo.entity.ActApartmentsView;
import com.example.demo.entity.DetailsApartment;
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
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> saveDetailsApartment(@RequestParam("apartmentCode") String apartmentCode,
                                                  @RequestBody DetailsApartment detailsApartment,
                                                  @RequestParam(value = "lookupType") String lookupType) throws Exception {
        log.info("Start saveDetailsApartment add >: ");
        try {
            log.info("Please enter saveDetailsApartment "+apartmentCode+"^__^"+lookupType);
             detailsApartmentService.save(detailsApartment,apartmentCode,lookupType);
            return success(new Utility("Details Apartment", BaseService.SUCCESS));
        } catch (Exception exception) {
            return wrapException(exception,exception.getMessage());
        }
    }
    @GetMapping(value = "/detailsApartment/searchApartment")
    public List<ActApartmentsView> searchOneApartment(@RequestParam("apartmentCode") String apartmentCode) throws ValidationException {
        try {
            return detailsApartmentService.getDetails(apartmentCode);
        }catch (ValidationException e) {
        throw new ValidationException(e.getMessage());
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

