package com.turkcell.rentACar.business.request.rentalCarRequests;

import java.time.LocalDate;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateRentalCarRequest {

	private LocalDate startingDate;

	private LocalDate endDate;

	private int cityPlate;

	@Positive
	private int carId;

}
