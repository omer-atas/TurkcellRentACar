package com.turkcell.rentACar.api.controller;

import java.util.List;

import javax.validation.Valid;

import com.turkcell.rentACar.api.modals.RentEndDateDelayPostServiceModal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACar.business.abstracts.RentService;
import com.turkcell.rentACar.business.dtos.rentDtos.RentGetDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentListDto;
import com.turkcell.rentACar.business.request.rentRequests.DeleteRentRequest;
import com.turkcell.rentACar.business.request.rentRequests.UpdateRentRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("/api/rentalCars")
public class RentsController {

    private final RentService rentService;

    @Autowired
    public RentsController(RentService rentService) {
        super();
        this.rentService = rentService;
    }

    @GetMapping("/getByRentId/{rentId}")
    public DataResult<RentGetDto> getByRentId(@RequestParam("rentId") int rentId) {
        return this.rentService.getByRentId(rentId);
    }

    @GetMapping("/getAll")
    public DataResult<List<RentListDto>> getAll() {
        return this.rentService.getAll();
    }

    @GetMapping("/getAllForCorporateCustomer")
    public DataResult<List<RentListDto>> getAllForCorporateCustomer() {
        return this.rentService.getAllForCorporateCustomer();
    }

    @GetMapping("/getAllForIndividualCustomer")
    public DataResult<List<RentListDto>> getAllForIndividualCustomer() {
        return this.rentService.getAllForIndividualCustomer();
    }

    @GetMapping("/getAllPaged")
    public DataResult<List<RentListDto>> getAllPaged(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize) {
        return this.rentService.getAllPaged(pageNo, pageSize);
    }

    @GetMapping("/getAllSorted")
    public DataResult<List<RentListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction) {
        return this.rentService.getAllSorted(direction);
    }

    @PutMapping("/update")
    public Result update(@RequestParam("rentId") int rentId, @RequestBody @Valid UpdateRentRequest updateRentRequest) throws BusinessException {
        return this.rentService.update(rentId, updateRentRequest);
    }

    @PutMapping("/updateRentDelayEndDateForIndividualCustomer")
    public Result updateRentDelayEndDateForIndividualCustomer(@RequestParam("rentId") int rentId, @RequestBody @Valid RentEndDateDelayPostServiceModal rentEndDateDelayPostServiceModal) throws BusinessException{
        return  this.rentService.updateRentDelayEndDateForIndividualCustomer(rentId,rentEndDateDelayPostServiceModal);
    }

    @PutMapping("/updateRentDelayEndDateForCorporateCustomer")
    public Result updateRentDelayEndDateForCorporateCustomer(@RequestParam("rentId") int rentId, @RequestBody @Valid RentEndDateDelayPostServiceModal rentEndDateDelayPostServiceModal) throws BusinessException{
        return this.rentService.updateRentDelayEndDateForCorporateCustomer(rentId,rentEndDateDelayPostServiceModal);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteRentRequest deleteRentRequest) throws BusinessException {
        return this.rentService.delete(deleteRentRequest);
    }

}
