package com.example.demo.controller;
import com.example.demo.configuration.BaseService;
import com.example.demo.configuration.Utility;
import com.example.demo.configurationController.BaseController;
import com.example.demo.dto.ApartmentDto;
import com.example.demo.entity.Apartment;
import com.example.demo.services.ApartmentService;
import jakarta.validation.Valid;
import lombok.extern.apachecommons.CommonsLog;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CommonsLog
@RestController
public class ApartmentController extends BaseController {

    @Autowired
    private ApartmentService apartmentService;
    @PostMapping(value = "/apartment/save")
    public ResponseEntity<?> add(@RequestBody @Valid ApartmentDto apartmentDto) {
        try {
            log.debug("start api save new apartment "+apartmentDto.getApartmentCode());
            var response =new Utility(apartmentService.save(apartmentDto), BaseService.SUCCESS);
            log.debug("return add apartment"+apartmentDto.getApartmentCode());
            return success(response);
        } catch (Exception e) {
           return wrapException(e,e.getMessage());
        }
    }

    @PutMapping(value = "/apartment/upDataApartment")
    public ResponseEntity<?> updateApartments(@RequestBody ApartmentDto apartmentDto) {
        log.info("Rest api to updateApartments ");
        try {
            log.debug("start updateApartments : > >"+ apartmentDto.getApartmentCode());
            var response =apartmentService.updateApartments(apartmentDto);
            return success(new Utility("apartment", BaseService.SUCCESS)+response);
        }catch (Exception exception) {
            return wrapException(exception,exception.getMessage());
        }
    }
    @GetMapping(value = "/apartment/getApartment")
    public Apartment findApartment(@RequestParam("apartmentCode") String apartmentCode) throws Exception {
        try {
            log.debug("start delete  apartment : >> -- ");
            var response= apartmentService.getApartment(apartmentCode);
            log.debug("return delete  apartment :"+response.getApartmentCode());
            return response;
        } catch (Exception exception) {
           throw new Exception(exception.getMessage());
        }
    }
    @GetMapping(value = "/apartment/allApartment")
    public List<Apartment> findALL() throws Exception {
      try {
          log.debug("start delete  apartment : >> -- ");
         return apartmentService.findAllApartment();
//        return success(new Utility("apartment find all", BaseService.SUCCESS));
    }catch (Exception exception) {
      throw new Exception(exception.getMessage());
      }
      }
    @DeleteMapping(value="/apartment/delete/{apartmentCode}")
    public ResponseEntity<?>  deleteApartment(@PathVariable String apartmentCode) throws Exception {
        try {
            log.debug("start delete  apartment : >> -- ");
            apartmentService.deleteApartment(apartmentCode);
            return success(new Utility("Apartment Delete", BaseService.SUCCESS));
        }catch (Exception exception){
            return wrapException(exception,exception.getMessage());
        }
    }
@PostMapping(value = "/apartment/finishProfit/{apartmentCode}")
public ResponseEntity<?> finalProfit(@PathVariable String apartmentCode,@RequestParam ("purchaseApartment") Double purchaseApartment, @RequestParam("amountApartmentSale") Double amountApartmentSale) {
    log.info("Rest API to Final Profit for Apartment: " + apartmentCode);
    try {
        apartmentService.finalProfit(apartmentCode, purchaseApartment,amountApartmentSale);
        return success(new Utility("Final Profit", BaseService.SUCCESS));
    } catch (Exception exception) {
        return wrapException(exception, exception.getMessage());
    }
}


}