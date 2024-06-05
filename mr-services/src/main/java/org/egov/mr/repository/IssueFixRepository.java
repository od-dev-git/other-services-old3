package org.egov.mr.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.egov.mr.repository.builder.IssueFixQueryBuilder;
import org.egov.mr.repository.rowmapper.DemandRowMapper;
import org.egov.mr.repository.rowmapper.PaymentRowMapper;
import org.egov.mr.repository.rowmapper.ProcessInstanceRowMapper;
import org.egov.mr.util.IssueFixConstants;
import org.egov.mr.web.models.MarriageRegistration;
import org.egov.mr.web.models.collection.DemandSearchCriteria;
import org.egov.mr.web.models.collection.Payment;
import org.egov.mr.web.models.collection.PaymentSearchCriteria;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.PaymentIssueFix;
import org.egov.mr.web.models.issuefix.StatusMismatchIssueFix;
import org.egov.mr.web.models.workflow.ProcessInstance;
import org.egov.mr.web.models.workflow.WorkFlowSearchCriteria;
import org.egov.mrcalculator.repository.rowmapper.PaymentIssueFixRowMapper;
import org.egov.mrcalculator.repository.rowmapper.StatusMismatchIssueRowMapper;
import org.egov.mrcalculator.web.models.demand.Demand;
import org.egov.mrcalculator.web.models.demand.DemandDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Slf4j
@Repository
public class IssueFixRepository {
	
	@Autowired
	private IssueFixQueryBuilder issueFixQueryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private PaymentIssueFixRowMapper paymentIssueFixRowMapper;
	
	@Autowired
    private StatusMismatchIssueRowMapper statusMismatchIssueRowMapper;
	
