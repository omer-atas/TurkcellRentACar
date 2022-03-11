package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.rentalCarDtos.RentalCarGetDto;
import com.turkcell.rentACar.business.request.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.additionalServiceRequests.DeleteAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.DeleteOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.entities.concretes.OrderedAdditionalService;
import com.turkcell.rentACar.entities.concretes.RentalCar;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface OrderedAdditionalServiceService {

    Result add(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) throws BusinessException;

    double findNoOfDaysBetween(RentalCarGetDto rentalCarGetDto);

    DataResult<List<OrderedAdditionalServiceListDto>> getAll();

    DataResult<List<OrderedAdditionalServiceListDto>> getAllPaged(int pageNo, int pageSize);

    DataResult<List<OrderedAdditionalServiceListDto>> getAllSorted(Sort.Direction direction);

    DataResult<OrderedAdditionalServiceGetDto> getByOrderedAdditionalServiceId(int orderedAdditionalServiceId);

    DataResult<List<OrderedAdditionalServiceListDto>> getByRentalCar_RentalId(int rentalId);

    Result update(int orderedAdditionalServiceId, UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalServiceRequest) throws BusinessException;

    Result delete(DeleteOrderedAdditionalServiceRequest deleteOrderedAdditionalServiceRequest) throws BusinessException;
}
