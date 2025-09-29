package com.pichincha.account.infrastructure.input.adapter.rest.impl;

import com.pichincha.account.application.input.port.TransactionService;
import com.pichincha.account.domain.Transaction;
import com.pichincha.account.domain.common.ValidationGroups;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public Transaction create(@Validated(ValidationGroups.Create.class) @RequestBody Transaction transaction) {
        return transactionService.create(transaction);
    }

    @GetMapping
    public Page<Transaction> findAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                        @RequestParam(defaultValue = "10") @Min(0) @Max(100) int size) {
        return transactionService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{transactionId}")
    public Transaction findById(@PathVariable("transactionId") String id) {
        return transactionService.findById(id);
    }

    @DeleteMapping("/{transactionId}")
    public void delete(@PathVariable("transactionId") String id) {
        transactionService.delete(id);
    }
}