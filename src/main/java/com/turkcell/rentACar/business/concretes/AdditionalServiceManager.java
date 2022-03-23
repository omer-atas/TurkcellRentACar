package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceListDto;
import com.turkcell.rentACar.business.request.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.additionalServiceRequests.DeleteAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.AdditionalServiceDao;
import com.turkcell.rentACar.entities.concretes.AdditionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdditionalServiceManager implements AdditionalServiceService {

    private AdditionalServiceDao additionalServiceDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public AdditionalServiceManager(AdditionalServiceDao additionalServiceDao, ModelMapperService modelMapperService) {
        this.additionalServiceDao = additionalServiceDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest) throws BusinessException {

        AdditionalService additionalService = this.modelMapperService.forRequest().map(createAdditionalServiceRequest, AdditionalService.class);
        additionalService.setAdditionalServiceId(0);

        this.additionalServiceDao.save(additionalService);

        return new SuccessResult(BusinessMessages.ADDITIONAL_SERVICE_ADD + additionalService.getAdditionalServiceName());
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getAll() {

        List<AdditionalService> result = this.additionalServiceDao.findAll();

        List<AdditionalServiceListDto> response = result.stream()
                .map(additionalService -> this.modelMapperService.forDto().map(additionalService, AdditionalServiceListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<AdditionalServiceListDto>>(response, BusinessMessages.ADDITIONAL_SERVİCE_GET_ALL);
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<AdditionalService> result = this.additionalServiceDao.findAll(pageable).getContent();

        List<AdditionalServiceListDto> response = result.stream()
                .map(car -> this.modelMapperService.forDto().map(car, AdditionalServiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<AdditionalServiceListDto>>(response, BusinessMessages.ADDITIONAL_SERVİCE_GET_ALL_PAGED);
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "additionalServiceName");

        List<AdditionalService> result = this.additionalServiceDao.findAll(s);

        List<AdditionalServiceListDto> response = result.stream()
                .map(additionalService -> this.modelMapperService.forDto().map(additionalService, AdditionalServiceListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<AdditionalServiceListDto>>(response,BusinessMessages.ADDITIONAL_SERVİCE_GET_ALL_SORTED);
    }

    @Override
    public DataResult<AdditionalServiceGetDto> getByAdditionalServiceId(int additionalServiceId) {

        AdditionalService result = this.additionalServiceDao.getByAdditionalServiceId(additionalServiceId);

        if (result == null) {
            return new ErrorDataResult<AdditionalServiceGetDto>(BusinessMessages.ADDITIONAL_SERVICE_NOT_FOUND);
        }

        AdditionalServiceGetDto response = this.modelMapperService.forDto().map(result, AdditionalServiceGetDto.class);

        return new SuccessDataResult<AdditionalServiceGetDto>(response, BusinessMessages.ADDITIONAL_SERVICE_GET_BY_ID);
    }

    @Override
    public Result update(int additionalServiceId, UpdateAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException {

        checkIfAdditionalServiceExists(additionalServiceId);

        AdditionalService additionalService = this.additionalServiceDao.getByAdditionalServiceId(additionalServiceId);

        AdditionalService additionalServiceUpdate = this.modelMapperService.forRequest().map(updateAdditionalServiceRequest, AdditionalService.class);

        IdCorrector(additionalService, additionalServiceUpdate);

        this.additionalServiceDao.save(additionalServiceUpdate);

        return new SuccessResult(additionalServiceUpdate.getAdditionalServiceName() + BusinessMessages.ADDITIONAL_SERVICE_UPDATE);

    }

    private void IdCorrector(AdditionalService additionalService, AdditionalService additionalServiceUpdate) {
        additionalServiceUpdate.setAdditionalServiceId(additionalService.getAdditionalServiceId());
    }

    @Override
    public Result delete(DeleteAdditionalServiceRequest deleteAdditionalServiceRequest) throws BusinessException {

        checkIfAdditionalServiceExists(deleteAdditionalServiceRequest.getAdditionalServiceId());
        
        AdditionalService additionalService = this.modelMapperService.forRequest().map(deleteAdditionalServiceRequest, AdditionalService.class);

        this.additionalServiceDao.deleteById(additionalService.getAdditionalServiceId());

        return new SuccessResult(deleteAdditionalServiceRequest.getAdditionalServiceId() + BusinessMessages.ADDITIONAL_SERVICE_DELETE);
    }

    private void checkIfAdditionalServiceExists(int additionalServiceId) throws BusinessException {

        if(this.additionalServiceDao.getByAdditionalServiceId(additionalServiceId) == null){
            throw new BusinessException(BusinessMessages.ADDITIONAL_SERVICE_NOT_FOUND);
        }
    }
}
