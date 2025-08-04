package com.jaswine.transactional_app.services;

import com.jaswine.transactional_app.db.entity.Account;
import com.jaswine.transactional_app.db.entity.Transaction;
import com.jaswine.transactional_app.db.entity.TransactionLog;
import com.jaswine.transactional_app.db.enums.TransactionStatus;
import com.jaswine.transactional_app.repositories.TransactionRepository;
import com.jaswine.transactional_app.utils.TransactionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final TransactionLogService transactionLogService;

    public TransactionLog createTransaction(long accountFromId, long accountToId,
                                            Float amount, String comment) {
        TransactionLog transactionLog;
        Optional<Account> accountFrom = accountService.findById(accountFromId);
        Optional<Account> accountTo = accountService.findById(accountToId);

        if (accountFrom.isPresent() && accountTo.isPresent()) {
            Transaction transaction = Transaction.builder()
                    .account(accountFrom.get())
                    .anotherAccount(accountTo.get())
                    .amount(amount)
                    .signature(TransactionUtils.generateTransactionSignature())
                    .comment(comment)
                    .status(TransactionStatus.PENDING)
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
        return null;
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

}
