package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.dtos.carDtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.dtos.cityDtos.CityGetDto;
import com.turkcell.rentACar.business.dtos.customerDtos.CustomerGetDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentListDto;
import com.turkcell.rentACar.business.request.rentRequests.CreateRentRequest;
import com.turkcell.rentACar.business.request.rentRequests.DeleteRentRequest;
import com.turkcell.rentACar.business.request.rentRequests.UpdateRentRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.RentDao;
import com.turkcell.rentACar.entities.concretes.City;
import com.turkcell.rentACar.entities.concretes.Customer;
import com.turkcell.rentACar.entities.concretes.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentManager implements RentService {

    private RentDao rentDao;
    private ModelMapperService modelMapperService;
    private CarMaintenanceService carMaintenanceService;
    private CarService carService;
    private CityService cityService;
    private OrderedAdditionalServiceService orderedAdditionalServiceService;
    private CustomerService customerService;

    @Lazy
    @Autowired
    public RentManager(RentDao rentDao, ModelMapperService modelMapperService, CarMaintenanceService carMaintenanceService, CarService carService,
                       CityService cityService, OrderedAdditionalServiceService orderedAdditionalServiceService,
                       CustomerService customerService) {
        this.rentDao = rentDao;
        this.modelMapperService = modelMapperService;
        this.carMaintenanceService = carMaintenanceService;
        this.carService = carService;
        this.cityService = cityService;
        this.orderedAdditionalServiceService = orderedAdditionalServiceService;
        this.customerService = customerService;
    }

    @Override
    public Result add(CreateRentRequest createRentRequest) throws BusinessException {

        checkIfCustomerExists(createRentRequest.getCustomerId());
        checkIfCarExists(createRentRequest.getCarId());
        checkIfToCityExists(createRentRequest.getToCityId());
        checkIfFromCityExists(createRentRequest.getFromCityId());

        checkIfCarMaintenance(createRentRequest.getCarId(),createRentRequest.getStartingDate(),createRentRequest.getEndDate());

        Rent rent = this.modelMapperService.forRequest().map(createRentRequest, Rent.class);
        RentGetDto rentGetDto = this.modelMapperService.forDto().map(rent,RentGetDto.class);

        rent.setRentId(0);
        rent.setTotalRentalDays(this.orderedAdditionalServiceService.findNoOfDaysBetween(rentGetDto));

        rent = manuelMappingForRentAdd(createRentRequest,rent);

        rent.setTotalPayment(calculatorTotalPaymentNotExisistsAdditionalService(createRentRequest.getCarId(),rent));

        this.rentDao.save(rent);

        return new SuccessResult("Added : " + rent.getRentId());
    }

    private Rent manuelMappingForRentAdd(CreateRentRequest createRentRequest,Rent rent){

        City toCity = getByToCity(createRentRequest.getToCityId());
        rent.setToCity(toCity);

        City fromCity = geyByFromCity(createRentRequest.getFromCityId());
        rent.setFromCity(fromCity);

        Customer customer = getByCustomer(createRentRequest.getCustomerId());
        rent.setCustomer(customer);

        return rent;
    }

    private void checkIfCustomerExists(int customerId) throws BusinessException {
        if(this.customerService.getByCustomerId(customerId) == null){
            throw new BusinessException("The customer with this ID is not available");
        }
    }

    private void checkIfToCityExists(int toCityId) throws BusinessException {

        if(this.cityService.getByCityPlate(toCityId) == null){
            throw new BusinessException("The city with this ID is not available - toCity");
        }

    }

    private void checkIfFromCityExists(int fromCityId) throws BusinessException {

        if(this.cityService.getByCityPlate(fromCityId) == null){
            throw new BusinessException("The city with this ID is not available - fromCity");
        }

    }

    private Customer getByCustomer(int customerId){

        CustomerGetDto customerGetDto = this.customerService.getByCustomerId(customerId);
        Customer customer = this.modelMapperService.forDto().map(customerGetDto,Customer.class);

        return customer;
    }

    private City geyByFromCity(int fromCityId){

        CityGetDto cityGetDto = this.cityService.getByCityPlate(fromCityId);
        City city = this.modelMapperService.forDto().map(cityGetDto,City.class);

        return city;
    }

    private City getByToCity(int toCityId){

        CityGetDto cityGetDto = this.cityService.getByCityPlate(toCityId);
        City city = this.modelMapperService.forDto().map(cityGetDto,City.class);

        return city;
    }

    private double calculatorTotalPaymentNotExisistsAdditionalService(int carId, Rent rentalCar ){

        double totalPayment = 0;

        double carDailyPrice = this.carService.getByCarId(carId).getData().getDailyPrice();

        RentGetDto rentGetDto = this.modelMapperService.forDto().map(rentalCar, RentGetDto.class);

        if(this.orderedAdditionalServiceService.getByRent_RentId(rentalCar.getRentId()).getData() == null){
            totalPayment = carDailyPrice * this.orderedAdditionalServiceService.findNoOfDaysBetween(rentGetDto) ;
        }

        return  totalPayment;
    }



    private void checkIfCarExists(int carId) throws BusinessException {

        DataResult<CarGetDto> result = this.carService.getByCarId(carId);

        if (!result.isSuccess()) {
            throw new BusinessException("The car with this id does not exist");
        }
    }

    private void checkIfCarMaintenance(int carId, LocalDate startingDate, LocalDate endDate) throws BusinessException {

        List<CarMaintenanceListDto> result = this.carMaintenanceService.getByCar_CarId(carId);

        if (result == null) {
            return;
        }

        for (CarMaintenanceListDto carMaintenance : result) {

            if ((carMaintenance.getReturnDate() != null) && (startingDate.isBefore(carMaintenance.getReturnDate()) || endDate.isBefore(carMaintenance.getReturnDate()))) {
                throw new BusinessException("This car cannot be rented as it is under maintenance.");
            }

            if (carMaintenance.getReturnDate() == null) {
                throw new BusinessException("This car cannot be rented as it is under maintenance. / return date equals null");
            }

        }

    }

    @Override
    public DataResult<RentGetDto> getByRentId(int rentId) {

        Rent result = this.rentDao.getByRentId(rentId);

        if (result == null) {
            return new ErrorDataResult<RentGetDto>("Car with submitted id not found");
        }

        RentGetDto response = this.modelMapperService.forDto().map(result, RentGetDto.class);

        response = manuelMappingByRentId(result,response);

        return new SuccessDataResult<RentGetDto>(response, "Success");
    }

    private RentGetDto manuelMappingByRentId(Rent result,RentGetDto response){

        response.setCustomerId(result.getCustomer().getCustomerId());
        response.setFromCityId(result.getFromCity().getCityPlate());
        response.setToCityId(result.getToCity().getCityPlate());

        return response;
    }

    @Override
    public DataResult<List<RentListDto>> getAll() {

        List<Rent> result = this.rentDao.findAll();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<RentListDto>>("Rental Cars not listed");
        }

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        response = manuelMappingForGetAll(result,response);

        return new SuccessDataResult<List<RentListDto>>(response, "Rental Cars Listed successfully..");
    }

    private List<RentListDto> manuelMappingForGetAll(List<Rent> result,List<RentListDto> response){

        for (int i= 0 ; i < result.size() ; i++){
            response.get(i).setFromCityId(result.get(i).getFromCity().getCityPlate());
            response.get(i).setToCityId(result.get(i).getToCity().getCityPlate());
            response.get(i).setCustomerId(result.get(i).getCustomer().getCustomerId());
        }

        return response;
    }

    @Override
    public DataResult<List<RentListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Rent> result = this.rentDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<RentListDto>>("Rental Cars not listed");
        }

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<RentListDto>>(response, "Rental Cars listed successfully..");
    }

    @Override
    public DataResult<List<RentListDto>> getAllSorted(Direction direction) {

        Sort s = Sort.by(direction, "returnDate");

        List<Rent> result = this.rentDao.findAll(s);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<RentListDto>>("Rental Cars not listed");
        }

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<RentListDto>>(response, "Rental Cars listed successfully..");
    }

    @Override
    public List<RentListDto> getByCar_CarId(int carId) {

        List<Rent> result = this.rentDao.getByCar_CarId(carId);

        if (result.isEmpty()) {
            return null;
        }

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        return response;
    }

    @Override
    public Result update(int rentalId, UpdateRentRequest updateRentRequest) throws BusinessException {

        Rent rent = this.rentDao.getByRentId(rentalId);

        checkIfParameterIsNull(updateRentRequest, rent);

        Rent rentUpdate = this.modelMapperService.forRequest().map(updateRentRequest, Rent.class);

        IdCorrector(rent, rentUpdate);

        rentUpdate = updateCity(updateRentRequest.getToCityId(),updateRentRequest.getFromCityId(),rentUpdate,rent);

        this.rentDao.save(rentUpdate);

        return new SuccessResult(rentUpdate.getRentId() + " updated..");
    }

    private Rent updateCity(int toCityId, int fromCityId, Rent rentUpdate,Rent rent ){

        if(toCityId == 0){
            CityGetDto cityGetDto = this.cityService.getByCityPlate(rent.getToCity().getCityPlate());
            City toCity = this.modelMapperService.forDto().map(cityGetDto,City.class);
            rentUpdate.setToCity(toCity);
        }

        if (toCityId != 0) {

            CityGetDto cityGetDto = this.cityService.getByCityPlate(toCityId);
            City toCity = this.modelMapperService.forDto().map(cityGetDto,City.class);

            rentUpdate.setToCity(toCity);
        }

        if(fromCityId == 0){
            CityGetDto cityGetDto = this.cityService.getByCityPlate(rent.getToCity().getCityPlate());
            City fromCity = this.modelMapperService.forDto().map(cityGetDto,City.class);
            rentUpdate.setFromCity(fromCity);
        }

        if (fromCityId != 0) {

            CityGetDto cityGetDto = this.cityService.getByCityPlate(fromCityId);
            City fromCity = this.modelMapperService.forDto().map(cityGetDto,City.class);

            rentUpdate.setToCity(fromCity);
        }

        return rentUpdate;
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

    private void IdCorrector(Rent rent, Rent rentUpdate) {

        rentUpdate.setRentId(rent.getRentId());
        rentUpdate.setCar(rent.getCar());
        rentUpdate.setCustomer(rent.getCustomer());

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

        checkIfRentalCarExists(deleteRentalCarRequest.getRentId());

        Rent rentalCar = this.modelMapperService.forRequest().map(deleteRentalCarRequest, Rent.class);

        this.rentDao.deleteById(rentalCar.getRentId());

        return new SuccessResult(deleteRentalCarRequest.getRentId() + " deleted..");
    }

    @Override
    public boolean checkIfRentalCarExists(int rentalId) throws BusinessException {

        if (this.rentDao.getByRentId(rentalId) == null) {
            throw new BusinessException("The rental car with this ID is not available..");
        }

        return true;
    }

}
