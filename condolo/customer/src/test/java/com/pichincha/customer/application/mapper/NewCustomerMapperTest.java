package com.pichincha.customer.application.mapper;

import com.pichincha.customer.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class NewCustomerMapperTest {

    private NewCustomerMapper newCustomerMapper;
    private Customer existingCustomer;

    @BeforeEach
    void setUp() {

        newCustomerMapper = Mappers.getMapper(NewCustomerMapper.class);

        existingCustomer = Customer.builder()
                .customerId("customer-123")
                .personId("person-456")
                .name("JUAN PEREZ")
                .gender("Masculino")
                .age((short) 30)
                .identification("1234567890")
                .address("Av Amazonas 123")
                .phone("0987654321")
                .password("oldPassword")
                .status(true)
                .build();
    }

    @Test
    void testUpdateCustomer_CompletelyNullSource_NoChanges() {

        Customer emptyUpdate = Customer.builder().build();

        String originalName = existingCustomer.getName();
        String originalPhone = existingCustomer.getPhone();
        String originalAddress = existingCustomer.getAddress();
        String originalGender = existingCustomer.getGender();
        short originalAge = existingCustomer.getAge();
        boolean originalStatus = existingCustomer.isStatus();

        newCustomerMapper.updateCustomer(emptyUpdate, existingCustomer);

        assertThat(existingCustomer.getName()).isEqualTo(originalName);
        assertThat(existingCustomer.getPhone()).isEqualTo(originalPhone);
        assertThat(existingCustomer.getAddress()).isEqualTo(originalAddress);
        assertThat(existingCustomer.getGender()).isEqualTo(originalGender);
        assertThat(existingCustomer.getAge()).isEqualTo(originalAge);
        assertThat(existingCustomer.isStatus()).isEqualTo(originalStatus);
        assertThat(existingCustomer.getCustomerId()).isEqualTo("customer-123");
        assertThat(existingCustomer.getPersonId()).isEqualTo("person-456");
    }
}
