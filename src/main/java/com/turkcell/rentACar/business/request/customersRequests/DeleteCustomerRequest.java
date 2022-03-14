package com.turkcell.rentACar.business.request.customersRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class DeleteCustomerRequest {

    @Positive
    private int customerId;
}
