package com.turkcell.rentACar.business.abstracts;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.turkcell.rentACar.business.dtos.CarMaintenanceGetDto;
import com.turkcell.rentACar.business.dtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.request.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.DeleteCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

public interface CarMaintenanceService {

	Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException;

	DataResult<List<CarMaintenanceListDto>> getByCarMaintenanceCarId(int carId) throws BusinessException;
	
	DataResult<CarMaintenanceGetDto> getByCarMaintenanceId(int carMaintanenceId) throws BusinessException;

	DataResult<List<CarMaintenanceListDto>> getAll() throws BusinessException;

	DataResult<List<CarMaintenanceListDto>> getAllPaged(int pageNo, int pageSize) throws BusinessException;

	DataResult<List<CarMaintenanceListDto>> getAllSorted(Sort.Direction direction) throws BusinessException;

	Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException;

	Result delete(DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) throws BusinessException;

}
