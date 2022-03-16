package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.DeleteOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.OrderedAdditionalServiceDao;
import com.turkcell.rentACar.entities.concretes.OrderedAdditionalService;
import com.turkcell.rentACar.entities.concretes.Rent;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderedAdditionalServiceManager implements OrderedAdditionalServiceService {

    private OrderedAdditionalServiceDao orderedAdditionalServiceDao;
    private ModelMapperService modelMapperService;
    private AdditionalServiceService additionalServiceService;
    private RentService rentService;
    private InvoiceService invoiceService;

    @Lazy
    @Autowired
    public OrderedAdditionalServiceManager(OrderedAdditionalServiceDao orderedAdditionalServiceDao, ModelMapperService modelMapperService,
                                           AdditionalServiceService additionalServiceService, RentService rentService, InvoiceService invoiceService) {
        this.orderedAdditionalServiceDao = orderedAdditionalServiceDao;
        this.modelMapperService = modelMapperService;
        this.additionalServiceService = additionalServiceService;
        this.rentService = rentService;
        this.invoiceService = invoiceService;
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

        return new SuccessResult("OrderedAdditionalService added : " + orderedAdditionalService.getOrderedAdditionalServiceId());
    }

    private Rent getByRent(int rentId){

        RentGetDto rentGetDto = this.rentService.getByRentId(rentId).getData();
        Rent rent = this.modelMapperService.forDto().map(rentGetDto, Rent.class);
        return rent;
    }

    @Override
    public double findNoOfDaysBetween(LocalDate startingDate, LocalDate endDate) {

        long noOfDaysBetween = ChronoUnit.DAYS.between(startingDate,endDate);

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

        this.invoiceService.calculatingDailyPriceToSubtractAfterAdditionalServiceUpdate( orderedAdditionalService.getAdditionalService().getDailyPrice()
                ,orderedAdditionalService.getRent().getRentId());

        this.invoiceService.calculatingDailyPriceToAddingAfterAdditionalServiceUpdate( this.additionalServiceService.getByAdditionalServiceId(orderedAdditionalServiceUpdate.getAdditionalService().getAdditionalServiceId()).getData().getDailyPrice()
                ,orderedAdditionalService.getRent().getRentId());

        IdCorrector(orderedAdditionalService, orderedAdditionalServiceUpdate);

        this.orderedAdditionalServiceDao.save(orderedAdditionalServiceUpdate);

        return new SuccessResult(orderedAdditionalServiceUpdate.getOrderedAdditionalServiceId() + " updated..");
    }

    private void IdCorrector(OrderedAdditionalService orderedAdditionalService, OrderedAdditionalService orderedAdditionalServiceUpdate) {
        orderedAdditionalServiceUpdate.setOrderedAdditionalServiceId(orderedAdditionalService.getOrderedAdditionalServiceId());
        orderedAdditionalServiceUpdate.setRent(orderedAdditionalService.getRent());
    }


    @Override
    public Result delete(DeleteOrderedAdditionalServiceRequest deleteOrderedAdditionalServiceRequest) throws BusinessException {

        checkIfOrderedAdditionalServiceExists(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId());

        OrderedAdditionalService orderedAdditionalService = this.modelMapperService.forRequest().map(deleteOrderedAdditionalServiceRequest, OrderedAdditionalService.class);

        this.invoiceService.extractionOfAdditionalServicesPrice(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId());

        this.orderedAdditionalServiceDao.deleteById(orderedAdditionalService.getOrderedAdditionalServiceId());

        return new SuccessResult(orderedAdditionalService.getOrderedAdditionalServiceId() + " deleted..");
    }

    private void checkIfOrderedAdditionalServiceExists(int orderedAdditionalServiceId) throws BusinessException {

        if (this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId) == null) {
            throw new BusinessException("There is no data in the id sent");
        }
    }
}