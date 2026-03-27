package com.pichincha.customer.infrastructure.input.adapter.rest.impl;
import com.pichincha.customer.application.exception.ValidationException;
import com.pichincha.customer.application.input.port.CustomerService;
import com.pichincha.customer.domain.Customer;
import com.pichincha.customer.domain.common.ValidationGroups;
import com.pichincha.customer.domain.util.Constants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;
import java.util.Optional;

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
        validateCustomerId(id);
        
        return Optional.ofNullable(this.customerService.findById(id))
                .map(customer -> {
                    customer.setPassword(null);
                    return customer;
                })
                .orElse(null);
    }

    @PatchMapping("/{customerId}")
    public Customer update(@PathVariable("customerId") String id,
                              @Validated(ValidationGroups.Update.class) @RequestBody Customer customerDto) {
        validateCustomerId(id);
        validateCustomerDto(customerDto);
        customerDto.setPersonId(id);
        customerDto.setStatus(Boolean.TRUE);
        return this.customerService.update(customerDto);
    }

    @DeleteMapping("/{customerId}")
    public void delete(@PathVariable("customerId") String id) {
        validateCustomerId(id);
        this.customerService.delete(id);
    }

    private void validateCustomerId(String customerId) {
        if (Objects.isNull(customerId) || customerId.isBlank()) {
            throw new ValidationException(Constants.CUSTOMER_ID_NULL_OR_EMPTY);
        }
    }

    private void validateCustomerDto(Customer customerDto) {
        Objects.requireNonNull(customerDto, Constants.GENERIC_ERROR);
    }
}

