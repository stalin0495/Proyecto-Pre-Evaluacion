package com.pichincha.account.infrastructure.input.adapter.rest.impl;

import com.pichincha.account.application.input.port.TransactionService;
import com.pichincha.account.domain.Report;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/customers")
public class ReportController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{customerId}/transactions/report")
    public Report findByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @PathVariable("customerId") String customerId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(0) @Max(100) int size) {
        return transactionService.findByDateBetween(startDate, endDate, customerId, PageRequest.of(page, size));
    }
}