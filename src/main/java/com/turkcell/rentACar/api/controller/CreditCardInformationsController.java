package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.business.abstracts.CreditCardInformationService;
import com.turkcell.rentACar.business.dtos.creditCardInformationDtos.CreditCardInformationGetDto;
import com.turkcell.rentACar.business.dtos.creditCardInformationDtos.CreditCardInformationListDto;
import com.turkcell.rentACar.business.request.creditCardInformationRequests.CreateCreditCardInformationRequest;
import com.turkcell.rentACar.business.request.creditCardInformationRequests.DeleteCreditCardInformationRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/creditCardInformations")
public class CreditCardInformationsController {

    private CreditCardInformationService creditCardInformationService;

    @Autowired
    public CreditCardInformationsController(CreditCardInformationService creditCardInformationService) {
        this.creditCardInformationService = creditCardInformationService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCreditCardInformationRequest createCreditCardInformationRequest) throws BusinessException{
        return this.creditCardInformationService.add(createCreditCardInformationRequest);
    }

    @GetMapping("/getByCreditCardInformationId/{creditCardInformationId}")
    public DataResult<CreditCardInformationGetDto> getByCreditCardInformationId(@RequestParam("creditCardInformationId") int creditCardInformationId) {
        return this.creditCardInformationService.getByCreditCardInformationId(creditCardInformationId);
    }

    @GetMapping("/getAll")
    public DataResult<List<CreditCardInformationListDto>> getAll() {
        return this.creditCardInformationService.getAll();
    }

    @GetMapping("/getByCustomer_CustomerId/{customerId}")
    public DataResult<List<CreditCardInformationListDto>> getByCustomer_CustomerId(@RequestParam("customerId") int customerId) {
        return this.creditCardInformationService.getByCustomer_CustomerId(customerId);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteCreditCardInformationRequest deleteCreditCardInformationRequest) throws BusinessException {
        return this.creditCardInformationService.delete(deleteCreditCardInformationRequest);
    }
}
