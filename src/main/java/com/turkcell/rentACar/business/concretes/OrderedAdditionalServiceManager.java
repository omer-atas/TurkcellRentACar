package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.dtos.invoiceDtos.InvoiceGetDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
import com.turkcell.rentACar.business.request.invoiceRequests.UpdateInvoiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.DeleteOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.rentRequests.UpdateRentRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.OrderedAdditionalServiceDao;
import com.turkcell.rentACar.entities.concretes.Invoice;
import com.turkcell.rentACar.entities.concretes.OrderedAdditionalService;
import com.turkcell.rentACar.entities.concretes.Rent;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CarService carService;
    private InvoiceService invoiceService;

    @Autowired
    public OrderedAdditionalServiceManager(OrderedAdditionalServiceDao orderedAdditionalServiceDao, ModelMapperService modelMapperService,
                                           AdditionalServiceService additionalServiceService, RentService rentService, CarService carService,InvoiceService invoiceService) {
        this.orderedAdditionalServiceDao = orderedAdditionalServiceDao;
        this.modelMapperService = modelMapperService;
        this.additionalServiceService = additionalServiceService;
        this.rentService = rentService;
        this.carService = carService;
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

        calculateTotalPayment(createOrderedAdditionalServiceRequest.getRentId(),createOrderedAdditionalServiceRequest.getAdditionalServiceId());

        return new SuccessResult("OrderedAdditionalService added : " + orderedAdditionalService.getOrderedAdditionalServiceId());
    }

    private Rent getByRent(int rentId){

        RentGetDto rentGetDto = this.rentService.getByRentId(rentId).getData();
        Rent rent = this.modelMapperService.forDto().map(rentGetDto, Rent.class);
        return rent;
    }

    private double sumOfAdditionalServicesPrice(int rentId,int additionalServiceId){

        double totalAdditionalServicesPrice = 0;

        List<OrderedAdditionalService> orderedAdditionalServices= this.orderedAdditionalServiceDao.getByRent_RentId(rentId);

        if(orderedAdditionalServices != null){
            for (OrderedAdditionalService o : orderedAdditionalServices) {
                totalAdditionalServicesPrice += o.getAdditionalService().getDailyPrice();
            }
        }

        totalAdditionalServicesPrice += this.additionalServiceService.getByAdditionalServiceId(additionalServiceId).getData().getDailyPrice();

        return  totalAdditionalServicesPrice;
    }

    private void calculateTotalPayment(int rentId,int additionalServiceId) throws BusinessException {

        double rentedCarTotalPrice = 0 , totalAdditionalServicesPrice = 0 ,totalpayment, citySwapPrice = 750.00;

        LocalDate startDate = this.rentService.getByRentId(rentId).getData().getStartingDate();
        LocalDate endDate = this.rentService.getByRentId(rentId).getData().getEndDate();

        totalAdditionalServicesPrice += sumOfAdditionalServicesPrice(rentId,additionalServiceId) * findNoOfDaysBetween(startDate,endDate);

        rentedCarTotalPrice += this.rentService.getByRentId(rentId).getData().getRentalPriceOfTheCar();

        if(this.rentService.getByRentId(rentId).getData().getToCityId() !=
                    this.rentService.getByRentId(rentId).getData().getFromCityId()){
            totalpayment = totalAdditionalServicesPrice + rentedCarTotalPrice + citySwapPrice;
        }else{
            totalpayment = totalAdditionalServicesPrice + rentedCarTotalPrice;
        }

        UpdateInvoiceRequest updateInvoiceRequest = updatetotalPaymnetInInvoice(rentId,totalpayment);
        this.invoiceService.update(this.invoiceService.getByRent_RentId(rentId).getData().getInvoiceId(),updateInvoiceRequest);
    }

    private UpdateInvoiceRequest updatetotalPaymnetInInvoice(int rentId,double totalpayment){

        InvoiceGetDto invoiceGetDto = this.invoiceService.getByRent_RentId(rentId).getData();
        UpdateInvoiceRequest updateInvoiceRequest = this.modelMapperService.forDto().map(invoiceGetDto,UpdateInvoiceRequest.class);
        updateInvoiceRequest.setTotalPayment(totalpayment);

        return updateInvoiceRequest;
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

        extractionOfAdditionalServicesPrice(orderedAdditionalServiceId);

        calculateTotalPayment(this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId).getRent().getRentId(),
                this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId).getAdditionalService().getAdditionalServiceId());

        OrderedAdditionalService orderedAdditionalService = this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId);

        OrderedAdditionalService orderedAdditionalServiceUpdate = this.modelMapperService.forRequest().map(updateOrderedAdditionalServiceRequest, OrderedAdditionalService.class);

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

        extractionOfAdditionalServicesPrice(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId());

        this.orderedAdditionalServiceDao.deleteById(orderedAdditionalService.getOrderedAdditionalServiceId());

        return new SuccessResult(orderedAdditionalService.getOrderedAdditionalServiceId() + " deleted..");
    }

    private void extractionOfAdditionalServicesPrice(int orderedAdditionalServiceId) throws BusinessException {

        OrderedAdditionalService orderedAdditionalService = this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId);

        LocalDate startingDate = orderedAdditionalService.getRent().getStartingDate();
        LocalDate endDate = orderedAdditionalService.getRent().getEndDate();

        double day = findNoOfDaysBetween(startingDate,endDate);

        double dailyPrice = this.additionalServiceService.getByAdditionalServiceId(orderedAdditionalService.getAdditionalService().getAdditionalServiceId()).getData().getDailyPrice();

        double totalPayment = this.invoiceService.getByRent_RentId(orderedAdditionalService.getRent().getRentId()).getData().getTotalPayment();

        totalPayment -= (day * dailyPrice);

        int rentId = this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId).getRent().getRentId();

        UpdateInvoiceRequest updateInvoiceRequest = updatetotalPaymnetInInvoice(rentId,totalPayment);

        this.invoiceService.update(this.invoiceService.getByRent_RentId(orderedAdditionalServiceId).getData().getInvoiceId(),updateInvoiceRequest);

    }

    private void checkIfOrderedAdditionalServiceExists(int orderedAdditionalServiceId) throws BusinessException {

        if (this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId) == null) {
            throw new BusinessException("There is no data in the id sent");
        }
    }
}
