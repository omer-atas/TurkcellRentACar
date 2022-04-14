package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.api.modals.RentEndDateDelayPostServiceModal;
import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
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
import com.turkcell.rentACar.entities.concretes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private IndividualCustomerService individualCustomerService;
    private CorporateCustomerService corporateCustomerService;
    private InvoiceService invoiceService;
    private PaymentService paymentService;

    @Lazy
    @Autowired
    public RentManager(RentDao rentDao, ModelMapperService modelMapperService, CarMaintenanceService carMaintenanceService, CarService carService, CityService cityService, OrderedAdditionalServiceService orderedAdditionalServiceService, CustomerService customerService, IndividualCustomerService individualCustomerService, CorporateCustomerService corporateCustomerService, InvoiceService invoiceService, PaymentService paymentService) {
        this.rentDao = rentDao;
        this.modelMapperService = modelMapperService;
        this.carMaintenanceService = carMaintenanceService;
        this.carService = carService;
        this.cityService = cityService;
        this.orderedAdditionalServiceService = orderedAdditionalServiceService;
        this.customerService = customerService;
        this.individualCustomerService = individualCustomerService;
        this.corporateCustomerService = corporateCustomerService;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
    }

    @Override
    public int carRentalForIndividualCustomer(CreateRentRequest createRentRequest) throws BusinessException {

        checkIfEndDateBeforeStartDate(createRentRequest.getStartingDate(), createRentRequest.getEndDate());
        checkIfCustomerExists(createRentRequest.getCustomerId());
        checkIfIndividualCustomerExists(createRentRequest.getCustomerId());
        checkIfCarExists(createRentRequest.getCarId());
        checkIfToCityExists(createRentRequest.getToCityId());
        checkIfFromCityExists(createRentRequest.getFromCityId());
        checkIfCarMaintenance(createRentRequest.getCarId(), createRentRequest.getStartingDate(), createRentRequest.getEndDate());

        Rent rent = this.modelMapperService.forRequest().map(createRentRequest, Rent.class);

        rent.setRentId(0);
        rent.setStartingKilometer(this.carService.getByCarId(createRentRequest.getCarId()).getData().getKilometerInformation());
        rent.setReturnKilometer(this.carService.getByCarId(createRentRequest.getCarId()).getData().getKilometerInformation());
        rent = manuelMappingForRentAdd(createRentRequest, rent);
        rent.setRentalPriceOfTheCar(calculatorRentalPriceOfTheCar(rent.getCar().getCarId(), rent.getStartingDate(), rent.getEndDate()));

        this.rentDao.save(rent);

        return rent.getRentId();
    }

    private void checkIfEndDateBeforeStartDate(LocalDate startingDate, LocalDate endDate) throws BusinessException {
        if (endDate.isBefore(startingDate)) {
            throw new BusinessException(BusinessMessages.RENT_END_DATE_BEFORE_STARTING_DATE);
        }
    }

    @Override
    public double calculatorRentalPriceOfTheCar(int carId, LocalDate startingDate, LocalDate endDate) {
        return this.carService.getByCarId(carId).getData().getDailyPrice() * this.orderedAdditionalServiceService.findNoOfDaysBetween(startingDate, endDate);
    }

    @Override
    public void checkIfIndividualCustomerExists(int customerId) throws BusinessException {
        if (this.individualCustomerService.getByIndividualCustomerId(customerId).getData() == null) {
            throw new BusinessException(BusinessMessages.INDIVIDUAL_CUSTOMER_NOT_FOUND);
        }
    }

    @Override
    public int carRentalForCorporateCustomer(CreateRentRequest createRentRequest) throws BusinessException {

        checkIfEndDateBeforeStartDate(createRentRequest.getStartingDate(), createRentRequest.getEndDate());
        checkIfCustomerExists(createRentRequest.getCustomerId());
        checkIfCorporateCustomerExists(createRentRequest.getCustomerId());
        checkIfCarExists(createRentRequest.getCarId());
        checkIfToCityExists(createRentRequest.getToCityId());
        checkIfFromCityExists(createRentRequest.getFromCityId());
        checkIfCarMaintenance(createRentRequest.getCarId(), createRentRequest.getStartingDate(), createRentRequest.getEndDate());

        Rent rent = this.modelMapperService.forRequest().map(createRentRequest, Rent.class);

        rent.setRentId(0);
        rent.setStartingKilometer(this.carService.getByCarId(createRentRequest.getCarId()).getData().getKilometerInformation());
        rent.setReturnKilometer(this.carService.getByCarId(createRentRequest.getCarId()).getData().getKilometerInformation());
        rent = manuelMappingForRentAdd(createRentRequest, rent);
        rent.setRentalPriceOfTheCar(calculatorRentalPriceOfTheCar(rent.getCar().getCarId(), rent.getStartingDate(), rent.getEndDate()));

        this.rentDao.save(rent);

        return rent.getRentId();
    }


    @Override
    public void checkIfCorporateCustomerExists(int customerId) throws BusinessException {

        if (this.corporateCustomerService.getByCorporateCustomerId(customerId).getData() == null) {
            throw new BusinessException(BusinessMessages.CORPORATE_CUSTOMER_NOT_FOUND);
        }

    }

    private Rent manuelMappingForRentAdd(CreateRentRequest createRentRequest, Rent rent) {

        City toCity = getByToCity(createRentRequest.getToCityId());
        rent.setToCity(toCity);

        City fromCity = getByFromCity(createRentRequest.getFromCityId());
        rent.setFromCity(fromCity);

        Customer customer = getByCustomer(createRentRequest.getCustomerId());
        rent.setCustomer(customer);

        return rent;
    }

    @Override
    public void checkIfCustomerExists(int customerId) throws BusinessException {
        if (this.customerService.getByCustomerId(customerId) == null) {
            throw new BusinessException(BusinessMessages.CUSTOMER_NOT_FOUND);
        }
    }

    private void checkIfToCityExists(int toCityId) throws BusinessException {

        if (this.cityService.getByCityPlate(toCityId) == null) {
            throw new BusinessException(BusinessMessages.RENT_TO_CITY_NOT_FOUND);
        }

    }

    private void checkIfFromCityExists(int fromCityId) throws BusinessException {

        if (this.cityService.getByCityPlate(fromCityId) == null) {
            throw new BusinessException(BusinessMessages.RENT_FROM_CITY_NOT_FOUND);
        }

    }

    private Customer getByCustomer(int customerId) {

        CustomerGetDto customerGetDto = this.customerService.getByCustomerId(customerId);
        Customer customer = this.modelMapperService.forDto().map(customerGetDto, Customer.class);

        return customer;
    }

    private City getByFromCity(int fromCityId) {

        CityGetDto cityGetDto = this.cityService.getByCityPlate(fromCityId);
        City city = this.modelMapperService.forDto().map(cityGetDto, City.class);

        return city;
    }

    private City getByToCity(int toCityId) {

        CityGetDto cityGetDto = this.cityService.getByCityPlate(toCityId);
        City city = this.modelMapperService.forDto().map(cityGetDto, City.class);

        return city;
    }

    private void checkIfCarExists(int carId) throws BusinessException {

        DataResult<CarGetDto> result = this.carService.getByCarId(carId);

        if (!result.isSuccess()) {
            throw new BusinessException(BusinessMessages.CAR_NOT_FOUND);
        }
    }

    private void checkIfCarMaintenance(int carId, LocalDate startingDate, LocalDate endDate) throws BusinessException {

        List<CarMaintenanceListDto> result = this.carMaintenanceService.getByCar_CarId(carId).getData();

        if (result == null) {
            return;
        }

        for (CarMaintenanceListDto carMaintenance : result) {

            if ((carMaintenance.getReturnDate() != null) && (startingDate.isBefore(carMaintenance.getReturnDate()) || endDate.isBefore(carMaintenance.getReturnDate()))) {
                throw new BusinessException(BusinessMessages.RENT_CAR_MAINTENANCE);
            }

            if (carMaintenance.getReturnDate() == null) {
                throw new BusinessException(BusinessMessages.RENT_CAR_MAINTENANCE_NULL_DATE);
            }

        }

    }

    @Override
    public DataResult<RentGetDto> getByRentId(int rentId) {

        Rent result = this.rentDao.getByRentId(rentId);

        if (result == null) {
            return new ErrorDataResult<RentGetDto>(BusinessMessages.RENT_NOT_FOUND);
        }

        RentGetDto response = this.modelMapperService.forDto().map(result, RentGetDto.class);

        response = manuelMappingByRentId(result, response);

        return new SuccessDataResult<RentGetDto>(response, BusinessMessages.RENT_GET_BY_ID);
    }

    private RentGetDto manuelMappingByRentId(Rent result, RentGetDto response) {

        response.setCustomerId(result.getCustomer().getCustomerId());
        response.setFromCityId(result.getFromCity().getCityPlate());
        response.setToCityId(result.getToCity().getCityPlate());

        return response;
    }

    @Override
    public DataResult<List<RentListDto>> getAll() {

        List<Rent> result = this.rentDao.findAll();

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        response = manuelMappingForGetAll(result, response);

        return new SuccessDataResult<List<RentListDto>>(response, BusinessMessages.RENT_GET_ALL);
    }

    @Override
    public DataResult<List<RentListDto>> getAllForCorporateCustomer() {

        List<Rent> result = this.rentDao.findAll();

        for (int i = 0; i < result.size(); i++) {
            if (this.corporateCustomerService.getByCorporateCustomerId(result.get(i).getCustomer().getCustomerId()).getData() == null) {
                result.remove(i);
            }
        }

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        response = manuelMappingForGetAll(result, response);


        return new SuccessDataResult<List<RentListDto>>(response, BusinessMessages.RENT_GET_ALL);
    }

    @Override
    public DataResult<List<RentListDto>> getAllForIndividualCustomer() {

        List<Rent> result = this.rentDao.findAll();

        for (int i = 0; i < result.size(); i++) {
            if (this.corporateCustomerService.getByCorporateCustomerId(result.get(i).getCustomer().getCustomerId()).getData() == null) {
                result.remove(i);
            }
        }

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        response = manuelMappingForGetAll(result, response);

        return new SuccessDataResult<List<RentListDto>>(response, BusinessMessages.RENT_GET_ALL);
    }

    private List<RentListDto> manuelMappingForGetAll(List<Rent> result, List<RentListDto> response) {

        for (int i = 0; i < result.size(); i++) {
            response.get(i).setFromCityId(result.get(i).getFromCity().getCityPlate());
            response.get(i).setToCityId(result.get(i).getToCity().getCityPlate());
            response.get(i).setCustomerId(result.get(i).getCustomer().getCustomerId());
            response.get(i).setStartingKilometer(result.get(i).getStartingKilometer());
            response.get(i).setReturnKilometer(result.get(i).getReturnKilometer());
        }

        return response;
    }

    @Override
    public DataResult<List<RentListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Rent> result = this.rentDao.findAll(pageable).getContent();

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        response = manuelMappingForGetAll(result, response);

        return new SuccessDataResult<List<RentListDto>>(response, BusinessMessages.RENT_GET_ALL_PAGED);
    }

    @Override
    public DataResult<List<RentListDto>> getAllSorted(Direction direction) {

        Sort s = Sort.by(direction, "endDate");

        List<Rent> result = this.rentDao.findAll(s);

        List<RentListDto> response = result.stream().map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentListDto.class)).collect(Collectors.toList());

        response = manuelMappingForGetAll(result, response);

        return new SuccessDataResult<List<RentListDto>>(response, BusinessMessages.RENT_GET_ALL_SORTED);
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
    public Result update(int rentId, UpdateRentRequest updateRentRequest) throws BusinessException {

        checkIfRentExists(rentId);

        Rent rent = this.rentDao.getByRentId(rentId);

        Rent rentUpdate = this.modelMapperService.forRequest().map(updateRentRequest, Rent.class);

        rentUpdate = manuelMappingToRentUpdate(rentId, rentUpdate);

        this.carService.updateKilometerInformation(this.rentDao.getByRentId(rentId).getCar().getCarId(), updateRentRequest.getReturnKilometer());

        IdCorrector(rent, rentUpdate);

        this.rentDao.save(rentUpdate);

        return new SuccessResult(rentUpdate.getRentId() + BusinessMessages.RENT_UPDATE);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = BusinessException.class)
    @Override
    public Result updateRentDelayEndDateForIndividualCustomer(int rentId, RentEndDateDelayPostServiceModal rentEndDateDelayPostServiceModal) throws BusinessException {

        checkIfRentExists(rentId);
        checkIfIndividualCustomerExists(this.rentDao.getByRentId(rentId).getCustomer().getCustomerId());

        Rent rent = this.rentDao.getByRentId(rentId);

        checkIfEndDateForRentDelay(rent.getEndDate(), rentEndDateDelayPostServiceModal.getUpdateEndDate());

        this.carService.updateKilometerInformation(this.rentDao.getByRentId(rentId).getCar().getCarId(), rentEndDateDelayPostServiceModal.getReturnKilometer());

        double totalRentalPriceForTheDelayEndDateOfTheCar = calculatorRentalPriceOfTheCar(rent.getCar().getCarId(), rent.getEndDate(), rentEndDateDelayPostServiceModal.getUpdateEndDate());

        double carRentalPrice = rent.getRentalPriceOfTheCar() + totalRentalPriceForTheDelayEndDateOfTheCar;


        this.paymentService.gettingPaidForIndividualCustomerDelayEndDate(rentEndDateDelayPostServiceModal, rentId, totalRentalPriceForTheDelayEndDateOfTheCar);

        rent.setRentalPriceOfTheCar(carRentalPrice);
        rent.setEndDate(rentEndDateDelayPostServiceModal.getUpdateEndDate());

        return new SuccessResult(BusinessMessages.RENT_DELAY_END_DATE_FOR_INDIVIDUAL_CUSTOMER);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = BusinessException.class)
    @Override
    public Result updateRentDelayEndDateForCorporateCustomer(int rentId, RentEndDateDelayPostServiceModal rentEndDateDelayPostServiceModal) throws BusinessException {

        checkIfCustomerExists(this.rentDao.getByRentId(rentId).getCustomer().getCustomerId());
        checkIfCorporateCustomerExists(this.rentDao.getByRentId(rentId).getCustomer().getCustomerId());

        Rent rent = this.rentDao.getByRentId(rentId);

        checkIfEndDateForRentDelay(rent.getEndDate(), rentEndDateDelayPostServiceModal.getUpdateEndDate());

        this.carService.updateKilometerInformation(this.rentDao.getByRentId(rentId).getCar().getCarId(), rentEndDateDelayPostServiceModal.getReturnKilometer());

        double totalRentalPriceForTheDelayEndDateOfTheCar = calculatorRentalPriceOfTheCar(rent.getCar().getCarId(), rent.getEndDate(), rentEndDateDelayPostServiceModal.getUpdateEndDate());

        double carRentalPrice = rent.getRentalPriceOfTheCar() + totalRentalPriceForTheDelayEndDateOfTheCar;

        this.paymentService.gettingPaidForCorporateCustomerDelayEndDate(rentEndDateDelayPostServiceModal, rentId, totalRentalPriceForTheDelayEndDateOfTheCar);

        rent.setRentalPriceOfTheCar(carRentalPrice);
        rent.setEndDate(rentEndDateDelayPostServiceModal.getUpdateEndDate());

        return new SuccessResult(BusinessMessages.RENT_DELAY_END_DATE_FOR_CORPORATE_CUSTOMER);
    }

    private void checkIfEndDateForRentDelay(LocalDate endDate, LocalDate updateEndDate) throws BusinessException {

        System.out.println(endDate);
        System.out.println(updateEndDate);

        if (updateEndDate.isBefore(endDate) || updateEndDate.equals(endDate)) {
            throw new BusinessException(BusinessMessages.RENT_DELAY_END_DATE_CHECK);
        }
    }


    private Rent manuelMappingToRentUpdate(int rentId, Rent rentUpdate) {
        rentUpdate.setToCity(this.rentDao.getByRentId(rentId).getToCity());
        rentUpdate.setFromCity(this.rentDao.getByRentId(rentId).getFromCity());
        rentUpdate.setRentalPriceOfTheCar(this.rentDao.getByRentId(rentId).getRentalPriceOfTheCar());
        rentUpdate.setStartingKilometer(this.rentDao.getByRentId(rentId).getStartingKilometer());

        return rentUpdate;
    }

    private void IdCorrector(Rent rent, Rent rentUpdate) {

        rentUpdate.setRentId(rent.getRentId());
        rentUpdate.setCar(rent.getCar());
        rentUpdate.setCustomer(rent.getCustomer());

    }

    @Override
    public Result delete(DeleteRentRequest deleteRentalCarRequest) throws BusinessException {

        checkIfRentExists(deleteRentalCarRequest.getRentId());
        checkIfThereIsOrderedAdditionalServiceWithThisRent(deleteRentalCarRequest.getRentId());
        checkIfThereIsInvoiceWithThisRent(deleteRentalCarRequest.getRentId());

        Rent rentalCar = this.modelMapperService.forRequest().map(deleteRentalCarRequest, Rent.class);

        this.rentDao.deleteById(rentalCar.getRentId());

        return new SuccessResult(deleteRentalCarRequest.getRentId() + BusinessMessages.RENT_DELETE);
    }

    private void checkIfThereIsInvoiceWithThisRent(int rentId) throws BusinessException {
        if (!this.invoiceService.getByRent_RentId(rentId).getData().isEmpty()) {
            throw new BusinessException(BusinessMessages.IS_THE_INVOICE_AVAILABLE_FOR_THIS_RENTAL);
        }
    }

    private void checkIfThereIsOrderedAdditionalServiceWithThisRent(int rentId) throws BusinessException {

        if (!this.orderedAdditionalServiceService.getByRent_RentId(rentId).getData().isEmpty()) {
            throw new BusinessException(BusinessMessages.IS_THE_ORDERED_ADDITIONAL_SERVICE_AVAILABLE_FOR_THIS_RENTAL);
        }
    }

    @Override
    public boolean checkIfRentExists(int rentalId) throws BusinessException {

        if (this.rentDao.getByRentId(rentalId) == null) {
            throw new BusinessException(BusinessMessages.RENT_NOT_FOUND);
        }

        return true;
    }

}
