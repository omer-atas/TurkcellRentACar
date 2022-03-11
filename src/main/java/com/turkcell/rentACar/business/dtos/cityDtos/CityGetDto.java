package com.turkcell.rentACar.business.dtos.cityDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityGetDto {

    private int cityPlate;

    private String cityName;
}
