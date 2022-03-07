package com.turkcell.rentACar.business.dtos.brandDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandGetDto {

	private int brandId;

	private String brandName;
}
