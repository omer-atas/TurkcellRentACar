package com.turkcell.rentACar.business.request.additionalServiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class DeleteAdditionalServiceRequest {

    @Positive
    private int additionalServiceId;
}
