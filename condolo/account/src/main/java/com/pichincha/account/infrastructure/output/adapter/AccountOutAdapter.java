package com.pichincha.account.infrastructure.output.adapter;

import com.pichincha.account.application.exception.ResourceNotFoundException;
import com.pichincha.account.application.output.port.AccountOutPort;
import com.pichincha.account.domain.Account;
import com.pichincha.account.infrastructure.output.repository.AccountRepository;
import com.pichincha.account.infrastructure.output.repository.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountOutAdapter implements AccountOutPort {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    @Override
    public Account save(Account account) {
        com.pichincha.account.infrastructure.output.repository.entity.Account newAccount = accountMapper.convertToEntity(account);
        return accountMapper.convertToDomain(
                accountRepository.save(
                        newAccount
                )
        );
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable)
                .map(accountMapper::convertToDomain);
    }

    @Override
    public Account findById(String id) {
        return accountMapper.convertToDomain(
                accountRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found"))
        );
    }

    @Override
    public void delete(String id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Account findByAccountNumber(String accountNumber, String accountNumberUpdate) {
        return accountMapper.convertToDomain(
                accountRepository.findByAccountNumber(accountNumber, accountNumberUpdate)
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found"))
        );
    }

    @Override
    public Page<Account> findAllActiveItems(Pageable pageable) {
        return accountRepository.findAllActiveItems(pageable)
                .map(accountMapper::convertToDomain);
    }
}
