package org.egov.integration.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.integration.web.model.GoSwiftInput;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class GoSwiftEncryptionValidator {

    @Autowired
    private ObjectMapper objectMapper;

    public void validateEncodedInput(GoSwiftInput goSwiftInput) {
    	checkForNullParams(goSwiftInput);
    }
    
    public void validateDecodedResponse(JsonNode jsonNode) {
        GoSwiftInput goSwiftInput= null;
        try {
            goSwiftInput = objectMapper.treeToValue(jsonNode, GoSwiftInput.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        checkForNullParams(goSwiftInput);
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
