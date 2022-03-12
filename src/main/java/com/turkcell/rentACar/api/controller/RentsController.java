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

import com.turkcell.rentACar.business.abstracts.RentService;
import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentListDto;
import com.turkcell.rentACar.business.request.rentRequests.CreateRentRequest;
import com.turkcell.rentACar.business.request.rentRequests.DeleteRentRequest;
import com.turkcell.rentACar.business.request.rentRequests.UpdateRentRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("/api/rentalCars")
public class RentsController {

	private RentService rentService;

	@Autowired
	public RentsController(RentService rentService) {
		super();
		this.rentService = rentService;
	}

	@PostMapping("/add")
	Result add(@RequestBody @Valid CreateRentRequest createRentRequest) throws BusinessException {
		return this.rentService.add(createRentRequest);
	}

	@GetMapping("/getByRentId/{rentId}")
	DataResult<RentGetDto> getByRentId(@RequestParam("rentId") int rentId) {
		return this.rentService.getByRentId(rentId);
	}

	@GetMapping("/getAll")
	DataResult<List<RentListDto>> getAll() {
		return this.rentService.getAll();
	}

	@GetMapping("/getAllPaged")
	DataResult<List<RentListDto>> getAllPaged(@RequestParam("pageNo") int pageNo,
											  @RequestParam("pageSize") int pageSize) {
		return this.rentService.getAllPaged(pageNo, pageSize);
	}

	@GetMapping("/getAllSorted")
	DataResult<List<RentListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction) {
		return this.rentService.getAllSorted(direction);
	}

	@PutMapping("/update")
	Result update(@RequestParam("rentalId") int rentalId, @RequestBody @Valid UpdateRentRequest updateRentRequest)
			throws BusinessException {
		return this.rentService.update(rentalId, updateRentRequest);
	}

	@DeleteMapping("/delete")
	Result delete(@RequestBody DeleteRentRequest deleteRentRequest) throws BusinessException {
		return this.rentService.delete(deleteRentRequest);
	}

}