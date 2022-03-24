package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.customerDtos.CustomerGetDto;

public interface CustomerService {

    CustomerGetDto  getByCustomerId(int customerId);

    boolean existsByEmail(String email);
}
