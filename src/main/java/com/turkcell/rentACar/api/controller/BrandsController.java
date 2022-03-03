package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.business.abstracts.BrandService;

import com.turkcell.rentACar.business.dtos.BrandGetDto;
import com.turkcell.rentACar.business.dtos.BrandListDto;
import com.turkcell.rentACar.business.request.CreateBrandRequest;
import com.turkcell.rentACar.business.request.DeleteBrandRequest;
import com.turkcell.rentACar.business.request.UpdateBrandRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandsController {

	private BrandService brandService;

	public BrandsController(BrandService brandService) {
		this.brandService = brandService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody CreateBrandRequest createBrandRequest) throws BusinessException {
		return this.brandService.add(createBrandRequest);
	}

	@GetMapping("/getall")
	public DataResult<List<BrandListDto>> getAll() throws BusinessException {
		return this.brandService.getAll();
	}

	@GetMapping("/getByBrandId/{brandId}")
	public DataResult<BrandGetDto> getByBrandId(@RequestParam("brandId") int brandId) throws BusinessException {
		return this.brandService.getByBrandId(brandId);
	}

	@PutMapping("/update")
	public Result update(@RequestBody UpdateBrandRequest updateBrandRequest) throws BusinessException {
		return this.brandService.update(updateBrandRequest);
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody DeleteBrandRequest deleteBrandRequest) throws BusinessException {
		return this.brandService.delete(deleteBrandRequest);
	}
}
