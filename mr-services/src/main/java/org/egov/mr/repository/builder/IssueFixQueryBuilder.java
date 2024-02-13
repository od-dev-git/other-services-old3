package org.egov.mr.repository.builder;

import java.util.List;
import java.util.Map;

import org.egov.mr.web.models.issuefix.IssueFix;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

	public static final String DELETE_PROCESS_INSTANCE_RECORD ="delete from public.eg_wf_processinstance_v2 ";


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

	public String getApplicationStatusMismatchIssueQuery(List<String> idList,List<Object> preparedStatementList){

		StringBuilder deleteQuery = new StringBuilder(DELETE_PROCESS_INSTANCE_RECORD);

		if(!CollectionUtils.isEmpty(idList)){
			addClauseIfRequired(preparedStatementList,deleteQuery);
			deleteQuery.append("id in (").append(createQuery(idList)).append(")");
			preparedStatementList.add(idList);
		}


		return deleteQuery.toString();
	}


	private String createQuery(List<String> ids) {
		StringBuilder builder = new StringBuilder();
		int length = ids.size();
		for (int i = 0; i < length; i++) {
			builder.append(" ?");
			if (i != length - 1)
				builder.append(",");
		}
		return builder.toString();
	}



}
