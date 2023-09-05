package org.egov.usm.service;

import java.util.List;

import javax.validation.Valid;

import org.egov.usm.model.enums.Status;
import org.egov.usm.repository.SurveyRepository;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.validator.SurveyRequestValidator;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.Survey;
import org.egov.usm.web.model.SurveyRequest;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SurveyService {
	
	private SurveyRequestValidator surveyRequestValidator;

	private SurveyRepository repository;
	
	@Autowired
	public SurveyService(SurveyRequestValidator surveyRequestValidator, SurveyRepository repository, USMUtil USMUtil) {
		this.surveyRequestValidator = surveyRequestValidator;
		this.repository = repository;
	}

	/**
	 * Service layer for Creating Survey Request
	 * 
	 * @param SurveyRequest
	 * @return created Survey
	 */
	public Survey create(SurveyRequest surveyRequest) {
		//Enrich surveyRequest
		surveyRequestValidator.enrichSurveyRequest(surveyRequest);

		repository.save(surveyRequest);
		return surveyRequest.getSurvey();
	}

	/**
	 * Service layer for searching Suvey Requests
	 * 
	 * @param requestInfoWrapper
	 * @param searchCriteria
	 * @return SurveyResponse with SurveyRequest List
	 */
	public List<Survey> searchSurveys(SurveySearchCriteria searchCriteria) {
		log.info("search: " + searchCriteria.toString());
		List<Survey> surveys = repository.getSurveyRequests(searchCriteria);
		return surveys;
	}

	/**
	 * Service layer for Updating Survey
	 * @param surveyRequest
	 * @return updated Survey
	 */
	public Survey updateSurvey(@Valid SurveyRequest surveyRequest) {
		Survey survey = surveyRequest.getSurvey();
		AuditDetails auditDetails = USMUtil.getAuditDetails(surveyRequest.getRequestInfo().getUserInfo().getUuid(), false);
		
        Survey existingSurvey = surveyRequestValidator.validateSurveyExistence(survey);
        
        auditDetails.setCreatedBy(existingSurvey.getAuditDetails().getCreatedBy());
        auditDetails.setCreatedTime(existingSurvey.getAuditDetails().getCreatedTime());
        
        survey.setAuditDetails(auditDetails);
        survey.setStatus(existingSurvey.getStatus());
        
        survey.getQuestionDetails().forEach(question -> {
        	question.setAuditDetails(auditDetails);
        });
        
		repository.update(surveyRequest);
		return survey;
	}

	/**
	 * Service layer for Delete Survey
	 * @param surveyRequest
	 */
	public void deleteSurvey(@Valid SurveyRequest surveyRequest) {
		Survey survey = surveyRequest.getSurvey();
        // Validate survey existence
		Boolean isSurveyExists = surveyRequestValidator.isSurveyExists(survey);

		survey.setStatus(Status.INACTIVE);
        AuditDetails auditDetails = USMUtil.getAuditDetails(surveyRequest.getRequestInfo().getUserInfo().getUuid(), false);
        survey.setAuditDetails(auditDetails);
        
		if(isSurveyExists != null)
			repository.deleteSurvey(surveyRequest);
	}

}
