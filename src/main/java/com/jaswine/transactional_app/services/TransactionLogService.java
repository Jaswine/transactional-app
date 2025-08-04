package com.jaswine.transactional_app.services;

import com.jaswine.transactional_app.db.entity.Transaction;
import com.jaswine.transactional_app.db.entity.TransactionLog;
import com.jaswine.transactional_app.db.enums.TransactionStatus;
import com.jaswine.transactional_app.repositories.TransactionLogRepository;
import com.jaswine.transactional_app.utils.TransactionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionLogService {
    private final TransactionLogRepository transactionLogRepository;

    public TransactionLog createTransactionLog(Transaction transaction,
                                     Integer status, String title, String description) {
        TransactionLog transactionLog = TransactionLog.builder()
                .transaction(transaction)
                .statusCode(status)
                .title(title)
                .description(description)
                .metadata("")
                .build();

        transactionLogRepository.save(transactionLog);
        return transactionLog;
    }
}
