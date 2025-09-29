package com.pichincha.account.domain;

import com.pichincha.account.domain.external.Customer;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report {
    Customer customer;
    Page<Transaction> transactions;
}
