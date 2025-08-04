package com.jaswine.transactional_app.db.enums;

public enum TransactionStatus {
    PENDING,        // It is processed right now
    COMPLETED,      // The transaction is successfully executed and confirmed
    CANCELED,       // Canceled by the user until the end
    FAILED,         // There was an error during processing
    REVERSED,       // Canceled after completion
    ON_HOLD,        // Temporarily suspended (for example, if you suspect fraud)
    BLOCKED         // Blocked by the system (for example, suspicion of fraud).
}
