package com.turkcell.rentACar.dataAccess.abstracts;

import java.time.LocalDate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACar.entities.concretes.RentalCar;

@Repository
public interface RentalCarDao extends JpaRepository<RentalCar, Integer> {

	List<RentalCar> getByCar_CarId(int carId);

	RentalCar getByRentalId(int rentalId);

	List<RentalCar> getByStartingDateGreaterThanEqualOrEndDate(LocalDate startingDate,LocalDate endDate);

}
