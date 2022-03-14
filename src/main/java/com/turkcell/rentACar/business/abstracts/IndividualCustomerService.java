package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerGetDto;
import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerListDto;
import com.turkcell.rentACar.business.request.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.turkcell.rentACar.business.request.individualCustomerRequests.DeleteIndividualCustomerRequest;
import com.turkcell.rentACar.business.request.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IndividualCustomerService {

    Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) throws BusinessException;

    DataResult<IndividualCustomerGetDto> getByIndividualCustomerId(int individualCustomerId);

    DataResult<List<IndividualCustomerListDto>> getAll();

    DataResult<List<IndividualCustomerListDto>> getAllPaged(int pageNo, int pageSize);

    DataResult<List<IndividualCustomerListDto>> getAllSorted(Sort.Direction direction);

    Result update(int individualCustomerId, UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws BusinessException;

    Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) throws BusinessException;
}
