CREATE table if not exists eg_usm_survey
(
id  character varying(128),
tenantid character varying(128),
title character varying(60),
description character varying(140),
status character varying(128),
startDate  bigint,
endDate bigint,
collectCitizenInfo boolean,
postedby character varying(128),
createdtime bigint, 
createdby   character varying(64),
lastmodifiedtime bigint,
lastmodifiedby  character varying(64),

CONSTRAINT pk_eg_usm_survey PRIMARY key (id)
);


CREATE TABLE if not exists eg_usm_question
(
  id      character varying(64),
  surveyid  character varying(128),
  questionstatement  character varying(1024),
  category    character varying(128),
  options    character varying(2048),
  type      character varying(128),
  status     character varying(128),
  required   boolean,
  createdby  character varying(64),
  lastmodifiedby character varying(64),
  createdtime  bigint,
  lastmodifiedtime bigint,
  CONSTRAINT pk_eg_usm_question PRIMARY key (id),
  CONSTRAINT fk_eg_usm_question FOREIGN key (surveyid) REFERENCES eg_usm_survey (id)
  );
  

 CREATE TABLE if not exists eg_usm_survey_submitted
(
     id       character varying(64),
    surveyId      character varying(64),
    surveysubmittedno  character varying(36),
    tenantid          character varying(128),
    ward             character varying(10),
    slumcode          character varying(10),
     surveytime        bigint,
     isclosed           boolean,
    createdtime bigint  ,     
    createdby   character varying(64),
    lastmodifiedtime   bigint,    
    lastmodifiedby  character varying(64),
    additionaldetail  JSONB,


    CONSTRAINT pk_eg_usm_survey_submitted PRIMARY KEY(id),
    CONSTRAINT fk_eg_usm_survey_submitted FOREIGN KEY(surveyId) REFERENCES eg_usm_survey(id)
);


CREATE table if not exists eg_usm_survey_submitted_answer
(
     id       character varying(64),
    surveysubmittedid      character varying(64),
    questionid      character varying(64),
    questioncategory      character varying(128),
    answer                character varying(2048),
     createdtime bigint  ,     
    createdby   character varying(64),
    lastmodifiedtime   bigint,    
    lastmodifiedby  character varying(64),
    additionaldetail  JSONB,

    CONSTRAINT pk_eg_usm_survey_submitted_answer PRIMARY KEY(id),
    CONSTRAINT fk_eg_usm_survey_submitted_answer FOREIGN KEY(surveysubmittedid) REFERENCES eg_usm_survey_submitted(id)
);



CREATE TABLE if not exists eg_usm_survey_ticket(
      id       character varying(64),
      tenantid  character varying(128),
      ticketno  character varying(128),
      surveyanswerid  character varying(64),
      questionid   character varying(64),
      ticketdescription character varying(256),
      status character varying(128),
      ticketcreatedtime bigint,
      ticketclosedtime  bigint,
      createdtime bigint  ,     
    createdby   character varying(64),
    lastmodifiedtime   bigint,    
    lastmodifiedby  character varying(64),
    additionaldetail  JSONB,


CONSTRAINT pk_eg_usm_survey_ticket PRIMARY KEY(id),
CONSTRAINT fk_eg_usm_survey_ticket FOREIGN KEY(surveyanswerid) REFERENCES eg_usm_survey_submitted_answer(id)

);

CREATE table if not exists eg_usm_slum_question_lookup(

    id character varying(64),
    tenantid character varying(128),
    slumcode  character varying(128),
    questionid  character varying(64),
    hasopenticket  boolean,
    ticketid character varying(64),
     createdtime bigint  ,     
    createdby   character varying(64),
    lastmodifiedtime   bigint,    
    lastmodifiedby  character varying(64),

    CONSTRAINT pk_eg_usm_slum_question_lookup PRIMARY key (id)
);



 CREATE TABLE if not exists eg_usm_sda_mapping  
    (
     id  character varying(64),
    userid  int8 not null,
    tenantid    character varying(128),
    slumcode    character varying(128),
    active  boolean,
    createdtime bigint,      
    createdby   character varying(64),
    lastmodifiedtime    bigint ,       
    lastmodifiedby  character varying(64)   ,
    
    CONSTRAINT pk_eg_usm_sda_mapping PRIMARY KEY(id) 
    );


DROP SEQUENCE IF EXISTS SEQ_SURVEY_SUBMITTED_NO ;
DROP SEQUENCE IF EXISTS SEQ_SURVEY_TICKET_NO;

CREATE SEQUENCE SEQ_SURVEY_SUBMITTED_NO;
CREATE SEQUENCE SEQ_SURVEY_TICKET_NO;

