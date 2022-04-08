package com.turkcell.rentACar.business.request.carCrashInformationRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarCrashInformationRequest {

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String crashDetail;
}
