package com.pichincha.customer.infrastructure.input.adapter.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichincha.customer.application.input.port.CustomerService;
import com.pichincha.customer.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Controller Validation Tests")
class CustomerControllerValidationTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        objectMapper = new ObjectMapper();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "123", "User123", "user@domain", "User-Name"})
    @DisplayName("Should reject invalid name formats")
    void testCreateCustomer_InvalidNameFormats(String invalidName) throws Exception {
        Customer customerWithInvalidName = Customer.builder()
                .name(invalidName)
                .gender("Masculino")
                .age((short) 30)
                .identification("1234567890")
                .address("Av Amazonas 123")
                .phone("0987654321")
                .password("Password123!")
                .build();

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerWithInvalidName)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).create(any(Customer.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789", "12345678901", "abcdefghij", "123456789a", ""})
    @DisplayName("Should reject invalid identification formats")
    void testCreateCustomer_InvalidIdentificationFormats(String invalidId) throws Exception {
        Customer customerWithInvalidId = Customer.builder()
                .name("Juan Perez")
                .gender("Masculino")
                .age((short) 30)
                .identification(invalidId)
                .address("Av Amazonas 123")
                .phone("0987654321")
                .password("Password123!")
                .build();

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerWithInvalidId)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).create(any(Customer.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "PASSWORD", "12345678", "Password", "Pass123", "password123!", "PASSWORD123!"})
    @DisplayName("Should reject weak password formats")
    void testCreateCustomer_WeakPasswords(String weakPassword) throws Exception {
        Customer customerWithWeakPassword = Customer.builder()
                .name("Juan Perez")
                .gender("Masculino")
                .age((short) 30)
                .identification("1234567890")
                .address("Av Amazonas 123")
                .phone("0987654321")
                .password(weakPassword)
                .build();

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerWithWeakPassword)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).create(any(Customer.class));
    }


    @Test
    @DisplayName("Should reject customer with null required fields")
    void testCreateCustomer_NullRequiredFields() throws Exception {
        Customer customerWithNulls = Customer.builder()
                .name(null)
                .gender(null)
                .identification(null)
                .address(null)
                .phone(null)
                .password(null)
                .build();

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerWithNulls)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).create(any(Customer.class));
    }

    @Test
    @DisplayName("Should reject malformed JSON")
    void testCreateCustomer_MalformedJson() throws Exception {
        // Given
        String malformedJson = "{ \"name\": \"Juan\", \"age\": }";

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).create(any(Customer.class));
    }

    @Test
    @DisplayName("Should reject request without Content-Type")
    void testCreateCustomer_MissingContentType() throws Exception {
        Customer validCustomer = Customer.builder()
                .name("Juan Perez")
                .gender("Masculino")
                .age((short) 30)
                .identification("1234567890")
                .address("Av Amazonas 123")
                .phone("0987654321")
                .password("Password123!")
                .build();

        mockMvc.perform(post("/v1/customers")
                        .content(objectMapper.writeValueAsString(validCustomer)))
                .andExpect(status().isUnsupportedMediaType());

        verify(customerService, never()).create(any(Customer.class));
    }

    @Test
    @DisplayName("Should handle empty request body")
    void testCreateCustomer_EmptyBody() throws Exception {
        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).create(any(Customer.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jose Maria", "Ana Sofia", "Luis Alberto", "Carmen Rosa"})
    @DisplayName("Should accept valid Spanish names")
    void testCreateCustomer_ValidSpanishNames(String validName) throws Exception {
        Customer validCustomer = Customer.builder()
                .name(validName)
                .gender("Masculino")
                .age((short) 30)
                .identification("1234567890")
                .address("Av Amazonas 123")
                .phone("0987654321")
                .password("Password123!")
                .build();

        Customer savedCustomer = Customer.builder()
                .customerId("1")
                .name(validName)
                .gender("Masculino")
                .age((short) 30)
                .identification("1234567890")
                .address("Av Amazonas 123")
                .phone("0987654321")
                .password("Password123!")
                .status(true)
                .build();

        when(customerService.create(any(Customer.class))).thenReturn(savedCustomer);

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(validName));

        verify(customerService, times(1)).create(any(Customer.class));
    }

    @Test
    @DisplayName("Should reject customer with invalid age range")
    void testCreateCustomer_InvalidAge() throws Exception {
        Customer customerWithInvalidAge = Customer.builder()
                .name("Test User")
                .gender("Masculino")
                .age((short) -1)
                .identification("1234567890")
                .address("Av Amazonas 123")
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
    @DisplayName("Should accept customer with maximum valid age")
    void testCreateCustomer_MaxValidAge() throws Exception {
        Customer validCustomer = Customer.builder()
                .name("Elena Vasquez")
                .gender("Femenino")
                .age((short) 255)
                .identification("1234567890")
                .address("Av Amazonas 123")
                .phone("0987654321")
                .password("Password123!")
                .build();

        Customer savedCustomer = Customer.builder()
                .customerId("1")
                .name("Elena Vasquez")
                .gender("Femenino")
                .age((short) 255)
                .identification("1234567890")
                .address("Av Amazonas 123")
                .phone("0987654321")
                .password("Password123!")
                .status(true)
                .build();

        when(customerService.create(any(Customer.class))).thenReturn(savedCustomer);

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.age").value(255));

        verify(customerService, times(1)).create(any(Customer.class));
    }
}
