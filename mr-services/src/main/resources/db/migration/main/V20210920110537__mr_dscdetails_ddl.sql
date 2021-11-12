CREATE TABLE eg_mr_dscdetails(
    id character varying(64),
    tenantid character varying(64),
    documenttype character varying(64),
    documentid character varying(64),
    mr_id character varying(64),
    applicationnumber character varying(64),
    approvedby character varying(64),
	additionalDetail JSONB,
    createdBy character varying(64),
    lastModifiedBy character varying(64),
    createdTime bigint,
    lastModifiedTime bigint,

    CONSTRAINT uk_eg_mr_dscdetails PRIMARY KEY (id),
    CONSTRAINT fk_eg_mr_dscdetails FOREIGN KEY (mr_id) REFERENCES eg_mr_application (id)
);

CREATE INDEX IF NOT EXISTS index_eg_mr_dscdetails_tenantid ON eg_mr_dscdetails (tenantid);

CREATE INDEX IF NOT EXISTS index_eg_mr_dscdetails_approvedby ON eg_mr_dscdetails (approvedby);

CREATE INDEX IF NOT EXISTS index_eg_mr_dscdetails_documentid ON eg_mr_dscdetails (documentid);