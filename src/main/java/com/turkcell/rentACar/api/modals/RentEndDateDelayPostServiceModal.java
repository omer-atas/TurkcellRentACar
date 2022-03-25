package com.turkcell.rentACar.api.modals;

import com.turkcell.rentACar.business.request.creditCartRequests.CreateCreditCardRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data

@AllArgsConstructor
@NoArgsConstructor
public class RentEndDateDelayPostServiceModal {

    private CreateCreditCardRequest createCreditCardRequest;

    private LocalDate updateEndDate;

    private double returnKilometer;
}