	public void deleteCertificate(String applicationNumber) {

		String certificateDeletionQuery = issueFixQueryBuilder.getCertificateDeletionQuery();

		jdbcTemplate.update(certificateDeletionQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, applicationNumber);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public List<String> getDSC(IssueFix issueFix) {

		List<Object> preparedStmtList = new ArrayList<>();

        String query = issueFixQueryBuilder.getDSC(issueFix,preparedStmtList);
        System.out.println("Query: "+query);
        System.out.println("preparedStmtList: "+preparedStmtList);
        List<String> data = jdbcTemplate.queryForList(query,preparedStmtList.toArray(), String.class);
        System.out.println("Data: "+data);
        return data;
	}

	public void updateDSC(IssueFix issueFix) {

		String query = issueFixQueryBuilder.searchDscQuery();
		System.out.println("Query: "+query);
		jdbcTemplate.update(query, preparedStatement -> {
			try {
					preparedStatement.setString(1, issueFix.getApplicationNo());
					preparedStatement.setString(2, issueFix.getTenantId());
					preparedStatement.setString(3, issueFix.getApplicationNo());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public void updateApplicationStatusMismatch(List<String> id){
		List<Object> preparedStatementList = new ArrayList<>();
		String applicationStatusMismatchIssueQuery= issueFixQueryBuilder.getApplicationStatusMismatchIssueQuery(id,preparedStatementList);

		jdbcTemplate.update(applicationStatusMismatchIssueQuery, preparedStatement -> {
			try {
				for(int i=0;i<id.size();i++) {
					preparedStatement.setString(i+1, id.get(i));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public List<Payment> getPayments(PaymentSearchCriteria paymentSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();

		String queryForPaymentSearch = issueFixQueryBuilder.getPaymentSearchQuery(paymentSearchCriteria,
				preparedStatementValues);

		List<Payment> payments = namedParameterJdbcTemplate.query(queryForPaymentSearch, preparedStatementValues,
				new PaymentRowMapper());

		return payments;
	}

	public List<ProcessInstance> getProcessInstances(WorkFlowSearchCriteria workFlowSearchCriteria) {

		Map<String, Object> preparedStatementValues = new HashMap<>();

		String queryForWFSearch = issueFixQueryBuilder.getProcessInstancesQuery(workFlowSearchCriteria,
				preparedStatementValues);

		List<ProcessInstance> processInstances = namedParameterJdbcTemplate.query(queryForWFSearch, preparedStatementValues,
				new ProcessInstanceRowMapper());

		return processInstances;
	}


	public List<Demand> getDemands(DemandSearchCriteria demandSearchCriteria) {

		Map<String, Object> preparedStatementValues = new HashMap<>();

		String queryForDemandSearch = issueFixQueryBuilder.getDemandSearchQuery(demandSearchCriteria,
				preparedStatementValues);

		List<Demand> demands = namedParameterJdbcTemplate.query(queryForDemandSearch, preparedStatementValues,
				new DemandRowMapper());

		return demands;
	}


	public void updateDemandDetail(DemandDetail demandDetail) {

		String updateDemandDetailQuery = issueFixQueryBuilder.getDemandDetailUpdateQuery();

		jdbcTemplate.update(updateDemandDetailQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, demandDetail.getId());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}


	public void updateDemand(Demand demandToBeUpdated) {

		String updateDemandQuery = issueFixQueryBuilder.getDemandUpdateQuery();

		jdbcTemplate.update(updateDemandQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, demandToBeUpdated.getConsumerCode());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}


	public void expireBill(@NotNull String consumerCode) {

		String billExpireQuery = issueFixQueryBuilder.getBillExpireQuery();

		jdbcTemplate.update(billExpireQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, consumerCode);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}


	public void updateApplication(MarriageRegistration applicationToBeUpdated, Payment payment) {

		String updateApplicationQuery = issueFixQueryBuilder.getApplicationUpdateQuery();

		jdbcTemplate.update(updateApplicationQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, IssueFixConstants.STATUS_PAY_PENDING_SCHEDULE);
					ps.setLong(2, payment.getAuditDetails().getCreatedTime());
					ps.setString(3, applicationToBeUpdated.getApplicationNumber());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}


	public void updateWorkflow(ProcessInstance processInstance, Payment payment) {
		String insertWorkFlowQuery = issueFixQueryBuilder.getInsertWorkflowQuery();

		String businessService = processInstance.getBusinessService();

		jdbcTemplate.update(insertWorkFlowQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, UUID.randomUUID().toString());
					ps.setString(2, processInstance.getTenantId());
					ps.setString(3, businessService);
					ps.setString(4, processInstance.getBusinessId());
					
					ps.setString(5, IssueFixConstants.MR_APPLICATION_STATUS);
					
					ps.setString(6, payment.getPayerId());
					ps.setString(7, payment.getPayerId());
					ps.setString(8, payment.getPayerId());
					ps.setLong(9, payment.getAuditDetails().getCreatedTime());
					ps.setLong(10, payment.getAuditDetails().getCreatedTime());

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
    
    public List<PaymentIssueFix> getPaymentIssueApplications() {
		String query = issueFixQueryBuilder.getPaymentIssueAppliactionsQuery();
		log.info("*************" + query);
		List<PaymentIssueFix> paymentIssueFixApplications = jdbcTemplate.query(query, paymentIssueFixRowMapper);
		return paymentIssueFixApplications;
	}
    
	public List<StatusMismatchIssueFix> getStatusMismatchApplications() {
		String query = issueFixQueryBuilder.getStatusMismatchAppliactionsQuery();
		System.out.println("*************" + query);
		List<StatusMismatchIssueFix> statusMismatchIssueFixs = jdbcTemplate.query(query,
				statusMismatchIssueRowMapper);
		return statusMismatchIssueFixs;
	}



	public void updateApplicationForStepBack(MarriageRegistration applicationToBeUpdated) {

		String updateApplicationQuery = issueFixQueryBuilder.getStepBackApplicationUpdateQuery();

		jdbcTemplate.update(updateApplicationQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, IssueFixConstants.STATUS_PENDING_APPROVAL);
					ps.setString(2, IssueFixConstants.ACTION_SCHEDULE);
					ps.setString(3, applicationToBeUpdated.getApplicationNumber());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public void updateWorkflowForStepBack(ProcessInstance processInstance) {

		String insertWorkFlowQuery = issueFixQueryBuilder.getDeleteWorkflowQuery();
		jdbcTemplate.update(insertWorkFlowQuery, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, processInstance.getBusinessId());
					ps.setString(2, processInstance.getId());
					ps.setString(2, IssueFixConstants.ACTION_APPROVE);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
