package org.egov.dss.repository.builder;

import static java.util.stream.Collectors.toSet;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.TargetSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


@Component
public class PaymentQueryBuilder {

	public static final String SELECT_PAYMENT_SQL = "SELECT py.*,pyd.*,"
			+ "py.id as py_id,py.tenantId as py_tenantId,py.totalAmountPaid as py_totalAmountPaid,py.createdBy as py_createdBy,py.createdtime as py_createdtime,"
			+ "py.lastModifiedBy as py_lastModifiedBy,py.lastmodifiedtime as py_lastmodifiedtime,py.additionalDetails as py_additionalDetails,"
			+ "pyd.id as pyd_id, pyd.tenantId as pyd_tenantId, pyd.manualreceiptnumber as manualreceiptnumber,pyd.manualreceiptdate as manualreceiptdate, pyd.createdBy as pyd_createdBy,pyd.createdtime as pyd_createdtime,pyd.lastModifiedBy as pyd_lastModifiedBy,"
			+ "pyd.lastmodifiedtime as pyd_lastmodifiedtime,pyd.additionalDetails as pyd_additionalDetails"
			+ " FROM egcl_payment py  " + " INNER JOIN egcl_paymentdetail pyd ON pyd.paymentid = py.id ";
	
	public static final String ID_QUERY = "SELECT DISTINCT py.id as id,py.transactiondate as date " +
            " FROM egcl_payment py  " +
            " INNER JOIN egcl_paymentdetail pyd ON pyd.paymentid = py.id " +
            " INNER JOIN egcl_bill bill ON bill.id = pyd.billid " +
            " INNER JOIN egcl_billdetial bd ON bd.billid = bill.id " ;
	
	public static final String BILL_BASE_QUERY = "SELECT b.id AS b_id, b.tenantid AS b_tenantid, b.iscancelled AS b_iscancelled, b.businessservice AS b_businessservice, "
			+ "b.billnumber AS b_billnumber, b.billdate AS b_billdate, b.consumercode AS b_consumercode, b.createdby AS b_createdby, b.status as b_status, b.minimumamounttobepaid AS b_minimumamounttobepaid, "
			+ "b.totalamount AS b_totalamount, b.partpaymentallowed AS b_partpaymentallowed, b.isadvanceallowed as b_isadvanceallowed, "
			+ "b.collectionmodesnotallowed AS b_collectionmodesnotallowed, b.createdtime AS b_createdtime, b.lastmodifiedby AS b_lastmodifiedby, "
			+ "b.lastmodifiedtime AS b_lastmodifiedtime, bd.id AS bd_id, bd.billid AS bd_billid, bd.tenantid AS bd_tenantid, bd.demandid, "
			+ "bd.fromperiod, bd.toperiod, bd.billdescription AS bd_billdescription, bd.displaymessage AS bd_displaymessage, bd.amount AS bd_amount, bd.amountpaid AS bd_amountpaid, "
			+ "bd.callbackforapportioning AS bd_callbackforapportioning, bd.expirydate AS bd_expirydate, ad.id AS ad_id, ad.tenantid AS ad_tenantid, "
			+ "ad.billdetailid AS ad_billdetailid, ad.order AS ad_order, ad.amount AS ad_amount, ad.adjustedamount AS ad_adjustedamount, "
			+ "ad.taxheadcode AS ad_taxheadcode, ad.demanddetailid as ad_demanddetailid, ad.isactualdemand AS ad_isactualdemand, b.additionaldetails as b_additionaldetails,  "
			+ "bd.additionaldetails as bd_additionaldetails,  ad.additionaldetails as ad_additionaldetails "
			+ "FROM egcl_bill b LEFT OUTER JOIN egcl_billdetial bd ON b.id = bd.billid AND b.tenantid = bd.tenantid "
			+ "LEFT OUTER JOIN egcl_billaccountdetail ad ON bd.id = ad.billdetailid AND bd.tenantid = ad.tenantid "
			+ "WHERE b.id IN (:id);"; 
	
