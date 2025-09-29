package com.pichincha.customer.infrastructure.output.repository.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends Person{

    @UuidGenerator
    @Column(name = "customer_id")
    private String customerId;

    private String password;

    private boolean status;
}