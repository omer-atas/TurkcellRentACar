package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice,Integer> {

    Invoice getByInvoiceId(int invoiceId);

    boolean existsByInvoiceNo(String invoiceNo);

    List<Invoice> getByRent_RentId(int rentId);

    List<Invoice> getByRent_Customer_CustomerId(int customerId);

    @Query(value = "SELECT i FROM Invoice i WHERE i.creationDate>=:fromDate AND i.creationDate<=:toDate")
    List<Invoice> getAllInvoicesInSpecificDateRange(LocalDate fromDate, LocalDate toDate);
}
