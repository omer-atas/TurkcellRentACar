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

	DataResult<CarGetDto> getByCarId(int carId);

	DataResult<List<CarListDto>> getAll() ;

	boolean checkIfBrand(int brandId) throws BusinessException;

	boolean checkIfColor(int colorId) throws BusinessException;

	DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize);

	DataResult<List<CarListDto>> getAllSorted(Sort.Direction direction);

	Result update(UpdateCarRequest updateCarRequest) throws BusinessException;

	Result delete(DeleteCarRequest deleteCarRequest) throws BusinessException;

	DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice);

	boolean checkIfIsThereCar(int carId) throws BusinessException;

}
