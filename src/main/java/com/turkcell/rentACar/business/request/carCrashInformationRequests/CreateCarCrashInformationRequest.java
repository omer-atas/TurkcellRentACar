package com.turkcell.rentACar.business.request.carCrashInformationRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateCarCrashInformationRequest {

    private int carCrashInformationId;

    private String crashDetail;

    private int carId;
}
