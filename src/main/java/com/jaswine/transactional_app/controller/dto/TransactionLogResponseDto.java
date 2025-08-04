package com.jaswine.transactional_app.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for outputting transaction log data
*/
@Data
@AllArgsConstructor
public class TransactionLogResponseDto {
    private int statusCode;
    private String title;
    private String description;
}
