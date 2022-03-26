package com.turkcell.rentACar.dataAccess.abstracts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACar.entities.concretes.Car;

@Repository
public interface CarDao extends JpaRepository<Car, Integer>{
	
	Car getByCarId(int carId);
	
	List<Car> getByBrand_BrandId(int brandId);
	
	List<Car> getByColor_ColorId(int colorId);

	
	List<Car> findByDailyPriceLessThanEqual(double dailyPrice);
}
