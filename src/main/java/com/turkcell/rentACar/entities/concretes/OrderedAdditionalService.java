package com.turkcell.rentACar.entities.concretes;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ordered_additional_services")
public class OrderedAdditionalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordered_additional_service_id")
    private int orderedAdditionalServiceId;

    @ManyToOne()
    @JoinColumn(name = "additional_service_id")
    private AdditionalService additionalService;

    @OneToMany(mappedBy = "orderedAdditionalService")
    private List<RentalCar> rentalCars;


}
