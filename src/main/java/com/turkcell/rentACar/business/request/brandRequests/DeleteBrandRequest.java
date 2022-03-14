package com.turkcell.rentACar.business.request.brandRequests;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteBrandRequest {

	@Positive
	private int brandId;
}
