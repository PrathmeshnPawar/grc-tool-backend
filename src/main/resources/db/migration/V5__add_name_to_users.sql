-- migration/V5__update_users_table.sql

ALTER TABLE users 
    ADD COLUMN name VARCHAR(255),
    ADD COLUMN password VARCHAR(255),
    ADD COLUMN role VARCHAR(50);