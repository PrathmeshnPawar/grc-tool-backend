
ALTER TABLE permissions
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
-- 1. Insert Core GRC Permissions
-- We use ON CONFLICT to avoid duplicate key errors if the script is re-run
INSERT INTO permissions (id, name, created_at, updated_at) VALUES 
(gen_random_uuid(), 'USER_WRITE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'RISK_WRITE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'AUDIT_EXECUTE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'SYSTEM_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- 2. Link Permissions to Admin User (John Doe)
-- User ID: 7881f342-e71a-467e-841d-6451c7d0f141
INSERT INTO user_permissions (user_id, permission_id)
SELECT '7881f342-e71a-467e-841d-6451c7d0f141', id 
FROM permissions 
WHERE name IN ('USER_WRITE', 'RISK_WRITE', 'AUDIT_EXECUTE', 'SYSTEM_ADMIN')
ON CONFLICT DO NOTHING;