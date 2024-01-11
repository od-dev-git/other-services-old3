CREATE TABLE IF NOT EXISTS  eg_bpa_utility_reports (
    id character varying(256) NOT NULL,
    tenantid character varying(256),
    reporttype character varying(64),
    filestoreid character varying(256),
    filename character varying(256),
	additionaldetails jsonb,
	createdby character varying(64),
    lastmodifiedby character varying(64),
    createdtime bigint,
    lastmodifiedtime bigint,
	CONSTRAINT pk_eg_bpa_utility_reports PRIMARY KEY (id)
);
	 
CREATE INDEX IF NOT EXISTS bpa_utility_reports_index  ON eg_bpa_utility_reports (
    reporttype,
    id
);

