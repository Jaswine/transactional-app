package com.jaswine.transactional_app.repositories;

import com.jaswine.transactional_app.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Поиск пользователя по email
     * @param email - email
     * @return {@link User} user
     */
    Optional<User> findByEmail(String email);

    /**
     * Поиск пользователя по username
     * @param username - имя пользователя
     * @return {@link User} user
     */
    Optional<User> findByUsername(String username);
}
