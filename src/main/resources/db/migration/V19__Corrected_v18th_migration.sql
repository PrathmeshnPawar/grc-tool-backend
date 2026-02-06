-- 1. Change the column type from VARCHAR to UUID
-- We use 'USING updated_by_id::uuid' to tell Postgres how to convert the string to a UUID
ALTER TABLE incident 
ALTER COLUMN updated_by_id TYPE UUID USING updated_by_id::uuid;

-- 2. Now that the types match, add the Foreign Key
ALTER TABLE incident
ADD CONSTRAINT fk_updated_by_id
FOREIGN KEY (updated_by_id)
REFERENCES users(id);