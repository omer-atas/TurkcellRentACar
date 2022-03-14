package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.CorporateCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorporateCustomerDao extends JpaRepository<CorporateCustomer,Integer> {

    CorporateCustomer getByCustomerId(int corporateCustomerId);

    boolean existsByTaxNumber(String taxNumber);
}
