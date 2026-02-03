CREATE TABLE audit_log (
    id UUID PRIMARY KEY,

    entity_name VARCHAR(100) NOT NULL,
    entity_id UUID NOT NULL,

    action VARCHAR(50) NOT NULL,
    change_details TEXT,

    performed_by UUID,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT fk_audit_log_user
        FOREIGN KEY (performed_by)
        REFERENCES users(id)
);

-- Indexes for audit queries (VERY important for GRC)
CREATE INDEX idx_audit_log_entity
    ON audit_log(entity_name, entity_id);

CREATE INDEX idx_audit_log_performed_by
    ON audit_log(performed_by);

CREATE INDEX idx_audit_log_created_at
    ON audit_log(created_at);
