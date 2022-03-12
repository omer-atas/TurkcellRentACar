package com.turkcell.rentACar.business.dtos.rentDtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class RentGetDto {
	
	private int rentId;

	private LocalDate startingDate;

	private LocalDate endDate;

	private double totalPayment;

	private int fromCityId;

	private int toCityId;

	private int carId;

}
