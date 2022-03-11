package com.turkcell.rentACar.business.dtos.rentalCarDtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class RentalCarGetDto {
	
	private int rentalId;

	private LocalDate startingDate;

	private LocalDate endDate;

	private double totalPayment;

	private int cityPlate;

	private int carId;

}
