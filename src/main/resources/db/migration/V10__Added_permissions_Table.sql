-- Create the master table for capabilities
CREATE TABLE permissions (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
    created_at TIMESTAMP,
);

-- Create the physical Join Table
CREATE TABLE user_permissions (
    user_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    PRIMARY KEY (user_id, permission_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE
    created_at TIMESTAMP,
);