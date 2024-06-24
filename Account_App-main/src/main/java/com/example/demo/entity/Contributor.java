package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="contributor")
public class Contributor extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount_invested")
    private double amountInvested;

    @Column(name = "shareholders_expenses")
    private Double  shareholdersExpenses;

    @Column(name = "shareholders_profits")
    private Double  shareholdersProfits;

    @Column(name = "end_data")
    private Double  endData;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
}

