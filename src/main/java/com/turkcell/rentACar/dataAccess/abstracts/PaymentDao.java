package com.turkcell.rentACar.dataAccess.abstracts;

import com.turkcell.rentACar.entities.concretes.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDao extends JpaRepository<Payment,Integer> {

    Payment getByPaymentId(int paymentId);

    Payment getByInvoice_InvoiceId(int invoiceId);
}
