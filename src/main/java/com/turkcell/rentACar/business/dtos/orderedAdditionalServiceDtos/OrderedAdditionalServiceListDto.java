package com.turkcell.rentACar.business.dtos.orderedAdditionalServiceDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class OrderedAdditionalServiceListDto {

    private int orderedAdditionalServiceId;

    private int additionalServiceId;

    private int rentalId;
}
