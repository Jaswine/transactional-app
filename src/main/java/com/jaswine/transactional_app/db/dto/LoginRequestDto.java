package com.jaswine.transactional_app.db.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
