package com.turkcell.rentACar.business.concretes;

import com.turkcell.rentACar.business.abstracts.CustomerService;
import com.turkcell.rentACar.business.dtos.customerDtos.CustomerGetDto;
import com.turkcell.rentACar.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACar.dataAccess.abstracts.CustomerDao;
import com.turkcell.rentACar.entities.concretes.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerManager implements CustomerService {

    private CustomerDao customerDao;
    private ModelMapperService modelMapperService;

    @Autowired
    public CustomerManager(CustomerDao customerDao, ModelMapperService modelMapperService) {
        this.customerDao = customerDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public CustomerGetDto getByCustomerId(int customerId) {

        Customer result = this.customerDao.getByCustomerId(customerId);

        if (result == null) {
            return null;
        }

        CustomerGetDto response = this.modelMapperService.forDto().map(result, CustomerGetDto.class);

        return response;
    }

    @Override
    public boolean existsByEmail(String email) {
        if(this.customerDao.existsByEmail(email)){
            return true;
        }
        return false;
    }
}
