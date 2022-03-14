package com.turkcell.rentACar.business.dtos.invoiceDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class InvoiceGetDto {

    private int invoiceId;

    private String invoiceNo;

    private LocalDate creationDate;

    private double totalPayment;

    private LocalDate startingDate;

    private LocalDate endDate;

    private int totalRentalDays;

    private int customerId;
}