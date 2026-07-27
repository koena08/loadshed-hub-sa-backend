-- User account management: enable/disable, brute-force lockout, created timestamp
ALTER TABLE users
    ADD COLUMN enabled BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN failed_login_attempts INT NOT NULL DEFAULT 0,
    ADD COLUMN locked_until TIMESTAMP NULL,
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Link each hub to the business owner (or admin) who registered it
ALTER TABLE locations
    ADD COLUMN owner_id BIGINT NULL,
    ADD CONSTRAINT fk_locations_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL;

-- Single-use, time-limited tokens for the forgot-password flow
CREATE TABLE password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(128) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_password_reset_tokens_user ON password_reset_tokens(user_id);
