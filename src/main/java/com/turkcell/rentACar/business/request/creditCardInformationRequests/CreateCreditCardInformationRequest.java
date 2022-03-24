package com.turkcell.rentACar.business.request.creditCardInformationRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateCreditCardInformationRequest {

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 1)
    private String cardNumber;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 1)
    private String cardOwnerName;

    @Positive
    @Min(1)
    @Max(12)
    private int cardEndMonth;

    @Positive
    private int cardEndYear;

    @Min(100)
    @Max(999)
    private int cardCVC;

    @Positive
    private int customerId;
}
