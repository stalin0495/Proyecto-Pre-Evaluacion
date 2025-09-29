package com.pichincha.account.application.service;

import com.pichincha.account.application.exception.ResourceNotFoundException;
import com.pichincha.account.application.exception.ValidationException;
import com.pichincha.account.application.input.port.AccountService;
import com.pichincha.account.application.input.port.CustomerService;
import com.pichincha.account.application.output.port.AccountOutPort;
import com.pichincha.account.domain.Account;
import com.pichincha.account.domain.external.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountUseCase implements AccountService {

    private final AccountOutPort accountOutPort;

    private final CustomerService customerService;

    @Override
    public Account create(Account account) {
        account.setStatus(Boolean.TRUE);
        return save(account, null);
    }

    @Override
    public Account update(Account account) {
        this.findById(account.getAccountId());
        return save(account, account.getAccountNumber());
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        return this.accountOutPort.findAllActiveItems(pageable);

    }

    @Override
    public Account findById(String id) {
        Account account = this.accountOutPort.findById(id);
        if (Boolean.FALSE.equals(account.isStatus())) {
            throw new ResourceNotFoundException("The account with id " + id + " not found");
        }
        return account;
    }

    @Override
    public void delete(String id) {
        Account account = findById(id);
        account.setStatus(Boolean.FALSE);
        this.update(account);
    }

    private Account save(Account account, String accountNumberUpdate){
        this.validateUniqueIdentification(account.getAccountNumber(), accountNumberUpdate);
        this.validateCustomerById(account.getCustomerId());
        return this.accountOutPort.save(
                account);
    }

    private void validateUniqueIdentification(String accountNumber, String accountNumberUpdate) {
        try {
            Account existingAccount = this.accountOutPort.findByAccountNumber(
                    accountNumber, accountNumberUpdate);
            if (existingAccount != null) {
                throw new ValidationException("The account number must be unique");
            }
        } catch (ResourceNotFoundException ex) {
            log.info("The account number {} is unique", accountNumber);
        }
    }

    private void validateCustomerById(String customerId) {
        Customer customer = this.customerService.findById(customerId);
        if (customer == null || Boolean.FALSE.equals(customer.isStatus())) {
            throw new ValidationException("The customer with id " + customerId + " not found");
        }
    }
}
