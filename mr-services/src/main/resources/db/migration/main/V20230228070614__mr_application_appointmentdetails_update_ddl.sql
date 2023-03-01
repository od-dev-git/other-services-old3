ALTER TABLE eg_mr_application
ADD COLUMN IF NOT EXISTS scheduleslaendtime int8;

ALTER TABLE eg_mr_application_audit
ADD COLUMN IF NOT EXISTS scheduleslaendtime int8;

ALTER TABLE eg_mr_appointmentdetails
ADD COLUMN IF NOT EXISTS approveslaendtime int8;
