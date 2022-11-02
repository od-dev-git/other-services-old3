package org.egov.integration.repository.builder;

import java.util.List;

import org.egov.integration.config.IntegrationConfiguration;
import org.egov.integration.model.revenue.RevenueNotificationSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RevenueNotificationQueryBuilder {

	@Autowired
	private IntegrationConfiguration config;

	private static final String SELECT = " SELECT ";
	private static final String INNER_JOIN = " INNER JOIN ";
	private static final String AND = " AND ";
	private static final String FROM = " from ";
	private static final String WHERE = "where ";
	private static final String ON = " on ";
	private static final String AS = " as ";
	private static final String IN = " in ";
	private static final String GROUP_BY = " group by ";
	private static final String ORDER_BY = " order by ";

	private final String paginationWrapper = "SELECT * FROM "
			+ "(SELECT *, DENSE_RANK() OVER (ORDER BY lastmodifiedtime DESC ) offset_ FROM " + "({})"
			+ " result) result_offset " + "WHERE offset_ > ? AND offset_ <= ?";

	private static final String revenueNotificationValues = " rn.id, rn.districtname, rn.tenantid, rn.revenuevillage, rn.plotno, rn.flatno,"
			+ " rn.address, rn.currentownername, rn.currentownermobilenumber, rn.newownername, rn.newownermobilenumber, rn.actiontaken, "
			+ " rn.action , rn.additionaldetails, rn.createdby, rn.createdtime, rn.lastmodifiedby, rn.lastmodifiedtime";

	private static final String revenueNotificationTable = " eg_uis_revenuenotification rn ";

	private static final String QUERY_FOR_REVENUE_NOTIFICATIONS_SEARCH = SELECT + revenueNotificationValues + FROM
			+ revenueNotificationTable;

	private static final String QUERY_FOR_REVENUE_NOTIFICATIONS_COUNT = SELECT + " rn.id " + FROM
			+ revenueNotificationTable;

	private String addPaginationWrapper(String query, List<Object> preparedStmtList,
			RevenueNotificationSearchCriteria criteria) {
		int limit = config.getDefaultLimit();
		int offset = config.getDefaultOffset();
		String finalQuery = paginationWrapper.replace("{}", query);

		if (criteria.getLimit() != null && criteria.getLimit() <= config.getMaxSearchLimit())
			limit = criteria.getLimit();

		if (criteria.getLimit() != null && criteria.getLimit() > config.getMaxSearchLimit())
			limit = config.getMaxSearchLimit();

		if (criteria.getOffset() != null)
			offset = criteria.getOffset();

		preparedStmtList.add(offset);
		preparedStmtList.add(limit + offset);

		log.info("Final Query : " + finalQuery);
		
		return finalQuery;
	}

	public String getNotificationsSearchQuery(RevenueNotificationSearchCriteria searchCriteria,
			List<Object> preparedStmtList) {

		StringBuilder query = new StringBuilder(QUERY_FOR_REVENUE_NOTIFICATIONS_SEARCH);

		query.append(WHERE).append(" rn.tenantid = ? ");
		preparedStmtList.add(searchCriteria.getTenantId());

		return addPaginationWrapper(query.toString(), preparedStmtList, searchCriteria);

	}

}
