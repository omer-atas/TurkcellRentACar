package com.turkcell.rentACar.business.request.rentRequests;

import java.time.LocalDate;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateRentRequest {

	private LocalDate startingDate;

	private LocalDate endDate;

	private int fromCityId;

	private int toCityId;

	@Positive
	private int carId;

	@Positive
	private int customerId;

}
