package com.turkcell.rentACar.business.request.invoiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateInvoiceRequest {

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String invoiceNo;

    private LocalDate startingDate;

    private LocalDate endDate;

    private double rentDay;

    @Positive
    private double totalRentCarPrice;

    @Positive
    private int rentId;
}
