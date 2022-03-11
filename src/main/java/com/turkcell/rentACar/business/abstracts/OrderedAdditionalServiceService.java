package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.DeleteOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface OrderedAdditionalServiceService {

    Result add(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) throws BusinessException;

    double findNoOfDaysBetween(RentGetDto rentGetDto);

    DataResult<List<OrderedAdditionalServiceListDto>> getAll();

    DataResult<List<OrderedAdditionalServiceListDto>> getAllPaged(int pageNo, int pageSize);

    DataResult<List<OrderedAdditionalServiceListDto>> getAllSorted(Sort.Direction direction);

    DataResult<OrderedAdditionalServiceGetDto> getByOrderedAdditionalServiceId(int orderedAdditionalServiceId);

    DataResult<List<OrderedAdditionalServiceListDto>> getByRent_RentId(int rentId);

    Result update(int orderedAdditionalServiceId, UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalServiceRequest) throws BusinessException;

    Result delete(DeleteOrderedAdditionalServiceRequest deleteOrderedAdditionalServiceRequest) throws BusinessException;
}
