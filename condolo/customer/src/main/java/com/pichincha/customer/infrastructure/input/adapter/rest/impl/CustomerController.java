package com.pichincha.customer.infrastructure.input.adapter.rest.impl;

import com.pichincha.customer.application.input.port.CustomerService;
import com.pichincha.customer.domain.Customer;
import com.pichincha.customer.domain.common.ValidationGroups;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public Customer create(@Validated(ValidationGroups.Create.class) @RequestBody Customer customerDto){
        return this.customerService.create(customerDto);
    }

    @GetMapping
    public Page<Customer> findAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                  @RequestParam(defaultValue = "10") @Min(0) @Max(100) int size){
        return this.customerService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{customerId}")
    public Customer findById(@PathVariable("customerId") String id){
        Customer customer = this.customerService.findById(id);
        customer.setPassword(null);
        return customer;
    }

    @PatchMapping("/{customerId}")
    public Customer update(@PathVariable("customerId") String id,
                              @Validated(ValidationGroups.Update.class) @RequestBody Customer customerDto) {
        customerDto.setPersonId(id);
        customerDto.setStatus(Boolean.TRUE);
        return this.customerService.update(customerDto);
    }

    @DeleteMapping("/{customerId}")
    public void delete(@PathVariable("customerId") String id) {
        this.customerService.delete(id);
    }
}
