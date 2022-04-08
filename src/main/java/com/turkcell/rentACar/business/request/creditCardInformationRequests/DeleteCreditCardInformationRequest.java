package com.turkcell.rentACar.business.request.creditCardInformationRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class DeleteCreditCardInformationRequest {

    @Positive
    private int creditCardInformationId;
}
