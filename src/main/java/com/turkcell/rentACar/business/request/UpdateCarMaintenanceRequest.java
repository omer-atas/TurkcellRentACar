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
public class UpdateCarMaintenanceRequest {

	@NotBlank
	@NotNull
	@NotEmpty
	@Positive
	private int carMaintanenceId;

	@Size(min = 1)
	private String description;

	private LocalDate returnDate;

	@Positive
	private int carId;
}
