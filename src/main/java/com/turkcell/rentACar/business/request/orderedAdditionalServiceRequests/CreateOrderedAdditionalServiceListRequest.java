package com.turkcell.rentACar.business.request.orderedAdditionalServiceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderedAdditionalServiceListRequest {

    private List<Integer> additionalServiceIds;

}