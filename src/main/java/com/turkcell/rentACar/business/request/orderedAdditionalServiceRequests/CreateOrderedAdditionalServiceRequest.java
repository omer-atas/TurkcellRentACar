package com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderedAdditionalServiceRequest {

    @Positive
    private int additionalServiceId;

    @Positive
    private int rentId;
}
