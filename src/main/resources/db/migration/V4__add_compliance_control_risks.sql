CREATE TABLE compliance_control_risks (
    compliance_control_id UUID NOT NULL,
    risk_id UUID NOT NULL,
    PRIMARY KEY (compliance_control_id, risk_id),
    CONSTRAINT fk_ccr_control
        FOREIGN KEY (compliance_control_id)
        REFERENCES compliance_control(id),
    CONSTRAINT fk_ccr_risk
        FOREIGN KEY (risk_id)
        REFERENCES risk(id)
);
CREATE INDEX idx_ccr_control ON compliance_control_risks(compliance_control_id);
CREATE INDEX idx_ccr_risk ON compliance_control_risks(risk_id);