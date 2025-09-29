package com.pichincha.account.infrastructure.input.adapter.rest.impl;

import com.pichincha.account.application.input.port.AccountService;
import com.pichincha.account.domain.Account;
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
@RequestMapping("/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    @ResponseStatus( HttpStatus.CREATED )
    public Account create(@Validated(ValidationGroups.Create.class) @RequestBody Account account) {
        return accountService.create(account);
    }

    @GetMapping
    public Page<Account> findAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                    @RequestParam(defaultValue = "10") @Min(0) @Max(100) int size) {
        return accountService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{accountId}")
    public Account findById(@PathVariable("accountId") String id) {
        return accountService.findById(id);
    }

    @PutMapping("/{accountId}")
    public Account update(
            @PathVariable("accountId") String id,
            @Validated(ValidationGroups.Update.class)
            @RequestBody Account account) {
        account.setAccountId(id);
        account.setStatus(Boolean.TRUE);
        return accountService.update(account);
    }

    @DeleteMapping("/{accountId}")
    public void delete(@PathVariable("accountId") String id) {
        accountService.delete(id);
    }
}
