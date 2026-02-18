-- 1. Ensure the status column exists in the audit_log table first
ALTER TABLE audit_log ADD COLUMN IF NOT EXISTS status VARCHAR(50);

-- 2. Update the Compliance Framework table with professional metadata
ALTER TABLE compliance_framework 
ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'DRAFT',
ADD COLUMN IF NOT EXISTS category VARCHAR(100),
ADD COLUMN IF NOT EXISTS regulatory_body VARCHAR(100),
ADD COLUMN IF NOT EXISTS reference_link VARCHAR(512),
ADD COLUMN IF NOT EXISTS owner_id UUID;

-- 3. Add the Foreign Key relationship
ALTER TABLE compliance_framework
ADD CONSTRAINT fk_framework_owner 
FOREIGN KEY (owner_id) REFERENCES users(id)
ON DELETE SET NULL;

-- 4. Fix the INSERT: Provide explicit timestamps for NOT NULL columns
INSERT INTO audit_log (
    id, 
    entity_name, 
    entity_id, 
    action, 
    change_details, 
    ip_address, 
    user_agent, 
    status, 
    created_at, 
    updated_at
)
VALUES (
    gen_random_uuid(),
    'System',
    '00000000-0000-0000-0000-000000000000',
    'DATABASE_MIGRATION',
    'Applied V26: Added professional metadata to Compliance Frameworks',
    '127.0.0.1',
    'Flyway-Migration-Engine',
    'SUCCESS',
    CURRENT_TIMESTAMP, -- Required to satisfy NOT NULL constraint
    CURRENT_TIMESTAMP
);