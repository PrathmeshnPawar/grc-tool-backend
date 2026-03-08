-- Migration to add file path and processing status for BSE/NSE OCR
ALTER TABLE policy 
ADD COLUMN file_path VARCHAR(255),
ADD COLUMN is_processed BOOLEAN DEFAULT FALSE;

-- Senior Tip: Add a comment to the column for future wizards
COMMENT ON COLUMN policy.file_path IS 'Local path on Ubuntu server for stored PDF/Doc files';