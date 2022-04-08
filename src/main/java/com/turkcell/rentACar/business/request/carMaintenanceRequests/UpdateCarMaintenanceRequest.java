package com.turkcell.rentACar.business.request.carMaintenanceRequests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarMaintenanceRequest {

	@NotBlank
	@NotNull
	@NotEmpty
	@Size(min = 1)
	private String description;

	private LocalDate returnDate;

}
