package com.turkcell.rentACar.business.request.additionalServiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateAdditionalServiceRequest {

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String additionalServiceName;

    @Positive
    private double dailyPrice;
}
