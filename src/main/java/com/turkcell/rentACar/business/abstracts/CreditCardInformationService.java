package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.creditCardInformationDtos.CreditCardInformationGetDto;
import com.turkcell.rentACar.business.dtos.creditCardInformationDtos.CreditCardInformationListDto;
import com.turkcell.rentACar.business.request.creditCardInformationRequests.CreateCreditCardInformationRequest;
import com.turkcell.rentACar.business.request.creditCardInformationRequests.DeleteCreditCardInformationRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

public interface CreditCardInformationService {

    Result add(CreateCreditCardInformationRequest createCreditCardInformationRequest) throws BusinessException;

    DataResult<CreditCardInformationGetDto> getByCreditCardInformationId(int creditCardInformationId);

    DataResult<List<CreditCardInformationListDto>> getAll();

    DataResult<List<CreditCardInformationListDto>> getByCustomer_CustomerId(int customerId);

    Result delete(DeleteCreditCardInformationRequest deleteCreditCardInformationRequest) throws BusinessException;
}
