package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.*;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
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

    private final OrderedAdditionalServiceDao orderedAdditionalServiceDao;
    private final ModelMapperService modelMapperService;
    private final AdditionalServiceService additionalServiceService;
    private final RentService rentService;

    @Lazy
    @Autowired
    public OrderedAdditionalServiceManager(OrderedAdditionalServiceDao orderedAdditionalServiceDao, ModelMapperService modelMapperService, AdditionalServiceService additionalServiceService, RentService rentService) {
        this.orderedAdditionalServiceDao = orderedAdditionalServiceDao;
        this.modelMapperService = modelMapperService;
        this.additionalServiceService = additionalServiceService;
        this.rentService = rentService;
    }

    @Override
    public void addOrderedAdditionalServiceForPayment(List<Integer> additionalServiceIds, int rentId) throws BusinessException {

        checkIfAdditionalServiceExists(additionalServiceIds);
        checkIfRentExists(rentId);

        for (int additionalServiceId:additionalServiceIds){

            CreateOrderedAdditionalServiceRequest orderedAdditionalServiceRequest = new CreateOrderedAdditionalServiceRequest();
            orderedAdditionalServiceRequest.setAdditionalServiceId(additionalServiceId);
            orderedAdditionalServiceRequest.setRentId(rentId);

            OrderedAdditionalService orderedAdditionalService = this.modelMapperService.forDto().map(orderedAdditionalServiceRequest,OrderedAdditionalService.class);
            orderedAdditionalService.setOrderedAdditionalServiceId(0);
            this.orderedAdditionalServiceDao.save(orderedAdditionalService);
        }
    }

    @Override
    public double findNoOfDaysBetween(LocalDate startingDate, LocalDate endDate) {

        long noOfDaysBetween = ChronoUnit.DAYS.between(startingDate, endDate);

        return (double) noOfDaysBetween;
    }


    private void checkIfAdditionalServiceExists(List<Integer> additionalServiceIds) throws BusinessException {
        for(int additionalServiceId:additionalServiceIds){
            if (this.additionalServiceService.getByAdditionalServiceId(additionalServiceId).getData() == null) {
                throw new BusinessException(BusinessMessages.ADDITIONAL_SERVICE_NOT_FOUND);
            }
        }


    }

    private void checkIfRentExists(int rentalCarId) throws BusinessException {

        if (this.rentService.getByRentId(rentalCarId).getData() == null) {
            throw new BusinessException(BusinessMessages.RENT_NOT_FOUND);
        }

    }

    @Override
    public DataResult<List<OrderedAdditionalServiceListDto>> getAll() {

        List<OrderedAdditionalService> result = this.orderedAdditionalServiceDao.findAll();

        List<OrderedAdditionalServiceListDto> response = result.stream().map(orderedAdditionalService -> this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<OrderedAdditionalServiceListDto>>(response, BusinessMessages.ORDERED_ADDITIONAL_SERVICE_GET_ALL);
    }

    @Override
    public DataResult<List<OrderedAdditionalServiceListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<OrderedAdditionalService> result = this.orderedAdditionalServiceDao.findAll(pageable).getContent();

        List<OrderedAdditionalServiceListDto> response = result.stream().map(car -> this.modelMapperService.forDto().map(car, OrderedAdditionalServiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<OrderedAdditionalServiceListDto>>(response, BusinessMessages.ORDERED_ADDITIONAL_SERVICE_GET_ALL_PAGED);
    }

    @Override
    public DataResult<List<OrderedAdditionalServiceListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "orderedAdditionalServiceId");

        List<OrderedAdditionalService> result = this.orderedAdditionalServiceDao.findAll(s);

        List<OrderedAdditionalServiceListDto> response = result.stream().map(orderedAdditionalService -> this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<OrderedAdditionalServiceListDto>>(response, BusinessMessages.ORDERED_ADDITIONAL_SERVICE_GET_ALL_SORTED);
    }

    @Override
    public DataResult<OrderedAdditionalServiceGetDto> getByOrderedAdditionalServiceId(int orderedAdditionalServiceId) {

        OrderedAdditionalService result = this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId);

        if (result == null) {
            return new ErrorDataResult<OrderedAdditionalServiceGetDto>(BusinessMessages.ORDERED_ADDITIONAL_SERVICE_NOT_FOUND);
        }

        OrderedAdditionalServiceGetDto response = this.modelMapperService.forDto().map(result, OrderedAdditionalServiceGetDto.class);

        return new SuccessDataResult<OrderedAdditionalServiceGetDto>(response, BusinessMessages.ORDERED_ADDITIONAL_SERVICE_GET_BY_ID);
    }

    @Override
    public DataResult<List<OrderedAdditionalServiceListDto>> getByRent_RentId(int rentId) {

        List<OrderedAdditionalService> result = this.orderedAdditionalServiceDao.getByRent_RentId(rentId);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<OrderedAdditionalServiceListDto>>(BusinessMessages.ORDERED_ADDITIONAL_SERVICE_RENT_NOT_FOUND);
        }

        List<OrderedAdditionalServiceListDto> response = result.stream().map(orderedAdditionalService -> this.modelMapperService.forDto().map(orderedAdditionalService, OrderedAdditionalServiceListDto.class)).collect(Collectors.toList());


        return new SuccessDataResult<List<OrderedAdditionalServiceListDto>>(response);
    }

    @Override
    public Result update(int orderedAdditionalServiceId, UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalServiceRequest) throws BusinessException {

        OrderedAdditionalService orderedAdditionalService = this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId);

        OrderedAdditionalService orderedAdditionalServiceUpdate = this.modelMapperService.forRequest().map(updateOrderedAdditionalServiceRequest, OrderedAdditionalService.class);

        IdCorrector(orderedAdditionalService, orderedAdditionalServiceUpdate);

        this.orderedAdditionalServiceDao.save(orderedAdditionalServiceUpdate);

        return new SuccessResult(orderedAdditionalServiceUpdate.getOrderedAdditionalServiceId() + BusinessMessages.ORDERED_ADDITIONAL_SERVICE_UPDATE);
    }

    private void IdCorrector(OrderedAdditionalService orderedAdditionalService, OrderedAdditionalService orderedAdditionalServiceUpdate) {
        orderedAdditionalServiceUpdate.setOrderedAdditionalServiceId(orderedAdditionalService.getOrderedAdditionalServiceId());
        orderedAdditionalServiceUpdate.setRent(orderedAdditionalService.getRent());
    }


    @Override
    public Result delete(DeleteOrderedAdditionalServiceRequest deleteOrderedAdditionalServiceRequest) throws BusinessException {

        checkIfOrderedAdditionalServiceExists(deleteOrderedAdditionalServiceRequest.getOrderedAdditionalServiceId());

        OrderedAdditionalService orderedAdditionalService = this.modelMapperService.forRequest().map(deleteOrderedAdditionalServiceRequest, OrderedAdditionalService.class);

        this.orderedAdditionalServiceDao.deleteById(orderedAdditionalService.getOrderedAdditionalServiceId());

        return new SuccessResult(orderedAdditionalService.getOrderedAdditionalServiceId() + BusinessMessages.ORDERED_ADDITIONAL_SERVICE_DELETE);
    }

    private void checkIfOrderedAdditionalServiceExists(int orderedAdditionalServiceId) throws BusinessException {

        if (this.orderedAdditionalServiceDao.getByOrderedAdditionalServiceId(orderedAdditionalServiceId) == null) {
            throw new BusinessException(BusinessMessages.ORDERED_ADDITIONAL_SERVICE_NOT_FOUND);
        }
    }
}