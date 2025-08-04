package com.jaswine.transactional_app.controller.dto;

import lombok.Data;

/**
 *  DTO for taking user data for registration
 */
@Data
public class RegisterRequestDto {
    private String username;
    private String email;
    private String password;
}
