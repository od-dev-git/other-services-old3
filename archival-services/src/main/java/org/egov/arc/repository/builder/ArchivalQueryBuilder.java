package org.egov.arc.repository.builder;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.arc.config.ArchivalConfig;
import org.egov.arc.model.DemandCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ArchivalQueryBuilder {

	@Autowired
	ArchivalConfig config;

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
			+ "dmdl.tenantid AS dltenantid,dmdl.additionaldetails as detailadditionaldetails "
			+ "FROM egbs_demand_v1_bkp dmd " + "INNER JOIN egbs_demanddetail_v1_bkp dmdl ON dmd.id=dmdl.demandid "
			+ "AND dmd.tenantid=dmdl.tenantid ";

	private final String paginationWrapper = "SELECT * FROM "
			+ "(SELECT *, DENSE_RANK() OVER (ORDER BY dlastmodifiedtime DESC) offset_ FROM " + "({})"
			+ " result) result_offset " + "WHERE offset_ > ? AND offset_ <= ?";

	public static final String DEMAND_QUERY_ORDER_BY_CLAUSE = "dmd.taxperiodfrom";

	public static String INSERT_ARCHIVE_DEMANDS_QUERY = " INSERT INTO public.egbs_demand_v1_archive "
			+ "(id, consumercode, consumertype, businessservice, payer, taxperiodfrom, taxperiodto, createdby, createdtime, lastmodifiedby, lastmodifiedtime, tenantid, minimumamountpayable, status, additionaldetails, billexpirytime, ispaymentcompleted, fixedbillexpirydate,archivaltime) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

	public static String INSERT_ARCHIVE_DEMAND_DETAILS_QUERY = " INSERT INTO public.egbs_demanddetail_v1_archive "
			+ "(id, demandid, taxheadcode, taxamount, collectionamount, createdby, createdtime, lastmodifiedby, lastmodifiedtime, tenantid, additionaldetails, archivaltime) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

	public static String DELETE_DEMAND_SQL_QUERY = "DELETE FROM egbs_demand_v1_bkp WHERE id in (:demandIds)";

	public static String DELETE_DEMAND_DETAILS_SQL_QUERY = "DELETE FROM egbs_demanddetail_v1_bkp WHERE demandid in (:demandIds)";

	public static final String GET_DEMAND_COUNT_QUERY = "SELECT count(distinct dmd.id) FROM egbs_demand_v1_bkp dmd   INNER JOIN egbs_demanddetail_v1_bkp dmdl ON dmd.id=dmdl.demandid "
			+ " AND dmd.tenantid=dmdl.tenantid ";

	public String getDemandQuery(DemandCriteria demandCriteria, Map<String,Object> preparedStatementValues) {

		StringBuilder demandQuery = new StringBuilder(BASE_DEMAND_QUERY);
		
		if (demandCriteria.getTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append(" dmd.tenantid = :tenantId ");
			demandQuery.append(" and dmdl.tenantid = :tenantId ");
			preparedStatementValues.put("tenantId",demandCriteria.getTenantId());
		}

		if (demandCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.status = :status");
			preparedStatementValues.put("status",demandCriteria.getStatus());
		}

		if (demandCriteria.getDemandId() != null && !CollectionUtils.isEmpty(demandCriteria.getDemandId())) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.id IN (:demandId)");
			preparedStatementValues.put("demandId",demandCriteria.getDemandId());
		}
		if (!CollectionUtils.isEmpty(demandCriteria.getPayer())) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.payer IN (:payer)");
			preparedStatementValues.put("payer",demandCriteria.getPayer());
		}
		if (!CollectionUtils.isEmpty(demandCriteria.getBusinessServices())) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.businessservice IN (:businessservice)");
			preparedStatementValues.put("businessservice",demandCriteria.getBusinessServices());
		}

		if (demandCriteria.getIsPaymentCompleted() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.ispaymentcompleted = :isPaymentCompleted");
			preparedStatementValues.put("isPaymentCompleted",demandCriteria.getIsPaymentCompleted());
		}

		if (demandCriteria.getPeriodFrom() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodFrom >= :taxPeriodFrom");
			preparedStatementValues.put("taxPeriodFrom",demandCriteria.getPeriodFrom());
		}

		if (demandCriteria.getPeriodTo() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodTo <= :taxPeriodTo");
			preparedStatementValues.put("taxPeriodTo",demandCriteria.getPeriodTo());
		}

		if (demandCriteria.getArchiveTillDate() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodFrom <= :archivalTillDate");
			preparedStatementValues.put("archivalTillDate",demandCriteria.getArchiveTillDate());
		}

		if (demandCriteria.getConsumerCode() != null && !demandCriteria.getConsumerCode().isEmpty()) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.consumercode IN (:consumerCode)");
			preparedStatementValues.put("consumerCode",demandCriteria.getConsumerCode());
		}

		addOrderByClause(demandQuery, DEMAND_QUERY_ORDER_BY_CLAUSE);
		return demandQuery.toString();

	}

	public String getDemandCountQuery(DemandCriteria demandCriteria, Map<String, Object> preparedStatementValues) {

		StringBuilder demandQuery = new StringBuilder(GET_DEMAND_COUNT_QUERY);

		if (!CollectionUtils.isEmpty(demandCriteria.getBusinessServices())) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.businessservice IN (:businessService)" );
			preparedStatementValues.put("businessService",demandCriteria.getBusinessServices());
		}

		if (demandCriteria.getIsPaymentCompleted() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.ispaymentcompleted = :ispaymentcompleted");
			preparedStatementValues.put("ispaymentcompleted",demandCriteria.getIsPaymentCompleted());
		}

		if (demandCriteria.getPeriodFrom() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodFrom >= :taxPeriodFrom");
			preparedStatementValues.put("taxPeriodFrom",demandCriteria.getPeriodFrom());
		}

		if (demandCriteria.getPeriodTo() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodTo <= :taxPeriodTo");
			preparedStatementValues.put("taxPeriodTo",demandCriteria.getPeriodTo());
		}

		if (demandCriteria.getArchiveTillDate() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodFrom <= :taxPeriodFrom");
			preparedStatementValues.put("taxPeriodFrom",demandCriteria.getArchiveTillDate());
		}
		
		if (demandCriteria.getArchiveTillDate() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodFrom <= :archivalTillDate");
			preparedStatementValues.put("archivalTillDate",demandCriteria.getArchiveTillDate());
		}

		if (demandCriteria.getConsumerCode() != null && !demandCriteria.getConsumerCode().isEmpty()) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.consumercode IN (:consumercode) ");
			preparedStatementValues.put("consumercode",demandCriteria.getConsumerCode());
		}
		
		if (demandCriteria.getTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append(" dmd.tenantid = :tenantId ");
			preparedStatementValues.put("tenantId",demandCriteria.getTenantId());
		}

		return demandQuery.toString();

	}

	private static boolean addAndClause(StringBuilder queryString) {
		queryString.append(" AND ");
		return true;
	}

	private static void addOrderByClause(StringBuilder demandQueryBuilder, String columnName) {
		demandQueryBuilder.append(" ORDER BY " + columnName);
	}

	private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND ");
		}
	}

	private static String getIdQueryForStrings(Set<String> idList) {
		 StringBuilder query = new StringBuilder();
		    if (!idList.isEmpty()) {
		        // Use LinkedHashSet to maintain insertion order
		        Set<String> linkedHashSet = new LinkedHashSet<>(idList);

		        String[] list = linkedHashSet.toArray(new String[linkedHashSet.size()]);
		        query.append("'" + list[0] + "'");
		        for (int i = 1; i < list.length; i++) {
		            query.append("," + "'" + list[i] + "'");
		        }
		    }
		    return query.insert(0, "(").append(")").toString();
	}

	private void addPaginationIfRequired(StringBuilder demandQuery, Integer limit, Integer offset,
			List<Object> preparedStatementValues) {

		if (limit != null && offset != null) {
			demandQuery.append(" limit ? ");
			preparedStatementValues.add(limit);

			demandQuery.append(" offset ? ");
			preparedStatementValues.add(offset);
		}

	}

}
