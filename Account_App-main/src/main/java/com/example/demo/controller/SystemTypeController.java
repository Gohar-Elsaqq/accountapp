package com.example.demo.controller;

import com.example.demo.configuration.ApartmentValidationException;
import com.example.demo.configuration.BaseService;
import com.example.demo.configuration.Utility;
import com.example.demo.configurationController.BaseController;
import com.example.demo.dto.SystemTypeDto;
import com.example.demo.entity.SystemType;
import com.example.demo.services.SystemTypeService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@CommonsLog
@RestController
public class SystemTypeController extends BaseController {
    @Autowired
    private SystemTypeService systemTypeService;
    @DeleteMapping(value="/systemType/delete/{lookupType}")
    public ResponseEntity<?> deleteSystemType(@PathVariable String lookupType) throws Exception {
        try {
            log.info("start delete  systemType : >> -- ");
            systemTypeService.deleteSystemType(lookupType);
            return success(new Utility("systemType Delete", BaseService.SUCCESS));
        }catch (Exception exception){
            return wrapException(exception,exception.getMessage());
        }
    }
    @PostMapping(value="/systemType/save")
    public ResponseEntity<?> save(@RequestBody SystemTypeDto apartmentDto) {
        log.info("Rest api to save new systemType ");
        try {
            systemTypeService.save(apartmentDto);
            return success(new Utility("systemType", BaseService.SUCCESS));
        } catch (Exception e) {
            return wrapException(e,e.getMessage());
        }
    }
    @GetMapping(value="/systemType/all")
    public List<SystemType> getAll() throws ApartmentValidationException {
        log.info("Rest api to save new systemType ");
        try {
           return systemTypeService.getAllLookupType();
        } catch (Exception e) {
           throw new ApartmentValidationException(e.getMessage());
        }
    }

}
