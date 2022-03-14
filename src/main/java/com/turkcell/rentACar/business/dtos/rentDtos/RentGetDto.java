package com.turkcell.rentACar.business.dtos.rentDtos;

import java.time.LocalDate;
import java.util.List;

import com.turkcell.rentACar.entities.concretes.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class RentGetDto {

	private int rentId;

	private LocalDate startingDate;

	private LocalDate endDate;

	private int totalRentalDays;

	private double totalPayment;

	private int fromCityId;

	private int toCityId;

	private int carId;

	private int customerId;

}
