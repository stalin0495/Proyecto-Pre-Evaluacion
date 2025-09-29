package com.pichincha.account.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.pichincha.account.domain.common.Constants;
import com.pichincha.account.domain.common.ValidationGroups;
import com.pichincha.account.domain.external.Customer;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {

    @JsonProperty(Constants.ID_LABEL)
    private String transactionId;

    @Size(max = 36, message = Constants.UUID_LENGTH,
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String accountId;

    private LocalDateTime date;

    @NotBlank(message = Constants.NOT_BLANK, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(min= 3, max = 32, message = Constants.CATALOG_LENGTH,
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String transactionType;

    @Digits(integer = 18, fraction = 2, message = Constants.BIG_DECIMAL_VALIDATION,
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private BigDecimal amount;

    private BigDecimal balance;

    private Customer customer;

    private Account account;
}