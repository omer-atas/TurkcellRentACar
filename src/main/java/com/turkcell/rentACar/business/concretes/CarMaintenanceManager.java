package com.turkcell.rentACar.business.concretes;

import java.util.List;

import java.util.stream.Collectors;

import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.turkcell.rentACar.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.abstracts.RentService;
import com.turkcell.rentACar.business.dtos.carDtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceGetDto;
import com.turkcell.rentACar.business.dtos.carMaintenanceDtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.dtos.rentDtos.RentListDto;
import com.turkcell.rentACar.business.request.carMaintenanceRequests.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.carMaintenanceRequests.DeleteCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.carMaintenanceRequests.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarMaintenanceDao;
import com.turkcell.rentACar.entities.concretes.CarMaintenance;

@Service
public class CarMaintenanceManager implements CarMaintenanceService {

	private CarMaintenanceDao carMaintenanceDao;
	private ModelMapperService modelMapperService;
	private RentService rentService;
	private CarService carService;

	@Lazy 
	@Autowired
	public CarMaintenanceManager(CarMaintenanceDao carMaintenanceDao, ModelMapperService modelMapperService,
								 RentService rentService, CarService carService) {
		this.carMaintenanceDao = carMaintenanceDao;
		this.modelMapperService = modelMapperService;
		this.rentService = rentService;
		this.carService = carService;
	}

	@Override
	public Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException {

		checkIfCarExists(createCarMaintenanceRequest);
		checkIfCarRented(createCarMaintenanceRequest);

		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(createCarMaintenanceRequest,
				CarMaintenance.class);
		carMaintenance.setMaintanenceId(0);

		this.carMaintenanceDao.save(carMaintenance);

		return new SuccessResult(BusinessMessages.CAR_MAINTENANCE_ADD + carMaintenance.getMaintanenceId());
	}

