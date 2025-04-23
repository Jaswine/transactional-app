package com.jaswine.transactional_app.db.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Auditable {

    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt = LocalDateTime.now();
}
