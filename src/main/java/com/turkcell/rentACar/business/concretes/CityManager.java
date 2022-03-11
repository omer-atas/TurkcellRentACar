package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CityService;
import com.turkcell.rentACar.business.dtos.cityDtos.CityGetDto;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.dataAccess.abstracts.CityDao;
import com.turkcell.rentACar.entities.concretes.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class CityManager implements  CityService{

    private CityDao cityDao;
    private ModelMapperService modelMapperService;

    @Lazy
    @Autowired
    public CityManager(CityDao cityDao, ModelMapperService modelMapperService) {
        this.cityDao = cityDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public CityGetDto getByCityPlate(int cityPlate) {

        City result = this.cityDao.getByCityPlate(cityPlate);

        if (result == null) {
            return null;
        }

        CityGetDto response = this.modelMapperService.forDto().map(result, CityGetDto.class);
        return response;
    }
}
