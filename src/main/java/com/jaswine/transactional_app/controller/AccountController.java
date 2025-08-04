package com.jaswine.transactional_app.controller;

import com.jaswine.transactional_app.db.entity.Account;
import com.jaswine.transactional_app.repositories.AccountRepository;
import com.jaswine.transactional_app.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Account Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public Page<Account> getAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String searchingText,
            @RequestParam(defaultValue = "") Float amountMinValue,
            @RequestParam(defaultValue = "") Float amountMaxValue
    ) {
        return accountService.findAllAccounts(
                page, limit,
                searchingText,
                amountMinValue, amountMaxValue
        );
    }

}
