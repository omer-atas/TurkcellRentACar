package com.turkcell.rentACar.business.request;

import java.time.LocalDate;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarMaintenanceRequest {

	@NotEmpty
	@NotNull
	@Positive
	private int carMaintanenceId;

	private String description;

	private LocalDate returnDate;

	@Positive
	private int carId;
}
