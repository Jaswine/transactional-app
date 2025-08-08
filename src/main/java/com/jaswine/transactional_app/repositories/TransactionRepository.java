package com.jaswine.transactional_app.repositories;

import com.jaswine.transactional_app.db.entity.Transaction;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value =
            "SELECT t.* " +
                    "FROM transactions t " +
                    "JOIN accounts a ON t.account_id = a.id " +
                    "JOIN users u ON a.user_id = u.id " +
                    "WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :searchingText, '%')) " +
                    "           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchingText, '%')) " +
                    "           OR ( :searchingText ~ '^\\d+(\\.\\d+)?$' " +
                    "               AND a.amount >= CAST(:searchingText AS DOUBLE PRECISION) ) " +
                    "           )" +
                    "  AND (:amountMinValue IS NULL OR t.amount >= CAST(:amountMinValue AS DOUBLE PRECISION)) " +
                    "  AND (:amountMaxValue IS NULL OR t.amount <= CAST(:amountMaxValue AS DOUBLE PRECISION))",
            nativeQuery = true)
    Page<Transaction> findActiveTransactionsByUserFilter(
            Pageable pageable,
            @Param("searchingText") String searchingText,
            @Param("amountMinValue") Float amountMinValue,
            @Param("amountMaxValue") Float amountMaxValue
    );

}
