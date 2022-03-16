package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.carCrashInformationDtos.CarCrashInformationGetDto;
import com.turkcell.rentACar.business.dtos.carCrashInformationDtos.CarCrashInformationListDto;
import com.turkcell.rentACar.business.request.carCrashInformationRequests.CreateCarCrashInformationRequest;
import com.turkcell.rentACar.business.request.carCrashInformationRequests.DeleteCarCrashInformationRequest;
import com.turkcell.rentACar.business.request.carCrashInformationRequests.UpdateCarCrashInformationRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CarCrashInformationService {

    Result add(CreateCarCrashInformationRequest createCarCrashInformationRequest) throws BusinessException;

    DataResult<CarCrashInformationGetDto> getByCarCrashInformationId(int carCrashInformationId);

    DataResult<List<CarCrashInformationListDto>> getAll();

    DataResult<List<CarCrashInformationListDto>> getAllPaged(int pageNo, int pageSize);

    DataResult<List<CarCrashInformationListDto>> getAllSorted(Sort.Direction direction);

    Result update(int carCrashInformationId, UpdateCarCrashInformationRequest updateCarCrashInformationRequest) throws BusinessException;

    Result delete(DeleteCarCrashInformationRequest deleteCarCrashInformationRequest) throws BusinessException;
}
