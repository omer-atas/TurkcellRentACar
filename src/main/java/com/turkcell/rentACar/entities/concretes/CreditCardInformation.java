package com.turkcell.rentACar.entities.concretes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "credit_card_information")
public class CreditCardInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_card_information_id")
    private int creditCardInformationId;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_owner_name")
    private String cardOwnerName;

    @Column(name = "card_end_month")
    private int cardEndMonth;

    @Column(name = "card_end_year")
    private int cardEndYear;

    @Column(name = "card_cvc")
    private int cardCVC;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
