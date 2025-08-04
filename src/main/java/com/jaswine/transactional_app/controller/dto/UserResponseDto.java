package com.jaswine.transactional_app.controller.dto;

import com.jaswine.transactional_app.db.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for user data output
*/
@Data
@AllArgsConstructor
public class UserResponseDto {
    private String username;
    private String email;
    private UserType type;
}
