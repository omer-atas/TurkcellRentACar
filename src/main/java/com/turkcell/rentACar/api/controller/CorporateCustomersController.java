package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.business.abstracts.CorporateCustomerService;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerGetDto;
import com.turkcell.rentACar.business.dtos.corporateCustomerDtos.CorporateCustomerListDto;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.DeleteCorporateCustomerRequest;
import com.turkcell.rentACar.business.request.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/corporateCustomers")
public class CorporateCustomersController {

    private CorporateCustomerService corporateCustomerService;

    @Autowired
    public CorporateCustomersController(CorporateCustomerService corporateCustomerService) {
        this.corporateCustomerService = corporateCustomerService;
    }

    @PostMapping("/add")
    public Result add( @RequestBody @Valid CreateCorporateCustomerRequest createCorporateCustomerRequest) throws BusinessException{
        return this.corporateCustomerService.add(createCorporateCustomerRequest);
    }

    @GetMapping("/getByCorporateCustomerId/{corporateCustomerId}")
    public DataResult<CorporateCustomerGetDto> getByCorporateCustomerId(@RequestParam("corporateCustomerId") int corporateCustomerId){
        return this.corporateCustomerService.getByCorporateCustomerId(corporateCustomerId);
    }

    @GetMapping("/getAll")
    public DataResult<List<CorporateCustomerListDto>> getAll(){
        return this.corporateCustomerService.getAll();
    }

    @GetMapping("/getAllPaged")
    public DataResult<List<CorporateCustomerListDto>> getAllPaged(@RequestParam("pageNo") int pageNo,@RequestParam("pageSize") int pageSize){
        return this.corporateCustomerService.getAllPaged(pageNo,pageSize);
    }

    @GetMapping("/getAllSorted")
    public DataResult<List<CorporateCustomerListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction){
        return this.corporateCustomerService.getAllSorted(direction);
    }

    @PutMapping("/update")
    public Result update(@RequestParam("corporateCustomerId") int corporateCustomerId, @RequestBody UpdateCorporateCustomerRequest updateCorporateCustomerRequest) throws BusinessException{
        return this.corporateCustomerService.update(corporateCustomerId,updateCorporateCustomerRequest);
    }

    @DeleteMapping("/delete")
    public  Result delete(@RequestBody @Valid DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) throws BusinessException{
        return this.corporateCustomerService.delete(deleteCorporateCustomerRequest);
    }
}
