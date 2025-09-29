package com.pichincha.customer.infrastructure.input.adapter.rest.impl;

import com.pichincha.customer.domain.Customer;

public class CustomerTestDataBuilder {

    public static Customer.CustomerBuilder validCustomerBuilder() {
        return Customer.builder()
                .name("Juan Perez")
                .gender("Masculino")
                .age((short) 30)
                .identification("1234567890")
                .address("Av Amazonas 123")
                .phone("0987654321")
                .password("Password123!")
                .status(true);
    }

    public static Customer validCustomer() {
        return validCustomerBuilder().build();
    }

    public static Customer validCustomerWithId(String customerId) {
        return validCustomerBuilder()
                .customerId(customerId)
                .build();
    }

    public static Customer customerForCreation() {
        return Customer.builder()
                .name("Maria Gonzalez")
                .gender("Femenino")
                .age((short) 25)
                .identification("0987654321")
                .address("Calle Los Rosales 456")
                .phone("0998765432")
                .password("SecurePass1!")
                .build();
    }

    public static Customer customerForUpdate() {
        return Customer.builder()
                .name("Carlos Mendoza")
                .address("Nueva direccion 789")
                .phone("0999888777")
                .build();
    }

    public static Customer invalidCustomerEmptyFields() {
        return Customer.builder()
                .name("")
                .gender("")
                .identification("")
                .address("")
                .phone("")
                .password("")
                .build();
    }

    public static Customer invalidCustomerShortIdentification() {
        return Customer.builder()
                .name("Ana Torres")
                .gender("Femenino")
                .age((short) 28)
                .identification("123") // Too short
                .address("Av Republica 123")
                .phone("0987654321")
                .password("Password123!")
                .build();
    }

    public static Customer invalidCustomerWeakPassword() {
        return Customer.builder()
                .name("Pedro Silva")
                .gender("Masculino")
                .age((short) 35)
                .identification("5566778899")
                .address("Calle Falsa 456")
                .phone("0987123456")
                .password("weak") // Weak password
                .build();
    }

    public static Customer invalidCustomerNegativeAge() {
        return Customer.builder()
                .name("Luis Moreno")
                .gender("Masculino")
                .age((short) -1) // Invalid age
                .identification("1122334455")
                .address("Av Central 789")
                .phone("0998877665")
                .password("Password123!")
                .build();
    }

    public static Customer customerWithSpecialCharacters() {
        return Customer.builder()
                .name("Jose Maria Fernandez Lopez")
                .gender("Masculino")
                .age((short) 40)
                .identification("9988776655")
                .address("Av Simon Bolivar 789")
                .phone("0987123456")
                .password("MyStr0ng#Pass")
                .build();
    }

    public static Customer elderlyCustomer() {
        return Customer.builder()
                .name("Elena Vasquez")
                .gender("Femenino")
                .age((short) 80)
                .identification("1111222233")
                .address("Residencial Los Pinos Casa 15")
                .phone("0987654321")
                .password("SecurePass1!")
                .build();
    }
}
