package com.turkcell.rentACar.business.request.userRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequest {

    @Positive
    private int userId;
}
