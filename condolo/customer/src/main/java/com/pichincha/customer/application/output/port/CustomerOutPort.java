package com.pichincha.customer.application.output.port;

import com.pichincha.customer.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerOutPort {

    Customer save(Customer customer);

    Page<Customer> findAll(Pageable pageable);

    Customer findById(String id);

    Customer findByCustomerId(String customerId);

    void delete(String id);
}
