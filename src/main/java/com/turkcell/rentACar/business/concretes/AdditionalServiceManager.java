package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.AdditionalServiceService;
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

        this.additionalServiceDao.save(additionalService);
        return new SuccessResult("AdditionalService added : " + additionalService.getAdditionalServiceName());
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getAll() {

        List<AdditionalService> result = this.additionalServiceDao.findAll();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<AdditionalServiceListDto>>("AdditionalServices not listed");
        }

        List<AdditionalServiceListDto> response = result.stream()
                .map(additionalService -> this.modelMapperService.forDto().map(additionalService, AdditionalServiceListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<AdditionalServiceListDto>>(response, "Success");
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<AdditionalService> result = this.additionalServiceDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<AdditionalServiceListDto>>("AdditionalServices not list - getAllPaged - ");
        }

        List<AdditionalServiceListDto> response = result.stream()
                .map(car -> this.modelMapperService.forDto().map(car, AdditionalServiceListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<AdditionalServiceListDto>>(response, "AdditionalServices Listed Successfully");
    }

    @Override
    public DataResult<List<AdditionalServiceListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "additionalServiceName");

        List<AdditionalService> result = this.additionalServiceDao.findAll(s);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<AdditionalServiceListDto>>("AdditionalServices not list - getAllSorted -");
        }

        List<AdditionalServiceListDto> response = result.stream()
                .map(additionalService -> this.modelMapperService.forDto().map(additionalService, AdditionalServiceListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<AdditionalServiceListDto>>(response);
    }

    @Override
    public DataResult<AdditionalServiceGetDto> getByAdditionalServiceId(int additionalServiceId) {

        AdditionalService result = this.additionalServiceDao.getByAdditionalServiceId(additionalServiceId);

        if (result == null) {
            return new ErrorDataResult<AdditionalServiceGetDto>("AdditionalService not found");
        }

        AdditionalServiceGetDto response = this.modelMapperService.forDto().map(result, AdditionalServiceGetDto.class);
        return new SuccessDataResult<AdditionalServiceGetDto>(response, "Success");
    }

    @Override
    public Result update(int additionalServiceId, UpdateAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException {

        AdditionalService additionalService = this.additionalServiceDao.getByAdditionalServiceId(additionalServiceId);

        AdditionalService additionalServiceUpdate = this.modelMapperService.forRequest().map(updateAdditionalServiceRequest, AdditionalService.class);

        IdCorrector(additionalService, additionalServiceUpdate);

        this.additionalServiceDao.save(additionalServiceUpdate);
        return new SuccessResult(additionalServiceUpdate.getAdditionalServiceName() + " updated..");

    }

    private void IdCorrector(AdditionalService additionalService, AdditionalService additionalServiceUpdate) {
        additionalServiceUpdate.setAdditionalServiceId(additionalService.getAdditionalServiceId());
    }

    @Override
    public Result delete(DeleteAdditionalServiceRequest deleteAdditionalServiceRequest) throws BusinessException {
        
        AdditionalService additionalService = this.modelMapperService.forRequest().map(deleteAdditionalServiceRequest, AdditionalService.class);

        checkIfAdditionalServiceExists(additionalService.getAdditionalServiceId());

        this.additionalServiceDao.deleteById(additionalService.getAdditionalServiceId());
        return new SuccessResult(deleteAdditionalServiceRequest.getAdditionalServiceId() + " deleted..");
    }

    private void checkIfAdditionalServiceExists(int additionalServiceId) throws BusinessException {

        if(this.additionalServiceDao.getByAdditionalServiceId(additionalServiceId) == null){
            throw new BusinessException("There is no data in the id sent");
        }
    }
}
