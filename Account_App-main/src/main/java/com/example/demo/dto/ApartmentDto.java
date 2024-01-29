package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Getter
@Setter
public class ApartmentDto {

    private String apartmentCode;

    private String locationApartment;
    private String newApartmentCode;

//    private Date purchaseDate;
    private String creationTime;

    private double purchaseApartment;


    private double expenses;


    private double amountApartmentSale;

    private double netOfApartment;
    private Date saleDate;

    private String comments;

    private String status;
    List<DetailsApartmentDto> detailsApartmentDto;

}
