CREATE TABLE IF NOT EXISTS state.eg_dss_response (
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
	valuetype varchar null,
	CONSTRAINT pk_eg_dss_response PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_eg_dss_response_id ON state.eg_dss_response(id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_visualizationcode ON state.eg_dss_response(visualizationcode);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_modulelevel ON state.eg_dss_response(modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_startdate ON state.eg_dss_response(startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_enddate ON state.eg_dss_response(enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_timeinterval ON state.eg_dss_response(timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_charttype ON state.eg_dss_response(charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tenantid ON state.eg_dss_response(tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_districtid ON state.eg_dss_response(districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_city ON state.eg_dss_response(city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_headername ON state.eg_dss_response(headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_valuetype ON state.eg_dss_response(valuetype);
