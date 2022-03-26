package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CarCrashInformationService;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
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
        carCrashInformation.setCarCrashInformationId(0);

        this.carCrashInformationDao.save(carCrashInformation);

        return new SuccessResult(BusinessMessages.CAR_CRASH_INFORMATION_ADD + carCrashInformation.getCarCrashInformationId());
    }

    private void checkIfCarExists(int carId) throws BusinessException {

        if(this.carService.getByCarId(carId).getData() == null){
            throw new BusinessException(BusinessMessages.CAR_NOT_FOUND);
        }
    }

    @Override
    public DataResult<CarCrashInformationGetDto> getByCarCrashInformationId(int carCrashInformationId) {

        CarCrashInformation result = this.carCrashInformationDao.getByCarCrashInformationId(carCrashInformationId);

        if (result == null) {
            return new ErrorDataResult<CarCrashInformationGetDto>(BusinessMessages.CAR_CRASH_INFORMATION_NOT_FOUND);
        }

        CarCrashInformationGetDto response = this.modelMapperService.forDto().map(result, CarCrashInformationGetDto.class);

        return new SuccessDataResult<CarCrashInformationGetDto>(response, BusinessMessages.CAR_CRASH_INFORMATION_GET_BY_ID);
    }

    @Override
    public DataResult<List<CarCrashInformationListDto>> getByCar_CarId(int carId) {

        List<CarCrashInformation> result = this.carCrashInformationDao.getByCar_CarId(carId);

        List<CarCrashInformationListDto> response = result.stream()
                .map(carCrashInformation -> this.modelMapperService.forDto().map(carCrashInformation, CarCrashInformationListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CarCrashInformationListDto>>(response);
    }

    @Override
    public DataResult<List<CarCrashInformationListDto>> getAll() {

        List<CarCrashInformation> result = this.carCrashInformationDao.findAll();

        List<CarCrashInformationListDto> response = result.stream()
                .map(carCrashInformation -> this.modelMapperService.forDto().map(carCrashInformation, CarCrashInformationListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CarCrashInformationListDto>>(response, BusinessMessages.CAR_CRASH_INFORMATION_GET_ALL);
    }

    @Override
    public DataResult<List<CarCrashInformationListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<CarCrashInformation> result = this.carCrashInformationDao.findAll(pageable).getContent();

        List<CarCrashInformationListDto> response = result.stream()
                .map(carCrashInformation -> this.modelMapperService.forDto().map(carCrashInformation, CarCrashInformationListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<CarCrashInformationListDto>>(response, BusinessMessages.CAR_CRASH_INFORMATION_GET_ALL_PAGED);
    }

    @Override
    public DataResult<List<CarCrashInformationListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "carCrashInformationId");

        List<CarCrashInformation> result = this.carCrashInformationDao.findAll(s);

        List<CarCrashInformationListDto> response = result.stream()
                .map(carCrashInformation -> this.modelMapperService.forDto().map(carCrashInformation, CarCrashInformationListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<CarCrashInformationListDto>>(response,BusinessMessages.CAR_CRASH_INFORMATION_GET_ALL_SORTED);
    }

    @Override
    public Result update(int carCrashInformationId, UpdateCarCrashInformationRequest updateCarCrashInformationRequest) throws BusinessException {

        checkIfCarCrashInformatioExists(carCrashInformationId);

        CarCrashInformation carCrashInformation = this.carCrashInformationDao.getByCarCrashInformationId(carCrashInformationId);

        CarCrashInformation carCrashInformationUpdate = this.modelMapperService.forRequest().map(updateCarCrashInformationRequest, CarCrashInformation.class);

        IdCorrector(carCrashInformation, carCrashInformationUpdate);

        this.carCrashInformationDao.save(carCrashInformationUpdate);

        return new SuccessResult(carCrashInformationUpdate.getCarCrashInformationId() + BusinessMessages.CAR_CRASH_INFORMATION_UPDATE);
    }

    private void checkIfCarCrashInformatioExists(int carCrashInformationId) throws BusinessException {
        if(this.carCrashInformationDao.getByCarCrashInformationId(carCrashInformationId) == null){
            throw new BusinessException(BusinessMessages.CAR_CRASH_INFORMATION_NOT_FOUND);
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

        this.carCrashInformationDao.deleteById(carCrashInformation.getCarCrashInformationId());

        return new SuccessResult(carCrashInformation.getCarCrashInformationId() + BusinessMessages.CAR_CRASH_INFORMATION_DELETE);
    }
}
