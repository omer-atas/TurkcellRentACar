package com.turkcell.rentACar.business.request.invoiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateInvoiceRequest {

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String invoiceNo;

    @Positive
    private int rentId;
}
