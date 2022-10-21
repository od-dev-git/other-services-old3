package org.egov.report.util;

import java.util.Objects;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.util.Util;
import org.egov.report.util.Constants;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Util {
    
    @Autowired
    private ObjectMapper mapper;
    
    public void validateTenantIdForUserType(String tenantId, RequestInfo requestInfo) {

        String userType = null;
        if(requestInfo.getUserInfo() != null)
        {
            userType = requestInfo.getUserInfo().getType();
        }
        if(Constants.EMPLOYEE_TYPE_CODE.equalsIgnoreCase(userType) && tenantId.split("\\.").length == 1) {
            throw new CustomException("EG_BS_INVALID_TENANTID","Employees cannot search based on state level tenantid");
        }
    }
    
    public JsonNode getJsonValue(PGobject pGobject){
        try {
            if(Objects.isNull(pGobject) || Objects.isNull(pGobject.getValue()))
                return null;
            else
                return mapper.readTree( pGobject.getValue());
        } catch (Exception e) {
            throw new CustomException(Constants.EG_BS_JSON_EXCEPTION_KEY, Constants.EG_BS_JSON_EXCEPTION_MSG);
        }
    }

}
