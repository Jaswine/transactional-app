package com.jaswine.transactional_app.repositories;

import com.jaswine.transactional_app.db.entity.Account;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value =
            "SELECT a.* " +
                    "FROM accounts a " +
                    "JOIN users u ON a.user_id = u.id " +
                    "WHERE ( " +
                    "      LOWER(u.username) LIKE LOWER(CONCAT('%', :searchingText, '%')) " +
                    "    OR LOWER(u.email)    LIKE LOWER(CONCAT('%', :searchingText, '%')) " +
                    "    OR ( :searchingText ~ '^\\d+(\\.\\d+)?$' " +
                    "         AND a.amount >= CAST(:searchingText AS DOUBLE PRECISION) ) " +
                    "    )" +
                    "  AND (:amountMinValue IS NULL OR a.amount >= CAST(:amountMinValue AS DOUBLE PRECISION)) " +
                    "  AND (:amountMaxValue IS NULL OR a.amount <= CAST(:amountMaxValue AS DOUBLE PRECISION))",
            nativeQuery = true)
    Page<Account> findActiveAccountsByUserFilter(
            Pageable pageable,
            @Param("searchingText") String searchingText,
            @Param("amountMinValue") Float amountMinValue,
            @Param("amountMaxValue") Float amountMaxValue
    );

    Optional<Account> findById(long id);

    @Query(value = "SELECT a.* " +
            "FROM accounts a " +
            "JOIN users u ON a.user_id = u.id " +
            "WHERE u.email = :email", nativeQuery = true)
    Optional<Account>findByUserEmail(
           @Param("email") String email
    );
}
