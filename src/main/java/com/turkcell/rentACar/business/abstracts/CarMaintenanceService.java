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

	DataResult<List<CarMaintenanceListDto>> getByCarMaintenanceCarId(int carId);

	DataResult<CarMaintenanceGetDto> getByCarMaintenanceId(int carMaintanenceId);

	DataResult<List<CarMaintenanceListDto>> getAll();

	DataResult<List<CarMaintenanceListDto>> getAllPaged(int pageNo, int pageSize);

	DataResult<List<CarMaintenanceListDto>> getAllSorted(Sort.Direction direction);

	Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException;

	Result delete(DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) throws BusinessException;

	boolean checkIfCarMaintenance(int carMaintenanceId) throws BusinessException;

}
