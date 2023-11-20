ALTER TABLE eg_usm_question
ADD COLUMN IF NOT EXISTS questionorder int NOT NULL DEFAULT 0;
