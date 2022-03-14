package com.turkcell.rentACar.business.request.invoiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateInvoiceRequest {

    private String invoiceNo;

    private LocalDate creationDate;

    private double totalPayment;
}
