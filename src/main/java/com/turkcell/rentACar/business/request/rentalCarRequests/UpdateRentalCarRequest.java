package com.turkcell.rentACar.business.request.rentalCarRequests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateRentalCarRequest {

	private LocalDate startingDate;

	private LocalDate endDate;

	private double totalPayment;

	private int cityPlate;

}
