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
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

    @Id
    @UuidGenerator
    @Column(name = "person_id")
    private String personId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    private short age;

    @Column(name = "identification", unique = true)
    private String identification;

    private String address;

    private String phone;
}