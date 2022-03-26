package com.turkcell.rentACar.business.concretes;

import java.util.List;

import java.util.stream.Collectors;

import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
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
	private CarService carService;

	@Autowired
	public BrandManager(BrandDao brandDao, ModelMapperService modelMapperService,CarService carService) {
		this.brandDao = brandDao;
		this.modelMapperService = modelMapperService;
		this.carService = carService;
	}

	@Override
	public Result add(CreateBrandRequest createBrandRequest) throws BusinessException {

		checkIfNameNotDuplicated(createBrandRequest.getBrandName());

		Brand brand = this.modelMapperService.forRequest().map(createBrandRequest, Brand.class);
		brand.setBrandId(0);

		this.brandDao.save(brand);

		return new SuccessResult(BusinessMessages.BRAND_ADD + brand.getBrandName());

	}

	private void checkIfNameNotDuplicated(String brandName) throws BusinessException {

		if (this.brandDao.existsByBrandName(brandName)) {
			throw new BusinessException(BusinessMessages.BRAND_NAME_NOT_DUPLICATED);
		}

    }

	@Override
	public DataResult<List<BrandListDto>> getAll() {

		List<Brand> result = this.brandDao.findAll();

		List<BrandListDto> response = result.stream()
				.map(brand -> this.modelMapperService.forDto().map(brand, BrandListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<BrandListDto>>(response, BusinessMessages.BRAND_GET_ALL);
	}

	@Override
	public DataResult<List<BrandListDto>> getAllPaged(int pageNo, int pageSize) {

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		List<Brand> result = this.brandDao.findAll(pageable).getContent();

		List<BrandListDto> response = result.stream()
				.map(brand -> this.modelMapperService.forDto().map(brand, BrandListDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<BrandListDto>>(response, BusinessMessages.BRAND_GET_ALL_PAGED);
	}

	@Override
	public DataResult<List<BrandListDto>> getAllSorted(Direction direction) {

		Sort s = Sort.by(direction, "brandName");

		List<Brand> result = this.brandDao.findAll(s);

		List<BrandListDto> response = result.stream()
				.map(brand -> this.modelMapperService.forDto().map(brand, BrandListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<BrandListDto>>(response,BusinessMessages.BRAND_GET_ALL_SORTED);
	}

	@Override
	public DataResult<BrandGetDto> getByBrandId(int brandId) {

		Brand result = this.brandDao.getByBrandId(brandId);

		if (result == null) {
			return new ErrorDataResult<BrandGetDto>(BusinessMessages.BRAND_NOT_FOUND);
		}

		BrandGetDto response = this.modelMapperService.forDto().map(result, BrandGetDto.class);

		return new SuccessDataResult<BrandGetDto>(response, BusinessMessages.BRAND_GET_BY_ID);

	}

	@Override
	public boolean checkIfBrandExists(int brandId) throws BusinessException {

		if (this.brandDao.getByBrandId(brandId) == null) {
			throw new BusinessException(BusinessMessages.BRAND_NOT_FOUND);
		}

		return true;

	}

	@Override
	public Result update(int brandId, UpdateBrandRequest updateBrandRequest) throws BusinessException {

		checkIfBrandExists(brandId);
		checkIfNameNotDuplicated(updateBrandRequest.getBrandName());

		Brand brand = this.brandDao.getByBrandId(brandId);

		Brand brandUpdate = this.modelMapperService.forRequest().map(updateBrandRequest, Brand.class);

		IdCorrector(brand, brandUpdate);

		this.brandDao.save(brandUpdate);

		return new SuccessResult(updateBrandRequest.getBrandName() + BusinessMessages.BRAND_UPDATE);

	}

	private void IdCorrector(Brand brand, Brand brandUpdate) {
		brandUpdate.setBrandId(brand.getBrandId());
	}

	@Override
	public Result delete(DeleteBrandRequest deleteBrandRequest) throws BusinessException {

		checkIfBrandExists(deleteBrandRequest.getBrandId());
		checkIfThereIsACarWithThisBrand(deleteBrandRequest.getBrandId());

		Brand brand = this.modelMapperService.forRequest().map(deleteBrandRequest, Brand.class);

		this.brandDao.deleteById(brand.getBrandId());

		return new SuccessResult(deleteBrandRequest.getBrandId() + BusinessMessages.BRAND_DELETE);

	}

	private void checkIfThereIsACarWithThisBrand(int brandId) throws BusinessException {

		if(!this.carService.getByBrand_BrandId(brandId).getData().isEmpty()){
			throw new BusinessException(BusinessMessages.IS_THERE_A_BRAND_OF_CAR_AVAILABLE);
		}
	}

}