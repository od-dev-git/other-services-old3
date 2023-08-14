package org.egov.dss.repository.builder;

import static java.util.stream.Collectors.toSet;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.DemandSearchCriteria;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.model.TargetSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;



@Component
@Slf4j
public class PaymentQueryBuilder {
	
	
	@Autowired
	private ConfigurationLoader configLoader;

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
			+ "coalesce(sum(rebate), 0) as rebateCollection,coalesce(sum(interest), 0) as interestCollection, "
			+ "coalesce(sum(advance), 0) as advanceCollection "
			+ "from  "
			+ "(select py.tenantid, "
			+ "case when bad.taxheadcode='PT_HOLDING_TAX' then bad.adjustedamount end as holdingTax,case when bad.taxheadcode='PT_OTHER_DUES' then bad.adjustedamount end as otherDues, "
			+ "case when bad.taxheadcode='PT_DRAINAGE_TAX' then bad.adjustedamount end as drainageTax, case when bad.taxheadcode='PT_WATER_TAX' then bad.adjustedamount end as waterTax, "
			+ "case when bad.taxheadcode='PT_SERVICE_TAX' then bad.adjustedamount end as serviceTax,case when bad.taxheadcode='PT_LATRINE_TAX' then bad.adjustedamount end as latrineTax, "
			+ "case when bad.taxheadcode='PT_SOLID_WASTE_USER_CHARGES' then bad.adjustedamount end as solidWasteUserCharge,case when bad.taxheadcode='PT_PARKING_TAX' then bad.adjustedamount end as parkingTax, "
			+ "case when bad.taxheadcode='PT_USAGE_EXCEMPTION' then bad.adjustedamount end as usageExcemption,case when bad.taxheadcode='PT_LIGHT_TAX' then bad.adjustedamount end as lightTax, "
			+ "case when bad.taxheadcode='PT_OWNERSHIP_EXCEMPTION' then bad.adjustedamount end as ownershipExcemption,case when bad.taxheadcode in ('PT_PENALTY', 'PT_TIME_PENALTY') then bad.adjustedamount end as penalty, "
			+ "case when bad.taxheadcode='PT_TIME_REBATE' then bad.adjustedamount end as rebate, case when bad.taxheadcode='PT_INTEREST' then bad.adjustedamount end as interest, "
			+ "case when bad.taxheadcode='PT_ADVANCE_CARRYFORWARD' then bad.amount end as advance "
			+ "from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id  "
			+ "inner join egcl_billdetial bdtl on bdtl.billid = pyd.billid  "
			+ "inner join egcl_billaccountdetail bad on bad.billdetailid = bdtl.id "; 

	public static final String USAGE_TYPE_QUERY = "SELECT epp.propertyid, epp.usagecategory  "
			+ "FROM eg_pt_property epp ";
	
	public static final String ALL_BUSINESS_SERVICE = " 'PT','WS','TL','MR','WS.ONE_TIME_FEE','SW','SW.ONE_TIME_FEE',"
	        +" 'PT.MUTATION','BPA.NC_APP_FEE','BPA.NC_SAN_FEE','BPA.NC_OC_APP_FEE','BPA.NC_OC_SAN_FEE' "; 
	
	public static final String TARGET_COLLECTION_QUERY = " select COALESCE (sum(budgetproposedformunicipalcorporation),0) from state.eg_dss_target edt  ";
	
	public static final String TOTAL_COLLECTION_QUERY = " select COALESCE(sum(py.totalamountpaid),0) from egcl_payment py "
			                                          + "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id   ";
			
	public static final String TRANSACTION_COUNT_QUERY = " select count(py.transactionnumber) from egcl_payment py "
            + "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id   ";
	
	public static final String TENANT_WISE_COLLECTION_QUERY = " select py.tenantid, COALESCE(sum(py.totalamountpaid),0) as totalamt from egcl_payment py "
            + "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id   ";
	
