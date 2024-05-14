package com.example.demo.dto;

import com.example.demo.entity.Apartment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@Data
public class DetailsApartmentDto {
    private Double amount;
    private String comments;
    private String establishing;
    private String finishing;
    private SystemTypeDto systemTypeDto;
    private Apartment apartment;
}
