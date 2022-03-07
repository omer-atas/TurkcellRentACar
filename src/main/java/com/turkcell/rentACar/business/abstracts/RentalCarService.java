package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarGetDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarListDto;
import com.turkcell.rentACar.business.request.rentalCarRequests.CreateRentalCarRequest;
import com.turkcell.rentACar.business.request.rentalCarRequests.DeleteRentalCarRequest;
import com.turkcell.rentACar.business.request.rentalCarRequests.UpdateRentalCarRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface RentalCarService {

	Result add(CreateRentalCarRequest createRentalCarRequest) throws BusinessException;

	DataResult<RentalCarGetDto> getByRentalId(int rentalId);

	DataResult<List<RentalCarListDto>> getAll();

	DataResult<List<RentalCarListDto>> getAllPaged(int pageNo, int pageSize);

	DataResult<List<RentalCarListDto>> getAllSorted(Sort.Direction direction);

	boolean checkIfRentalCar(int rentalId) throws BusinessException;

	boolean checkIfCarAvaliable(int carId) throws BusinessException;

	List<RentalCarListDto> getByCar_CarId(int carId);

	Result update(int rentalId, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException;

	Result delete(DeleteRentalCarRequest deleteRentalCarRequest) throws BusinessException;

}
