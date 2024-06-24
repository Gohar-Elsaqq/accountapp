package com.example.demo.dto;

import com.example.demo.entity.Apartment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Component
public class ApartmentDto {

    private int id;
    private String apartmentCode;
    private String apartmentCodeNew;
    private String locationApartment;
    private String purchaseDate;
    private String creationTime;
    private double purchaseApartment;
    private double expenses;
    private double amountApartmentSale;
    private double netOfApartment;
    private String saleDate;
    private String comments;
    private String status;
    private double  totalCost;
    private List<ContributorDto> contributors;

    public static ApartmentDto mapToApartmentDto(Apartment apartment) {
        ApartmentDto apartmentDto = new ApartmentDto();
        // Formatting === >>>>> date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        apartmentDto.setApartmentCode(apartment.getApartmentCode());
        apartmentDto.setId(apartment.getId());
        apartmentDto.setLocationApartment(apartment.getLocationApartment() != null ? apartment.getLocationApartment() : "");
        apartmentDto.setSaleDate(apartment.getSaleDate() != null ? apartment.getSaleDate().format(formatter) : "");
        apartmentDto.setCreationTime(apartment.getCreationTime() != null ? apartment.getCreationTime().format(formatter) : "");
        apartmentDto.setPurchaseApartment(apartment.getPurchaseApartment());
        apartmentDto.setExpenses(apartment.getExpenses());
        apartmentDto.setAmountApartmentSale(apartment.getAmountApartmentSale());
        apartmentDto.setNetOfApartment(apartment.getNetOfApartment());
        apartmentDto.setComments(apartment.getComments() != null ? apartment.getComments() : "");
        apartmentDto.setStatus(apartment.getStatus() != null ? apartment.getStatus() : "");
        apartmentDto.setTotalCost(apartment.getTotalCost());
        List<ContributorDto> contributorsDto = apartment.getContributor().stream()
                .map(ContributorDto::mapToContributorDto)
                .collect(Collectors.toList());
        apartmentDto.setContributors(contributorsDto);
        return apartmentDto;
    }
}
