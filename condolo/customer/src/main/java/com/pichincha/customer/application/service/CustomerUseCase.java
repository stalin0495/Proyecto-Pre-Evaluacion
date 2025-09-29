package com.pichincha.customer.application.service;

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

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerUseCase implements CustomerService {

    private final CustomerOutPort customerOutPort;

    private final  PersonService personService;

    private final NewCustomerMapper customerMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Customer create(Customer customer) {
        this.validateIdentification(customer, null);
        customer.setName(customer.getName().toUpperCase());
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setStatus(Boolean.TRUE);
        return customerOutPort.save(customer);
    }

    @Override
    public Customer update(Customer customer) {
        Customer customerSearch = this.findById(customer.getPersonId());
        String customerId = customerSearch.getCustomerId();
        String previousIdentification = customerSearch.getIdentification();
        customerMapper.updateCustomer(customer, customerSearch);
        this.validateIdentification(customerSearch, previousIdentification);
        customer.setCustomerId(customerId);
        return customerOutPort.save(customer);
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
    public void delete(String id) {
        Customer customer = this.findById(id);
        customer.setStatus(Boolean.FALSE);
        this.update(customer);
    }

    private void validateIdentification(Customer customer, String identification){
        if (Boolean.FALSE.equals(this.isUniqueIdentification(customer.getIdentification(), identification))){
            throw new ValidationException("The identification is already registered");
        }
    }

    private boolean isUniqueIdentification(String identification, String identificationUpdate) {
        boolean isUniqueIdentification = Boolean.TRUE;
        try{
            this.personService.findByIdentification(identification, identificationUpdate);
            isUniqueIdentification = Boolean.FALSE;
        }catch(Exception e){
            log.info(e.getMessage());
        }
        return isUniqueIdentification;
    }

}
