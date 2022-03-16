package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.CarCrashInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarCrashInformationDao extends JpaRepository<CarCrashInformation,Integer> {

    CarCrashInformation getByCarCrashInformationId(int carCrashInformationId);
}
