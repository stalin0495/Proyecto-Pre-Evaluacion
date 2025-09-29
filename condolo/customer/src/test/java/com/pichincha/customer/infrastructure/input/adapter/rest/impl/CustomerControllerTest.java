package com.pichincha.customer.infrastructure.input.adapter.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichincha.customer.application.input.port.CustomerService;
import com.pichincha.customer.domain.Customer;
import jakarta.servlet.ServletException;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

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
        sampleCustomer = CustomerTestDataBuilder.validCustomerWithId("1");
    }

    @Test
    void testCreateCustomer_Success() throws Exception {

        Customer customerRequest = CustomerTestDataBuilder.customerForCreation();
        when(customerService.create(any(Customer.class))).thenReturn(sampleCustomer);


        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value("1"))
                .andExpect(jsonPath("$.name").value("Juan Perez"))
                .andExpect(jsonPath("$.identification").value("1234567890"))
                .andExpect(jsonPath("$.status").value(true));

        verify(customerService, times(1)).create(any(Customer.class));
    }

    @Test
    void testFindAllCustomers_Success() throws Exception {

        List<Customer> customerList = List.of(sampleCustomer);
        Page<Customer> customerPage = new PageImpl<>(customerList, PageRequest.of(0, 10), 1);

        when(customerService.findAll(any(PageRequest.class))).thenReturn(customerPage);


        mockMvc.perform(get("/v1/customers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].customerId").value("1"))
                .andExpect(jsonPath("$.content[0].name").value("Juan Perez"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(10));

        verify(customerService, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testFindAllCustomers_WithDefaultParams() throws Exception {

        Page<Customer> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(customerService.findAll(any(PageRequest.class))).thenReturn(emptyPage);


        mockMvc.perform(get("/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));

        verify(customerService, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    void testFindCustomerById_Success() throws Exception {

        String customerId = "1";
        when(customerService.findById(customerId)).thenReturn(sampleCustomer);


        mockMvc.perform(get("/v1/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value("1"))
                .andExpect(jsonPath("$.name").value("Juan Perez"))
                .andExpect(jsonPath("$.identification").value("1234567890"))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(customerService, times(1)).findById(customerId);
    }

    @Test
    void testUpdateCustomer_Success() throws Exception {

        String customerId = "1";
        Customer updateRequest = CustomerTestDataBuilder.customerForUpdate();

        Customer updatedCustomer = Customer.builder()
                .customerId(customerId)
                .name("Carlos Mendoza")
                .gender("Masculino")
                .age((short) 30)
                .identification("1234567890")
                .address("Nueva direccion 789")
                .phone("0999888777")
                .status(true)
                .build();

        when(customerService.update(any(Customer.class))).thenReturn(updatedCustomer);

        mockMvc.perform(patch("/v1/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.name").value("Carlos Mendoza"))
                .andExpect(jsonPath("$.address").value("Nueva direccion 789"))
                .andExpect(jsonPath("$.phone").value("0999888777"))
                .andExpect(jsonPath("$.status").value(true));

        verify(customerService, times(1)).update(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_Success() throws Exception {

        String customerId = "1";
        doNothing().when(customerService).delete(customerId);

        mockMvc.perform(delete("/v1/customers/{customerId}", customerId))
                .andExpect(status().isOk());

        verify(customerService, times(1)).delete(customerId);
    }

    @Test
    void testCreateCustomer_ServiceThrowsException() throws Exception {

        Customer customerRequest = CustomerTestDataBuilder.validCustomer();
        when(customerService.create(any(Customer.class)))
                .thenThrow(new RuntimeException("Service error"));

        try {
            mockMvc.perform(post("/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerRequest)));
        } catch (Exception e) {

            assert e instanceof ServletException || e.getCause() instanceof RuntimeException;
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

            assert e instanceof ServletException || e.getCause() instanceof RuntimeException;
        }

        verify(customerService, times(1)).findById(nonExistentId);
    }

    @Test
    void testUpdateCustomer_ServiceThrowsException() throws Exception {

        String customerId = "1";
        Customer updateRequest = CustomerTestDataBuilder.validCustomer();
        when(customerService.update(any(Customer.class)))
                .thenThrow(new RuntimeException("Update failed"));


        try {
            mockMvc.perform(patch("/v1/customers/{customerId}", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)));
        } catch (Exception e) {

            assert e instanceof ServletException || e.getCause() instanceof RuntimeException;
        }

        verify(customerService, times(1)).update(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_ServiceThrowsException() throws Exception {

        String customerId = "1";
        doThrow(new RuntimeException("Cannot delete customer"))
                .when(customerService).delete(customerId);

        try {
            mockMvc.perform(delete("/v1/customers/{customerId}", customerId));
        } catch (Exception e) {

            assert e instanceof ServletException || e.getCause() instanceof RuntimeException;
        }

        verify(customerService, times(1)).delete(customerId);
    }
}
