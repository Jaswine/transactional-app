package com.jaswine.transactional_app.controller;

import com.jaswine.transactional_app.controller.dto.TransactionRequestDto;
import com.jaswine.transactional_app.controller.dto.TransactionLogResponseDto;
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

    @PostMapping("/{accountToId}")
    public ResponseEntity<TransactionLogResponseDto> register(@PathVariable long accountToId,
                                           @RequestBody TransactionRequestDto request) {
        Optional<Account> currentAccount = accountService.findByUserEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());
        TransactionLog transactionLog = transactionService.createTransaction(currentAccount.get().getId(),
                accountToId, request.getAmount(), request.getComment());
        return ResponseEntity.ok(new TransactionLogResponseDto(
                transactionLog.getStatusCode(),
                transactionLog.getTitle(),
                transactionLog.getDescription()
        ));
    }
}
