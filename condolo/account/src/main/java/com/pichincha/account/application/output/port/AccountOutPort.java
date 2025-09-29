package com.pichincha.account.application.output.port;

import com.pichincha.account.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountOutPort {

    Account save(Account account);

    Page<Account> findAll(Pageable pageable);

    Account findById(String id);

    void delete(String id);

    Account findByAccountNumber(String accountNumber, String accountNumberUpdate);

    Page<Account> findAllActiveItems(Pageable pageable);
}
