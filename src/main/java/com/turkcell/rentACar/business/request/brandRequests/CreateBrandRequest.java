package com.turkcell.rentACar.business.request.brandRequests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateBrandRequest {

	@NotBlank
	@NotNull
	@NotEmpty
	@Size(min = 1)
	private String brandName;

}
