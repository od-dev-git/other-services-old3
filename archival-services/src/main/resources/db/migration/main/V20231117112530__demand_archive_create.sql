CREATE TABLE IF NOT EXISTS public.egbs_demand_v1_archive (
	id varchar(64) NOT NULL,
	consumercode varchar(250) NOT NULL,
	consumertype varchar(250) NOT NULL,
	businessservice varchar(250) NOT NULL,
	payer varchar(250) NULL,
	taxperiodfrom int8 NOT NULL,
	taxperiodto int8 NOT NULL,
	createdby varchar(256) NOT NULL,
	createdtime int8 NOT NULL,
	lastmodifiedby varchar(256) NULL,
	lastmodifiedtime int8 NULL,
	tenantid varchar(250) NOT NULL,
	minimumamountpayable numeric(12, 2) NULL,
	status varchar(64) NULL,
	additionaldetails json NULL,
	billexpirytime int8 NULL,
	ispaymentcompleted bool NULL DEFAULT false,
	fixedbillexpirydate int8 NULL,
	archivaltime int8 NULL,
	CONSTRAINT pk_egbs_demand_v1_archive PRIMARY KEY (id, tenantid)
);
CREATE INDEX idx_egbs_demand_v1_archive_businessservice ON public.egbs_demand_v1_archive USING btree (businessservice);
CREATE INDEX idx_egbs_demand_v1_archive_consumercode ON public.egbs_demand_v1_archive USING btree (consumercode);
CREATE INDEX idx_egbs_demand_v1_archive_consumertype ON public.egbs_demand_v1_archive USING btree (consumertype);
CREATE INDEX idx_egbs_demand_v1_archive_id ON public.egbs_demand_v1_archive USING btree (id);
CREATE INDEX idx_egbs_demand_v1_archive_payer ON public.egbs_demand_v1_archive USING btree (payer);
CREATE INDEX idx_egbs_demand_v1_archive_taxperiodfrom ON public.egbs_demand_v1_archive USING btree (taxperiodfrom);
CREATE INDEX idx_egbs_demand_v1_archive_taxperiodto ON public.egbs_demand_v1_archive USING btree (taxperiodto);
CREATE INDEX idx_egbs_demand_v1_archive_tenantid ON public.egbs_demand_v1_archive USING btree (tenantid);
CREATE UNIQUE INDEX uk_egbs_demand_v1_archive_consumercode_businessservice ON public.egbs_demand_v1_archive USING btree (consumercode, tenantid, taxperiodfrom, taxperiodto, businessservice, status) WHERE ((status)::text = 'ACTIVE'::text);


CREATE TABLE IF NOT EXISTS  public.egbs_demanddetail_v1_archive (
	id varchar(64) NOT NULL,
	demandid varchar(64) NOT NULL,
	taxheadcode varchar(250) NOT NULL,
	taxamount numeric(12, 2) NOT NULL,
	collectionamount numeric(12, 2) NOT NULL,
	createdby varchar(256) NOT NULL,
	createdtime int8 NOT NULL,
	lastmodifiedby varchar(256) NULL,
	lastmodifiedtime int8 NULL,
	tenantid varchar(250) NOT NULL,
	additionaldetails json NULL,
	archivaltime int8 NULL,
	CONSTRAINT pk_egbs_demanddetail_v1_archive PRIMARY KEY (id, tenantid)
);
CREATE INDEX idx_egbs_demanddetail_v1_archive_demandid ON public.egbs_demanddetail_v1_archive USING btree (demandid);
CREATE INDEX idx_egbs_demanddetail_v1_archive_tenantid ON public.egbs_demanddetail_v1_archive USING btree (tenantid);
