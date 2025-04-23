package com.jaswine.transactional_app.db.entity;

import com.jaswine.transactional_app.db.entity.base.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Transaction log model
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction_logs")
public class TransactionLog extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne()
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "metadata")
    private String metadata;

}
