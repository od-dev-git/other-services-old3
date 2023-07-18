package org.egov.usm.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.usm.service.SurveyService;
import org.egov.usm.web.model.Survey;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class SurveyRequestValidator {

	
	public void validateUserType(RequestInfo requestInfo) {
		// TODO Auto-generated method stub
		
	}

	public void validateQuestions(Survey survey) {
		// TODO Auto-generated method stub
		
	}

	public void validateSurveyUniqueness(Survey survey) {
		SurveySearchCriteria criteria = SurveySearchCriteria.builder()
                .tenantId(survey.getTenantId())
                .build();

        
	}

	public void validateQuestionsWhileUpdate(Survey survey) {
		// TODO Auto-generated method stub
		
	}

	public void validateUpdateRequest(Survey survey) {
		// TODO Auto-generated method stub
		
	}

	public Survey validateSurveyExistence(Survey survey) {
		// TODO Auto-generated method stub
		return null;
	}

}
