package com.turkcell.rentACar.business.request.paymentRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentRequest {

    private int amount;
}
