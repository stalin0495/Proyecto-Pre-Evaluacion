package com.pichincha.account.application.service;

import com.pichincha.account.application.exception.ValidationException;
import com.pichincha.account.application.input.port.AccountService;
import com.pichincha.account.application.input.port.CustomerService;
import com.pichincha.account.application.output.port.TransactionOutPort;
import com.pichincha.account.domain.Account;
import com.pichincha.account.domain.Report;
import com.pichincha.account.domain.Transaction;
import com.pichincha.account.domain.external.Customer;
import feign.FeignException;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionUseCaseTest {

    @Mock
    private TransactionOutPort transactionOutPort;

    @Mock
    private AccountService accountService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private TransactionUseCase transactionUseCase;

    private Transaction mockTransaction;
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

        mockTransaction = Transaction.builder()
                .transactionId("TXN001")
                .accountId("ACC001")
                .transactionType("DEPOSIT")
                .amount(new BigDecimal("500.00"))
                .balance(new BigDecimal("1500.00"))
                .date(LocalDateTime.now())
                .build();

        mockCustomer = Customer.builder()
                .customerId("CUST001")
                .name("Juan Pérez")
                .status(true)
                .build();
    }

    @Test
    void testCreateTransaction_DepositSuccess() {
        Transaction inputTransaction = Transaction.builder()
                .accountId("ACC001")
                .transactionType("DEPOSIT")
                .amount(new BigDecimal("500.00"))
                .build();

        when(accountService.findById("ACC001")).thenReturn(mockAccount);
        when(transactionOutPort.save(any(Transaction.class))).thenReturn(mockTransaction);
        when(accountService.update(any(Account.class))).thenReturn(mockAccount);

        Transaction result = transactionUseCase.create(inputTransaction);

        assertNotNull(result);
        assertEquals("TXN001", result.getTransactionId());
        assertEquals("ACC001", result.getAccountId());
        assertEquals("DEPOSIT", result.getTransactionType());
        assertEquals(new BigDecimal("500.00"), result.getAmount());
        assertEquals(new BigDecimal("1500.00"), result.getBalance());

        verify(accountService, times(1)).findById("ACC001");
        verify(transactionOutPort, times(1)).save(any(Transaction.class));
        verify(accountService, times(1)).update(any(Account.class));
    }

    @Test
    void testCreateTransaction_WithdrawalSuccess() {
        Transaction inputTransaction = Transaction.builder()
                .accountId("ACC001")
                .transactionType("WITHDRAWAL")
                .amount(new BigDecimal("-300.00"))
                .build();

        Transaction withdrawalTransaction = Transaction.builder()
                .transactionId("TXN002")
                .accountId("ACC001")
                .transactionType("WITHDRAWAL")
                .amount(new BigDecimal("-300.00"))
                .balance(new BigDecimal("700.00"))
                .date(LocalDateTime.now())
                .build();

        when(accountService.findById("ACC001")).thenReturn(mockAccount);
        when(transactionOutPort.save(any(Transaction.class))).thenReturn(withdrawalTransaction);
        when(accountService.update(any(Account.class))).thenReturn(mockAccount);

        Transaction result = transactionUseCase.create(inputTransaction);

        assertNotNull(result);
        assertEquals("TXN002", result.getTransactionId());
        assertEquals("ACC001", result.getAccountId());
        assertEquals("WITHDRAWAL", result.getTransactionType());
        assertEquals(new BigDecimal("-300.00"), result.getAmount());
        assertEquals(new BigDecimal("700.00"), result.getBalance());

        verify(accountService, times(1)).findById("ACC001");
        verify(transactionOutPort, times(1)).save(any(Transaction.class));
        verify(accountService, times(1)).update(any(Account.class));
    }

    @Test
    void testCreateTransaction_InvalidTransactionType() {
        Transaction inputTransaction = Transaction.builder()
                .accountId("ACC001")
                .transactionType("INVALID_TYPE")
                .amount(new BigDecimal("500.00"))
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
            () -> transactionUseCase.create(inputTransaction));

        assertEquals("The transaction type is not valid", exception.getMessage());

        verify(accountService, never()).findById(any());
        verify(transactionOutPort, never()).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_ZeroAmount() {
        Transaction inputTransaction = Transaction.builder()
                .accountId("ACC001")
                .transactionType("DEPOSIT")
                .amount(BigDecimal.ZERO)
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
            () -> transactionUseCase.create(inputTransaction));

        assertEquals("The amount is not valid", exception.getMessage());

        verify(accountService, never()).findById(any());
        verify(transactionOutPort, never()).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_InsufficientBalance() {
        Transaction inputTransaction = Transaction.builder()
                .accountId("ACC001")
                .transactionType("WITHDRAWAL")
                .amount(new BigDecimal("-1500.00"))
                .build();

        when(accountService.findById("ACC001")).thenReturn(mockAccount);

        ValidationException exception = assertThrows(ValidationException.class,
            () -> transactionUseCase.create(inputTransaction));

        assertEquals("Insufficient account balance", exception.getMessage());

        verify(accountService, times(1)).findById("ACC001");
        verify(transactionOutPort, never()).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_DepositWithNegativeAmount() {
        Transaction inputTransaction = Transaction.builder()
                .accountId("ACC001")
                .transactionType("DEPOSIT")
                .amount(new BigDecimal("-500.00"))
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
            () -> transactionUseCase.create(inputTransaction));

        assertEquals("The amount is not valid", exception.getMessage());

        verify(accountService, never()).findById(any());
        verify(transactionOutPort, never()).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_WithdrawalWithPositiveAmount() {
        Transaction inputTransaction = Transaction.builder()
                .accountId("ACC001")
                .transactionType("WITHDRAWAL")
                .amount(new BigDecimal("500.00"))
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
            () -> transactionUseCase.create(inputTransaction));

        assertEquals("The amount is not valid", exception.getMessage());

        verify(accountService, never()).findById(any());
        verify(transactionOutPort, never()).save(any(Transaction.class));
    }

    @Test
    void testFindById_Success() {
        String transactionId = "TXN001";
        when(transactionOutPort.findById(transactionId)).thenReturn(mockTransaction);

        Transaction result = transactionUseCase.findById(transactionId);

        assertNotNull(result);
        assertEquals("TXN001", result.getTransactionId());
        assertEquals("ACC001", result.getAccountId());

        verify(transactionOutPort, times(1)).findById(transactionId);
    }

    @Test
    void testFindAll_Success() {
        List<Transaction> transactions = Arrays.asList(mockTransaction);
        Page<Transaction> transactionPage = new PageImpl<>(transactions, PageRequest.of(0, 10), 1);
        Pageable pageable = PageRequest.of(0, 10);

        when(transactionOutPort.findAll(pageable)).thenReturn(transactionPage);

        Page<Transaction> result = transactionUseCase.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("TXN001", result.getContent().get(0).getTransactionId());

        verify(transactionOutPort, times(1)).findAll(pageable);
    }

    @Test
    void testDelete_Success() {
        String transactionId = "TXN001";
        doNothing().when(transactionOutPort).delete(transactionId);

        transactionUseCase.delete(transactionId);

        verify(transactionOutPort, times(1)).delete(transactionId);
    }

    @Test
    void testFindByDateBetween_Success() {
        LocalDateTime startDate = LocalDateTime.of(2025, 9, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 9, 30, 23, 59);
        String customerId = "CUST001";
        Pageable pageable = PageRequest.of(0, 10);

        List<Transaction> transactions = Arrays.asList(mockTransaction);
        Page<Transaction> transactionPage = new PageImpl<>(transactions, pageable, 1);

        when(customerService.findById(customerId)).thenReturn(mockCustomer);
        when(transactionOutPort.findByDateBetween(startDate, endDate, customerId, pageable))
                .thenReturn(transactionPage);

        Report result = transactionUseCase.findByDateBetween(startDate, endDate, customerId, pageable);

        assertNotNull(result);
        assertNotNull(result.getCustomer());
        assertEquals("CUST001", result.getCustomer().getCustomerId());
        assertEquals("Juan Pérez", result.getCustomer().getName());
        assertNotNull(result.getTransactions());
        assertEquals(1, result.getTransactions().getTotalElements());
        assertEquals("TXN001", result.getTransactions().getContent().get(0).getTransactionId());

        verify(customerService, times(1)).findById(customerId);
        verify(transactionOutPort, times(1)).findByDateBetween(startDate, endDate, customerId, pageable);
    }

    @Test
    void testFindByDateBetween_CustomerServiceUnavailable() {
        LocalDateTime startDate = LocalDateTime.of(2025, 9, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 9, 30, 23, 59);
        String customerId = "CUST001";
        Pageable pageable = PageRequest.of(0, 10);

        List<Transaction> transactions = Arrays.asList(mockTransaction);
        Page<Transaction> transactionPage = new PageImpl<>(transactions, pageable, 1);

        when(customerService.findById(customerId)).thenThrow(mock(FeignException.class));
        when(transactionOutPort.findByDateBetween(startDate, endDate, customerId, pageable))
                .thenReturn(transactionPage);

        Report result = transactionUseCase.findByDateBetween(startDate, endDate, customerId, pageable);

        assertNotNull(result);
        assertNotNull(result.getCustomer());
        assertEquals("CUST001", result.getCustomer().getCustomerId());
        assertEquals("Customer unavailable", result.getCustomer().getName());
        assertNotNull(result.getTransactions());
        assertEquals(1, result.getTransactions().getTotalElements());

        verify(customerService, times(1)).findById(customerId);
        verify(transactionOutPort, times(1)).findByDateBetween(startDate, endDate, customerId, pageable);
    }
}
