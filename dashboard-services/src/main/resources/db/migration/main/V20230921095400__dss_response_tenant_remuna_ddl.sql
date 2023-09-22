CREATE TABLE IF NOT EXISTS state.eg_dss_response_remuna (
	id varchar NOT NULL,
	visualizationcode varchar NULL,
	modulelevel varchar NULL,
	startdate int8 NULL,
	enddate int8 NULL,
	timeinterval varchar NULL,
	charttype varchar NULL,
	responsedata jsonb NULL,
	tenantid varchar NULL,
	districtid varchar NULL,
	city varchar NULL,
	headername varchar NULL,
	valuetype varchar NULL,
	lastmodifiedtime int8 NULL,
	CONSTRAINT pk_eg_dss_response_remuna PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_charttype ON state.eg_dss_response_remuna (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_city ON state.eg_dss_response_remuna (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_districtid ON state.eg_dss_response_remuna (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_enddate ON state.eg_dss_response_remuna (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_headername ON state.eg_dss_response_remuna (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_id ON state.eg_dss_response_remuna (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_modulelevel ON state.eg_dss_response_remuna (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_startdate ON state.eg_dss_response_remuna (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_tenantid ON state.eg_dss_response_remuna (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_timeinterval ON state.eg_dss_response_remuna (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_valuetype ON state.eg_dss_response_remuna (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_remuna_visualizationcode ON state.eg_dss_response_remuna (visualizationcode);
