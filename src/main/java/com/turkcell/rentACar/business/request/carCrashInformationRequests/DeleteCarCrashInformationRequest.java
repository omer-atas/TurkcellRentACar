package com.turkcell.rentACar.business.request.carCrashInformationRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class DeleteCarCrashInformationRequest {

    @Positive
    private int carCrashInformationId;
}
