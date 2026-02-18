CREATE TABLE incident_risks (
    incident_id UUID NOT NULL REFERENCES incident(id) ON DELETE CASCADE,
    risk_id UUID NOT NULL REFERENCES risk(id) ON DELETE CASCADE,
    PRIMARY KEY (incident_id, risk_id)
);