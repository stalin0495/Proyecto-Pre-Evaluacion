package com.pichincha.account.application.input.port;

import com.pichincha.account.domain.Report;
import com.pichincha.account.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TransactionService {

    Transaction create(Transaction transaction);

    Transaction update(Transaction transaction);

    Page<Transaction> findAll(Pageable pageable);

    Transaction findById(String id);

    void delete(String id);

    Report findByDateBetween(
            LocalDateTime startDate, LocalDateTime endDate, String customerId, Pageable pageable);
}
