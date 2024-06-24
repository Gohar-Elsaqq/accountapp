package com.example.demo.controller;
import com.example.demo.configuration.BaseService;
import com.example.demo.configuration.Utility;
import com.example.demo.configurationController.BaseController;
import com.example.demo.dto.ApartmentDto;
import com.example.demo.dto.FinalProfitDTO;
import com.example.demo.services.ApartmentService;
import jakarta.validation.Valid;
import lombok.extern.apachecommons.CommonsLog;
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
            var response =new Utility(apartmentService.saveOrUpdateApartment(apartmentDto), BaseService.SUCCESS);
            log.debug("return add apartment"+apartmentDto.getApartmentCode());
            return success(response);
        } catch (Exception e) {
           return wrapException(e,e.getMessage());
        }
    }
    @GetMapping(value = "/apartment/getApartment")
    public ApartmentDto findApartment(@RequestParam("apartmentCode") String apartmentCode) throws Exception {
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
    public List<ApartmentDto> findALL() throws Exception {
      try {
          log.debug("start delete  apartment : >> -- ");
         return apartmentService.findAllApartments();
    }catch (Exception exception) {
      throw new Exception(exception.getMessage());
      }
      }
    @DeleteMapping(value="/apartment/delete/{apartmentCode}")
    public ResponseEntity<?>  deleteApartment(@PathVariable String apartmentCode) {
        try {
            log.debug("start delete  apartment : >> -- ");
            apartmentService.deleteApartment(apartmentCode);
            return success(new Utility("Apartment Delete", BaseService.SUCCESS));
        }catch (Exception exception){
            return wrapException(exception,exception.getMessage());
        }
    }
@PostMapping(value = "/apartment/finishProfit/{apartmentCode}")
public ResponseEntity<?> finalProfit(@PathVariable String apartmentCode,@RequestBody FinalProfitDTO finalProfitDTO) {
    log.info("Rest API to Final Profit for Apartment: " + apartmentCode);
    try {
        apartmentService.finalProfit(apartmentCode,finalProfitDTO);
        return success(new Utility("Final Profit", BaseService.SUCCESS));
    } catch (Exception exception) {
        return wrapException(exception, exception.getMessage());
    }
}

    @GetMapping(value = "/apartment/getApartmentName")
    public List<String> findApartmentName() throws Exception {
        try {
            log.debug("start delete  apartment : >> -- ");
            return apartmentService.getNameApartmentCode();
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

}