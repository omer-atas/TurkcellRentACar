package com.turkcell.rentACar.business.request.individualCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateIndividualCustomerRequest {

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String firstName;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String lastName;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 11,max = 11)
    private String nationalIdentity;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    @Email
    private String email;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String password;
}
