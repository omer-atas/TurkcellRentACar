package com.turkcell.rentACar.business.request.invoiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class DeleteInvoiceRequest {

    private int invoiceId;
}
