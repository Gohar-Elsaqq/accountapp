package com.example.demo.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;
import jakarta.persistence.*;


@Entity
@Table(name="systemType")
@Data
public class SystemType extends  BaseEntity{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Please enter a valid lookup Type, it must not be blank")
    @NotNull(message = "Please enter a valid lookup Type, it must not be null")
    @Size(min = 1, message = "Please enter a valid lookup Type, it must have at least 1 character")
    @Column(name = "lookupType",unique = true)
    private String lookupType;

    @Column(name = "status")
    private String status;
    @JsonIgnore
    @OneToMany(mappedBy = "systemType",fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DetailsApartment> detailsApartment;


}
