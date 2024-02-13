package org.egov.mr.service.issuefix;

import lombok.extern.slf4j.Slf4j;
import org.egov.mr.repository.IssueFixRepository;
import org.egov.mr.service.MarriageRegistrationService;
import org.egov.mr.util.MRConstants;
import org.egov.mr.validator.IssueFixValidator;
import org.egov.mr.web.models.MarriageRegistration;
import org.egov.mr.web.models.MarriageRegistrationSearchCriteria;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.egov.mr.web.models.workflow.ProcessInstance;
import org.egov.mr.workflow.WorkflowService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ApplicationStatusMismatchIssueFixService implements IIssueFixService{

    @Autowired
    private IssueFixValidator issueFixValidator;

    @Autowired
    private IssueFixRepository issueFixRepository;

    @Autowired
    private MarriageRegistrationService marriageRegistrationService;

    @Autowired
    private WorkflowService workflowService;

    @Override
    public IssueFix issueFix(IssueFixRequest issueFixRequest, HttpHeaders headers) {

        MarriageRegistrationSearchCriteria searchCriteria = getSearchCriteria(issueFixRequest);
        List<MarriageRegistration> marriageRegistrations = marriageRegistrationService.search(searchCriteria,
                issueFixRequest.getRequestInfo(), "MR", headers);
        log.info("@Class: ApplicationStatusMismatchIssueFixService @method:issueFix @message: Water Search Completed");
        issueFixValidator.validateMarriageRegistrationApplicationStatusMismatch(marriageRegistrations);
        MarriageRegistration marriageRegistration= marriageRegistrations.get(0);
        List<ProcessInstance> processInstance = workflowService.getProcessInstanceForIssueFix(
                issueFixRequest.getRequestInfo(),issueFixRequest.getIssueFix().getApplicationNo(),
                issueFixRequest.getIssueFix().getTenantId(),marriageRegistration.getBusinessService(),true
        );
        log.info("@Class: ApplicationStatusMismatchIssueFixService @method:issueFix @message: Received Process Instances {}",processInstance.toString());
        issueFixValidator.validateProcessInstanceApplicationStatusMismatch(marriageRegistration,processInstance);
        List<String> idList=getProcessInstancesForDeletion(marriageRegistration,processInstance);


        deleteProcessInstance(idList);
        return issueFixRequest.getIssueFix();
    }

    @Transactional
    private void deleteProcessInstance(List<String> idList) {
        issueFixRepository.updateApplicationStatusMismatch(idList);
    }

    private List<String> getProcessInstancesForDeletion(MarriageRegistration marriageRegistration, List<ProcessInstance> processInstanceList) {
        List<String> processInstanceIdList = new ArrayList<>();
        for (ProcessInstance processInstance : processInstanceList) {
            if (processInstance.getState().getApplicationStatus().equalsIgnoreCase(marriageRegistration.getStatus())) {
                break;
            }
            else {
                checkIfInstanceIsOfPayment(processInstance);
                processInstanceIdList.add(processInstance.getId());
            }
        }
        return processInstanceIdList;
    }

    private void checkIfInstanceIsOfPayment(ProcessInstance processInstance) {

        if(processInstance.getAction().equalsIgnoreCase(MRConstants.ACTION_PAY)){
            throw new CustomException("INVALID_PROCESS_INSTANCE","Payment is already completed so it can't be reversed!");
        }
    }

    private MarriageRegistrationSearchCriteria getSearchCriteria(IssueFixRequest issueFixRequest) {
        MarriageRegistrationSearchCriteria searchCriteria = MarriageRegistrationSearchCriteria.builder()
                .tenantId(issueFixRequest.getIssueFix().getTenantId()).build();

        if(!StringUtils.isEmpty(issueFixRequest.getIssueFix().getMrNumber())){
            searchCriteria.setMrNumbers(Arrays.asList(issueFixRequest.getIssueFix().getMrNumber()));
        }

        if(!StringUtils.isEmpty(issueFixRequest.getIssueFix().getApplicationNo())){
            searchCriteria.setApplicationNumber(issueFixRequest.getIssueFix().getApplicationNo());
        }

        return searchCriteria;
    }
}
