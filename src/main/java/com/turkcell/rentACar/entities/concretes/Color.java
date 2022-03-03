package com.turkcell.rentACar.entities.concretes;

import lombok.*;

import java.util.List;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "colors")
public class Color {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "color_id")
	private int colorId;

	@Column(name = "color_name", unique = true)
	private String colorName;
	
	@OneToMany(mappedBy = "color",cascade = CascadeType.ALL)
	private List<Car> cars;

}
