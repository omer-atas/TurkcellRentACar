package com.turkcell.rentACar.entities.concretes;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.persistence.*;

@Data

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brands")
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "brand_id")
	private int brandId;


	@Column(name = "brand_name", unique = true)
	private String brandName;
	
	@OneToMany(mappedBy = "brand",cascade = CascadeType.ALL)
	private List<Car> cars;

}
