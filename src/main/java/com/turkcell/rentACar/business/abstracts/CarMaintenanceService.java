package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceGetDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.request.carMaintenanceRequests.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.carMaintenanceRequests.DeleteCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.carMaintenanceRequests.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface CarMaintenanceService {

	Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException;

	DataResult<List<CarMaintenanceListDto>> getByCarMaintenanceCarId(int carId);

	DataResult<CarMaintenanceGetDto> getByCarMaintenanceId(int carMaintanenceId);

	DataResult<List<CarMaintenanceListDto>> getAll();

	DataResult<List<CarMaintenanceListDto>> getAllPaged(int pageNo, int pageSize);

	DataResult<List<CarMaintenanceListDto>> getAllSorted(Sort.Direction direction);

	Result update(int carMaintenanceId, UpdateCarMaintenanceRequest updateCarMaintenanceRequest)
			throws BusinessException;

	Result delete(DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) throws BusinessException;

	boolean checkIfCarMaintenanceExists(int carMaintenanceId) throws BusinessException;

	List<CarMaintenanceListDto> getAllCarMaintenanceByCarId(int carId);

	List<CarMaintenanceListDto> getByCar_CarId(int carId);

}
