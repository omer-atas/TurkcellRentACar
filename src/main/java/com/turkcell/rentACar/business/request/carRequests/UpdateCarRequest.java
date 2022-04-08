package com.turkcell.rentACar.business.request.carRequests;

import javax.validation.constraints.*;

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

	@NotBlank
	@NotNull
	@NotEmpty
	@Size(min = 1)
	private String description;

	@Positive
	private double kilometerInformation;

	@Positive
	private int colorId;

}
