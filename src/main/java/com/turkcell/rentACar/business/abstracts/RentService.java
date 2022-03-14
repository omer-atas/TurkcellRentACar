package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentListDto;
import com.turkcell.rentACar.business.request.rentRequests.CreateRentRequest;
import com.turkcell.rentACar.business.request.rentRequests.DeleteRentRequest;
import com.turkcell.rentACar.business.request.rentRequests.UpdateRentRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface RentService {

	Result carRentalForIndividualCustomer(CreateRentRequest createRentRequest) throws BusinessException;

	Result carRentalForCorporateCustomer(CreateRentRequest createRentRequest) throws BusinessException;

	DataResult<RentGetDto> getByRentId(int rentId);

	DataResult<List<RentListDto>> getAll();

	DataResult<List<RentListDto>> getAllPaged(int pageNo, int pageSize);

	DataResult<List<RentListDto>> getAllSorted(Sort.Direction direction);

	boolean checkIfRentalCarExists(int rentalId) throws BusinessException;

	boolean checkIfCarAvaliable(int carId) throws BusinessException;

	List<RentListDto> getByCar_CarId(int carId);

	Result update(int rentalId, UpdateRentRequest updateRentRequest) throws BusinessException;


	Result delete(DeleteRentRequest deleteRentalCarRequest) throws BusinessException;

}
