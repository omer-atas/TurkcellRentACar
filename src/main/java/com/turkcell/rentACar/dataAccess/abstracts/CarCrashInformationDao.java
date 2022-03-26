package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.CarCrashInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarCrashInformationDao extends JpaRepository<CarCrashInformation,Integer> {

    CarCrashInformation getByCarCrashInformationId(int carCrashInformationId);

    List<CarCrashInformation> getByCar_CarId(int carId);
}
