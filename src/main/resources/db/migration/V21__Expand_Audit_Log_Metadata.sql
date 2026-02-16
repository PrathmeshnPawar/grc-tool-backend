-- migration/V21__Expand_Audit_Log_Metadata.sql
ALTER TABLE audit_log 
    ADD COLUMN IF NOT EXISTS ip_address VARCHAR(45),
    ADD COLUMN IF NOT EXISTS user_agent TEXT,
    ADD COLUMN IF NOT EXISTS session_id VARCHAR(255);