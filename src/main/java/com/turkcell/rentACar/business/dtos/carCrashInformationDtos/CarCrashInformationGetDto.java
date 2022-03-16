package com.turkcell.rentACar.business.dtos.carCrashInformationDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CarCrashInformationGetDto {

    private int carCrashInformationId;

    private String crashDetail;

    private int carId;
}
