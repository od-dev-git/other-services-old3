package org.egov.report.repository.builder;

import java.util.List;

import org.egov.report.web.model.issuefix.IssueFix;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class IssueFixQueryBuilder {
	
	private static final String QUERY_FOR_TRANSACTIONS = "select * from eg_pg_transactions txn ";
	
	private static final String QUERY_FOR_BILL_STATUS = "select status from egbs_bill_v1 bill ";
	
	private static final String QUERY_TO_UPDATE_BILL_IN_TRANSACTION = "update eg_pg_transactions set bill_id = (select distinct bill.id "
			+ "from egbs_billdetail_v1 billdtl inner join egbs_bill_v1 bill on bill.id=billdtl.billid where billdtl.consumercode = ? "
			+ "and bill.status ='ACTIVE'), additional_details = (select jsonb_set((select additional_details from eg_pg_transactions "
			+ "ept where txn_id = ? ), '{taxAndPayments}', (select jsonb_set((select cast(additional_details ->> 'taxAndPayments' as "
			+ "jsonb) from eg_pg_transactions ept where txn_id = ? ), '{0,billId}', (select to_jsonb(bll.id) from "
			+ "(select distinct bill.id from egbs_billdetail_v1 billdtl inner join egbs_bill_v1 bill on bill.id=billdtl.billid where "
			+ "billdtl.consumercode = ? and bill.status ='ACTIVE') bll))))) where txn_id = ? and txn_status <> 'SUCCESS' ";

	public static final String HRMS_QUERY = " select id from eg_hrms_employee ";
	
	public static final String EMAS_DELETION_QUERY = " select f_delete_dsc(?) ";
	
	public static final String EMAS_NAME_QUERY = " select common_name from public.emas_cust_temp ";
	
    public static final String BILL_EXPIRE_QUERY = "update egbs_bill_v1 ebv "
            + "set status ='EXPIRED' "
            + "where id in (select billid from egbs_billdetail_v1 ebv2 where consumercode in (?)) and status = 'ACTIVE'";
	
	public String getTransactionsQuery(IssueFix issueFix, List<Object> preparedPropStmtList) {

		StringBuilder query = new StringBuilder(QUERY_FOR_TRANSACTIONS);

		if (!StringUtils.isEmpty(issueFix.getConsumerCode())) {
			addClauseIfRequired(preparedPropStmtList, query);
			query.append(" txn.consumer_code = ? ");
			preparedPropStmtList.add(issueFix.getConsumerCode());
		}

		if (!StringUtils.isEmpty(issueFix.getTxnId())) {
			addClauseIfRequired(preparedPropStmtList, query);
			query.append(" txn.txn_id = ? ");
			preparedPropStmtList.add(issueFix.getTxnId());
		}
		
		if (!StringUtils.isEmpty(issueFix.getTenantId())) {
			addClauseIfRequired(preparedPropStmtList, query);
			query.append(" txn.tenant_id = ? ");
			preparedPropStmtList.add(issueFix.getTenantId());
		}

		return query.toString();

	}
	
	private static void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}

	public String getBillStatusQuery(String billId, List<Object> preparedPropStmtList) {

		StringBuilder query = new StringBuilder(QUERY_FOR_BILL_STATUS);

		if (!StringUtils.isEmpty(billId)) {
			addClauseIfRequired(preparedPropStmtList, query);
			query.append(" bill.id = ? ");
			preparedPropStmtList.add(billId);
		}

		return query.toString();

	}

	public String getUpdateBillIdInTransactionQuery() {	
		return QUERY_TO_UPDATE_BILL_IN_TRANSACTION;
	}

	public String getHRMSSearchQuery(String tenantId, String empId, List<Object> preparedPropStmtList) {
		
		StringBuilder query = new StringBuilder(HRMS_QUERY);
	
		if (!StringUtils.isEmpty(tenantId)) {
			addClauseIfRequired(preparedPropStmtList, query);
			query.append(" tenantid = ? ");
			preparedPropStmtList.add(tenantId);
		}
		
		if (!StringUtils.isEmpty(empId)) {
			addClauseIfRequired(preparedPropStmtList, query);
			query.append(" code = ? ");
			preparedPropStmtList.add(empId);
		}

		return query.toString();
	}

	public String getEMASDeletionQuery(int userId, List<Object> preparedPropStmtList) {

		StringBuilder query = new StringBuilder(EMAS_DELETION_QUERY);

		if (!StringUtils.isEmpty(userId)) {
			preparedPropStmtList.add(userId + "~ch4");
		}

		return query.toString();

	}

	public String getNameFromEMASDBQuery(int userId, List<Object> preparedPropStmtList) {
		
		StringBuilder query = new StringBuilder(EMAS_NAME_QUERY);
		
		if (!StringUtils.isEmpty(userId)) {
			addClauseIfRequired(preparedPropStmtList, query);
			query.append(" unique_id = ? ");
			preparedPropStmtList.add(userId+"~ch4");
		}

		return query.toString();
	}

    public String getBillExpireQuery() {
        return BILL_EXPIRE_QUERY;
    }

}
