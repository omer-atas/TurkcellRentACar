package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.business.abstracts.ColorService;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorGetDto;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorListDto;
import com.turkcell.rentACar.business.request.colorRequests.CreateColorRequest;
import com.turkcell.rentACar.business.request.colorRequests.DeleteColorRequest;
import com.turkcell.rentACar.business.request.colorRequests.UpdateColorRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/colors")
public class ColorsController {

	private ColorService colorService;

	@Autowired
	public ColorsController(ColorService colorService) {
		this.colorService = colorService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody @Valid CreateColorRequest createColorRequest) throws BusinessException {
		return this.colorService.add(createColorRequest);
	}

	@GetMapping("/getall")
	public DataResult<List<ColorListDto>> getAll() {
		return this.colorService.getAll();
	}

	@GetMapping("/getAllSorted")
	public DataResult<List<ColorListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction) {
		return this.colorService.getAllSorted(direction);
	}

	@GetMapping("/getAllPaged")
	public DataResult<List<ColorListDto>> getAllPaged(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize) {
		return this.colorService.getAllPaged(pageNo, pageSize);
	}

	@GetMapping("/getByColorId/{colorId}")
	public DataResult<ColorGetDto> getByColorId(@RequestParam("colorId") int colorId) {
		return this.colorService.getByColorId(colorId);
	}

	@PutMapping("/update")
	public Result update(@RequestParam("colorId") int colorId,
			@RequestBody @Valid UpdateColorRequest updateColorRequest) throws BusinessException {
		return this.colorService.update(colorId, updateColorRequest);

	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody @Valid DeleteColorRequest deleteColorRequest) throws BusinessException {
		return this.colorService.delete(deleteColorRequest);
	}
}