	public static final String TENANT_WISE_TARGET_COLLECTION_QUERY = " select tenantidformunicipalcorporation as tenantid,COALESCE (sum(budgetproposedformunicipalcorporation),0) as totalamt from state.eg_dss_target edt  ";
	
	public static final String TENANT_WISE_TRANSACTION_QUERY = " select py.tenantid, COALESCE(count(py.transactionnumber),0) as totalamt from egcl_payment py "
            + "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id   ";
	
	public static final String CUMULATIVE_COLLECTION_QUERY = " select to_char(monthYear, 'Mon-YYYY') as name, sum(totalCollection) over (order by monthYear asc rows between unbounded preceding and current row) as value "
			+ "from (select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(pyd.receiptdate/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(pyd.receiptdate/1000))),'DD-MM-YYYY') as monthYear,sum(py.totalamountpaid) as totalCollection from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id  ";
	
	public static final String COLLECTION_BY_USAGE_TYPE = " select pt.usagecategory as name, COALESCE(sum(py.totalamountpaid),0) as value  from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id "
			+ "inner join egcl_bill bill on bill.id = pyd.billid "
			+ "inner join eg_pt_property pt on pt.propertyid = bill.consumercode ";
	
	public static final String WS_COLLECTION_BY_USAGE_TYPE = " select ws.usagecategory as name, COALESCE(sum(py.totalamountpaid),0) as value from egcl_payment py "
			+ " inner join egcl_paymentdetail pyd on pyd.paymentid = py.id "
			+ " inner join egcl_bill bill on bill.id=pyd.billid "
			+ " inner join eg_ws_connection conn on conn.applicationno = bill.consumercode "
			+ " inner join eg_ws_service ws on ws.connection_id = conn.id ";
	
	public static final String TENANT_WISE_ASSESSED_PROPERTIES = " select tenantid as tenantid, coalesce(count(distinct propertyid),0) as totalamt from eg_pt_asmt_assessment ";
	
	public static final String TENANT_WISE_PROPERTIES_PAID = " select py.tenantid as tenantid , count(distinct bill.consumercode) as totalamt from egcl_payment py inner join egcl_paymentdetail pyd on pyd.paymentid = py.id inner join egcl_bill bill on bill.id = pyd.billid ";
	
	public static final String COLLECTION_CHANNEL_QUERY = " select py.paymentmode as name, COALESCE(sum(py.totalamountpaid),0) as value from egcl_payment py "
			+ " inner join egcl_paymentdetail pyd on pyd.paymentid = py.id ";
	
