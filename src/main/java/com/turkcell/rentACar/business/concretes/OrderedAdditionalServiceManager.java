package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.DeleteOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.rentRequests.UpdateRentRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.OrderedAdditionalServiceDao;
import com.turkcell.rentACar.entities.concretes.OrderedAdditionalService;
import com.turkcell.rentACar.entities.concretes.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderedAdditionalServiceManager implements OrderedAdditionalServiceService {

    private OrderedAdditionalServiceDao orderedAdditionalServiceDao;
    private ModelMapperService modelMapperService;
    private AdditionalServiceService additionalServiceService;
    private RentService rentService;
    private CarService carService;

    @Autowired
    public OrderedAdditionalServiceManager(OrderedAdditionalServiceDao orderedAdditionalServiceDao, ModelMapperService modelMapperService, AdditionalServiceService additionalServiceService, RentService rentService, CarService carService) {
        this.orderedAdditionalServiceDao = orderedAdditionalServiceDao;
        this.modelMapperService = modelMapperService;
        this.additionalServiceService = additionalServiceService;
        this.rentService = rentService;
        this.carService = carService;
    }

    @Override
    public Result add(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) throws BusinessException {

        checkIfAdditionalServiceExists(createOrderedAdditionalServiceRequest.getAdditionalServiceId());
        checkIfRentExists(createOrderedAdditionalServiceRequest.getRentId());

        OrderedAdditionalService orderedAdditionalService = this.modelMapperService.forRequest().map(createOrderedAdditionalServiceRequest, OrderedAdditionalService.class);
        orderedAdditionalService.setOrderedAdditionalServiceId(0);

        Rent rent = getByRent(createOrderedAdditionalServiceRequest.getRentId());
        orderedAdditionalService.setRent(rent);

        this.orderedAdditionalServiceDao.save(orderedAdditionalService);

        calculateTotalPayment(createOrderedAdditionalServiceRequest);

        return new SuccessResult("OrderedAdditionalService added : " + orderedAdditionalService.getOrderedAdditionalServiceId());
    }

    private Rent getByRent(int rentId){

        RentGetDto rentGetDto = this.rentService.getByRentId(rentId).getData();
        Rent rent = this.modelMapperService.forDto().map(rentGetDto, Rent.class);
        return rent;
    }


    private boolean checkIfSameCity(Rent rent) {

        if(this.rentService.getByRentId(rent.getRentId()).getData().getFromCityId() == this.rentService.getByRentId(rent.getRentId()).getData().getToCityId()){
            return true;
        }
        return  false;
    }

    private double sumOfAdditionalServicesPrice(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest){

        double totalAdditionalServicesPrice = 0;

        List<OrderedAdditionalService> orderedAdditionalServices= this.orderedAdditionalServiceDao.getByRent_RentId(createOrderedAdditionalServiceRequest.getRentId());

        if(orderedAdditionalServices != null){
            for (OrderedAdditionalService o : orderedAdditionalServices) {
                totalAdditionalServicesPrice += o.getAdditionalService().getDailyPrice();
            }
        }

        totalAdditionalServicesPrice += this.additionalServiceService.getByAdditionalServiceId(createOrderedAdditionalServiceRequest.getAdditionalServiceId()).getData().getDailyPrice();

        return  totalAdditionalServicesPrice;
    }

    private void calculateTotalPayment(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) throws BusinessException {

        double rentedDailyPrice = 0 , totalAdditionalServicesPrice = 0 ,totalpayment, citySwapPrice = 750.00;

        totalAdditionalServicesPrice += sumOfAdditionalServicesPrice(createOrderedAdditionalServiceRequest);

        Rent rent = getByRent(createOrderedAdditionalServiceRequest.getRentId());
        RentGetDto rentGetDto = this.modelMapperService.forDto().map(rent, RentGetDto.class);

        rentedDailyPrice += this.carService.getByCarId(this.rentService.getByRentId(createOrderedAdditionalServiceRequest.getRentId()).getData().getCarId()).getData().getDailyPrice();

        totalpayment = rentedDailyPrice + totalAdditionalServicesPrice;

        if (checkIfSameCity(rent)) {
            totalpayment = (totalpayment * findNoOfDaysBetween(rentGetDto));
        } else {
            totalpayment = totalpayment * findNoOfDaysBetween(rentGetDto) + citySwapPrice;
        }

        updateRentalCarTotalPaymnet(createOrderedAdditionalServiceRequest.getRentId(),rent,totalpayment);

    }

    private void updateRentalCarTotalPaymnet(int rentalCarId, Rent rent, double totalpayment) throws BusinessException {

        UpdateRentRequest updateRentRequest = new UpdateRentRequest();

        updateRentRequest.setEndDate(rent.getEndDate());
        updateRentRequest.setStartingDate(rent.getStartingDate());
        updateRentRequest.setTotalPayment(totalpayment);
        updateRentRequest.setToCityId(rent.getToCity().getCityPlate());

        this.rentService.update(rentalCarId, updateRentRequest);
    }

    @Override
    public double findNoOfDaysBetween(RentGetDto rentGetDto) {

        Rent rent = this.modelMapperService.forDto().map(rentGetDto, Rent.class);
        long noOfDaysBetween = ChronoUnit.DAYS.between(rent.getStartingDate(), rent.getEndDate());

        return (double) noOfDaysBetween;
    }


    private void checkIfAdditionalServiceExists(int additionalServiceId) throws BusinessException {

        if (this.additionalServiceService.getByAdditionalServiceId(additionalServiceId).getData() == null) {
            throw new BusinessException("There is no additional service corresponding to the sent id");
        }

    }

    private void checkIfRentExists(int rentalCarId) throws BusinessException {

        if (this.rentService.getByRentId(rentalCarId).getData() == null) {
            throw new BusinessException("There is no rental car corresponding to the sent id");
        }

    }

    @Override
    public DataResult<List<OrderedAdditionalServiceListDto>> getAll() {

        List<OrderedAdditionalService> result = this.orderedAdditionalServiceDao.findAll();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<OrderedAdditionalServiceListDto>>("OrderedAdditionalService not listed");
        }

        List<OrderedAdditionalServiceListDto> response = result.stream().map(orderedAdditionalService -> this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<OrderedAdditionalServiceListDto>>(response, "Success");
    }

    @Override
    public DataResult<List<OrderedAdditionalServiceListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<OrderedAdditionalService> result = this.orderedAdditionalServiceDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<OrderedAdditionalServiceListDto>>("OrderedAdditionalService not list - getAllPaged - ");
        }

        List<OrderedAdditionalServiceListDto> response = result.stream().map(car -> this.modelMapperService.forDto().map(car, OrderedAdditionalServiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<OrderedAdditionalServiceListDto>>(response, "OrderedAdditionalService Listed Successfully");
    }

    @Override
    public DataResult<List<OrderedAdditionalServiceListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "orderedAdditionalServiceId");

        List<OrderedAdditionalService> result = this.orderedAdditionalServiceDao.findAll(s);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<OrderedAdditionalServiceListDto>>("OrderedAdditionalService not list - getAllSorted -");
        }

        List<OrderedAdditionalServiceListDto> response = result.stream().map(orderedAdditionalService -> this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<OrderedAdditionalServiceListDto>>(response);
    }

    @Override
    public DataResult<OrderedAdditionalServiceGetDto> getByOrderedAdditionalServiceId(int orderedAdditionalServiceId) {

        OrderedAdditionalService result = this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId);

        if (result == null) {
            return new ErrorDataResult<OrderedAdditionalServiceGetDto>("OrderedAdditionalService not found");
        }

        OrderedAdditionalServiceGetDto response = this.modelMapperService.forDto().map(result, OrderedAdditionalServiceGetDto.class);

        return new SuccessDataResult<OrderedAdditionalServiceGetDto>(response, "Success");
    }

    @Override
    public DataResult<List<OrderedAdditionalServiceListDto>> getByRent_RentId(int rentId) {

        List<OrderedAdditionalService> result = this.orderedAdditionalServiceDao.getByRent_RentId(rentId);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<OrderedAdditionalServiceListDto>>("OrderedAdditionalService not found");
        }

        List<OrderedAdditionalServiceListDto> response = result.stream().map(orderedAdditionalService -> this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceListDto.class)).collect(Collectors.toList());


        return new SuccessDataResult<List<OrderedAdditionalServiceListDto>>(response, "Success");
    }

    @Override
    public Result update(int orderedAdditionalServiceId, UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalServiceRequest) throws BusinessException {

        checkIfAdditionalServiceExists(updateOrderedAdditionalServiceRequest.getAdditionalServiceId());

        OrderedAdditionalService orderedAdditionalService = this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId);

        OrderedAdditionalService orderedAdditionalServiceUpdate = this.modelMapperService.forRequest().map(updateOrderedAdditionalServiceRequest, OrderedAdditionalService.class);

        IdCorrector(orderedAdditionalService, orderedAdditionalServiceUpdate);

        this.orderedAdditionalServiceDao.save(orderedAdditionalServiceUpdate);

        return new SuccessResult(orderedAdditionalServiceUpdate.getOrderedAdditionalServiceId() + " updated..");
    }

    private void IdCorrector(OrderedAdditionalService orderedAdditionalService, OrderedAdditionalService orderedAdditionalServiceUpdate) {
        orderedAdditionalServiceUpdate.setOrderedAdditionalServiceId(orderedAdditionalService.getOrderedAdditionalServiceId());
    }


    @Override
    public Result delete(DeleteOrderedAdditionalServiceRequest deleteOrderedAdditionalServiceRequest) throws BusinessException {

        checkIfOrderedAdditionalServiceExists(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId());

        OrderedAdditionalService orderedAdditionalService = this.modelMapperService.forRequest().map(deleteOrderedAdditionalServiceRequest, OrderedAdditionalService.class);

        extractionOfAdditionalServicesPrice(deleteOrderedAdditionalServiceRequest);

        this.orderedAdditionalServiceDao.deleteById(orderedAdditionalService.getOrderedAdditionalServiceId());

        return new SuccessResult(orderedAdditionalService.getOrderedAdditionalServiceId() + " deleted..");
    }

    private void extractionOfAdditionalServicesPrice(DeleteOrderedAdditionalServiceRequest deleteOrderedAdditionalServiceRequest) throws BusinessException {

        double totalpayment = 0;

        Rent rent = this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId()).getRent();
        RentGetDto rentGetDto = this.modelMapperService.forDto().map(rent, RentGetDto.class);

        double findNoOfDaysBetween  = findNoOfDaysBetween(rentGetDto);

        totalpayment = (this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId()).getRent().getTotalPayment())  -  (this.orderedAdditionalServiceDao.
                getByOrderedAdditionalServiceId(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId()).getAdditionalService().getDailyPrice())*(findNoOfDaysBetween);

        Rent rentUpdate = getByRent(this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId()).getRent().getRentId());

        updateRentalCarTotalPaymnet( this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId()).getRent().getRentId() ,rentUpdate,totalpayment);

    }

    private void checkIfOrderedAdditionalServiceExists(int orderedAdditionalServiceId) throws BusinessException {

        if (this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId) == null) {
            throw new BusinessException("There is no data in the id sent");
        }
    }
}
