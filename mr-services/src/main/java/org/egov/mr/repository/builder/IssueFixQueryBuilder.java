package org.egov.mr.repository.builder;

import java.util.List;
import java.util.Map;

import org.egov.mr.web.models.issuefix.IssueFix;
import org.springframework.stereotype.Component;

@Component
public class IssueFixQueryBuilder {
	
	public static final String CERTIFICATE_DELETION_QUERY = "update eg_mr_dscdetails "
			+ "set documenttype = null, documentid = null "
			+ "where applicationnumber = ?";
	
	public String getCertificateDeletionQuery() {
		return CERTIFICATE_DELETION_QUERY;
	}
	
	private static final String DELETE_DUPLICATE_DSC = "WITH CTE AS (SELECT *, ROW_NUMBER() OVER(PARTITION BY applicationnumber ORDER BY id) AS DuplicateCount FROM eg_mr_dscdetails where applicationnumber = ? and tenantid = ?) \r\n"
			+ "delete from eg_mr_dscdetails emd where applicationnumber = ? and id not in \r\n"
			+ "(SELECT id FROM CTE where DuplicateCount = 1)";

	private static final String DSC_SEARCH = "select id from eg_mr_dscdetails emd ";

	public String searchDscQuery() {
		return DELETE_DUPLICATE_DSC;
	}

	public String getDSC(IssueFix issueFix, List<Object> preparedStmtList) {


		StringBuilder builder = new StringBuilder(DSC_SEARCH);

		addClauseIfRequired(preparedStmtList, builder);
		builder.append(" emd.applicationnumber =? ");
		preparedStmtList.add(issueFix.getApplicationNo());

		addClauseIfRequired(preparedStmtList, builder);
		builder.append(" emd.tenantid =? ");
		preparedStmtList.add(issueFix.getTenantId());

		return builder.toString();		
	}
	
	private void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}
	
	

}