	public static final String WS_TAXHEAD_WISE_COLLECTION_QUERY = "select tenantid, "
			+ "coalesce(sum(waterCharge), 0) as waterChargeCollection, "
			+ "coalesce(sum(sewerageCharge), 0) as sewerageChargesCollection, "
			+ "coalesce(sum(sewerageAdhocCharge), 0) as sewerageAdhocChargeCollection, "
			+ "coalesce(sum(penalty), 0) as penaltyCollection, "
			+ "coalesce(sum(rebate), 0) as rebateCollection, "
			+ "coalesce(sum(interest), 0) as interestCollection, "
			+ "coalesce(sum(scrutiny), 0) as scrutinyCollection, "
			+ "coalesce(sum(securityCharge), 0) as securityCollection, "
			+ "coalesce(sum(ownershipChange), 0) as ownershipChangeCollection, "
			+ "coalesce(sum(labourfee), 0) as labourCollection, "
			+ "coalesce(sum(advance), 0) as advanceCollection "
           	+ "from  "
			+ "(select py.tenantid, "
			+ "case when bad.taxheadcode='WS_CHARGE' then bad.adjustedamount end as waterCharge, "
			+ "case when bad.taxheadcode='SW_CHARGE' then bad.adjustedamount end as sewerageCharge, "
			+ "case when bad.taxheadcode='SW_ADHOC_CHARGE' then bad.adjustedamount end as sewerageAdhocCharge, "
			+ "case when bad.taxheadcode in ('WS_PENALTY', 'WS_TIME_PENALTY') then bad.adjustedamount end as penalty, "
			+ "case when bad.taxheadcode in ('WS_TIME_REBATE', 'WS_SPECIAL_REBATE','SW_SPECIAL_REBATE','SW_TIME_REBATE','WS_ANNUAL_PAYMENT_REBATE') then bad.adjustedamount end as rebate, "
			+ "case when bad.taxheadcode in ('WS_TIME_INTEREST','SW_TIME_INTEREST') then bad.adjustedamount end as interest, "
			+ "case when bad.taxheadcode in ('WS_SCRUTINY_FEE_INSTALLMENT','WS_SCRUTINY_FEE','SW_SCRUTINY_FEE') then bad.adjustedamount end as scrutiny, "
	        + "case when bad.taxheadcode in ('WS_SECURITY_CHARGE','SW_SECURITY_CHARGE') then bad.adjustedamount end as securityCharge, "
	        + "case when bad.taxheadcode in ('WS_OWNERSHIP_CHANGE_FEE') then bad.adjustedamount end as ownershipChange, " 
	        + "case when bad.taxheadcode in ('WS_LABOUR_FEE','WS_LABOUR_FEE_INSTALLMENT') then bad.adjustedamount end as labourfee, "
	        + "case when bad.taxheadcode in ('WS_ADVANCE_CARRYFORWARD','SW_ADVANCE_CARRYFORWARD') then bad.amount end as advance "
			+ "from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id "
			+ "inner join egcl_billdetial bdtl on bdtl.billid = pyd.billid "
			+ "inner join egcl_billaccountdetail bad on bad.billdetailid = bdtl.id ";
	
	public static final String TENANT_WISE_WS_CONNECTIONS = " select tenantid as tenantid, count(*) as connections from eg_ws_connection py where py.applicationstatus = :status and py.isoldapplication = 'false' ";
	
	public static final String TL_COLLECTIONS_By_LICENSE_QUERY = "select tunit.tradetype as name, sum(py.totalamountpaid) as value from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id "
			+ "inner join egcl_bill bill on bill.id = pyd.billid  "
			+ "inner join eg_tl_tradelicense tl on tl.applicationnumber = bill.consumercode "
			+ "inner join eg_tl_tradelicensedetail dtl on dtl.tradelicenseid = tl.id "
			+ "inner join eg_tl_tradeunit tunit on tunit.tradelicensedetailid = dtl.id";
	
	public static final String TL_TENANT_WISE_LICENSES_ISSUED = "select tenantid as tenantid, count(applicationnumber) as connections from eg_tl_tradelicense tl  where businessservice = 'TL' and status = :status ";
	
	public static final String MR_TENANT_WISE_APPLICATIONS = " select tenantid, count(applicationnumber) connections from eg_mr_application ";
	
	public static final String DEPT_WISE_COLLECTION = "select businessservice as name, sum(totalCollection) as value from "
			+ "(select "
			+ "case when pyd.businessservice in ('BPA.NC_SAN_FEE', 'BPA.NC_APP_FEE','BPA.NC_OC_APP_FEE','BPA.NC_OC_SAN_FEE') then 'OBPS' "
			+ "when pyd.businessservice in ('WS','WS.ONE_TIME_FEE') then 'WS' "
			+ "when pyd.businessservice in ('PT','PT.MUTATION') then 'PT' "
			+ "when pyd.businessservice in ('TL') then 'TL' "
			+ "when pyd.businessservice in ('MR') then 'MR' "
			+ "when pyd.businessservice in ('FSM.TRIP_CHARGES') then 'FSM' end businessservice, "
			+ "sum(py.totalamountpaid) as totalCollection from egcl_payment py "
			+ "inner join egcl_paymentdetail pyd on pyd.paymentid = py.id ";
	
