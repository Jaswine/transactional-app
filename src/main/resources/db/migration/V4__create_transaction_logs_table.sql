
CREATE TABLE IF NOT EXISTS transaction_logs (
    id              BIGSERIAL PRIMARY KEY,
    transaction_id  BIGINT NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    status_code     INTEGER NOT NULL,
    title           VARCHAR(255) NOT NULL,
    description     TEXT DEFAULT NULL,
    metadata        TEXT DEFAULT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE transaction_logs IS 'Transaction log table';
