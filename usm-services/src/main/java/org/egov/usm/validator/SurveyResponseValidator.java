package org.egov.usm.validator;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.usm.repository.SDAMemberRepository;
import org.egov.usm.repository.SurveyDetailsRepository;
import org.egov.usm.utility.Constants;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.web.model.MemberSearchCriteria;
import org.egov.usm.web.model.SDAMember;
import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
public class SurveyResponseValidator {
	
	@Autowired
	private SurveyDetailsRepository repository;
	
	@Autowired
	private SDAMemberRepository sdaMemberRepository;

	public Boolean validateSurveyExistsForToday(SurveyDetailsRequest surveyDetailsRequest) {
		
		RequestInfo requestInfo = surveyDetailsRequest.getRequestInfo();
		SurveyDetails surveyDetails = surveyDetailsRequest.getSurveyDetails();
		
		if(ObjectUtils.isEmpty(surveyDetails.getSurveySubmittedId()))
            throw new CustomException("EG_SY_UUID_NOT_PROVIDED_ERR", "Providing survey submitted id is mandatory for updating and deleting surveys");

		if(ObjectUtils.isEmpty(surveyDetails.getTenantId()))
            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing tenantId mandatory for submitting surveys");

		if(ObjectUtils.isEmpty(surveyDetails.getWard()))
            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing ward is mandatory for submitting surveys");

		if(ObjectUtils.isEmpty(surveyDetails.getSlumCode()))
            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing slumCode is mandatory for submitting surveys");


		//Validate SDA with slum access
		validateSDAMemberHasAccess(requestInfo.getUserInfo().getUuid(), surveyDetails.getTenantId(), surveyDetails.getWard(), surveyDetails.getSlumCode());
				
		SurveySearchCriteria criteria = SurveySearchCriteria.builder()
        		.surveySubmittedId(surveyDetails.getSurveySubmittedId())
                .build();
		
        Boolean isSurveyExists = repository.isSurveyExistsForToday(criteria);
        if(!isSurveyExists)
            throw new CustomException("EG_SURVEY_DOES_NOT_EXIST_ERR", "The survey entity provided in update request does not exist or closed for today.");

        surveyDetails.getSubmittedAnswers().forEach(answer -> {
			if(ObjectUtils.isEmpty(answer.getId()))
	            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing answer id is mandatory for submitting surveys");
			
			if(ObjectUtils.isEmpty(answer.getQuestionCategory()))
	            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing questionCategory is mandatory for submitting surveys");
			
			if(ObjectUtils.isEmpty(answer.getAnswer()))
	            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing all questions valid answer is mandatory for submitting surveys");

		});
        return isSurveyExists;
	}
	
	
	public void validateSurveySubmittedForToday(SurveyDetailsRequest surveyDetailsRequest) {
		
		RequestInfo requestInfo = surveyDetailsRequest.getRequestInfo();
		SurveyDetails surveyDetails = surveyDetailsRequest.getSurveyDetails();
		
		if(ObjectUtils.isEmpty(surveyDetails.getSurveyId()))
            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing surveyId is mandatory for submitting surveys");

		if(ObjectUtils.isEmpty(surveyDetails.getTenantId()))
            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing tenantId mandatory for submitting surveys");

		if(ObjectUtils.isEmpty(surveyDetails.getWard()))
            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing ward is mandatory for submitting surveys");

		if(ObjectUtils.isEmpty(surveyDetails.getSlumCode()))
            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing slumCode is mandatory for submitting surveys");


		//Validate SDA with slum access
		validateSDAMemberHasAccess(requestInfo.getUserInfo().getUuid(), surveyDetails.getTenantId(), surveyDetails.getWard(), surveyDetails.getSlumCode());
				
		surveyDetails.getSubmittedAnswers().forEach(answer -> {
			if(ObjectUtils.isEmpty(answer.getQuestionCategory()))
	            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing questionCategory is mandatory for submitting surveys");
			
			if(ObjectUtils.isEmpty(answer.getAnswer()))
	            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing all questions valid answer is mandatory for submitting surveys");

			if(Constants.CATEGORY_STREETLIGHT.equalsIgnoreCase(answer.getQuestionCategory()) && !USMUtil.isGreaterTime(Constants.STREETLIGHT_SURVEY_TIME_IN_HOUR))
	            throw new CustomException("EG_SY_SURVEY_NOT_VALID_ERR", "Providing StreetLight Survey after 6PM is mandatory for submitting surveys");

		});
		
		SurveySearchCriteria criteria = SurveySearchCriteria.builder()
        		.surveyId(surveyDetails.getSurveyId())
        		.tenant(surveyDetails.getTenantId())
        		.ward(surveyDetails.getWard())
        		.slumCode(surveyDetails.getSlumCode())
                .build();
		
        Boolean isSurveyExists = repository.isSurveyExistsForToday(criteria);
        if(isSurveyExists)
            throw new CustomException("EG_SURVEY_ALREADY_EXIST_ERR", "The survey entity provided in request already exist for today.");
	}

	
	public void validateGenerateSurveyRequest(SurveyDetailsRequest surveyDetailsRequest) {
		RequestInfo requestInfo = surveyDetailsRequest.getRequestInfo();
		SurveyDetails surveyDetails = surveyDetailsRequest.getSurveyDetails();
		
		if(ObjectUtils.isEmpty(surveyDetails.getSurveyId()))
            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing surveyId is mandatory for generate surveys");

		if(ObjectUtils.isEmpty(surveyDetails.getTenantId()))
            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing tenantId mandatory for submitting surveys");

		if(ObjectUtils.isEmpty(surveyDetails.getSlumCode()))
            throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing slumCode is mandatory for submitting surveys");

		//Validate SDA with slum access
		validateSDAMemberHasAccess(requestInfo.getUserInfo().getUuid(), surveyDetails.getTenantId(), surveyDetails.getWard(), surveyDetails.getSlumCode());
		
		
	}


	public void validateSDAMemberHasAccess(String userId, String tenantId, String ward, String slumCode) {
		//Validate SDA with slum access
		MemberSearchCriteria searchCriteria = MemberSearchCriteria.builder()
													.userId(userId)
													.tenantId(tenantId)
													.ward(ward)
													.slumCode(slumCode)
													.build();
		List<SDAMember> sdaMembers = sdaMemberRepository.searchSDAMembers(searchCriteria);
		
		if(CollectionUtils.isEmpty(sdaMembers)) 
			throw new CustomException("EG_SY_FIELD_NOT_PROVIDED_ERR", "Providing tenantId or ward or slumCode is not valid for this Member");
		
		
	}
	

}
