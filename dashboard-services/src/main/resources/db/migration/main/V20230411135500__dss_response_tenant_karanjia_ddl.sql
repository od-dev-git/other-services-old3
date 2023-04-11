CREATE TABLE IF NOT EXISTS state.eg_dss_response_karanjia (
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
	CONSTRAINT pk_eg_dss_response_karanjia PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_charttype ON state.eg_dss_response_karanjia (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_city ON state.eg_dss_response_karanjia (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_districtid ON state.eg_dss_response_karanjia (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_enddate ON state.eg_dss_response_karanjia (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_headername ON state.eg_dss_response_karanjia (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_id ON state.eg_dss_response_karanjia (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_modulelevel ON state.eg_dss_response_karanjia (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_startdate ON state.eg_dss_response_karanjia (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_tenantid ON state.eg_dss_response_karanjia (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_timeinterval ON state.eg_dss_response_karanjia (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_valuetype ON state.eg_dss_response_karanjia (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_karanjia_visualizationcode ON state.eg_dss_response_karanjia (visualizationcode);
