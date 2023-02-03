ALTER TABLE eg_mr_application
ADD COLUMN IF NOT EXISTS istatkalapplication boolean;

ALTER TABLE eg_mr_application_audit
ADD COLUMN IF NOT EXISTS istatkalapplication boolean;
