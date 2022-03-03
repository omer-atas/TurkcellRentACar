package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.business.abstracts.ColorService;


import com.turkcell.rentACar.business.dtos.ColorGetDto;
import com.turkcell.rentACar.business.dtos.ColorListDto;
import com.turkcell.rentACar.business.request.CreateColorRequest;
import com.turkcell.rentACar.business.request.DeleteColorRequest;
import com.turkcell.rentACar.business.request.UpdateColorRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
public class ColorsController {

	private ColorService colorService;

	@Autowired
	public ColorsController(ColorService colorService) {
		this.colorService = colorService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody CreateColorRequest createColorRequest) throws BusinessException {
		return this.colorService.add(createColorRequest);
	}

	@GetMapping("/getall")
	public DataResult<List<ColorListDto>> getAll() throws BusinessException{
		return this.colorService.getAll();
	}

	@GetMapping("/getByColorId/{colorId}")
	public DataResult<ColorGetDto> getByColorId(@RequestParam("colorId") int colorId) throws BusinessException{
		return this.colorService.getByColorId(colorId);
	}

	@PutMapping("/update")
	public Result update(@RequestBody UpdateColorRequest updateColorRequest) throws BusinessException {

		return this.colorService.update(updateColorRequest);

	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody DeleteColorRequest deleteColorRequest) throws BusinessException {
		return this.colorService.delete(deleteColorRequest);
	}
}
