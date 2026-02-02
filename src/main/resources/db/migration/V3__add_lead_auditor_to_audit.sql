ALTER TABLE audit
ADD COLUMN lead_auditor_id UUID;

ALTER TABLE audit
ADD CONSTRAINT fk_audit_lead_auditor
FOREIGN KEY (lead_auditor_id)
REFERENCES users(id);
