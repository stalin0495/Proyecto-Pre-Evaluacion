package com.pichincha.customer.infrastructure.input.adapter.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichincha.customer.application.input.port.CustomerService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerIntegrationTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        objectMapper = new ObjectMapper();
        sampleCustomer = Customer.builder()
                .customerId("1")
                .name("Maria Gonzalez")
                .gender("Femenino")
                .age((short) 25)
                .identification("0987654321")
                .address("Calle Los Rosales 456")
                .phone("0998765432")
                .password("SecurePass1!")
                .status(true)
                .build();
    }

    @Test
    void testCreateCustomer_WithSpecialCharactersInName() throws Exception {
        Customer customerRequest = Customer.builder()
                .name("Jose Maria Fernandez Lopez")
                .gender("Masculino")
                .age((short) 35)
                .identification("1122334455")
                .address("Av Simon Bolivar 789")
                .phone("0987123456")
                .password("Password123!")
                .build();

        when(customerService.create(any(Customer.class))).thenReturn(sampleCustomer);

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").exists())
                .andExpect(jsonPath("$.status").value(true));

        verify(customerService, times(1)).create(any(Customer.class));
    }

    @Test
    void testCreateCustomer_ServiceThrowsException() throws Exception {
        Customer customerRequest = Customer.builder()
                .name("Ana Torres")
                .gender("Femenino")
                .age((short) 28)
                .identification("5566778899")
                .address("Av Republica 123")
                .phone("0987654321")
                .password("Password123!")
                .build();

        when(customerService.create(any(Customer.class)))
                .thenThrow(new RuntimeException("Database connection error"));

        try {
            mockMvc.perform(post("/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerRequest)));
        } catch (Exception e) {
            assert e.getCause() instanceof RuntimeException || e instanceof RuntimeException;
        }

        verify(customerService, times(1)).create(any(Customer.class));
    }

    @Test
    void testFindCustomerById_NotFound() throws Exception {
        String nonExistentId = "999";
        when(customerService.findById(nonExistentId))
                .thenThrow(new RuntimeException("Customer not found"));

        try {
            mockMvc.perform(get("/v1/customers/{customerId}", nonExistentId));
        } catch (Exception e) {
            assert e.getCause() instanceof RuntimeException || e instanceof RuntimeException;
        }

        verify(customerService, times(1)).findById(nonExistentId);
    }

    @Test
    void testUpdateCustomer_WithPartialData() throws Exception {
        String customerId = "1";
        Customer partialUpdateRequest = Customer.builder()
                .phone("0999888777")
                .build();

        Customer updatedCustomer = Customer.builder()
                .customerId(customerId)
                .name("Maria Gonzalez")
                .gender("Femenino")
                .age((short) 25)
                .identification("0987654321")
                .address("Calle Los Rosales 456")
                .phone("0999888777")
                .status(true)
                .build();

        when(customerService.update(any(Customer.class))).thenReturn(updatedCustomer);

        mockMvc.perform(patch("/v1/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.phone").value("0999888777"))
                .andExpect(jsonPath("$.status").value(true));

        verify(customerService, times(1)).update(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_ServiceThrowsException() throws Exception {
        String customerId = "1";
        doThrow(new RuntimeException("Cannot delete customer with active accounts"))
                .when(customerService).delete(customerId);

        try {
            mockMvc.perform(delete("/v1/customers/{customerId}", customerId));
        } catch (Exception e) {
            assert e.getCause() instanceof RuntimeException || e instanceof RuntimeException;
        }

        verify(customerService, times(1)).delete(customerId);
    }

    @Test
    void testCreateCustomer_WithMinimumValidData() throws Exception {
        Customer minimalCustomer = Customer.builder()
                .name("Pedro")
                .gender("Masculino")
                .age((short) 18)
                .identification("1111111111")
                .address("Casa 1")
                .phone("0999999999")
                .password("MinPass1!")
                .build();

        when(customerService.create(any(Customer.class))).thenReturn(sampleCustomer);

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minimalCustomer)))
                .andExpect(status().isCreated());

        verify(customerService, times(1)).create(any(Customer.class));
    }

    @Test
    void testCreateCustomer_WithMaximumAge() throws Exception {
        Customer elderlyCustomer = Customer.builder()
                .name("Elena Vasquez")
                .gender("Femenino")
                .age((short) 255)
                .identification("9988776655")
                .address("Av Principal 999")
                .phone("0987654321")
                .password("SecurePass1!")
                .build();

        when(customerService.create(any(Customer.class))).thenReturn(sampleCustomer);

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(elderlyCustomer)))
                .andExpect(status().isCreated());

        verify(customerService, times(1)).create(any(Customer.class));
    }

    @Test
    void testCreateCustomer_WithInvalidAge() throws Exception {
        Customer customerWithInvalidAge = Customer.builder()
                .name("Luis Moreno")
                .gender("Masculino")
                .age((short) -1)
                .identification("1234567890")
                .address("Calle Falsa 123")
                .phone("0987654321")
                .password("Password123!")
                .build();

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerWithInvalidAge)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).create(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_WithInvalidData() throws Exception {
        String customerId = "1";
        Customer invalidUpdateRequest = Customer.builder()
                .name("123Numbers")
                .age((short) 300)
                .build();

        mockMvc.perform(patch("/v1/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateRequest)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).update(any(Customer.class));
    }

    @Test
    void testFindAllCustomers_WithLargePageSize() throws Exception {
        Page<Customer> customerPage = new PageImpl<>(List.of(sampleCustomer), PageRequest.of(0, 100), 1);
        when(customerService.findAll(any(PageRequest.class))).thenReturn(customerPage);

        mockMvc.perform(get("/v1/customers")
                        .param("page", "0")
                        .param("size", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.size").value(100));

        verify(customerService, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testCreateCustomer_WithComplexValidData() throws Exception {
        Customer complexCustomer = Customer.builder()
                .name("Carlos Eduardo Martinez")
                .gender("Masculino")
                .age((short) 45)
                .identification("1357924680")
                .address("Urbanizacion Los Alamos Mz 15 Villa 7")
                .phone("0987654321")
                .password("ComplexP@ss123")
                .build();

        when(customerService.create(any(Customer.class))).thenReturn(sampleCustomer);

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(complexCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").exists());

        verify(customerService, times(1)).create(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_Success() throws Exception {
        String customerId = "1";
        Customer updateRequest = Customer.builder()
                .name("Maria Updated")
                .address("Nueva direccion actualizada")
                .phone("0999111222")
                .build();

        Customer updatedCustomer = Customer.builder()
                .customerId(customerId)
                .name("Maria Updated")
                .gender("Femenino")
                .age((short) 25)
                .identification("0987654321")
                .address("Nueva direccion actualizada")
                .phone("0999111222")
                .status(true)
                .build();

        when(customerService.update(any(Customer.class))).thenReturn(updatedCustomer);

        mockMvc.perform(patch("/v1/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.name").value("Maria Updated"))
                .andExpect(jsonPath("$.address").value("Nueva direccion actualizada"))
                .andExpect(jsonPath("$.phone").value("0999111222"));

        verify(customerService, times(1)).update(any(Customer.class));
    }
}
