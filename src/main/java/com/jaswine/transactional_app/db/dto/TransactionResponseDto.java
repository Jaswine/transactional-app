package com.jaswine.transactional_app.db.dto;

import com.jaswine.transactional_app.db.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for outputting transaction data
 */
@Data
@AllArgsConstructor
public class TransactionResponseDto {
    private Float amount;
    private String signature;
    private TransactionStatus status;
    private String comment;

}
