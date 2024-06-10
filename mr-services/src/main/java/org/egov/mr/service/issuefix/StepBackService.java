package org.egov.mr.service.issuefix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.mr.repository.IssueFixRepository;
import org.egov.mr.service.MarriageRegistrationService;
import org.egov.mr.util.IssueFixConstants;
import org.egov.mr.validator.IssueFixValidator;
import org.egov.mr.web.models.MarriageRegistration;
import org.egov.mr.web.models.MarriageRegistrationSearchCriteria;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.egov.mr.web.models.workflow.ProcessInstance;
import org.egov.mr.web.models.workflow.WorkFlowSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Service("stepBackService")
@Slf4j
public class StepBackService implements IIssueFixService {

	@Autowired
	private IssueFixValidator issueFixValidator;

	@Autowired
	private IssueFixRepository issueFixRepository;

	@Autowired
	private MarriageRegistrationService marriageRegistrationService;

	@Override
	public IssueFix issueFix(IssueFixRequest issueFixRequest, HttpHeaders headers) {
		Map<String, Boolean> isDataUpdateNeeded = new HashMap<>();
		issueFixValidator.validateIssueFixRequest(issueFixRequest.getIssueFix(), headers);
		MarriageRegistrationSearchCriteria searchCriteria = getSearchCriteria(issueFixRequest);
		
		List<MarriageRegistration> marriageRegistrations = marriageRegistrationService.search(searchCriteria,
				issueFixRequest.getRequestInfo(), "MR", headers);
		
		if (CollectionUtils.isEmpty(marriageRegistrations) || marriageRegistrations.size() > 1) {
			throw new CustomException("SEARCH_ERROR", "Either no or multiple applications found with the mentioned application number !!");
		}
		
		ProcessInstance processInstance = checkIfWorkFlowUpdatedProperly(issueFixRequest, isDataUpdateNeeded);

		MarriageRegistration applicationToBeUpdated = checkIfApplicationUpdatedProperly(issueFixRequest,
				marriageRegistrations.get(0), isDataUpdateNeeded);

		updateDataIfNeeded( processInstance, applicationToBeUpdated, isDataUpdateNeeded);
		
		return issueFixRequest.getIssueFix();
	}
	
	
	/**
	 * @param issueFixRequest
	 * @param isDataUpdateNeeded
	 * @return
	 */
	private ProcessInstance checkIfWorkFlowUpdatedProperly(IssueFixRequest issueFixRequest,
			Map<String, Boolean> isDataUpdateNeeded) {

		String tenantId = issueFixRequest.getIssueFix().getTenantId();

		String applicationNumber = issueFixRequest.getIssueFix().getApplicationNo();

		WorkFlowSearchCriteria workFlowSearchCriteria = WorkFlowSearchCriteria.builder()
				.businessId(applicationNumber)
				.tenantId(tenantId)
				.build();

		List<ProcessInstance> processInstances = issueFixRepository.getProcessInstances(workFlowSearchCriteria);

		if (CollectionUtils.isEmpty(processInstances)) {
			throw new CustomException("WORKFLOW_ISSUE", "Found No Workflow records for mentioned Application Number !!");
		}

		ProcessInstance lastestProcessInstance = processInstances.get(0);

		if (!CollectionUtils.isEmpty(processInstances)) {
			lastestProcessInstance = processInstances.get(0);

			if (lastestProcessInstance.getAction().equalsIgnoreCase(IssueFixConstants.ACTION_APPROVE)) {
				isDataUpdateNeeded.put(IssueFixConstants.IS_WORKFLOW_DELETE_NEEDED, true);
			} else {
				log.info("Process instance is not found for action APPROVE");
			}
		}
		
		return lastestProcessInstance;
	}
	

	/**
	 * @param demandToBeUpdated
	 * @param processInstance
	 * @param applicationToBeUpdated
	 * @param payment
	 * @param isDataUpdateNeeded
	 */
	@Transactional
	private void updateDataIfNeeded(ProcessInstance processInstance,
			MarriageRegistration applicationToBeUpdated, Map<String, Boolean> isDataUpdateNeeded) {

		
		if (isDataUpdateNeeded.containsKey(IssueFixConstants.IS_APPLICATION_UPDATE_NEEDED)
				&& isDataUpdateNeeded.get(IssueFixConstants.IS_APPLICATION_UPDATE_NEEDED)) {
			issueFixRepository.updateApplicationForStepBack(applicationToBeUpdated);
		}
		
		if (isDataUpdateNeeded.containsKey(IssueFixConstants.IS_WORKFLOW_UPDATE_NEEDED)
				&& isDataUpdateNeeded.get(IssueFixConstants.IS_WORKFLOW_UPDATE_NEEDED)) {
			issueFixRepository.updateWorkflowForStepBack(processInstance);
		}

	}

	
	/**
	 * @param issueFixRequest
	 * @param marriageRegistration
	 * @param isDataUpdateNeeded
	 * @return
	 */
	private MarriageRegistration checkIfApplicationUpdatedProperly(IssueFixRequest issueFixRequest,
			MarriageRegistration marriageRegistration, Map<String, Boolean> isDataUpdateNeeded) {

		if (marriageRegistration.getStatus().equalsIgnoreCase(IssueFixConstants.STATUS_APPROVED)
				&& IssueFixConstants.APPLICATION_TYPE.contains(marriageRegistration.getApplicationType().toString())) {
			isDataUpdateNeeded.put(IssueFixConstants.IS_APPLICATION_UPDATE_NEEDED, true);
			
		} else {
			throw new CustomException("APPLICATION_UPDATE_ISSUE", "Current Status of the application is not in " + IssueFixConstants.STATUS_APPROVED + " step");
		}
		
		return marriageRegistration;
	}

}
