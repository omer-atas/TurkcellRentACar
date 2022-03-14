package com.turkcell.rentACar.business.request.corporateCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateCorporateCustomerRequest {

    private String companyName;

    private String taxNumber;

    @Email
    private String email;

    private String password;
}
