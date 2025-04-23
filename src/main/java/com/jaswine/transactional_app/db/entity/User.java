package com.jaswine.transactional_app.db.entity;

import com.jaswine.transactional_app.db.entity.base.Auditable;
import com.jaswine.transactional_app.db.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

/**
 * User model
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "type")
    private UserType type;

    @Column(name = "is_active")
    private Boolean isActive;

}

