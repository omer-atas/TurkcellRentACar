package com.turkcell.rentACar.business.request.corporateCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UpdateCorporateCustomerRequest {

    private String companyName;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 10,max = 10)
    private String taxNumber;

    @Email
    private String email;

    private String password;
}
