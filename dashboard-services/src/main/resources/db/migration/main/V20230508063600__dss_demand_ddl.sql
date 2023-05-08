CREATE TABLE IF NOT EXISTS state.eg_dss_demand (
	id varchar NOT NULL,
	tenantid varchar not NULL,
	businessservice varchar NULL,
	financialyear varchar null,
	amount numeric null,
	taxperiodfrom int8 NULL,
	taxperiodto int8 null,
	lastmodifiedtime int8 null,
	CONSTRAINT pk_eg_dss_demand PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_eg_dss_demand_id ON state.eg_dss_demand(id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_demand_tenantid ON state.eg_dss_demand(tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_demand_businessservice ON state.eg_dss_demand(businessservice);
CREATE INDEX IF NOT EXISTS idx_eg_dss_demand_financialyear ON state.eg_dss_demand(financialyear);
CREATE INDEX IF NOT EXISTS idx_eg_dss_demand_taxperiodfrom ON state.eg_dss_demand(taxperiodfrom);
CREATE INDEX IF NOT EXISTS idx_eg_dss_demand_taxperiodto ON state.eg_dss_demand(taxperiodto);
