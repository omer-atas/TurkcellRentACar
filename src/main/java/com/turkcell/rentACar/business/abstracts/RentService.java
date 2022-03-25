package com.turkcell.rentACar.business.abstracts;

import java.time.LocalDate;
import java.util.List;

import com.turkcell.rentACar.api.modals.RentEndDateDelayPostServiceModal;
import com.turkcell.rentACar.business.request.rentRequests.CreateRentRequest;
import org.springframework.data.domain.Sort;

import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentListDto;
import com.turkcell.rentACar.business.request.rentRequests.DeleteRentRequest;
import com.turkcell.rentACar.business.request.rentRequests.UpdateRentRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface RentService {

	int carRentalForIndividualCustomer(CreateRentRequest createRentRequest) throws BusinessException;

	int carRentalForCorporateCustomer(CreateRentRequest createRentRequest) throws BusinessException;

	DataResult<RentGetDto> getByRentId(int rentId);

	DataResult<List<RentListDto>> getAll();

	DataResult<List<RentListDto>> getAllForCorporateCustomer();

	DataResult<List<RentListDto>> getAllForIndividualCustomer();

	DataResult<List<RentListDto>> getAllPaged(int pageNo, int pageSize);

	DataResult<List<RentListDto>> getAllSorted(Sort.Direction direction);

	boolean checkIfRentExists(int rentalId) throws BusinessException;

	void checkIfIndividualCustomerExists(int customerId) throws BusinessException;

	void checkIfCustomerExists(int customerId) throws BusinessException;

	void checkIfCorporateCustomerExists(int customerId) throws BusinessException;

	Result updateRentDelayEndDateForIndividualCustomer(int rentId, RentEndDateDelayPostServiceModal rentEndDateDelayPostServiceModal) throws BusinessException;

	Result updateRentDelayEndDateForCorporateCustomer(int rentId, RentEndDateDelayPostServiceModal rentEndDateDelayPostServiceModal) throws BusinessException;

	List<RentListDto> getByCar_CarId(int carId);

	Result update(int rentId, UpdateRentRequest updateRentRequest) throws BusinessException;

	Result delete(DeleteRentRequest deleteRentalCarRequest) throws BusinessException;

}
