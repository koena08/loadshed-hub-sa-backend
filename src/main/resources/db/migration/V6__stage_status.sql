-- Single-row table holding the current national load-shedding stage, admin-editable.
CREATE TABLE stage_status (
    id BIGINT PRIMARY KEY,
    stage INT NOT NULL DEFAULT 0,
    note VARCHAR(255),
    updated_at TIMESTAMP NOT NULL,
    updated_by VARCHAR(255)
);

INSERT INTO stage_status (id, stage, note, updated_at, updated_by)
VALUES (1, 0, 'Initial value - update from the admin console', CURRENT_TIMESTAMP, 'system');
