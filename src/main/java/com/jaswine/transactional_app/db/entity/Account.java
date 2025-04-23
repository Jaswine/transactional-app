package com.jaswine.transactional_app.db.entity;

import com.jaswine.transactional_app.db.entity.base.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Account model
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "address")
    private String address;

    @Column(name = "amount")
    private Float amount;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_active")
    private Boolean isActive;
}
