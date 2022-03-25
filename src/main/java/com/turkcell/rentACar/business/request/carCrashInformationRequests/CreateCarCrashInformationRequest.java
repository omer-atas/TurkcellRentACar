package com.turkcell.rentACar.business.request.carCrashInformationRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateCarCrashInformationRequest {

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String crashDetail;

    @Positive
    private int carId;
}
