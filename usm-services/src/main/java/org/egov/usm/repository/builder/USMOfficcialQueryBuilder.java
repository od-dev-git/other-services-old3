package org.egov.usm.repository.builder;

import java.util.List;

import javax.validation.Valid;

import org.egov.usm.web.model.USMOfficialSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class USMOfficcialQueryBuilder {

	private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
		if (preparedStmtList.isEmpty()) {
			query.append(" WHERE ");
		} else {
			query.append(" AND ");
		}
	}

	public String getUSMOfficialSearchQuery(@Valid USMOfficialSearchCriteria searchCriteria,
			List<Object> preparedStmtList) {
		StringBuilder query = new StringBuilder(
				"select department.id ,department.\"role\", department.category,department.tenantid,department.ward,department.slumcode,department.assigned,department.createdtime,department.createdby,department.lastmodifiedtime,department.lastmodifiedby from eg_usm_dept_mapping department ");

		if (!ObjectUtils.isEmpty(searchCriteria.getSlumcode())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" department.slumcode = ? ");
			preparedStmtList.add(searchCriteria.getSlumcode());
		}

		if (!ObjectUtils.isEmpty(searchCriteria.getTenantId())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" department.tenantid = ? ");
			preparedStmtList.add(searchCriteria.getTenantId());
		}
		if (!ObjectUtils.isEmpty(searchCriteria.getWard())) {
			addClauseIfRequired(query, preparedStmtList);
			query.append(" department.ward = ? ");
			preparedStmtList.add(searchCriteria.getWard());
		}

		return query.toString();
	}

}
