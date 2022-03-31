package com.turkcell.rentACar.entities.concretes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private int invoiceId;

    @Column(name = "invoice_no", unique = true)
    private String invoiceNo;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "total_payment")
    private double totalPayment;

    @Column(name = "starting_date")
    private LocalDate startingDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "rent_day")
    private double rentDay;

    @Column(name = "rental_price_of_the_car")
    private double totalRentCarPrice;

    @ManyToOne
    @JoinColumn(name = "rent_id")
    private Rent rent;

    @OneToOne(mappedBy = "invoice")
    private Payment payment;

}
