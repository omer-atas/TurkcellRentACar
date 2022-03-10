package com.turkcell.rentACar.business.concretes;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.turkcell.rentACar.business.abstracts.BrandService;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.abstracts.ColorService;
import com.turkcell.rentACar.business.dtos.carDtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.carDtos.CarListDto;
import com.turkcell.rentACar.business.request.carRequests.CreateCarRequest;
import com.turkcell.rentACar.business.request.carRequests.DeleteCarRequest;
import com.turkcell.rentACar.business.request.carRequests.UpdateCarRequest;
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

    @Autowired
    public CarManager(CarDao carDao, ModelMapperService modelMapperService, BrandService brandService, ColorService colorService) {
        this.carDao = carDao;
        this.modelMapperService = modelMapperService;
        this.brandService = brandService;
        this.colorService = colorService;
    }

    @Override
    public Result add(CreateCarRequest createCarRequest) throws BusinessException {

        Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);

        checkIfBrandExists(createCarRequest.getBrandId());
        checkIfColorExists(createCarRequest.getColorId());

        this.carDao.save(car);
        return new SuccessResult("Car added : " + car.getCarId());
    }

    public boolean checkIfBrandExists(int brandId) throws BusinessException {

        if (this.brandService.getByBrandId(brandId).isSuccess()) {
            return true;
        }
        throw new BusinessException("There is no brand corresponding to the sent id");
    }

    public boolean checkIfColorExists(int colorId) throws BusinessException {

        if (this.colorService.getByColorId(colorId).isSuccess()) {
            return true;
        }
        throw new BusinessException("There is no color corresponding to the sent id");

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

        List<CarListDto> response = result.stream().map(color -> this.modelMapperService.forDto().map(color, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CarListDto>>(response, "Cars Listed Successfully");
    }

    @Override
    public DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Car> result = this.carDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<CarListDto>>("Cars not list - getAllPaged - ");
        }

        List<CarListDto> response = result.stream().map(car -> this.modelMapperService.forDto().map(car, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CarListDto>>(response);
    }

    @Override
    public DataResult<List<CarListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "dailyPrice");

        List<Car> result = this.carDao.findAll(s);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<CarListDto>>("Car not list - getAllSorted -");
        }

        List<CarListDto> response = result.stream().map(product -> this.modelMapperService.forDto().map(product, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CarListDto>>(response);
    }

    @Override
    public Result update(int carId, UpdateCarRequest updateCarRequest) throws BusinessException {

        Car car = this.carDao.getByCarId(carId);

        checkIfParameterIsNull(updateCarRequest, car);
        checkIfColorExists(car.getColor().getColorId());

        Car carUpdate = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);

        IdCorrector(car, carUpdate);

        this.carDao.save(carUpdate);
        return new SuccessResult(car.getCarId() + " updated..");

    }

    private UpdateCarRequest checkIfParameterIsNull(UpdateCarRequest updateCarRequest, Car car) {

        if (updateCarRequest.getDescription() == null) {
            updateCarRequest.setDescription(car.getDescription());
        }

        if (updateCarRequest.getDailyPrice() == 0) {
            updateCarRequest.setDailyPrice(car.getDailyPrice());
        }

        if (updateCarRequest.getModelYear() == 0) {
            updateCarRequest.setModelYear(car.getModelYear());
        }

        if (updateCarRequest.getColorId() == 0) {
            updateCarRequest.setColorId(car.getColor().getColorId());
        }

        return updateCarRequest;
    }

    private void IdCorrector(Car car, Car carUpdate) {
        carUpdate.setCarId(car.getCarId());
        carUpdate.setBrand(car.getBrand());
    }

    @Override
    public Result delete(DeleteCarRequest deleteCarRequest) throws BusinessException {

        Car car = this.modelMapperService.forRequest().map(deleteCarRequest, Car.class);

        checkIfCarExists(deleteCarRequest.getCarId());

        this.carDao.deleteById(car.getCarId());

        return new SuccessResult(deleteCarRequest.getCarId() + " deleted..");

    }

    @Override
    public DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice) {

        List<Car> result = this.carDao.findByDailyPriceLessThanEqual(dailyPrice);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<CarListDto>>("findByDailyPriceLessThanEqual not list");
        }

        List<CarListDto> response = result.stream().map(color -> this.modelMapperService.forDto().map(color, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CarListDto>>(response, "Listed");

    }

    @Override
    public boolean checkIfCarExists(int carId) throws BusinessException {

        if (this.carDao.getByCarId(carId) == null) {
            throw new BusinessException("Car not found ");
        }

        return true;

    }

}
