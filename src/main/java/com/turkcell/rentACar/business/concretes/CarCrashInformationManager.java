package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CarCrashInformationService;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.dtos.carCrashInformationDtos.CarCrashInformationGetDto;
import com.turkcell.rentACar.business.dtos.carCrashInformationDtos.CarCrashInformationListDto;
import com.turkcell.rentACar.business.request.carCrashInformationRequests.CreateCarCrashInformationRequest;
import com.turkcell.rentACar.business.request.carCrashInformationRequests.DeleteCarCrashInformationRequest;
import com.turkcell.rentACar.business.request.carCrashInformationRequests.UpdateCarCrashInformationRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.CarCrashInformationDao;
import com.turkcell.rentACar.entities.concretes.CarCrashInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarCrashInformationManager implements CarCrashInformationService {

    private CarCrashInformationDao carCrashInformationDao;
    private ModelMapperService modelMapperService;
    private CarService carService;

    @Autowired
    public CarCrashInformationManager(CarCrashInformationDao carCrashInformationDao, ModelMapperService modelMapperService, CarService carService) {
        this.carCrashInformationDao = carCrashInformationDao;
        this.modelMapperService = modelMapperService;
        this.carService = carService;
    }

    @Override
    public Result add(CreateCarCrashInformationRequest createCarCrashInformationRequest) throws BusinessException {

        checkIfCarExists(createCarCrashInformationRequest.getCarId());

        CarCrashInformation carCrashInformation = this.modelMapperService.forRequest().map(createCarCrashInformationRequest, CarCrashInformation.class);

        this.carCrashInformationDao.save(carCrashInformation);

        return new SuccessResult("CarCrashInformation added : " + carCrashInformation.getCarCrashInformationId());
    }

    private void checkIfCarExists(int carId) throws BusinessException {

        if(this.carService.getByCarId(carId).getData() == null){
            throw new BusinessException("Araba yok..");
        }
    }

    @Override
    public DataResult<CarCrashInformationGetDto> getByCarCrashInformationId(int carCrashInformationId) {

        CarCrashInformation result = this.carCrashInformationDao.getByCarCrashInformationId(carCrashInformationId);

        if (result == null) {
            return new ErrorDataResult<CarCrashInformationGetDto>("CarCrashInformation not found");
        }

        CarCrashInformationGetDto response = this.modelMapperService.forDto().map(result, CarCrashInformationGetDto.class);

        return new SuccessDataResult<CarCrashInformationGetDto>(response, "Success");
    }

    @Override
    public DataResult<List<CarCrashInformationListDto>> getAll() {

        List<CarCrashInformation> result = this.carCrashInformationDao.findAll();

        List<CarCrashInformationListDto> response = result.stream()
                .map(carCrashInformation -> this.modelMapperService.forDto().map(carCrashInformation, CarCrashInformationListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CarCrashInformationListDto>>(response, "Success");
    }

    @Override
    public DataResult<List<CarCrashInformationListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<CarCrashInformation> result = this.carCrashInformationDao.findAll(pageable).getContent();

        List<CarCrashInformationListDto> response = result.stream()
                .map(carCrashInformation -> this.modelMapperService.forDto().map(carCrashInformation, CarCrashInformationListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CarCrashInformationListDto>>(response, "Brands Listed Successfully");
    }

    @Override
    public DataResult<List<CarCrashInformationListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "carCrashInformationId");

        List<CarCrashInformation> result = this.carCrashInformationDao.findAll(s);

        List<CarCrashInformationListDto> response = result.stream()
                .map(carCrashInformation -> this.modelMapperService.forDto().map(carCrashInformation, CarCrashInformationListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CarCrashInformationListDto>>(response);
    }

    @Override
    public Result update(int carCrashInformationId, UpdateCarCrashInformationRequest updateCarCrashInformationRequest) throws BusinessException {

        checkIfCarCrashInformatioExists(carCrashInformationId);

        CarCrashInformation carCrashInformation = this.carCrashInformationDao.getByCarCrashInformationId(carCrashInformationId);

        CarCrashInformation carCrashInformationUpdate = this.modelMapperService.forRequest().map(updateCarCrashInformationRequest, CarCrashInformation.class);

        IdCorrector(carCrashInformation, carCrashInformationUpdate);

        this.carCrashInformationDao.save(carCrashInformationUpdate);

        return new SuccessResult(carCrashInformationUpdate.getCarCrashInformationId() + " updated..");
    }

    private void checkIfCarCrashInformatioExists(int carCrashInformationId) throws BusinessException {
        if(this.carCrashInformationDao.getByCarCrashInformationId(carCrashInformationId) == null){
            throw new BusinessException("Yok");
        }
    }

    private void IdCorrector(CarCrashInformation carCrashInformation, CarCrashInformation carCrashInformationUpdate) {
        carCrashInformationUpdate.setCarCrashInformationId(carCrashInformation.getCarCrashInformationId());
        carCrashInformationUpdate.setCar(carCrashInformation.getCar());
    }


    @Override
    public Result delete(DeleteCarCrashInformationRequest deleteCarCrashInformationRequest) throws BusinessException {

        checkIfCarCrashInformatioExists(deleteCarCrashInformationRequest.getCarCrashInformationId());

        CarCrashInformation carCrashInformation = this.modelMapperService.forRequest().map(deleteCarCrashInformationRequest, CarCrashInformation.class);

        this.carCrashInformationDao.deleteById(deleteCarCrashInformationRequest.getCarCrashInformationId());

        return new SuccessResult(deleteCarCrashInformationRequest.getCarCrashInformationId() + " deleted..");
    }
}
