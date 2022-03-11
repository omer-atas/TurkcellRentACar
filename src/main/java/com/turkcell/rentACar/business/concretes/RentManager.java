package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.dtos.carDtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.dtos.cityDtos.CityGetDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentListDto;
import com.turkcell.rentACar.business.request.rentRequests.CreateRentRequest;
import com.turkcell.rentACar.business.request.rentRequests.DeleteRentRequest;
import com.turkcell.rentACar.business.request.rentRequests.UpdateRentRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.RentrDao;
import com.turkcell.rentACar.entities.concretes.City;
import com.turkcell.rentACar.entities.concretes.Rent;
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
public class RentManager implements RentService {

    private RentrDao rentrDao;
    private ModelMapperService modelMapperService;
    private CarMaintenanceService carMaintenanceService;
    private CarService carService;
    private CityService cityService;
    private OrderedAdditionalServiceService orderedAdditionalServiceService;

    @Lazy
    @Autowired
    public RentManager(RentrDao rentrDao, ModelMapperService modelMapperService, CarMaintenanceService carMaintenanceService, CarService carService, CityService cityService, OrderedAdditionalServiceService orderedAdditionalServiceService) {
        this.rentrDao = rentrDao;
        this.modelMapperService = modelMapperService;
        this.carMaintenanceService = carMaintenanceService;
        this.carService = carService;
        this.cityService = cityService;
        this.orderedAdditionalServiceService = orderedAdditionalServiceService;
    }

    @Override
    public Result add(CreateRentRequest createRentalCarRequest) throws BusinessException {

        Rent rentalCar = this.modelMapperService.forRequest().map(createRentalCarRequest, Rent.class);
        rentalCar.setRentId(0);

        checkIfCarExists(rentalCar.getCar().getCarId());
        checkIfCarMaintenanced(createRentalCarRequest);

        City city = updateCity(createRentalCarRequest);
        rentalCar.setReturnCity(city);

        rentalCar.setTotalPayment(calculatorTotalPaymentNotExisistsAdditionalService(createRentalCarRequest,rentalCar));

        this.rentrDao.save(rentalCar);
        return new SuccessResult("Added : " + rentalCar.getRentId());
    }

    private double calculatorTotalPaymentNotExisistsAdditionalService(CreateRentRequest createRentalCarRequest, Rent rentalCar ){

        double totalPayment = 0;

        double carDailyPrice = this.carService.getByCarId(createRentalCarRequest.getCarId()).getData().getDailyPrice();

        RentGetDto rentGetDto = this.modelMapperService.forDto().map(rentalCar, RentGetDto.class);

        if(this.orderedAdditionalServiceService.getByRent_RentId(rentalCar.getRentId()).getData() == null){
            totalPayment = carDailyPrice * this.orderedAdditionalServiceService.findNoOfDaysBetween(rentGetDto) ;
        }

        return  totalPayment;
    }

    private City updateCity(CreateRentRequest createRentalCarRequest){
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

    private void checkIfCarMaintenanced(CreateRentRequest createRentalCarRequest) throws BusinessException {

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
    public DataResult<RentGetDto> getByRentId(int rentId) {

        Rent result = this.rentrDao.getByRentId(rentId);

        if (result == null) {
            return new ErrorDataResult<RentGetDto>("Car with submitted id not found");
        }

        RentGetDto response = this.modelMapperService.forDto().map(result, RentGetDto.class);

        return new SuccessDataResult<RentGetDto>(response, "Success");
    }

    @Override
    public DataResult<List<RentListDto>> getAll() {

        List<Rent> result = this.rentrDao.findAll();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<RentListDto>>("Rental Cars not listed");
        }

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<RentListDto>>(response, "Rental Cars Listed successfully..");
    }

    @Override
    public DataResult<List<RentListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Rent> result = this.rentrDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<RentListDto>>("Rental Cars not listed");
        }

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<RentListDto>>(response, "Rental Cars listed successfully..");
    }

    @Override
    public DataResult<List<RentListDto>> getAllSorted(Direction direction) {

        Sort s = Sort.by(direction, "returnDate");

        List<Rent> result = this.rentrDao.findAll(s);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<RentListDto>>("Rental Cars not listed");
        }

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<RentListDto>>(response, "Rental Cars listed successfully..");
    }

    @Override
    public List<RentListDto> getByCar_CarId(int carId) {

        List<Rent> result = this.rentrDao.getByCar_CarId(carId);

        if (result.isEmpty()) {
            return null;
        }

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        return response;
    }

    @Override
    public Result update(int rentalId, UpdateRentRequest updateRentRequest) throws BusinessException {

        Rent rentalCar = this.rentrDao.getByRentId(rentalId);

        checkIfParameterIsNull(updateRentRequest, rentalCar);

        Rent rentalCarUpdate = this.modelMapperService.forRequest().map(updateRentRequest, Rent.class);

        IdCorrector(rentalCar, rentalCarUpdate);

        if(updateRentRequest.getCityPlate() == 0){
            CityGetDto cityGetDto = this.cityService.getByCityPlate(rentalCar.getReturnCity().getCityPlate());
            City city = this.modelMapperService.forDto().map(cityGetDto,City.class);
            rentalCarUpdate.setReturnCity(city);
        }

        if (updateRentRequest.getCityPlate() != 0) {

            CityGetDto cityGetDto = this.cityService.getByCityPlate(updateRentRequest.getCityPlate());
            City city = this.modelMapperService.forDto().map(cityGetDto,City.class);

            rentalCarUpdate.setReturnCity(city);
        }

        this.rentrDao.save(rentalCarUpdate);
        return new SuccessResult(rentalCarUpdate.getRentId() + " updated..");
    }

    private UpdateRentRequest checkIfParameterIsNull(UpdateRentRequest updateRentRequest, Rent rentalCar) {

        if (updateRentRequest.getStartingDate() == null) {
            updateRentRequest.setStartingDate(rentalCar.getStartingDate());
        }

        if (updateRentRequest.getEndDate() == null) {
            updateRentRequest.setEndDate(rentalCar.getEndDate());
        }

        return updateRentRequest;
    }

    private void IdCorrector(Rent rentalCar, Rent rentalCarUpdate) {
        rentalCarUpdate.setCar(rentalCar.getCar());
        rentalCarUpdate.setRentId(rentalCar.getRentId());
    }

    @Override
    public boolean checkIfCarAvaliable(int carId) throws BusinessException {

        if (this.carService.getByCarId(carId) == null) {
            throw new BusinessException("Araba yok");
        }
        return true;

    }

    @Override
    public Result delete(DeleteRentRequest deleteRentalCarRequest) throws BusinessException {

        Rent rentalCar = this.modelMapperService.forRequest().map(deleteRentalCarRequest, Rent.class);

        checkIfRentalCarExists(deleteRentalCarRequest.getRentId());

        this.rentrDao.deleteById(rentalCar.getRentId());

        return new SuccessResult(deleteRentalCarRequest.getRentId() + " deleted..");
    }

    @Override
    public boolean checkIfRentalCarExists(int rentalId) throws BusinessException {

        if (this.rentrDao.getByRentId(rentalId) == null) {
            throw new BusinessException("The rental car with this ID is not available..");
        }

        return true;
    }

}
