package com.turkcell.rentACar.business.concretes;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.dtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.CarListDto;
import com.turkcell.rentACar.business.request.CreateCarRequest;
import com.turkcell.rentACar.business.request.DeleteCarRequest;
import com.turkcell.rentACar.business.request.UpdateCarRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.BrandDao;
import com.turkcell.rentACar.dataAccess.abstracts.CarDao;
import com.turkcell.rentACar.dataAccess.abstracts.CarMaintenanceDao;
import com.turkcell.rentACar.dataAccess.abstracts.ColorDao;
import com.turkcell.rentACar.entities.concretes.Car;

@Service
public class CarManager implements CarService {

	private CarDao carDao;
	private BrandDao brandDao;
	private ColorDao colorDao;
	private ModelMapperService modelMapperService;
	private CarMaintenanceDao carMaintenanceDao;

	@Autowired
	public CarManager(CarDao carDao, ModelMapperService modelMapperService, BrandDao brandDao, ColorDao colorDao,
			CarMaintenanceDao carMaintenanceDao) {
		this.carDao = carDao;
		this.modelMapperService = modelMapperService;
		this.brandDao = brandDao;
		this.colorDao = colorDao;
		this.carMaintenanceDao = carMaintenanceDao;
	}

	@Override
	public Result add(CreateCarRequest createCarRequest) throws BusinessException {

		checkIfColorAndBrandİsAvailable(createCarRequest.getBrandId(), createCarRequest.getColorId());

		Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);

		checkIfEmpty(car);

		this.carDao.save(car);
		return new SuccessResult("Car added : " + car.getDescription());
	}
	
	private Car checkIfEmpty(Car result) throws BusinessException {

		if (result == null) {
			throw new BusinessException("No data");
		}

		return result;
	}

	private List<Car> checkIfListEmpty(List<Car> result) throws BusinessException {

		if (result == null) {
			throw new BusinessException("No data");
		}

		return result;
	}

	public void checkIfBrand(int brandId) throws BusinessException {

		if (this.brandDao.getByBrandId(brandId) == null) {
			throw new BusinessException("Brand cannot found");
		}
	}

	public void checkIfColor(int colorId) throws BusinessException {

		if (this.colorDao.getByColorId(colorId) == null) {
			throw new BusinessException("Brand cannot found");
		}
	}

	private void checkIfColorAndBrandİsAvailable(int brandId, int colorId) throws BusinessException {

		if (this.brandDao.getByBrandId(brandId) == null) {
			throw new BusinessException("Brand does not exist.");
		} else if (this.colorDao.getByColorId(colorId) == null) {
			throw new BusinessException("Color does not exist.");
		}

	}

	@Override
	public DataResult<CarGetDto> getByCarId(int carId) throws BusinessException {

		Car result = this.carDao.getByCarId(carId);

		checkIfEmpty(result);

		CarGetDto response = this.modelMapperService.forDto().map(result, CarGetDto.class);
		return new SuccessDataResult<CarGetDto>(response, "Success");

	}

	@Override
	public DataResult<List<CarListDto>> getAll() throws BusinessException {

		List<Car> result = this.carDao.findAll();

		checkIfListEmpty(result);

		List<CarListDto> response = result.stream()
				.map(color -> this.modelMapperService.forDto().map(color, CarListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CarListDto>>(response, "Success");
	}

	private UpdateCarRequest checkIfUpdateCarRequest(int carId, UpdateCarRequest updateCar) {

		if (updateCar.getDescription().isEmpty() || updateCar.getDescription().isBlank()
				|| updateCar.getDescription().equals("string") || updateCar.getDescription().equals("")
				|| updateCar.getDescription().equals(null)) {
			updateCar.setDescription(this.carDao.getByCarId(carId).getDescription());
		}

		if (updateCar.getModelYear() == 0) {
			updateCar.setModelYear(this.carDao.getByCarId(carId).getModelYear());
		}

		if (updateCar.getDailyPrice() == 0) {
			updateCar.setDailyPrice(this.carDao.getByCarId(carId).getDailyPrice());
		}

		if (updateCar.getBrandId() == 0) {
			updateCar.setBrandId(this.carDao.getByCarId(carId).getBrand().getBrandId());
		}

		if (updateCar.getColorId() == 0) {
			updateCar.setColorId(this.carDao.getByCarId(carId).getColor().getColorId());
		}

		return updateCar;
	}

	@Override
	public Result update(UpdateCarRequest updateCarRequest) throws BusinessException {

		updateCarRequest = checkIfUpdateCarRequest(updateCarRequest.getCarId(), updateCarRequest);

		checkIfColorAndBrandİsAvailable(updateCarRequest.getBrandId(), updateCarRequest.getColorId());

		Car car = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);

		checkIfIsData(updateCarRequest.getCarId());

		this.carDao.save(car);
		return new SuccessResult(updateCarRequest.getCarId() + " updated..");

	}

	@Override
	public Result delete(DeleteCarRequest deleteCarRequest) throws BusinessException {

		Car car = this.modelMapperService.forRequest().map(deleteCarRequest, Car.class);

		checkIfIsData(deleteCarRequest.getCarId());

		this.carDao.deleteById(car.getCarId());
		this.carMaintenanceDao.deleteByCar_CarId(car.getCarId());
		return new SuccessResult(deleteCarRequest.getCarId() + " deleted..");

	}

	@Override
	public DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice) throws BusinessException {

		List<Car> result = this.carDao.findByDailyPriceLessThanEqual(dailyPrice);

		checkIfListEmpty(result);

		List<CarListDto> response = result.stream()
				.map(color -> this.modelMapperService.forDto().map(color, CarListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CarListDto>>(response, "Listed");

	}

	@Override
	public DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) throws BusinessException {

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		List<Car> result = this.carDao.findAll(pageable).getContent();

		checkIfListEmpty(result);

		List<CarListDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, CarListDto.class)).collect(Collectors.toList());
		return new SuccessDataResult<List<CarListDto>>(response);
	}

	@Override
	public DataResult<List<CarListDto>> getAllSorted(Sort.Direction direction) throws BusinessException {

		Sort s = Sort.by(direction, "dailyPrice");

		List<Car> result = this.carDao.findAll(s);

		checkIfListEmpty(result);

		List<CarListDto> response = result.stream()
				.map(product -> this.modelMapperService.forDto().map(product, CarListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CarListDto>>(response);
	}

	private boolean checkIfIsData(int carId) throws BusinessException {

		if (this.carDao.getByCarId(carId) == null) {
			throw new BusinessException("No data");
		}

		return true;

	}

}
