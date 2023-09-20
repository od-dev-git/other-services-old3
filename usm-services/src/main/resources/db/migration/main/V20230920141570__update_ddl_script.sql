ALTER TABLE IF EXISTS eg_usm_survey
ADD COLUMN IF NOT EXISTS startTime character varying(10),
ADD COLUMN IF NOT EXISTS endTime character varying(10);


ALTER TABLE IF EXISTS eg_usm_question
ADD COLUMN IF NOT EXISTS questionstatement_odia  character varying(1024);


