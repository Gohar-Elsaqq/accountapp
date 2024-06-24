package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name="apartment")

public class Apartment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "apartment_code", unique = true)
    @NotBlank(message = "Please enter a valid Apartment code, it must not be blank")
    @Size(min = 1, message = "Please enter a valid Apartment code, it must have at least 1 character")
    private String apartmentCode;
    @Column(name = "locationApartment")
    private String locationApartment;
    @Column(name = "saleDate")
    private LocalDateTime saleDate;
    @Column(name = "purchase")
    private double purchaseApartment;
    @Column(name = "expenses")
    private double expenses;
    @Column(name = "totalCost")
    private double  totalCost;
    @Column(name = "amountApartmentSale")
    private double amountApartmentSale;
    @Column(name = "netOfApartment")
    private double netOfApartment;
    @Column(name = "comments")
    private String comments;
    @Column(name = "status")
    private String status;
    @JsonIgnore
    @OneToMany(mappedBy = "apartment", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<DetailsApartment> detailsApartments;

    @JsonIgnore
    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contributor> contributor;

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", apartmentCode='" + apartmentCode + '\'' +
                ", locationApartment='" + locationApartment + '\'' +
                ", saleDate=" + saleDate +
                ", purchaseApartment=" + purchaseApartment +
                ", expenses=" + expenses +
                ", totalCost=" + totalCost +
                ", amountApartmentSale=" + amountApartmentSale +
                ", netOfApartment=" + netOfApartment +
                ", comments='" + comments + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}