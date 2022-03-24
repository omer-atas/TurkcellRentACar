package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.CreditCardInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardInformationDao extends JpaRepository<CreditCardInformation,Integer> {

    CreditCardInformation getByCreditCardInformationId(int creditCardInformationId);

    List<CreditCardInformation> getByCustomer_CustomerId(int customerId);
}
