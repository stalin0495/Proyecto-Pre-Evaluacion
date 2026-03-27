package com.pichincha.customer.application.service;

import com.pichincha.customer.application.exception.ResourceNotFoundException;
import com.pichincha.customer.application.exception.SystemException;
import com.pichincha.customer.application.input.port.CustomerService;
import com.pichincha.customer.application.input.port.PersonService;
import com.pichincha.customer.application.mapper.NewCustomerMapper;
import com.pichincha.customer.application.output.port.CustomerOutPort;
import com.pichincha.customer.domain.Customer;
import com.pichincha.customer.application.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerUseCase implements CustomerService {

    private final CustomerOutPort customerOutPort;

    private final  PersonService personService;

    private final NewCustomerMapper customerMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Customer create(Customer customer) {
        if (customer == null) {
            throw new ValidationException("Customer cannot be null");
        }
        
        try {
            this.validateIdentification(customer, null);
            customer.setName(customer.getName().toUpperCase());
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customer.setStatus(Boolean.TRUE);
            return customerOutPort.save(customer);
        } catch (ValidationException e) {
            throw e;
        } catch (NullPointerException e) {
            log.error("Null value in required field", e);
            throw new ValidationException("Required field is missing");
        } catch (Exception e) {
            log.error("Error creating customer", e);
            throw new SystemException("Error creating customer", e);
        }
    }

    @Override
    @Transactional
    public Customer update(Customer customer) {
        if (customer == null || customer.getPersonId() == null) {
            throw new ValidationException("Customer and PersonId cannot be null");
        }
        
        try {
            Customer customerSearch = this.findById(customer.getPersonId());
            String customerId = customerSearch.getCustomerId();
            String previousIdentification = customerSearch.getIdentification();
            
            customerMapper.updateCustomer(customer, customerSearch);
            this.validateIdentification(customerSearch, previousIdentification);
            
            customerSearch.setCustomerId(customerId);
            return customerOutPort.save(customerSearch);
        } catch (ResourceNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating customer: {}", customer.getPersonId(), e);
            throw new SystemException("Error updating customer", e);
        }
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerOutPort.findAll(pageable);
    }

    @Override
    public Customer findById(String id) {
        return customerOutPort.findById(id);
    }

    @Override
    @Transactional
    public void delete(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new ValidationException("Customer ID cannot be null or empty");
        }
        
        try {
            Customer customer = customerOutPort.findByCustomerId(customerId);
            customer.setStatus(Boolean.FALSE);
            customerOutPort.save(customer);
            
            log.info("Customer soft deleted successfully: {}", customerId);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting customer: {}", customerId, e);
            throw new SystemException("Error deleting customer", e);
        }
    }

    private void validateIdentification(Customer customer, String identification){
        if (Boolean.FALSE.equals(this.isUniqueIdentification(customer.getIdentification(), identification))){
            throw new ValidationException("The identification is already registered");
        }
    }

    private boolean isUniqueIdentification(String identification, String identificationUpdate) {
        try {
            this.personService.findByIdentification(identification, identificationUpdate);
            return Boolean.FALSE;
        } catch (ResourceNotFoundException e) {
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Unexpected error validating identification: {}", identification, e);
            throw new SystemException("Error validating identification", e);
        }
    }
}
