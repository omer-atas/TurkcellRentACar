package com.turkcell.rentACar.entities.concretes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car_crash_informations")
public class CarCrashInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_crash_information_id")
    private int carCrashInformationId;

    @Column(name = "crash_detail")
    private String crashDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="car_id")
    private Car car;
}
