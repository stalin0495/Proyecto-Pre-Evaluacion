package com.pichincha.customer.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pichincha.customer.domain.common.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer extends Person {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String customerId;

    @NotBlank(message = "Password cannot be blank", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^(?=.*[A-ZÑ])(?=.*[a-zñ])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Password must be at least 8 characters and maximum 20 characters and include an" +
                    " uppercase letter, a lowercase letter, a number, and a special character",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String password;

    private boolean status;
}