	private void checkIfCarExists(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException {

		DataResult<CarGetDto> result = this.carService.getByCarId(createCarMaintenanceRequest.getCarId());

		if (!result.isSuccess()) {
			throw new BusinessException(BusinessMessages.CAR_NOT_FOUND);
		}
	}

	private void checkIfCarRented(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException {

		List<RentListDto> result = this.rentService.getByCar_CarId(createCarMaintenanceRequest.getCarId());

		if (result == null) {
			return;
		}

		for (RentListDto rent : result) {

			if ((rent.getEndDate() != null)
					&& (createCarMaintenanceRequest.getReturnDate().isAfter(rent.getStartingDate())
							|| createCarMaintenanceRequest.getReturnDate().equals(rent.getStartingDate()))
					&& (createCarMaintenanceRequest.getReturnDate().isBefore(rent.getEndDate())
							|| createCarMaintenanceRequest.getReturnDate().equals(rent.getEndDate()))) {
				throw new BusinessException(BusinessMessages.CAR_MAINTENANCE_CAR_RENTED);
			}

			if ((rent.getEndDate() == null)
					&& (createCarMaintenanceRequest.getReturnDate().isAfter(rent.getStartingDate())
							|| createCarMaintenanceRequest.getReturnDate().equals(rent.getStartingDate()))) {
				throw new BusinessException(
						BusinessMessages.CAR_MAINTENANCE_CAR_RENTED_NULL_DATE);
			}

		}

	}

	@Override
	public DataResult<List<CarMaintenanceListDto>> getAll() {

		List<CarMaintenance> result = this.carMaintenanceDao.findAll();

		List<CarMaintenanceListDto> response = result.stream().map(
				carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarMaintenanceListDto>>(response, BusinessMessages.CAR_MAINTENANCE_GET_ALL);

	}

	@Override
	public DataResult<List<CarMaintenanceListDto>> getAllPaged(int pageNo, int pageSize) {

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		List<CarMaintenance> result = this.carMaintenanceDao.findAll(pageable).getContent();

		List<CarMaintenanceListDto> response = result.stream().map(
				carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarMaintenanceListDto>>(response, BusinessMessages.CAR_MAINTENANCE_GET_ALL_PAGED);
	}

	@Override
	public DataResult<List<CarMaintenanceListDto>> getAllSorted(Direction direction) {

		Sort s = Sort.by(direction, "returnDate");

		List<CarMaintenance> result = this.carMaintenanceDao.findAll(s);

		List<CarMaintenanceListDto> response = result.stream().map(
				carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarMaintenanceListDto>>(response, BusinessMessages.CAR_MAINTENANCE_GET_ALL_SORTED);
	}

	@Override
	public boolean checkIfCarMaintenanceExists(int carMaintenanceId) throws BusinessException {

		if (this.carMaintenanceDao.getByMaintanenceId(carMaintenanceId) == null) {
			throw new BusinessException(BusinessMessages.CAR_MAINTENANCE_NOT_FOUND);
		}

		return true;
	}

	@Override
	public DataResult<List<CarMaintenanceListDto>> getByCar_CarId(int carId) {

		List<CarMaintenance> result = this.carMaintenanceDao.getByCar_CarId(carId);

		List<CarMaintenanceListDto> response = result.stream().map(
						carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarMaintenanceListDto>>(response);
	}

	@Override
	public Result update(int carMaintenanceId, UpdateCarMaintenanceRequest updateCarMaintenanceRequest)
			throws BusinessException {

		checkIfCarMaintenanceExists(carMaintenanceId);

		CarMaintenance carMaintenance = this.carMaintenanceDao.getByMaintanenceId(carMaintenanceId);

		checkIfParameterIsNull(updateCarMaintenanceRequest, carMaintenance);

		CarMaintenance carMaintenanceUpdate = this.modelMapperService.forRequest().map(updateCarMaintenanceRequest,
				CarMaintenance.class);

		IdCorrector(carMaintenance, carMaintenanceUpdate);

		this.carMaintenanceDao.save(carMaintenanceUpdate);

		return new SuccessResult(carMaintenanceUpdate.getMaintanenceId() + BusinessMessages.CAR_MAINTENANCE__UPDATE);
	}

	private UpdateCarMaintenanceRequest checkIfParameterIsNull(UpdateCarMaintenanceRequest updateCarMaintenanceRequest,
			CarMaintenance carMaintenance) {

		if (updateCarMaintenanceRequest.getDescription() == null) {
			updateCarMaintenanceRequest.setDescription(carMaintenance.getDescription());
		}

		if (updateCarMaintenanceRequest.getReturnDate() == null) {
			updateCarMaintenanceRequest.setReturnDate(carMaintenance.getReturnDate());
		}

		return updateCarMaintenanceRequest;
	}

	private void IdCorrector(CarMaintenance carMaintenance, CarMaintenance carMaintenanceUpdate) {
		carMaintenanceUpdate.setCar(carMaintenance.getCar());
		carMaintenanceUpdate.setMaintanenceId(carMaintenance.getMaintanenceId());
	}

	@Override
	public Result delete(DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) throws BusinessException {

		checkIfCarMaintenanceExists(deleteCarMaintenanceRequest.getCarMaintanenceId());

		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(deleteCarMaintenanceRequest,
				CarMaintenance.class);

		this.carMaintenanceDao.deleteById(carMaintenance.getMaintanenceId());

		return new SuccessResult(carMaintenance.getMaintanenceId() + BusinessMessages.CAR_MAINTENANCE_DELETE);

	}

	@Override
	public DataResult<CarMaintenanceGetDto> getByCarMaintenanceId(int carMaintanenceId) {

		CarMaintenance result = this.carMaintenanceDao.getByMaintanenceId(carMaintanenceId);

		if (result == null) {
			return new ErrorDataResult<CarMaintenanceGetDto>(BusinessMessages.CAR_MAINTENANCE_NOT_FOUND);
		}

		CarMaintenanceGetDto response = this.modelMapperService.forDto().map(result, CarMaintenanceGetDto.class);

		return new SuccessDataResult<CarMaintenanceGetDto>(response, BusinessMessages.CAR_MAINTENANCE_GET_BY_ID);
	}

	@Override
	public DataResult<List<CarMaintenanceListDto>> getByCarMaintenanceCarId(int carId) {

		List<CarMaintenance> result = this.carMaintenanceDao.getByCar_CarId(carId);

		if (result.isEmpty()) {
			return new ErrorDataResult<List<CarMaintenanceListDto>>(BusinessMessages.CAR_MAINTENANCE_CAR_NOT_FOUND);
		}

		List<CarMaintenanceListDto> response = result.stream().map(
				carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarMaintenanceListDto>>(response);
	}

}