	public static final String CURRENT_DEMAND_QUERY = " select coalesce(sum(amount), 0) as amount from state.eg_dss_demand ";
	
	public static final String ARREAR_DEMAND_QUERY = " select coalesce(sum(amount), 0) - coalesce(sum(collectionamount), 0) as amount from state.eg_dss_demand ";
	
	public static final String PT_CURRENT_COLLECTION_QUERY = " select sum(case when eb2.fromperiod >=startingDate and eb2.toperiod <= endingDate then eb3.adjustedamount else 0 end ) as currentcollectionamount ";
	
	public static final String PT_ARREAR_COLLECTION_QUERY = " select sum(case when eb2.toperiod < startingDate then eb3.adjustedamount else 0 end ) as arrearcollection ";
	
	public static final String PT_PREVIOUS_YEAR_COLLECTION_QUERY = " select	coalesce(sum(eb3.adjustedamount),0) as previousyearcollectionamount ";

	public static final String PT_ARREAR_AND_TOTAL_COLLECTION_COMMON_QUERY = " from egcl_bill eb "
			+ "inner join egcl_paymentdetail ep on	eb.id = ep.billid and eb.businessservice IN ('PT') " 
			+ "inner join egcl_payment epp on epp.id = ep.paymentid	and epp.paymentstatus not in ('CANCELLED', 'DISHONOURED') and epp.tenantid != 'od.testing' "
			+ "inner join egcl_billdetial eb2 on eb.id = eb2.billid "
			+ "inner join egcl_billaccountdetail eb3 on	eb2.id = eb3.billdetailid "
			+ "where 1 = 1 and eb3.taxheadcode not in ('PT_ADVANCE_CARRYFORWARD') ";
	
	public static final String PT_GROWTH_RATE_QUERY_UPPER_HALF= " with year_collection as ( SELECT "
			+ " CONCAT( EXTRACT(YEAR FROM financial_year_start) - CASE WHEN EXTRACT(MONTH FROM financial_year_start) < 4 THEN 1 ELSE 0 END,'-', EXTRACT(YEAR FROM financial_year_start) - CASE WHEN EXTRACT(MONTH FROM financial_year_start) < 4 THEN 0 ELSE -1 END) AS Financial_Year, "
			+ " coalesce(sum(totalamountpaid), 0) AS Total_Collection FROM ( SELECT TO_TIMESTAMP(pyd.receiptdate / 1000) AS payment_date,py.totalamountpaid, "
			+ " CASE WHEN EXTRACT(MONTH FROM TO_TIMESTAMP(pyd.receiptdate / 1000)) < 4 THEN TO_TIMESTAMP((EXTRACT(YEAR FROM TO_TIMESTAMP(pyd.receiptdate / 1000)) - 1) || '-04-01', 'YYYY-MM-DD') "
			+ " ELSE TO_TIMESTAMP(EXTRACT(YEAR FROM TO_TIMESTAMP(pyd.receiptdate / 1000)) || '-04-01', 'YYYY-MM-DD') "
			+ " END AS financial_year_start FROM egcl_payment py INNER JOIN egcl_paymentdetail pyd ON "
			+ " pyd.paymentid = py.id WHERE UPPER(py.paymentstatus) NOT IN ('CANCELLED', 'DISHONOURED') AND pyd.businessService IN ('PT', 'PT.MUTATION')\r\n"
			+ " AND py.tenantid != 'od.testing' " ;
	
	public static final String PT_GROWTH_RATE_QUERY_LOWER_HALF= " ) AS converted_data GROUP BY Financial_Year ORDER BY Financial_Year ), "
			+ " year_collection_data as ( select Financial_Year as current_year, lag(Financial_Year) over (order by Financial_Year) as previous_year,Total_Collection as current_collection, "
			+ " lag(Total_Collection) over (order by Financial_Year) as previous_collection, case  when lag(Total_Collection) over (order by Financial_Year) = 0 then 0.0 else ((Total_Collection)/lag(Total_Collection) over (order by Financial_Year)) * 100.0 end as growth_rate from year_collection order by Financial_Year "
			+ " ) select previous_year as name, round(growth_rate,2) as value  from year_collection_data where growth_rate is not null ";
	
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

