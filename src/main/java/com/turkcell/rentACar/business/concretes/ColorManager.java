package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.ColorService;

import com.turkcell.rentACar.business.dtos.ColorGetDto;
import com.turkcell.rentACar.business.dtos.ColorListDto;
import com.turkcell.rentACar.business.request.CreateColorRequest;
import com.turkcell.rentACar.business.request.DeleteColorRequest;
import com.turkcell.rentACar.business.request.UpdateColorRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.CarDao;
import com.turkcell.rentACar.dataAccess.abstracts.ColorDao;
import com.turkcell.rentACar.entities.concretes.Color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorManager implements ColorService {

	private ColorDao colorDao;
	private ModelMapperService modelMapperService;
	private CarDao carDao;

	@Autowired
	public ColorManager(ColorDao colorDao, ModelMapperService modelMapperService, CarDao carDao) {
		this.colorDao = colorDao;
		this.modelMapperService = modelMapperService;
		this.carDao = carDao;
	}

	@Override
	public Result add(CreateColorRequest createColorRequest) throws BusinessException {

		Color color = this.modelMapperService.forRequest().map(createColorRequest, Color.class);

		checkIfEmpty(color);
		checkIfNameNotDuplicated(createColorRequest.getColorName());

		this.colorDao.save(color);
		return new SuccessResult("Color added : " + color.getColorName());

	}

	private Color checkIfEmpty(Color result) throws BusinessException {

		if (result == null) {
			throw new BusinessException("No data");
		}

		return result;
	}

	private List<Color> checkIfListEmpty(List<Color> result) throws BusinessException {

		if (result == null) {
			throw new BusinessException("No data");
		}

		return result;
	}

	private boolean checkIfNameNotDuplicated(String colorName) throws BusinessException {

		var response = this.colorDao.existsByColorName(colorName);

		if (response) {
			throw new BusinessException("Names can't be the same");
		}

		return true;

	}

	@Override
	public DataResult<List<ColorListDto>> getAll() throws BusinessException {

		List<Color> result = this.colorDao.findAll();

		checkIfListEmpty(result);

		List<ColorListDto> response = result.stream()
				.map(color -> this.modelMapperService.forDto().map(color, ColorListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<ColorListDto>>(response, "Success");
	}

	@Override
	public DataResult<ColorGetDto> getByColorId(int colorId) throws BusinessException {

		Color result = this.colorDao.getByColorId(colorId);

		checkIfEmpty(result);

		ColorGetDto response = this.modelMapperService.forDto().map(result, ColorGetDto.class);
		return new SuccessDataResult<ColorGetDto>(response, "Success");
	}

	@Override
	public Result update(UpdateColorRequest updateColorRequest) throws BusinessException {

		Color color = this.modelMapperService.forRequest().map(updateColorRequest, Color.class);

		checkIfIsData(color.getColorId());
		checkIfNameNotDuplicated(updateColorRequest.getColorName());

		this.colorDao.save(color);
		return new SuccessResult(updateColorRequest.getColorName() + " updated..");

	}

	private boolean checkIfIsData(int colorId) throws BusinessException {

		if (this.colorDao.getByColorId(colorId) == null) {
			throw new BusinessException("There is no data in the id sent");
		}

		return true;

	}

	private void checkIfUseOnCar(int colorId) throws BusinessException {
		if (this.carDao.getByColor_ColorId(colorId) != null) {
			throw new BusinessException("This color is used in the car class..");
		}
	}

	@Override
	public Result delete(DeleteColorRequest deleteColorRequest) throws BusinessException {

		Color color = this.modelMapperService.forRequest().map(deleteColorRequest, Color.class);

		checkIfIsData(color.getColorId());
		checkIfUseOnCar(deleteColorRequest.getColorId());

		this.colorDao.deleteById(color.getColorId());
		return new SuccessResult(deleteColorRequest.getColorId() + " deleted..");

	}
}
