package com.turkcell.rentACar.api.controller;

import com.turkcell.rentACar.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceGetDto;
import com.turkcell.rentACar.business.dtos.additionalServiceDtos.AdditionalServiceListDto;
import com.turkcell.rentACar.business.request.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.additionalServiceRequests.DeleteAdditionalServiceRequest;
import com.turkcell.rentACar.business.request.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/additionalServices")
public class AdditionalServicesController {

    private AdditionalServiceService additionalServiceService;

    @Autowired
    public AdditionalServicesController(AdditionalServiceService additionalServiceService) {
        this.additionalServiceService = additionalServiceService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateAdditionalServiceRequest createAdditionalServiceRequest) throws BusinessException {
        return this.additionalServiceService.add(createAdditionalServiceRequest);
    }

    @GetMapping("/getall")
    public DataResult<List<AdditionalServiceListDto>> getAll() {
        return this.additionalServiceService.getAll();
    }

    @GetMapping("/getAllSorted")
    public DataResult<List<AdditionalServiceListDto>> getAllSorted(@RequestParam("direction") Sort.Direction direction) {
        return this.additionalServiceService.getAllSorted(direction);
    }

    @GetMapping("/getAllPaged")
    public DataResult<List<AdditionalServiceListDto>> getAllPaged(@RequestParam("pageNo") int pageNo,
                                                      @RequestParam("pageSize") int pageSize) {
        return this.additionalServiceService.getAllPaged(pageNo, pageSize);
    }

    @GetMapping("/getByAdditionalServiceId/{additionalServiceId}")
    public DataResult<AdditionalServiceGetDto> getByAdditionalServiceId(@RequestParam("additionalServiceId") int additionalServiceId) {
        return this.additionalServiceService.getByAdditionalServiceId(additionalServiceId);
    }

    @PutMapping("/update")
    public Result update(@RequestParam("additionalServiceId") int additionalServiceId,
                         @RequestBody @Valid UpdateAdditionalServiceRequest updateAdditionalServiceRequest) throws BusinessException {
        return this.additionalServiceService.update(additionalServiceId, updateAdditionalServiceRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteAdditionalServiceRequest deleteAdditionalServiceRequest) throws BusinessException {
        return this.additionalServiceService.delete(deleteAdditionalServiceRequest);
    }
}
