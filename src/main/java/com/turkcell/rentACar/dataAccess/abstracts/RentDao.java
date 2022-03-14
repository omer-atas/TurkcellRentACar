package com.turkcell.rentACar.dataAccess.abstracts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACar.entities.concretes.Rent;

@Repository
public interface RentDao extends JpaRepository<Rent, Integer> {

	List<Rent> getByCar_CarId(int carId);

	Rent getByRentId(int rentId);


}
