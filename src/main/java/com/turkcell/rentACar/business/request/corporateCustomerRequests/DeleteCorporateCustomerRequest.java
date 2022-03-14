package com.turkcell.rentACar.business.request.corporateCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class DeleteCorporateCustomerRequest {

    @Positive
    private int corporateCustomerId;
}
