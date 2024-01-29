package com.example.demo.configurationController;

import com.example.demo.configuration.Utility;
import com.example.demo.dto.BaseDTO;
import com.google.gson.Gson;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
@Configuration
public abstract class BaseController {
    public ResponseEntity<Boolean> success(Boolean results) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(results);
    }

    public ResponseEntity<String> success(String results) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(results);
    }
    public <D extends BaseDTO> ResponseEntity<BaseDTO> success(D results) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(results);
    }
    public ResponseEntity<String> wrapException(Exception e, String functionName) {
        if (e.getCause() instanceof JpaSystemException
                || e.getCause() instanceof IncorrectResultSizeDataAccessException
                || e.getCause() instanceof ConstraintViolationException) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new Utility("3", "General Error, try again later").toString());
        } else {
            Utility businessException = new Utility();
            String errorCode = businessException.getErrorCode();
            if (errorCode == null) {
                errorCode = "1";
            }
            Gson gson = new Gson();
            String json = gson.toJson(new Utility(errorCode, e.getMessage()));
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);
        }
    }



}
