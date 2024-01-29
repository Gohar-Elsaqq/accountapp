package com.example.demo.services;


import com.example.demo.configuration.BaseService;
import com.example.demo.dto.SystemTypeDto;
import com.example.demo.entity.SystemType;
import com.example.demo.vaildation.SystemTypeValidation;
import jakarta.validation.ValidationException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@CommonsLog
@Service
public class SystemTypeService extends BaseService {

    @Autowired
    private SystemTypeValidation systemTypeValidation;

    public SystemType searchLookupType(String lookupType) throws Exception {
        log.info("Start search lookupType");
        try {
            return systemTypeValidation.validateLookupType(lookupType);
        } catch (ValidationException e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<SystemType> getAllLookupType(){
        try {
            log.info("start getAllLookupType : > " );
        return systemTypeValidation.findAll();
    }catch (ValidationException exception){
            throw new ValidationException(exception.getMessage());
        }

        }

    public void deleteSystemType(String lookupType) throws Exception {
        try {
            log.info("start deleteSystemType : > " + lookupType);
            systemTypeValidation.delete(lookupType);
            log.info("end deleteSystemType : > " + lookupType);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }
    public void save(SystemTypeDto systemTypeDto) throws Exception {
        try {
            log.info("Start save >>>>: create systemType >>>>");
            systemTypeValidation.save(systemTypeDto);
            log.info("this is save systemType");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
