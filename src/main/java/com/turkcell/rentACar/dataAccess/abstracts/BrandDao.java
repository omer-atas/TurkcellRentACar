package com.turkcell.rentACar.dataAccess.abstracts;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACar.entities.concretes.Brand;

@Repository
public interface BrandDao extends JpaRepository<Brand, Integer> {
	
	Brand getByBrandId(int brandId);
	
	boolean existsByBrandName(String brandName);
	
	Brand getByBrandName(String brandName);
	
}
