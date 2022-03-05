package com.turkcell.rentACar.business.request;

import java.time.LocalDate;

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

	@NotNull
	@Size(min = 1)
	private String description;

	@NotNull
	@Positive
	private int carId;
}
