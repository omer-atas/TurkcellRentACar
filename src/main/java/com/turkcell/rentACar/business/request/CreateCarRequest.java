package com.turkcell.rentACar.business.request;

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

	@NotEmpty
	@NotNull
	@Positive
	private double dailyPrice;

	@NotEmpty
	@NotNull
	@Positive
	private int modelYear;

	@NotBlank
	@NotNull
	@NotEmpty
	@Size(min = 1)
	private String description;

	@NotNull
	@NotEmpty
	@Positive
	private int brandId;

	@NotNull
	@NotEmpty
	@Positive
	private int colorId;
}
