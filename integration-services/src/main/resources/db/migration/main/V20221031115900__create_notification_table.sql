create table eg_uis_revenuenotification(
	id character varying(256) NOT NULL,
	districtname character varying(256) ,
	tenantid character varying(256),
	revenuevillage character varying(256),
	plotno character varying(256),
	flatno character varying(256),
	address character varying(256),
	currentownername character varying(256),
	currentownermobilenumber character varying(256),
	newownername character varying(256),
	newownermobilenumber character varying(256),
	actiontaken boolean,
	action character varying(256),
	additionaldetails jsonb,
	createdby character varying(256),
	createdtime bigint,
	lastmodifiedby character varying(256),
	lastmodifiedtime bigint,
	CONSTRAINT pk_eg_is_revenuenotification PRIMARY KEY (id)
);

CREATE INDEX eg_uis_revenuenotification_index ON eg_uis_revenuenotification(tenantid);