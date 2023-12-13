package org.egov.arc.repository.builder;

import java.util.List;
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
			+ "FROM egbs_demand_v1 dmd " + "INNER JOIN egbs_demanddetail_v1 dmdl ON dmd.id=dmdl.demandid "
			+ "AND dmd.tenantid=dmdl.tenantid ";

	private final String paginationWrapper = "SELECT * FROM "
			+ "(SELECT *, DENSE_RANK() OVER (ORDER BY dlastmodifiedtime DESC) offset_ FROM " + "({})"
			+ " result) result_offset " + "WHERE offset_ > ? AND offset_ <= ?";

	public static final String DEMAND_QUERY_ORDER_BY_CLAUSE = "dmd.taxperiodfrom";

	public static String INSERT_ARCHIVE_DEMANDS_QUERY = " INSERT INTO public.egbs_demand_v1_archive "
			+ "(id, consumercode, consumertype, businessservice, payer, taxperiodfrom, taxperiodto, createdby, createdtime, lastmodifiedby, lastmodifiedtime, tenantid, minimumamountpayable, status, additionaldetails, billexpirytime, ispaymentcompleted, fixedbillexpirydate, archivaltime) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

	public static String INSERT_ARCHIVE_DEMAND_DETAILS_QUERY = " INSERT INTO public.egbs_demanddetail_v1_archive "
			+ "(id, demandid, taxheadcode, taxamount, collectionamount, createdby, createdtime, lastmodifiedby, lastmodifiedtime, tenantid, additionaldetails, archivaltime) "
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

	public static String DELETE_DEMAND_SQL_QUERY = "DELETE FROM egbs_demand_v1 WHERE id in (:demandIds)";

	public static String DELETE_DEMAND_DETAILS_SQL_QUERY = "DELETE FROM egbs_demanddetail_v1 WHERE demandid in (:demandIds)";

	public static final String GET_DEMAND_COUNT_QUERY = "SELECT count(*) FROM egbs_demand_v1 dmd   INNER JOIN egbs_demanddetail_v1 dmdl ON dmd.id=dmdl.demandid "
			+ " AND dmd.tenantid=dmdl.tenantid ";

	public String getDemandQuery(DemandCriteria demandCriteria, List<Object> preparedStatementValues) {

		StringBuilder demandQuery = new StringBuilder(BASE_DEMAND_QUERY);

//		String tenantId = demandCriteria.getTenantId();
//		String[] tenantIdChunks = tenantId.split("\\.");
//
//		if (tenantIdChunks.length == 1) {
//			demandQuery.append(" dmd.tenantid LIKE ? ");
//			preparedStatementValues.add(demandCriteria.getTenantId() + '%');
//		} else {
//			demandQuery.append(" dmd.tenantid = ? ");
//			preparedStatementValues.add(demandCriteria.getTenantId());
//		}

		if (demandCriteria.getStatus() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.status=?");
			preparedStatementValues.add(demandCriteria.getStatus());
		}

		if (demandCriteria.getDemandId() != null && !CollectionUtils.isEmpty(demandCriteria.getDemandId())) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.id IN (" + getIdQueryForStrings(demandCriteria.getDemandId()));
		}
		if (!CollectionUtils.isEmpty(demandCriteria.getPayer())) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.payer IN (" + getIdQueryForStrings(demandCriteria.getPayer()));
		}
		if (!CollectionUtils.isEmpty(demandCriteria.getBusinessServices())) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.businessservice IN (" + getIdQueryForStrings(demandCriteria.getBusinessServices()));
		}

		if (demandCriteria.getIsPaymentCompleted() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.ispaymentcompleted = ?");
			preparedStatementValues.add(demandCriteria.getIsPaymentCompleted());
		}

		if (demandCriteria.getPeriodFrom() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodFrom >= ?");
			preparedStatementValues.add(demandCriteria.getPeriodFrom());
		}

		if (demandCriteria.getPeriodTo() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodTo <= ?");
			preparedStatementValues.add(demandCriteria.getPeriodTo());
		}

		if (demandCriteria.getArchiveTillDate() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodFrom <= ?");
			preparedStatementValues.add(demandCriteria.getArchiveTillDate());
		}

		if (demandCriteria.getConsumerCode() != null && !demandCriteria.getConsumerCode().isEmpty()) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.consumercode IN (" + getIdQueryForStrings(demandCriteria.getConsumerCode()));
		}

		addOrderByClause(demandQuery, DEMAND_QUERY_ORDER_BY_CLAUSE);
		addPaginationIfRequired(demandQuery, demandCriteria.getLimit(), demandCriteria.getOffset(),
				preparedStatementValues);

		return demandQuery.toString();

	}

	public String getDemandCountQuery(DemandCriteria demandCriteria, List<Object> preparedStatementValues) {

		StringBuilder demandQuery = new StringBuilder(GET_DEMAND_COUNT_QUERY);

		if (!CollectionUtils.isEmpty(demandCriteria.getBusinessServices())) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.businessservice IN (" + getIdQueryForStrings(demandCriteria.getBusinessServices()));
		}

		if (demandCriteria.getIsPaymentCompleted() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.ispaymentcompleted = ?");
			preparedStatementValues.add(demandCriteria.getIsPaymentCompleted());
		}

		if (demandCriteria.getPeriodFrom() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodFrom >= ?");
			preparedStatementValues.add(demandCriteria.getPeriodFrom());
		}

		if (demandCriteria.getPeriodTo() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodTo <= ?");
			preparedStatementValues.add(demandCriteria.getPeriodTo());
		}

		if (demandCriteria.getArchiveTillDate() != null) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.taxPeriodFrom <= ?");
			preparedStatementValues.add(demandCriteria.getArchiveTillDate());
		}

		if (demandCriteria.getConsumerCode() != null && !demandCriteria.getConsumerCode().isEmpty()) {
			addClauseIfRequired(preparedStatementValues, demandQuery);
			demandQuery.append("dmd.consumercode IN (" + getIdQueryForStrings(demandCriteria.getConsumerCode()));
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

	private static void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}

	private static String getIdQueryForStrings(Set<String> idList) {

		StringBuilder query = new StringBuilder();
		if (!idList.isEmpty()) {

			String[] list = idList.toArray(new String[idList.size()]);
			query.append("'" + list[0] + "'");
			for (int i = 1; i < idList.size(); i++) {
				query.append("," + "'" + list[i] + "'");
			}
		}
		return query.append(")").toString();
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
