package com.pichincha.account.application.service;

import com.pichincha.account.application.exception.ResourceNotFoundException;
import com.pichincha.account.application.exception.ValidationException;
import com.pichincha.account.application.input.port.CustomerService;
import com.pichincha.account.application.output.port.AccountOutPort;
import com.pichincha.account.domain.Account;
import com.pichincha.account.domain.external.Customer;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountUseCaseTest {

    @Mock
    private AccountOutPort accountOutPort;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AccountUseCase accountUseCase;

    private Account mockAccount;
    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        mockAccount = Account.builder()
                .accountId("ACC001")
                .accountNumber("1001234567")
                .accountType("AHORROS")
                .customerId("CUST001")
                .initialBalance(new BigDecimal("1000.00"))
                .status(true)
                .build();

        mockCustomer = Customer.builder()
                .customerId("CUST001")
                .name("Juan Pérez")
                .status(true)
                .build();
    }

    @Test
    void testCreateAccount_Success() {
        Account inputAccount = Account.builder()
                .accountNumber("1001234567")
                .accountType("AHORROS")
                .customerId("CUST001")
                .initialBalance(new BigDecimal("1000.00"))
                .build();

        when(customerService.findById("CUST001")).thenReturn(mockCustomer);
        when(accountOutPort.findByAccountNumber(anyString(), any())).thenThrow(new ResourceNotFoundException("Not found"));
        when(accountOutPort.save(any(Account.class))).thenReturn(mockAccount);

        Account result = accountUseCase.create(inputAccount);

        assertNotNull(result);
        assertEquals("ACC001", result.getAccountId());
        assertEquals("1001234567", result.getAccountNumber());
        assertEquals("AHORROS", result.getAccountType());
        assertEquals("CUST001", result.getCustomerId());
        assertTrue(result.isStatus());

        verify(customerService, times(1)).findById("CUST001");
        verify(accountOutPort, times(1)).findByAccountNumber("1001234567", null);
        verify(accountOutPort, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateAccount_CustomerNotFound() {
        Account inputAccount = Account.builder()
                .accountNumber("1001234567")
                .accountType("AHORROS")
                .customerId("CUST999")
                .initialBalance(new BigDecimal("1000.00"))
                .build();

        when(customerService.findById("CUST999")).thenReturn(null);

        ValidationException exception = assertThrows(ValidationException.class,
            () -> accountUseCase.create(inputAccount));

        assertEquals("The customer with id CUST999 not found", exception.getMessage());

        verify(customerService, times(1)).findById("CUST999");
        verify(accountOutPort, never()).save(any(Account.class));
    }


    @Test
    void testCreateAccount_DuplicateAccountNumber() {
        Account inputAccount = Account.builder()
                .accountNumber("1001234567")
                .accountType("AHORROS")
                .customerId("CUST001")
                .initialBalance(new BigDecimal("1000.00"))
                .build();

        when(accountOutPort.findByAccountNumber("1001234567", null)).thenReturn(mockAccount);

        ValidationException exception = assertThrows(ValidationException.class,
            () -> accountUseCase.create(inputAccount));

        assertEquals("The account number must be unique", exception.getMessage());

        verify(accountOutPort, times(1)).findByAccountNumber("1001234567", null);
        verify(customerService, never()).findById(any());
        verify(accountOutPort, never()).save(any(Account.class));
    }

    @Test
    void testFindById_Success() {
        String accountId = "ACC001";
        when(accountOutPort.findById(accountId)).thenReturn(mockAccount);

        Account result = accountUseCase.findById(accountId);

        assertNotNull(result);
        assertEquals("ACC001", result.getAccountId());
        assertEquals("1001234567", result.getAccountNumber());
        assertTrue(result.isStatus());

        verify(accountOutPort, times(1)).findById(accountId);
    }

    @Test
    void testFindAll_Success() {
        List<Account> accounts = Arrays.asList(mockAccount);
        Page<Account> accountPage = new PageImpl<>(accounts, PageRequest.of(0, 10), 1);
        Pageable pageable = PageRequest.of(0, 10);

        when(accountOutPort.findAllActiveItems(pageable)).thenReturn(accountPage);

        Page<Account> result = accountUseCase.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("ACC001", result.getContent().get(0).getAccountId());

        verify(accountOutPort, times(1)).findAllActiveItems(pageable);
    }

    @Test
    void testUpdateAccount_Success() {
        Account updateAccount = Account.builder()
                .accountId("ACC001")
                .accountNumber("1001234567")
                .accountType("CORRIENTE")
                .customerId("CUST001")
                .initialBalance(new BigDecimal("2000.00"))
                .status(true)
                .build();

        when(accountOutPort.findById("ACC001")).thenReturn(mockAccount);
        when(customerService.findById("CUST001")).thenReturn(mockCustomer);
        when(accountOutPort.findByAccountNumber("1001234567", "1001234567")).thenThrow(new ResourceNotFoundException("Not found"));
        when(accountOutPort.save(any(Account.class))).thenReturn(updateAccount);

        Account result = accountUseCase.update(updateAccount);

        assertNotNull(result);
        assertEquals("ACC001", result.getAccountId());
        assertEquals("CORRIENTE", result.getAccountType());
        assertEquals(new BigDecimal("2000.00"), result.getInitialBalance());

        verify(accountOutPort, times(1)).findById("ACC001");
        verify(customerService, times(1)).findById("CUST001");
        verify(accountOutPort, times(1)).save(any(Account.class));
    }

    @Test
    void testDeleteAccount_Success() {
        String accountId = "ACC001";

        Account accountForFirstCall = Account.builder()
                .accountId("ACC001")
                .accountNumber("1001234567")
                .accountType("AHORROS")
                .customerId("CUST001")
                .initialBalance(new BigDecimal("1000.00"))
                .status(true)
                .build();

        Account accountForSecondCall = Account.builder()
                .accountId("ACC001")
                .accountNumber("1001234567")
                .accountType("AHORROS")
                .customerId("CUST001")
                .initialBalance(new BigDecimal("1000.00"))
                .status(true)
                .build();

        when(accountOutPort.findById(accountId))
                .thenReturn(accountForFirstCall)
                .thenReturn(accountForSecondCall);

        when(customerService.findById("CUST001")).thenReturn(mockCustomer);
        when(accountOutPort.findByAccountNumber("1001234567", "1001234567"))
                .thenThrow(new ResourceNotFoundException("Not found"));
        when(accountOutPort.save(any(Account.class))).thenReturn(accountForFirstCall);

        accountUseCase.delete(accountId);

        verify(accountOutPort, times(2)).findById(accountId);
        verify(customerService, times(1)).findById("CUST001");
        verify(accountOutPort, times(1)).findByAccountNumber("1001234567", "1001234567");
        verify(accountOutPort, times(1)).save(any(Account.class));

        verify(accountOutPort).save(argThat(account ->
            account.getAccountId().equals("ACC001") &&
            Boolean.FALSE.equals(account.isStatus())
        ));
    }
}
