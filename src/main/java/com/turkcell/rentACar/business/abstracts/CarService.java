package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.turkcell.rentACar.business.dtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.CarListDto;
import com.turkcell.rentACar.business.request.CreateCarRequest;
import com.turkcell.rentACar.business.request.DeleteCarRequest;
import com.turkcell.rentACar.business.request.UpdateCarRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface CarService {
	
	Result add(CreateCarRequest createCarRequest) throws BusinessException;

	DataResult<CarGetDto> getByCarId(int carId) throws BusinessException;

	DataResult<List<CarListDto>> getAll() throws BusinessException ;
	
	DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) throws BusinessException ;

	DataResult<List<CarListDto>> getAllSorted(Sort.Direction direction) throws BusinessException ;

	Result update(UpdateCarRequest updateCarRequest) throws BusinessException;

	Result delete(DeleteCarRequest deleteCarRequest) throws BusinessException;
	
	DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice) throws BusinessException;

}
