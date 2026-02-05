-- 1. Insert Core GRC Permissions
-- We use ON CONFLICT to avoid duplicate key errors if the script is re-run
-- 1. Insert Core GRC Permissions only if the name doesn't exist
INSERT INTO permissions (id, name, created_at, updated_at)
SELECT gen_random_uuid(), p_name, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM (VALUES 
    ('USER_WRITE'), 
    ('RISK_WRITE'), 
    ('AUDIT_EXECUTE'), 
    ('SYSTEM_ADMIN')
) AS t(p_name)
WHERE NOT EXISTS (
    SELECT 1 FROM permissions WHERE name = t.p_name
);

-- 2. Link Permissions to John Doe only if the link doesn't already exist
INSERT INTO user_permissions (user_id, permission_id)
SELECT '7881f342-e71a-467e-841d-6451c7d0f141', p.id 
FROM permissions p
WHERE p.name IN ('USER_WRITE', 'RISK_WRITE', 'AUDIT_EXECUTE', 'SYSTEM_ADMIN')
AND NOT EXISTS (
    SELECT 1 FROM user_permissions up 
    WHERE up.user_id = '7881f342-e71a-467e-841d-6451c7d0f141' 
    AND up.permission_id = p.id
);