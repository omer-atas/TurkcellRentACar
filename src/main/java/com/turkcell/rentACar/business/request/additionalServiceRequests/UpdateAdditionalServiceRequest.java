package com.turkcell.rentACar.business.request.additionalServiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateAdditionalServiceRequest {

    private String additionalServiceName;

    private double dailyPrice;
}
