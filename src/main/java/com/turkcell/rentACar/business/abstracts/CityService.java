package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.carDtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.cityDtos.CityGetDto;
import com.turkcell.rentACar.core.utilities.results.DataResult;

public interface CityService {

    CityGetDto getByCityPlate(int cityPlate);
}
