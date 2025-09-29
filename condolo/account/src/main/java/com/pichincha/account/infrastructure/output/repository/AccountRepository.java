package com.pichincha.account.infrastructure.output.repository;

import com.pichincha.account.infrastructure.output.repository.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("SELECT a FROM Account a " +
            "WHERE (:accountNumberUpdate IS NULL OR a.accountNumber <> :accountNumberUpdate) " +
            "AND a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumber(String accountNumber, String accountNumberUpdate);

    @Query("SELECT a FROM Account a WHERE a.status = true")
    Page<Account> findAllActiveItems(Pageable pageable);
}
