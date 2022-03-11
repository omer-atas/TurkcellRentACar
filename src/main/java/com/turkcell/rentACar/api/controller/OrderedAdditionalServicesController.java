package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceListDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos.OrderedAdditionalServiceListDto;
import com.turkcell.rentACar.business.request.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.additionalServiceRequests.DeleteAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.DeleteOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ orderedAdditionalServices")
public class OrderedAdditionalServicesController {

    private OrderedAdditionalServiceService orderedAdditionalServiceService;

    @Autowired
    public OrderedAdditionalServicesController(OrderedAdditionalServiceService orderedAdditionalServiceService) {
        this.orderedAdditionalServiceService = orderedAdditionalServiceService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) throws BusinessException {
        return this.orderedAdditionalServiceService.add(createOrderedAdditionalServiceRequest);
    }

    @GetMapping("/getall")
    public DataResult<List<OrderedAdditionalServiceListDto>> getAll() {
        return this.orderedAdditionalServiceService.getAll();
    }

    @GetMapping("/getAllSorted")
    public DataResult<List<OrderedAdditionalServiceListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction) {
        return this.orderedAdditionalServiceService.getAllSorted(direction);
    }

    @GetMapping("/getAllPaged")
    public DataResult<List<OrderedAdditionalServiceListDto>> getAllPaged(@RequestParam("pageNo") int pageNo,
                                                                  @RequestParam("pageSize") int pageSize) {
        return this.orderedAdditionalServiceService.getAllPaged(pageNo, pageSize);
    }

    @GetMapping("/getByOrderedAdditionalServiceId/{orderedAdditionalServiceId}")
    public DataResult<OrderedAdditionalServiceGetDto> getByOrderedAdditionalServiceId(@RequestParam("orderedAdditionalServiceId") int orderedAdditionalServiceId) {
        return this.orderedAdditionalServiceService.getByOrderedAdditionalServiceId(orderedAdditionalServiceId);
    }

    @GetMapping("/getByRentalCar_RentalId/{rentalId}")
    public DataResult<List<OrderedAdditionalServiceListDto>> getByRentalCar_RentalId(int rentalId){
        return this.orderedAdditionalServiceService.getByRentalCar_RentalId(rentalId);
    }

    @PutMapping("/update")
    public Result update(@RequestParam("orderedAdditionalServiceId") int orderedAdditionalServiceId,
                         @RequestBody @Valid UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalServiceRequest) throws BusinessException {
        return this.orderedAdditionalServiceService.update(orderedAdditionalServiceId, updateOrderedAdditionalServiceRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody DeleteOrderedAdditionalServiceRequest deleteOrderedAdditionalServiceRequest) throws BusinessException {
        return this.orderedAdditionalServiceService.delete(deleteOrderedAdditionalServiceRequest);
    }
}
