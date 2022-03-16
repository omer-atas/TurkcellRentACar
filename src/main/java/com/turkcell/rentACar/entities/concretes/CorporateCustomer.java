package com.turkcell.rentACar.entities.concretes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "corporate_customers")
@PrimaryKeyJoinColumn(name = "corporate_customer_id",referencedColumnName = "customer_id")
public class CorporateCustomer extends Customer{

    @Column(name = "corporate_customer_id",insertable = false,updatable = false)
    private int corporateCustomerId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "tax_number",unique = true)
    private String taxNumber;
}