	public static final String PT_TAXHEAD_WISE_COLLECTION_QUERY = "select tenantid, "
			+ "coalesce(sum(holdingTax), 0) as holdingTaxCollection,coalesce(sum(otherDues), 0) as otherDuesCollection,coalesce(sum(drainageTax), 0) as drainageTaxCollection, "
			+ "coalesce(sum(waterTax), 0) as waterTaxCollection,coalesce(sum(serviceTax), 0) as serviceTaxCollection,coalesce(sum(latrineTax), 0) as latrineTaxCollection, "
			+ "coalesce(sum(solidWasteUserCharge), 0) as solidWasteUserChargeCollection,coalesce(sum(parkingTax), 0) as parkingTaxCollection, coalesce(sum(usageExcemption), 0) as usageExcemptionCollection, "
			+ "coalesce(sum(lightTax), 0) as lightTaxCollection,coalesce(sum(ownershipExcemption), 0) as ownershipExcemptionCollection,coalesce(sum(penalty), 0) as penaltyCollection, "
			+ "coalesce(sum(rebate), 0) as rebateCollection,coalesce(sum(interest), 0) as interestCollection "
			+ "from  "
			+ "(select py.tenantid, "
			+ "case when bad.taxheadcode='PT_HOLDING_TAX' then bad.adjustedamount end as holdingTax,case when bad.taxheadcode='PT_OTHER_DUES' then bad.adjustedamount end as otherDues, "
			+ "case when bad.taxheadcode='PT_DRAINAGE_TAX' then bad.adjustedamount end as drainageTax, case when bad.taxheadcode='PT_WATER_TAX' then bad.adjustedamount end as waterTax, "
			+ "case when bad.taxheadcode='PT_SERVICE_TAX' then bad.adjustedamount end as serviceTax,case when bad.taxheadcode='PT_LATRINE_TAX' then bad.adjustedamount end as latrineTax, "
			+ "case when bad.taxheadcode='PT_SOLID_WASTE_USER_CHARGES' then bad.adjustedamount end as solidWasteUserCharge,case when bad.taxheadcode='PT_PARKING_TAX' then bad.adjustedamount end as parkingTax, "
			+ "case when bad.taxheadcode='PT_USAGE_EXCEMPTION' then bad.adjustedamount end as usageExcemption,case when bad.taxheadcode='PT_LIGHT_TAX' then bad.adjustedamount end as lightTax, "
			+ "case when bad.taxheadcode='PT_OWNERSHIP_EXCEMPTION' then bad.adjustedamount end as ownershipExcemption,case when bad.taxheadcode in ('PT_PENALTY', 'PT_TIME_PENALTY') then bad.adjustedamount end as penalty, "
			+ "case when bad.taxheadcode='PT_TIME_REBATE' then bad.adjustedamount end as rebate, case when bad.taxheadcode='PT_INTEREST' then bad.adjustedamount end as interest "
			+ "from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id  "
			+ "inner join egcl_billdetial bdtl on bdtl.billid = pyd.billid  "
			+ "inner join egcl_billaccountdetail bad on bad.billdetailid = bdtl.id "; 

	public static final String USAGE_TYPE_QUERY = "SELECT epp.propertyid, epp.usagecategory  "
			+ "FROM eg_pt_property epp ";
	
	public static final String ALL_BUSINESS_SERVICE = " 'PT','WS','TL','MR','WS.ONE_TIME_FEE','SW','SW.ONE_TIME_FEE',"
	        +" 'PT.MUTATION','BPA.NC_APP_FEE','BPA.NC_SAN_FEE','BPA.NC_OC_APP_FEE','BPA.NC_OC_SAN_FEE' "; 
	
	public static final String TARGET_COLLECTION_QUERY = " select COALESCE (sum(budgetproposedformunicipalcorporation),0) from eg_dss_target edt  ";
	
