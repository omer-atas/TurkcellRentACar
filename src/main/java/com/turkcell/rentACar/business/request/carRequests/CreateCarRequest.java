package com.turkcell.rentACar.business.request.carRequests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateCarRequest {

	@Positive
	private double dailyPrice;

	@Positive
	private int modelYear;

	@NotBlank
	@NotNull
	@NotEmpty
	@Size(min = 1)
	private String description;

	@Positive
	private double kilometerInformation;

	@Positive
	private int brandId;

	@Positive
	private int colorId;
}
