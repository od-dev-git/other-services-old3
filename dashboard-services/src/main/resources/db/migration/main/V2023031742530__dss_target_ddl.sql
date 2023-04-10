-- Create state schema for dashboard
CREATE SCHEMA IF NOT EXISTS state;

CREATE TABLE IF NOT EXISTS state.eg_dss_target (
	id varchar NOT NULL,
	snoForMunicipalCorporation varchar NULL,
	tenantIdForMunicipalCorporation varchar NULL,
	ulbName varchar NULL,
	districtId varchar NULL,
	actualCollectionForMunicipalCorporation decimal(15,2)  NULL,
	budgetProposedForMunicipalCorporation decimal(15,2) NULL,
	actualCollectionBudgetedForMunicipalCorporation decimal(15,2) NULL,
	financialYear varchar NULL,
	businessService varchar NULL,
	timestamp bigint NULL,
	CONSTRAINT pk_eg_dss_target PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_eg_dss_target_id ON state.eg_dss_target(id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_target_ulbName ON state.eg_dss_target(ulbName);
CREATE INDEX IF NOT EXISTS idx_eg_dss_target_districtId ON state.eg_dss_target(districtId);
CREATE INDEX IF NOT EXISTS idx_eg_dss_target_tenantIdForMunicipalCorporation ON state.eg_dss_target(tenantIdForMunicipalCorporation);
CREATE INDEX IF NOT EXISTS idx_eg_dss_target_financialYear ON state.eg_dss_target(financialYear);
CREATE INDEX IF NOT EXISTS idx_eg_dss_target_businessService ON state.eg_dss_target(businessService);
