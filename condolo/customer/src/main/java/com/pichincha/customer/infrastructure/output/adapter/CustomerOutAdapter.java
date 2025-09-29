package com.pichincha.customer.infrastructure.output.adapter;

import com.pichincha.customer.application.output.port.CustomerOutPort;
import com.pichincha.customer.domain.Customer;
import com.pichincha.customer.application.exception.ResourceNotFoundException;
import com.pichincha.customer.infrastructure.output.repository.CustomerRepository;
import com.pichincha.customer.infrastructure.output.repository.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerOutAdapter implements CustomerOutPort {

    private final CustomerMapper customerMapper;

    private final CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        com.pichincha.customer.infrastructure.output.repository.entity.Customer c = customerMapper.convertToEntity(customer);
        return customerMapper.convertToDomain(
                customerRepository.save(
                        c
                )
        );
    }
    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::convertToDomain);
    }

    @Override
    public Customer findById(String id) {
        return customerMapper.convertToDomain(
                customerRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer not found")
        ));
    }

    @Override
    public void delete(String id) {
        customerRepository.deleteById(id);
    }
}
