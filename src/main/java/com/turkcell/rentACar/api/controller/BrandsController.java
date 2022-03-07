package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandGetDto;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandListDto;
import com.turkcell.rentACar.business.request.brandRequests.CreateBrandRequest;
import com.turkcell.rentACar.business.request.brandRequests.DeleteBrandRequest;
import com.turkcell.rentACar.business.request.brandRequests.UpdateBrandRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/brands")
public class BrandsController {

	private BrandService brandService;

	public BrandsController(BrandService brandService) {
		this.brandService = brandService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody @Valid CreateBrandRequest createBrandRequest) throws BusinessException {
		return this.brandService.add(createBrandRequest);
	}

	@GetMapping("/getall")
	public DataResult<List<BrandListDto>> getAll() {
		return this.brandService.getAll();
	}

	@GetMapping("/getAllSorted")
	public DataResult<List<BrandListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction) {
		return this.brandService.getAllSorted(direction);
	}

	@GetMapping("/getAllPaged")
	public DataResult<List<BrandListDto>> getAllPaged(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize) {
		return this.brandService.getAllPaged(pageNo, pageSize);
	}

	@GetMapping("/getByBrandId/{brandId}")
	public DataResult<BrandGetDto> getByBrandId(@RequestParam("brandId") int brandId) {
		return this.brandService.getByBrandId(brandId);
	}

	@PutMapping("/update")
	public Result update(@RequestParam("brandId") int brandId,
			@RequestBody @Valid UpdateBrandRequest updateBrandRequest) throws BusinessException {
		return this.brandService.update(brandId, updateBrandRequest);
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody DeleteBrandRequest deleteBrandRequest) throws BusinessException {
		return this.brandService.delete(deleteBrandRequest);
	}
}
