package com.example.demo.dto;

import com.example.demo.entity.Apartment;
import com.example.demo.entity.Contributor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ContributorDto extends  BaseDTO{
    private String name;
    private String phoneNumber;
    private String address;
    private double amountInvested;
    private int apartment;
    private double shareholdersExpenses;
    private double shareholdersProfits;
    private double  endData;

    public static ContributorDto mapToContributorDto(Contributor contributor) {
        ContributorDto contributorDto = new ContributorDto();
        contributorDto.setName(contributor.getName());
        contributorDto.setAmountInvested(contributor.getAmountInvested());
        contributorDto.setShareholdersExpenses(contributor.getShareholdersExpenses() != null ? contributor.getShareholdersExpenses() : 0.0);
        contributorDto.setShareholdersProfits(contributor.getShareholdersProfits() != null ? contributor.getShareholdersProfits() : 0.0);
        contributorDto.setEndData(contributor.getEndData() != null ? contributor.getEndData() : 0.0);
        return contributorDto;
    }
    public static Contributor mapToContributor(ContributorDto contributorDto, Apartment apartment) {
        Contributor contributor = new Contributor();
        contributor.setName(contributorDto.getName());
        contributor.setAmountInvested(contributorDto.getAmountInvested());
        contributor.setShareholdersExpenses(contributorDto.getShareholdersExpenses());
        contributor.setShareholdersProfits(contributorDto.getShareholdersProfits());
        contributor.setApartment(apartment);
        return contributor;
    }
}
