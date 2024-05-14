package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name="apartment")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Apartment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //كود الشقه
    @Column(name = "apartment_code", unique = true)
    @NotBlank(message = "Please enter a valid Apartment code, it must not be blank")
    @NotNull(message = "Please enter a valid Apartment code, it must not be null")
    @Size(min = 1, message = "Please enter a valid Apartment code, it must have at least 1 character")
    private String apartmentCode;

//    @Column(name = "newApartmentCode")
//    private String newApartmentCode;

    //مكان الشقه
    @Column(name = "locationApartment")
    private String locationApartment;

    //تاريخ البيع
    @Column(name = "saleDate")
    private LocalDateTime saleDate;
    //الشراء
    @Column(name = "purchase")
    private double purchaseApartment;

    //مصاريف الشقه
    @Column(name = "expenses")
    private double expenses;

    @Column(name = "totalCost")
    private double  totalCost;

    //بيع الشقه
    @Column(name = "amountApartmentSale")
    private double amountApartmentSale;

    //صافى الشقه
    @Column(name = "netOfApartment")
    private double netOfApartment;

    //ملاحظات
    @Column(name = "comments")
    private String comments;
    @Column(name = "status")
    private String status;

    @JsonIgnore
//    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY)
    @OneToMany(mappedBy = "apartment", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<DetailsApartment> detailsApartments;

}