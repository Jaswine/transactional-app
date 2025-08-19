package com.jaswine.transactional_app.db.entity;

import com.jaswine.transactional_app.db.entity.base.Auditable;
import com.jaswine.transactional_app.db.enums.TransactionStatus;
import com.jaswine.transactional_app.db.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "another_account_id", nullable = false)
    private Account anotherAccount;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "signature", nullable = false)
    private String signature;

    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Column(name = "external_source")
    private String externalSource;

    @Column(name = "external_reference")
    private String externalReference;

    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "comment")
    private String comment;

    @Column(name = "is_active")
    private Boolean isActive;
}
