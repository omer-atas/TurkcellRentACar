package com.turkcell.rentACar.business.request;

import java.time.LocalDate;

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
public class UpdateRentalCarRequest {

	@NotBlank
	@NotNull
	@NotEmpty
	@Positive
	private int rentalId;
	
	@NotBlank
	@NotNull
	@NotEmpty
	private LocalDate startingDate;

	private LocalDate endDate;

	@Positive
	private int carId;
}
