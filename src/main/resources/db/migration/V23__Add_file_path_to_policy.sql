-- V6__add_policy_file_path.sql
-- Bridge the gap between Policy.java and the database
ALTER TABLE policy 
ADD COLUMN file_path VARCHAR(512),
ADD COLUMN is_processed BOOLEAN DEFAULT FALSE;