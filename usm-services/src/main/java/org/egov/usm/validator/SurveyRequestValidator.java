package org.egov.usm.validator;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.usm.model.enums.Status;
import org.egov.usm.repository.SurveyRepository;
import org.egov.usm.utility.Constants;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.Survey;
import org.egov.usm.web.model.SurveyRequest;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
public class SurveyRequestValidator {

	
	@Autowired
	private SurveyRepository surveyRepository;
	

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
		AuditDetails auditDetails = USMUtil.getAuditDetails(uuid, true);
		
		surveyRequest.getSurvey().setId(USMUtil.generateUUID());
		//set audit details
		surveyRequest.getSurvey().setAuditDetails(auditDetails);
		surveyRequest.getSurvey().setStatus(Status.ACTIVE);
		surveyRequest.getSurvey().setPostedBy(surveyRequest.getRequestInfo().getUserInfo().getName());
		surveyRequest.getSurvey().setCollectCitizenInfo(Boolean.TRUE);
		
		
		surveyRequest.getSurvey().getQuestionDetails().forEach(question -> {
			question.setId(USMUtil.generateUUID());
			question.setSurveyId(surveyRequest.getSurvey().getId());
			question.setAuditDetails(auditDetails);
			question.setStatus(Status.ACTIVE);
			question.setRequired(Boolean.TRUE);
			question.setType(Constants.RADIO_BUTTON);
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
