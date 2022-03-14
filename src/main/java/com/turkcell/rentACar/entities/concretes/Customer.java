package com.turkcell.rentACar.entities.concretes;

import com.turkcell.rentACar.core.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "customer_id",referencedColumnName = "user_id")
public class Customer extends User {

    @Column(name = "customer_id",insertable = false,updatable = false)
    private int customerId;

    @OneToMany(mappedBy = "customer")
    List<Rent> rents;

}
