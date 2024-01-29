package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@Data
public class DetailsApartmentDto {
    private String amount;
//    private Date date;
    private String comments;
    private String establishing;
    private String finishing;
    private SystemTypeDto systemTypeDto;
}
