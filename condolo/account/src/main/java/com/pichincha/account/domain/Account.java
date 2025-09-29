package com.pichincha.account.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pichincha.account.domain.common.Constants;
import com.pichincha.account.domain.common.ValidationGroups;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {

    @JsonProperty(Constants.ID_LABEL)
    private String accountId;

    @NotBlank(message = Constants.NOT_BLANK, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(max = 36, message = Constants.UUID_LENGTH,
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String customerId;

    @Pattern(regexp = "^\\d{10}$", message = "Should be a 10-digit number",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @NotBlank(message = Constants.NOT_BLANK, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String accountNumber;

    @NotBlank(message = "Cannot be blank", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String accountType;

    @Digits(integer = 18, fraction = 2, message = Constants.BIG_DECIMAL_VALIDATION,
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private BigDecimal initialBalance;

    private boolean status;
}