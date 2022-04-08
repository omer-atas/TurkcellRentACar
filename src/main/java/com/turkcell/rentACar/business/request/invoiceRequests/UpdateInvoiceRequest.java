package com.turkcell.rentACar.business.request.invoiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateInvoiceRequest {

    @Positive
    private double totalPayment;

    private LocalDate startingDate;

    private LocalDate endDate;

    @Positive
    private double rentDay;

    @Positive
    private double totalRentCarPrice;
}
