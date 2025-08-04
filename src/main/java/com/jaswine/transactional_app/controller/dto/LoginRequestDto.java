package com.jaswine.transactional_app.controller.dto;

import lombok.Data;

/**
 *  DTO for taking user data for login
 */
@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
