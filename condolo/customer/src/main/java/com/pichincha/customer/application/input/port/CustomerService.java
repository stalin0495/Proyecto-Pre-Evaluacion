package com.pichincha.customer.application.input.port;

import com.pichincha.customer.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    Customer create(Customer customer);

    Customer update(Customer customer);

    Page<Customer> findAll(Pageable pageable);

    Customer findById(String id);

    void delete(String id);
}
