package com.pichincha.customer.infrastructure.output.repository.entity;

import com.pichincha.customer.domain.common.Constants;
import com.pichincha.customer.domain.common.ValidationGroups;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    private String name;

    @NotBlank(message = Constants.NOT_BLANK, groups = ValidationGroups.Create.class)
    @Size(min= 1, max = 255, message = "Should not be less than 1 and should not be greater than 255",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Pattern(regexp = "^[A-Za-zÑn]+( [A-Za-zÑñ]+)*$",
            message = "Gender must contain only letters and single spaces between words",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Column(nullable = false)
    private String gender;

    private short age;

    @Column(name = "identification", unique = true)
    private String identification;

    private String address;

    private String phone;
}