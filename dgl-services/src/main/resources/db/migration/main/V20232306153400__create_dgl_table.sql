CREATE TABLE eg_dgl_reference_data(

tenantid                character varying(64) NOT NULL,
consumercode            character varying(256),
maskedid                character varying(256)  NOT NULL,
filestore               character varying(256) NOT NULL,
additionaldetails       JSONB,
CONSTRAINT pk_eg_dgl_reference_data PRIMARY KEY (consumercode)
);
