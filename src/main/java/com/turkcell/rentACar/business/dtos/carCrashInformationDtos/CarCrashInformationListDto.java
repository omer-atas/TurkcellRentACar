package com.turkcell.rentACar.business.dtos.carCrashInformationDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CarCrashInformationListDto {

    private int carCrashInformationId;

    private String crashDetail;

    private int carId;
}
