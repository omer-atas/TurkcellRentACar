package com.turkcell.rentACar.business.concretes;

import java.util.List;

import java.util.stream.Collectors;

import com.turkcell.rentACar.business.abstracts.CityService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
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
    private CityService cityService;

    @Autowired
    public CarManager(CarDao carDao, ModelMapperService modelMapperService, BrandService brandService, ColorService colorService, CityService cityService) {
        this.carDao = carDao;
        this.modelMapperService = modelMapperService;
        this.brandService = brandService;
        this.colorService = colorService;
        this.cityService = cityService;
    }

    @Override
    public Result add(CreateCarRequest createCarRequest) throws BusinessException {

        checkIfBrandExists(createCarRequest.getBrandId());
        checkIfColorExists(createCarRequest.getColorId());

        Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);
        car.setCarId(0);

        this.carDao.save(car);

        return new SuccessResult(BusinessMessages.CAR_ADD + car.getCarId());
    }

    public boolean checkIfBrandExists(int brandId) throws BusinessException {

        if (this.brandService.getByBrandId(brandId).isSuccess()) {
            return true;
        }
        throw new BusinessException(BusinessMessages.BRAND_NOT_FOUND);
    }

    public boolean checkIfColorExists(int colorId) throws BusinessException {

        if (this.colorService.getByColorId(colorId).isSuccess()) {
            return true;
        }
        throw new BusinessException(BusinessMessages.COLOR_NOT_FOUND);

    }

    @Override
    public DataResult<CarGetDto> getByCarId(int carId) {

        Car result = this.carDao.getByCarId(carId);

        if (result == null) {
            return new ErrorDataResult<CarGetDto>(BusinessMessages.CAR_NOT_FOUND);
        }

        CarGetDto response = this.modelMapperService.forDto().map(result, CarGetDto.class);

        return new SuccessDataResult<CarGetDto>(response, BusinessMessages.CAR_GET_BY_ID);

    }

    @Override
    public DataResult<List<CarListDto>> getAll() {

        List<Car> result = this.carDao.findAll();

        List<CarListDto> response = result.stream().map(color -> this.modelMapperService.forDto().map(color, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CarListDto>>(response, BusinessMessages.CAR_GET_ALL);
    }

    @Override
    public DataResult<List<CarListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Car> result = this.carDao.findAll(pageable).getContent();

        List<CarListDto> response = result.stream().map(car -> this.modelMapperService.forDto().map(car, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CarListDto>>(response,BusinessMessages.CAR_GET_ALL_PAGED);
    }

    @Override
    public DataResult<List<CarListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "dailyPrice");

        List<Car> result = this.carDao.findAll(s);

        List<CarListDto> response = result.stream().map(car -> this.modelMapperService.forDto().map(car, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CarListDto>>(response,BusinessMessages.CAR_GET_ALL_SORTED);
    }

    @Override
    public Result update(int carId, UpdateCarRequest updateCarRequest) throws BusinessException {

        Car car = this.carDao.getByCarId(carId);

        checkIfParameterIsNull(updateCarRequest, car);
        checkIfColorExists(car.getColor().getColorId());

        Car carUpdate = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);

        IdCorrector(car, carUpdate);

        this.carDao.save(carUpdate);

        return new SuccessResult(car.getCarId() + BusinessMessages.CAR_UPDATE);

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

        checkIfCarExists(deleteCarRequest.getCarId());

        Car car = this.modelMapperService.forRequest().map(deleteCarRequest, Car.class);

        this.carDao.deleteById(car.getCarId());

        return new SuccessResult(deleteCarRequest.getCarId() + BusinessMessages.CAR_DELETE);

    }

    @Override
    public DataResult<List<CarListDto>> findByDailyPriceLessThanEqual(double dailyPrice) {

        List<Car> result = this.carDao.findByDailyPriceLessThanEqual(dailyPrice);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<CarListDto>>(BusinessMessages.CAR_FIND_BY_DAILY_PRICE_LESS_THAN_EQUAL_NOT_FOUND);
        }

        List<CarListDto> response = result.stream().map(color -> this.modelMapperService.forDto().map(color, CarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CarListDto>>(response, BusinessMessages.CAR_FIND_BY_DAILY_PRICE_LESS_THAN_EQUAL);

    }

    @Override
    public boolean checkIfCarExists(int carId) throws BusinessException {

        if (this.carDao.getByCarId(carId) == null) {
            throw new BusinessException(BusinessMessages.CAR_CRASH_INFORMATION_NOT_FOUND);
        }

        return true;

    }

    @Override
    public void updateKilometerInformation(int carId, double kilometerInformation) {
        this.carDao.getByCarId(carId).setKilometerInformation(kilometerInformation);
    }

}
