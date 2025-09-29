package com.pichincha.account.application.service;

import com.pichincha.account.application.exception.ValidationException;
import com.pichincha.account.application.input.port.AccountService;
import com.pichincha.account.application.input.port.CustomerService;
import com.pichincha.account.application.input.port.TransactionService;
import com.pichincha.account.application.output.port.TransactionOutPort;
import com.pichincha.account.domain.Account;
import com.pichincha.account.domain.Report;
import com.pichincha.account.domain.Transaction;
import com.pichincha.account.domain.enums.TransactionType;
import com.pichincha.account.domain.external.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import feign.FeignException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionUseCase implements TransactionService {

    private final TransactionOutPort transactionOutPort;

    private final AccountService accountService;

    private final CustomerService customerService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public Transaction create(Transaction transaction) {
        this.validateTransactionType(transaction);
        this.validateAmount(transaction);
        Account account = this.accountService.findById(transaction.getAccountId());
        this.validateAccountBalance(account, transaction.getAmount());
        transaction.setBalance(account.getInitialBalance().add(transaction.getAmount()));
        transaction.setDate(LocalDateTime.now());
        Transaction newTransaction = transactionOutPort.save(
                transaction);
        updateAccountBalance(account, newTransaction.getBalance());
        return newTransaction;
    }

    @Override
    public Transaction update(Transaction transaction) {
        this.findById(transaction.getTransactionId());
        return this.transactionOutPort.save(
                transaction);
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        return this.transactionOutPort.findAll(pageable);
    }

    @Override
    public Transaction findById(String id) {
        return this.transactionOutPort.findById(id);
    }

    @Override
    public void delete(String id) {
        this.transactionOutPort.delete(id);
    }

    @Override
    public Report findByDateBetween(
            LocalDateTime startDate, LocalDateTime endDate, String customerId, Pageable pageable) {
        Customer customer = this.findCustomerById(customerId);
        Page<Transaction> transactions = this.transactionOutPort.findByDateBetween(
                startDate, endDate, customerId, pageable);
        return Report.builder().transactions(transactions).customer(customer).build();
    }

    private void updateAccountBalance(Account account, BigDecimal balance) {
        account.setInitialBalance(balance);
        this.accountService.update(account);
    }

    private void validateAccountBalance(Account account,  BigDecimal balance) {
        if (balance.add(account.getInitialBalance()).compareTo(BigDecimal.ZERO) < BigDecimal.ZERO.intValue()) {
            throw new ValidationException("Insufficient account balance");
        }
    }

    private Customer findCustomerById(String customerId){
        try{
            return this.customerService.findById(customerId);
        }catch (FeignException ex){
            log.error(ex.getMessage());
            return Customer.builder().customerId(customerId).name("Customer unavailable").build();
        }
    }

    private void validateAmount(Transaction transaction){
        String invalidAmountMessage = "The amount is not valid";
        switch (transaction.getAmount().compareTo(BigDecimal.ZERO)) {
            case 0 -> {
                throw new ValidationException(invalidAmountMessage);
            }
            case -1 -> {
                if(transaction.getTransactionType().equals(TransactionType.DEPOSIT.name())){
                    throw new ValidationException(invalidAmountMessage);
                }
            }
            case 1 -> {
                if(transaction.getTransactionType().equals(TransactionType.WITHDRAWAL.name())){
                    throw new ValidationException(invalidAmountMessage);
                }
            }
        }
    }

    private void validateTransactionType(Transaction transaction){
        if(!transaction.getTransactionType().equals(TransactionType.DEPOSIT.name()) &&
                !transaction.getTransactionType().equals(TransactionType.WITHDRAWAL.name())){
            throw new ValidationException("The transaction type is not valid");
        }
    }

}
