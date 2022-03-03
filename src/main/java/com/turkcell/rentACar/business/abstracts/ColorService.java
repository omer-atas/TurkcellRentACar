package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.ColorGetDto;


import com.turkcell.rentACar.business.dtos.ColorListDto;

import com.turkcell.rentACar.business.request.CreateColorRequest;
import com.turkcell.rentACar.business.request.DeleteColorRequest;
import com.turkcell.rentACar.business.request.UpdateColorRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface ColorService {

	Result add(CreateColorRequest createColorRequest) throws BusinessException;

	DataResult<List<ColorListDto>> getAll() throws BusinessException;

	DataResult<ColorGetDto> getByColorId(int colorId) throws BusinessException;
	
	Result update(UpdateColorRequest updateColorRequest) throws BusinessException;

	Result delete(DeleteColorRequest deleteColorRequest) throws BusinessException;
}
