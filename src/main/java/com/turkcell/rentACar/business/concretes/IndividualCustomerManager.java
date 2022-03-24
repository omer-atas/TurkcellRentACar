package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CustomerService;
import com.turkcell.rentACar.business.abstracts.IndividualCustomerService;
import com.turkcell.rentACar.business.constants.messages.BusinessMessages;
import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerGetDto;
import com.turkcell.rentACar.business.dtos.individualCustomerDtos.IndividualCustomerListDto;
import com.turkcell.rentACar.business.request.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.turkcell.rentACar.business.request.individualCustomerRequests.DeleteIndividualCustomerRequest;
import com.turkcell.rentACar.business.request.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.turkcell.rentACar.core.exception.BusinessException;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.core.utilities.results.*;
import com.turkcell.rentACar.dataAccess.abstracts.IndividualCustomerDao;
import com.turkcell.rentACar.entities.concretes.IndividualCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndividualCustomerManager implements IndividualCustomerService {

    private IndividualCustomerDao individualCustomerDao;
    private ModelMapperService modelMapperService;
    private CustomerService customerService;

    @Autowired
    public IndividualCustomerManager(IndividualCustomerDao individualCustomerDao, ModelMapperService modelMapperService,CustomerService customerService) {
        this.individualCustomerDao = individualCustomerDao;
        this.modelMapperService = modelMapperService;
        this.customerService = customerService;
    }

    @Override
    public Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) throws BusinessException {

        checkIfNationalIdentityNotDuplicated(createIndividualCustomerRequest.getNationalIdentity());
        checkIfNationalIdentityNumberRegex(createIndividualCustomerRequest.getNationalIdentity());
        checkIfEmailExists(createIndividualCustomerRequest.getEmail());

        IndividualCustomer individualCustomer = this.modelMapperService.forRequest().map(createIndividualCustomerRequest, IndividualCustomer.class);
        individualCustomer.setCustomerId(0);

        this.individualCustomerDao.save(individualCustomer);

        return new SuccessResult(BusinessMessages.INDIVIDUAL_CUSTOMER_ADD + individualCustomer.getNationalIdentity());
    }

    private void checkIfEmailExists(String email) throws BusinessException {
        if(this.customerService.existsByEmail(email)){
            throw new BusinessException(BusinessMessages.USER_EMAİL_EXİSTS);
        }
    }

    private void checkIfNationalIdentityNumberRegex(String nationalIdentity) throws BusinessException {

        String identityNumber = new String("nationalIdentity");
        boolean matches = identityNumber.matches("^[1-9]{1}[0-9]{9}[02468]{1}$");

        if(matches){
            throw new BusinessException(BusinessMessages.INDIVIDUAL_CUSTOMER_NATIONAL_IDENTITY_REGEX);
        }


    }

    private void checkIfNationalIdentityNotDuplicated(String nationalIdentity) throws BusinessException {
        if(this.individualCustomerDao.existsByNationalIdentity(nationalIdentity) == true){
            throw new BusinessException(BusinessMessages.INDIVIDUAL_CUSTOMER_NATIONAL_IDENTITY_NOT_DUPLICATED);
        }
    }

    @Override
    public DataResult<IndividualCustomerGetDto> getByIndividualCustomerId(int individualCustomerId){

        IndividualCustomer result = this.individualCustomerDao.getByCustomerId(individualCustomerId);

        if (result == null) {
            return new ErrorDataResult<IndividualCustomerGetDto>(BusinessMessages.INDIVIDUAL_CUSTOMER_NOT_FOUND);
        }

        IndividualCustomerGetDto response = this.modelMapperService.forDto().map(result, IndividualCustomerGetDto.class);

        return new SuccessDataResult<IndividualCustomerGetDto>(response, BusinessMessages.INDIVIDUAL_CUSTOMER_GET_BY_ID);
    }

    @Override
    public DataResult<List<IndividualCustomerListDto>> getAll() {

        List<IndividualCustomer> result = this.individualCustomerDao.findAll();

        List<IndividualCustomerListDto> response = result.stream()
                .map(individualCustomer -> this.modelMapperService.forDto().map(individualCustomer, IndividualCustomerListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<IndividualCustomerListDto>>(response, BusinessMessages.INDIVIDUAL_CUSTOMER_GET_ALL);
    }

    @Override
    public DataResult<List<IndividualCustomerListDto>> getAllPaged(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<IndividualCustomer> result = this.individualCustomerDao.findAll(pageable).getContent();

        List<IndividualCustomerListDto> response = result.stream()
                .map(individualCustomer -> this.modelMapperService.forDto().map(individualCustomer, IndividualCustomerListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<List<IndividualCustomerListDto>>(response, BusinessMessages.INDIVIDUAL_CUSTOMER_GET_ALL_PAGED);
    }

    @Override
    public DataResult<List<IndividualCustomerListDto>> getAllSorted(Sort.Direction direction) {

        Sort s = Sort.by(direction, "nationalIdentity");

        List<IndividualCustomer> result = this.individualCustomerDao.findAll(s);

        List<IndividualCustomerListDto> response = result.stream()
                .map(individualCustomer -> this.modelMapperService.forDto().map(individualCustomer, IndividualCustomerListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<IndividualCustomerListDto>>(response,BusinessMessages.INDIVIDUAL_CUSTOMER_GET_ALL_SORTED);
    }

    @Override
    public Result update(int individualCustomerId, UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws BusinessException {

        checkIfIndividualCustomerExists(individualCustomerId);
        checkIfNationalIdentityNotDuplicated(updateIndividualCustomerRequest.getNationalIdentity());
        checkIfNationalIdentityNumberRegex(updateIndividualCustomerRequest.getNationalIdentity());

        IndividualCustomer individualCustomer = this.individualCustomerDao.getByCustomerId(individualCustomerId);

        IndividualCustomer individualCustomerUpdate = this.modelMapperService.forRequest().map(updateIndividualCustomerRequest, IndividualCustomer.class);

        IdCorrector(individualCustomer, individualCustomerUpdate);

        this.individualCustomerDao.save(individualCustomerUpdate);

        return new SuccessResult(updateIndividualCustomerRequest.getNationalIdentity() + BusinessMessages.INDIVIDUAL_CUSTOMER_UPDATE);
    }

    private void IdCorrector(IndividualCustomer individualCustomer, IndividualCustomer individualCustomerUpdate) {
        individualCustomerUpdate.setCustomerId(individualCustomer.getCustomerId());
    }

    private void checkIfIndividualCustomerExists(int individualCustomerId) throws BusinessException {
        if(this.individualCustomerDao.getByCustomerId(individualCustomerId) == null){
            throw new BusinessException(BusinessMessages.INDIVIDUAL_CUSTOMER_NOT_FOUND);
        }
    }

    @Override
    public Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) throws BusinessException {

        checkIfIndividualCustomerExists(deleteIndividualCustomerRequest.getIndividualCustomerId());

        IndividualCustomer individualCustomer = this.modelMapperService.forRequest().map(deleteIndividualCustomerRequest, IndividualCustomer.class);

        this.individualCustomerDao.deleteById(individualCustomer.getCustomerId());

        return new SuccessResult(deleteIndividualCustomerRequest.getIndividualCustomerId() + BusinessMessages.INDIVIDUAL_CUSTOMER_DELETE);
    }
}

