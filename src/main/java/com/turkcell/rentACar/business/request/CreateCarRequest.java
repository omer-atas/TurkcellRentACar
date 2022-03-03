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

	@NotBlank
	@NotNull
	@NotEmpty
	@Positive
	private double dailyPrice;

	@NotBlank
	@NotNull
	@NotEmpty
	@Positive
	private int modelYear;

	@NotBlank
	@NotNull
	@NotEmpty
	@Size(min = 0)
	private String description;

	@NotBlank
	@NotNull
	@NotEmpty
	@Positive
	private int brandId;

	@NotBlank
	@NotNull
	@NotEmpty
	@Positive
	private int colorId;
}
