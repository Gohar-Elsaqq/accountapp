package com.example.demo.vaildation;

import com.example.demo.configuration.ApartmentValidationException;
import com.example.demo.dao.SystemTypeDOA;
import com.example.demo.dto.SystemTypeDto;
import com.example.demo.entity.Apartment;
import com.example.demo.entity.SystemType;
import com.example.demo.enums.Status;
import jakarta.validation.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Component
public class SystemTypeValidation {
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private SystemTypeDOA systemTypeDOA;

    //get System Type(lookupType)
    public SystemType validateLookupType(String lookupType) throws ValidationException, ApartmentValidationException {
        if (lookupType == null || lookupType.trim().isEmpty()) {
            throw new ValidationException("Invalid or empty lookup type");
        }
        Optional<SystemType> optionalSystemType = systemTypeDOA.findByLookupTypeAndStatus(lookupType, String.valueOf(Status.ACT));
        SystemType systemType=null;
        if (optionalSystemType.isPresent()) {
             systemType=optionalSystemType.get();
             return systemType;
        }
        throw new ApartmentValidationException("SystemType with lookup type : > " + lookupType + " < : not found");
    }

    //find all SystemType
    public List<SystemType> findAll() {
        try {
            return systemTypeDOA.findAll();
        } catch (ValidationException exception) {
            throw new ValidationException("An error was found in returning data. Try again");
        }
    }

    //delete SystemType(lookupType)
    public void delete(String lookupType) throws Exception {
        try {
            if (lookupType == null) {
                throw new Exception("Invalid apartment code");
            }
            SystemType systemType = systemTypeDOA.findByLookupType(lookupType)
                    .orElseThrow(() -> new ApartmentValidationException("not found "));
            if (systemType != null && Status.SUSPEND.name().equals(systemType.getStatus())) {
                throw new ApartmentValidationException("Apartment is already deleted: " + lookupType);
            }
            assert systemType != null;
            systemType.setStatus(String.valueOf(Status.SUSPEND));
            systemTypeDOA.save(systemType);
        } catch (ValidationException exception) {
            throw new Exception("This type does not exist in the system");
        }
    }

    public void save(SystemTypeDto systemTypeDto) throws Exception {
        try {
            checkIfSystemTypeExists(systemTypeDto.getLookupType());
            SystemType systemType = mapDtoToEntity(systemTypeDto);
            setSystemTypeDefaults(systemType);
            systemTypeDOA.save(systemType);
        } catch (ValidationException exception) {
            throw new Exception(exception.getMessage());
        }
    }

    public SystemType mapDtoToEntity(SystemTypeDto systemTypeDto) {
        return modelMapper.map(systemTypeDto, SystemType.class);
    }

    public void checkIfSystemTypeExists(String lookupType) throws ApartmentValidationException {
        if (systemTypeDOA.findByLookupType(lookupType).isPresent()) {
            throw new ApartmentValidationException("An lookupType with this code already exists");
        }
    }

    public void setSystemTypeDefaults(SystemType systemType) {
        try {
            systemType.setStatus(String.valueOf(Status.ACT));
        } catch (Exception e) {
            throw new ValidationException("don't set Status ");
        }
    }
}
