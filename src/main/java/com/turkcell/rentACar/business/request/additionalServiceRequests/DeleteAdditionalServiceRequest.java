package com.turkcell.rentACar.business.request.additionalServiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class DeleteAdditionalServiceRequest {

    private int additionalServiceId;
}
