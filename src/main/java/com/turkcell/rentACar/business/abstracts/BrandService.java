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

public interface BrandService {

	Result add(CreateBrandRequest CreateBrandRequest) throws BusinessException;

	DataResult<BrandGetDto> getByBrandId(int brandId) throws BusinessException;

	DataResult<List<BrandListDto>> getAll() throws BusinessException;

	Result update(UpdateBrandRequest updateBrandRequest) throws BusinessException;

	Result delete(DeleteBrandRequest deleteBrandRequest) throws BusinessException;

}