	public static final String TOTAL_COLLECTION_QUERY = " select COALESCE(sum(py.totalamountpaid),0) from egcl_payment py "
			                                          + "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id   ";
			
	public static final String TRANSACTION_COUNT_QUERY = " select count(py.transactionnumber) from egcl_payment py "
            + "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id   ";
	
	public static final String TENANT_WISE_COLLECTION_QUERY = " select py.tenantid, COALESCE(sum(py.totalamountpaid),0) as totalamt from egcl_payment py "
            + "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id   ";
	
	public static final String TENANT_WISE_TARGET_COLLECTION_QUERY = " select tenantidformunicipalcorporation as tenantid,COALESCE (sum(budgetproposedformunicipalcorporation),0) as totalamt from eg_dss_target edt  ";
	
	public static final String TENANT_WISE_TRANSACTION_QUERY = " select py.tenantid, COALESCE(count(py.transactionnumber),0) as totalamt from egcl_payment py "
            + "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id   ";
	
	public static final String CUMULATIVE_COLLECTION_QUERY = " select to_char(monthYear, 'Mon-YYYY') as name, sum(totalCollection) over (order by monthYear asc rows between unbounded preceding and current row) as value "
			+ "from (select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(pyd.receiptdate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(pyd.receiptdate/1000))),'DD-MM-YYYY') as monthYear,sum(py.totalamountpaid) as totalCollection from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id  ";
	
