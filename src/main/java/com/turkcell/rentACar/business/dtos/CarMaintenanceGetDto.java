package com.turkcell.rentACar.business.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CarMaintenanceGetDto {

	private int carMaintanenceId;

	private String description;

	private LocalDate returnDate;

	private String brandName;
}
