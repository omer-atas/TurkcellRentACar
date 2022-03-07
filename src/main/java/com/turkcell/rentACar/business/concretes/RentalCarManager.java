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
import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.abstracts.RentalCarService;
import com.turkcell.rentACar.business.dtos.CarGetDto;
import com.turkcell.rentACar.business.dtos.CarMaintenanceListDto;
import com.turkcell.rentACar.business.dtos.RentalCarGetDto;
import com.turkcell.rentACar.business.dtos.RentalCarListDto;
import com.turkcell.rentACar.business.request.CreateRentalCarRequest;
import com.turkcell.rentACar.business.request.DeleteRentalCarRequest;
import com.turkcell.rentACar.business.request.UpdateRentalCarRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.RentalCarDao;
import com.turkcell.rentACar.entities.concretes.RentalCar;

@Service
public class RentalCarManager implements RentalCarService {

	private RentalCarDao rentalCarDao;
	private ModelMapperService modelMapperService;
	private CarMaintenanceService carMaintenanceService;
	private CarService carService;

	@Autowired
	public RentalCarManager(RentalCarDao rentalCarDao, ModelMapperService modelMapperService,
			CarMaintenanceService carMaintenanceService, CarService carService) {
		this.rentalCarDao = rentalCarDao;
		this.modelMapperService = modelMapperService;
		this.carMaintenanceService = carMaintenanceService;
		this.carService = carService;
	}

	@Override
	public Result add(CreateRentalCarRequest createRentalCarRequest) throws BusinessException {

		RentalCar rentalCar = this.modelMapperService.forRequest().map(createRentalCarRequest, RentalCar.class);
		rentalCar.setRentalId(0);

		checkIfCarIsAvaliable(rentalCar.getCar().getCarId());
		checkIfInMaintenance(rentalCar);

		this.rentalCarDao.save(rentalCar);
		return new SuccessResult("Added : " + rentalCar.getRentalId());
	}

	private void checkIfCarIsAvaliable(int carId) throws BusinessException {

		DataResult<CarGetDto> result = this.carService.getByCarId(carId);

		if (!result.isSuccess()) {
			throw new BusinessException("The car with this id does not exist");
		}
	}

	private boolean checkIfInMaintenance(RentalCar rentalCar) throws BusinessException {

		List<CarMaintenanceListDto> result = this.carMaintenanceService.getByCar_CarId(rentalCar.getCar().getCarId());

		if (result == null) {
			return true;
		}

		for (CarMaintenanceListDto carMaintenance : result) {

			if (rentalCar.getStartingDate().isBefore(carMaintenance.getReturnDate())
					|| rentalCar.getEndDate().isBefore(carMaintenance.getReturnDate())) {
				throw new BusinessException("This car cannot be rented as it is under maintenance.");
			}

		}

		return true;
	}

	@Override
	public DataResult<RentalCarGetDto> getByRentalId(int rentalId) {

		RentalCar result = this.rentalCarDao.getByRentalId(rentalId);

		if (result == null) {
			return new ErrorDataResult<RentalCarGetDto>("Car with submitted id not found");
		}

		RentalCarGetDto response = this.modelMapperService.forDto().map(result, RentalCarGetDto.class);

		return new SuccessDataResult<RentalCarGetDto>(response, "Success");
	}

	@Override
	public DataResult<List<RentalCarListDto>> getAll() {

		List<RentalCar> result = this.rentalCarDao.findAll();

		if (result.isEmpty()) {
			return new ErrorDataResult<List<RentalCarListDto>>("Rental Cars not listed");
		}

		List<RentalCarListDto> response = result.stream()
				.map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<RentalCarListDto>>(response, "Rental Cars Listed successfully..");
	}

	@Override
	public DataResult<List<RentalCarListDto>> getAllPaged(int pageNo, int pageSize) {

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		List<RentalCar> result = this.rentalCarDao.findAll(pageable).getContent();

		if (result.isEmpty()) {
			return new ErrorDataResult<List<RentalCarListDto>>("Rental Cars not listed");
		}

		List<RentalCarListDto> response = result.stream()
				.map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<RentalCarListDto>>(response, "Rental Cars listed successfully..");
	}

	@Override
	public DataResult<List<RentalCarListDto>> getAllSorted(Direction direction) {

		Sort s = Sort.by(direction, "returnDate");

		List<RentalCar> result = this.rentalCarDao.findAll(s);

		if (result.isEmpty()) {
			return new ErrorDataResult<List<RentalCarListDto>>("Rental Cars not listed");
		}

		List<RentalCarListDto> response = result.stream()
				.map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<RentalCarListDto>>(response, "Rental Cars listed successfully..");
	}

	@Override
	public List<RentalCarListDto> getByCar_CarId(int carId) {

		List<RentalCar> result = this.rentalCarDao.getByCar_CarId(carId);

		if (result.isEmpty()) {
			return null;
		}

		List<RentalCarListDto> response = result.stream()
				.map(rentalCar -> this.modelMapperService.forDto().map(rentalCar, RentalCarListDto.class))
				.collect(Collectors.toList());

		return response;
	}

	@Override
	public Result update(int rentalId, UpdateRentalCarRequest updateRentalCarRequest) throws BusinessException {

		RentalCar rentalCar = this.rentalCarDao.getByRentalId(rentalId);

		RentalCar rentalCarUpdate = this.modelMapperService.forRequest().map(updateRentalCarRequest, RentalCar.class);

		IdCorrector(rentalCar, rentalCarUpdate);

		this.rentalCarDao.save(rentalCarUpdate);
		return new SuccessResult(rentalCarUpdate.getRentalId() + " updated..");
	}

	private void IdCorrector(RentalCar rentalCar, RentalCar rentalCarUpdate) {
		rentalCarUpdate.setCar(rentalCar.getCar());
		rentalCarUpdate.setRentalId(rentalCar.getRentalId());
	}

	@Override
	public boolean checkIfCarAvaliable(int carId) throws BusinessException {

		if (this.carService.getByCarId(carId) == null) {
			throw new BusinessException("Araba yok");
		}
		return true;

	}

	@Override
	public Result delete(DeleteRentalCarRequest deleteRentalCarRequest) throws BusinessException {

		RentalCar rentalCar = this.modelMapperService.forRequest().map(deleteRentalCarRequest, RentalCar.class);

		checkIfRentalCar(deleteRentalCarRequest.getRentalId());

		this.rentalCarDao.deleteById(rentalCar.getRentalId());

		return new SuccessResult(deleteRentalCarRequest.getRentalId() + " deleted..");
	}

	@Override
	public boolean checkIfRentalCar(int rentalId) throws BusinessException {

		if (this.rentalCarDao.getByRentalId(rentalId) == null) {
			throw new BusinessException("The rental car with this ID is not available..");
		}

		return true;
	}

}
