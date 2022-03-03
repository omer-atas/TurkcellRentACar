package com.turkcell.rentACar.business.request;

import java.time.LocalDate;


import javax.persistence.Column;
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

	@NotNull
	@Size(min = 1)
	private String description;

	@Column(name = "returnDate")
	private LocalDate returnDate;

	@NotNull
	@Positive
	private int carId;
}
