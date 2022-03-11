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
@Table(name = "rental_cars")
public class RentalCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_car_id")
    private int rentalId;

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

    @OneToMany(mappedBy = "rentalCar")
    private List<OrderedAdditionalService> orderedAdditionalService;

}
