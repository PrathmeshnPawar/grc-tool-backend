-- Enable UUID support
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===================== USERS =====================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100),
    email VARCHAR(150),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ===================== COMPLIANCE FRAMEWORK =====================
CREATE TABLE compliance_framework (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(200) NOT NULL,
    version VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ===================== COMPLIANCE CONTROL =====================
CREATE TABLE compliance_control (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(200),
    control_code VARCHAR(50),
    description TEXT,
    status VARCHAR(50),
    framework_id UUID REFERENCES compliance_framework(id),
    vendor_id UUID,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ===================== POLICY =====================
CREATE TABLE policy (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200),
    version VARCHAR(50),
    description TEXT,
    content TEXT,
    status VARCHAR(50),
    owner_id UUID REFERENCES users(id),
    framework_id UUID REFERENCES compliance_framework(id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ===================== INCIDENT =====================
CREATE TABLE incident (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200),
    description TEXT,
    severity VARCHAR(50),
    status VARCHAR(50),
    date_reported DATE,
    reported_by UUID REFERENCES users(id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ===================== RISK =====================
CREATE TABLE risk (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200),
    description TEXT,
    category VARCHAR(50),
    impact INT,
    likelihood INT,
    risk_score INT,
    status VARCHAR(50),
    owner_id UUID REFERENCES users(id),
    incident_id UUID REFERENCES incident(id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ===================== AUDIT =====================
CREATE TABLE audit (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(200),
    start_date DATE,
    end_date DATE,
    lead_auditor UUID REFERENCES users(id),
    risk_id UUID REFERENCES risk(id),
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ===================== JOIN TABLES =====================
CREATE TABLE audit_tested_controls (
    audit_id UUID REFERENCES audit(id),
    control_id UUID REFERENCES compliance_control(id),
    PRIMARY KEY (audit_id, control_id)
);

CREATE TABLE audit_reviewed_policies (
    audit_id UUID REFERENCES audit(id),
    policy_id UUID REFERENCES policy(id),
    PRIMARY KEY (audit_id, policy_id)
);

CREATE TABLE policy_controls (
    policy_id UUID REFERENCES policy(id),
    control_id UUID REFERENCES compliance_control(id),
    PRIMARY KEY (policy_id, control_id)
);
