-- migration/V6__create_vendor_table.sql

CREATE TABLE vendor (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255),
    contact_email VARCHAR(255),
    status VARCHAR(50),
    tier VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Add the missing foreign key constraint in compliance_control
-- which was previously unable to reference a non-existent table
ALTER TABLE compliance_control
ADD CONSTRAINT fk_compliance_control_vendor
FOREIGN KEY (vendor_id) REFERENCES vendor(id);