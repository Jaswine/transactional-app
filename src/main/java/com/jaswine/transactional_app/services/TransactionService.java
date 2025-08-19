package com.jaswine.transactional_app.services;

import com.jaswine.transactional_app.db.entity.Account;
import com.jaswine.transactional_app.db.entity.Transaction;
import com.jaswine.transactional_app.db.entity.TransactionLog;
import com.jaswine.transactional_app.db.enums.TransactionStatus;
import com.jaswine.transactional_app.db.enums.TransactionType;
import com.jaswine.transactional_app.repositories.TransactionRepository;
import com.jaswine.transactional_app.utils.TransactionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final TransactionLogService transactionLogService;

    /**
     * Find active transactions with filters by searching text and max/min values
     *
     * @param page - Page
     * @param limit - Elements limit
     * @param searchingText - Text to search
     * @param amountMinValue - Minimal amount value
     * @param amountMaxValue - Maxium amount value
     * @return
     */
    public Page<Transaction> findActiveTransactionsByUserFilter(int page, int limit,
                                                 String searchingText, Float amountMinValue, Float amountMaxValue) {
        return transactionRepository.findActiveTransactionsByUserFilter(PageRequest.of(page, limit),
                searchingText, amountMinValue, amountMaxValue);
    }

    /**
     * Create a new transaction
     *
     * @param accountFromId - Account id who's sending money
     * @param accountToEmail - Account email to send money
     * @param amount - Amount
     * @param comment - User's comment
     * @return Transaction log with status, title and message
     */
    public TransactionLog createTransaction(long accountFromId, String accountToEmail,
                                            Float amount, String comment) {
        TransactionLog transactionLog;
        Optional<Account> accountFrom = accountService.findById(accountFromId);
        Optional<Account> accountTo = accountService.findByUserEmail(accountToEmail);

        if (amount > 0) {
            if (accountFrom.isPresent() && accountTo.isPresent()) {
                System.out.println("Transaction creation...");
                Transaction transaction = Transaction.builder()
                        .account(accountTo.get())
                        .anotherAccount(accountFrom.get())
                        .amount(amount)
                        .signature(TransactionUtils.generateTransactionSignature())
                        .comment(comment)
                        .status(TransactionStatus.PENDING)
                        .type(TransactionType.TRANSFER)
                        .externalSource("")
                        .externalReference("")
                        .isActive(true)
                        .build();

                try {
                    save(transaction);

                    if (!accountFrom.get().getIsActive() || !accountFrom.get().getIsActive()) {
                        transaction.setStatus(TransactionStatus.FAILED);
                        save(transaction);
                        transactionLog = transactionLogService.createTransactionLog(transaction,
                                400, "User not found",
                                "Transaction failed, user not found");
                    } else if (accountFrom.get().getAmount() < amount) {
                        transaction.setStatus(TransactionStatus.FAILED);
                        save(transaction);
                        transactionLog = transactionLogService.createTransactionLog(transaction,
                                400, "Transaction failed",
                                "Transaction failed, insufficient funds on balance");
                    } else {
                        accountFrom.get().setAmount(accountFrom.get().getAmount() - amount);
                        accountTo.get().setAmount(accountTo.get().getAmount() + amount);

                        accountService.save(accountFrom.get());
                        accountService.save(accountTo.get());

                        transaction.setStatus(TransactionStatus.COMPLETED);
                        save(transaction);
                        transactionLog = transactionLogService.createTransactionLog(transaction,
                                201, "Transaction was successful", "Transaction was successful");
                    }

                    return transactionLog;
                } catch (Exception e) {
                    transaction.setStatus(TransactionStatus.FAILED);
                    save(transaction);

                    throw e;
                }
            }
        }
        return null;
    }

    /**
     * Create a new replenishment
     *
     * @param accountEmail - Email
     * @param amount - Amount
     * @param externalSource - External source
     * @param externalReference - External reference
     * @return Translation log with status, title and message
     */
    public TransactionLog createReplenishment(String accountEmail, Float amount,
                                              String externalSource, String externalReference) {
        TransactionLog transactionLog;
        Optional<Account> account = accountService.findByUserEmail(accountEmail);

        if (amount > 0) {
            if (account.isPresent() && !externalSource.isEmpty() && !externalReference.isEmpty()) {
                Transaction transaction = Transaction.builder()
                        .account(account.get())
                        .amount(amount)
                        .signature(TransactionUtils.generateTransactionSignature())
                        .status(TransactionStatus.PENDING)
                        .type(TransactionType.REPLENISHMENT)
                        .externalSource(externalSource)
                        .externalReference(externalReference)
                        .isActive(true)
                        .build();

                try {
                    save(transaction);

                    if (!account.get().getIsActive()) {
                        transaction.setStatus(TransactionStatus.FAILED);
                        save(transaction);
                        transactionLog = transactionLogService.createTransactionLog(transaction,
                                400, "User not found",
                                "Transaction failed, user not found");
                    } else if (externalReference.length() < 7 || externalSource.length() < 2) {
                        transaction.setStatus(TransactionStatus.FAILED);
                        save(transaction);
                        transactionLog = transactionLogService.createTransactionLog(transaction,
                                400, "The external source is doubtful",
                                "Transaction failed, user not found");
                    } else {
                        account.get().setAmount(account.get().getAmount() + amount);

                        accountService.save(account.get());

                        transaction.setStatus(TransactionStatus.COMPLETED);

                        save(transaction);

                        transactionLog = transactionLogService.createTransactionLog(transaction,
                                201, "Transaction was successful", "Transaction was successful");
                    }

                    return transactionLog;
                } catch (Exception e) {
                    transaction.setStatus(TransactionStatus.FAILED);
                    save(transaction);

                    throw e;
                }
            }
        }
        return null;
    }

    /**
     * Save transaction
     *
     * @param transaction - Transaction
     */
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

}
