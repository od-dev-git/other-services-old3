CREATE TABLE IF NOT EXISTS public.eg_dss_response_cuttack (
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
	CONSTRAINT pk_eg_dss_response_cuttack PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_charttype ON public.eg_dss_response_cuttack (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_city ON public.eg_dss_response_cuttack (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_districtid ON public.eg_dss_response_cuttack (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_enddate ON public.eg_dss_response_cuttack (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_headername ON public.eg_dss_response_cuttack (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_id ON public.eg_dss_response_cuttack (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_modulelevel ON public.eg_dss_response_cuttack (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_startdate ON public.eg_dss_response_cuttack (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_tenantid ON public.eg_dss_response_cuttack (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_timeinterval ON public.eg_dss_response_cuttack (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_valuetype ON public.eg_dss_response_cuttack (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttack_visualizationcode ON public.eg_dss_response_cuttack (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_jatni (
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
	CONSTRAINT pk_eg_dss_response_jatni PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_charttype ON public.eg_dss_response_jatni (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_city ON public.eg_dss_response_jatni (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_districtid ON public.eg_dss_response_jatni (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_enddate ON public.eg_dss_response_jatni (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_headername ON public.eg_dss_response_jatni (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_id ON public.eg_dss_response_jatni (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_modulelevel ON public.eg_dss_response_jatni (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_startdate ON public.eg_dss_response_jatni (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_tenantid ON public.eg_dss_response_jatni (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_timeinterval ON public.eg_dss_response_jatni (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_valuetype ON public.eg_dss_response_jatni (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jatni_visualizationcode ON public.eg_dss_response_jatni (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_angul (
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
	CONSTRAINT pk_eg_dss_response_angul PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_charttype ON public.eg_dss_response_angul (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_city ON public.eg_dss_response_angul (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_districtid ON public.eg_dss_response_angul (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_enddate ON public.eg_dss_response_angul (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_headername ON public.eg_dss_response_angul (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_id ON public.eg_dss_response_angul (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_modulelevel ON public.eg_dss_response_angul (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_startdate ON public.eg_dss_response_angul (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_tenantid ON public.eg_dss_response_angul (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_timeinterval ON public.eg_dss_response_angul (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_valuetype ON public.eg_dss_response_angul (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_angul_visualizationcode ON public.eg_dss_response_angul (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_choudwar (
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
	CONSTRAINT pk_eg_dss_response_choudwar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_charttype ON public.eg_dss_response_choudwar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_city ON public.eg_dss_response_choudwar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_districtid ON public.eg_dss_response_choudwar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_enddate ON public.eg_dss_response_choudwar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_headername ON public.eg_dss_response_choudwar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_id ON public.eg_dss_response_choudwar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_modulelevel ON public.eg_dss_response_choudwar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_startdate ON public.eg_dss_response_choudwar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_tenantid ON public.eg_dss_response_choudwar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_timeinterval ON public.eg_dss_response_choudwar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_valuetype ON public.eg_dss_response_choudwar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_choudwar_visualizationcode ON public.eg_dss_response_choudwar (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_berhampurdevelopmentauthority (
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
	CONSTRAINT pk_eg_dss_response_berhampurdevelopmentauthority PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_charttype ON public.eg_dss_response_berhampurdevelopmentauthority (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_city ON public.eg_dss_response_berhampurdevelopmentauthority (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_districtid ON public.eg_dss_response_berhampurdevelopmentauthority (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_enddate ON public.eg_dss_response_berhampurdevelopmentauthority (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_headername ON public.eg_dss_response_berhampurdevelopmentauthority (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_id ON public.eg_dss_response_berhampurdevelopmentauthority (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_modulelevel ON public.eg_dss_response_berhampurdevelopmentauthority (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_startdate ON public.eg_dss_response_berhampurdevelopmentauthority (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_tenantid ON public.eg_dss_response_berhampurdevelopmentauthority (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_timeinterval ON public.eg_dss_response_berhampurdevelopmentauthority (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_valuetype ON public.eg_dss_response_berhampurdevelopmentauthority (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampurdevelopmentauthority_visualizationcode ON public.eg_dss_response_berhampurdevelopmentauthority (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_birmitrapur (
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
	CONSTRAINT pk_eg_dss_response_birmitrapur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_charttype ON public.eg_dss_response_birmitrapur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_city ON public.eg_dss_response_birmitrapur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_districtid ON public.eg_dss_response_birmitrapur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_enddate ON public.eg_dss_response_birmitrapur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_headername ON public.eg_dss_response_birmitrapur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_id ON public.eg_dss_response_birmitrapur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_modulelevel ON public.eg_dss_response_birmitrapur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_startdate ON public.eg_dss_response_birmitrapur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_tenantid ON public.eg_dss_response_birmitrapur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_timeinterval ON public.eg_dss_response_birmitrapur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_valuetype ON public.eg_dss_response_birmitrapur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_birmitrapur_visualizationcode ON public.eg_dss_response_birmitrapur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_berhampur (
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
	CONSTRAINT pk_eg_dss_response_berhampur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_charttype ON public.eg_dss_response_berhampur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_city ON public.eg_dss_response_berhampur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_districtid ON public.eg_dss_response_berhampur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_enddate ON public.eg_dss_response_berhampur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_headername ON public.eg_dss_response_berhampur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_id ON public.eg_dss_response_berhampur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_modulelevel ON public.eg_dss_response_berhampur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_startdate ON public.eg_dss_response_berhampur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_tenantid ON public.eg_dss_response_berhampur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_timeinterval ON public.eg_dss_response_berhampur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_valuetype ON public.eg_dss_response_berhampur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_berhampur_visualizationcode ON public.eg_dss_response_berhampur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_chatrapur (
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
	CONSTRAINT pk_eg_dss_response_chatrapur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_charttype ON public.eg_dss_response_chatrapur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_city ON public.eg_dss_response_chatrapur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_districtid ON public.eg_dss_response_chatrapur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_enddate ON public.eg_dss_response_chatrapur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_headername ON public.eg_dss_response_chatrapur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_id ON public.eg_dss_response_chatrapur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_modulelevel ON public.eg_dss_response_chatrapur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_startdate ON public.eg_dss_response_chatrapur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_tenantid ON public.eg_dss_response_chatrapur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_timeinterval ON public.eg_dss_response_chatrapur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_valuetype ON public.eg_dss_response_chatrapur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chatrapur_visualizationcode ON public.eg_dss_response_chatrapur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_cuttackdevelopmentauthority (
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
	CONSTRAINT pk_eg_dss_response_cuttackdevelopmentauthority PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_charttype ON public.eg_dss_response_cuttackdevelopmentauthority (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_city ON public.eg_dss_response_cuttackdevelopmentauthority (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_districtid ON public.eg_dss_response_cuttackdevelopmentauthority (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_enddate ON public.eg_dss_response_cuttackdevelopmentauthority (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_headername ON public.eg_dss_response_cuttackdevelopmentauthority (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_id ON public.eg_dss_response_cuttackdevelopmentauthority (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_modulelevel ON public.eg_dss_response_cuttackdevelopmentauthority (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_startdate ON public.eg_dss_response_cuttackdevelopmentauthority (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_tenantid ON public.eg_dss_response_cuttackdevelopmentauthority (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_timeinterval ON public.eg_dss_response_cuttackdevelopmentauthority (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_valuetype ON public.eg_dss_response_cuttackdevelopmentauthority (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_cuttackdevelopmentauthority_visualizationcode ON public.eg_dss_response_cuttackdevelopmentauthority (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_gopalpur (
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
	CONSTRAINT pk_eg_dss_response_gopalpur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_charttype ON public.eg_dss_response_gopalpur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_city ON public.eg_dss_response_gopalpur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_districtid ON public.eg_dss_response_gopalpur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_enddate ON public.eg_dss_response_gopalpur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_headername ON public.eg_dss_response_gopalpur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_id ON public.eg_dss_response_gopalpur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_modulelevel ON public.eg_dss_response_gopalpur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_startdate ON public.eg_dss_response_gopalpur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_tenantid ON public.eg_dss_response_gopalpur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_timeinterval ON public.eg_dss_response_gopalpur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_valuetype ON public.eg_dss_response_gopalpur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gopalpur_visualizationcode ON public.eg_dss_response_gopalpur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_kalinganagardevelopmentauthority (
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
	CONSTRAINT pk_eg_dss_response_kalinganagardevelopmentauthority PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_charttype ON public.eg_dss_response_kalinganagardevelopmentauthority (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_city ON public.eg_dss_response_kalinganagardevelopmentauthority (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_districtid ON public.eg_dss_response_kalinganagardevelopmentauthority (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_enddate ON public.eg_dss_response_kalinganagardevelopmentauthority (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_headername ON public.eg_dss_response_kalinganagardevelopmentauthority (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_id ON public.eg_dss_response_kalinganagardevelopmentauthority (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_modulelevel ON public.eg_dss_response_kalinganagardevelopmentauthority (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_startdate ON public.eg_dss_response_kalinganagardevelopmentauthority (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_tenantid ON public.eg_dss_response_kalinganagardevelopmentauthority (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_timeinterval ON public.eg_dss_response_kalinganagardevelopmentauthority (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_valuetype ON public.eg_dss_response_kalinganagardevelopmentauthority (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kalinganagardevelopmentauthority_visualizationcode ON public.eg_dss_response_kalinganagardevelopmentauthority (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_khordha (
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
	CONSTRAINT pk_eg_dss_response_khordha PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_charttype ON public.eg_dss_response_khordha (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_city ON public.eg_dss_response_khordha (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_districtid ON public.eg_dss_response_khordha (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_enddate ON public.eg_dss_response_khordha (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_headername ON public.eg_dss_response_khordha (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_id ON public.eg_dss_response_khordha (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_modulelevel ON public.eg_dss_response_khordha (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_startdate ON public.eg_dss_response_khordha (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_tenantid ON public.eg_dss_response_khordha (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_timeinterval ON public.eg_dss_response_khordha (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_valuetype ON public.eg_dss_response_khordha (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khordha_visualizationcode ON public.eg_dss_response_khordha (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_konark (
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
	CONSTRAINT pk_eg_dss_response_konark PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_charttype ON public.eg_dss_response_konark (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_city ON public.eg_dss_response_konark (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_districtid ON public.eg_dss_response_konark (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_enddate ON public.eg_dss_response_konark (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_headername ON public.eg_dss_response_konark (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_id ON public.eg_dss_response_konark (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_modulelevel ON public.eg_dss_response_konark (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_startdate ON public.eg_dss_response_konark (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_tenantid ON public.eg_dss_response_konark (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_timeinterval ON public.eg_dss_response_konark (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_valuetype ON public.eg_dss_response_konark (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_konark_visualizationcode ON public.eg_dss_response_konark (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_paradeep (
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
	CONSTRAINT pk_eg_dss_response_paradeep PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_charttype ON public.eg_dss_response_paradeep (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_city ON public.eg_dss_response_paradeep (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_districtid ON public.eg_dss_response_paradeep (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_enddate ON public.eg_dss_response_paradeep (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_headername ON public.eg_dss_response_paradeep (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_id ON public.eg_dss_response_paradeep (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_modulelevel ON public.eg_dss_response_paradeep (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_startdate ON public.eg_dss_response_paradeep (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_tenantid ON public.eg_dss_response_paradeep (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_timeinterval ON public.eg_dss_response_paradeep (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_valuetype ON public.eg_dss_response_paradeep (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradeep_visualizationcode ON public.eg_dss_response_paradeep (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_paradipdevelopmentauthority (
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
	CONSTRAINT pk_eg_dss_response_paradipdevelopmentauthority PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_charttype ON public.eg_dss_response_paradipdevelopmentauthority (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_city ON public.eg_dss_response_paradipdevelopmentauthority (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_districtid ON public.eg_dss_response_paradipdevelopmentauthority (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_enddate ON public.eg_dss_response_paradipdevelopmentauthority (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_headername ON public.eg_dss_response_paradipdevelopmentauthority (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_id ON public.eg_dss_response_paradipdevelopmentauthority (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_modulelevel ON public.eg_dss_response_paradipdevelopmentauthority (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_startdate ON public.eg_dss_response_paradipdevelopmentauthority (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_tenantid ON public.eg_dss_response_paradipdevelopmentauthority (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_timeinterval ON public.eg_dss_response_paradipdevelopmentauthority (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_valuetype ON public.eg_dss_response_paradipdevelopmentauthority (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paradipdevelopmentauthority_visualizationcode ON public.eg_dss_response_paradipdevelopmentauthority (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_puri (
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
	CONSTRAINT pk_eg_dss_response_puri PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_charttype ON public.eg_dss_response_puri (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_city ON public.eg_dss_response_puri (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_districtid ON public.eg_dss_response_puri (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_enddate ON public.eg_dss_response_puri (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_headername ON public.eg_dss_response_puri (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_id ON public.eg_dss_response_puri (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_modulelevel ON public.eg_dss_response_puri (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_startdate ON public.eg_dss_response_puri (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_tenantid ON public.eg_dss_response_puri (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_timeinterval ON public.eg_dss_response_puri (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_valuetype ON public.eg_dss_response_puri (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_puri_visualizationcode ON public.eg_dss_response_puri (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_purikonarkdevelopmentauthority (
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
	CONSTRAINT pk_eg_dss_response_purikonarkdevelopmentauthority PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_charttype ON public.eg_dss_response_purikonarkdevelopmentauthority (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_city ON public.eg_dss_response_purikonarkdevelopmentauthority (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_districtid ON public.eg_dss_response_purikonarkdevelopmentauthority (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_enddate ON public.eg_dss_response_purikonarkdevelopmentauthority (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_headername ON public.eg_dss_response_purikonarkdevelopmentauthority (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_id ON public.eg_dss_response_purikonarkdevelopmentauthority (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_modulelevel ON public.eg_dss_response_purikonarkdevelopmentauthority (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_startdate ON public.eg_dss_response_purikonarkdevelopmentauthority (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_tenantid ON public.eg_dss_response_purikonarkdevelopmentauthority (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_timeinterval ON public.eg_dss_response_purikonarkdevelopmentauthority (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_valuetype ON public.eg_dss_response_purikonarkdevelopmentauthority (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purikonarkdevelopmentauthority_visualizationcode ON public.eg_dss_response_purikonarkdevelopmentauthority (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_rajgangpur (
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
	CONSTRAINT pk_eg_dss_response_rajgangpur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_charttype ON public.eg_dss_response_rajgangpur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_city ON public.eg_dss_response_rajgangpur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_districtid ON public.eg_dss_response_rajgangpur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_enddate ON public.eg_dss_response_rajgangpur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_headername ON public.eg_dss_response_rajgangpur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_id ON public.eg_dss_response_rajgangpur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_modulelevel ON public.eg_dss_response_rajgangpur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_startdate ON public.eg_dss_response_rajgangpur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_tenantid ON public.eg_dss_response_rajgangpur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_timeinterval ON public.eg_dss_response_rajgangpur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_valuetype ON public.eg_dss_response_rajgangpur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rajgangpur_visualizationcode ON public.eg_dss_response_rajgangpur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_rourkela (
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
	CONSTRAINT pk_eg_dss_response_rourkela PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_charttype ON public.eg_dss_response_rourkela (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_city ON public.eg_dss_response_rourkela (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_districtid ON public.eg_dss_response_rourkela (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_enddate ON public.eg_dss_response_rourkela (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_headername ON public.eg_dss_response_rourkela (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_id ON public.eg_dss_response_rourkela (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_modulelevel ON public.eg_dss_response_rourkela (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_startdate ON public.eg_dss_response_rourkela (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_tenantid ON public.eg_dss_response_rourkela (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_timeinterval ON public.eg_dss_response_rourkela (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_valuetype ON public.eg_dss_response_rourkela (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkela_visualizationcode ON public.eg_dss_response_rourkela (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_rourkeladevelopmentauthority (
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
	CONSTRAINT pk_eg_dss_response_rourkeladevelopmentauthority PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_charttype ON public.eg_dss_response_rourkeladevelopmentauthority (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_city ON public.eg_dss_response_rourkeladevelopmentauthority (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_districtid ON public.eg_dss_response_rourkeladevelopmentauthority (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_enddate ON public.eg_dss_response_rourkeladevelopmentauthority (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_headername ON public.eg_dss_response_rourkeladevelopmentauthority (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_id ON public.eg_dss_response_rourkeladevelopmentauthority (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_modulelevel ON public.eg_dss_response_rourkeladevelopmentauthority (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_startdate ON public.eg_dss_response_rourkeladevelopmentauthority (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_tenantid ON public.eg_dss_response_rourkeladevelopmentauthority (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_timeinterval ON public.eg_dss_response_rourkeladevelopmentauthority (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_valuetype ON public.eg_dss_response_rourkeladevelopmentauthority (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rourkeladevelopmentauthority_visualizationcode ON public.eg_dss_response_rourkeladevelopmentauthority (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_sambalpur (
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
	CONSTRAINT pk_eg_dss_response_sambalpur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_charttype ON public.eg_dss_response_sambalpur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_city ON public.eg_dss_response_sambalpur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_districtid ON public.eg_dss_response_sambalpur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_enddate ON public.eg_dss_response_sambalpur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_headername ON public.eg_dss_response_sambalpur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_id ON public.eg_dss_response_sambalpur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_modulelevel ON public.eg_dss_response_sambalpur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_startdate ON public.eg_dss_response_sambalpur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_tenantid ON public.eg_dss_response_sambalpur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_timeinterval ON public.eg_dss_response_sambalpur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_valuetype ON public.eg_dss_response_sambalpur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpur_visualizationcode ON public.eg_dss_response_sambalpur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_sambalpurdevelopmentauthority (
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
	CONSTRAINT pk_eg_dss_response_sambalpurdevelopmentauthority PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_charttype ON public.eg_dss_response_sambalpurdevelopmentauthority (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_city ON public.eg_dss_response_sambalpurdevelopmentauthority (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_districtid ON public.eg_dss_response_sambalpurdevelopmentauthority (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_enddate ON public.eg_dss_response_sambalpurdevelopmentauthority (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_headername ON public.eg_dss_response_sambalpurdevelopmentauthority (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_id ON public.eg_dss_response_sambalpurdevelopmentauthority (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_modulelevel ON public.eg_dss_response_sambalpurdevelopmentauthority (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_startdate ON public.eg_dss_response_sambalpurdevelopmentauthority (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_tenantid ON public.eg_dss_response_sambalpurdevelopmentauthority (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_timeinterval ON public.eg_dss_response_sambalpurdevelopmentauthority (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_valuetype ON public.eg_dss_response_sambalpurdevelopmentauthority (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sambalpurdevelopmentauthority_visualizationcode ON public.eg_dss_response_sambalpurdevelopmentauthority (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_sundargarh (
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
	CONSTRAINT pk_eg_dss_response_sundargarh PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_charttype ON public.eg_dss_response_sundargarh (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_city ON public.eg_dss_response_sundargarh (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_districtid ON public.eg_dss_response_sundargarh (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_enddate ON public.eg_dss_response_sundargarh (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_headername ON public.eg_dss_response_sundargarh (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_id ON public.eg_dss_response_sundargarh (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_modulelevel ON public.eg_dss_response_sundargarh (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_startdate ON public.eg_dss_response_sundargarh (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_tenantid ON public.eg_dss_response_sundargarh (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_timeinterval ON public.eg_dss_response_sundargarh (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_valuetype ON public.eg_dss_response_sundargarh (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sundargarh_visualizationcode ON public.eg_dss_response_sundargarh (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_talcher (
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
	CONSTRAINT pk_eg_dss_response_talcher PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_charttype ON public.eg_dss_response_talcher (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_city ON public.eg_dss_response_talcher (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_districtid ON public.eg_dss_response_talcher (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_enddate ON public.eg_dss_response_talcher (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_headername ON public.eg_dss_response_talcher (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_id ON public.eg_dss_response_talcher (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_modulelevel ON public.eg_dss_response_talcher (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_startdate ON public.eg_dss_response_talcher (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_tenantid ON public.eg_dss_response_talcher (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_timeinterval ON public.eg_dss_response_talcher (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_valuetype ON public.eg_dss_response_talcher (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcher_visualizationcode ON public.eg_dss_response_talcher (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (
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
	CONSTRAINT pk_eg_dss_response_talcherangulmeramandalidevelopmentauthority PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_charttype ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_city ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_districtid ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_enddate ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_headername ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_id ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_modulelevel ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_startdate ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_tenantid ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_timeinterval ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_valuetype ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_talcherangulmeramandalidevelopmentauthority_visualizationcode ON public.eg_dss_response_talcherangulmeramandalidevelopmentauthority (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_vyasanagar (
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
	CONSTRAINT pk_eg_dss_response_vyasanagar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_charttype ON public.eg_dss_response_vyasanagar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_city ON public.eg_dss_response_vyasanagar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_districtid ON public.eg_dss_response_vyasanagar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_enddate ON public.eg_dss_response_vyasanagar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_headername ON public.eg_dss_response_vyasanagar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_id ON public.eg_dss_response_vyasanagar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_modulelevel ON public.eg_dss_response_vyasanagar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_startdate ON public.eg_dss_response_vyasanagar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_tenantid ON public.eg_dss_response_vyasanagar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_timeinterval ON public.eg_dss_response_vyasanagar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_valuetype ON public.eg_dss_response_vyasanagar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_vyasanagar_visualizationcode ON public.eg_dss_response_vyasanagar (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_anandapur (
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
	CONSTRAINT pk_eg_dss_response_anandapur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_charttype ON public.eg_dss_response_anandapur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_city ON public.eg_dss_response_anandapur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_districtid ON public.eg_dss_response_anandapur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_enddate ON public.eg_dss_response_anandapur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_headername ON public.eg_dss_response_anandapur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_id ON public.eg_dss_response_anandapur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_modulelevel ON public.eg_dss_response_anandapur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_startdate ON public.eg_dss_response_anandapur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_tenantid ON public.eg_dss_response_anandapur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_timeinterval ON public.eg_dss_response_anandapur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_valuetype ON public.eg_dss_response_anandapur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_anandapur_visualizationcode ON public.eg_dss_response_anandapur (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_aska (
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
	CONSTRAINT pk_eg_dss_response_aska PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_charttype ON public.eg_dss_response_aska (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_city ON public.eg_dss_response_aska (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_districtid ON public.eg_dss_response_aska (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_enddate ON public.eg_dss_response_aska (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_headername ON public.eg_dss_response_aska (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_id ON public.eg_dss_response_aska (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_modulelevel ON public.eg_dss_response_aska (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_startdate ON public.eg_dss_response_aska (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_tenantid ON public.eg_dss_response_aska (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_timeinterval ON public.eg_dss_response_aska (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_valuetype ON public.eg_dss_response_aska (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_aska_visualizationcode ON public.eg_dss_response_aska (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_athagarh (
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
	CONSTRAINT pk_eg_dss_response_athagarh PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_charttype ON public.eg_dss_response_athagarh (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_city ON public.eg_dss_response_athagarh (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_districtid ON public.eg_dss_response_athagarh (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_enddate ON public.eg_dss_response_athagarh (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_headername ON public.eg_dss_response_athagarh (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_id ON public.eg_dss_response_athagarh (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_modulelevel ON public.eg_dss_response_athagarh (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_startdate ON public.eg_dss_response_athagarh (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_tenantid ON public.eg_dss_response_athagarh (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_timeinterval ON public.eg_dss_response_athagarh (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_valuetype ON public.eg_dss_response_athagarh (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athagarh_visualizationcode ON public.eg_dss_response_athagarh (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_athmallik (
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
	CONSTRAINT pk_eg_dss_response_athmallik PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_charttype ON public.eg_dss_response_athmallik (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_city ON public.eg_dss_response_athmallik (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_districtid ON public.eg_dss_response_athmallik (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_enddate ON public.eg_dss_response_athmallik (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_headername ON public.eg_dss_response_athmallik (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_id ON public.eg_dss_response_athmallik (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_modulelevel ON public.eg_dss_response_athmallik (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_startdate ON public.eg_dss_response_athmallik (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_tenantid ON public.eg_dss_response_athmallik (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_timeinterval ON public.eg_dss_response_athmallik (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_valuetype ON public.eg_dss_response_athmallik (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_athmallik_visualizationcode ON public.eg_dss_response_athmallik (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_attabira (
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
	CONSTRAINT pk_eg_dss_response_attabira PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_charttype ON public.eg_dss_response_attabira (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_city ON public.eg_dss_response_attabira (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_districtid ON public.eg_dss_response_attabira (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_enddate ON public.eg_dss_response_attabira (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_headername ON public.eg_dss_response_attabira (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_id ON public.eg_dss_response_attabira (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_modulelevel ON public.eg_dss_response_attabira (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_startdate ON public.eg_dss_response_attabira (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_tenantid ON public.eg_dss_response_attabira (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_timeinterval ON public.eg_dss_response_attabira (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_valuetype ON public.eg_dss_response_attabira (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_attabira_visualizationcode ON public.eg_dss_response_attabira (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_balangir (
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
	CONSTRAINT pk_eg_dss_response_balangir PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_charttype ON public.eg_dss_response_balangir (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_city ON public.eg_dss_response_balangir (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_districtid ON public.eg_dss_response_balangir (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_enddate ON public.eg_dss_response_balangir (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_headername ON public.eg_dss_response_balangir (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_id ON public.eg_dss_response_balangir (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_modulelevel ON public.eg_dss_response_balangir (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_startdate ON public.eg_dss_response_balangir (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_tenantid ON public.eg_dss_response_balangir (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_timeinterval ON public.eg_dss_response_balangir (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_valuetype ON public.eg_dss_response_balangir (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balangir_visualizationcode ON public.eg_dss_response_balangir (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_balasore (
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
	CONSTRAINT pk_eg_dss_response_balasore PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_charttype ON public.eg_dss_response_balasore (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_city ON public.eg_dss_response_balasore (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_districtid ON public.eg_dss_response_balasore (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_enddate ON public.eg_dss_response_balasore (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_headername ON public.eg_dss_response_balasore (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_id ON public.eg_dss_response_balasore (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_modulelevel ON public.eg_dss_response_balasore (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_startdate ON public.eg_dss_response_balasore (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_tenantid ON public.eg_dss_response_balasore (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_timeinterval ON public.eg_dss_response_balasore (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_valuetype ON public.eg_dss_response_balasore (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balasore_visualizationcode ON public.eg_dss_response_balasore (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_balimela (
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
	CONSTRAINT pk_eg_dss_response_balimela PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_charttype ON public.eg_dss_response_balimela (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_city ON public.eg_dss_response_balimela (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_districtid ON public.eg_dss_response_balimela (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_enddate ON public.eg_dss_response_balimela (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_headername ON public.eg_dss_response_balimela (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_id ON public.eg_dss_response_balimela (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_modulelevel ON public.eg_dss_response_balimela (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_startdate ON public.eg_dss_response_balimela (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_tenantid ON public.eg_dss_response_balimela (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_timeinterval ON public.eg_dss_response_balimela (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_valuetype ON public.eg_dss_response_balimela (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balimela_visualizationcode ON public.eg_dss_response_balimela (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_balliguda (
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
	CONSTRAINT pk_eg_dss_response_balliguda PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_charttype ON public.eg_dss_response_balliguda (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_city ON public.eg_dss_response_balliguda (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_districtid ON public.eg_dss_response_balliguda (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_enddate ON public.eg_dss_response_balliguda (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_headername ON public.eg_dss_response_balliguda (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_id ON public.eg_dss_response_balliguda (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_modulelevel ON public.eg_dss_response_balliguda (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_startdate ON public.eg_dss_response_balliguda (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_tenantid ON public.eg_dss_response_balliguda (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_timeinterval ON public.eg_dss_response_balliguda (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_valuetype ON public.eg_dss_response_balliguda (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balliguda_visualizationcode ON public.eg_dss_response_balliguda (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_balugaon (
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
	CONSTRAINT pk_eg_dss_response_balugaon PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_charttype ON public.eg_dss_response_balugaon (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_city ON public.eg_dss_response_balugaon (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_districtid ON public.eg_dss_response_balugaon (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_enddate ON public.eg_dss_response_balugaon (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_headername ON public.eg_dss_response_balugaon (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_id ON public.eg_dss_response_balugaon (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_modulelevel ON public.eg_dss_response_balugaon (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_startdate ON public.eg_dss_response_balugaon (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_tenantid ON public.eg_dss_response_balugaon (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_timeinterval ON public.eg_dss_response_balugaon (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_valuetype ON public.eg_dss_response_balugaon (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_balugaon_visualizationcode ON public.eg_dss_response_balugaon (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_banki (
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
	CONSTRAINT pk_eg_dss_response_banki PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_charttype ON public.eg_dss_response_banki (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_city ON public.eg_dss_response_banki (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_districtid ON public.eg_dss_response_banki (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_enddate ON public.eg_dss_response_banki (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_headername ON public.eg_dss_response_banki (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_id ON public.eg_dss_response_banki (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_modulelevel ON public.eg_dss_response_banki (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_startdate ON public.eg_dss_response_banki (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_tenantid ON public.eg_dss_response_banki (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_timeinterval ON public.eg_dss_response_banki (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_valuetype ON public.eg_dss_response_banki (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banki_visualizationcode ON public.eg_dss_response_banki (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_banpur (
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
	CONSTRAINT pk_eg_dss_response_banpur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_charttype ON public.eg_dss_response_banpur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_city ON public.eg_dss_response_banpur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_districtid ON public.eg_dss_response_banpur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_enddate ON public.eg_dss_response_banpur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_headername ON public.eg_dss_response_banpur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_id ON public.eg_dss_response_banpur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_modulelevel ON public.eg_dss_response_banpur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_startdate ON public.eg_dss_response_banpur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_tenantid ON public.eg_dss_response_banpur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_timeinterval ON public.eg_dss_response_banpur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_valuetype ON public.eg_dss_response_banpur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_banpur_visualizationcode ON public.eg_dss_response_banpur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_bargarh (
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
	CONSTRAINT pk_eg_dss_response_bargarh PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_charttype ON public.eg_dss_response_bargarh (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_city ON public.eg_dss_response_bargarh (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_districtid ON public.eg_dss_response_bargarh (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_enddate ON public.eg_dss_response_bargarh (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_headername ON public.eg_dss_response_bargarh (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_id ON public.eg_dss_response_bargarh (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_modulelevel ON public.eg_dss_response_bargarh (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_startdate ON public.eg_dss_response_bargarh (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_tenantid ON public.eg_dss_response_bargarh (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_timeinterval ON public.eg_dss_response_bargarh (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_valuetype ON public.eg_dss_response_bargarh (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bargarh_visualizationcode ON public.eg_dss_response_bargarh (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_barbil (
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
	CONSTRAINT pk_eg_dss_response_barbil PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_charttype ON public.eg_dss_response_barbil (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_city ON public.eg_dss_response_barbil (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_districtid ON public.eg_dss_response_barbil (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_enddate ON public.eg_dss_response_barbil (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_headername ON public.eg_dss_response_barbil (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_id ON public.eg_dss_response_barbil (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_modulelevel ON public.eg_dss_response_barbil (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_startdate ON public.eg_dss_response_barbil (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_tenantid ON public.eg_dss_response_barbil (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_timeinterval ON public.eg_dss_response_barbil (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_valuetype ON public.eg_dss_response_barbil (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barbil_visualizationcode ON public.eg_dss_response_barbil (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_baripada (
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
	CONSTRAINT pk_eg_dss_response_baripada PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_charttype ON public.eg_dss_response_baripada (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_city ON public.eg_dss_response_baripada (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_districtid ON public.eg_dss_response_baripada (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_enddate ON public.eg_dss_response_baripada (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_headername ON public.eg_dss_response_baripada (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_id ON public.eg_dss_response_baripada (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_modulelevel ON public.eg_dss_response_baripada (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_startdate ON public.eg_dss_response_baripada (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_tenantid ON public.eg_dss_response_baripada (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_timeinterval ON public.eg_dss_response_baripada (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_valuetype ON public.eg_dss_response_baripada (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_baripada_visualizationcode ON public.eg_dss_response_baripada (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_barpali (
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
	CONSTRAINT pk_eg_dss_response_barpali PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_charttype ON public.eg_dss_response_barpali (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_city ON public.eg_dss_response_barpali (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_districtid ON public.eg_dss_response_barpali (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_enddate ON public.eg_dss_response_barpali (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_headername ON public.eg_dss_response_barpali (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_id ON public.eg_dss_response_barpali (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_modulelevel ON public.eg_dss_response_barpali (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_startdate ON public.eg_dss_response_barpali (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_tenantid ON public.eg_dss_response_barpali (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_timeinterval ON public.eg_dss_response_barpali (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_valuetype ON public.eg_dss_response_barpali (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_barpali_visualizationcode ON public.eg_dss_response_barpali (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_basudevpur (
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
	CONSTRAINT pk_eg_dss_response_basudevpur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_charttype ON public.eg_dss_response_basudevpur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_city ON public.eg_dss_response_basudevpur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_districtid ON public.eg_dss_response_basudevpur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_enddate ON public.eg_dss_response_basudevpur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_headername ON public.eg_dss_response_basudevpur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_id ON public.eg_dss_response_basudevpur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_modulelevel ON public.eg_dss_response_basudevpur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_startdate ON public.eg_dss_response_basudevpur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_tenantid ON public.eg_dss_response_basudevpur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_timeinterval ON public.eg_dss_response_basudevpur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_valuetype ON public.eg_dss_response_basudevpur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_basudevpur_visualizationcode ON public.eg_dss_response_basudevpur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_bellaguntha (
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
	CONSTRAINT pk_eg_dss_response_bellaguntha PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_charttype ON public.eg_dss_response_bellaguntha (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_city ON public.eg_dss_response_bellaguntha (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_districtid ON public.eg_dss_response_bellaguntha (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_enddate ON public.eg_dss_response_bellaguntha (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_headername ON public.eg_dss_response_bellaguntha (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_id ON public.eg_dss_response_bellaguntha (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_modulelevel ON public.eg_dss_response_bellaguntha (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_startdate ON public.eg_dss_response_bellaguntha (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_tenantid ON public.eg_dss_response_bellaguntha (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_timeinterval ON public.eg_dss_response_bellaguntha (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_valuetype ON public.eg_dss_response_bellaguntha (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bellaguntha_visualizationcode ON public.eg_dss_response_bellaguntha (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_belpahar (
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
	CONSTRAINT pk_eg_dss_response_belpahar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_charttype ON public.eg_dss_response_belpahar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_city ON public.eg_dss_response_belpahar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_districtid ON public.eg_dss_response_belpahar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_enddate ON public.eg_dss_response_belpahar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_headername ON public.eg_dss_response_belpahar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_id ON public.eg_dss_response_belpahar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_modulelevel ON public.eg_dss_response_belpahar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_startdate ON public.eg_dss_response_belpahar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_tenantid ON public.eg_dss_response_belpahar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_timeinterval ON public.eg_dss_response_belpahar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_valuetype ON public.eg_dss_response_belpahar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_belpahar_visualizationcode ON public.eg_dss_response_belpahar (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_bhadrak (
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
	CONSTRAINT pk_eg_dss_response_bhadrak PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_charttype ON public.eg_dss_response_bhadrak (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_city ON public.eg_dss_response_bhadrak (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_districtid ON public.eg_dss_response_bhadrak (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_enddate ON public.eg_dss_response_bhadrak (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_headername ON public.eg_dss_response_bhadrak (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_id ON public.eg_dss_response_bhadrak (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_modulelevel ON public.eg_dss_response_bhadrak (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_startdate ON public.eg_dss_response_bhadrak (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_tenantid ON public.eg_dss_response_bhadrak (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_timeinterval ON public.eg_dss_response_bhadrak (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_valuetype ON public.eg_dss_response_bhadrak (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhadrak_visualizationcode ON public.eg_dss_response_bhadrak (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_bhanjanagar (
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
	CONSTRAINT pk_eg_dss_response_bhanjanagar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_charttype ON public.eg_dss_response_bhanjanagar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_city ON public.eg_dss_response_bhanjanagar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_districtid ON public.eg_dss_response_bhanjanagar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_enddate ON public.eg_dss_response_bhanjanagar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_headername ON public.eg_dss_response_bhanjanagar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_id ON public.eg_dss_response_bhanjanagar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_modulelevel ON public.eg_dss_response_bhanjanagar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_startdate ON public.eg_dss_response_bhanjanagar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_tenantid ON public.eg_dss_response_bhanjanagar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_timeinterval ON public.eg_dss_response_bhanjanagar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_valuetype ON public.eg_dss_response_bhanjanagar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhanjanagar_visualizationcode ON public.eg_dss_response_bhanjanagar (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_bhawanipatna (
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
	CONSTRAINT pk_eg_dss_response_bhawanipatna PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_charttype ON public.eg_dss_response_bhawanipatna (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_city ON public.eg_dss_response_bhawanipatna (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_districtid ON public.eg_dss_response_bhawanipatna (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_enddate ON public.eg_dss_response_bhawanipatna (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_headername ON public.eg_dss_response_bhawanipatna (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_id ON public.eg_dss_response_bhawanipatna (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_modulelevel ON public.eg_dss_response_bhawanipatna (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_startdate ON public.eg_dss_response_bhawanipatna (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_tenantid ON public.eg_dss_response_bhawanipatna (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_timeinterval ON public.eg_dss_response_bhawanipatna (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_valuetype ON public.eg_dss_response_bhawanipatna (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhawanipatna_visualizationcode ON public.eg_dss_response_bhawanipatna (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_bhuban (
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
	CONSTRAINT pk_eg_dss_response_bhuban PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_charttype ON public.eg_dss_response_bhuban (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_city ON public.eg_dss_response_bhuban (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_districtid ON public.eg_dss_response_bhuban (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_enddate ON public.eg_dss_response_bhuban (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_headername ON public.eg_dss_response_bhuban (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_id ON public.eg_dss_response_bhuban (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_modulelevel ON public.eg_dss_response_bhuban (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_startdate ON public.eg_dss_response_bhuban (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_tenantid ON public.eg_dss_response_bhuban (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_timeinterval ON public.eg_dss_response_bhuban (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_valuetype ON public.eg_dss_response_bhuban (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhuban_visualizationcode ON public.eg_dss_response_bhuban (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_bijepur (
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
	CONSTRAINT pk_eg_dss_response_bijepur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_charttype ON public.eg_dss_response_bijepur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_city ON public.eg_dss_response_bijepur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_districtid ON public.eg_dss_response_bijepur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_enddate ON public.eg_dss_response_bijepur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_headername ON public.eg_dss_response_bijepur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_id ON public.eg_dss_response_bijepur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_modulelevel ON public.eg_dss_response_bijepur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_startdate ON public.eg_dss_response_bijepur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_tenantid ON public.eg_dss_response_bijepur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_timeinterval ON public.eg_dss_response_bijepur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_valuetype ON public.eg_dss_response_bijepur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bijepur_visualizationcode ON public.eg_dss_response_bijepur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_binka (
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
	CONSTRAINT pk_eg_dss_response_binka PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_charttype ON public.eg_dss_response_binka (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_city ON public.eg_dss_response_binka (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_districtid ON public.eg_dss_response_binka (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_enddate ON public.eg_dss_response_binka (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_headername ON public.eg_dss_response_binka (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_id ON public.eg_dss_response_binka (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_modulelevel ON public.eg_dss_response_binka (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_startdate ON public.eg_dss_response_binka (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_tenantid ON public.eg_dss_response_binka (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_timeinterval ON public.eg_dss_response_binka (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_valuetype ON public.eg_dss_response_binka (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_binka_visualizationcode ON public.eg_dss_response_binka (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_boudhgarh (
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
	CONSTRAINT pk_eg_dss_response_boudhgarh PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_charttype ON public.eg_dss_response_boudhgarh (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_city ON public.eg_dss_response_boudhgarh (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_districtid ON public.eg_dss_response_boudhgarh (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_enddate ON public.eg_dss_response_boudhgarh (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_headername ON public.eg_dss_response_boudhgarh (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_id ON public.eg_dss_response_boudhgarh (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_modulelevel ON public.eg_dss_response_boudhgarh (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_startdate ON public.eg_dss_response_boudhgarh (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_tenantid ON public.eg_dss_response_boudhgarh (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_timeinterval ON public.eg_dss_response_boudhgarh (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_valuetype ON public.eg_dss_response_boudhgarh (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_boudhgarh_visualizationcode ON public.eg_dss_response_boudhgarh (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_brajrajnagar (
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
	CONSTRAINT pk_eg_dss_response_brajrajnagar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_charttype ON public.eg_dss_response_brajrajnagar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_city ON public.eg_dss_response_brajrajnagar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_districtid ON public.eg_dss_response_brajrajnagar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_enddate ON public.eg_dss_response_brajrajnagar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_headername ON public.eg_dss_response_brajrajnagar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_id ON public.eg_dss_response_brajrajnagar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_modulelevel ON public.eg_dss_response_brajrajnagar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_startdate ON public.eg_dss_response_brajrajnagar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_tenantid ON public.eg_dss_response_brajrajnagar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_timeinterval ON public.eg_dss_response_brajrajnagar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_valuetype ON public.eg_dss_response_brajrajnagar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_brajrajnagar_visualizationcode ON public.eg_dss_response_brajrajnagar (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_buguda (
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
	CONSTRAINT pk_eg_dss_response_buguda PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_charttype ON public.eg_dss_response_buguda (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_city ON public.eg_dss_response_buguda (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_districtid ON public.eg_dss_response_buguda (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_enddate ON public.eg_dss_response_buguda (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_headername ON public.eg_dss_response_buguda (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_id ON public.eg_dss_response_buguda (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_modulelevel ON public.eg_dss_response_buguda (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_startdate ON public.eg_dss_response_buguda (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_tenantid ON public.eg_dss_response_buguda (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_timeinterval ON public.eg_dss_response_buguda (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_valuetype ON public.eg_dss_response_buguda (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_buguda_visualizationcode ON public.eg_dss_response_buguda (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_champua (
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
	CONSTRAINT pk_eg_dss_response_champua PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_charttype ON public.eg_dss_response_champua (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_city ON public.eg_dss_response_champua (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_districtid ON public.eg_dss_response_champua (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_enddate ON public.eg_dss_response_champua (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_headername ON public.eg_dss_response_champua (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_id ON public.eg_dss_response_champua (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_modulelevel ON public.eg_dss_response_champua (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_startdate ON public.eg_dss_response_champua (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_tenantid ON public.eg_dss_response_champua (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_timeinterval ON public.eg_dss_response_champua (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_valuetype ON public.eg_dss_response_champua (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_champua_visualizationcode ON public.eg_dss_response_champua (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_chandbali (
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
	CONSTRAINT pk_eg_dss_response_chandbali PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_charttype ON public.eg_dss_response_chandbali (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_city ON public.eg_dss_response_chandbali (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_districtid ON public.eg_dss_response_chandbali (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_enddate ON public.eg_dss_response_chandbali (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_headername ON public.eg_dss_response_chandbali (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_id ON public.eg_dss_response_chandbali (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_modulelevel ON public.eg_dss_response_chandbali (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_startdate ON public.eg_dss_response_chandbali (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_tenantid ON public.eg_dss_response_chandbali (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_timeinterval ON public.eg_dss_response_chandbali (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_valuetype ON public.eg_dss_response_chandbali (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chandbali_visualizationcode ON public.eg_dss_response_chandbali (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_chikiti (
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
	CONSTRAINT pk_eg_dss_response_chikiti PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_charttype ON public.eg_dss_response_chikiti (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_city ON public.eg_dss_response_chikiti (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_districtid ON public.eg_dss_response_chikiti (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_enddate ON public.eg_dss_response_chikiti (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_headername ON public.eg_dss_response_chikiti (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_id ON public.eg_dss_response_chikiti (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_modulelevel ON public.eg_dss_response_chikiti (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_startdate ON public.eg_dss_response_chikiti (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_tenantid ON public.eg_dss_response_chikiti (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_timeinterval ON public.eg_dss_response_chikiti (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_valuetype ON public.eg_dss_response_chikiti (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_chikiti_visualizationcode ON public.eg_dss_response_chikiti (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_daspalla (
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
	CONSTRAINT pk_eg_dss_response_daspalla PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_charttype ON public.eg_dss_response_daspalla (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_city ON public.eg_dss_response_daspalla (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_districtid ON public.eg_dss_response_daspalla (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_enddate ON public.eg_dss_response_daspalla (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_headername ON public.eg_dss_response_daspalla (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_id ON public.eg_dss_response_daspalla (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_modulelevel ON public.eg_dss_response_daspalla (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_startdate ON public.eg_dss_response_daspalla (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_tenantid ON public.eg_dss_response_daspalla (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_timeinterval ON public.eg_dss_response_daspalla (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_valuetype ON public.eg_dss_response_daspalla (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_daspalla_visualizationcode ON public.eg_dss_response_daspalla (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_deogarh (
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
	CONSTRAINT pk_eg_dss_response_deogarh PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_charttype ON public.eg_dss_response_deogarh (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_city ON public.eg_dss_response_deogarh (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_districtid ON public.eg_dss_response_deogarh (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_enddate ON public.eg_dss_response_deogarh (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_headername ON public.eg_dss_response_deogarh (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_id ON public.eg_dss_response_deogarh (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_modulelevel ON public.eg_dss_response_deogarh (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_startdate ON public.eg_dss_response_deogarh (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_tenantid ON public.eg_dss_response_deogarh (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_timeinterval ON public.eg_dss_response_deogarh (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_valuetype ON public.eg_dss_response_deogarh (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_deogarh_visualizationcode ON public.eg_dss_response_deogarh (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_dhamnagar (
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
	CONSTRAINT pk_eg_dss_response_dhamnagar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_charttype ON public.eg_dss_response_dhamnagar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_city ON public.eg_dss_response_dhamnagar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_districtid ON public.eg_dss_response_dhamnagar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_enddate ON public.eg_dss_response_dhamnagar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_headername ON public.eg_dss_response_dhamnagar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_id ON public.eg_dss_response_dhamnagar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_modulelevel ON public.eg_dss_response_dhamnagar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_startdate ON public.eg_dss_response_dhamnagar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_tenantid ON public.eg_dss_response_dhamnagar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_timeinterval ON public.eg_dss_response_dhamnagar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_valuetype ON public.eg_dss_response_dhamnagar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhamnagar_visualizationcode ON public.eg_dss_response_dhamnagar (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_dharamgarh (
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
	CONSTRAINT pk_eg_dss_response_dharamgarh PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_charttype ON public.eg_dss_response_dharamgarh (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_city ON public.eg_dss_response_dharamgarh (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_districtid ON public.eg_dss_response_dharamgarh (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_enddate ON public.eg_dss_response_dharamgarh (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_headername ON public.eg_dss_response_dharamgarh (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_id ON public.eg_dss_response_dharamgarh (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_modulelevel ON public.eg_dss_response_dharamgarh (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_startdate ON public.eg_dss_response_dharamgarh (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_tenantid ON public.eg_dss_response_dharamgarh (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_timeinterval ON public.eg_dss_response_dharamgarh (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_valuetype ON public.eg_dss_response_dharamgarh (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dharamgarh_visualizationcode ON public.eg_dss_response_dharamgarh (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_dhenkanal (
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
	CONSTRAINT pk_eg_dss_response_dhenkanal PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_charttype ON public.eg_dss_response_dhenkanal (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_city ON public.eg_dss_response_dhenkanal (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_districtid ON public.eg_dss_response_dhenkanal (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_enddate ON public.eg_dss_response_dhenkanal (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_headername ON public.eg_dss_response_dhenkanal (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_id ON public.eg_dss_response_dhenkanal (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_modulelevel ON public.eg_dss_response_dhenkanal (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_startdate ON public.eg_dss_response_dhenkanal (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_tenantid ON public.eg_dss_response_dhenkanal (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_timeinterval ON public.eg_dss_response_dhenkanal (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_valuetype ON public.eg_dss_response_dhenkanal (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_dhenkanal_visualizationcode ON public.eg_dss_response_dhenkanal (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_digapahandi (
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
	CONSTRAINT pk_eg_dss_response_digapahandi PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_charttype ON public.eg_dss_response_digapahandi (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_city ON public.eg_dss_response_digapahandi (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_districtid ON public.eg_dss_response_digapahandi (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_enddate ON public.eg_dss_response_digapahandi (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_headername ON public.eg_dss_response_digapahandi (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_id ON public.eg_dss_response_digapahandi (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_modulelevel ON public.eg_dss_response_digapahandi (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_startdate ON public.eg_dss_response_digapahandi (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_tenantid ON public.eg_dss_response_digapahandi (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_timeinterval ON public.eg_dss_response_digapahandi (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_valuetype ON public.eg_dss_response_digapahandi (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_digapahandi_visualizationcode ON public.eg_dss_response_digapahandi (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_gudayagiri (
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
	CONSTRAINT pk_eg_dss_response_gudayagiri PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_charttype ON public.eg_dss_response_gudayagiri (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_city ON public.eg_dss_response_gudayagiri (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_districtid ON public.eg_dss_response_gudayagiri (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_enddate ON public.eg_dss_response_gudayagiri (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_headername ON public.eg_dss_response_gudayagiri (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_id ON public.eg_dss_response_gudayagiri (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_modulelevel ON public.eg_dss_response_gudayagiri (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_startdate ON public.eg_dss_response_gudayagiri (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_tenantid ON public.eg_dss_response_gudayagiri (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_timeinterval ON public.eg_dss_response_gudayagiri (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_valuetype ON public.eg_dss_response_gudayagiri (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudayagiri_visualizationcode ON public.eg_dss_response_gudayagiri (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_ganjam (
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
	CONSTRAINT pk_eg_dss_response_ganjam PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_charttype ON public.eg_dss_response_ganjam (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_city ON public.eg_dss_response_ganjam (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_districtid ON public.eg_dss_response_ganjam (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_enddate ON public.eg_dss_response_ganjam (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_headername ON public.eg_dss_response_ganjam (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_id ON public.eg_dss_response_ganjam (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_modulelevel ON public.eg_dss_response_ganjam (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_startdate ON public.eg_dss_response_ganjam (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_tenantid ON public.eg_dss_response_ganjam (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_timeinterval ON public.eg_dss_response_ganjam (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_valuetype ON public.eg_dss_response_ganjam (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ganjam_visualizationcode ON public.eg_dss_response_ganjam (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_gudari (
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
	CONSTRAINT pk_eg_dss_response_gudari PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_charttype ON public.eg_dss_response_gudari (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_city ON public.eg_dss_response_gudari (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_districtid ON public.eg_dss_response_gudari (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_enddate ON public.eg_dss_response_gudari (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_headername ON public.eg_dss_response_gudari (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_id ON public.eg_dss_response_gudari (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_modulelevel ON public.eg_dss_response_gudari (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_startdate ON public.eg_dss_response_gudari (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_tenantid ON public.eg_dss_response_gudari (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_timeinterval ON public.eg_dss_response_gudari (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_valuetype ON public.eg_dss_response_gudari (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gudari_visualizationcode ON public.eg_dss_response_gudari (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_gunupur (
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
	CONSTRAINT pk_eg_dss_response_gunupur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_charttype ON public.eg_dss_response_gunupur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_city ON public.eg_dss_response_gunupur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_districtid ON public.eg_dss_response_gunupur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_enddate ON public.eg_dss_response_gunupur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_headername ON public.eg_dss_response_gunupur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_id ON public.eg_dss_response_gunupur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_modulelevel ON public.eg_dss_response_gunupur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_startdate ON public.eg_dss_response_gunupur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_tenantid ON public.eg_dss_response_gunupur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_timeinterval ON public.eg_dss_response_gunupur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_valuetype ON public.eg_dss_response_gunupur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_gunupur_visualizationcode ON public.eg_dss_response_gunupur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_hindol (
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
	CONSTRAINT pk_eg_dss_response_hindol PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_charttype ON public.eg_dss_response_hindol (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_city ON public.eg_dss_response_hindol (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_districtid ON public.eg_dss_response_hindol (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_enddate ON public.eg_dss_response_hindol (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_headername ON public.eg_dss_response_hindol (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_id ON public.eg_dss_response_hindol (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_modulelevel ON public.eg_dss_response_hindol (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_startdate ON public.eg_dss_response_hindol (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_tenantid ON public.eg_dss_response_hindol (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_timeinterval ON public.eg_dss_response_hindol (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_valuetype ON public.eg_dss_response_hindol (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hindol_visualizationcode ON public.eg_dss_response_hindol (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_hinjilicut (
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
	CONSTRAINT pk_eg_dss_response_hinjilicut PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_charttype ON public.eg_dss_response_hinjilicut (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_city ON public.eg_dss_response_hinjilicut (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_districtid ON public.eg_dss_response_hinjilicut (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_enddate ON public.eg_dss_response_hinjilicut (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_headername ON public.eg_dss_response_hinjilicut (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_id ON public.eg_dss_response_hinjilicut (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_modulelevel ON public.eg_dss_response_hinjilicut (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_startdate ON public.eg_dss_response_hinjilicut (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_tenantid ON public.eg_dss_response_hinjilicut (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_timeinterval ON public.eg_dss_response_hinjilicut (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_valuetype ON public.eg_dss_response_hinjilicut (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_hinjilicut_visualizationcode ON public.eg_dss_response_hinjilicut (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_jagatsinghpur (
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
	CONSTRAINT pk_eg_dss_response_jagatsinghpur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_charttype ON public.eg_dss_response_jagatsinghpur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_city ON public.eg_dss_response_jagatsinghpur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_districtid ON public.eg_dss_response_jagatsinghpur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_enddate ON public.eg_dss_response_jagatsinghpur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_headername ON public.eg_dss_response_jagatsinghpur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_id ON public.eg_dss_response_jagatsinghpur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_modulelevel ON public.eg_dss_response_jagatsinghpur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_startdate ON public.eg_dss_response_jagatsinghpur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_tenantid ON public.eg_dss_response_jagatsinghpur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_timeinterval ON public.eg_dss_response_jagatsinghpur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_valuetype ON public.eg_dss_response_jagatsinghpur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jagatsinghpur_visualizationcode ON public.eg_dss_response_jagatsinghpur (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_jajpur (
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
	CONSTRAINT pk_eg_dss_response_jajpur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_charttype ON public.eg_dss_response_jajpur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_city ON public.eg_dss_response_jajpur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_districtid ON public.eg_dss_response_jajpur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_enddate ON public.eg_dss_response_jajpur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_headername ON public.eg_dss_response_jajpur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_id ON public.eg_dss_response_jajpur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_modulelevel ON public.eg_dss_response_jajpur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_startdate ON public.eg_dss_response_jajpur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_tenantid ON public.eg_dss_response_jajpur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_timeinterval ON public.eg_dss_response_jajpur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_valuetype ON public.eg_dss_response_jajpur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jajpur_visualizationcode ON public.eg_dss_response_jajpur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_jaleswar (
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
	CONSTRAINT pk_eg_dss_response_jaleswar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_charttype ON public.eg_dss_response_jaleswar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_city ON public.eg_dss_response_jaleswar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_districtid ON public.eg_dss_response_jaleswar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_enddate ON public.eg_dss_response_jaleswar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_headername ON public.eg_dss_response_jaleswar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_id ON public.eg_dss_response_jaleswar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_modulelevel ON public.eg_dss_response_jaleswar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_startdate ON public.eg_dss_response_jaleswar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_tenantid ON public.eg_dss_response_jaleswar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_timeinterval ON public.eg_dss_response_jaleswar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_valuetype ON public.eg_dss_response_jaleswar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jaleswar_visualizationcode ON public.eg_dss_response_jaleswar (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_jeypore (
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
	CONSTRAINT pk_eg_dss_response_jeypore PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_charttype ON public.eg_dss_response_jeypore (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_city ON public.eg_dss_response_jeypore (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_districtid ON public.eg_dss_response_jeypore (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_enddate ON public.eg_dss_response_jeypore (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_headername ON public.eg_dss_response_jeypore (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_id ON public.eg_dss_response_jeypore (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_modulelevel ON public.eg_dss_response_jeypore (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_startdate ON public.eg_dss_response_jeypore (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_tenantid ON public.eg_dss_response_jeypore (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_timeinterval ON public.eg_dss_response_jeypore (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_valuetype ON public.eg_dss_response_jeypore (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jeypore_visualizationcode ON public.eg_dss_response_jeypore (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_jharsuguda (
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
	CONSTRAINT pk_eg_dss_response_jharsuguda PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_charttype ON public.eg_dss_response_jharsuguda (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_city ON public.eg_dss_response_jharsuguda (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_districtid ON public.eg_dss_response_jharsuguda (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_enddate ON public.eg_dss_response_jharsuguda (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_headername ON public.eg_dss_response_jharsuguda (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_id ON public.eg_dss_response_jharsuguda (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_modulelevel ON public.eg_dss_response_jharsuguda (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_startdate ON public.eg_dss_response_jharsuguda (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_tenantid ON public.eg_dss_response_jharsuguda (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_timeinterval ON public.eg_dss_response_jharsuguda (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_valuetype ON public.eg_dss_response_jharsuguda (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_jharsuguda_visualizationcode ON public.eg_dss_response_jharsuguda (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_joda (
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
	CONSTRAINT pk_eg_dss_response_joda PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_charttype ON public.eg_dss_response_joda (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_city ON public.eg_dss_response_joda (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_districtid ON public.eg_dss_response_joda (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_enddate ON public.eg_dss_response_joda (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_headername ON public.eg_dss_response_joda (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_id ON public.eg_dss_response_joda (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_modulelevel ON public.eg_dss_response_joda (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_startdate ON public.eg_dss_response_joda (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_tenantid ON public.eg_dss_response_joda (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_timeinterval ON public.eg_dss_response_joda (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_valuetype ON public.eg_dss_response_joda (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_joda_visualizationcode ON public.eg_dss_response_joda (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_junagarh (
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
	CONSTRAINT pk_eg_dss_response_junagarh PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_charttype ON public.eg_dss_response_junagarh (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_city ON public.eg_dss_response_junagarh (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_districtid ON public.eg_dss_response_junagarh (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_enddate ON public.eg_dss_response_junagarh (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_headername ON public.eg_dss_response_junagarh (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_id ON public.eg_dss_response_junagarh (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_modulelevel ON public.eg_dss_response_junagarh (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_startdate ON public.eg_dss_response_junagarh (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_tenantid ON public.eg_dss_response_junagarh (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_timeinterval ON public.eg_dss_response_junagarh (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_valuetype ON public.eg_dss_response_junagarh (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_junagarh_visualizationcode ON public.eg_dss_response_junagarh (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_kamakhyanagar (
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
	CONSTRAINT pk_eg_dss_response_kamakhyanagar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_charttype ON public.eg_dss_response_kamakhyanagar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_city ON public.eg_dss_response_kamakhyanagar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_districtid ON public.eg_dss_response_kamakhyanagar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_enddate ON public.eg_dss_response_kamakhyanagar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_headername ON public.eg_dss_response_kamakhyanagar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_id ON public.eg_dss_response_kamakhyanagar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_modulelevel ON public.eg_dss_response_kamakhyanagar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_startdate ON public.eg_dss_response_kamakhyanagar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_tenantid ON public.eg_dss_response_kamakhyanagar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_timeinterval ON public.eg_dss_response_kamakhyanagar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_valuetype ON public.eg_dss_response_kamakhyanagar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kamakhyanagar_visualizationcode ON public.eg_dss_response_kamakhyanagar (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_kantabanji (
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
	CONSTRAINT pk_eg_dss_response_kantabanji PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_charttype ON public.eg_dss_response_kantabanji (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_city ON public.eg_dss_response_kantabanji (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_districtid ON public.eg_dss_response_kantabanji (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_enddate ON public.eg_dss_response_kantabanji (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_headername ON public.eg_dss_response_kantabanji (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_id ON public.eg_dss_response_kantabanji (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_modulelevel ON public.eg_dss_response_kantabanji (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_startdate ON public.eg_dss_response_kantabanji (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_tenantid ON public.eg_dss_response_kantabanji (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_timeinterval ON public.eg_dss_response_kantabanji (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_valuetype ON public.eg_dss_response_kantabanji (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kantabanji_visualizationcode ON public.eg_dss_response_kantabanji (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_kashinagar (
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
	CONSTRAINT pk_eg_dss_response_kashinagar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_charttype ON public.eg_dss_response_kashinagar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_city ON public.eg_dss_response_kashinagar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_districtid ON public.eg_dss_response_kashinagar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_enddate ON public.eg_dss_response_kashinagar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_headername ON public.eg_dss_response_kashinagar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_id ON public.eg_dss_response_kashinagar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_modulelevel ON public.eg_dss_response_kashinagar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_startdate ON public.eg_dss_response_kashinagar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_tenantid ON public.eg_dss_response_kashinagar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_timeinterval ON public.eg_dss_response_kashinagar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_valuetype ON public.eg_dss_response_kashinagar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kashinagar_visualizationcode ON public.eg_dss_response_kashinagar (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_kabisuryanagar (
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
	CONSTRAINT pk_eg_dss_response_kabisuryanagar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_charttype ON public.eg_dss_response_kabisuryanagar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_city ON public.eg_dss_response_kabisuryanagar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_districtid ON public.eg_dss_response_kabisuryanagar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_enddate ON public.eg_dss_response_kabisuryanagar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_headername ON public.eg_dss_response_kabisuryanagar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_id ON public.eg_dss_response_kabisuryanagar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_modulelevel ON public.eg_dss_response_kabisuryanagar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_startdate ON public.eg_dss_response_kabisuryanagar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_tenantid ON public.eg_dss_response_kabisuryanagar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_timeinterval ON public.eg_dss_response_kabisuryanagar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_valuetype ON public.eg_dss_response_kabisuryanagar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kabisuryanagar_visualizationcode ON public.eg_dss_response_kabisuryanagar (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_kendrapara (
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
	CONSTRAINT pk_eg_dss_response_kendrapara PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_charttype ON public.eg_dss_response_kendrapara (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_city ON public.eg_dss_response_kendrapara (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_districtid ON public.eg_dss_response_kendrapara (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_enddate ON public.eg_dss_response_kendrapara (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_headername ON public.eg_dss_response_kendrapara (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_id ON public.eg_dss_response_kendrapara (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_modulelevel ON public.eg_dss_response_kendrapara (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_startdate ON public.eg_dss_response_kendrapara (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_tenantid ON public.eg_dss_response_kendrapara (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_timeinterval ON public.eg_dss_response_kendrapara (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_valuetype ON public.eg_dss_response_kendrapara (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kendrapara_visualizationcode ON public.eg_dss_response_kendrapara (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_keonjhargarh (
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
	CONSTRAINT pk_eg_dss_response_keonjhargarh PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_charttype ON public.eg_dss_response_keonjhargarh (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_city ON public.eg_dss_response_keonjhargarh (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_districtid ON public.eg_dss_response_keonjhargarh (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_enddate ON public.eg_dss_response_keonjhargarh (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_headername ON public.eg_dss_response_keonjhargarh (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_id ON public.eg_dss_response_keonjhargarh (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_modulelevel ON public.eg_dss_response_keonjhargarh (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_startdate ON public.eg_dss_response_keonjhargarh (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_tenantid ON public.eg_dss_response_keonjhargarh (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_timeinterval ON public.eg_dss_response_keonjhargarh (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_valuetype ON public.eg_dss_response_keonjhargarh (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_keonjhargarh_visualizationcode ON public.eg_dss_response_keonjhargarh (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_kesinga (
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
	CONSTRAINT pk_eg_dss_response_kesinga PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_charttype ON public.eg_dss_response_kesinga (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_city ON public.eg_dss_response_kesinga (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_districtid ON public.eg_dss_response_kesinga (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_enddate ON public.eg_dss_response_kesinga (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_headername ON public.eg_dss_response_kesinga (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_id ON public.eg_dss_response_kesinga (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_modulelevel ON public.eg_dss_response_kesinga (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_startdate ON public.eg_dss_response_kesinga (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_tenantid ON public.eg_dss_response_kesinga (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_timeinterval ON public.eg_dss_response_kesinga (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_valuetype ON public.eg_dss_response_kesinga (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kesinga_visualizationcode ON public.eg_dss_response_kesinga (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_khallikote (
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
	CONSTRAINT pk_eg_dss_response_khallikote PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_charttype ON public.eg_dss_response_khallikote (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_city ON public.eg_dss_response_khallikote (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_districtid ON public.eg_dss_response_khallikote (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_enddate ON public.eg_dss_response_khallikote (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_headername ON public.eg_dss_response_khallikote (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_id ON public.eg_dss_response_khallikote (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_modulelevel ON public.eg_dss_response_khallikote (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_startdate ON public.eg_dss_response_khallikote (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_tenantid ON public.eg_dss_response_khallikote (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_timeinterval ON public.eg_dss_response_khallikote (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_valuetype ON public.eg_dss_response_khallikote (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khallikote_visualizationcode ON public.eg_dss_response_khallikote (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_khandapada (
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
	CONSTRAINT pk_eg_dss_response_khandapada PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_charttype ON public.eg_dss_response_khandapada (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_city ON public.eg_dss_response_khandapada (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_districtid ON public.eg_dss_response_khandapada (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_enddate ON public.eg_dss_response_khandapada (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_headername ON public.eg_dss_response_khandapada (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_id ON public.eg_dss_response_khandapada (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_modulelevel ON public.eg_dss_response_khandapada (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_startdate ON public.eg_dss_response_khandapada (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_tenantid ON public.eg_dss_response_khandapada (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_timeinterval ON public.eg_dss_response_khandapada (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_valuetype ON public.eg_dss_response_khandapada (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khandapada_visualizationcode ON public.eg_dss_response_khandapada (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_khariar (
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
	CONSTRAINT pk_eg_dss_response_khariar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_charttype ON public.eg_dss_response_khariar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_city ON public.eg_dss_response_khariar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_districtid ON public.eg_dss_response_khariar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_enddate ON public.eg_dss_response_khariar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_headername ON public.eg_dss_response_khariar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_id ON public.eg_dss_response_khariar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_modulelevel ON public.eg_dss_response_khariar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_startdate ON public.eg_dss_response_khariar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_tenantid ON public.eg_dss_response_khariar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_timeinterval ON public.eg_dss_response_khariar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_valuetype ON public.eg_dss_response_khariar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariar_visualizationcode ON public.eg_dss_response_khariar (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_khariarroad (
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
	CONSTRAINT pk_eg_dss_response_khariarroad PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_charttype ON public.eg_dss_response_khariarroad (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_city ON public.eg_dss_response_khariarroad (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_districtid ON public.eg_dss_response_khariarroad (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_enddate ON public.eg_dss_response_khariarroad (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_headername ON public.eg_dss_response_khariarroad (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_id ON public.eg_dss_response_khariarroad (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_modulelevel ON public.eg_dss_response_khariarroad (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_startdate ON public.eg_dss_response_khariarroad (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_tenantid ON public.eg_dss_response_khariarroad (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_timeinterval ON public.eg_dss_response_khariarroad (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_valuetype ON public.eg_dss_response_khariarroad (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_khariarroad_visualizationcode ON public.eg_dss_response_khariarroad (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_kodala (
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
	CONSTRAINT pk_eg_dss_response_kodala PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_charttype ON public.eg_dss_response_kodala (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_city ON public.eg_dss_response_kodala (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_districtid ON public.eg_dss_response_kodala (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_enddate ON public.eg_dss_response_kodala (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_headername ON public.eg_dss_response_kodala (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_id ON public.eg_dss_response_kodala (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_modulelevel ON public.eg_dss_response_kodala (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_startdate ON public.eg_dss_response_kodala (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_tenantid ON public.eg_dss_response_kodala (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_timeinterval ON public.eg_dss_response_kodala (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_valuetype ON public.eg_dss_response_kodala (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kodala_visualizationcode ON public.eg_dss_response_kodala (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_koraput (
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
	CONSTRAINT pk_eg_dss_response_koraput PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_charttype ON public.eg_dss_response_koraput (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_city ON public.eg_dss_response_koraput (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_districtid ON public.eg_dss_response_koraput (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_enddate ON public.eg_dss_response_koraput (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_headername ON public.eg_dss_response_koraput (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_id ON public.eg_dss_response_koraput (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_modulelevel ON public.eg_dss_response_koraput (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_startdate ON public.eg_dss_response_koraput (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_tenantid ON public.eg_dss_response_koraput (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_timeinterval ON public.eg_dss_response_koraput (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_valuetype ON public.eg_dss_response_koraput (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_koraput_visualizationcode ON public.eg_dss_response_koraput (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_kotpad (
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
	CONSTRAINT pk_eg_dss_response_kotpad PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_charttype ON public.eg_dss_response_kotpad (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_city ON public.eg_dss_response_kotpad (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_districtid ON public.eg_dss_response_kotpad (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_enddate ON public.eg_dss_response_kotpad (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_headername ON public.eg_dss_response_kotpad (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_id ON public.eg_dss_response_kotpad (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_modulelevel ON public.eg_dss_response_kotpad (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_startdate ON public.eg_dss_response_kotpad (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_tenantid ON public.eg_dss_response_kotpad (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_timeinterval ON public.eg_dss_response_kotpad (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_valuetype ON public.eg_dss_response_kotpad (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kotpad_visualizationcode ON public.eg_dss_response_kotpad (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_kuchinda (
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
	CONSTRAINT pk_eg_dss_response_kuchinda PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_charttype ON public.eg_dss_response_kuchinda (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_city ON public.eg_dss_response_kuchinda (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_districtid ON public.eg_dss_response_kuchinda (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_enddate ON public.eg_dss_response_kuchinda (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_headername ON public.eg_dss_response_kuchinda (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_id ON public.eg_dss_response_kuchinda (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_modulelevel ON public.eg_dss_response_kuchinda (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_startdate ON public.eg_dss_response_kuchinda (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_tenantid ON public.eg_dss_response_kuchinda (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_timeinterval ON public.eg_dss_response_kuchinda (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_valuetype ON public.eg_dss_response_kuchinda (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_kuchinda_visualizationcode ON public.eg_dss_response_kuchinda (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_malkangiri (
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
	CONSTRAINT pk_eg_dss_response_malkangiri PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_charttype ON public.eg_dss_response_malkangiri (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_city ON public.eg_dss_response_malkangiri (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_districtid ON public.eg_dss_response_malkangiri (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_enddate ON public.eg_dss_response_malkangiri (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_headername ON public.eg_dss_response_malkangiri (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_id ON public.eg_dss_response_malkangiri (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_modulelevel ON public.eg_dss_response_malkangiri (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_startdate ON public.eg_dss_response_malkangiri (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_tenantid ON public.eg_dss_response_malkangiri (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_timeinterval ON public.eg_dss_response_malkangiri (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_valuetype ON public.eg_dss_response_malkangiri (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_malkangiri_visualizationcode ON public.eg_dss_response_malkangiri (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_nayagarh (
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
	CONSTRAINT pk_eg_dss_response_nayagarh PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_charttype ON public.eg_dss_response_nayagarh (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_city ON public.eg_dss_response_nayagarh (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_districtid ON public.eg_dss_response_nayagarh (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_enddate ON public.eg_dss_response_nayagarh (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_headername ON public.eg_dss_response_nayagarh (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_id ON public.eg_dss_response_nayagarh (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_modulelevel ON public.eg_dss_response_nayagarh (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_startdate ON public.eg_dss_response_nayagarh (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_tenantid ON public.eg_dss_response_nayagarh (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_timeinterval ON public.eg_dss_response_nayagarh (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_valuetype ON public.eg_dss_response_nayagarh (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nayagarh_visualizationcode ON public.eg_dss_response_nayagarh (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_nilgiri (
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
	CONSTRAINT pk_eg_dss_response_nilgiri PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_charttype ON public.eg_dss_response_nilgiri (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_city ON public.eg_dss_response_nilgiri (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_districtid ON public.eg_dss_response_nilgiri (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_enddate ON public.eg_dss_response_nilgiri (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_headername ON public.eg_dss_response_nilgiri (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_id ON public.eg_dss_response_nilgiri (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_modulelevel ON public.eg_dss_response_nilgiri (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_startdate ON public.eg_dss_response_nilgiri (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_tenantid ON public.eg_dss_response_nilgiri (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_timeinterval ON public.eg_dss_response_nilgiri (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_valuetype ON public.eg_dss_response_nilgiri (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nilgiri_visualizationcode ON public.eg_dss_response_nilgiri (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_nimapara (
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
	CONSTRAINT pk_eg_dss_response_nimapara PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_charttype ON public.eg_dss_response_nimapara (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_city ON public.eg_dss_response_nimapara (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_districtid ON public.eg_dss_response_nimapara (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_enddate ON public.eg_dss_response_nimapara (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_headername ON public.eg_dss_response_nimapara (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_id ON public.eg_dss_response_nimapara (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_modulelevel ON public.eg_dss_response_nimapara (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_startdate ON public.eg_dss_response_nimapara (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_tenantid ON public.eg_dss_response_nimapara (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_timeinterval ON public.eg_dss_response_nimapara (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_valuetype ON public.eg_dss_response_nimapara (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nimapara_visualizationcode ON public.eg_dss_response_nimapara (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_nabarangpur (
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
	CONSTRAINT pk_eg_dss_response_nabarangpur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_charttype ON public.eg_dss_response_nabarangpur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_city ON public.eg_dss_response_nabarangpur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_districtid ON public.eg_dss_response_nabarangpur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_enddate ON public.eg_dss_response_nabarangpur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_headername ON public.eg_dss_response_nabarangpur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_id ON public.eg_dss_response_nabarangpur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_modulelevel ON public.eg_dss_response_nabarangpur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_startdate ON public.eg_dss_response_nabarangpur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_tenantid ON public.eg_dss_response_nabarangpur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_timeinterval ON public.eg_dss_response_nabarangpur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_valuetype ON public.eg_dss_response_nabarangpur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nabarangpur_visualizationcode ON public.eg_dss_response_nabarangpur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_nuapada (
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
	CONSTRAINT pk_eg_dss_response_nuapada PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_charttype ON public.eg_dss_response_nuapada (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_city ON public.eg_dss_response_nuapada (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_districtid ON public.eg_dss_response_nuapada (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_enddate ON public.eg_dss_response_nuapada (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_headername ON public.eg_dss_response_nuapada (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_id ON public.eg_dss_response_nuapada (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_modulelevel ON public.eg_dss_response_nuapada (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_startdate ON public.eg_dss_response_nuapada (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_tenantid ON public.eg_dss_response_nuapada (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_timeinterval ON public.eg_dss_response_nuapada (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_valuetype ON public.eg_dss_response_nuapada (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_nuapada_visualizationcode ON public.eg_dss_response_nuapada (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_odagaon (
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
	CONSTRAINT pk_eg_dss_response_odagaon PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_charttype ON public.eg_dss_response_odagaon (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_city ON public.eg_dss_response_odagaon (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_districtid ON public.eg_dss_response_odagaon (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_enddate ON public.eg_dss_response_odagaon (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_headername ON public.eg_dss_response_odagaon (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_id ON public.eg_dss_response_odagaon (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_modulelevel ON public.eg_dss_response_odagaon (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_startdate ON public.eg_dss_response_odagaon (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_tenantid ON public.eg_dss_response_odagaon (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_timeinterval ON public.eg_dss_response_odagaon (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_valuetype ON public.eg_dss_response_odagaon (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_odagaon_visualizationcode ON public.eg_dss_response_odagaon (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_padampur (
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
	CONSTRAINT pk_eg_dss_response_padampur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_charttype ON public.eg_dss_response_padampur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_city ON public.eg_dss_response_padampur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_districtid ON public.eg_dss_response_padampur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_enddate ON public.eg_dss_response_padampur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_headername ON public.eg_dss_response_padampur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_id ON public.eg_dss_response_padampur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_modulelevel ON public.eg_dss_response_padampur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_startdate ON public.eg_dss_response_padampur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_tenantid ON public.eg_dss_response_padampur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_timeinterval ON public.eg_dss_response_padampur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_valuetype ON public.eg_dss_response_padampur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_padampur_visualizationcode ON public.eg_dss_response_padampur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_paralakhemundi (
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
	CONSTRAINT pk_eg_dss_response_paralakhemundi PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_charttype ON public.eg_dss_response_paralakhemundi (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_city ON public.eg_dss_response_paralakhemundi (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_districtid ON public.eg_dss_response_paralakhemundi (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_enddate ON public.eg_dss_response_paralakhemundi (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_headername ON public.eg_dss_response_paralakhemundi (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_id ON public.eg_dss_response_paralakhemundi (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_modulelevel ON public.eg_dss_response_paralakhemundi (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_startdate ON public.eg_dss_response_paralakhemundi (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_tenantid ON public.eg_dss_response_paralakhemundi (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_timeinterval ON public.eg_dss_response_paralakhemundi (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_valuetype ON public.eg_dss_response_paralakhemundi (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_paralakhemundi_visualizationcode ON public.eg_dss_response_paralakhemundi (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_patnagarh (
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
	CONSTRAINT pk_eg_dss_response_patnagarh PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_charttype ON public.eg_dss_response_patnagarh (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_city ON public.eg_dss_response_patnagarh (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_districtid ON public.eg_dss_response_patnagarh (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_enddate ON public.eg_dss_response_patnagarh (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_headername ON public.eg_dss_response_patnagarh (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_id ON public.eg_dss_response_patnagarh (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_modulelevel ON public.eg_dss_response_patnagarh (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_startdate ON public.eg_dss_response_patnagarh (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_tenantid ON public.eg_dss_response_patnagarh (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_timeinterval ON public.eg_dss_response_patnagarh (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_valuetype ON public.eg_dss_response_patnagarh (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_patnagarh_visualizationcode ON public.eg_dss_response_patnagarh (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_pattamundai (
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
	CONSTRAINT pk_eg_dss_response_pattamundai PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_charttype ON public.eg_dss_response_pattamundai (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_city ON public.eg_dss_response_pattamundai (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_districtid ON public.eg_dss_response_pattamundai (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_enddate ON public.eg_dss_response_pattamundai (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_headername ON public.eg_dss_response_pattamundai (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_id ON public.eg_dss_response_pattamundai (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_modulelevel ON public.eg_dss_response_pattamundai (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_startdate ON public.eg_dss_response_pattamundai (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_tenantid ON public.eg_dss_response_pattamundai (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_timeinterval ON public.eg_dss_response_pattamundai (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_valuetype ON public.eg_dss_response_pattamundai (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pattamundai_visualizationcode ON public.eg_dss_response_pattamundai (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_phulbani (
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
	CONSTRAINT pk_eg_dss_response_phulbani PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_charttype ON public.eg_dss_response_phulbani (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_city ON public.eg_dss_response_phulbani (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_districtid ON public.eg_dss_response_phulbani (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_enddate ON public.eg_dss_response_phulbani (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_headername ON public.eg_dss_response_phulbani (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_id ON public.eg_dss_response_phulbani (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_modulelevel ON public.eg_dss_response_phulbani (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_startdate ON public.eg_dss_response_phulbani (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_tenantid ON public.eg_dss_response_phulbani (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_timeinterval ON public.eg_dss_response_phulbani (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_valuetype ON public.eg_dss_response_phulbani (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_phulbani_visualizationcode ON public.eg_dss_response_phulbani (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_pipli (
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
	CONSTRAINT pk_eg_dss_response_pipli PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_charttype ON public.eg_dss_response_pipli (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_city ON public.eg_dss_response_pipli (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_districtid ON public.eg_dss_response_pipli (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_enddate ON public.eg_dss_response_pipli (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_headername ON public.eg_dss_response_pipli (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_id ON public.eg_dss_response_pipli (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_modulelevel ON public.eg_dss_response_pipli (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_startdate ON public.eg_dss_response_pipli (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_tenantid ON public.eg_dss_response_pipli (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_timeinterval ON public.eg_dss_response_pipli (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_valuetype ON public.eg_dss_response_pipli (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_pipli_visualizationcode ON public.eg_dss_response_pipli (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_polasara (
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
	CONSTRAINT pk_eg_dss_response_polasara PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_charttype ON public.eg_dss_response_polasara (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_city ON public.eg_dss_response_polasara (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_districtid ON public.eg_dss_response_polasara (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_enddate ON public.eg_dss_response_polasara (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_headername ON public.eg_dss_response_polasara (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_id ON public.eg_dss_response_polasara (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_modulelevel ON public.eg_dss_response_polasara (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_startdate ON public.eg_dss_response_polasara (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_tenantid ON public.eg_dss_response_polasara (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_timeinterval ON public.eg_dss_response_polasara (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_valuetype ON public.eg_dss_response_polasara (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_polasara_visualizationcode ON public.eg_dss_response_polasara (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_purushottampur (
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
	CONSTRAINT pk_eg_dss_response_purushottampur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_charttype ON public.eg_dss_response_purushottampur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_city ON public.eg_dss_response_purushottampur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_districtid ON public.eg_dss_response_purushottampur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_enddate ON public.eg_dss_response_purushottampur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_headername ON public.eg_dss_response_purushottampur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_id ON public.eg_dss_response_purushottampur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_modulelevel ON public.eg_dss_response_purushottampur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_startdate ON public.eg_dss_response_purushottampur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_tenantid ON public.eg_dss_response_purushottampur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_timeinterval ON public.eg_dss_response_purushottampur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_valuetype ON public.eg_dss_response_purushottampur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_purushottampur_visualizationcode ON public.eg_dss_response_purushottampur (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_rairangpur (
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
	CONSTRAINT pk_eg_dss_response_rairangpur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_charttype ON public.eg_dss_response_rairangpur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_city ON public.eg_dss_response_rairangpur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_districtid ON public.eg_dss_response_rairangpur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_enddate ON public.eg_dss_response_rairangpur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_headername ON public.eg_dss_response_rairangpur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_id ON public.eg_dss_response_rairangpur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_modulelevel ON public.eg_dss_response_rairangpur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_startdate ON public.eg_dss_response_rairangpur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_tenantid ON public.eg_dss_response_rairangpur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_timeinterval ON public.eg_dss_response_rairangpur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_valuetype ON public.eg_dss_response_rairangpur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rairangpur_visualizationcode ON public.eg_dss_response_rairangpur (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_rambha (
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
	CONSTRAINT pk_eg_dss_response_rambha PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_charttype ON public.eg_dss_response_rambha (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_city ON public.eg_dss_response_rambha (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_districtid ON public.eg_dss_response_rambha (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_enddate ON public.eg_dss_response_rambha (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_headername ON public.eg_dss_response_rambha (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_id ON public.eg_dss_response_rambha (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_modulelevel ON public.eg_dss_response_rambha (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_startdate ON public.eg_dss_response_rambha (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_tenantid ON public.eg_dss_response_rambha (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_timeinterval ON public.eg_dss_response_rambha (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_valuetype ON public.eg_dss_response_rambha (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rambha_visualizationcode ON public.eg_dss_response_rambha (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_ranpur (
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
	CONSTRAINT pk_eg_dss_response_ranpur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_charttype ON public.eg_dss_response_ranpur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_city ON public.eg_dss_response_ranpur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_districtid ON public.eg_dss_response_ranpur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_enddate ON public.eg_dss_response_ranpur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_headername ON public.eg_dss_response_ranpur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_id ON public.eg_dss_response_ranpur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_modulelevel ON public.eg_dss_response_ranpur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_startdate ON public.eg_dss_response_ranpur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_tenantid ON public.eg_dss_response_ranpur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_timeinterval ON public.eg_dss_response_ranpur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_valuetype ON public.eg_dss_response_ranpur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_ranpur_visualizationcode ON public.eg_dss_response_ranpur (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_rayagada (
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
	CONSTRAINT pk_eg_dss_response_rayagada PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_charttype ON public.eg_dss_response_rayagada (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_city ON public.eg_dss_response_rayagada (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_districtid ON public.eg_dss_response_rayagada (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_enddate ON public.eg_dss_response_rayagada (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_headername ON public.eg_dss_response_rayagada (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_id ON public.eg_dss_response_rayagada (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_modulelevel ON public.eg_dss_response_rayagada (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_startdate ON public.eg_dss_response_rayagada (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_tenantid ON public.eg_dss_response_rayagada (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_timeinterval ON public.eg_dss_response_rayagada (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_valuetype ON public.eg_dss_response_rayagada (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_rayagada_visualizationcode ON public.eg_dss_response_rayagada (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_redhakhol (
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
	CONSTRAINT pk_eg_dss_response_redhakhol PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_charttype ON public.eg_dss_response_redhakhol (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_city ON public.eg_dss_response_redhakhol (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_districtid ON public.eg_dss_response_redhakhol (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_enddate ON public.eg_dss_response_redhakhol (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_headername ON public.eg_dss_response_redhakhol (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_id ON public.eg_dss_response_redhakhol (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_modulelevel ON public.eg_dss_response_redhakhol (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_startdate ON public.eg_dss_response_redhakhol (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_tenantid ON public.eg_dss_response_redhakhol (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_timeinterval ON public.eg_dss_response_redhakhol (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_valuetype ON public.eg_dss_response_redhakhol (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_redhakhol_visualizationcode ON public.eg_dss_response_redhakhol (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_sonepur (
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
	CONSTRAINT pk_eg_dss_response_sonepur PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_charttype ON public.eg_dss_response_sonepur (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_city ON public.eg_dss_response_sonepur (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_districtid ON public.eg_dss_response_sonepur (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_enddate ON public.eg_dss_response_sonepur (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_headername ON public.eg_dss_response_sonepur (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_id ON public.eg_dss_response_sonepur (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_modulelevel ON public.eg_dss_response_sonepur (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_startdate ON public.eg_dss_response_sonepur (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_tenantid ON public.eg_dss_response_sonepur (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_timeinterval ON public.eg_dss_response_sonepur (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_valuetype ON public.eg_dss_response_sonepur (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sonepur_visualizationcode ON public.eg_dss_response_sonepur (visualizationcode);



CREATE TABLE IF NOT EXISTS public.eg_dss_response_soro (
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
	CONSTRAINT pk_eg_dss_response_soro PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_charttype ON public.eg_dss_response_soro (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_city ON public.eg_dss_response_soro (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_districtid ON public.eg_dss_response_soro (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_enddate ON public.eg_dss_response_soro (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_headername ON public.eg_dss_response_soro (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_id ON public.eg_dss_response_soro (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_modulelevel ON public.eg_dss_response_soro (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_startdate ON public.eg_dss_response_soro (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_tenantid ON public.eg_dss_response_soro (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_timeinterval ON public.eg_dss_response_soro (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_valuetype ON public.eg_dss_response_soro (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_soro_visualizationcode ON public.eg_dss_response_soro (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_surada (
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
	CONSTRAINT pk_eg_dss_response_surada PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_charttype ON public.eg_dss_response_surada (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_city ON public.eg_dss_response_surada (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_districtid ON public.eg_dss_response_surada (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_enddate ON public.eg_dss_response_surada (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_headername ON public.eg_dss_response_surada (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_id ON public.eg_dss_response_surada (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_modulelevel ON public.eg_dss_response_surada (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_startdate ON public.eg_dss_response_surada (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_tenantid ON public.eg_dss_response_surada (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_timeinterval ON public.eg_dss_response_surada (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_valuetype ON public.eg_dss_response_surada (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_surada_visualizationcode ON public.eg_dss_response_surada (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_sunabeda (
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
	CONSTRAINT pk_eg_dss_response_sunabeda PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_charttype ON public.eg_dss_response_sunabeda (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_city ON public.eg_dss_response_sunabeda (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_districtid ON public.eg_dss_response_sunabeda (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_enddate ON public.eg_dss_response_sunabeda (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_headername ON public.eg_dss_response_sunabeda (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_id ON public.eg_dss_response_sunabeda (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_modulelevel ON public.eg_dss_response_sunabeda (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_startdate ON public.eg_dss_response_sunabeda (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_tenantid ON public.eg_dss_response_sunabeda (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_timeinterval ON public.eg_dss_response_sunabeda (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_valuetype ON public.eg_dss_response_sunabeda (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_sunabeda_visualizationcode ON public.eg_dss_response_sunabeda (visualizationcode);

CREATE TABLE IF NOT EXISTS public.eg_dss_response_tarbha (
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
	CONSTRAINT pk_eg_dss_response_tarbha PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_charttype ON public.eg_dss_response_tarbha (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_city ON public.eg_dss_response_tarbha (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_districtid ON public.eg_dss_response_tarbha (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_enddate ON public.eg_dss_response_tarbha (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_headername ON public.eg_dss_response_tarbha (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_id ON public.eg_dss_response_tarbha (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_modulelevel ON public.eg_dss_response_tarbha (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_startdate ON public.eg_dss_response_tarbha (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_tenantid ON public.eg_dss_response_tarbha (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_timeinterval ON public.eg_dss_response_tarbha (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_valuetype ON public.eg_dss_response_tarbha (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tarbha_visualizationcode ON public.eg_dss_response_tarbha (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_titilagarh (
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
	CONSTRAINT pk_eg_dss_response_titilagarh PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_charttype ON public.eg_dss_response_titilagarh (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_city ON public.eg_dss_response_titilagarh (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_districtid ON public.eg_dss_response_titilagarh (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_enddate ON public.eg_dss_response_titilagarh (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_headername ON public.eg_dss_response_titilagarh (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_id ON public.eg_dss_response_titilagarh (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_modulelevel ON public.eg_dss_response_titilagarh (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_startdate ON public.eg_dss_response_titilagarh (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_tenantid ON public.eg_dss_response_titilagarh (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_timeinterval ON public.eg_dss_response_titilagarh (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_valuetype ON public.eg_dss_response_titilagarh (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_titilagarh_visualizationcode ON public.eg_dss_response_titilagarh (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_tusura (
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
	CONSTRAINT pk_eg_dss_response_tusura PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_charttype ON public.eg_dss_response_tusura (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_city ON public.eg_dss_response_tusura (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_districtid ON public.eg_dss_response_tusura (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_enddate ON public.eg_dss_response_tusura (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_headername ON public.eg_dss_response_tusura (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_id ON public.eg_dss_response_tusura (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_modulelevel ON public.eg_dss_response_tusura (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_startdate ON public.eg_dss_response_tusura (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_tenantid ON public.eg_dss_response_tusura (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_timeinterval ON public.eg_dss_response_tusura (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_valuetype ON public.eg_dss_response_tusura (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_tusura_visualizationcode ON public.eg_dss_response_tusura (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_udala (
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
	CONSTRAINT pk_eg_dss_response_udala PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_charttype ON public.eg_dss_response_udala (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_city ON public.eg_dss_response_udala (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_districtid ON public.eg_dss_response_udala (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_enddate ON public.eg_dss_response_udala (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_headername ON public.eg_dss_response_udala (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_id ON public.eg_dss_response_udala (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_modulelevel ON public.eg_dss_response_udala (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_startdate ON public.eg_dss_response_udala (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_tenantid ON public.eg_dss_response_udala (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_timeinterval ON public.eg_dss_response_udala (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_valuetype ON public.eg_dss_response_udala (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_udala_visualizationcode ON public.eg_dss_response_udala (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_umerkote (
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
	CONSTRAINT pk_eg_dss_response_umerkote PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_charttype ON public.eg_dss_response_umerkote (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_city ON public.eg_dss_response_umerkote (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_districtid ON public.eg_dss_response_umerkote (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_enddate ON public.eg_dss_response_umerkote (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_headername ON public.eg_dss_response_umerkote (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_id ON public.eg_dss_response_umerkote (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_modulelevel ON public.eg_dss_response_umerkote (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_startdate ON public.eg_dss_response_umerkote (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_tenantid ON public.eg_dss_response_umerkote (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_timeinterval ON public.eg_dss_response_umerkote (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_valuetype ON public.eg_dss_response_umerkote (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_umerkote_visualizationcode ON public.eg_dss_response_umerkote (visualizationcode);



CREATE TABLE IF NOT EXISTS public.eg_dss_response_bhubaneswar (
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
	CONSTRAINT pk_eg_dss_response_bhubaneswar PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_charttype ON public.eg_dss_response_bhubaneswar (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_city ON public.eg_dss_response_bhubaneswar (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_districtid ON public.eg_dss_response_bhubaneswar (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_enddate ON public.eg_dss_response_bhubaneswar (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_headername ON public.eg_dss_response_bhubaneswar (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_id ON public.eg_dss_response_bhubaneswar (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_modulelevel ON public.eg_dss_response_bhubaneswar (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_startdate ON public.eg_dss_response_bhubaneswar (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_tenantid ON public.eg_dss_response_bhubaneswar (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_timeinterval ON public.eg_dss_response_bhubaneswar (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_valuetype ON public.eg_dss_response_bhubaneswar (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswar_visualizationcode ON public.eg_dss_response_bhubaneswar (visualizationcode);


CREATE TABLE IF NOT EXISTS public.eg_dss_response_bhubaneswardevelopmentauthority (
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
	CONSTRAINT pk_eg_dss_response_bhubaneswardevelopmentauthority PRIMARY KEY (id) 
);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_charttype ON public.eg_dss_response_bhubaneswardevelopmentauthority (charttype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_city ON public.eg_dss_response_bhubaneswardevelopmentauthority (city);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_districtid ON public.eg_dss_response_bhubaneswardevelopmentauthority (districtid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_enddate ON public.eg_dss_response_bhubaneswardevelopmentauthority (enddate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_headername ON public.eg_dss_response_bhubaneswardevelopmentauthority (headername);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_id ON public.eg_dss_response_bhubaneswardevelopmentauthority (id);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_modulelevel ON public.eg_dss_response_bhubaneswardevelopmentauthority (modulelevel);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_startdate ON public.eg_dss_response_bhubaneswardevelopmentauthority (startdate);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_tenantid ON public.eg_dss_response_bhubaneswardevelopmentauthority (tenantid);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_timeinterval ON public.eg_dss_response_bhubaneswardevelopmentauthority (timeinterval);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_valuetype ON public.eg_dss_response_bhubaneswardevelopmentauthority (valuetype);
CREATE INDEX IF NOT EXISTS idx_eg_dss_response_bhubaneswardevelopmentauthority_visualizationcode ON public.eg_dss_response_bhubaneswardevelopmentauthority (visualizationcode);
