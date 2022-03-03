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
import com.turkcell.rentACar.business.dtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.request.CreateCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.DeleteCarMaintenanceRequest;
import com.turkcell.rentACar.business.request.UpdateCarMaintenanceRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarDao;
import com.turkcell.rentACar.dataAccess.abstracts.CarMaintenanceDao;
import com.turkcell.rentACar.entities.concretes.CarMaintenance;

@Service
public class CarMaintenanceManager implements CarMaintenanceService {

	private CarMaintenanceDao carMaintenanceDao;
	private ModelMapperService modelMapperService;
	private CarDao carDao;

	@Autowired
	public CarMaintenanceManager(CarMaintenanceDao carMaintenanceDao, ModelMapperService modelMapperService,
			CarDao carDao) {
		this.carMaintenanceDao = carMaintenanceDao;
		this.modelMapperService = modelMapperService;
		this.carDao = carDao;
	}

	@Override
	public Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException {

		checkIfIsCar(createCarMaintenanceRequest.getCarId());

		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(createCarMaintenanceRequest,
				CarMaintenance.class);
		
		this.carMaintenanceDao.save(carMaintenance);
		return new SuccessResult("Added : " + carMaintenance.getCarMaintanenceId());
	}

	private boolean checkIfIsCar(int carId) throws BusinessException {
		
		if (this.carDao.getByCarId(carId) == null) {
			throw new BusinessException("The car with this ID is not available..");
		}

		return true;
	}

	@Override
	public DataResult<List<CarMaintenanceListDto>> getByCarMaintenanceCarId(int carId) throws BusinessException {

		List<CarMaintenance> result = this.carMaintenanceDao.getByCar_CarId(carId);

		if (result.isEmpty()) {
			throw new BusinessException("No data");
		}

		List<CarMaintenanceListDto> response = result.stream().map(
				carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CarMaintenanceListDto>>(response,
				"Successfully retrieved maintenance information for this car..");

	}

	@Override
	public DataResult<List<CarMaintenanceListDto>> getAll() throws BusinessException {

		List<CarMaintenance> result = this.carMaintenanceDao.findAll();

		if (result.isEmpty()) {
			throw new BusinessException("No data");
		}

		List<CarMaintenanceListDto> response = result.stream().map(
				carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<CarMaintenanceListDto>>(response, "Car maintenance listed successfully..");

	}

	private boolean checkIfCarMaintenance(int carMaintenanceId) throws BusinessException {
		
		if (this.carMaintenanceDao.getByCarMaintanenceId(carMaintenanceId) == null) {
			throw new BusinessException("The car maintenance with this ID is not available..");
		}

		return true;
	}

	@Override
	public Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws BusinessException {

		checkIfCarMaintenance(updateCarMaintenanceRequest.getCarMaintanenceId());
		
		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(updateCarMaintenanceRequest,
				CarMaintenance.class);

		if (!checkIfIsData(updateCarMaintenanceRequest.getCarId())) {
			return null;
		}

		this.carMaintenanceDao.save(carMaintenance);
		return new SuccessResult(carMaintenance.getCarMaintanenceId() + " updated..");
	}

	@Override
	public Result delete(DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) throws BusinessException {

		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(deleteCarMaintenanceRequest,
				CarMaintenance.class);

		if (!checkIfIsData(deleteCarMaintenanceRequest.getCarMaintanenceId())) {
			return null;
		} else {
			this.carMaintenanceDao.deleteById(carMaintenance.getCarMaintanenceId());
			return new SuccessResult(carMaintenance.getCarMaintanenceId() + " deleted..");
		}

	}

	private boolean checkIfIsData(int id) throws BusinessException {
		
		if (this.carMaintenanceDao.getByCarMaintanenceId(id) == null) {
			throw new BusinessException("There is no data in the id sent");
		}

		return true;
	}

	@Override
	public DataResult<List<CarMaintenanceListDto>> getAllPaged(int pageNo, int pageSize) throws BusinessException {

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		List<CarMaintenance> result = this.carMaintenanceDao.findAll(pageable).getContent();

		if (result.isEmpty()) {
			throw new BusinessException("No data");
		}

		List<CarMaintenanceListDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, CarMaintenanceListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<CarMaintenanceListDto>>(response);
	}

	@Override
	public DataResult<List<CarMaintenanceListDto>> getAllSorted(Direction direction) throws BusinessException {

		Sort s = Sort.by(direction, "returnDate");

		List<CarMaintenance> result = this.carMaintenanceDao.findAll(s);

		if (result.isEmpty()) {
			throw new BusinessException("No data");
		}

		List<CarMaintenanceListDto> response = result.stream()
				.map(product -> this.modelMapperService.forDto().map(product, CarMaintenanceListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarMaintenanceListDto>>(response);
	}

}
