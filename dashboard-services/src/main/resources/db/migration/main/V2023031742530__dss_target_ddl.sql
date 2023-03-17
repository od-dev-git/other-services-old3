CREATE TABLE eg_dss_target (
	id varchar NOT NULL,
	snoForMunicipalCorporation varchar NULL,
	tenantIdForMunicipalCorporation varchar NULL,
	ulbName varchar NULL,
	districtId varchar NULL,
	actualCollectionForMunicipalCorporation decimal(15,2)  NULL,
	budgetProposedForMunicipalCorporation decimal(15,2) NULL,
	actualCollectionBudgetedForMunicipalCorporation decimal(15,2) NULL,
	financialYear jsonb NULL,
	businessService varchar NULL,
	Timestamp varchar NULL,
	CONSTRAINT pk_eg_dss_target PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_eg_dss_target_id ON eg_dss_target(id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_target_ulbName ON eg_dss_target(ulbName);
CREATE INDEX IF NOT EXISTS idx_eg_dss_target_districtId ON eg_dss_target(districtId);
CREATE INDEX IF NOT EXISTS idx_eg_dss_target_tenantIdForMunicipalCorporation ON eg_dss_target(tenantIdForMunicipalCorporation);
CREATE INDEX IF NOT EXISTS idx_eg_dss_target_financialYear ON eg_dss_target(financialYear);
CREATE INDEX IF NOT EXISTS idx_eg_dss_target_businessService ON eg_dss_target(businessService);
