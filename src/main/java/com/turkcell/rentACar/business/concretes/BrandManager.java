package com.turkcell.rentACar.business.concretes;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandGetDto;
import com.turkcell.rentACar.business.dtos.brandDtos.BrandListDto;
import com.turkcell.rentACar.business.request.brandRequests.CreateBrandRequest;
import com.turkcell.rentACar.business.request.brandRequests.DeleteBrandRequest;
import com.turkcell.rentACar.business.request.brandRequests.UpdateBrandRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.ErrorDataResult;
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

		checkIfSameName(createBrandRequest.getBrandName());

		this.brandDao.save(brand);
		return new SuccessResult("Brand added : " + brand.getBrandName());

	}

	private boolean checkIfSameName(String brandName) throws BusinessException {

		if (this.brandDao.existsByBrandName(brandName)) {
			throw new BusinessException("Names can't be the same");
		}

		return true;

	}

	@Override
	public DataResult<List<BrandListDto>> getAll() {

		List<Brand> result = this.brandDao.findAll();

		if (result.isEmpty()) {
			return new ErrorDataResult<List<BrandListDto>>("Brands not listed");
		}

		List<BrandListDto> response = result.stream()
				.map(brand -> this.modelMapperService.forDto().map(brand, BrandListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<BrandListDto>>(response, "Success");
	}

	@Override
	public DataResult<List<BrandListDto>> getAllPaged(int pageNo, int pageSize) {

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		List<Brand> result = this.brandDao.findAll(pageable).getContent();

		if (result.isEmpty()) {
			return new ErrorDataResult<List<BrandListDto>>("Brands not list - getAllPaged - ");
		}

		List<BrandListDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, BrandListDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<BrandListDto>>(response, "Brands Listed Successfully");
	}

	@Override
	public DataResult<List<BrandListDto>> getAllSorted(Direction direction) {

		Sort s = Sort.by(direction, "brandName");

		List<Brand> result = this.brandDao.findAll(s);

		if (result.isEmpty()) {
			return new ErrorDataResult<List<BrandListDto>>("Cars not list - getAllSorted -");
		}

		List<BrandListDto> response = result.stream()
				.map(product -> this.modelMapperService.forDto().map(product, BrandListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<BrandListDto>>(response);
	}

	@Override
	public DataResult<BrandGetDto> getByBrandId(int brandId) {

		Brand result = this.brandDao.getByBrandId(brandId);

		if (result == null) {
			return new ErrorDataResult<BrandGetDto>("Brand not found");
		}

		BrandGetDto response = this.modelMapperService.forDto().map(result, BrandGetDto.class);
		return new SuccessDataResult<BrandGetDto>(response, "Success");

	}

	@Override
	public boolean checkIfIsThereBrand(int brandId) throws BusinessException {

		if (this.brandDao.getByBrandId(brandId) == null) {
			throw new BusinessException("There is no data in the id sent");
		}

		return true;

	}

	@Override
	public Result update(int brandId, UpdateBrandRequest updateBrandRequest) throws BusinessException {

		Brand brand = this.brandDao.getByBrandId(brandId);

		Brand brandUpdate = this.modelMapperService.forRequest().map(updateBrandRequest, Brand.class);

		IdCorrector(brand, brandUpdate);
		checkIfSameName(brandUpdate.getBrandName());

		this.brandDao.save(brandUpdate);
		return new SuccessResult(updateBrandRequest.getBrandName() + " updated..");

	}

	private void IdCorrector(Brand brand, Brand brandUpdate) {
		brandUpdate.setBrandId(brand.getBrandId());
	}

	@Override
	public Result delete(DeleteBrandRequest deleteBrandRequest) throws BusinessException {

		Brand brand = this.modelMapperService.forRequest().map(deleteBrandRequest, Brand.class);

		checkIfIsThereBrand(brand.getBrandId());

		this.brandDao.deleteById(brand.getBrandId());
		return new SuccessResult(deleteBrandRequest.getBrandId() + " deleted..");

	}

}