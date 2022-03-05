package com.turkcell.rentACar.business.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateRentalCarRequest {

	private LocalDate startingDate;
	
	private LocalDate endDate;

	private int carId;

}
