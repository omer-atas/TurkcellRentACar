package com.turkcell.rentACar.business.request.paymentRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {

    @Positive
    private int invoiceId;
}