	public static String getPaymentSearchQuery(List<String> ids, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(SELECT_PAYMENT_SQL);
		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" py.id IN (:id)  ");
		preparedStatementValues.put("id", ids);
		return addOrderByClause(selectQuery);
	}

	public String getIdQuery(PaymentSearchCriteria searchCriteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(ID_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, searchCriteria);
		StringBuilder finalQuery = addWrapperQuery(selectQuery);
		return finalQuery.toString();
	}

	private static void addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			PaymentSearchCriteria searchCriteria) {

		if (StringUtils.isNotBlank(searchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getTenantId().split("\\.").length > 1) {
				selectQuery.append(" py.tenantId =:tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId());
			} else {
				selectQuery.append(" py.tenantId LIKE :tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId() + "%");
			}

		}

//		if (!CollectionUtils.isEmpty(searchCriteria.getBusinessServices())) {
//			if (searchCriteria.getBusinessServices() != null && searchCriteria.getBusinessServices().contains("TL")) {
//				addClauseIfRequired(preparedStatementValues, selectQuery);
//				selectQuery.append(" py.totalamountpaid > 0");
//				
//			}
//		}

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

		if (StringUtils.isNotBlank(searchCriteria.getMobileNumber())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.mobileNumber = :mobileNumber");
			preparedStatementValues.put("mobileNumber", searchCriteria.getMobileNumber());
		}

		if (StringUtils.isNotBlank(searchCriteria.getTransactionNumber())) {
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
			/*Calendar c = Calendar.getInstance();
			c.setTime(new Date(searchCriteria.getToDate()));
			c.add(Calendar.DATE, 1);
			searchCriteria.setToDate(c.getTime().getTime());*/

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
		
		if (!StringUtils.isEmpty(searchCriteria.getExcludedTenant())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.tenantid != :excludedTenant");
			preparedStatementValues.put("excludedTenant", searchCriteria.getExcludedTenant());
		}

	}
	
	public static String getBillQuery() {
		return BILL_BASE_QUERY;
	}

	private StringBuilder addWrapperQuery(StringBuilder builder) {
		String wrapper = "select id from ( {{PLACEHOLDER}} ) t ORDER BY date DESC";
		wrapper = wrapper.replace("{{PLACEHOLDER}}", builder.toString());

		return new StringBuilder(wrapper);

	}

	private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}

	private static String addOrderByClause(StringBuilder selectQuery) {
		return selectQuery.append(" ORDER BY py.transactiondate DESC ").toString();

	}

	public String getUsageTypeQuery(PaymentSearchCriteria paymentSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		
		StringBuilder query = new StringBuilder(USAGE_TYPE_QUERY);
		
		if(paymentSearchCriteria.getTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, query);
			query.append(" epp.tenantid = :tenantId ");
			preparedStatementValues.put("tenantId", paymentSearchCriteria.getTenantId());
		}
		
		if(paymentSearchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, query);
			query.append(" epp.createdtime  >= :fromDate ");
			preparedStatementValues.put("fromDate", paymentSearchCriteria.getFromDate());
		}
		
		if(paymentSearchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, query);
			query.append(" epp.createdtime  <= :toDate ");
			preparedStatementValues.put("toDate", paymentSearchCriteria.getToDate());
		}
		
		return query.toString();
	}
	
	public static String getTargetCollection(TargetSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TARGET_COLLECTION_QUERY);
		return addWhereClauseForTaget(selectQuery, preparedStatementValues, criteria);
	}
	
	private static String addWhereClauseForTaget(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			TargetSearchCriteria searchCriteria) {

		if (StringUtils.isNotBlank(searchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getTenantId().split("\\.").length > 1) {
				selectQuery.append(" edt.tenantidformunicipalcorporation =:tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId());
			} else {
				selectQuery.append(" edt.tenantidformunicipalcorporation LIKE :tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId() + "%");
			}

		}

		if (!CollectionUtils.isEmpty(searchCriteria.getBusinessServices())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" edt.businessService IN (:businessService)  ");
			preparedStatementValues.put("businessService", searchCriteria.getBusinessServices());
		}

		return selectQuery.toString();

	}
	
	public static String getTotalCollection(PaymentSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_COLLECTION_QUERY);
		 addWhereClause(selectQuery, preparedStatementValues, criteria);
		 return selectQuery.toString();
	}

	public String getTransactionsCount(PaymentSearchCriteria paymentSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TRANSACTION_COUNT_QUERY);
		 addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
		 return selectQuery.toString();
	}

	public String getTenantWiseCollection(PaymentSearchCriteria paymentSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TENANT_WISE_COLLECTION_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
		selectQuery.append(" group by py.tenantid  ");
		return selectQuery.toString();
	}

	public String getTenantWiseTargetCollection(TargetSearchCriteria targerSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TENANT_WISE_TARGET_COLLECTION_QUERY);
		addWhereClauseForTaget(selectQuery, preparedStatementValues, targerSearchCriteria);
		selectQuery.append(" group by tenantid  ");
		return selectQuery.toString();
	}
	
	public String getTenantWiseTransaction(PaymentSearchCriteria paymentSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TENANT_WISE_TRANSACTION_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
		selectQuery.append(" group by py.tenantid  ");
		return selectQuery.toString();
	}
	
	public String getCumulativeCollection(PaymentSearchCriteria paymentSearchCriteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(CUMULATIVE_COLLECTION_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
		addGroupByClause(selectQuery,"to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(pyd.receiptdate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(pyd.receiptdate/1000))),'DD-MM-YYYY')");
		addOrderByClause(selectQuery,"to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(pyd.receiptdate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(pyd.receiptdate/1000))),'DD-MM-YYYY')) as payment");
		return selectQuery.toString();
	}
	
	 private static void addGroupByClause(StringBuilder demandQueryBuilder,String columnName) {
	        demandQueryBuilder.append(" GROUP BY " + columnName);
	    }

	    private static void addOrderByClause(StringBuilder demandQueryBuilder,String columnName) {
	        demandQueryBuilder.append(" ORDER BY " + columnName);
	    }

		public String getptTaxHeadsBreakupListQuery(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			StringBuilder selectQuery = new StringBuilder(PT_TAXHEAD_WISE_COLLECTION_QUERY);
			addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
			selectQuery.append( " ) coll ");
			addGroupByClause(selectQuery," tenantid ");
			return selectQuery.toString();
		}
	
}
