package com.turkcell.rentACar.business.request.rentRequests;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class DeleteRentRequest {

	@Positive
	private int rentId;

}
