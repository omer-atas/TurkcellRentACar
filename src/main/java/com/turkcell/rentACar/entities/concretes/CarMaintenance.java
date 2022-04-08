package com.turkcell.rentACar.entities.concretes;

import java.time.LocalDate;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car_maintenances")
public class CarMaintenance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "car_maintanence_id")
	private int maintanenceId;

	@Column(name = "description")
	private String description;

	@Column(name = "returnDate")
	private LocalDate returnDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "car_id")
	private Car car;

}
