package org.egov.mr.service.issuefix;

import lombok.extern.slf4j.Slf4j;
import org.egov.mr.repository.IssueFixRepository;
import org.egov.mr.service.MarriageRegistrationService;
import org.egov.mr.util.IssueFixConstants;
import org.egov.mr.validator.IssueFixValidator;
import org.egov.mr.web.models.MarriageRegistration;
import org.egov.mr.web.models.MarriageRegistrationSearchCriteria;
import org.egov.mr.web.models.collection.DemandSearchCriteria;
import org.egov.mr.web.models.collection.Payment;
import org.egov.mr.web.models.collection.PaymentSearchCriteria;
import org.egov.mr.web.models.issuefix.IssueFix;
import org.egov.mr.web.models.issuefix.IssueFixRequest;
import org.egov.mr.web.models.workflow.ProcessInstance;
import org.egov.mr.web.models.workflow.WorkFlowSearchCriteria;
import org.egov.mrcalculator.web.models.demand.Demand;
import org.egov.mrcalculator.web.models.demand.DemandDetail;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("paymentIssueFixService")
@Slf4j
public class PaymentIssueFixService implements IIssueFixService{

    @Autowired
    private IssueFixValidator issueFixValidator;

    @Autowired
    private IssueFixRepository issueFixRepository;

    @Autowired
    private MarriageRegistrationService marriageRegistrationService;

    @Override
    public IssueFix issueFix(IssueFixRequest issueFixRequest, HttpHeaders headers) {
        issueFixValidator.validateIssueFixRequestForPaymentIssue(issueFixRequest);
        MarriageRegistrationSearchCriteria searchCriteria = getSearchCriteria(issueFixRequest);
        List<MarriageRegistration> marriageRegistrations = marriageRegistrationService.search(searchCriteria,
                issueFixRequest.getRequestInfo(), "MR", headers);
        MarriageRegistration marriageRegistration=marriageRegistrations.get(0);
        log.info("@Class: PaymentIssueFixService @method:issueFix @message: MR Search Completed");
        issueFixValidator.validateIssueFixRequestForPaymentIssue(issueFixRequest);
        Map<String, Boolean> isDataUpdateNeeded = new HashMap<>();
        Payment payment = checkIfPaymentReceived(issueFixRequest);

        Demand demandToBeUpdated = checkIfDemandUpdatedProperly(issueFixRequest,isDataUpdateNeeded);

        ProcessInstance processInstance = checkIfWorkFlowUpdatedProperly(issueFixRequest, isDataUpdateNeeded);

        MarriageRegistration applicationToBeUpdated = checkIfApplicationUpdatedProperly(issueFixRequest,
                marriageRegistration, isDataUpdateNeeded);

        updateDataIfNeeded(demandToBeUpdated, processInstance, applicationToBeUpdated, payment, isDataUpdateNeeded);
        return issueFixRequest.getIssueFix();
    }

    private Demand checkIfDemandUpdatedProperly(IssueFixRequest issueFixRequest,
                                                Map<String, Boolean> isDataUpdateNeeded) {

        String tenantId = issueFixRequest.getIssueFix().getTenantId();

        String applicationNumber = issueFixRequest.getIssueFix().getApplicationNo();

        DemandSearchCriteria demandSearchCriteria = DemandSearchCriteria.builder().consumerCode(applicationNumber)
                .businessService(IssueFixConstants.businessService_MR).tenantId(tenantId).build();

        // Search demand here
        List<Demand> demands = issueFixRepository.getDemands(demandSearchCriteria);

        if (CollectionUtils.isEmpty(demands) || demands.size() > 1) {

            throw new CustomException("DEMAND_ISSUE",
                    "Found No or Multiple Demans for mentioned Application Number. Kindly revalidate the Application State !!");
        }

        // Start Checking if the demand is updated properly, if not update the demand
        Demand demand = demands.get(0);

        List<DemandDetail> demandDetails = demand.getDemandDetails();

        for (DemandDetail demandDetail : demandDetails) {
            if (demandDetail.getTaxAmount().compareTo(demandDetail.getCollectionAmount()) == 0) {
                log.info("Demand Details are already updated for .. "+demandDetail.getTaxHeadMasterCode() + " Moving to next step...");
            } else {
//				repository.updateDemandDetail(demandDetail);
                isDataUpdateNeeded.put(IssueFixConstants.IS_DEMAND_DETAILS_UPDATE_NEEDED, true);
            }
        }

        if (!demand.getIsPaymentCompleted()) {
//			repository.updateDemand(demand);
            isDataUpdateNeeded.put(IssueFixConstants.IS_DEMAND_UPDATE_NEEDED, true);
        } else {
            log.info("Is PaymentCompleted Flag already updated.. Moving to next step...");
        }
        return demand;
    }