		 if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.tenantId in ( :tenantId )");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds() );
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
		
		if (!StringUtils.isEmpty(searchCriteria.getPropertyStatus())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pt.status = :propertyStatus");
			preparedStatementValues.put("propertyStatus", searchCriteria.getPropertyStatus());
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
		
		if(paymentSearchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(paymentSearchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, query);
			query.append(" epp.tenantid in (:tenantId )");
			preparedStatementValues.put("tenantId", paymentSearchCriteria.getTenantIds());
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
	
	private static void addWhereClauseForProperties(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			PaymentSearchCriteria searchCriteria) {

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			    addClauseIfRequired(preparedStatementValues, selectQuery);
			    selectQuery.append(" tenantId in (:tenantId)");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
			}
		
        if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" createdtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" createdtime <= :toDate");
		    preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}

		if (!StringUtils.isEmpty(searchCriteria.getPropertyStatus())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" status = :propertyStatus");
			preparedStatementValues.put("propertyStatus", searchCriteria.getPropertyStatus());
		}
		
		if (!StringUtils.isEmpty(searchCriteria.getExcludedTenant())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" tenantid != :excludedTenant");
			preparedStatementValues.put("excludedTenant", searchCriteria.getExcludedTenant());
		}

	}

	
	public static String getTargetCollection(TargetSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TARGET_COLLECTION_QUERY);
		return addWhereClauseForTarget(selectQuery, preparedStatementValues, criteria);
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
	
	public static String getTotalCollection(PaymentSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(TOTAL_COLLECTION_QUERY);
		 addWhereClause(selectQuery, preparedStatementValues, criteria);
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
		addWhereClauseForTarget(selectQuery, preparedStatementValues, targerSearchCriteria);
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
	    
	    private void addWhereClauseForTL(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
				PaymentSearchCriteria searchCriteria) {
	    	
	    	if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			    addClauseIfRequired(preparedStatementValues, selectQuery);
			    selectQuery.append(" tenantId in (:tenantId)");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
			}

	       if (searchCriteria.getFromDate() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" lastmodifiedtime >= :fromDate");
				preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
			}

			if (searchCriteria.getToDate() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" lastmodifiedtime <= :toDate");
			    preparedStatementValues.put("toDate", searchCriteria.getToDate());
			}
			
			if (!StringUtils.isEmpty(searchCriteria.getExcludedTenant())) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" tenantid != :excludedTenant");
				preparedStatementValues.put("excludedTenant", searchCriteria.getExcludedTenant());
			}
	
		}
    
	    private void addWhereClauseForMR(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
				PaymentSearchCriteria searchCriteria) {
	    	
	    	if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			    addClauseIfRequired(preparedStatementValues, selectQuery);
			    selectQuery.append(" tenantId in (:tenantId) ");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
			}

	       if (searchCriteria.getFromDate() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" applicationdate >= :fromDate ");
				preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
			}

			if (searchCriteria.getToDate() != null) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" applicationdate <= :toDate ");
			    preparedStatementValues.put("toDate", searchCriteria.getToDate());
			}
			
			if (!CollectionUtils.isEmpty(searchCriteria.getStatusNotIn())) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" status not in (:statusNotIn) ");
				preparedStatementValues.put("statusNotIn", searchCriteria.getStatusNotIn());
			}
			
			if (!StringUtils.isEmpty(searchCriteria.getExcludedTenant())) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" tenantid != :excludedTenant ");
				preparedStatementValues.put("excludedTenant", searchCriteria.getExcludedTenant());
			}
	
		}
	    
		public String getptTaxHeadsBreakupListQuery(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			StringBuilder selectQuery = new StringBuilder(PT_TAXHEAD_WISE_COLLECTION_QUERY);
			addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
			selectQuery.append( " ) coll ");
			addGroupByClause(selectQuery," tenantid ");
			return selectQuery.toString();
		}
	
	    
	    public String getCollectionByUsageTypeQuery(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			StringBuilder selectQuery = new StringBuilder(COLLECTION_BY_USAGE_TYPE);
			addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
			selectQuery.append(" group  by pt.usagecategory ");
			return selectQuery.toString();
		}
	    
	    public String getTenantPropertiesPaid(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			StringBuilder selectQuery = new StringBuilder(TENANT_WISE_PROPERTIES_PAID);
			addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
			selectQuery.append(" group by py.tenantid  ");
			return selectQuery.toString();
		}
	    
	    public String getWSCollectionByUsageTypeQuery(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			StringBuilder selectQuery = new StringBuilder(WS_COLLECTION_BY_USAGE_TYPE);
			addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
			selectQuery.append(" group by ws.usagecategory ");
			return selectQuery.toString();
		}

		public String getCollectionByChannel(PaymentSearchCriteria criteria,
				Map<String, Object> preparedStatementValues) {
			StringBuilder selectQuery = new StringBuilder(COLLECTION_CHANNEL_QUERY);
			addWhereClause(selectQuery, preparedStatementValues, criteria);
			selectQuery.append(" group by py.paymentmode ");
			return selectQuery.toString();
		}
		
		public String getWSTaxHeadsBreakupListQuery(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			StringBuilder selectQuery = new StringBuilder(WS_TAXHEAD_WISE_COLLECTION_QUERY);
			addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
			selectQuery.append( " ) coll ");
			addGroupByClause(selectQuery," tenantid ");
			return selectQuery.toString();
		}

		public String getTenantWiseWSConnections(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			StringBuilder selectQuery = new StringBuilder(TENANT_WISE_WS_CONNECTIONS);
			preparedStatementValues.put("status", "CONNECTION_ACTIVATED");
			addWhereClauseForProperties(selectQuery, preparedStatementValues, paymentSearchCriteria);
			selectQuery.append(" group by tenantid  ");
			return selectQuery.toString();
		}

		public String getTlCollectionsByLicenseTypeQuery(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {	
			StringBuilder selectQuery = new StringBuilder(TL_COLLECTIONS_By_LICENSE_QUERY);
			addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
			selectQuery.append(" group by tunit.tradetype  ");
			selectQuery.append(" order by value desc  limit :limit");
			preparedStatementValues.put("limit", configLoader.getCollectionByTradeLimit());
			return selectQuery.toString();
		}

		public String getTenantWiseLicensesIssued(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			
			StringBuilder selectQuery = new StringBuilder(TL_TENANT_WISE_LICENSES_ISSUED);
			preparedStatementValues.put("status", "APPROVED");
			addWhereClauseForTL(selectQuery, preparedStatementValues, paymentSearchCriteria);
			selectQuery.append(" group by tenantid  ");
			return selectQuery.toString();
		}

		public String getTenantWiseMrApplications(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			paymentSearchCriteria.setStatusNotIn(Sets.newHashSet(DashboardConstants.STATUS_INITIATED));
			StringBuilder selectQuery = new StringBuilder(MR_TENANT_WISE_APPLICATIONS);
			addWhereClauseForMR(selectQuery, preparedStatementValues, paymentSearchCriteria);
			selectQuery.append(" group by tenantid  ");
			return selectQuery.toString();
		}

		public String getServiceWiseCollectionQuery(PaymentSearchCriteria criteria,
				Map<String, Object> preparedStatementValues) {
			
			StringBuilder selectQuery = new StringBuilder(DEPT_WISE_COLLECTION);
			addWhereClause(selectQuery, preparedStatementValues, criteria);
			selectQuery.append(" group by pyd.businessservice) tmpPay  ");
			selectQuery.append(" group by businessservice ");
			return selectQuery.toString();
		}

		private static void addWhereClauseForDemand(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
				DemandSearchCriteria searchCriteria) {

			if (!StringUtils.isEmpty(searchCriteria.getTenantId())) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" tenantId = :tenantId ");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId());
			}

            if (!StringUtils.isEmpty(searchCriteria.getBusinessService())) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" businessservice = :businessService ");
				preparedStatementValues.put("businessService", searchCriteria.getBusinessService());
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

		public String getCurrentCollection(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			String query = PT_CURRENT_COLLECTION_QUERY;
			addOffset(paymentSearchCriteria);
			query = setFromAndToDate(paymentSearchCriteria, query);
			removeOffset(paymentSearchCriteria);
			
			StringBuilder modifiedQuery = new StringBuilder();
			modifiedQuery.append(query);
			StringBuilder selectQuery = new StringBuilder(PT_ARREAR_AND_TOTAL_COLLECTION_COMMON_QUERY);
			modifiedQuery.append(selectQuery);
			setTenantIDAndTransactionDate(paymentSearchCriteria, preparedStatementValues, modifiedQuery);

			return modifiedQuery.toString();
		}

		private void addOffset(PaymentSearchCriteria paymentSearchCriteria) {
			Long startMillisGMT = paymentSearchCriteria.getFromDate();
			Long endMillisGMT = paymentSearchCriteria.getToDate();
			log.info(String.valueOf(startMillisGMT));
			log.info(String.valueOf(endMillisGMT));
			
			Integer istOffsetHours = 5;
			Integer istOffsetMinutes = 30;

			Long offsetMillis = 2*((long) istOffsetHours * 60 + istOffsetMinutes) * 60 * 1000;

			// increasing range to adjust for error due to timezone variation
			startMillisGMT = startMillisGMT - offsetMillis;
			endMillisGMT = endMillisGMT + offsetMillis;
			
			log.info(String.valueOf(startMillisGMT));
			log.info(String.valueOf(endMillisGMT));
			
			paymentSearchCriteria.setFromDate(startMillisGMT);
			paymentSearchCriteria.setToDate(endMillisGMT);
		}
		
		private void removeOffset(PaymentSearchCriteria paymentSearchCriteria) {
			Long startMillisGMT = paymentSearchCriteria.getFromDate();
			Long endMillisGMT = paymentSearchCriteria.getToDate();
			log.info(String.valueOf(startMillisGMT));
			log.info(String.valueOf(endMillisGMT));
			
			Integer istOffsetHours = 5;
			Integer istOffsetMinutes = 30;

			long offsetMillis = 2*((long) istOffsetHours * 60 + istOffsetMinutes) * 60 * 1000;

			// increasing range to adjust for error due to timezone variation
			startMillisGMT = startMillisGMT + offsetMillis;
			endMillisGMT = endMillisGMT - offsetMillis;
			log.info(String.valueOf(startMillisGMT));
			log.info(String.valueOf(endMillisGMT));

			
			paymentSearchCriteria.setFromDate(startMillisGMT);
			paymentSearchCriteria.setToDate(endMillisGMT);
		}

		private String setFromAndToDate(PaymentSearchCriteria paymentSearchCriteria, String query) {
			if (paymentSearchCriteria.getFromDate() != null) {
				query=query.replace("startingDate", paymentSearchCriteria.getFromDate().toString());
			}

			if (paymentSearchCriteria.getToDate() != null) {
				query=query.replace("endingDate", paymentSearchCriteria.getToDate().toString());
			}
			
			return query;
		}

		private void setTenantIDAndTransactionDate(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues, StringBuilder selectQuery) {

			if (paymentSearchCriteria.getFromDate() != null) {
				selectQuery.append("and ep.receiptdate >= :fromDate ");
				preparedStatementValues.put("fromDate", paymentSearchCriteria.getFromDate());
			}

			if (paymentSearchCriteria.getToDate() != null) {
				selectQuery.append(" and ep.receiptdate <= :toDate ");
				preparedStatementValues.put("toDate", paymentSearchCriteria.getToDate());
			}

			if (paymentSearchCriteria.getTenantIds() != null
					&& !CollectionUtils.isEmpty(paymentSearchCriteria.getTenantIds())) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" eb.tenantId in ( :tenantId )");
				preparedStatementValues.put("tenantId", paymentSearchCriteria.getTenantIds());
			}
		}

		public String getArrearCollection(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			String query = PT_ARREAR_COLLECTION_QUERY;
			addOffset(paymentSearchCriteria);
			query = setFromAndToDate(paymentSearchCriteria, query);
			removeOffset(paymentSearchCriteria);

			StringBuilder modifiedQuery = new StringBuilder();
			modifiedQuery.append(query);
			StringBuilder selectQuery = new StringBuilder(PT_ARREAR_AND_TOTAL_COLLECTION_COMMON_QUERY);
			modifiedQuery.append(selectQuery);
			setTenantIDAndTransactionDate(paymentSearchCriteria, preparedStatementValues, modifiedQuery);

			return modifiedQuery.toString();
		}

		public String getPreviousYearCollection(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			String query = PT_PREVIOUS_YEAR_COLLECTION_QUERY;

			Long fromDate = paymentSearchCriteria.getFromDate();
			
			StringBuilder modifiedQuery = new StringBuilder();
			modifiedQuery.append(query);
			StringBuilder selectQuery = new StringBuilder(PT_ARREAR_AND_TOTAL_COLLECTION_COMMON_QUERY);
			modifiedQuery.append(selectQuery);
			
			paymentSearchCriteria.setFromDate(null);
			setTenantIDAndTransactionDate(paymentSearchCriteria, preparedStatementValues, modifiedQuery);
									
			ZonedDateTime startIST = Instant.ofEpochMilli(fromDate).atZone(ZoneId.of("Asia/Kolkata"));		
			startIST = startIST.minusYears(1);
			
			Long startMillisIST = startIST.toInstant().toEpochMilli();
			
			log.info("Start TIme in Previous Year Collection : " + startMillisIST.toString());
			
			paymentSearchCriteria.setFromDate(startMillisIST);
			paymentSearchCriteria.setToDate(fromDate);

			addOffset(paymentSearchCriteria);

			if (paymentSearchCriteria.getFromDate() != null) {
				modifiedQuery.append("and eb2.fromperiod >= :pyFromDate ");
				preparedStatementValues.put("pyFromDate", paymentSearchCriteria.getFromDate());
			}
			
			if (paymentSearchCriteria.getToDate() != null) {
				modifiedQuery.append(" and eb2.toperiod <= :pyToDate ");
				preparedStatementValues.put("pyToDate", paymentSearchCriteria.getToDate());
			}

			return modifiedQuery.toString();
		}

		public String getCollectionGrowthRate(PaymentSearchCriteria paymentSearchCriteria,
				Map<String, Object> preparedStatementValues) {
			StringBuilder upperQuery = new StringBuilder(PT_GROWTH_RATE_QUERY_UPPER_HALF);
			StringBuilder lowerQuery = new StringBuilder(PT_GROWTH_RATE_QUERY_LOWER_HALF);
			
			
			 if (paymentSearchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(paymentSearchCriteria.getTenantIds())) {
				 upperQuery.append(" AND py.tenantid in ( :tenantId ) ");
					preparedStatementValues.put("tenantId", paymentSearchCriteria.getTenantIds() );
					}
			 upperQuery.append(lowerQuery);
			
			return upperQuery.toString();
		}

	
}
