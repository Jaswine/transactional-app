package com.jaswine.transactional_app.controller;

import com.jaswine.transactional_app.controller.dto.ReplenishmentRequestDto;
import com.jaswine.transactional_app.controller.dto.TransactionRequestDto;
import com.jaswine.transactional_app.controller.dto.TransactionLogResponseDto;
import com.jaswine.transactional_app.controller.dto.TransactionResponseDto;
import com.jaswine.transactional_app.db.entity.Account;
import com.jaswine.transactional_app.db.entity.TransactionLog;
import com.jaswine.transactional_app.services.AccountService;
import com.jaswine.transactional_app.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Контроллер транзакций
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;

    @PostMapping("/transaction/{accountToEmail}")
    public ResponseEntity<TransactionLogResponseDto> sendTransaction(@PathVariable String accountToEmail,
                                           @RequestBody TransactionRequestDto request) {
        Optional<Account> currentAccount = accountService.findByUserEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());
        TransactionLog transactionLog = transactionService.createTransaction(currentAccount.get().getId(),
                accountToEmail, request.getAmount(), request.getComment());
        return ResponseEntity.ok(new TransactionLogResponseDto(
                transactionLog.getStatusCode(),
                transactionLog.getTitle(),
                transactionLog.getDescription()
        ));
    }

    @PostMapping("/replenishment/{accountToEmail}")
    public ResponseEntity<TransactionLogResponseDto> sendReplenishment(@PathVariable String accountToEmail,
                                                                       @RequestBody ReplenishmentRequestDto request) {
        TransactionLog transactionLog = transactionService.createReplenishment(accountToEmail,
                request.getAmount(), request.getExternalSource(), request.getExternalReference());
        return ResponseEntity.ok(new TransactionLogResponseDto(
                transactionLog.getStatusCode(),
                transactionLog.getTitle(),
                transactionLog.getDescription()
        ));
    }

    @GetMapping("/")
    public ResponseEntity<TransactionResponseDto> getAllTransactions() {
        Optional<Account> currentAccount = accountService.findByUserEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());

        return ResponseEntity.ok(new TransactionResponseDto(

        ));
    }
}
