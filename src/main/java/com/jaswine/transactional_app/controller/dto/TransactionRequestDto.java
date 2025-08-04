package com.jaswine.transactional_app.controller.dto;

import lombok.Data;

/**
 * DTO for taking transaction data for its creation
*/
@Data
public class TransactionRequestDto {
    private Float amount;
    private String comment;
}
