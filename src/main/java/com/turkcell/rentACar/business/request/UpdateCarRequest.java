package com.turkcell.rentACar.business.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarRequest {

	@NotBlank
	@NotNull
	@NotEmpty
	@Positive
	private int carId;

	@Positive
	private double dailyPrice;

	@Positive
	private int modelYear;

	private String description;

	@Positive
	private int brandId;

	@Positive
	private int colorId;

}
