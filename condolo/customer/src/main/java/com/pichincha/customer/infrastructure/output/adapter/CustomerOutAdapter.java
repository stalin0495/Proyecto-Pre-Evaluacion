package com.pichincha.customer.infrastructure.output.adapter;

import com.pichincha.customer.application.exception.DatabaseException;
import com.pichincha.customer.application.output.port.CustomerOutPort;
import com.pichincha.customer.domain.Customer;
import com.pichincha.customer.application.exception.ResourceNotFoundException;
import com.pichincha.customer.infrastructure.output.repository.CustomerRepository;
import com.pichincha.customer.infrastructure.output.repository.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerOutAdapter implements CustomerOutPort {

    private final CustomerMapper customerMapper;

    private final CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        try {
            com.pichincha.customer.infrastructure.output.repository.entity.Customer c = customerMapper.convertToEntity(customer);
            return customerMapper.convertToDomain(
                    customerRepository.save(c)
            );
        } catch (DataAccessException e) {
            log.error("Database error while saving customer", e);
            throw new DatabaseException("Error saving customer: " + e.getMessage(), e);
        }
    }
    @Override
    public Page<Customer> findAll(Pageable pageable) {
        try {
            return customerRepository.findAll(pageable)
                    .map(customerMapper::convertToDomain);
        } catch (DataAccessException e) {
            log.error("Database error while finding all customers", e);
            throw new DatabaseException("Error retrieving customers: " + e.getMessage(), e);
        }
    }

    @Override
    public Customer findById(String id) {
        try {
            return customerMapper.convertToDomain(
                    customerRepository.findByCustomerId(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id)
            ));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while finding customer by id: {}", id, e);
            throw new DatabaseException("Error retrieving customer: " + e.getMessage(), e);
        }
    }

    @Override
    public Customer findByCustomerId(String customerId) {
        try {
            return customerMapper.convertToDomain(
                    customerRepository.findByCustomerId(customerId)
                            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with customerId: " + customerId)
            ));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while finding customer by customerId: {}", customerId, e);
            throw new DatabaseException("Error retrieving customer: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
            customerRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while deleting customer: {}", id, e);
            throw new DatabaseException("Error deleting customer: " + e.getMessage(), e);
        }
    }
}
