package org.egov.report.validator;

import java.util.HashMap;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.validator.DemandValidatorV1;
import org.egov.report.util.Util;
import org.egov.report.web.model.DemandCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DemandValidatorV1 {
    
    @Autowired
    private Util util;
    
    public void validateDemandCriteria(DemandCriteria demandCriteria, RequestInfo requestInfo) {

        util.validateTenantIdForUserType(demandCriteria.getTenantId(), requestInfo);
        Map<String, String> errorMap = new HashMap<>();

        if (demandCriteria.getDemandId() == null && demandCriteria.getConsumerCode() == null
                && demandCriteria.getEmail() == null && demandCriteria.getMobileNumber() == null
                && demandCriteria.getBusinessService() == null && demandCriteria.getDemandFrom() == null
                && demandCriteria.getDemandTo() == null && demandCriteria.getType() == null)
            errorMap.put("businessService", " Any one of the fields additional to tenantId is mandatory");

        if (!CollectionUtils.isEmpty(errorMap))
            throw new CustomException(errorMap);
    }

}
