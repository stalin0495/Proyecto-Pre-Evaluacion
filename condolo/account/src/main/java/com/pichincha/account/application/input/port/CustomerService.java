package com.pichincha.account.application.input.port;

import org.springframework.cloud.openfeign.FeignClient;
import com.pichincha.account.domain.external.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-services",
        url = "${customer.services.url}" )
public interface CustomerService {
    @GetMapping("customer-services/api/v1/customers/{customerId}")
    Customer findById(@PathVariable("customerId") String id);
}
