package com.pichincha.account.domain.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    private String personId;
    private String name;
    private String gender;
    private int age;
    private String identification;
    private String address;
    private String phone;
}
