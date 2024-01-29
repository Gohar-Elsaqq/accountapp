package com.example.demo.configuration;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;

public class ApartmentValidationException extends Exception {

    public ApartmentValidationException(String message) {
        super(message);
    }


}
