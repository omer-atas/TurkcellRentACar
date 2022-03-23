package com.turkcell.rentACar.api.modals;

import com.turkcell.rentACar.business.request.creditCartRequests.CreateCreditCardRequest;
import com.turkcell.rentACar.business.request.rentRequests.CreateRentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class RentPostServiceModal {

    private CreateRentRequest createRentRequest;
    private CreateCreditCardRequest createCreditCardRequest;
}
