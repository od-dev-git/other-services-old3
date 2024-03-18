package org.egov.report.repository.builder;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DCBQueryBuilder {
	
	public static final String QUERY_TO_GET_PROPERTIES = " select distinct pt.propertyid, pt.oldpropertyid, pta.ward from eg_pt_property pt "
			+ " inner join eg_pt_address pta on pta.propertyid=pt.id ";
	
	public static final String COLLECTIONS_QUERY = " select bill.consumercode, sum(pay.totalamountpaid) totalpaid from egcl_payment pay "
			+ "inner join egcl_paymentdetail pdtl on pdtl.paymentid=pay.id "
			+ "inner join egcl_bill bill on bill.id=pdtl.billid ";
	
	public static final String DEMANDS_QUERY = "select dmd.consumercode, sum(taxamount) taxamount, sum(collectionamount) collectionamount from egbs_demand_v1 dmd "
			+ "inner join egbs_demanddetail_v1 dtl on dtl.demandid=dmd.id ";

	public static final String ARREAR_DUE_QUERY = " select dmd.consumercode, sum(taxamount) taxamount, sum(collectionamount) collectionamount, sum(taxamount-collectionamount) due from egbs_demand_v1 dmd "
			+ "inner join egbs_demanddetail_v1 dtl on dtl.demandid=dmd.id ";
	
	public String getPropertiesDetialsQuery(String tenantId, Map<String, Object> preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(QUERY_TO_GET_PROPERTIES);

		if (!StringUtils.isEmpty(tenantId)) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pt.tenantId = :tenantId ");
			preparedStatementValues.put("tenantId", tenantId);

		}

		return selectQuery.toString();
	}
	
	private void addClauseIfRequired(Map<String, Object> preparedStatementValues, StringBuilder selectQuery) {

		if (preparedStatementValues.isEmpty())
			selectQuery.append(" WHERE ");
		else {
			selectQuery.append(" AND");
		}

	}

	public String getCollectionsQuery(String tenantId, Long startDate, Long endDate,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(COLLECTIONS_QUERY);
		
		if (!StringUtils.isEmpty(tenantId)) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pay.tenantId = :tenantId ");
			preparedStatementValues.put("tenantId", tenantId);

		}
		
		if (startDate != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pdtl.receiptdate >= :startDate ");
			preparedStatementValues.put("startDate", startDate);

		}
		
		if (endDate != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pdtl.receiptdate <= :endDate ");
			preparedStatementValues.put("endDate", endDate);

		}
		
		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" pdtl.businessservice = 'PT' ");
		
		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" pay.paymentstatus not in ('CANCELLED', 'DISHONOURED') ");
		
		selectQuery.append("group by bill.consumercode ");
		
		return selectQuery.toString();
	
	}

	public String getDemandsQuery(String tenantId, Long startDate, Long endDate,
			Map<String, Object> preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(DEMANDS_QUERY);

		if (!StringUtils.isEmpty(tenantId)) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" dmd.tenantid = :tenantId ");
			preparedStatementValues.put("tenantId", tenantId);

		}

		if (startDate != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" taxperiodfrom >= :taxperiodfrom ");
			preparedStatementValues.put("taxperiodfrom", startDate);

		}

		if (endDate != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" taxperiodto <= :taxperiodto ");
			preparedStatementValues.put("taxperiodto", endDate);

		}

		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" dmd.status !='CANCELLED' ");

		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" dmd.businessservice='PT' ");
		
		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" dtl.taxheadcode not in ('PT_ADVANCE_CARRYFORWARD', 'PT_TIME_REBATE','PT_TIME_PENALTY') ");

		selectQuery.append("group by dmd.consumercode ");

		return selectQuery.toString();

	}

	public String getArrearDueQuery(String tenantId, Long startDate, Map<String, Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(ARREAR_DUE_QUERY);

		if (!StringUtils.isEmpty(tenantId)) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" dmd.tenantid = :tenantId ");
			preparedStatementValues.put("tenantId", tenantId);

		}

		//start date for this FY is mapped with taxperiodto, cuz we want to fetch arrear data
		if (startDate != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" taxperiodto < :taxperiodto ");
			preparedStatementValues.put("taxperiodto", startDate);

		}

		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" dmd.status !='CANCELLED' ");

		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" dmd.businessservice='PT' ");
		
		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" dtl.taxheadcode not in ('PT_ADVANCE_CARRYFORWARD', 'PT_TIME_REBATE','PT_TIME_PENALTY') ");

		selectQuery.append("group by dmd.consumercode ");

		return selectQuery.toString();
	}

}
