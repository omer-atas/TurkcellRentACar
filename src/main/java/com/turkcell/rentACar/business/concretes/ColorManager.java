package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CarService;
import com.turkcell.rentACar.business.abstracts.ColorService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorGetDto;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorListDto;
import com.turkcell.rentACar.business.request.colorRequests.CreateColorRequest;
import com.turkcell.rentACar.business.request.colorRequests.DeleteColorRequest;
import com.turkcell.rentACar.business.request.colorRequests.UpdateColorRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import com.turkcell.rentACar.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACar.core.utilities.results.SuccessResult;
import com.turkcell.rentACar.dataAccess.abstracts.ColorDao;
import com.turkcell.rentACar.entities.concretes.Color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorManager implements ColorService {

	private ColorDao colorDao;
	private ModelMapperService modelMapperService;
	private CarService carService;

	@Autowired
	public ColorManager(ColorDao colorDao, ModelMapperService modelMapperService,CarService carService) {
		this.colorDao = colorDao;
		this.modelMapperService = modelMapperService;
		this.carService = carService;
	}

	@Override
	public Result add(CreateColorRequest createColorRequest) throws BusinessException {

		checkIfNameNotDuplicated(createColorRequest.getColorName());

		Color color = this.modelMapperService.forRequest().map(createColorRequest, Color.class);
		color.setColorId(0);

		this.colorDao.save(color);

		return new SuccessResult(BusinessMessages.COLOR_ADD + color.getColorName());

	}

	public void checkIfNameNotDuplicated(String colorName) throws BusinessException {

		var response = this.colorDao.existsByColorName(colorName);

		if (response) {
			throw new BusinessException(BusinessMessages.COLOR_NAME_NOT_DUPLICATED);
		}

	}

	@Override
	public DataResult<List<ColorListDto>> getAll() {

		List<Color> result = this.colorDao.findAll();

		List<ColorListDto> response = result.stream()
				.map(color -> this.modelMapperService.forDto().map(color, ColorListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ColorListDto>>(response, BusinessMessages.COLOR_GET_ALL);
	}

	@Override
	public DataResult<List<ColorListDto>> getAllPaged(int pageNo, int pageSize) {

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		List<Color> result = this.colorDao.findAll(pageable).getContent();

		List<ColorListDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, ColorListDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<ColorListDto>>(response, BusinessMessages.COLOR_GET_ALL_PAGED);
	}

	@Override
	public DataResult<List<ColorListDto>> getAllSorted(Direction direction) {

		Sort s = Sort.by(direction, "colorName");

		List<Color> result = this.colorDao.findAll(s);

		List<ColorListDto> response = result.stream()
				.map(color -> this.modelMapperService.forDto().map(color, ColorListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ColorListDto>>(response, BusinessMessages.COLOR_GET_ALL_SORTED);
	}

	@Override
	public DataResult<ColorGetDto> getByColorId(int colorId) {

		Color result = this.colorDao.getByColorId(colorId);

		if (result == null) {
			return new ErrorDataResult<ColorGetDto>(BusinessMessages.COLOR_NOT_FOUND);
		}

		ColorGetDto response = this.modelMapperService.forDto().map(result, ColorGetDto.class);
		return new SuccessDataResult<ColorGetDto>(response, BusinessMessages.COLOR_GET_BY_ID);
	}

	@Override
	public Result update(int colorId, UpdateColorRequest updateColorRequest) throws BusinessException {

		checkIfColorExists(colorId);

		Color color = this.colorDao.getByColorId(colorId);

		Color colorUpdate = this.modelMapperService.forRequest().map(updateColorRequest, Color.class);

		IdCorrector(color, colorUpdate);
		checkIfNameNotDuplicated(updateColorRequest.getColorName());

		this.colorDao.save(colorUpdate);

		return new SuccessResult(updateColorRequest.getColorName() + BusinessMessages.COLOR_UPDATE);

	}

	private void IdCorrector(Color color, Color colorUpdate) {
		colorUpdate.setColorId(color.getColorId());
	}

	@Override
	public boolean checkIfColorExists(int colorId) throws BusinessException {

		if (this.colorDao.getByColorId(colorId) == null) {
			throw new BusinessException(BusinessMessages.COLOR_NOT_FOUND);
		}

		return true;

	}

	@Override
	public Result delete(DeleteColorRequest deleteColorRequest) throws BusinessException {

		checkIfColorExists(deleteColorRequest.getColorId());
		checkIfThereIsACarWithThisColor(deleteColorRequest.getColorId());

		Color color = this.modelMapperService.forRequest().map(deleteColorRequest, Color.class);

		this.colorDao.deleteById(color.getColorId());

		return new SuccessResult(deleteColorRequest.getColorId() + BusinessMessages.COLOR_DELETE);

	}

	private void checkIfThereIsACarWithThisColor(int colorId) throws BusinessException {

		if(!this.carService.getByColor_ColorId(colorId).getData().isEmpty()){
			throw new BusinessException(BusinessMessages.IS_THERE_A_COLOR_OF_CAR_AVAILABLE);
		}
	}

}
