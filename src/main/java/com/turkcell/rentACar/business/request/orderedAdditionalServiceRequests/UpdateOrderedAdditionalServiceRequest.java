package com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderedAdditionalServiceRequest {

    @Positive
    private int additionalServiceId;
}
