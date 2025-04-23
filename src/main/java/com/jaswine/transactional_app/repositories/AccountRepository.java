package com.jaswine.transactional_app.repositories;

import com.jaswine.transactional_app.db.entity.Account;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a " +
            "WHERE a.isActive = true " +
            "AND a.user.isActive = true " +
            "AND (:username IS NULL OR LOWER(a.user.username) LIKE LOWER(CONCAT('%', :username, '%'))) " +
            "AND (:email IS NULL OR LOWER(a.user.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    List<Account> findActiveAccountsByUserFilter(@Param("username") String username,
                                                 @Param("email") String email);

}
