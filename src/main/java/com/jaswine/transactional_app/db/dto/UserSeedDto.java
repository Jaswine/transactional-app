package com.jaswine.transactional_app.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSeedDto {
    private String username;
    private String email;
    private String password;
    private String userType;
    private Boolean active;
}
