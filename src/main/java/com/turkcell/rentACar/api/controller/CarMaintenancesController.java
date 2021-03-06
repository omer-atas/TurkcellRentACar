package com.turkcell.rentACar.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACar.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceGetDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.request.carMaintenanceRequests.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.carMaintenanceRequests.DeleteCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.carMaintenanceRequests.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("/api/carMaintenances")
public class CarMaintenancesController {

	private CarMaintenanceService carMaintenanceService;

	@Autowired
	public CarMaintenancesController(CarMaintenanceService carMaintenanceService) {
		this.carMaintenanceService = carMaintenanceService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody @Valid CreateCarMaintenanceRequest createCarMaintenanceRequest)
			throws BusinessException {
		return this.carMaintenanceService.add(createCarMaintenanceRequest);
	}

	@GetMapping("/getByCarMaintenanceCarId/{carId}")
	public DataResult<List<CarMaintenanceListDto>> getByCarMaintenanceCarId(@RequestParam("carId") int carId) {
		return this.carMaintenanceService.getByCarMaintenanceCarId(carId);
	}

	@GetMapping("/getAll")
	public DataResult<List<CarMaintenanceListDto>> getAll() {
		return this.carMaintenanceService.getAll();
	}

	@GetMapping("/getAllPaged")
	public DataResult<List<CarMaintenanceListDto>> getAllPaged(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize) {
		return this.carMaintenanceService.getAllPaged(pageNo, pageSize);
	}

	@GetMapping("/getAllSorted")
	DataResult<List<CarMaintenanceListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction) {
		return this.carMaintenanceService.getAllSorted(direction);
	}

	@GetMapping("/getByCarMaintenanceId/{carMaintanenceId}")
	public DataResult<CarMaintenanceGetDto> getByCarMaintenanceId(
			@RequestParam("carMaintanenceId") int carMaintanenceId) {
		return this.carMaintenanceService.getByCarMaintenanceId(carMaintanenceId);
	}

	@PutMapping("/update")
	public Result update(@RequestParam("carMaintenanceId") int carMaintenanceId,
			@RequestBody @Valid UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException {
		return this.carMaintenanceService.update(carMaintenanceId, updateCarMaintenanceRequest);
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody @Valid DeleteCarMaintenanceRequest deleteCarMaintenanceRequest)
			throws BusinessException {
		return this.carMaintenanceService.delete(deleteCarMaintenanceRequest);
	}

}
