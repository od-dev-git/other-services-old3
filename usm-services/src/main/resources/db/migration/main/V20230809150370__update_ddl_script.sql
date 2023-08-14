ALTER TABLE eg_usm_dept_mapping
ADD COLUMN IF NOT EXISTS tenantid character varying(128),
ADD COLUMN IF NOT EXISTS ward character varying(10),
ADD COLUMN IF NOT EXISTS slumcode character varying(128),
ADD COLUMN IF NOT EXISTS assigned character varying(64),
ADD COLUMN IF NOT EXISTS createdtime bigint,     
ADD COLUMN IF NOT EXISTS createdby   character varying(64),
ADD COLUMN IF NOT EXISTS lastmodifiedtime   bigint,    
ADD COLUMN IF NOT EXISTS lastmodifiedby  character varying(64),
DROP COLUMN department;


ALTER TABLE eg_usm_sda_mapping
ADD COLUMN IF NOT EXISTS ward character varying(10),
ALTER COLUMN userid TYPE  character varying(64);


