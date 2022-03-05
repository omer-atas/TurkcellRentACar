package com.turkcell.rentACar.business.concretes;

import java.time.LocalDate;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.abstracts.ColorService;
import com.turkcell.rentACar.business.dtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.CarListDto;
import com.turkcell.rentACar.business.dtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.request.CreateCarRequest;
import com.turkcell.rentACar.business.request.DeleteCarRequest;
import com.turkcell.rentACar.business.request.UpdateCarRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarDao;
import com.turkcell.rentACar.entities.concretes.Car;

@Service
public class CarManager implements CarService {

	private CarDao carDao;
	private ModelMapperService modelMapperService;
	private BrandService brandService;
	private ColorService colorService;
	private CarMaintenanceService carMaintenanceService;

	@Autowired
	public CarManager(CarDao carDao, ModelMapperService modelMapperService, BrandService brandService,
			ColorService colorService, CarMaintenanceService carMaintenanceService) {
		this.carDao = carDao;
		this.modelMapperService = modelMapperService;
		this.brandService = brandService;
		this.colorService = colorService;
		this.carMaintenanceService = carMaintenanceService;
	}

	@Override
	public Result add(CreateCarRequest createCarRequest) throws BusinessException {

		Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);

		checkIfBrand(createCarRequest.getBrandId());
		checkIfColor(createCarRequest.getColorId());

		this.carDao.save(car);
		return new SuccessResult("Car added : " + car.getCarId());
	}

	@Override
	public boolean checkIfBrand(int brandId) throws BusinessException {

		if (this.brandService.getByBrandId(brandId).isSuccess()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkIfColor(int colorId) throws BusinessException {

		if (this.colorService.getByColorId(colorId).isSuccess()) {
			return true;
		}
		return false;

	}
	

	@Override
	public DataResult<CarGetDto> getByCarId(int carId) {

		Car result = this.carDao.getByCarId(carId);

		if (result == null) {
			return new ErrorDataResult<CarGetDto>("Car with submitted id not found");
		}

		CarGetDto response = this.modelMapperService.forDto().map(result, CarGetDto.class);

		return new SuccessDataResult<CarGetDto>(response, "Success");

	}

	@Override
	public DataResult<List<CarListDto>> getAll() {

		List<Car> result = this.carDao.findAll();

		if (result.isEmpty()) {
			return new ErrorDataResult<List<CarListDto>>("Cars not listed");
		}

		List<CarListDto> response = result.stream()
				.map(color -> this.modelMapperService.forDto().map(color, CarListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarListDto>>(response, "Cars Listed Successfully");
	}

	@Override
	public DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) {

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		List<Car> result = this.carDao.findAll(pageable).getContent();

		if (result.isEmpty()) {
			return new ErrorDataResult<List<CarListDto>>("Cars not list - getAllPaged - ");
		}

		List<CarListDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, CarListDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<CarListDto>>(response);
	}

	@Override
	public DataResult<List<CarListDto>> getAllSorted(Sort.Direction direction) {

		Sort s = Sort.by(direction, "dailyPrice");

		List<Car> result = this.carDao.findAll(s);

		if (result.isEmpty()) {
			return new ErrorDataResult<List<CarListDto>>("Car not list - getAllSorted -");
		}

		List<CarListDto> response = result.stream()
				.map(product -> this.modelMapperService.forDto().map(product, CarListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarListDto>>(response);
	}

	@Override
	public Result update(UpdateCarRequest updateCarRequest) throws BusinessException {

		Car car = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);

		checkIfIsThereCar(car.getCarId());
		checkIfBrand(car.getBrand().getBrandId());
		checkIfColor(car.getColor().getColorId());

		this.carDao.save(car);
		return new SuccessResult(updateCarRequest.getCarId() + " updated..");

	}

	@Override
	public Result delete(DeleteCarRequest deleteCarRequest) throws BusinessException {

		Car car = this.modelMapperService.forRequest().map(deleteCarRequest, Car.class);

		checkIfIsThereCar(deleteCarRequest.getCarId());

		this.carDao.deleteById(car.getCarId());

		return new SuccessResult(deleteCarRequest.getCarId() + " deleted..");

	}

	@Override
	public DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice) {

		List<Car> result = this.carDao.findByDailyPriceLessThanEqual(dailyPrice);

		if (result.isEmpty()) {
			return new ErrorDataResult<List<CarListDto>>("findByDailyPriceLessThanEqual not list");
		}

		List<CarListDto> response = result.stream()
				.map(color -> this.modelMapperService.forDto().map(color, CarListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarListDto>>(response, "Listed");

	}

	@Override
	public boolean checkIfIsThereCar(int carId) throws BusinessException {

		if (this.carDao.getByCarId(carId) == null) {
			throw new BusinessException("Car not found ");
		}

		return true;

	}

	private void checkIfCarIsInMaintenance(int carId) throws BusinessException {

		checkIfIsThereCar(carId);

		for (CarMaintenanceListDto carMaintenance : this.carMaintenanceService.getAllCarMaintenanceByCarId(carId)) {

			if (carMaintenance.getReturnDate() == null) {
				throw new BusinessException("Dönüş tarihi null olduğu için kiraya verilemez....");
			}

			if (LocalDate.now().isBefore(carMaintenance.getReturnDate())
					|| LocalDate.now().equals(carMaintenance.getReturnDate())) {
				throw new BusinessException("Araba bakımda olduğu için kiraya verilemez....");
			}

		}
	}

	@Override
	public DataResult<CarGetDto> carCanBeRent(int carId) throws BusinessException {

		Car car = this.carDao.getByCarId(carId);

		checkIfCarIsInMaintenance(carId);

		CarGetDto response = this.modelMapperService.forDto().map(car, CarGetDto.class);

		return new SuccessDataResult<CarGetDto>(response, carId + " id'sine sahip araç kiraya verilebilir..");

	}

}
