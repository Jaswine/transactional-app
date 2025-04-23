package com.jaswine.transactional_app.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponseDto {
    private String AccessToken;
    private String RefreshToken;
}
