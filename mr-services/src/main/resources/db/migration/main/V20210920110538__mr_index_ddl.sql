CREATE INDEX IF NOT EXISTS index_eg_mr_application_accountid ON eg_mr_application (accountid);

CREATE INDEX IF NOT EXISTS index_eg_mr_application_tenantid ON eg_mr_application (tenantid);

CREATE INDEX IF NOT EXISTS index_eg_mr_application_applicationnumber ON eg_mr_application (applicationnumber);

CREATE INDEX IF NOT EXISTS index_eg_mr_application_mrnumber ON eg_mr_application (mrnumber);

CREATE INDEX IF NOT EXISTS index_eg_mr_application_applicationdate ON eg_mr_application (applicationdate);

CREATE INDEX IF NOT EXISTS index_eg_mr_marriageplace_mr_id on eg_mr_marriageplace(mr_id);

CREATE INDEX IF NOT EXISTS index_eg_mr_couple_mr_id on eg_mr_couple(mr_id);

CREATE INDEX IF NOT EXISTS index_eg_mr_address_mr_couple_id on eg_mr_address(mr_couple_id);

CREATE INDEX IF NOT EXISTS index_eg_mr_gaurdiandetails_mr_couple_id on eg_mr_gaurdiandetails(mr_couple_id);

CREATE INDEX IF NOT EXISTS index_eg_mr_witness_mr_couple_id on eg_mr_witness(mr_couple_id);

CREATE INDEX IF NOT EXISTS index_eg_mr_applicationdocument_mr_id on eg_mr_applicationdocument(mr_id);

CREATE INDEX IF NOT EXISTS index_eg_mr_verificationDocument_mr_id on eg_mr_verificationDocument(mr_id);

CREATE INDEX IF NOT EXISTS index_eg_mr_appointmentdetails_mr_id on eg_mr_appointmentdetails(mr_id);