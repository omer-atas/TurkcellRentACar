package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.BrandGetDto;


import com.turkcell.rentACar.business.dtos.BrandListDto;
import com.turkcell.rentACar.business.request.CreateBrandRequest;
import com.turkcell.rentACar.business.request.DeleteBrandRequest;
import com.turkcell.rentACar.business.request.UpdateBrandRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

import org.springframework.data.domain.Sort;

public interface BrandService {

	Result add(CreateBrandRequest CreateBrandRequest) throws BusinessException;

	DataResult<BrandGetDto> getByBrandId(int brandId);

	DataResult<List<BrandListDto>> getAll();
	
	DataResult<List<BrandListDto>> getAllPaged(int pageNo, int pageSize);

	DataResult<List<BrandListDto>> getAllSorted(Sort.Direction direction);

	Result update(int brandId,UpdateBrandRequest updateBrandRequest) throws BusinessException;

	boolean checkIfIsThereBrand(int brandId) throws BusinessException;

	Result delete(DeleteBrandRequest deleteBrandRequest) throws BusinessException;

}
