package com.turkcell.rentACar.dataAccess.abstracts;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.rentACar.entities.concretes.CarMaintenance;

public interface CarMaintenanceDao extends JpaRepository<CarMaintenance, Integer> {

	List<CarMaintenance> getByCar_CarId(int carId);
	
	List<CarMaintenance> deleteByCar_CarId(int carId);

	CarMaintenance getByCarMaintanenceId(int carMaintanenceId);
}
