CREATE TABLE public.eg_dss_response (
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
