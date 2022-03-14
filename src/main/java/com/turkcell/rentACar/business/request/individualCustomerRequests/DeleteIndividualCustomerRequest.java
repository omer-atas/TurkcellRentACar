package com.turkcell.rentACar.business.request.individualCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class DeleteIndividualCustomerRequest {

    @Positive
    private int individualCustomerId;
}
