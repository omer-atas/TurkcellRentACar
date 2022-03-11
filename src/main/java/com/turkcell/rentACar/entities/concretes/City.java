package com.turkcell.rentACar.entities.concretes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_plate")
    private int cityPlate;

    @Column(name = "city_name")
    private String cityName;

    @OneToMany(mappedBy = "currentCity")
    private List<Car> cars;

    @OneToMany(mappedBy = "returnCity")
    private List<Rent> rentalCarsReturnCity;
}
