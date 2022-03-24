package com.turkcell.rentACar.api.modals;

import com.turkcell.rentACar.business.request.creditCartRequests.CreateCreditCardRequest;
import com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests.CreateOrderedAdditionalServiceListRequest;
import com.turkcell.rentACar.business.request.paymentRequests.CreatePaymentRequest;
import com.turkcell.rentACar.business.request.rentRequests.CreateRentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class PaymentPostServiceModal {

    private CreateRentRequest createRentRequest;

    private CreateOrderedAdditionalServiceListRequest createOrderedAdditionalServiceListRequests;

    private CreateCreditCardRequest createCreditCardRequest;
}
