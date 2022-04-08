package com.turkcell.rentACar.business.request.rentRequests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateRentRequest {

    @Positive
    private double returnKilometer;

}
