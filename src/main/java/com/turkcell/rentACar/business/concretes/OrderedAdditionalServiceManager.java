package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarGetDto;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.DeleteOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.rentalCarRequests.UpdateRentalCarRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.OrderedAdditionalServiceDao;
import com.turkcell.rentACar.entities.concretes.OrderedAdditionalService;
import com.turkcell.rentACar.entities.concretes.RentalCar;
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

    private final OrderedAdditionalServiceDao orderedAdditionalServiceDao;
    private final ModelMapperService modelMapperService;
    private final AdditionalServiceService additionalServiceService;
    private final RentalCarService rentalCarService;
    private final CarService carService;

    @Autowired
    public OrderedAdditionalServiceManager(OrderedAdditionalServiceDao orderedAdditionalServiceDao, ModelMapperService modelMapperService, AdditionalServiceService additionalServiceService, RentalCarService rentalCarService, CarService carService) {
        this.orderedAdditionalServiceDao = orderedAdditionalServiceDao;
        this.modelMapperService = modelMapperService;
        this.additionalServiceService = additionalServiceService;
        this.rentalCarService = rentalCarService;
        this.carService = carService;
    }

    @Override
    public Result add(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) throws BusinessException {

        checkIfAdditionalServiceExists(createOrderedAdditionalServiceRequest.getAdditionalServiceId());
        checkIfRentalCarExists(createOrderedAdditionalServiceRequest.getRentalId());

        OrderedAdditionalService orderedAdditionalService = this.modelMapperService.forRequest().map(createOrderedAdditionalServiceRequest, OrderedAdditionalService.class);

        RentalCar rentalCar = checkIRentalCarExists(createOrderedAdditionalServiceRequest.getRentalId());
        orderedAdditionalService.setRentalCar(rentalCar);

        this.orderedAdditionalServiceDao.save(orderedAdditionalService);
        calculateTotalPayment(createOrderedAdditionalServiceRequest);
        return new SuccessResult("OrderedAdditionalService added : " + orderedAdditionalService.getOrderedAdditionalServiceId());
    }

    private RentalCar checkIRentalCarExists(int rentalCarID){

        RentalCarGetDto rentalCarGetDto = this.rentalCarService.getByRentalId(rentalCarID).getData();
        RentalCar rentalCar = this.modelMapperService.forDto().map(rentalCarGetDto, RentalCar.class);
        return rentalCar;
    }


    private boolean checkIfSameCity(RentalCar rentalCar) {
        if(this.rentalCarService.getByRentalId(rentalCar.getRentalId()).getData().getCityPlate() == this.carService.getByCarId(this.rentalCarService.getByRentalId(rentalCar.getRentalId()).getData().getCarId()).getData().getCityPlate()){
            return true;
        }
        return  false;
    }

    private double sumOfAdditionalServicesPrice(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest){

        double totalAdditionalServicesPrice = 0;

        for (OrderedAdditionalService o : this.orderedAdditionalServiceDao.getByRentalCar_RentalId(createOrderedAdditionalServiceRequest.getRentalId())) {
                totalAdditionalServicesPrice += o.getAdditionalService().getDailyPrice();

        }

        return  totalAdditionalServicesPrice;
    }

    private void calculateTotalPayment(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) throws BusinessException {

        double rentedCarDailyPrice = 0 , totalAdditionalServicesPrice = 0 ,totalpayment, citySwapPrice = 750.00;

        totalAdditionalServicesPrice += sumOfAdditionalServicesPrice(createOrderedAdditionalServiceRequest);

        RentalCar rentalCar = checkIRentalCarExists(createOrderedAdditionalServiceRequest.getRentalId());
        RentalCarGetDto  rentalCarGetDto= this.modelMapperService.forDto().map(rentalCar,RentalCarGetDto.class);

        rentedCarDailyPrice += this.carService.getByCarId(this.rentalCarService.getByRentalId(createOrderedAdditionalServiceRequest.getRentalId()).getData().getCarId()).getData().getDailyPrice();

        totalpayment = rentedCarDailyPrice + totalAdditionalServicesPrice;

        if (checkIfSameCity(rentalCar)) {
            totalpayment = (totalpayment * findNoOfDaysBetween(rentalCarGetDto));
        } else {
            totalpayment = totalpayment * findNoOfDaysBetween(rentalCarGetDto) + citySwapPrice;
        }

        updateRentalCarTotalPaymnet(createOrderedAdditionalServiceRequest.getRentalId(),rentalCar,totalpayment);

    }

    private void updateRentalCarTotalPaymnet(int rentalCarId, RentalCar rentalCar, double totalpayment) throws BusinessException {

        UpdateRentalCarRequest updateRentalCarRequest = new UpdateRentalCarRequest();

        updateRentalCarRequest.setEndDate(rentalCar.getEndDate());
        updateRentalCarRequest.setStartingDate(rentalCar.getStartingDate());
        updateRentalCarRequest.setTotalPayment(totalpayment);
        updateRentalCarRequest.setCityPlate(0);

        this.rentalCarService.update(rentalCarId,updateRentalCarRequest);
    }

    @Override
    public double findNoOfDaysBetween(RentalCarGetDto rentalCarGetDto) {
        RentalCar rentalCar = this.modelMapperService.forDto().map(rentalCarGetDto,RentalCar.class);
        long noOfDaysBetween = ChronoUnit.DAYS.between(rentalCar.getStartingDate(), rentalCar.getEndDate());
        return (double) noOfDaysBetween;
    }


    private void checkIfAdditionalServiceExists(int additionalServiceId) throws BusinessException {

        if (this.additionalServiceService.getByAdditionalServiceId(additionalServiceId).getData() == null) {
            throw new BusinessException("There is no additional service corresponding to the sent id");
        }

    }

    private void checkIfRentalCarExists(int rentalCarId) throws BusinessException {

        if (this.rentalCarService.getByRentalId(rentalCarId).getData() == null) {
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

        Sort s = Sort.by(direction, "additionalServiceId");

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
    public DataResult<List<OrderedAdditionalServiceListDto>> getByRentalCar_RentalId(int rentalId) {

        List<OrderedAdditionalService> result = this.orderedAdditionalServiceDao.getByRentalCar_RentalId(rentalId);

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

        OrderedAdditionalService orderedAdditionalService = this.modelMapperService.forRequest().map(deleteOrderedAdditionalServiceRequest, OrderedAdditionalService.class);

        checkIfOrderedAdditionalServiceExists(orderedAdditionalService.getOrderedAdditionalServiceId());

        extractionOfAdditionalServicesPrice(deleteOrderedAdditionalServiceRequest);
        this.orderedAdditionalServiceDao.deleteById(orderedAdditionalService.getOrderedAdditionalServiceId());

        return new SuccessResult(orderedAdditionalService.getOrderedAdditionalServiceId() + " deleted..");
    }

    private void extractionOfAdditionalServicesPrice(DeleteOrderedAdditionalServiceRequest deleteOrderedAdditionalServiceRequest) throws BusinessException {

        double totalpayment = 0;

        RentalCar rentedCar = this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId()).getRentalCar();
        RentalCarGetDto  rentalCarGetDto= this.modelMapperService.forDto().map(rentedCar,RentalCarGetDto.class);

        double findNoOfDaysBetween  = findNoOfDaysBetween(rentalCarGetDto);

        totalpayment = (this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId()).getRentalCar().getTotalPayment())  -  (this.orderedAdditionalServiceDao.
                getByOrderedAdditionalServiceId(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId()).getAdditionalService().getDailyPrice())*(findNoOfDaysBetween);

        RentalCar rentalCar = checkIRentalCarExists(this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId()).getRentalCar().getRentalId());

        updateRentalCarTotalPaymnet( this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId()).getRentalCar().getRentalId() ,rentalCar,totalpayment);

    }

    private void checkIfOrderedAdditionalServiceExists(int orderedAdditionalServiceId) throws BusinessException {

        if (this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId) == null) {
            throw new BusinessException("There is no data in the id sent");
        }
    }
}
