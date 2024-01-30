package org.egov.mr.repository.builder;

import org.springframework.stereotype.Component;

@Component
public class IssueFixQueryBuilder {
	
	public static final String CERTIFICATE_DELETION_QUERY = "update eg_mr_dscdetails "
			+ "set documenttype = null, documentid = null "
			+ "where applicationnumber = ?";
	
	public String getCertificateDeletionQuery() {
		return CERTIFICATE_DELETION_QUERY;
	}

}
