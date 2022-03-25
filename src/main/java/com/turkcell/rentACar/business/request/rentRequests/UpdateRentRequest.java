package com.turkcell.rentACar.business.request.rentRequests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateRentRequest {

    private double returnKilometer;

}
