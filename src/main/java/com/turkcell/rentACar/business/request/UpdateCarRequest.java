package com.turkcell.rentACar.business.request;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarRequest {

	@Positive
	private int carId;

	@Positive
	private double dailyPrice;

	@Positive
	private int modelYear;

	@Size(min = 1)
	private String description;

	@Positive
	private int brandId;

	@Positive
	private int colorId;

}