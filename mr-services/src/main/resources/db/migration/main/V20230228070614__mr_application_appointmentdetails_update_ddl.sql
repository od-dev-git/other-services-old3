ALTER TABLE eg_mr_application
ADD COLUMN IF NOT EXISTS slaendtime int8;

ALTER TABLE eg_mr_application_audit
ADD COLUMN IF NOT EXISTS slaendtime int8;

ALTER TABLE eg_mr_application
ADD COLUMN IF NOT EXISTS additionaldetails jsonb;

ALTER TABLE eg_mr_application_audit
ADD COLUMN IF NOT EXISTS additionaldetails jsonb;

