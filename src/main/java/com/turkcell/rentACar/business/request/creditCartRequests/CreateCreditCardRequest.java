package com.turkcell.rentACar.business.request.creditCartRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateCreditCardRequest {

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String cardNumber;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String cardOwnerName;

    @Min(1)
    @Max(12)
    private int cardEndMonth;

    @Positive
    private int cardEndYear;

    @Positive
    private int cardCVC;

    @Positive
    private double totalPrice;
}
