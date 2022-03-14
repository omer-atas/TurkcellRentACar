package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CorporateCustomerService;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerGetDto;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerListDto;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.DeleteCorporateCustomerRequest;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.CorporateCustomerDao;
import com.turkcell.rentACar.entities.concretes.Brand;
import com.turkcell.rentACar.entities.concretes.CorporateCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorporateCustomerManager implements CorporateCustomerService {

    private CorporateCustomerDao corporateCustomerDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public CorporateCustomerManager(CorporateCustomerDao corporateCustomerDao, ModelMapperService modelMapperService) {
        this.corporateCustomerDao = corporateCustomerDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) throws BusinessException {
        
        checkIfTaxNumberNotDuplicated(createCorporateCustomerRequest.getTaxNumber());

        CorporateCustomer corporateCustomer = this.modelMapperService.forRequest().map(createCorporateCustomerRequest, CorporateCustomer.class);
        corporateCustomer.setCustomerId(0);

        this.corporateCustomerDao.save(corporateCustomer);

        return new SuccessResult("CorporateCustomer added : " + createCorporateCustomerRequest.getTaxNumber());
    }

    private void checkIfTaxNumberNotDuplicated(String taxNumber) throws BusinessException {

        if(this.corporateCustomerDao.existsByTaxNumber(taxNumber) == true){
            throw new BusinessException("Tax number can't be the same");
        }
    }

    @Override
    public DataResult<CorporateCustomerGetDto> getByCorporateCustomerId(int corporateCustomerId) {

        CorporateCustomer result = this.corporateCustomerDao.getByCustomerId(corporateCustomerId);

        if (result == null) {
            return new ErrorDataResult<CorporateCustomerGetDto>("CorporateCustomer not found");
        }

        CorporateCustomerGetDto response = this.modelMapperService.forDto().map(result, CorporateCustomerGetDto.class);

        return new SuccessDataResult<CorporateCustomerGetDto>(response, "Success");
    }

    @Override
    public DataResult<List<CorporateCustomerListDto>> getAll() {

        List<CorporateCustomer> result = this.corporateCustomerDao.findAll();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<CorporateCustomerListDto>>("CorporateCustomers not listed");
        }

        List<CorporateCustomerListDto> response = result.stream()
                .map(corporateCustomer -> this.modelMapperService.forDto().map(corporateCustomer, CorporateCustomerListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CorporateCustomerListDto>>(response, "Success");
    }

    @Override
    public DataResult<List<CorporateCustomerListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<CorporateCustomer> result = this.corporateCustomerDao.findAll(pageable).getContent();

        if (result.isEmpty()) {
            return new ErrorDataResult<List<CorporateCustomerListDto>>("CorporateCustomers not list - getAllPaged - ");
        }

        List<CorporateCustomerListDto> response = result.stream()
                .map(corporateCustomer -> this.modelMapperService.forDto().map(corporateCustomer, CorporateCustomerListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CorporateCustomerListDto>>(response, "CorporateCustomers Listed Successfully");
    }

    @Override
    public DataResult<List<CorporateCustomerListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "taxNumber");

        List<CorporateCustomer> result = this.corporateCustomerDao.findAll(s);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<CorporateCustomerListDto>>("CorporateCustomers not list - getAllSorted -");
        }

        List<CorporateCustomerListDto> response = result.stream()
                .map(corporateCustomer -> this.modelMapperService.forDto().map(corporateCustomer, CorporateCustomerListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CorporateCustomerListDto>>(response);
    }

    @Override
    public Result update(int corporateCustomerId, UpdateCorporateCustomerRequest updateCorporateCustomerRequest) throws BusinessException {

        checkIfCorporateCustomerExists(corporateCustomerId);
        checkIfTaxNumberNotDuplicated(updateCorporateCustomerRequest.getTaxNumber());

        CorporateCustomer corporateCustomer = this.corporateCustomerDao.getByCustomerId(corporateCustomerId);

        CorporateCustomer corporateCustomerUpdate = this.modelMapperService.forRequest().map(updateCorporateCustomerRequest, CorporateCustomer.class);

        IdCorrector(corporateCustomer, corporateCustomerUpdate);


        this.corporateCustomerDao.save(corporateCustomerUpdate);

        return new SuccessResult(updateCorporateCustomerRequest.getTaxNumber() + " updated..");
    }

    private void IdCorrector(CorporateCustomer corporateCustomer, CorporateCustomer corporateCustomerUpdate) {
        corporateCustomerUpdate.setCustomerId(corporateCustomer.getCustomerId());
    }

    @Override
    public Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) throws BusinessException {

        checkIfCorporateCustomerExists(deleteCorporateCustomerRequest.getCorporateCustomerId());

        CorporateCustomer corporateCustomer = this.modelMapperService.forRequest().map(deleteCorporateCustomerRequest, CorporateCustomer.class);

        this.corporateCustomerDao.deleteById(corporateCustomer.getCustomerId());

        return new SuccessResult(deleteCorporateCustomerRequest.getCorporateCustomerId() + " deleted..");
    }

    private void checkIfCorporateCustomerExists(int corporateCustomerId) throws BusinessException {
        if(this.corporateCustomerDao.getByCustomerId(corporateCustomerId) == null){
            throw new BusinessException("There is no data in the id sent");
        }
    }
}
