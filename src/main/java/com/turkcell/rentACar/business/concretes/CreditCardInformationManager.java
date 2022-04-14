package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CreditCardInformationService;
import com.turkcell.rentACar.business.abstracts.CustomerService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.creditCardInformationDtos.CreditCardInformationGetDto;
import com.turkcell.rentACar.business.dtos.creditCardInformationDtos.CreditCardInformationListDto;
import com.turkcell.rentACar.business.request.creditCardInformationRequests.CreateCreditCardInformationRequest;
import com.turkcell.rentACar.business.request.creditCardInformationRequests.DeleteCreditCardInformationRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.CreditCardInformationDao;
import com.turkcell.rentACar.entities.concretes.CreditCardInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditCardInformationManager implements CreditCardInformationService {

    private CreditCardInformationDao creditCardInformationDao;
    private ModelMapperService modelMapperService;
    private CustomerService customerService;

    @Autowired
    public CreditCardInformationManager(CreditCardInformationDao creditCardInformationDao, ModelMapperService modelMapperService, CustomerService customerService) {
        this.creditCardInformationDao = creditCardInformationDao;
        this.modelMapperService = modelMapperService;
        this.customerService = customerService;
    }

    @Override
    public Result add(CreateCreditCardInformationRequest createCreditCardInformationRequest) throws BusinessException {

        checkIfCustomerExists(createCreditCardInformationRequest.getCustomerId());

        CreditCardInformation creditCardInformation = this.modelMapperService.forRequest().map(createCreditCardInformationRequest, CreditCardInformation.class);
        creditCardInformation.setCreditCardInformationId(0);

        this.creditCardInformationDao.save(creditCardInformation);

        return new SuccessResult(BusinessMessages.CREDIT_CARD_INFORMATION_ADD + creditCardInformation.getCreditCardInformationId());
    }

    private void checkIfCustomerExists(int customerId) throws BusinessException {
        if (this.customerService.getByCustomerId(customerId) == null) {
            throw new BusinessException(BusinessMessages.CUSTOMER_NOT_FOUND);
        }
    }

    @Override
    public DataResult<CreditCardInformationGetDto> getByCreditCardInformationId(int creditCardInformationId) {

        CreditCardInformation result = this.creditCardInformationDao.getByCreditCardInformationId(creditCardInformationId);

        if (result == null) {
            return new ErrorDataResult<CreditCardInformationGetDto>(BusinessMessages.CREDIT_CARD_INFORMATION_NOT_FOUND);
        }

        CreditCardInformationGetDto response = this.modelMapperService.forDto().map(result, CreditCardInformationGetDto.class);

        return new SuccessDataResult<CreditCardInformationGetDto>(response, BusinessMessages.CREDIT_CARD_INFORMATION_GET_BY_ID);
    }

    @Override
    public DataResult<List<CreditCardInformationListDto>> getAll() {

        List<CreditCardInformation> result = this.creditCardInformationDao.findAll();

        List<CreditCardInformationListDto> response = result.stream().map(color -> this.modelMapperService.forDto().map(color, CreditCardInformationListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CreditCardInformationListDto>>(response, BusinessMessages.CREDIT_CARD_GET_ALL);
    }

    @Override
    public DataResult<List<CreditCardInformationListDto>> getByCustomer_CustomerId(int customerId) {

        List<CreditCardInformation> result = this.creditCardInformationDao.getByCustomer_CustomerId(customerId);

        if (result.isEmpty()) {
            return new ErrorDataResult<List<CreditCardInformationListDto>>(BusinessMessages.CUSTOMER_CREDIT_CARD_INFORMATION_NOT_FOUND);
        }

        List<CreditCardInformationListDto> response = result.stream().map(creditCardInformation -> this.modelMapperService.forDto().map(creditCardInformation, CreditCardInformationListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CreditCardInformationListDto>>(response, BusinessMessages.CUSTOMER_CREDIT_CARD_INFORMATION_LISTED);
    }

    @Override
    public Result delete(DeleteCreditCardInformationRequest deleteCreditCardInformationRequest) throws BusinessException {

        checkIfCreditCartInformationExists(deleteCreditCardInformationRequest.getCreditCardInformationId());

        CreditCardInformation creditCardInformation = this.modelMapperService.forRequest().map(deleteCreditCardInformationRequest, CreditCardInformation.class);

        this.creditCardInformationDao.deleteById(creditCardInformation.getCreditCardInformationId());

        return new SuccessResult(creditCardInformation.getCreditCardInformationId() + BusinessMessages.CREDIT_CARD_INFORMATION_DELETE);
    }

    private void checkIfCreditCartInformationExists(int creditCardInformationId) throws BusinessException {
        if(this.creditCardInformationDao.getByCreditCardInformationId(creditCardInformationId) == null){
            throw new BusinessException(BusinessMessages.CREDIT_CARD_INFORMATION_NOT_FOUND);
        }
    }
}
