CREATE TABLE IF NOT EXISTS eg_uis_feedback(
    id character varying(64) PRIMARY KEY,
    tenantId character varying(250) NOT NULL,
    module character varying(20) NOT NULL,
    rating bigint NOT NULL,
    comment character varying(1024),
    createdTime bigint NOT NULL,
    createdBy character varying(64) NOT NULL,
    lastModifiedTime bigint NOT NULL,
    lastModifiedBy character varying(64) NOT NULL,
    additionalDetails JSONB
);

CREATE INDEX IF NOT EXISTS index_eg_uis_feedback_tenantId ON eg_uis_feedback(tenantId);