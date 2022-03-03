package com.turkcell.rentACar.business.request;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateColorRequest {

	@Positive
	private int colorId;

	@Positive
	private String colorName;
}
