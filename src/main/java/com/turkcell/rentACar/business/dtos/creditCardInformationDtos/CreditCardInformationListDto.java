package com.turkcell.rentACar.business.dtos.creditCardInformationDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreditCardInformationListDto {

    private int creditCardInformationId;

    private String cardNumber;

    private String cardOwnerName;

    private int cardEndMonth;

    private int cardEndYear;

    private int cardCVC;

    private int customerId;
}
