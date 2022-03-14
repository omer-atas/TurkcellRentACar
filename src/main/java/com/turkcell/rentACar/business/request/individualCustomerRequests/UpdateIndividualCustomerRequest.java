package com.turkcell.rentACar.business.request.individualCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateIndividualCustomerRequest {

    private String firstName;

    private String lastName;

    private String nationalIdentity;

    private String email;

    private String password;
}
