package com.turkcell.rentACar.business.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBrandRequest {

	@NotBlank
	@NotNull
	@NotEmpty
	@Positive
	private int brandId;

	@NotBlank
	@NotEmpty
	@Size(min = 1)
	private String brandName;
}
