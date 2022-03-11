package com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderedAdditionalServiceRequest {

    private int additionalServiceId;

    private int rentalId;
}
