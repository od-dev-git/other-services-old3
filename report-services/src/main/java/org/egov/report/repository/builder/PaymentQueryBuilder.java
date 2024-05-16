package org.egov.report.repository.builder;

import static java.util.stream.Collectors.toSet;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.report.model.PaymentSearchCriteria;
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
			+ "WHERE b.id IN (:id) and b.tenantid=:tenantId and bd.tenantid=:tenantId and ad.tenantid=:tenantId;";
	
	public static final String ID_QUERY = "SELECT DISTINCT py.id as id,py.transactiondate as date " +
            " FROM egcl_payment py  " +
            " INNER JOIN egcl_paymentdetail pyd ON pyd.paymentid = py.id " +
            " INNER JOIN egcl_bill bill ON bill.id = pyd.billid " +
            " INNER JOIN egcl_billdetial bd ON bd.billid = bill.id " ;
	
	public static final String PAYMENTS_COUNT_QUERY = "SELECT count(DISTINCT py.id) " +
            " FROM egcl_payment py  " +
            " INNER JOIN egcl_paymentdetail pyd ON pyd.paymentid = py.id " +
            " INNER JOIN egcl_bill bill ON bill.id = pyd.billid " +
            " INNER JOIN egcl_billdetial bd ON bd.billid = bill.id " ;

	public static String getPaymentSearchQuery(List<String> ids, Map<String, Object> preparedStatementValues,
			PaymentSearchCriteria searchCriteria) {
		StringBuilder selectQuery = new StringBuilder(SELECT_PAYMENT_SQL);

		if (StringUtils.isNotBlank(searchCriteria.getTenantId())) {
            addClauseIfRequired(preparedStatementValues, selectQuery);
            if(searchCriteria.getTenantId().split("\\.").length > 1) {
                selectQuery.append(" py.tenantId =:tenantId");
                preparedStatementValues.put("tenantId", searchCriteria.getTenantId());

                addClauseIfRequired(preparedStatementValues, selectQuery);
                selectQuery.append(" pyd.tenantId =:tenantId ");
            }
            else {
                selectQuery.append(" py.tenantId LIKE :tenantId");
                preparedStatementValues.put("tenantId", searchCriteria.getTenantId() + "%");

                addClauseIfRequired(preparedStatementValues, selectQuery);
                selectQuery.append(" pyd.tenantId LIKE :tenantId ");
            }

        }

		addClauseIfRequired(preparedStatementValues, selectQuery);
		selectQuery.append(" py.id IN (:id)  ");
		preparedStatementValues.put("id", ids);
		return addOrderByClause(selectQuery);
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

	public static String getBillQuery() {
		return BILL_BASE_QUERY;
	}

	public String getIdQuery(PaymentSearchCriteria searchCriteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(ID_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, searchCriteria);
		StringBuilder finalQuery = addWrapperQuery(selectQuery);
		if(searchCriteria.getLimit() != null && searchCriteria.getOffset() != null)
			addPagination(finalQuery, preparedStatementValues, searchCriteria);
		return finalQuery.toString();
	}

	private static void addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			PaymentSearchCriteria searchCriteria) {

		if (StringUtils.isNotBlank(searchCriteria.getTenantId())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			if (searchCriteria.getTenantId().split("\\.").length > 1) {
				selectQuery.append(" py.tenantId =:tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId());
				
				addClauseIfRequired(preparedStatementValues, selectQuery);
                selectQuery.append(" pyd.tenantId =:tenantId ");
                addClauseIfRequired(preparedStatementValues, selectQuery);
                selectQuery.append(" bill.tenantId =:tenantId ");
                addClauseIfRequired(preparedStatementValues, selectQuery);
                selectQuery.append(" bd.tenantId =:tenantId ");
				
			} else {
				selectQuery.append(" py.tenantId LIKE :tenantId");
				preparedStatementValues.put("tenantId", searchCriteria.getTenantId() + "%");
				
				addClauseIfRequired(preparedStatementValues, selectQuery);
                selectQuery.append(" pyd.tenantId LIKE :tenantId ");
                addClauseIfRequired(preparedStatementValues, selectQuery);
                selectQuery.append(" bill.tenantId LIKE :tenantId ");
                addClauseIfRequired(preparedStatementValues, selectQuery);
                selectQuery.append(" bd.tenantId LIKE :tenantId ");
			}

		}

		if (!CollectionUtils.isEmpty(searchCriteria.getBusinessServices())) {
			if (searchCriteria.getBusinessServices() != null && searchCriteria.getBusinessServices().contains("TL")) {
				addClauseIfRequired(preparedStatementValues, selectQuery);
				selectQuery.append(" py.totalamountpaid > 0");
			}
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
			selectQuery.append(" UPPER(py.paymentstatus) in (:status)");
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
			selectQuery.append(" py.transactionDate >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" py.transactionDate <= :toDate");
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
		
		if (!CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
        	addClauseIfRequired(preparedStatementValues, selectQuery);

        	selectQuery.append(" py.tenantId =:tenantId");
            preparedStatementValues.put("tenantId", searchCriteria.getTenantId());

            addClauseIfRequired(preparedStatementValues, selectQuery);
            selectQuery.append(" pyd.tenantId =:tenantId ");
            addClauseIfRequired(preparedStatementValues, selectQuery);
            selectQuery.append(" bill.tenantId =:tenantId ");
            addClauseIfRequired(preparedStatementValues, selectQuery);
            selectQuery.append(" bd.tenantId =:tenantId ");

        }

	}
	
	private StringBuilder addWrapperQuery(StringBuilder builder){
        String wrapper = "select id from ( {{PLACEHOLDER}} ) t ORDER BY date DESC";
        wrapper = wrapper.replace("{{PLACEHOLDER}}", builder.toString());

        return new StringBuilder(wrapper);

    }

	public String getIdCountQuery(PaymentSearchCriteria paymentSearchCriteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(PAYMENTS_COUNT_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, paymentSearchCriteria);
		//StringBuilder finalQuery = addWrapperQuery(selectQuery);
		return selectQuery.toString();
	}
	
	private void addPagination(StringBuilder query,Map<String, Object> preparedStatementValues,PaymentSearchCriteria criteria){
        
		int limit = criteria.getLimit();
        int offset = criteria.getOffset();
        query.append(" OFFSET :offset ");
        query.append(" LIMIT :limit ");

        preparedStatementValues.put("offset", offset);
        preparedStatementValues.put("limit", limit);

    }
}
