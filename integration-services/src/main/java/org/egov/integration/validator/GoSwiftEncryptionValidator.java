package org.egov.integration.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.egov.common.contract.request.RequestInfo;
import org.egov.integration.service.MdmsService;
import org.egov.integration.web.model.GoSwiftInput;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class GoSwiftEncryptionValidator {

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private MdmsService mdmsService;

    public void validateEncodedInput(GoSwiftInput goSwiftInput, RequestInfo requestInfo) {
    	checkForNullParams(goSwiftInput);
    	checkSecurityToken(goSwiftInput.getSecurityToken(),requestInfo);
    }
    
    private void checkSecurityToken(String securityToken, RequestInfo requestInfo) {
    	Boolean isValidSecurityToken = false;
		JsonNode mdmsResponse=objectMapper.valueToTree(mdmsService.mDMSCall(requestInfo, "od"));
		for(JsonNode jsonNode:mdmsResponse.path("MdmsRes").path("DataSecurity").path("SecurityToken")) {
			if(jsonNode.path("entityName").asText().equals("GOSWIFT")) {
				if(jsonNode.path("isActive").asBoolean()) {
					if(jsonNode.path("token").asText().equals(securityToken)) {
						isValidSecurityToken=true;
						break;
					}
				}
			}
		}
		
		if(!isValidSecurityToken) {
			throw new CustomException("UNAUTHORIZED_ACCESS","Not Authorized to access this resource");
		}
		
	}

	public void validateDecodedResponse(JsonNode jsonNode, RequestInfo requestInfo) {
        GoSwiftInput goSwiftInput= null;
        try {
            goSwiftInput = objectMapper.treeToValue(jsonNode, GoSwiftInput.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        checkForNullParams(goSwiftInput);
        checkSecurityToken(goSwiftInput.getSecurityToken(),requestInfo);
    }

    private void checkForNullParams(Object goSwiftInput) {
        if(goSwiftInput==null){
            throw new CustomException("INVALID_DATA","The Object is either null or empty");
        }

        Class<?> clazz =goSwiftInput.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            try {
                if(field.get(goSwiftInput)==null){
                    throw new CustomException("INVALID_DATA", field.getName()+"  can't be null");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
