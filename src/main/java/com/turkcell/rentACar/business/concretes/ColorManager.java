package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.ColorService;
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

	@Autowired
	public ColorManager(ColorDao colorDao, ModelMapperService modelMapperService) {
		this.colorDao = colorDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreateColorRequest createColorRequest) throws BusinessException {

		Color color = this.modelMapperService.forRequest().map(createColorRequest, Color.class);

		checkIfNameNotDuplicated(createColorRequest.getColorName());

		this.colorDao.save(color);
		return new SuccessResult("Color added : " + color.getColorName());

	}

	@Override
	public boolean checkIfNameNotDuplicated(String colorName) throws BusinessException {

		var response = this.colorDao.existsByColorName(colorName);

		if (response) {
			throw new BusinessException("Names can't be the same");
		}

		return true;

	}

	@Override
	public DataResult<List<ColorListDto>> getAll() {

		List<Color> result = this.colorDao.findAll();

		if (result.isEmpty()) {
			return new ErrorDataResult<List<ColorListDto>>("Colors not list");
		}

		List<ColorListDto> response = result.stream()
				.map(color -> this.modelMapperService.forDto().map(color, ColorListDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<ColorListDto>>(response, "Colors Listed Successfully");
	}

	@Override
	public DataResult<List<ColorListDto>> getAllPaged(int pageNo, int pageSize) {

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		List<Color> result = this.colorDao.findAll(pageable).getContent();

		if (result.isEmpty()) {
			return new ErrorDataResult<List<ColorListDto>>("Colors not list - getAllPaged - ");
		}

		List<ColorListDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, ColorListDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<ColorListDto>>(response, "Colors Listed Successfully");
	}

	@Override
	public DataResult<List<ColorListDto>> getAllSorted(Direction direction) {

		Sort s = Sort.by(direction, "colorName");

		List<Color> result = this.colorDao.findAll(s);

		if (result.isEmpty()) {
			return new ErrorDataResult<List<ColorListDto>>("Colors not list - getAllSorted -");
		}

		List<ColorListDto> response = result.stream()
				.map(product -> this.modelMapperService.forDto().map(product, ColorListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ColorListDto>>(response, "Colors Listed Successfully");
	}

	@Override
	public DataResult<ColorGetDto> getByColorId(int colorId) {

		Color result = this.colorDao.getByColorId(colorId);

		if (result == null) {
			return new ErrorDataResult<ColorGetDto>("There is no color in the id sent");
		}

		ColorGetDto response = this.modelMapperService.forDto().map(result, ColorGetDto.class);
		return new SuccessDataResult<ColorGetDto>(response, "Success");
	}

	@Override
	public Result update(int colorId, UpdateColorRequest updateColorRequest) throws BusinessException {

		Color color = this.colorDao.getByColorId(colorId);

		Color colorUpdate = this.modelMapperService.forRequest().map(updateColorRequest, Color.class);

		IdCorrector(color, colorUpdate);
		checkIfNameNotDuplicated(updateColorRequest.getColorName());

		this.colorDao.save(colorUpdate);
		return new SuccessResult(updateColorRequest.getColorName() + " updated..");

	}

	private void IdCorrector(Color color, Color colorUpdate) {
		colorUpdate.setColorId(color.getColorId());
	}

	@Override
	public boolean checkIfIsData(int colorId) throws BusinessException {

		if (this.colorDao.getByColorId(colorId) == null) {
			throw new BusinessException("There is no color in the id sent");
		}

		return true;

	}

	@Override
	public Result delete(DeleteColorRequest deleteColorRequest) throws BusinessException {

		Color color = this.modelMapperService.forRequest().map(deleteColorRequest, Color.class);

		checkIfIsData(color.getColorId());

		this.colorDao.deleteById(color.getColorId());
		return new SuccessResult(deleteColorRequest.getColorId() + " deleted..");

	}

}
