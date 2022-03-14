package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.userDtos.UserGetDto;
import com.turkcell.rentACar.business.dtos.userDtos.UserListDto;
import com.turkcell.rentACar.business.request.userRequests.CreateUserRequest;
import com.turkcell.rentACar.business.request.userRequests.DeleteUserRequest;
import com.turkcell.rentACar.business.request.userRequests.UpdateUserRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.results.DataResult;
import com.turkcell.rentACar.core.utilities.results.Result;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserService {

    Result add(CreateUserRequest createUserRequest) throws BusinessException;

    DataResult<UserGetDto> getByUserId(int userId);

    DataResult<List<UserListDto>> getAll();

    DataResult<List<UserListDto>> getAllPaged(int pageNo, int pageSize);

    DataResult<List<UserListDto>> getAllSorted(Sort.Direction direction);

    Result update(int userId, UpdateUserRequest updateUserRequest) throws BusinessException;

    Result delete(DeleteUserRequest deleteUserRequest) throws BusinessException;
}
