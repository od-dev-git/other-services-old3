package org.egov.usm.service;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
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
	
	public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";

	private SurveyRequestValidator surveyRequestValidator;

	private SurveyRepository repository;
	
	private USMUtil USMUtil;
	
	@Autowired
	public SurveyService(SurveyRequestValidator surveyRequestValidator, SurveyRepository repository, USMUtil USMUtil) {
		this.surveyRequestValidator = surveyRequestValidator;
		this.repository = repository;
		this.USMUtil = USMUtil;
	}

	/**
	 * Service layer for Creating Survey Request
	 * 
	 * @param SurveyRequest
	 * @return created Survey
	 */
	public Survey create(SurveyRequest surveyRequest) {
		Survey survey = surveyRequest.getSurvey();

		// Validate whether authorized usertype is trying to create survey.
		//surveyRequestValidator.validateUserType(surveyRequest.getRequestInfo());
		// Validate question types.
		//surveyRequestValidator.validateQuestions(survey);
		// Validate survey uniqueness.
		//surveyRequestValidator.validateSurveyUniqueness(survey);
		
		//Enrich service
		surveyRequestValidator.enrichSurveyRequest(surveyRequest);

		repository.save(surveyRequest);
		return survey;
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
		RequestInfo requestInfo = surveyRequest.getRequestInfo();
		
		// Validate survey existence
        Survey existingSurvey = surveyRequestValidator.validateSurveyExistence(survey);
//		// Validate whether authorized usertype is trying to create survey.
//		surveyRequestValidator.validateUserType(surveyRequest.getRequestInfo());
//		// Validate question types.
//		surveyRequestValidator.validateQuestionsWhileUpdate(survey);
//		// Validate survey uniqueness.
//		surveyRequestValidator.validateUpdateRequest(survey);
		
//        sanitizeSurveyForUpdate(survey);
        
        survey.setAuditDetails(existingSurvey.getAuditDetails());

        survey.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUuid());
        survey.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
        
        survey.getQuestionDetails().forEach(qs -> {
        	qs.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUuid());
        	qs.getAuditDetails().setLastModifiedTime(survey.getAuditDetails().getLastModifiedTime());
        });
        
        
		repository.update(surveyRequest);
		return survey;
	}

	private void sanitizeSurveyForUpdate(Survey survey) {
		// TODO Auto-generated method stub
	}

	public void deleteSurvey(@Valid SurveyRequest surveyRequest) {
		Survey survey = surveyRequest.getSurvey();
        // Validate survey existence
		Boolean isSurveyExists = surveyRequestValidator.isSurveyExists(survey);

		survey.setStatus(INACTIVE);
        AuditDetails auditDetails = USMUtil.getAuditDetails(surveyRequest.getRequestInfo().getUserInfo().getUuid(), false);
        survey.setAuditDetails(auditDetails);
        
		if(isSurveyExists != null)
			repository.deleteSurvey(surveyRequest);
	}

}
