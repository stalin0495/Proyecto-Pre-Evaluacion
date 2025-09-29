package com.pichincha.customer.infrastructure.output.repository;

import com.pichincha.customer.infrastructure.output.repository.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}