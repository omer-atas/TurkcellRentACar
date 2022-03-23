package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import com.turkcell.rentACar.api.modals.RentPostServiceModal;
import org.springframework.data.domain.Sort;

import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentListDto;
import com.turkcell.rentACar.business.request.rentRequests.DeleteRentRequest;
import com.turkcell.rentACar.business.request.rentRequests.UpdateRentRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface RentService {

	Result carRentalForIndividualCustomer(RentPostServiceModal rentPostServiceModal) throws BusinessException;

	Result carRentalForCorporateCustomer(RentPostServiceModal rentPostServiceModal) throws BusinessException;

	DataResult<RentGetDto> getByRentId(int rentId);

	DataResult<List<RentListDto>> getAll();

	DataResult<List<RentListDto>> getAllForCorporateCustomer();

	DataResult<List<RentListDto>> getAllForIndividualCustomer();

	DataResult<List<RentListDto>> getAllPaged(int pageNo, int pageSize);

	DataResult<List<RentListDto>> getAllSorted(Sort.Direction direction);

	boolean checkIfRentExists(int rentalId) throws BusinessException;

	List<RentListDto> getByCar_CarId(int carId);

	Result update(int rentId, UpdateRentRequest updateRentRequest) throws BusinessException;

	Result delete(DeleteRentRequest deleteRentalCarRequest) throws BusinessException;

}
