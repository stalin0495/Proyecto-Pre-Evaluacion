package com.pichincha.customer.application.service;

import com.pichincha.customer.application.input.port.PersonService;
import com.pichincha.customer.application.mapper.NewCustomerMapper;
import com.pichincha.customer.application.output.port.CustomerOutPort;
import com.pichincha.customer.application.exception.ValidationException;
import com.pichincha.customer.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerUseCaseTest {

    @Mock
    private CustomerOutPort customerOutPort;

    @Mock
    private PersonService personService;

    @Mock
    private NewCustomerMapper customerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerUseCase customerUseCase;

    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        sampleCustomer = Customer.builder()
                .customerId("1")
                .name("Juan Perez")
                .gender("Masculino")
                .age((short) 30)
                .identification("1234567890")
                .address("Av Amazonas 123")
                .phone("0987654321")
                .password("Password123!")
                .status(true)
                .build();
    }

    @Test
    void testCreateCustomer_DuplicateIdentification_ThrowsValidationException() {
        Customer customerToCreate = Customer.builder()
                .name("juan perez")
                .identification("1234567890")
                .password("Password123!")
                .build();

        when(personService.findByIdentification(anyString(), any())).thenReturn(sampleCustomer);

        assertThatThrownBy(() -> customerUseCase.create(customerToCreate))
                .isInstanceOf(ValidationException.class)
                .hasMessage("The identification is already registered");

        verify(customerOutPort, never()).save(any(Customer.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void testFindAllCustomers_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> customerPage = new PageImpl<>(List.of(sampleCustomer), pageable, 1);
        when(customerOutPort.findAll(pageable)).thenReturn(customerPage);

        Page<Customer> result = customerUseCase.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getCustomerId()).isEqualTo("1");
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(customerOutPort).findAll(pageable);
    }

    @Test
    void testFindCustomerById_Success() {
        String customerId = "1";
        when(customerOutPort.findById(customerId)).thenReturn(sampleCustomer);

        Customer result = customerUseCase.findById(customerId);

        assertThat(result).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo(customerId);
        assertThat(result.getName()).isEqualTo("Juan Perez");

        verify(customerOutPort).findById(customerId);
    }

    @Test
    void testUpdateCustomer_Success() {
        String customerId = "1";
        Customer updateData = Customer.builder()
                .personId(customerId)
                .name("Juan Carlos Updated")
                .phone("0999888777")
                .build();

        Customer existingCustomer = Customer.builder()
                .customerId("customer-123")
                .personId(customerId)
                .name("Juan Perez")
                .identification("1234567890")
                .phone("0987654321")
                .build();

        Customer updatedCustomer = Customer.builder()
                .customerId("customer-123")
                .name("Juan Carlos Updated")
                .identification("1234567890")
                .phone("0999888777")
                .build();

        when(customerOutPort.findById(customerId)).thenReturn(existingCustomer);
        when(personService.findByIdentification(anyString(), anyString())).thenThrow(new RuntimeException("Not found"));
        when(customerOutPort.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerUseCase.update(updateData);

        assertThat(result).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo("customer-123");
        assertThat(result.getName()).isEqualTo("Juan Carlos Updated");
        assertThat(result.getPhone()).isEqualTo("0999888777");

        verify(customerOutPort).findById(customerId);
        verify(customerMapper).updateCustomer(updateData, existingCustomer);
        verify(personService).findByIdentification(existingCustomer.getIdentification(), existingCustomer.getIdentification());
        verify(customerOutPort).save(updateData);
    }

    @Test
    void testValidateIdentification_UniqueIdentification_NoException() {
        Customer customer = Customer.builder()
                .identification("9999999999")
                .name("New Customer")
                .password("Password123!")
                .build();

        when(personService.findByIdentification(anyString(), any())).thenThrow(new RuntimeException("Not found"));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(customerOutPort.save(any(Customer.class))).thenReturn(customer);

        customerUseCase.create(customer);

        verify(personService).findByIdentification(customer.getIdentification(), null);
    }

    @Test
    void testPasswordEncoding() {
        String plainPassword = "MySecretPassword123!";
        Customer customer = Customer.builder()
                .name("test user")
                .identification("5555555555")
                .password(plainPassword)
                .build();

        when(personService.findByIdentification(anyString(), any())).thenThrow(new RuntimeException("Not found"));
        when(passwordEncoder.encode(plainPassword)).thenReturn("$2a$10$encodedPasswordHash");
        when(customerOutPort.save(any(Customer.class))).thenReturn(customer);

        customerUseCase.create(customer);

        verify(passwordEncoder).encode(plainPassword);
        verify(customerOutPort).save(argThat(c ->
            "$2a$10$encodedPasswordHash".equals(c.getPassword())
        ));
    }
}
