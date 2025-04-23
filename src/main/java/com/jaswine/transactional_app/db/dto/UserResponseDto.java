package com.jaswine.transactional_app.db.dto;

import com.jaswine.transactional_app.db.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private String username;
    private String email;
    private UserType type;
}
