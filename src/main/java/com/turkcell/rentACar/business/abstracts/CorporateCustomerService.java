package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerGetDto;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerListDto;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.DeleteCorporateCustomerRequest;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CorporateCustomerService {

    Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) throws BusinessException;

    DataResult<CorporateCustomerGetDto> getByCorporateCustomerId(int corporateCustomerId);

    DataResult<List<CorporateCustomerListDto>> getAll();

    DataResult<List<CorporateCustomerListDto>> getAllPaged(int pageNo, int pageSize);

    DataResult<List<CorporateCustomerListDto>> getAllSorted(Sort.Direction direction);

    Result update(int corporateCustomerId, UpdateCorporateCustomerRequest updateCorporateCustomerRequest) throws BusinessException;

    Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) throws BusinessException;
}
