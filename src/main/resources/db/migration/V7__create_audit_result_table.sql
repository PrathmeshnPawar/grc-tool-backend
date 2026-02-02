CREATE TABLE audit_result (
    id UUID PRIMARY KEY,

    audit_id UUID,
    control_id UUID,

    status VARCHAR(30) NOT NULL,
    findings TEXT,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT fk_audit_result_audit
        FOREIGN KEY (audit_id)
        REFERENCES audit(id),

    CONSTRAINT fk_audit_result_control
        FOREIGN KEY (control_id)
        REFERENCES compliance_control(id)
);
CREATE INDEX idx_audit_result_audit_id ON audit_result(audit_id);
CREATE INDEX idx_audit_result_control_id ON audit_result(control_id);