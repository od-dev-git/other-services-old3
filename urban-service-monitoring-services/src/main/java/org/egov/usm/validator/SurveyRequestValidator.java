package org.egov.usm.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.usm.model.enums.Status;
import org.egov.usm.repository.SurveyRepository;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.Survey;
import org.egov.usm.web.model.SurveyRequest;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class SurveyRequestValidator {

	@Autowired
	private USMUtil usmUtil ;
	
	@Autowired
	private SurveyRepository surveyRepository;
	
	
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
		if(ObjectUtils.isEmpty(survey.getId()))
            throw new CustomException("EG_SY_UUID_NOT_PROVIDED_ERR", "Providing survey id is mandatory for updating and deleting surveys");

        SurveySearchCriteria criteria = SurveySearchCriteria.builder()
        		.surveyId(survey.getId())
                .build();
        
        List<Survey> surveys = surveyRepository.getSurveyRequests(criteria);
        if(CollectionUtils.isEmpty(surveys))
            throw new CustomException("EG_SURVEY_DOES_NOT_EXIST_ERR", "The survey entity provided in update request does not exist.");

        return surveys.get(0);
	}

	public void enrichSurveyRequest(SurveyRequest surveyRequest) {
		RequestInfo requestInfo = surveyRequest.getRequestInfo();
		String uuid = requestInfo.getUserInfo().getUuid();
		AuditDetails auditDetails = usmUtil.getAuditDetails(uuid, true);
		
		surveyRequest.getSurvey().setId(UUID.randomUUID().toString());
		//set audit details
		surveyRequest.getSurvey().setAuditDetails(auditDetails);
		surveyRequest.getSurvey().setStatus("ACTIVE");
		surveyRequest.getSurvey().setPostedBy(surveyRequest.getRequestInfo().getUserInfo().getName());
		
		surveyRequest.getSurvey().getQuestionDetails().forEach(question -> {
			question.setId(UUID.randomUUID().toString());
			question.setSurveyId(surveyRequest.getSurvey().getId());
			question.setAuditDetails(auditDetails);
			question.setStatus(Status.ACTIVE);
		});
	}

	
	public Boolean isSurveyExists(Survey survey) {
		SurveySearchCriteria criteria = SurveySearchCriteria.builder()
	        		.surveyId(survey.getId())
	                .build();
        List<String> isSurveyExists = surveyRepository.isSurveyExists(criteria);
        
		return !CollectionUtils.isEmpty(isSurveyExists);
	}

}
