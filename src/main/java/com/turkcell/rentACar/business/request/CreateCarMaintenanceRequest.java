package com.turkcell.rentACar.business.request;

import java.time.LocalDate;

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
public class CreateCarMaintenanceRequest {
	
	private LocalDate returnDate;

	@NotBlank
	@NotNull
	@NotEmpty
	@Size(min = 1)
	private String description;

	
	@Positive
	private int carId;
}
