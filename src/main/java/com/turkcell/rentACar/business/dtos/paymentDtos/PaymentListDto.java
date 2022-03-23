package com.turkcell.rentACar.business.dtos.paymentDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class PaymentListDto {

    private int paymentId;

    private int amount;

    private int rentId;
}
