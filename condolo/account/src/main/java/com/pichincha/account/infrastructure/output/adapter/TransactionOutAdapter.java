package com.pichincha.account.infrastructure.output.adapter;

import com.pichincha.account.application.exception.ResourceNotFoundException;
import com.pichincha.account.application.output.port.TransactionOutPort;
import com.pichincha.account.domain.Transaction;
import com.pichincha.account.infrastructure.output.repository.TransactionRepository;
import com.pichincha.account.infrastructure.output.repository.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionOutAdapter implements TransactionOutPort {

    private final TransactionMapper transactionMapper;

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return transactionMapper.convertToDomain(
                transactionRepository.save(
                        transactionMapper.convertToEntity(transaction)
                )
        );
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        Page<com.pichincha.account.infrastructure.output.repository.entity.Transaction> page =
                transactionRepository.findAll(pageable);

        List<Transaction> content =
                page.getContent().stream()
                        .map(transactionMapper::convertToDomain)
                        .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @Override
    public Transaction findById(String id) {
        return transactionMapper.convertToDomain(
                transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"))
        );
    }

    @Override
    public void delete(String id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Page<Transaction> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate, String customerId, Pageable pageable) {
        Page<com.pichincha.account.infrastructure.output.repository.entity.Transaction> page =
                transactionRepository.getByQueryDate(startDate, endDate, customerId, pageable);

        List<Transaction> content =
                page.getContent().stream()
                        .map(transactionMapper::convertToDomain)
                        .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, page.getTotalElements());
    }
}
