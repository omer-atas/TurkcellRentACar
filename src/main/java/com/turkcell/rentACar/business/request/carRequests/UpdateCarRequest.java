package com.turkcell.rentACar.business.request.carRequests;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarRequest {

	@Positive
	private double dailyPrice;

	@Positive
	private int modelYear;

	private String description;

	@Positive
	private int colorId;

}
