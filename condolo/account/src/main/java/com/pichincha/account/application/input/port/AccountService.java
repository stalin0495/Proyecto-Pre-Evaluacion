package com.pichincha.account.application.input.port;

import com.pichincha.account.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    Account create(Account account);

    Account update(Account account);

    Page<Account> findAll(Pageable pageable);

    Account findById(String id);

    void delete(String id);
}
