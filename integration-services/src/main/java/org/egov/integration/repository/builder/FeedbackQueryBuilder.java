package org.egov.integration.repository.builder;

import org.egov.integration.config.IntegrationConfiguration;
import org.egov.integration.web.model.FeedbackSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class FeedbackQueryBuilder {

    @Autowired
    private IntegrationConfiguration configuration;

    private static final String PAGINATION_WRAPPER = "SELECT * FROM " +
            "(SELECT *, DENSE_RANK() OVER (ORDER BY id) offset_ FROM " +
            "({})" +
            " result) result_offset " +
            "WHERE offset_ > ? AND offset_ <= ?";

    private static final String FEEDBACK_SEARCH_QUERY="SELECT euf.* FROM eg_uis_feedback euf ";

    public String getSearchQueryString(FeedbackSearchCriteria criteria, List<Object> preparedStatement){
        if (criteria.isEmpty())
            return null;
        StringBuilder query = new StringBuilder(FEEDBACK_SEARCH_QUERY);


        if (!StringUtils.isEmpty(criteria.getTenantId())) {
            addClauseIfRequired(preparedStatement, query);
            query.append(" euf.tenantid = ? ");
            preparedStatement.add(criteria.getTenantId());
        }

        if (!StringUtils.isEmpty(criteria.getModule())) {
            addClauseIfRequired(preparedStatement, query);
            query.append(" euf.module = ? ");
            preparedStatement.add(criteria.getModule());
        }

        if (!StringUtils.isEmpty(criteria.getSubmittedBy())) {
            addClauseIfRequired(preparedStatement, query);
            query.append(" euf.createdby = ? ");
            preparedStatement.add(criteria.getSubmittedBy());
        }
        
        if(!StringUtils.isEmpty(criteria.getRating())) {
        	addClauseIfRequired(preparedStatement, query);
            query.append(" euf.rating = ? ");
            preparedStatement.add(criteria.getRating());
        }

        return addPaginationWrapper(query.toString(), preparedStatement, criteria);
    }

    private void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
        if (values.isEmpty())
            queryString.append(" WHERE ");
        else {
            queryString.append(" AND");
        }
    }

    private String addPaginationWrapper(String query, List<Object> preparedStmtList, FeedbackSearchCriteria criteria) {
        Integer limit = configuration.getDefaultLimit();
        Integer offset = configuration.getDefaultOffset();
        if (criteria.getLimit() == null && criteria.getOffset() == null)
            limit = configuration.getMaxSearchLimit();

        if (criteria.getLimit() != null && criteria.getLimit() <= configuration.getDefaultLimit())
            limit = criteria.getLimit();

        if (criteria.getLimit() != null && criteria.getLimit() > configuration.getDefaultOffset())
            limit = configuration.getDefaultLimit();

        if (criteria.getOffset() != null)
            offset = criteria.getOffset();

        preparedStmtList.add(offset);
        preparedStmtList.add(limit + offset);
        return PAGINATION_WRAPPER.replace("{}",query);
    }

}
