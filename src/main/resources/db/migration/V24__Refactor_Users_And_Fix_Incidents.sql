-- 1. CLEANUP: Remove invalid mapping in Incident table 
-- ChatGPT catch: We must remove the 'updated_at' column if it was incorrectly mapped as a ManyToOne.
-- BaseEntity (inherited) will provide the proper timestamp columns.
ALTER TABLE IF EXISTS incidents 
DROP COLUMN IF EXISTS updated_at_id; -- Dropping the FK if it was created 

-- 2. REFACTOR: Update Users table for SSO and RBAC
-- Adding sso_id to link with third-party providers
ALTER TABLE users ADD COLUMN IF NOT EXISTS sso_id VARCHAR(255) UNIQUE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS picture VARCHAR(500);
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(50) NOT NULL DEFAULT 'EMPLOYEE'; -- Enum as STRING

-- 3. ENSURE: Standardize BaseEntity Timestamps [cite: 33]
-- We ensure all entities have the BaseEntity columns if they weren't there.
-- PostgreSQL 'timestamp with time zone' is best for global compliance.
DO $$ 
BEGIN 
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='users' AND column_name='created_at') THEN
        ALTER TABLE users ADD COLUMN created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
        ALTER TABLE users ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
    END IF;
END $$;

-- 4. PERMISSIONS: Explicit Join Entity setup
CREATE TABLE IF NOT EXISTS permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_permissions (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, permission_id) -- Explicit Join Table [cite: 41, 47]
);

-- 5. POLICY: Add OCR/Document Storage Fields [cite: 49, 51]
-- We store the path to the file, not the binary blob.
ALTER TABLE policy ADD COLUMN IF NOT EXISTS is_processed BOOLEAN DEFAULT FALSE;
ALTER TABLE policy ADD COLUMN IF NOT EXISTS file_path VARCHAR(1024);
ALTER TABLE policy ADD COLUMN IF NOT EXISTS file_hash VARCHAR(64); -- SHA-256 for audit integrity [cite: 50]