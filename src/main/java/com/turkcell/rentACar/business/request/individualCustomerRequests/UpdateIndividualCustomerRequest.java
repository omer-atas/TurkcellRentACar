package com.turkcell.rentACar.business.request.individualCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateIndividualCustomerRequest {

    private String firstName;

    private String lastName;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 11,max = 11)
    private String nationalIdentity;

    @Email
    private String email;

    private String password;
}
