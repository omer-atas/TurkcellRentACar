package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.OrderedAdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedAdditionalServiceDao extends JpaRepository<OrderedAdditionalService,Integer> {

    OrderedAdditionalService getByOrderedAdditionalServiceId( int orderedAdditionalServiceId);

    List<OrderedAdditionalService> getByRent_RentId(int rentId);

    List<OrderedAdditionalService> getByAdditionalService_AdditionalServiceId(int additionalServiceId);
}
