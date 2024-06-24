ALTER TABLE eg_usm_survey_ticket
ADD COLUMN IF NOT EXISTS escalatedid  character varying(128),
ADD COLUMN IF NOT EXISTS escalatedtime bigint;