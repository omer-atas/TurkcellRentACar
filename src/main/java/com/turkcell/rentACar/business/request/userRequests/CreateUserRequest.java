package com.turkcell.rentACar.business.request.userRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @Email
    private String email;

    private String password;
}
