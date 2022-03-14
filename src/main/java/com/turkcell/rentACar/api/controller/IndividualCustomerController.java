package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.business.abstracts.IndividualCustomerService;
import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerGetDto;
import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerListDto;
import com.turkcell.rentACar.business.request.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.turkcell.rentACar.business.request.individualCustomerRequests.DeleteIndividualCustomerRequest;
import com.turkcell.rentACar.business.request.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/individualCustomerControllers")
public class IndividualCustomerController {

    private final IndividualCustomerService individualCustomerService;

    @Autowired
    public IndividualCustomerController(IndividualCustomerService individualCustomerService) {
        this.individualCustomerService = individualCustomerService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateIndividualCustomerRequest createIndividualCustomerRequest) throws BusinessException {
        return this.individualCustomerService.add(createIndividualCustomerRequest);
    }

    @GetMapping("/getByIndividualCustomerId/{individualCustomerId}")
    public DataResult<IndividualCustomerGetDto> getByIndividualCustomerId(@RequestParam("individualCustomerId") int individualCustomerId) {
        return this.individualCustomerService.getByIndividualCustomerId(individualCustomerId);
    }

    @GetMapping("/getAll")
    public DataResult<List<IndividualCustomerListDto>> getAll() {
        return this.individualCustomerService.getAll();
    }

    @GetMapping("/getAllPaged")
    public DataResult<List<IndividualCustomerListDto>> getAllPaged(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return this.individualCustomerService.getAllPaged(pageNo, pageSize);
    }

    @GetMapping("/getAllSorted")
    public DataResult<List<IndividualCustomerListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction) {
        return this.individualCustomerService.getAllSorted(direction);
    }

    @PutMapping("/update")
    public Result update(@RequestParam("individualCustomerId") int individualCustomerId,@RequestBody @Valid UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws BusinessException {
        return this.individualCustomerService.update(individualCustomerId,updateIndividualCustomerRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) throws BusinessException {
        return this.individualCustomerService.delete(deleteIndividualCustomerRequest);
    }
}
