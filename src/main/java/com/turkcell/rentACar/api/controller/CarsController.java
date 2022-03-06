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

import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.dtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.CarListDto;
import com.turkcell.rentACar.business.request.CreateCarRequest;
import com.turkcell.rentACar.business.request.DeleteCarRequest;
import com.turkcell.rentACar.business.request.UpdateCarRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("/api/cars")
public class CarsController {

	private CarService carService;

	@Autowired
	public CarsController(CarService carService) {
		this.carService = carService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody @Valid CreateCarRequest createCarRequest) throws BusinessException {

		return this.carService.add(createCarRequest);

	}

	@GetMapping("/getall")
	public DataResult<List<CarListDto>> getAll() {
		return this.carService.getAll();
	}

	@GetMapping("/getAllSorted")
	public DataResult<List<CarListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction) {
		return this.carService.getAllSorted(direction);
	}

	@GetMapping("/getAllPaged")
	public DataResult<List<CarListDto>> getAllPaged(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize) {
		return this.carService.getAllPaged(pageNo, pageSize);
	}

	@GetMapping("/getByCarId/{carId}")
	public DataResult<CarGetDto> getByCarId(@RequestParam("carId") int carId) {
		return this.carService.getByCarId(carId);
	}

	@GetMapping("/findByDailyPriceLessThanEqual")
	public DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(@RequestParam("dailyPrice") double dailyPrice) {
		return this.carService.findByDailyPriceLessThanEqual(dailyPrice);
	}

	@PutMapping("/update")
	public Result update(@RequestBody @Valid UpdateCarRequest updateCarRequest) throws BusinessException {
		return this.carService.update(updateCarRequest);
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody DeleteCarRequest deleteCarRequest) throws BusinessException {
		return this.carService.delete(deleteCarRequest);
	}
}