    private Payment checkIfPaymentReceived(IssueFixRequest issueFixRequest) {
        String tenantId = issueFixRequest.getIssueFix().getTenantId();

        String applicationNumber = issueFixRequest.getIssueFix().getApplicationNo();

        PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder().consumerCode(applicationNumber)
                .businessService(IssueFixConstants.businessService_MR).tenantId(tenantId).build();

        List<Payment> payments = issueFixRepository.getPayments(paymentSearchCriteria);

        if (CollectionUtils.isEmpty(payments) || payments.size() > 1) {

            throw new CustomException("PAYMENT_ISSUE",
                    "Found No or Multiple Payments for mentioned Application Number. Kindly revalidate the payments !!");
        }

        log.info("Payment received.. Moving to next step !!");

        return payments.get(0);
    }

    private ProcessInstance checkIfWorkFlowUpdatedProperly(IssueFixRequest issueFixRequest,
                                                           Map<String, Boolean> isDataUpdateNeeded) {

        String tenantId = issueFixRequest.getIssueFix().getTenantId();

        String applicationNumber = issueFixRequest.getIssueFix().getApplicationNo();

        WorkFlowSearchCriteria workFlowSearchCriteria = WorkFlowSearchCriteria.builder().businessId(applicationNumber)
                .tenantId(tenantId).build();

        List<ProcessInstance> processInstances = issueFixRepository.getProcessInstances(workFlowSearchCriteria);


        if(CollectionUtils.isEmpty(processInstances)) {
            throw new CustomException("WORKFLOW_ISSUE",
                    "Found No Workflow records for mentioned Application Number !!");
        }

        ProcessInstance lastestProcessInstance = processInstances.get(0);

        if(lastestProcessInstance.getAction().equalsIgnoreCase(IssueFixConstants.ACTION_PAY)) {
            log.info("Workflow is already updated.. Pay Action is already there... Moving to next step...");
        } else {
            isDataUpdateNeeded.put(IssueFixConstants.IS_WORKFLOW_UPDATE_NEEDED, true);
        }

        List<ProcessInstance> processInstanceForApply = processInstances.stream()
                .filter(process -> process.getAction().equalsIgnoreCase(IssueFixConstants.ACTION_APPLY))
                .map(process -> process).collect(Collectors.toList());

        return processInstanceForApply.get(0);
    }

    @Transactional
    private void updateDataIfNeeded(Demand demandToBeUpdated, ProcessInstance processInstance,
                                    MarriageRegistration applicationToBeUpdated, Payment payment, Map<String, Boolean> isDataUpdateNeeded) {

        if (isDataUpdateNeeded.containsKey(IssueFixConstants.IS_DEMAND_DETAILS_UPDATE_NEEDED)
                && isDataUpdateNeeded.get(IssueFixConstants.IS_DEMAND_DETAILS_UPDATE_NEEDED)) {
            demandToBeUpdated.getDemandDetails().forEach(demandDetail -> {
                issueFixRepository.updateDemandDetail(demandDetail);
            });
        }
        if (isDataUpdateNeeded.containsKey(IssueFixConstants.IS_DEMAND_UPDATE_NEEDED)
                && isDataUpdateNeeded.get(IssueFixConstants.IS_DEMAND_UPDATE_NEEDED)) {
            issueFixRepository.updateDemand(demandToBeUpdated);
        }

        if ((isDataUpdateNeeded.containsKey(IssueFixConstants.IS_DEMAND_DETAILS_UPDATE_NEEDED)
                && isDataUpdateNeeded.get(IssueFixConstants.IS_DEMAND_DETAILS_UPDATE_NEEDED))
                || (isDataUpdateNeeded.containsKey(IssueFixConstants.IS_DEMAND_UPDATE_NEEDED)
                && isDataUpdateNeeded.get(IssueFixConstants.IS_DEMAND_UPDATE_NEEDED))) {

            issueFixRepository.expireBill(demandToBeUpdated.getConsumerCode());

        }

        if (isDataUpdateNeeded.containsKey(IssueFixConstants.IS_APPLICATION_UPDATE_NEEDED)
                && isDataUpdateNeeded.get(IssueFixConstants.IS_APPLICATION_UPDATE_NEEDED)) {
            issueFixRepository.updateApplication(applicationToBeUpdated, payment);
        }
        if (isDataUpdateNeeded.containsKey(IssueFixConstants.IS_WORKFLOW_UPDATE_NEEDED)
                && isDataUpdateNeeded.get(IssueFixConstants.IS_WORKFLOW_UPDATE_NEEDED)) {
            issueFixRepository.updateWorkflow(processInstance, payment);
        }

    }

    private MarriageRegistration checkIfApplicationUpdatedProperly(IssueFixRequest issueFixRequest, MarriageRegistration marriageRegistration,
                                                           Map<String, Boolean> isDataUpdateNeeded) {

        if (marriageRegistration.getStatus().equalsIgnoreCase(IssueFixConstants.STATUS_PAY_PENDING_SCHEDULE) && IssueFixConstants.APPLICATION_TYPE.contains(marriageRegistration.getApplicationType().toString())) {
            log.info(" Application state already updated.. No Changes done in Application status...");
        } else {
//		repository.updateApplication(waterConnection);
            isDataUpdateNeeded.put(IssueFixConstants.IS_APPLICATION_UPDATE_NEEDED, true);
        }
        return marriageRegistration;
    }
}
