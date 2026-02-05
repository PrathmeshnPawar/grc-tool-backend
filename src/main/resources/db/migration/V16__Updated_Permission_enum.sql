-- 1. Remove anything NOT in the Enum
DELETE FROM permissions WHERE name NOT IN ('USER_WRITE', 'RISK_CREATE', 'AUDIT_EXECUTE', 'SYSTEM_ADMIN', 'USER_READ');

-- 2. Insert or Update the exact Enum names
INSERT INTO permissions (id, name) VALUES 
(gen_random_uuid(), 'USER_WRITE'),
(gen_random_uuid(), 'RISK_CREATE'),
(gen_random_uuid(), 'AUDIT_EXECUTE'),
(gen_random_uuid(), 'SYSTEM_ADMIN'),
(gen_random_uuid(), 'USER_READ')
ON CONFLICT (name) DO NOTHING;