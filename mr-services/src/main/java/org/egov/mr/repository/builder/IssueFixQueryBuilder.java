package org.egov.mr.repository.builder;

import java.util.List;
import java.util.Map;

import org.egov.mr.web.models.collection.DemandSearchCriteria;
import org.egov.mr.web.models.collection.PaymentSearchCriteria;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.workflow.WorkFlowSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

	public static final String SELECT_PAYMENT_SQL = "SELECT py.*,pyd.*," +
			"py.id as py_id,py.tenantId as py_tenantId,py.totalAmountPaid as py_totalAmountPaid,py.createdBy as py_createdBy,py.createdtime as py_createdtime," +
			"py.lastModifiedBy as py_lastModifiedBy,py.lastmodifiedtime as py_lastmodifiedtime,py.additionalDetails as py_additionalDetails," +
			"pyd.id as pyd_id, pyd.tenantId as pyd_tenantId, pyd.manualreceiptnumber as manualreceiptnumber,pyd.manualreceiptdate as manualreceiptdate, pyd.createdBy as pyd_createdBy,pyd.createdtime as pyd_createdtime,pyd.lastModifiedBy as pyd_lastModifiedBy," +
			"pyd.lastmodifiedtime as pyd_lastmodifiedtime,pyd.additionalDetails as pyd_additionalDetails" +
			" FROM egcl_payment py  " +
			" INNER JOIN egcl_paymentdetail pyd ON pyd.paymentid = py.id " +
			" INNER JOIN egcl_bill bill ON bill.id = pyd.billid " +
			" INNER JOIN egcl_billdetial bd ON bd.billid = bill.id " ;

	public static final String BASE_DEMAND_QUERY = "SELECT dmd.id AS did,dmd.consumercode AS dconsumercode,"
			+ "dmd.consumertype AS dconsumertype,dmd.businessservice AS dbusinessservice,dmd.payer,"
			+ "dmd.billexpirytime AS dbillexpirytime, dmd.fixedBillExpiryDate as dfixedBillExpiryDate, "
			+ "dmd.taxperiodfrom AS dtaxperiodfrom,dmd.taxperiodto AS dtaxperiodto,"
			+ "dmd.minimumamountpayable AS dminimumamountpayable,dmd.createdby AS dcreatedby,"
			+ "dmd.lastmodifiedby AS dlastmodifiedby,dmd.createdtime AS dcreatedtime,"
			+ "dmd.lastmodifiedtime AS dlastmodifiedtime,dmd.tenantid AS dtenantid,dmd.status,"
			+ "dmd.additionaldetails as demandadditionaldetails,dmd.ispaymentcompleted as ispaymentcompleted,"
			+ "dmdl.id AS dlid,dmdl.demandid AS dldemandid,dmdl.taxheadcode AS dltaxheadcode,"
			+ "dmdl.taxamount AS dltaxamount,dmdl.collectionamount AS dlcollectionamount,"
			+ "dmdl.createdby AS dlcreatedby,dmdl.lastModifiedby AS dllastModifiedby,"
			+ "dmdl.createdtime AS dlcreatedtime,dmdl.lastModifiedtime AS dllastModifiedtime,"
			+ "dmdl.tenantid AS dltenantid,dmdl.additionaldetails as detailadditionaldetails " + "FROM egbs_demand_v1 dmd "
			+ "INNER JOIN egbs_demanddetail_v1 dmdl ON dmd.id=dmdl.demandid " + "AND dmd.tenantid=dmdl.tenantid ";

	public static final String PAYMENT_QUERY_ORDER_BY_CLAUSE = " py.transactiondate DESC ";

	public static final String DEMAND_QUERY_ORDER_BY_CLAUSE = " dmd.taxperiodfrom DESC";

	public static final String PROCESS_INSTANCE_QUERY = "select * from eg_wf_processinstance_v2 wf ";

	public static final String MR_QUERY_ORDER_BY_CLAUSE = " wf.lastmodifiedtime DESC";
	
	public static final String SEARCH_STATUS_MISMATCH_ISSUE_APPLICATIONS  = "select ema.tenantid,ema.applicationnumber,ewpv.action as actioninprocessinstance,ema.status as currentstatus,ewsv.applicationstatus as expectedstatus "
			+ " from eg_mr_application ema inner join (select distinct on(businessid) * from eg_wf_processinstance_v2 where businessservice in('MR') order by businessid,createdtime desc) ewpv on ewpv.businessid = ema.applicationnumber inner join eg_wf_state_v2 ewsv on ewsv.uuid = ewpv.status where ema.status <> ewsv.applicationstatus and ema.status not in ('DELETED') and ema.createdtime >= ( select (extract(EPOCH from DATE_TRUNC('year', current_timestamp))* 1000 + 19800000)) ";

	public static final String DEMAND_UPDATE_QUERY = "update egbs_demand_v1 "
			+ " set ispaymentcompleted = true "
			+ " where consumercode = ?";

	public static final String DEMAND_DETAIL_UPDATE_QUERY = "update egbs_demanddetail_v1"
			+ " set collectionamount =  taxamount "
			+ " where id = ?";

	public static final String BILL_EXPIRE_QUERY = "update egbs_bill_v1 ebv "
			+ "set status ='EXPIRED' "
			+ "where id in (select billid from egbs_billdetail_v1 ebv2 where consumercode in (?)) and status = 'ACTIVE'";

	public static final String APPLICATION_UPDATE_QUERY = "update eg_mr_application "
			+ " set status = ?, action = 'PAY', lastmodifiedtime = ? "
			+ " where applicationnumber = ?";

	public static final String INSERT_WORKFLOW_QUERY = "INSERT INTO public.eg_wf_processinstance_v2 "
			+ "(id, tenantid, businessservice, businessid, action, status, comment, assigner, assignee, statesla, previousstatus, createdby, lastmodifiedby, createdtime, lastmodifiedtime, modulename, businessservicesla, rating) "
			+ "VALUES( ? , ? , ? , ? , 'PAY' , ? , NULL, ? , NULL, 43200000, NULL, ? , ? , ? , ? , 'tl-services', 259052163, NULL)";

   
	public static final String GET_PAYMENT_ISSUES_APPLICATIONS_QUERY = "select mr.applicationnumber, mr.tenantid, txn.module  from eg_mr_application mr "
			+ "inner join eg_pg_transactions txn on txn.consumer_code=mr.applicationnumber where mr.status in ('PENDINGPAYMENT') and txn.txn_status='SUCCESS' and txn.module='MR' ";
	
	public static final String STEPBACK_APPLICATION_UPDATE_QUERY = "update eg_mr_application "
			+ " set status = ?, action = ?, issueddate = null, mrnumber = null"
			+ " where applicationnumber = ?";

		public static final String DELETE_WORKFLOW_QUERY = "delete from public.eg_wf_processinstance_v2 where businessid = ? and id = ? and action = ?";

	
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

	private void addClauseIfRequired(Map<String, Object> preparedStatementValues, StringBuilder selectQuery) {

		if (preparedStatementValues.isEmpty())
			selectQuery.append(" WHERE ");
		else {
			selectQuery.append(" AND");
		}

	}
	private static void addOrderByClause(StringBuilder query,String columnName) {
		query.append(" ORDER BY " + columnName);
	}

	public String getPaymentSearchQuery(PaymentSearchCriteria paymentSearchCriteria,
										Map<String, Object> preparedStatementValues) {
		// TODO Auto-generated method stub
		StringBuilder selectQuery = new StringBuilder(SELECT_PAYMENT_SQL);

		if(!StringUtils.isEmpty(paymentSearchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.tenantId =:tenantId ");
			preparedStatementValues.put("tenantId", paymentSearchCriteria.getTenantId());

		}

		if(!StringUtils.isEmpty(paymentSearchCriteria.getConsumerCode())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" bill.consumerCode in (:consumerCode) ");
			preparedStatementValues.put("consumerCode", paymentSearchCriteria.getConsumerCode());

		}

		if(!StringUtils.isEmpty(paymentSearchCriteria.getBusinessService())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" pyd.businessService IN (:businessService) ");
			preparedStatementValues.put("businessService", paymentSearchCriteria.getBusinessService());

		}

		addOrderByClause(selectQuery, PAYMENT_QUERY_ORDER_BY_CLAUSE);

		return selectQuery.toString();
	}

	public String getProcessInstancesQuery(WorkFlowSearchCriteria workFlowSearchCriteria,
										   Map<String, Object> preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(PROCESS_INSTANCE_QUERY);

		if(!StringUtils.isEmpty(workFlowSearchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" wf.tenantid = :tenantId ");
			preparedStatementValues.put("tenantId", workFlowSearchCriteria.getTenantId());

		}

		if(!StringUtils.isEmpty(workFlowSearchCriteria.getBusinessId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" wf.businessid = :businessId ");
			preparedStatementValues.put("businessId", workFlowSearchCriteria.getBusinessId());

		}

		addOrderByClause(selectQuery, MR_QUERY_ORDER_BY_CLAUSE);

		return selectQuery.toString();
	}

	public String getDemandSearchQuery(DemandSearchCriteria demandSearchCriteria,
									   Map<String, Object> preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(BASE_DEMAND_QUERY);

		if(!StringUtils.isEmpty(demandSearchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" dmd.tenantid = :tenantId and  dmd.status != 'CANCELLED'");
			preparedStatementValues.put("tenantId", demandSearchCriteria.getTenantId());

		}

		if(!StringUtils.isEmpty(demandSearchCriteria.getConsumerCode())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" dmd.consumercode in (:consumerCode) ");
			preparedStatementValues.put("consumerCode", demandSearchCriteria.getConsumerCode());

		}

		if(!StringUtils.isEmpty(demandSearchCriteria.getBusinessService())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" dmd.businessservice IN (:businessService) ");
			preparedStatementValues.put("businessService", demandSearchCriteria.getBusinessService());

		}

		addOrderByClause(selectQuery, DEMAND_QUERY_ORDER_BY_CLAUSE);

		return selectQuery.toString();
	}

	public String getDemandDetailUpdateQuery() {
		return DEMAND_DETAIL_UPDATE_QUERY;
	}

	public String getDemandUpdateQuery() {
		return DEMAND_UPDATE_QUERY;
	}

	public String getBillExpireQuery() {
		return BILL_EXPIRE_QUERY;
	}

	public String getApplicationUpdateQuery() {
		return APPLICATION_UPDATE_QUERY;
	}

	public String getInsertWorkflowQuery() {
		return INSERT_WORKFLOW_QUERY;
	}

	public String getPaymentIssueAppliactionsQuery() {
		return GET_PAYMENT_ISSUES_APPLICATIONS_QUERY;
	}
	
	public String getStatusMismatchAppliactionsQuery() {
		return SEARCH_STATUS_MISMATCH_ISSUE_APPLICATIONS;
	}

	public String getStepBackApplicationUpdateQuery() {
		return STEPBACK_APPLICATION_UPDATE_QUERY;
	}

	public String getDeleteWorkflowQuery() {
		return DELETE_WORKFLOW_QUERY;
	}

}
