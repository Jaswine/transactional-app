CREATE TABLE IF NOT EXISTS transactions (
    id                  BIGSERIAL PRIMARY KEY,
    account_id          BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    another_account_id  BIGINT REFERENCES accounts(id) DEFAULT NULL,
    amount              DECIMAL(15,2) NOT NULL DEFAULT 0.0,
    signature           VARCHAR(255) NOT NULL UNIQUE,
    status              VARCHAR(15) NOT NULL,
    type                VARCHAR(15) NOT NULL,
    external_source     VARCHAR(100) DEFAULT NULL,
    external_reference  TEXT DEFAULT NULL,
    comment             VARCHAR(255) DEFAULT NULL,
    is_active           BOOLEAN NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE transactions IS 'Transaction table';

