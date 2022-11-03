create table if not exists eg_uis_revenuenotification(
	id character varying(256) NOT NULL,
	districtname character varying(256) ,
	tenantid character varying(256),
	revenuevillage character varying(256),
	plotno character varying(256),
	flatno character varying(256),
	address character varying(1000),
	actiontaken boolean,
	action character varying(256),
	additionaldetails jsonb,
	createdby character varying(256),
	createdtime bigint,
	lastmodifiedby character varying(256),
	lastmodifiedtime bigint,
	CONSTRAINT pk_eg_is_revenuenotification PRIMARY KEY (id)
);

create index if not exists eg_uis_revenuenotification_index ON eg_uis_revenuenotification(tenantid);



create table if not exists eg_uis_revenuenotification_owners(
	revenuenotificationid character varying(256) NOT NULL,
	ownername character varying(256),
	mobilenumber character varying(256),
	ownertype character varying(50),
	createdby character varying(256),
	createdtime bigint,
	lastmodifiedby character varying(256),
	lastmodifiedtime bigint,	
	CONSTRAINT eeg_uis_revenuenotification_owners_id_fkey FOREIGN KEY (revenuenotificationid) REFERENCES eg_uis_revenuenotification (id) 
  	ON UPDATE CASCADE
  	ON DELETE CASCADE
);

create index if not exists eg_uis_revenuenotification_owners_index ON eg_uis_revenuenotification_owners(revenuenotificationid);