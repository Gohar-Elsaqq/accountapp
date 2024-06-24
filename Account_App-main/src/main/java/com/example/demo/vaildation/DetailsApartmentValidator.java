package com.example.demo.vaildation;

import com.example.demo.dto.DetailsApartmentDto;
import com.example.demo.entity.DetailsApartment;
import lombok.extern.apachecommons.CommonsLog;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
@Component
@CommonsLog
public class DetailsApartmentValidator {
    private final ModelMapper modelMapper = new ModelMapper();

    public void validateBooleanFields(DetailsApartmentDto detailsApartmentDto) throws Exception {
        boolean atLeastOneTrue = detailsApartmentDto.isEstablishing() || detailsApartmentDto.isFinishing();
        boolean bothEstablishingAndFinishing = detailsApartmentDto.isEstablishing() && detailsApartmentDto.isFinishing();
        if (!atLeastOneTrue) {
            throw new Exception("At least one boolean field must be true");
        } else if (bothEstablishingAndFinishing) {
            throw new Exception("Please select only one of Establishing or Finishing");
        }
    }
    public DetailsApartment mapDtoToEntity(DetailsApartmentDto detailsApartmentDto) {
        return modelMapper.map(detailsApartmentDto, DetailsApartment.class);
    }
}