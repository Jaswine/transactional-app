package com.jaswine.transactional_app.db.entity;

import com.jaswine.transactional_app.db.entity.base.Auditable;
import com.jaswine.transactional_app.db.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * Transaction model
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "another_account_id", nullable = false)
    private Account anotherAccount;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "signature")
    private String signature;

    @Column(name = "status")
    private TransactionStatus status;

    @Column(name = "comment")
    private String comment;

    @Column(name = "is_active")
    private Boolean isActive;

}
