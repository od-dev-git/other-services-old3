package org.egov.dss.repository.builder;

import java.util.Map;

import org.egov.dss.model.UsmSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class USMQueryBuilder {

	public static final String USM_TOTAL_SUBMITTED_FEEDBACK = "select count(submit.id)  from eg_usm_survey_submitted  submit";
	public static final String USM_TOTAL_SUBMITTED_FEEDBACK_CATEGORYWISE = "select submit.tenantid as tenantid , count(distinct(submit.id)) as totalamt  from eg_usm_survey_submitted submit left join eg_usm_survey_submitted_answer answer on answer.surveysubmittedid  = submit .id ";
	public static final String USM_TOTAL_ISSUE = "select count(*) from eg_usm_survey_ticket ticket left join eg_usm_survey_submitted_answer answer on ticket.surveyanswerid  = answer.id left join eg_usm_survey_submitted submit on answer.surveysubmittedid  = submit .id ";

	public static final String USM_TOTAL_SLUM_SUBMITED_FEEDBACK = "select count(distinct (tenantid ,slumcode))from eg_usm_survey_submitted submit";
	public static final String USM_TOP_ISSUE_CATEGORY = "select answer.questioncategory as name, count(ticket.id) as value from eg_usm_survey_submitted_answer answer inner join eg_usm_survey_ticket ticket   on answer.id = ticket.surveyanswerid";
	public static final String USM_TOTAL_FEEDBACK = "SELECT submit.tenantid as tenantid, COUNT(*) as totalamt FROM eg_usm_survey_submitted submit ";

	public static final String USM_TOTAL_OPEN_ISSUES = "SELECT ticket.tenantid as tenantid, SUM(CASE WHEN ticket.status = 'OPEN' THEN 1 ELSE 0 END) as totalamt FROM eg_usm_survey_ticket ticket JOIN eg_usm_survey_submitted_answer answer ON answer.id = ticket.surveyanswerid";

	public static final String USM_TOTAL_CLOSED_ISSUES = "SELECT ticket.tenantid as tenantid, SUM(CASE WHEN ticket.status = 'CLOSED' THEN 1 ELSE 0 END) as totalamt FROM eg_usm_survey_ticket ticket JOIN eg_usm_survey_submitted_answer answer ON answer.id = ticket.surveyanswerid ";

	public static final String USM_CUMULATIVE_APPLICATIONS = "select to_char(monthYear, 'Mon-YYYY') as name, sum(noOffeedback) over (order by monthYear asc rows between unbounded preceding and current row) as value from (select to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') monthYear , count(id) noOffeedback from eg_usm_survey_submitted submit";

	public static final String USM_TOTAL_ISSUE_TENANTWISE = "select ticket.tenantid as name , count(*) as value from eg_usm_survey_ticket ticket";

	public static final String USM_TOTAL_CATEGORY_WISE_ISSUE_COUNT = "select answer.questioncategory as name, count(ticket.id) as value from eg_usm_survey_submitted_answer answer inner join eg_usm_survey_ticket ticket  on answer.id = ticket.surveyanswerid";

	public static final String USM_TOTAL_CLOSED_WITH_SATISFACTORY = "SELECT  SUM(case when ticket.status = 'CLOSED' and ticket.issatisfied = true then 1 else 0 end) as totalclosedsatisfactory FROM eg_usm_survey_ticket ticket";

	public static final String USM_TOTAL_UNATTENDED_ISSUE = "SELECT  SUM(CASE WHEN ticket.status = 'OPEN' and ticket.unattended  = true THEN 1 ELSE 0 END) as totalamt  FROM eg_usm_survey_ticket ticket";

	public static final String USM_TOTAL_ESCALATED_ISSUE = "select  SUM(CASE WHEN ticket.status = 'CLOSED' AND ticket.issatisfied  = false  THEN 1 ELSE 0 END) as totalamt FROM eg_usm_survey_ticket ticket";

	public static final String USM_TOTAL_ESCALATED_RESPONDED_ISSUE = "select count(distinct(comm.ticketid)) from eg_usm_survey_ticket_comment comm inner join eg_usm_survey_ticket ticket on ticket .id = comm.ticketid ";

	public static final String USM_TOTAL_TICKET_CREATED = "select submit.tenantid as tenantid , count(*) as totalamt  from eg_usm_survey_ticket ticket left join eg_usm_survey_submitted_answer answer on ticket.surveyanswerid  = answer.id left join eg_usm_survey_submitted submit on answer.surveysubmittedid  = submit .id ";

	public static String getTotalFeebackSubmitted(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_SUBMITTED_FEEDBACK);
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}

	public static String getCategoryWiseIssueCount(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_CATEGORY_WISE_ISSUE_COUNT);
		addWhereClauseForTicket(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, " name ");
		return selectQuery.toString();
	}

	public static String getTotalOpenIssue(UsmSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_ISSUE);
		selectQuery.append(" where ticket.status = :status");
		preparedStatementValues.put("status", "OPEN");
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}

	public static String getTotalClosedIssue(UsmSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_ISSUE);
		selectQuery.append(" where ticket.status = :status");
		preparedStatementValues.put("status", "CLOSED");
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}

	public static String getTopIssueByCategory(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOP_ISSUE_CATEGORY);
		addWhereClauseForTicket(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, " name ");
		return selectQuery.toString();
	}

	public static String getTotalSlumSubmittedFeedback(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_SLUM_SUBMITED_FEEDBACK);
		return addWhereClause(selectQuery, preparedStatementValues, criteria);
	}

	public String getTotalFeedbackByTenantWise(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_FEEDBACK);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid");
		return selectQuery.toString();
	}

	public String getTotalClosedTicketByTenantWise(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_CLOSED_ISSUES);
		addWhereClauseForTicket(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "ticket.tenantid");
		return selectQuery.toString();
	}

	public String getTotalOpenTicketByTenantWise(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_OPEN_ISSUES);
		addWhereClauseForTicket(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "ticket.tenantid");
		return selectQuery.toString();
	}

	public String getTotalIssueTenantWise(UsmSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_ISSUE_TENANTWISE);
		addWhereClauseForTicket(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "ticket.tenantid");
		return selectQuery.toString();
	}

	public String getTotalUnattendedTicketByTenantWise(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where ticket.status = 'OPEN' and ticket.unattended = true ");
		preparedStatementValues.put("unattended", true);
		addWhereClauseForTicket(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid");
		return selectQuery.toString();
	}

	public String getTotalSatisfiedTicketByTenantWise(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where ticket.status = 'CLOSED' and ticket.issatisfied  = true");
		preparedStatementValues.put("status", "'CLOSED'");
		preparedStatementValues.put("issatisfied", true);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}
	

	public String getTotalDissatisfiedTicketByTenantWise(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where ticket.status = 'CLOSED' and ticket.issatisfied  = false");
		preparedStatementValues.put("status", "'CLOSED'");
		preparedStatementValues.put("issatisfied", false);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getCumulativeApplications(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_CUMULATIVE_APPLICATIONS);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery,
				" to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') ");
		addOrderByClause(selectQuery,
				" to_date(concat('01-',EXTRACT(MONTH FROM to_timestamp(lastmodifiedtime/1000)),'-' ,EXTRACT(YEAR FROM to_timestamp(lastmodifiedtime/1000))),'DD-MM-YYYY') asc) mrTmp ");
		return selectQuery.toString();
	}

	public static String getTotalClosedSatisfactoryIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_CLOSED_WITH_SATISFACTORY);
		return addWhereClauseForTicket(selectQuery, preparedStatementValues, criteria);
	}

	public static String getTotalUnattendedIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_UNATTENDED_ISSUE);
		return addWhereClauseForTicket(selectQuery, preparedStatementValues, criteria);
	}

	public static String getTotalEscalatedIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_ESCALATED_ISSUE);
		return addWhereClauseForTicket(selectQuery, preparedStatementValues, criteria);
	}

	public static String getTotalEscalatedRespondedIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_ESCALATED_RESPONDED_ISSUE);
		return addWhereClauseForTicket(selectQuery, preparedStatementValues, criteria);
	}

	public static String getTotalWaterFeedbackSubmitted(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_SUBMITTED_FEEDBACK_CATEGORYWISE);
		selectQuery.append(" where upper(answer.questioncategory) = 'WATER' ");
		preparedStatementValues.put("questioncategory", "WATER");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalClosedIssueWater(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'WATER' and ticket.status = 'CLOSED' ");
		preparedStatementValues.put("questioncategory", "'WATER'");
		preparedStatementValues.put("status", "'CLOSED'");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}
	
	public static String getTotalOpenIssueWater(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'WATER' and upper(ticket.status) = 'OPEN' ");
		preparedStatementValues.put("questioncategory", "'WATER'");
		preparedStatementValues.put("status", "'OPEN'");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfWaterIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'WATER' and upper(answer.answer) = 'NO'");
		preparedStatementValues.put("questioncategory", "'WATER'");
		preparedStatementValues.put("answer", "'NO'");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfUnattendedWaterIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'WATER' and ticket.status  = 'OPEN' and ticket.unattended  = true");
		preparedStatementValues.put("questioncategory", "'WATER'");
		preparedStatementValues.put("status", "'OPEN'");
		preparedStatementValues.put("unattended", true);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfSatisfactoryWaterIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'WATER' and ticket.status = 'CLOSED' and ticket.issatisfied  = true");
		preparedStatementValues.put("questioncategory", "'WATER'");
		preparedStatementValues.put("status", "'CLOSED'");
		preparedStatementValues.put("issatisfied", true);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfDissatisfactoryWaterIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'WATER' and ticket.status = 'CLOSED' and ticket.issatisfied  = false");
		preparedStatementValues.put("questioncategory", "'WATER'");
		preparedStatementValues.put("status", "'CLOSED'");
		preparedStatementValues.put("issatisfied", false);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalStreetlightFeedbackSubmitted(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_SUBMITTED_FEEDBACK_CATEGORYWISE);
		selectQuery.append(" where upper(answer.questioncategory) = 'STREETLIGHT' ");
		preparedStatementValues.put("questioncategory", "STREETLIGHT");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalClosedIssueStreetlight(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'STREETLIGHT' and ticket.status = 'CLOSED' ");
		preparedStatementValues.put("questioncategory", "'STREETLIGHT'");
		preparedStatementValues.put("status", "'CLOSED'");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}
	
	public static String getTotalOpenIssueStreetlight(UsmSearchCriteria criteria, Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'STREETLIGHT' and upper(ticket.status) = 'OPEN' ");
		preparedStatementValues.put("questioncategory", "'STREETLIGHT'");
		preparedStatementValues.put("status", "'OPEN'");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfStreetlighIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'STREETLIGHT' and answer.answer = 'NO'");
		preparedStatementValues.put("questioncategory", "'STREETLIGHT'");
		preparedStatementValues.put("answer", "'NO'");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfUnattendedstreetlightIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'STREETLIGHT' and ticket.status  = 'OPEN' and ticket.unattended  = true ");
		preparedStatementValues.put("questioncategory", "'STREETLIGHT'");
		preparedStatementValues.put("status", "'OPEN'");
		preparedStatementValues.put("unattended", true);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfSatisfactoryStreetlightIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'STREETLIGHT' and ticket.status  = 'CLOSED' and ticket.issatisfied  = true");
		preparedStatementValues.put("questioncategory", "'STREETLIGHT'");
		preparedStatementValues.put("status", "'CLOSED'");
		preparedStatementValues.put("issatisfied", true);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfDissatisfactoryStreetlightIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'STREETLIGHT' and ticket.status  = 'CLOSED' and ticket.issatisfied  = false");
		preparedStatementValues.put("questioncategory", "'STREETLIGHT'");
		preparedStatementValues.put("status", "'CLOSED'");
		preparedStatementValues.put("issatisfied", false);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalSanitationFeedbackSubmitted(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_SUBMITTED_FEEDBACK_CATEGORYWISE);
		selectQuery.append(" where upper(answer.questioncategory) = 'SANITATION' ");
		preparedStatementValues.put("questioncategory", "SANITATION");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalClosedIssueSanitation(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'SANITATION' and ticket.status = 'CLOSED' ");
		preparedStatementValues.put("questioncategory", "'SANITATION'");
		preparedStatementValues.put("status", "'CLOSED'");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}
	
	
	public String getTotalOpenIssueSanitation(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'SANITATION' and upper(ticket.status) = 'OPEN'");
		preparedStatementValues.put("questioncategory", "'SANITATION'");
		preparedStatementValues.put("status", "'OPEN'");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfSanitationIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'SANITATION' and answer.answer = 'NO'");
		preparedStatementValues.put("questioncategory", "'SANITATION'");
		preparedStatementValues.put("answer", "'NO'");
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfUnattendedSanitationIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'SANITATION' and ticket.status  = 'OPEN' and ticket.unattended  = true ");
		preparedStatementValues.put("questioncategory", "'SANITATION'");
		preparedStatementValues.put("status", "'OPEN'");
		preparedStatementValues.put("unattended", true);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfSatisfactorySanitationIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'SANITATION' and ticket.status  = 'CLOSED' and ticket.issatisfied  = true");
		preparedStatementValues.put("questioncategory", "'SANITATION'");
		preparedStatementValues.put("status", "'CLOSED'");
		preparedStatementValues.put("issatisfied", true);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	public static String getTotalNoOfDissatisfactorySanitationIssue(UsmSearchCriteria criteria,
			Map<String, Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(USM_TOTAL_TICKET_CREATED);
		selectQuery.append(" where upper(answer.questioncategory) = 'SANITATION' and ticket.status  = 'CLOSED' and ticket.issatisfied  = false");
		preparedStatementValues.put("questioncategory", "'SANITATION'");
		preparedStatementValues.put("status", "'CLOSED'");
		preparedStatementValues.put("issatisfied", false);
		addWhereClause(selectQuery, preparedStatementValues, criteria);
		addGroupByClause(selectQuery, "submit.tenantid ");
		return selectQuery.toString();
	}

	private static String addWhereClause(StringBuilder selectQuery, Map<String, Object> preparedStatementValues,
			UsmSearchCriteria searchCriteria) {

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" submit.tenantId IN ( :tenantId )");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
		}
		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" submit.createdtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" submit.createdtime <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}

		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" submit.lastmodifiedtime - submit.createdtime < " + searchCriteria.getSlaThreshold());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" submit.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}

		return selectQuery.toString();

	}

	private static String addWhereClauseForTicket(StringBuilder selectQuery,
			Map<String, Object> preparedStatementValues, UsmSearchCriteria searchCriteria) {

		if (searchCriteria.getTenantIds() != null && !CollectionUtils.isEmpty(searchCriteria.getTenantIds())) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ticket.tenantId IN ( :tenantId )");
			preparedStatementValues.put("tenantId", searchCriteria.getTenantIds());
		}
		if (searchCriteria.getFromDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ticket.createdtime >= :fromDate");
			preparedStatementValues.put("fromDate", searchCriteria.getFromDate());
		}

		if (searchCriteria.getToDate() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ticket.createdtime <= :toDate");
			preparedStatementValues.put("toDate", searchCriteria.getToDate());
		}

		if (searchCriteria.getSlaThreshold() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ticket.lastmodifiedtime - submit.createdtime < " + searchCriteria.getSlaThreshold());
		}

		if (searchCriteria.getExcludedTenantId() != null) {
			addClauseIfRequired(preparedStatementValues, selectQuery);
			selectQuery.append(" ticket.tenantId != :excludedTenantId");
			preparedStatementValues.put("excludedTenantId", searchCriteria.getExcludedTenantId());
		}

		return selectQuery.toString();

	}

	private static void addClauseIfRequired(Map<String, Object> values, StringBuilder queryString) {
		if (values.isEmpty())
			queryString.append(" WHERE ");
		else {
			queryString.append(" AND");
		}
	}

	private static void addGroupByClause(StringBuilder query, String columnName) {
		query.append(" GROUP BY " + columnName);
	}

	private static void addOrderByClause(StringBuilder demandQueryBuilder, String columnName) {
		demandQueryBuilder.append(" ORDER BY " + columnName);
	}

}
