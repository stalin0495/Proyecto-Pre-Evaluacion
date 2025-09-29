package com.pichincha.account.application.output.port;

import com.pichincha.account.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TransactionOutPort {

    Transaction save(Transaction transaction);

    Page<Transaction> findAll(Pageable pageable);

    Transaction findById(String id);

    void delete(String id);

    Page<Transaction> findByDateBetween(
            LocalDateTime startDate, LocalDateTime endDate, String customerId, Pageable pageable);
}