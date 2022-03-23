package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CorporateCustomerService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerGetDto;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerListDto;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.DeleteCorporateCustomerRequest;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.CorporateCustomerDao;
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
        checkIfTaxNumberRegex(createCorporateCustomerRequest.getTaxNumber());

        CorporateCustomer corporateCustomer = this.modelMapperService.forRequest().map(createCorporateCustomerRequest, CorporateCustomer.class);
        corporateCustomer.setCustomerId(0);

        this.corporateCustomerDao.save(corporateCustomer);

        return new SuccessResult(BusinessMessages.CORPORATE_CUSTOMER_ADD + createCorporateCustomerRequest.getTaxNumber());
    }

    private void checkIfTaxNumberRegex(String taxNumber) throws BusinessException {

        String corporateTaxNumber = new String("taxNumber");
        boolean matches = corporateTaxNumber.matches("/^[0-9]{10}$/");

        if(matches){
            throw new BusinessException(BusinessMessages.CORPORATE_CUSTOMER_TAX_NUMBER_REGEX);
        }

    }

    private void checkIfTaxNumberNotDuplicated(String taxNumber) throws BusinessException {

        if(this.corporateCustomerDao.existsByTaxNumber(taxNumber) == true){
            throw new BusinessException(BusinessMessages.CORPORATE_CUSTOMER_TAX_NUMBER_NOT_DUPLICATED);
        }
    }

    @Override
    public DataResult<CorporateCustomerGetDto> getByCorporateCustomerId(int corporateCustomerId) {

        CorporateCustomer result = this.corporateCustomerDao.getByCustomerId(corporateCustomerId);

        if (result == null) {
            return new ErrorDataResult<CorporateCustomerGetDto>(BusinessMessages.CORPORATE_CUSTOMER_NOT_FOUND);
        }

        CorporateCustomerGetDto response = this.modelMapperService.forDto().map(result, CorporateCustomerGetDto.class);

        return new SuccessDataResult<CorporateCustomerGetDto>(response, BusinessMessages.CORPORATE_CUSTOMER_GET_BY_ID);
    }

    @Override
    public DataResult<List<CorporateCustomerListDto>> getAll() {

        List<CorporateCustomer> result = this.corporateCustomerDao.findAll();

        List<CorporateCustomerListDto> response = result.stream()
                .map(corporateCustomer -> this.modelMapperService.forDto().map(corporateCustomer, CorporateCustomerListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CorporateCustomerListDto>>(response, BusinessMessages.CORPORATE_CUSTOMER_GET_ALL);
    }

    @Override
    public DataResult<List<CorporateCustomerListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<CorporateCustomer> result = this.corporateCustomerDao.findAll(pageable).getContent();

        List<CorporateCustomerListDto> response = result.stream()
                .map(corporateCustomer -> this.modelMapperService.forDto().map(corporateCustomer, CorporateCustomerListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CorporateCustomerListDto>>(response, BusinessMessages.CORPORATE_CUSTOMER_GET_ALL_PAGED);
    }

    @Override
    public DataResult<List<CorporateCustomerListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "taxNumber");

        List<CorporateCustomer> result = this.corporateCustomerDao.findAll(s);

        List<CorporateCustomerListDto> response = result.stream()
                .map(corporateCustomer -> this.modelMapperService.forDto().map(corporateCustomer, CorporateCustomerListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CorporateCustomerListDto>>(response,BusinessMessages.CORPORATE_CUSTOMER_GET_ALL_SORTED);
    }

    @Override
    public Result update(int corporateCustomerId, UpdateCorporateCustomerRequest updateCorporateCustomerRequest) throws BusinessException {

        checkIfCorporateCustomerExists(corporateCustomerId);
        checkIfTaxNumberNotDuplicated(updateCorporateCustomerRequest.getTaxNumber());
        checkIfTaxNumberRegex(updateCorporateCustomerRequest.getTaxNumber());

        CorporateCustomer corporateCustomer = this.corporateCustomerDao.getByCustomerId(corporateCustomerId);

        CorporateCustomer corporateCustomerUpdate = this.modelMapperService.forRequest().map(updateCorporateCustomerRequest, CorporateCustomer.class);

        IdCorrector(corporateCustomer, corporateCustomerUpdate);


        this.corporateCustomerDao.save(corporateCustomerUpdate);

        return new SuccessResult(updateCorporateCustomerRequest.getTaxNumber() + BusinessMessages.CORPORATE_CUSTOMER_UPDATE);
    }

    private void IdCorrector(CorporateCustomer corporateCustomer, CorporateCustomer corporateCustomerUpdate) {
        corporateCustomerUpdate.setCustomerId(corporateCustomer.getCustomerId());
    }

    @Override
    public Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) throws BusinessException {

        checkIfCorporateCustomerExists(deleteCorporateCustomerRequest.getCorporateCustomerId());

        CorporateCustomer corporateCustomer = this.modelMapperService.forRequest().map(deleteCorporateCustomerRequest, CorporateCustomer.class);

        this.corporateCustomerDao.deleteById(corporateCustomer.getCustomerId());

        return new SuccessResult(deleteCorporateCustomerRequest.getCorporateCustomerId() + BusinessMessages.CORPORATE_CUSTOMER_DELETE);
    }

    private void checkIfCorporateCustomerExists(int corporateCustomerId) throws BusinessException {
        if(this.corporateCustomerDao.getByCustomerId(corporateCustomerId) == null){
            throw new BusinessException(BusinessMessages.CORPORATE_CUSTOMER_NOT_FOUND);
        }
    }
}
