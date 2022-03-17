package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.turkcell.rentACar.business.dtos.carDtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.carDtos.CarListDto;
import com.turkcell.rentACar.business.request.carRequests.CreateCarRequest;
import com.turkcell.rentACar.business.request.carRequests.DeleteCarRequest;
import com.turkcell.rentACar.business.request.carRequests.UpdateCarRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface CarService {

	Result add(CreateCarRequest createCarRequest) throws BusinessException;

	DataResult<CarGetDto> getByCarId(int carId);

	DataResult<List<CarListDto>> getAll();

	DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize);

	DataResult<List<CarListDto>> getAllSorted(Sort.Direction direction);

	Result update(int carId, UpdateCarRequest updateCarRequest) throws BusinessException;

	Result delete(DeleteCarRequest deleteCarRequest) throws BusinessException;

	DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice);

	boolean checkIfCarExists(int carId) throws BusinessException;

	void updateKilometerInformation(int carId,double kilometerInformation);

}
