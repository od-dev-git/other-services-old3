package org.egov.integration.validator;

import com.jayway.jsonpath.JsonPath;
import org.egov.common.contract.request.RequestInfo;
import org.egov.integration.config.IntegrationConfiguration;
import org.egov.integration.repository.FeedbackRepository;
import org.egov.integration.repository.ServiceRepository;
import org.egov.integration.util.RevenueNotificationConstants;
import org.egov.integration.web.model.Feedback;
import org.egov.integration.web.model.FeedbackCreationRequest;
import org.egov.integration.web.model.FeedbackSearchCriteria;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.temporal.ValueRange;
import java.util.*;

import static org.egov.integration.constants.FeedbackConstants.MAX_RATING;
import static org.egov.integration.constants.FeedbackConstants.MIN_RATING;

@Component
public class FeedbackValidator {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    IntegrationConfiguration configuration;

    @Autowired
    private FeedbackRepository feedbackRepository;

    public void validateCreateRequest(FeedbackCreationRequest feedbackCreationRequest){
        Map<String, String> errorMap = new HashMap<>();
        if(StringUtils.isEmpty(feedbackCreationRequest.getFeedback().getTenantId())){
            errorMap.put("INVALID_TENANT_ID","Tenant Id can't be null or empty");
        }
        if(StringUtils.isEmpty(feedbackCreationRequest.getFeedback().getConsumerNo())){
            errorMap.put("INVALID_CONSUMER_NO","Consumer No can't be null or empty");
        }
        if(StringUtils.isEmpty(feedbackCreationRequest.getFeedback().getModule())){
            errorMap.put("INVALID_MODULE","Module can't be null or empty");
        }
        if(feedbackCreationRequest.getFeedback().getRating() == null){
            errorMap.put("INVALID_RATING_VALUE","Rating can't be null");
        }
        else {
            final ValueRange ratingRange = ValueRange.of(MIN_RATING, MAX_RATING);
            if (!ratingRange.isValidIntValue(feedbackCreationRequest.getFeedback().getRating())) {
                errorMap.put("INVALID_RATING_VALUE", "Rating value must be between 1 and 5");
            }
        }
        validateTenantIdByMDMS(feedbackCreationRequest.getFeedback().getTenantId(), errorMap, feedbackCreationRequest.getRequestInfo());
        validateDuplicateFeedbacks(feedbackCreationRequest.getFeedback().getTenantId(),feedbackCreationRequest.getFeedback().getConsumerNo(), feedbackCreationRequest.getFeedback().getModule(), errorMap);

        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);


    }

    private void validateDuplicateFeedbacks(String tenantId, String consumerNo, String module, Map<String, String> errorMap) {

        FeedbackSearchCriteria feedbackSearchCriteria = FeedbackSearchCriteria.builder()
                .consumerNo(consumerNo).tenantId(tenantId).module(module).build();

        List<Feedback> feedbacks=feedbackRepository.getFeedbackList(feedbackSearchCriteria);
        if(!CollectionUtils.isEmpty(feedbacks))
            errorMap.put("DUPLICATE_CONSUMER_CODE","Consumer Code is duplicate");
    }

    public void validateSearchCriteria(FeedbackSearchCriteria criteria,RequestInfo requestInfo){
        Map<String, String> errorMap = new HashMap<>();
        if(StringUtils.isEmpty(criteria.getTenantId())){
            errorMap.put("INVALID_TENANT_ID","Tenant Id passed can't be null or empty");
        }
        validateTenantIdByMDMS(criteria.getTenantId(), errorMap, requestInfo);
        if(criteria.getRating()!=null){
            final ValueRange ratingRange = ValueRange.of(MIN_RATING, MAX_RATING);
            if (!ratingRange.isValidIntValue(criteria.getRating())) {
                errorMap.put("INVALID_RATING_VALUE", "Rating value must be between 1 and 5");
            }
        }
        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);

    }



    private void validateTenantIdByMDMS(String tenantId, Map<String, String> errorMap, RequestInfo requestInfo) {

        List<String> tenantids = getDataFromMdms(requestInfo);
        if(!tenantids.contains(tenantId)) {
            errorMap.put("INVAILD_TENANTID", "Tenant Id not valid");
        }


    }

    private List<String> getDataFromMdms(RequestInfo request) {

        String mdmsHost = configuration.getMdmsHost();
        String mdmsEndpoint = configuration.getMdmsEndpoint();
        StringBuilder uri = new StringBuilder(mdmsHost).append(mdmsEndpoint);
        List<String> names = Arrays.asList(RevenueNotificationConstants.MDMS_NAME_TENANTS);
        MdmsCriteriaReq criteriaReq = prepareMdMsRequest(RevenueNotificationConstants.MDMS_MODULE_NAME, names,
                RevenueNotificationConstants.MDMS_TENANT_ID, RevenueNotificationConstants.MDMS_FILTER,
                request);
        try {
            Object result = serviceRepository.fetchResult(uri, criteriaReq);
            return JsonPath.read(result, RevenueNotificationConstants.JSON_FILTER);
        } catch (Exception e) {
            throw new CustomException("MDMS_FETCH_EXCEPTION", "Data fetch exception from MDMS");
        }
    }

    private MdmsCriteriaReq prepareMdMsRequest(String moduleName, List<String> names, String tenantId,
                                               String filter, RequestInfo requestInfo) {

        List<MasterDetail> masterDetails = new ArrayList<>();
        names.forEach(name -> {
            masterDetails.add(MasterDetail.builder().name(name).filter(filter).build());
        });
        ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(moduleName).masterDetails(masterDetails).build();
        List<ModuleDetail> moduleDetails = new ArrayList<>();
        moduleDetails.add(moduleDetail);
        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
        return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
    }
}
