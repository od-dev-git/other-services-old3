CREATE TABLE IF NOT EXISTS eg_usm_survey_ticket_comment(
 
  id                 character varying(128),
  ticketid           character varying(64),
  comment            character varying(64),
  createdtime        bigint,     
  createdby          character varying(64),
  lastmodifiedtime   bigint,    
  lastmodifiedby     character varying(64),

  CONSTRAINT pk_eg_usm_survey_ticket_comment PRIMARY key (id),
  CONSTRAINT fk_eg_usm_survey_ticket_comment FOREIGN key (ticketid) REFERENCES eg_usm_survey_ticket(id)
);

CREATE TABLE  eg_usm_dept_mapping(

  id         character varying(64),
  role       character varying(64),
  category   character varying(64),
  department character varying(64),
  
  CONSTRAINT pk_eg_usm_dept_mapping PRIMARY key (id)
);


ALTER TABLE eg_usm_survey_ticket
ADD COLUMN IF NOT EXISTS unattended boolean;


ALTER TABLE eg_usm_survey_ticket
ADD COLUMN IF NOT EXISTS issatisfied boolean;

ALTER TABLE eg_usm_survey_submitted
DROP COLUMN isclosed;
