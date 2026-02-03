-- 1. Add columns (initially nullable for safety)
ALTER TABLE audit_result
ADD COLUMN evidence_path TEXT,
ADD COLUMN evidence_type VARCHAR(50),
ADD COLUMN evidence_checksum VARCHAR(64),
ADD COLUMN evidence_uploaded_at TIMESTAMP;

-- 2. Backfill existing rows with safe defaults
UPDATE audit_result
SET
    evidence_path = 'N/A',
    evidence_uploaded_at = created_at
WHERE evidence_path IS NULL;

-- 3. Enforce NOT NULL constraints to match entity
ALTER TABLE audit_result
ALTER COLUMN evidence_path SET NOT NULL;

ALTER TABLE audit_result
ALTER COLUMN evidence_uploaded_at SET NOT NULL;

-- 4. (Optional but recommended) Add index for evidence lookups
CREATE INDEX idx_audit_result_evidence_path
ON audit_result(evidence_path);
