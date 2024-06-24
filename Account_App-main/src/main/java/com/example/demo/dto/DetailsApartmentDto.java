package com.example.demo.dto;

import com.example.demo.entity.Apartment;
import com.example.demo.entity.DetailsApartment;
import com.example.demo.entity.SystemType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DetailsApartmentDto extends BaseDTO{
    private Double amount;
    private String comments;
    private boolean establishing;
    private boolean finishing;
    private String lookupType;
    private String apartmentCode;
}
