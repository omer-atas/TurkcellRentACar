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

    // Denormalize edebileceğin kadar et !

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

    @OneToOne()
    @JoinColumn(name = "rent_id")
    private Rent rent;
}
