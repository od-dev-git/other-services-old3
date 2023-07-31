package org.egov.usm.validator;

import org.egov.tracer.model.CustomException;
import org.egov.usm.repository.SurveyDetailsRepository;
import org.egov.usm.web.model.SurveyDetails;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class SurveyResponseValidator {
	
	@Autowired
	private SurveyDetailsRepository repository;

	public Boolean validateSurveyExistsForToday(SurveyDetails surveyDetails) {
		if(ObjectUtils.isEmpty(surveyDetails.getId()))
            throw new CustomException("EG_SY_UUID_NOT_PROVIDED_ERR", "Providing survey id is mandatory for updating and deleting surveys");

		SurveySearchCriteria criteria = SurveySearchCriteria.builder()
        		.surveyId(surveyDetails.getId())
                .build();
		
        Boolean isSurveyExists = repository.isSurveyExistsForToday(criteria);
        if(!isSurveyExists)
            throw new CustomException("EG_SURVEY_DOES_NOT_EXIST_ERR", "The survey entity provided in update request does not exist or closed for today.");

        return isSurveyExists;
	}
	

}
