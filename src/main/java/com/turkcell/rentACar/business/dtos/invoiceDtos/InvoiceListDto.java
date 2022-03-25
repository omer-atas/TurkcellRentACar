package com.turkcell.rentACar.business.dtos.invoiceDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class InvoiceListDto {

    private int invoiceId;

    private String invoiceNo;

    private LocalDate creationDate;

    private double totalPayment;

    private LocalDate startingDate;

    private LocalDate endDate;

    private double rentDay;

    private int totalRentCarPrice;

    private int customerId;
}
