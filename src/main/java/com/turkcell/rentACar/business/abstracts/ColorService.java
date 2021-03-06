package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.colorDtos.ColorGetDto;
import com.turkcell.rentACar.business.dtos.colorDtos.ColorListDto;
import com.turkcell.rentACar.business.request.colorRequests.CreateColorRequest;
import com.turkcell.rentACar.business.request.colorRequests.DeleteColorRequest;
import com.turkcell.rentACar.business.request.colorRequests.UpdateColorRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;

import java.util.List;

import org.springframework.data.domain.Sort;

public interface ColorService {

	Result add(CreateColorRequest createColorRequest) throws BusinessException;

	DataResult<List<ColorListDto>> getAll();

	DataResult<List<ColorListDto>> getAllPaged(int pageNo, int pageSize);

	DataResult<List<ColorListDto>> getAllSorted(Sort.Direction direction);

	DataResult<ColorGetDto> getByColorId(int colorId);

	Result update(int colorId, UpdateColorRequest updateColorRequest) throws BusinessException;

	boolean checkIfColorExists(int colorId) throws BusinessException;

	Result delete(DeleteColorRequest deleteColorRequest) throws BusinessException;
}
