package com.turkcell.rentACar.business.dtos.carMaintenanceDtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CarMaintenanceGetDto {

	private int maintanenceId;

	private String description;

	private LocalDate returnDate;

	private int carId;
}
