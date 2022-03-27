package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.cityDtos.CityGetDto;

public interface CityService {

    CityGetDto getByCityPlate(int cityPlate);
}
