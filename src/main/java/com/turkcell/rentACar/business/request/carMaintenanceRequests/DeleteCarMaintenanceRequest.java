package com.turkcell.rentACar.business.request.carMaintenanceRequests;

import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data

@AllArgsConstructor
@NoArgsConstructor
public class DeleteCarMaintenanceRequest {

	@Positive
	private int carMaintanenceId;
}
