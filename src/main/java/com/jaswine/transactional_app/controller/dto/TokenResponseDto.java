package com.jaswine.transactional_app.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  DTO for token withdrawal
 */
@Data
@AllArgsConstructor
public class TokenResponseDto {
    private String AccessToken;
    private String RefreshToken;
}
