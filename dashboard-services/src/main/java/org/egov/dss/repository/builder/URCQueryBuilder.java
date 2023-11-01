package org.egov.dss.repository.builder;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Map;

import org.egov.dss.model.DemandSearchCriteria;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.model.TargetSearchCriteria;
import org.egov.dss.model.UrcSearchCriteria;
import org.egov.dss.model.WaterSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class URCQueryBuilder {

	public static final String TOTAL_COLLECTION_QUERY = " select COALESCE(sum(py.totalamountpaid),0) from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id   ";
	
	public static final String ULBS_UNDER_URC = " select count(distinct hrms.tenantid)  from eg_hrms_employee hrms ";
	
	public static final String JALSATHI_ONBOARDED = " select count(distinct hrms.id)  from eg_hrms_employee hrms ";
	
	public static final String URC_PROPERTIES_PAID = " select count(distinct bill.consumercode) as totalamt from egcl_payment py inner join egcl_paymentdetail pyd on pyd.paymentid = py.id inner join egcl_bill bill on bill.id = pyd.billid  ";
	
	public static final String URC_MONTH_WISE_COLLECTION = " WITH months AS (SELECT DATE_TRUNC('month', MIN(TO_TIMESTAMP(fromdate / 1000 + 19800) at TIME zone 'UTC')) as start_month, "
			+ "DATE_TRUNC('month', MAX(TO_TIMESTAMP(todate / 1000 + 19800) at TIME zone 'UTC')) as end_month),monthly_counts AS ( "
			+ "SELECT DATE_TRUNC('month', TO_TIMESTAMP(pyd.receiptdate / 1000 + 19800 ) at TIME zone 'UTC') as month_start,COALESCE(SUM(py.totalamountpaid), 0) as totalamt "
			+ "FROM egcl_payment py "
			+ "INNER JOIN egcl_paymentdetail pyd ON pyd.paymentid = py.id ";
	
	public static final String URC_MONTH_WISE_COLLECTION_SUB_QUERY = " all_months AS (select generate_series(start_month, end_month, interval '1 month') as month_range FROM months) "
			+ "SELECT TO_CHAR(am.month_range, 'Mon-YYYY') as name, COALESCE(totalamt, 0) AS value FROM all_months am LEFT JOIN "
			+ "monthly_counts mc ON am.month_range = mc.month_start ";
	
	public static final String MONTH_YEAR_QUERY = " WITH  months AS (SELECT "
			+ " DATE_TRUNC('month', MIN(TO_TIMESTAMP(fromdate / 1000 + 19800)  AT TIME ZONE 'UTC')) AS start_month, "
			+ " DATE_TRUNC('month', MAX(TO_TIMESTAMP(todate / 1000 + 19800) AT TIME ZONE 'UTC')) AS end_month )  ";
	
	public static final String TARGET_COLLECTION_QUERY = " select COALESCE (sum(budgetproposedformunicipalcorporation),0) from state.eg_dss_target edt  ";
	
    public static final String CURRENT_DEMAND_QUERY = " select coalesce(sum(amount), 0) as amount from state.eg_dss_demand ";
	
	public static final String ARREAR_DEMAND_QUERY = " select coalesce(sum(amount), 0) - coalesce(sum(collectionamount), 0) as amount from state.eg_dss_demand ";
	
	public static final String JALSATHI_WISE_COLLECTION = " select usr.uuid as name,COALESCE(sum(py.totalamountpaid),0) as value from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id "
			+ "inner join eg_hrms_employee hrms on py.createdby = hrms.id:: character varying "
			+ "inner join eg_user usr on hrms.uuid = usr.uuid ";
	
	public static final String PROPERTIES_COVER_BY_JALSATHI = " select usr.uuid as name,count(bill.consumercode)  as value from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id "
			+ "inner join egcl_bill bill on bill.id = pyd.billid "
			+ "inner join eg_hrms_employee hrms on py.createdby = hrms.id:: character varying "
			+ "inner join eg_user usr on hrms.uuid = usr.uuid ";
	
	private static final String HRMS_QUERY = "select id, tenantid from eg_hrms_employee ";
	
	public static final String TOTAL_PROPERTIES_SQL = " select count(distinct propertyid) from eg_pt_property epaa ";
	
	public static final String WATER_ACTIVE_CONNECTIONS_COUNT = " select count(*) from eg_ws_connection  conn "
			+ "inner join eg_ws_service ws on ws.connection_id = conn.id ";
	
	public static final String ACTIVE_JALSATHI = " select count(distinct hrms.code)  as totalamt from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id "
			+ "inner join egcl_bill bill on bill.id = pyd.billid "
			+ "inner join eg_hrms_employee hrms on py.createdby = hrms.id:: character varying "
			+ "inner join eg_user usr on hrms.uuid = usr.uuid ";
	
	public static final String PROPERTY_COVERED_BY_JALSATHI = " select count(distinct bill.consumercode)  as totalamt from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id "
			+ "inner join egcl_bill bill on bill.id = pyd.billid "
			+ "inner join eg_hrms_employee hrms on py.createdby = hrms.id:: character varying "
			+ "inner join eg_user usr on hrms.uuid = usr.uuid ";
	
	public static final String COLLECTION_BY_JALSATHI = " select COALESCE(sum(py.totalamountpaid),0)  as totalamt from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id "
			+ "inner join egcl_bill bill on bill.id = pyd.billid "
			+ "inner join eg_hrms_employee hrms on py.createdby = hrms.id:: character varying "
			+ "inner join eg_user usr on hrms.uuid = usr.uuid ";


	public static String getTotalCollection(PaymentSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_COLLECTION_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}
	
	public static String getUlbsUnderUrc(UrcSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(ULBS_UNDER_URC);
		addWhereClauseForHrms(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}
	
	public static String jalSathiOnboarded(UrcSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(JALSATHI_ONBOARDED);
		addWhereClauseForHrms(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}
	
	public static String urcPropertiesPaid(PaymentSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(URC_PROPERTIES_PAID);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}
	
	
	public static String activeJalsathi(PaymentSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(ACTIVE_JALSATHI);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}
	
	public static String propertyCoveredByJalsathi(PaymentSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PROPERTY_COVERED_BY_JALSATHI);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}
	
	public static String collectionByJalsathi(PaymentSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(COLLECTION_BY_JALSATHI);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}
	
	public String getMonthWiseCollection(PaymentSearchCriteria paymentSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder monthQuery = new StringBuilder(URC_MONTH_WISE_COLLECTION);
		String query = monthQuery.toString();
		query = query.replaceAll("fromdate", paymentSearchCriteria.getFromDate().toString());
		query = query.replaceAll("todate", paymentSearchCriteria.getToDate().toString());
		StringBuilder monthQueryModified = new StringBuilder(query);
		addWhereClause(monthQueryModified, preparedStatementValues, paymentSearchCriteria);
		addGroupByClause(monthQueryModified, " month_start ), ");
		monthQueryModified.append(URC_MONTH_WISE_COLLECTION_SUB_QUERY);
		addOrderByClause(monthQueryModified, " am.month_range ");
		return monthQueryModified.toString();
	}
	
	public static String getTargetCollection(TargetSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TARGET_COLLECTION_QUERY);
		return addWhereClauseForTarget(selectQuery, preparedStatementValues, criteria);
	}
	
	public static String jalSathiWiseCollection(PaymentSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(JALSATHI_WISE_COLLECTION);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery," usr.uuid ");
		return selectQuery.toString();
	}
	
	public static String propertiesCoverByJalsathi(PaymentSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PROPERTIES_COVER_BY_JALSATHI);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery," usr.uuid ");
		return selectQuery.toString();
	}
	
	public static String getActiveConnectionCount(WaterSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(WATER_ACTIVE_CONNECTIONS_COUNT);
		return addWhereClauseForWaterService(selectQuery, preparedStatementValues, criteria);
	}
	
	public String getEmployeeBaseTenantQuery(List<Long> userIds, List<Object> preparedStatement) {
		StringBuilder builder = new StringBuilder(HRMS_QUERY);
		String userId = " id in (";
		addClauseIfRequired(preparedStatement, builder);
		builder.append(userId).append(createQuery(userIds)).append(" )");
		addToPreparedStatement(preparedStatement, userIds);
		return builder.toString();
	}

	private void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}

	private String createQuery(List<Long> ids) {
		StringBuilder builder = new StringBuilder();
		int length = ids.size();
		for (int i = 0; i < length; i++) {
			builder.append(" ?");
			if (i != length - 1)
				builder.append(",");
		}
		return builder.toString();
	}
	
	private void addToPreparedStatement(List<Object> preparedStatement, List<Long> ids) {
		preparedStatement.addAll(ids);
	}
	
	private static String addWhereClauseForTarget(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			TargetSearchCriteria searchCriteria) {

        if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append("  edt.tenantidformunicipalcorporation IN ( :tenantId )");
			preparedStatementValues.put("tenantId",searchCriteria.getTenantIds());
		}

		if (!CollectionUtils.isEmpty(searchCriteria.getBusinessServices())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" edt.businessService IN (:businessService)  ");
			preparedStatementValues.put("businessService", searchCriteria.getBusinessServices());
		}
		
		if (!StringUtils.isEmpty(searchCriteria.getFinancialYear())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" edt.financialyear = :financialYear  ");
			preparedStatementValues.put("financialYear", searchCriteria.getFinancialYear());
		}

		return selectQuery.toString();

	}
	
	public static String getCurrentDemand(DemandSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(CURRENT_DEMAND_QUERY);
		addWhereClauseForDemand(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}
	
	public static String getArrearDemand(DemandSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(ARREAR_DEMAND_QUERY);
		addWhereClauseForDemand(selectQuery, preparedStatementValues, criteria);
		return selectQuery.toString();
	}
	
	public String getTotalPropertiesCountQuery(PropertySerarchCriteria propertySearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_PROPERTIES_SQL);
		return addWhereClauseForProperty(selectQuery, preparedStatementValues, propertySearchCriteria,true);
	}

	private static void addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			PaymentSearchCriteria searchCriteria) {

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.tenantId in ( :tenantId )");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
		}

		if (!CollectionUtils.isEmpty(searchCriteria.getIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.id IN (:id)  ");
			preparedStatementValues.put("id", searchCriteria.getIds());
		}

		if (searchCriteria.getReceiptNumbers() != null && !searchCriteria.getReceiptNumbers().isEmpty()) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pyd.receiptNumber IN (:receiptnumber)  ");
			preparedStatementValues.put("receiptnumber", searchCriteria.getReceiptNumbers());
		}

		if (!CollectionUtils.isEmpty(searchCriteria.getStatus())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" UPPER(py.paymentstatus) not in (:status)");
			preparedStatementValues.put("status",
					searchCriteria.getStatus().stream().map(String::toUpperCase).collect(toSet()));
		}

		if (!CollectionUtils.isEmpty(searchCriteria.getInstrumentStatus())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" UPPER(py.instrumentStatus) in (:instrumentStatus)");
			preparedStatementValues.put("instrumentStatus",
					searchCriteria.getInstrumentStatus().stream().map(String::toUpperCase).collect(toSet()));
		}

		if (!CollectionUtils.isEmpty(searchCriteria.getPaymentModes())) {

			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" UPPER(py.paymentMode) in (:paymentMode)");
			preparedStatementValues.put("paymentMode",
					searchCriteria.getPaymentModes().stream().map(String::toUpperCase).collect(toSet()));
		}

		if (!StringUtils.isEmpty(searchCriteria.getMobileNumber())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.mobileNumber = :mobileNumber");
			preparedStatementValues.put("mobileNumber", searchCriteria.getMobileNumber());
		}

		if (!StringUtils.isEmpty(searchCriteria.getTransactionNumber())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.transactionNumber = :transactionNumber");
			preparedStatementValues.put("transactionNumber", searchCriteria.getTransactionNumber());
		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pyd.receiptdate >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pyd.receiptdate <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}

		if (!CollectionUtils.isEmpty(searchCriteria.getPayerIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.payerid IN (:payerid)  ");
			preparedStatementValues.put("payerid", searchCriteria.getPayerIds());
		}

		if (!CollectionUtils.isEmpty(searchCriteria.getBusinessServices())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pyd.businessService IN (:businessService)  ");
			preparedStatementValues.put("businessService", searchCriteria.getBusinessServices());
		}

		if (!CollectionUtils.isEmpty(searchCriteria.getConsumerCodes())) {

			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bill.consumerCode in (:consumerCodes)");
			preparedStatementValues.put("consumerCodes", searchCriteria.getConsumerCodes());
		}

		if (!CollectionUtils.isEmpty(searchCriteria.getBillIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pyd.billid in (:billid)");
			preparedStatementValues.put("billid", searchCriteria.getBillIds());
		}

		if (!CollectionUtils.isEmpty(searchCriteria.getPaymentModes())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.paymentmode in (:paymentMode)");
			preparedStatementValues.put("paymentMode", searchCriteria.getPaymentModes());
		}

		if (!StringUtils.isEmpty(searchCriteria.getPropertyStatus())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pt.status = :propertyStatus");
			preparedStatementValues.put("propertyStatus", searchCriteria.getPropertyStatus());
		}
		
		if (searchCriteria.getIsJalSathi() != null && searchCriteria.getIsJalSathi().equals(Boolean.TRUE)) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" hrms.code like (:empcode)");
			preparedStatementValues.put("empcode", "SUJOG_JAL_%");
		}

		if (!StringUtils.isEmpty(searchCriteria.getExcludedTenant())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.tenantid != :excludedTenant");
			preparedStatementValues.put("excludedTenant", searchCriteria.getExcludedTenant());
		}

	}
	
	private static void addWhereClauseForHrms(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			UrcSearchCriteria searchCriteria) {
		
		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" hrms.tenantId in ( :tenantId )");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
		}
		
		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" hrms.createddate >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" hrms.createddate <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getHrmsCode() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" hrms.code like (:hrmsCode)");
			preparedStatementValues.put("hrmsCode", searchCriteria.getHrmsCode());
		}
		
		if (searchCriteria.getIsActive() == Boolean.TRUE) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" hrms.active = true ");
			preparedStatementValues.put("isActive", searchCriteria.getIsActive());
		}
		
		if (!StringUtils.isEmpty(searchCriteria.getExcludedTenant())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" hrms.tenantid != :excludedTenant");
			preparedStatementValues.put("excludedTenant", searchCriteria.getExcludedTenant());
		}

		
	}

	private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}
	
	private static void addGroupByClause(StringBuilder query,String columnName) {
		query.append(" GROUP BY " + columnName);
    }

    private static void addOrderByClause(StringBuilder query,String columnName) {
    	query.append(" ORDER BY " + columnName);
    }
    
	private static void addWhereClauseForDemand(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			DemandSearchCriteria searchCriteria) {

		if (!StringUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" tenantId in ( :tenantId ) ");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
		}

		if (!CollectionUtils.isEmpty(searchCriteria.getBusinessServices())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" businessservice in ( :businessService ) ");
			preparedStatementValues.put("businessService", searchCriteria.getBusinessServices());
		}

		if (!StringUtils.isEmpty(searchCriteria.getFinancialYear())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getIsArrearDemand() == Boolean.TRUE) {
				selectQuery.append(" financialyear < :financialYear ");
			} else {
				selectQuery.append(" financialyear = :financialYear ");
			}
			preparedStatementValues.put("financialYear", searchCriteria.getFinancialYear());
		}

		if (!StringUtils.isEmpty(searchCriteria.getExcludedTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" tenantid != :excludedTenant");
			preparedStatementValues.put("excludedTenant", searchCriteria.getExcludedTenantId());
		}

	}
	
	private static String addWhereClauseForProperty(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			PropertySerarchCriteria searchCriteria,boolean isULBPerformance) {

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.tenantId in ( :tenantIds )");
			preparedStatementValues.put("tenantIds",searchCriteria.getTenantIds());
		}

       if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.createdtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if(isULBPerformance = true) {
				selectQuery.append(" epaa.lastmodifiedtime <= :toDate");
			}else {
				selectQuery.append(" epaa.createdtime <= :toDate");	
			}
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
	    }
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.lastmodifiedtime - epaa.createdtime < " + searchCriteria.getSlaThreshold());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.status = :status");
			preparedStatementValues.put("status", searchCriteria.getStatus());
	    }
		
		if (searchCriteria.getIsPropertyAssessed() == Boolean.TRUE) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.lastmodifiedtime != epaa.createdtime ");			
	    }
		
		if (searchCriteria.getStatusNotIn() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.status not in ( :statusNotIn) ");
			preparedStatementValues.put("statusNotIn", searchCriteria.getStatusNotIn());
	    }
		
		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
	    }
		
		if (searchCriteria.getCreationReasons() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" epaa.creationreason = :creationReason");
			preparedStatementValues.put("creationReason", searchCriteria.getCreationReasons());
	    }
		
		return selectQuery.toString();
	
	}
	
	private static String addWhereClauseForWaterService(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			WaterSearchCriteria searchCriteria) {
		
		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
		    addClauseIfRequired(preparedStatementValues, selectQuery);
		    selectQuery.append(" conn.tenantId in (:tenantId)");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
		}

		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ws.connectionExecutionDate >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ws.connectionExecutionDate <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}
		
		if (searchCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.applicationstatus = :status");
			preparedStatementValues.put("status", searchCriteria.getStatus());
		}
		
		if (searchCriteria.getIsOldApplication() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.isoldapplication = :isoldapplication");
			preparedStatementValues.put("isoldapplication", searchCriteria.getIsOldApplication());
		}
		
		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.lastmodifiedtime - conn.createdtime < " + searchCriteria.getSlaThreshold());
			
		}
		
		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" conn.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}
		
		if (searchCriteria.getConnectionType() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ws.connectiontype = :connectiontype");
			preparedStatementValues.put("connectiontype", searchCriteria.getConnectionType());
		}

		if (searchCriteria.getConnectionFacility() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ws.connectionfacility = :connectionfacility");
			preparedStatementValues.put("connectionfacility", searchCriteria.getConnectionFacility());
		}
		
		return selectQuery.toString();

	}

}
