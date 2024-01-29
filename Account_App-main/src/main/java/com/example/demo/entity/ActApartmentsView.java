package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "act_apartments_views")
@Immutable
public class ActApartmentsView {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;
    private String apartmentCode;
    private String detailsAmount;
    private String detailsDate;
    private String detailsComments;
    private String systemTypeLookupType;
    private String establishing;
    private String finishing;
}
