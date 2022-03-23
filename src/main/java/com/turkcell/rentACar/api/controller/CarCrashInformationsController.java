package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.business.abstracts.CarCrashInformationService;
import com.turkcell.rentACar.business.dtos.carCrashInformationDtos.CarCrashInformationGetDto;
import com.turkcell.rentACar.business.dtos.carCrashInformationDtos.CarCrashInformationListDto;
import com.turkcell.rentACar.business.request.carCrashInformationRequests.CreateCarCrashInformationRequest;
import com.turkcell.rentACar.business.request.carCrashInformationRequests.DeleteCarCrashInformationRequest;
import com.turkcell.rentACar.business.request.carCrashInformationRequests.UpdateCarCrashInformationRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ carCrashInformations")
public class CarCrashInformationsController {

    private CarCrashInformationService carCrashInformationService;

    @Autowired
    public CarCrashInformationsController(CarCrashInformationService carCrashInformationService) {
        this.carCrashInformationService = carCrashInformationService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCarCrashInformationRequest carCrashInformationRequest) throws BusinessException {
        return this.carCrashInformationService.add(carCrashInformationRequest);
    }

    @GetMapping("/getall")
    public DataResult<List<CarCrashInformationListDto>> getAll() {
        return this.carCrashInformationService.getAll();
    }

    @GetMapping("/getAllSorted")
    public DataResult<List<CarCrashInformationListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction) {
        return this.carCrashInformationService.getAllSorted(direction);
    }

    @GetMapping("/getAllPaged")
    public DataResult<List<CarCrashInformationListDto>> getAllPaged(@RequestParam("pageNo") int pageNo,
                                                        @RequestParam("pageSize") int pageSize) {
        return this.carCrashInformationService.getAllPaged(pageNo, pageSize);
    }

    @GetMapping("/getByCarCrashInformationId/{carCrashInformationId}")
    public DataResult<CarCrashInformationGetDto> getByCarCrashInformationId(@RequestParam("carCrashInformationId") int carCrashInformationId) {
        return this.carCrashInformationService.getByCarCrashInformationId(carCrashInformationId);
    }

    @PutMapping("/update")
    public Result update(@RequestParam("carCrashInformationId") int carCrashInformationId,
                         @RequestBody @Valid UpdateCarCrashInformationRequest updateCarCrashInformationRequest) throws BusinessException {
        return this.carCrashInformationService.update(carCrashInformationId, updateCarCrashInformationRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteCarCrashInformationRequest deleteCarCrashInformationRequest) throws BusinessException {
        return this.carCrashInformationService.delete(deleteCarCrashInformationRequest);
    }
}
