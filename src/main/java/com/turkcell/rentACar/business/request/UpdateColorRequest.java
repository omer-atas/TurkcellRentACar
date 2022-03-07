package com.turkcell.rentACar.business.request;

import javax.validation.constraints.NotNull;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateColorRequest {

	@NotNull
	@Size(min = 1)
	private String colorName;
}
