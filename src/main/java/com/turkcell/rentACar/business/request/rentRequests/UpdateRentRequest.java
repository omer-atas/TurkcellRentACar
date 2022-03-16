package com.turkcell.rentACar.business.request.rentRequests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateRentRequest {

	private LocalDate startingDate;

	private LocalDate endDate;

	private double returnKilometer;

	private int fromCityId;

	private int toCityId;

}
