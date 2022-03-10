package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorGetDto;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorListDto;
import com.turkcell.rentACar.business.request.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.additionalServiceRequests.DeleteAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.colorRequests.CreateColorRequest;
import com.turkcell.rentACar.business.request.colorRequests.DeleteColorRequest;
import com.turkcell.rentACar.business.request.colorRequests.UpdateColorRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AdditionalServiceService {

    Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest) throws BusinessException;

    DataResult<List<AdditionalServiceListDto>> getAll();

    DataResult<List<AdditionalServiceListDto>> getAllPaged(int pageNo, int pageSize);

    DataResult<List<AdditionalServiceListDto>> getAllSorted(Sort.Direction direction);

    DataResult<AdditionalServiceGetDto> getByAdditionalServiceId(int additionalServiceId);

    Result update(int additionalServiceId, UpdateAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException;

    Result delete(DeleteAdditionalServiceRequest deleteAdditionalServiceRequest) throws BusinessException;
}
