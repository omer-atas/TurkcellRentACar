package com.turkcell.rentACar.business.concretes;

import java.util.List;


import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.dtos.BrandGetDto;
import com.turkcell.rentACar.business.dtos.BrandListDto;
import com.turkcell.rentACar.business.request.CreateBrandRequest;
import com.turkcell.rentACar.business.request.DeleteBrandRequest;
import com.turkcell.rentACar.business.request.UpdateBrandRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.BrandDao;
import com.turkcell.rentACar.entities.concretes.Brand;

@Service
public class BrandManager implements BrandService {

	private BrandDao brandDao;
	private ModelMapperService modelMapperService;

	@Autowired
	public BrandManager(BrandDao brandDao, ModelMapperService modelMapperService) {
		this.brandDao = brandDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreateBrandRequest createBrandRequest) throws BusinessException {

		Brand brand = this.modelMapperService.forRequest().map(createBrandRequest, Brand.class);

		checkIfName(createBrandRequest.getBrandName());
		this.brandDao.save(brand);
		return new SuccessResult("Brand added : " + brand.getBrandName());

	}

	private boolean checkIfName(String brandName) throws BusinessException {

		if (this.brandDao.existsByBrandName(brandName)) {
			throw new BusinessException("Names can't be the same");
		}

		return true;

	}

	@Override
	public DataResult<List<BrandListDto>> getAll() throws BusinessException {

		List<Brand> result = this.brandDao.findAll();

		checkIfListEmpty(result);

		List<BrandListDto> response = result.stream()
				.map(brand -> this.modelMapperService.forDto().map(brand, BrandListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<BrandListDto>>(response, "Success");
	}

	private Brand checkIfEmpty(Brand result) throws BusinessException {

		if (result == null) {
			throw new BusinessException("No data");
		}

		return result;
	}

	private List<Brand> checkIfListEmpty(List<Brand> result) throws BusinessException {

		if (result == null) {
			throw new BusinessException("No data");
		}

		return result;
	}

	@Override
	public DataResult<BrandGetDto> getByBrandId(int brandId) throws BusinessException {

		Brand result = this.brandDao.getByBrandId(brandId);

		checkIfEmpty(result);

		BrandGetDto response = this.modelMapperService.forDto().map(result, BrandGetDto.class);
		return new SuccessDataResult<BrandGetDto>(response, "Success");

	}

	private boolean checkIfIsData(int brandId) throws BusinessException {

		if (this.brandDao.getByBrandId(brandId) == null) {
			throw new BusinessException("There is no data in the id sent");
		}

		return true;

	}

	@Override
	public Result update(UpdateBrandRequest updateBrandRequest) throws BusinessException {

		Brand brand = this.modelMapperService.forRequest().map(updateBrandRequest, Brand.class);

		checkIfIsData(brand.getBrandId());
		checkIfName(updateBrandRequest.getBrandName());

		this.brandDao.save(brand);
		return new SuccessResult(updateBrandRequest.getBrandName() + " updated..");

	}

	@Override
	public Result delete(DeleteBrandRequest deleteBrandRequest) throws BusinessException {

		Brand brand = this.modelMapperService.forRequest().map(deleteBrandRequest, Brand.class);

		checkIfIsData(brand.getBrandId());
		this.brandDao.deleteById(brand.getBrandId());
		return new SuccessResult(deleteBrandRequest.getBrandId() + " deleted..");

	}
}