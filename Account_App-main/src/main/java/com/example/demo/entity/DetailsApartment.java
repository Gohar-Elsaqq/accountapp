package com.example.demo.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name="detailsApartment")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailsApartment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="amount")
    private Double amount;
//    @Column(name="date")
//    private Date date;
    @Column(name="totalcost")
    private Double totalcost;
    @Column(name="comments")
    private String comments;
    @Column(name="establishing")
    private boolean establishing;
    @Column(name="finishing")
    private boolean finishing;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "system_type_id")
    private SystemType systemType;


}
