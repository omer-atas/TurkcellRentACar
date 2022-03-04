package com.turkcell.rentACar.business.concretes;

import java.util.List;


import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.turkcell.rentACar.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACar.business.dtos.CarMaintenanceGetDto;
import com.turkcell.rentACar.business.dtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.request.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.DeleteCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.UpdateCarMaintenanceRequest;
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

	@Autowired
	public CarMaintenanceManager(CarMaintenanceDao carMaintenanceDao, ModelMapperService modelMapperService) {
		this.carMaintenanceDao = carMaintenanceDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException {

		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(createCarMaintenanceRequest,
				CarMaintenance.class);

		this.carMaintenanceDao.save(carMaintenance);
		return new SuccessResult("Added : " + carMaintenance.getMaintanenceId());
	}

	@Override
	public DataResult<List<CarMaintenanceListDto>> getAll() {

		List<CarMaintenance> result = this.carMaintenanceDao.findAll();

		if (result.isEmpty()) {
			return new ErrorDataResult<List<CarMaintenanceListDto>>("Maintenances not listed");
		}

		List<CarMaintenanceListDto> response = result.stream().map(
				carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarMaintenanceListDto>>(response, "Car maintenance listed successfully..");

	}
	
	@Override
	public DataResult<List<CarMaintenanceListDto>> getAllPaged(int pageNo, int pageSize) {

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		List<CarMaintenance> result = this.carMaintenanceDao.findAll(pageable).getContent();

		if (result.isEmpty()) {
			return new ErrorDataResult<List<CarMaintenanceListDto>>("Maintenances not listed");
		}

		List<CarMaintenanceListDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, CarMaintenanceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarMaintenanceListDto>>(response, "Car maintenance listed successfully..");
	}
	
	@Override
	public DataResult<List<CarMaintenanceListDto>> getAllSorted(Direction direction) {

		Sort s = Sort.by(direction, "returnDate");

		List<CarMaintenance> result = this.carMaintenanceDao.findAll(s);

		if (result.isEmpty()) {
			return new ErrorDataResult<List<CarMaintenanceListDto>>("Maintenances not listed");
		}

		List<CarMaintenanceListDto> response = result.stream()
				.map(product -> this.modelMapperService.forDto().map(product, CarMaintenanceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarMaintenanceListDto>>(response, "Car maintenance listed successfully..");
	}

	@Override
	public boolean checkIfCarMaintenance(int carMaintenanceId) throws BusinessException {

		if (this.carMaintenanceDao.getByMaintanenceId(carMaintenanceId) == null) {
			throw new BusinessException("The car maintenance with this ID is not available..");
		}

		return true;
	}

	@Override
	public Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException {

		checkIfCarMaintenance(updateCarMaintenanceRequest.getCarMaintanenceId());

		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(updateCarMaintenanceRequest,
				CarMaintenance.class);

		this.carMaintenanceDao.save(carMaintenance);
		return new SuccessResult(carMaintenance.getMaintanenceId() + " updated..");
	}

	@Override
	public Result delete(DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) throws BusinessException {

		checkIfCarMaintenance(deleteCarMaintenanceRequest.getCarMaintanenceId());

		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(deleteCarMaintenanceRequest,
				CarMaintenance.class);

		this.carMaintenanceDao.deleteById(carMaintenance.getMaintanenceId());
		return new SuccessResult(carMaintenance.getMaintanenceId() + " deleted..");

	}

	

	/*
	 * model mapper misatake solution private List<CarMaintenanceListDto>
	 * updateCarCarMaintenance(List<CarMaintenance> result,
	 * List<CarMaintenanceListDto> response) {
	 * 
	 * for (int i = 0; i < result.size(); i++) {
	 * response.get(i).setCarId(result.get(i).getCar().getCarId()); } return
	 * response;
	 * 
	 * }
	 */

	

	@Override
	public DataResult<CarMaintenanceGetDto> getByCarMaintenanceId(int carMaintanenceId) {

		CarMaintenance result = this.carMaintenanceDao.getByMaintanenceId(carMaintanenceId);

		if (result == null) {
			return new ErrorDataResult<CarMaintenanceGetDto>("Maintenances not listed");
		}

		CarMaintenanceGetDto response = this.modelMapperService.forDto().map(result, CarMaintenanceGetDto.class);
		response.setCarId(result.getMaintanenceId());

		return new SuccessDataResult<CarMaintenanceGetDto>(response, "Success");
	}

	@Override
	public DataResult<List<CarMaintenanceListDto>> getByCarMaintenanceCarId(int carId) {

		List<CarMaintenance> result = this.carMaintenanceDao.getByCar_CarId(carId);

		if (result.isEmpty()) {
			return new ErrorDataResult<List<CarMaintenanceListDto>>("Maintenances not listed");
		}

		List<CarMaintenanceListDto> response = result.stream()
				.map(product -> this.modelMapperService.forDto().map(product, CarMaintenanceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarMaintenanceListDto>>(response, "Success");
	}

}
