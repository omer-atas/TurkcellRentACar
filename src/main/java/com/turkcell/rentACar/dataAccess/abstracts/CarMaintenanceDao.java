package com.turkcell.rentACar.dataAccess.abstracts;


import java.time.LocalDate;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACar.entities.concretes.CarMaintenance;

@Repository
public interface CarMaintenanceDao extends JpaRepository<CarMaintenance, Integer> {

	List<CarMaintenance> getByCar_CarId(int carId);
	
	List<CarMaintenance> deleteByCar_CarId(int carId);

	CarMaintenance getByMaintanenceId(int maintanenceId);
	
	List<CarMaintenance> getByReturnDateLessThanEqual(LocalDate returnDate);
	
	List<CarMaintenance> getByReturnDateGreaterThanEqual(LocalDate returnDate);
}
