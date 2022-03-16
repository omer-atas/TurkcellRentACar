package com.turkcell.rentACar.business.request.corporateCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class CreateCorporateCustomerRequest {

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String companyName;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 10,max = 10)
    private String taxNumber;

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
