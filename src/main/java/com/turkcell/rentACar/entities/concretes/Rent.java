package com.turkcell.rentACar.entities.concretes;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rents")
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rent_id")
    private int rentId;

    @Column(name = "starting_date")
    private LocalDate startingDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_payment")
    private double totalPayment;

    @ManyToOne
    @JoinColumn(name = "city_plate")
    private City returnCity;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "rent")
    private List<OrderedAdditionalService> orderedAdditionalService;

}
