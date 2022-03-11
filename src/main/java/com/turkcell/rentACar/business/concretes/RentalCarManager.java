package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.dtos.carDtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.dtos.cityDtos.CityGetDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarGetDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarListDto;
import com.turkcell.rentACar.business.request.rentalCarRequests.CreateRentalCarRequest;
import com.turkcell.rentACar.business.request.rentalCarRequests.DeleteRentalCarRequest;
import com.turkcell.rentACar.business.request.rentalCarRequests.UpdateRentalCarRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.RentalCarDao;
import com.turkcell.rentACar.entities.concretes.City;
import com.turkcell.rentACar.entities.concretes.RentalCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalCarManager implements RentalCarService {

    private RentalCarDao rentalCarDao;
    private ModelMapperService modelMapperService;
    private CarMaintenanceService carMaintenanceService;
    private CarService carService;
    private CityService cityService;
    private OrderedAdditionalServiceService orderedAdditionalServiceService;

    @Lazy
    @Autowired
    public RentalCarManager(RentalCarDao rentalCarDao, ModelMapperService modelMapperService, CarMaintenanceService carMaintenanceService, CarService carService,CityService cityService,OrderedAdditionalServiceService orderedAdditionalServiceService) {
        this.rentalCarDao = rentalCarDao;
        this.modelMapperService = modelMapperService;
        this.carMaintenanceService = carMaintenanceService;
        this.carService = carService;
        this.cityService = cityService;
        this.orderedAdditionalServiceService = orderedAdditionalServiceService;
    }

    @Override
    public Result add(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        RentalCar rentalCar = this.modelMapperService.forRequest().map(createRentalCarRequest, RentalCar.class);
        rentalCar.setRentalId(0);

        checkIfCarExists(rentalCar.getCar().getCarId());
        checkIfCarMaintenanced(createRentalCarRequest);

        City city = updateCity(createRentalCarRequest);
        rentalCar.setReturnCity(city);

        rentalCar.setTotalPayment(calculatorTotalPaymentNotExisistsAdditionalService(createRentalCarRequest,rentalCar));

        this.rentalCarDao.save(rentalCar);
        return new SuccessResult("Added : " + rentalCar.getRentalId());
    }

    private double calculatorTotalPaymentNotExisistsAdditionalService(CreateRentalCarRequest createRentalCarRequest,RentalCar rentalCar ){

        double totalPayment = 0;

        double carDailyPrice = this.carService.getByCarId(createRentalCarRequest.getCarId()).getData().getDailyPrice();
        System.out.println(carDailyPrice);
        RentalCarGetDto rentalCarGetDto = this.modelMapperService.forDto().map(rentalCar,RentalCarGetDto.class);

        if(this.orderedAdditionalServiceService.getByRentalCar_RentalId(rentalCar.getRentalId()).getData() == null){
            totalPayment = carDailyPrice * this.orderedAdditionalServiceService.findNoOfDaysBetween(rentalCarGetDto) ;
        }

        return  totalPayment;
    }

    private City updateCity(CreateRentalCarRequest createRentalCarRequest){
        CityGetDto cityGetDto = this.cityService.getByCityPlate(createRentalCarRequest.getCityPlate());
        City city = this.modelMapperService.forDto().map(cityGetDto,City.class);
        return city;
    }

    private void checkIfCarExists(int carId) throws BusinessException {

        DataResult<CarGetDto> result = this.carService.getByCarId(carId);

        if (!result.isSuccess()) {
            throw new BusinessException("The car with this id does not exist");
        }
    }

    private void checkIfCarMaintenanced(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

        List<CarMaintenanceListDto> result = this.carMaintenanceService.getByCar_CarId(createRentalCarRequest.getCarId());

        if (result == null) {
            return;
        }

        for (CarMaintenanceListDto carMaintenance : result) {

            if ((carMaintenance.getReturnDate() != null) && (createRentalCarRequest.getStartingDate().isBefore(carMaintenance.getReturnDate()) || createRentalCarRequest.getEndDate().isBefore(carMaintenance.getReturnDate()))) {
                throw new BusinessException("This car cannot be rented as it is under maintenance.");
            }

            if (carMaintenance.getReturnDate() == null) {
                throw new BusinessException("This car cannot be rented as it is under maintenance. / return date equals null");
            }

        }

    }

    @Override
    public DataResult<RentalCarGetDto> getByRentalId(int rentalId) {

        RentalCar result = this.rentalCarDao.getByRentalId(rentalId);

        if (result == null) {
            return new ErrorDataResult<RentalCarGetDto>("Car with submitted id not found");
        }

        RentalCarGetDto response = this.modelMapperService.forDto().map(result, RentalCarGetDto.class);

        return new SuccessDataResult<RentalCarGetDto>(response, "Success");
    }

    @Override
    public DataResult<List<RentalCarListDto>> getAll() {

        List<RentalCar> result = this.rentalCarDao.findAll();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<RentalCarListDto>>("Rental Cars not listed");
        }

        List<RentalCarListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<RentalCarListDto>>(response, "Rental Cars Listed successfully..");
    }

    @Override
    public DataResult<List<RentalCarListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<RentalCar> result = this.rentalCarDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<RentalCarListDto>>("Rental Cars not listed");
        }

        List<RentalCarListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<RentalCarListDto>>(response, "Rental Cars listed successfully..");
    }

    @Override
    public DataResult<List<RentalCarListDto>> getAllSorted(Direction direction) {

        Sort s = Sort.by(direction, "returnDate");

        List<RentalCar> result = this.rentalCarDao.findAll(s);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<RentalCarListDto>>("Rental Cars not listed");
        }

        List<RentalCarListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<RentalCarListDto>>(response, "Rental Cars listed successfully..");
    }

    @Override
    public List<RentalCarListDto> getByCar_CarId(int carId) {

        List<RentalCar> result = this.rentalCarDao.getByCar_CarId(carId);

        if (result.isEmpty()) {
            return null;
        }

        List<RentalCarListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class)).collect(Collectors.toList());

        return response;
    }

    @Override
    public Result update(int rentalId, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

        RentalCar rentalCar = this.rentalCarDao.getByRentalId(rentalId);

        checkIfParameterIsNull(updateRentalCarRequest, rentalCar);

        RentalCar rentalCarUpdate = this.modelMapperService.forRequest().map(updateRentalCarRequest, RentalCar.class);

        IdCorrector(rentalCar, rentalCarUpdate);

        if(updateRentalCarRequest.getCityPlate() == 0){
            CityGetDto cityGetDto = this.cityService.getByCityPlate(rentalCar.getReturnCity().getCityPlate());
            City city = this.modelMapperService.forDto().map(cityGetDto,City.class);
            rentalCarUpdate.setReturnCity(city);
        }

        if (updateRentalCarRequest.getCityPlate() != 0) {

            CityGetDto cityGetDto = this.cityService.getByCityPlate(updateRentalCarRequest.getCityPlate());
            City city = this.modelMapperService.forDto().map(cityGetDto,City.class);

            rentalCarUpdate.setReturnCity(city);
        }

        this.rentalCarDao.save(rentalCarUpdate);
        return new SuccessResult(rentalCarUpdate.getRentalId() + " updated..");
    }

    private UpdateRentalCarRequest checkIfParameterIsNull(UpdateRentalCarRequest updateRentalCarRequest, RentalCar rentalCar) {

        if (updateRentalCarRequest.getStartingDate() == null) {
            updateRentalCarRequest.setStartingDate(rentalCar.getStartingDate());
        }

        if (updateRentalCarRequest.getEndDate() == null) {
            updateRentalCarRequest.setEndDate(rentalCar.getEndDate());
        }

        return updateRentalCarRequest;
    }

    private void IdCorrector(RentalCar rentalCar, RentalCar rentalCarUpdate) {
        rentalCarUpdate.setCar(rentalCar.getCar());
        rentalCarUpdate.setRentalId(rentalCar.getRentalId());
    }

    @Override
    public boolean checkIfCarAvaliable(int carId) throws BusinessException {

        if (this.carService.getByCarId(carId) == null) {
            throw new BusinessException("Araba yok");
        }
        return true;

    }

    @Override
    public Result delete(DeleteRentalCarRequest deleteRentalCarRequest) throws BusinessException {

        RentalCar rentalCar = this.modelMapperService.forRequest().map(deleteRentalCarRequest, RentalCar.class);

        checkIfRentalCarExists(deleteRentalCarRequest.getRentalId());

        this.rentalCarDao.deleteById(rentalCar.getRentalId());

        return new SuccessResult(deleteRentalCarRequest.getRentalId() + " deleted..");
    }

    @Override
    public boolean checkIfRentalCarExists(int rentalId) throws BusinessException {

        if (this.rentalCarDao.getByRentalId(rentalId) == null) {
            throw new BusinessException("The rental car with this ID is not available..");
        }

        return true;
    }

}
