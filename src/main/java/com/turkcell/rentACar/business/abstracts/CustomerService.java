package com.turkcell.rentACar.business.abstracts;

import com.turkcell.rentACar.business.dtos.customerDtos.CustomerGetDto;

//end point olmayan durumlarda dto kullanmaya gerek yoktur!!!!
public interface CustomerService {

    CustomerGetDto  getByCustomerId(int customerId);; //  ilerleyen zamnalarda end point istenirse böyle yapılır yani customerGetDto
}
