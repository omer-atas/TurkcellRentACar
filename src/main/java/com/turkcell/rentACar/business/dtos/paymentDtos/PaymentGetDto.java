package com.turkcell.rentACar.business.dtos.paymentDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class PaymentGetDto {

    private int paymentId;

    private int amount;

    private int rentId;
}
