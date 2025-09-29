package com.pichincha.account.infrastructure.output.repository;


import com.pichincha.account.infrastructure.output.repository.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    @Query("SELECT t FROM transactions t " +
            "WHERE t.date BETWEEN :startDate AND :endDate " +
            "AND t.account.customerId = :customerId" )
    Page<Transaction> getByQueryDate(
            LocalDateTime startDate, LocalDateTime endDate, String customerId, Pageable pageable);
}