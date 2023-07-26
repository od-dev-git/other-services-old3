package org.egov.usm.service;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.usm.config.USMConfiguration;
import org.egov.usm.model.enums.SurveyAnswer;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.QuestionDetail;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class EnrichmentService {
	
	private USMConfiguration config;
	
	private USMUtil usmUtil;
	
	
	@Autowired
	public EnrichmentService(USMConfiguration config, USMUtil usmUtil) {
		this.config = config;
		this.usmUtil = usmUtil;
	}
	
	
	
	public void enrichSurveySubmitRequest(SurveyDetailsRequest surveyDetailsRequest) {
		RequestInfo requestInfo = surveyDetailsRequest.getRequestInfo();
		String uuid = requestInfo.getUserInfo().getUuid();
		AuditDetails auditDetails = USMUtil.getAuditDetails(uuid, true);
		
		surveyDetailsRequest.getSurveyDetails().setId(USMUtil.generateUUID());
		
		surveyDetailsRequest.getSurveyDetails().setSurveyTime(auditDetails.getCreatedTime());
		
		//check all question answered or not , then set status accordingly
		List<QuestionDetail> questionDetails = surveyDetailsRequest.getSurveyDetails().getQuestionDetails().stream()
										.filter(qs -> qs.getAnswer().equals(SurveyAnswer.NA) || qs.getHasOpenTicket())
										.collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(questionDetails)) {
			surveyDetailsRequest.getSurveyDetails().setIsClosed(Boolean.TRUE);
		} else {
			surveyDetailsRequest.getSurveyDetails().setIsClosed(Boolean.FALSE);
		}
		
		//Generate Survey number and set it
		List<String> surveyNumbers = usmUtil.getIdList(requestInfo, surveyDetailsRequest.getSurveyDetails().getTenantId(), config.getSurveyNoIdgenName(), config.getSurveyNoIdgenFormat(), 1 );
		surveyDetailsRequest.getSurveyDetails().setSurveyNo(surveyNumbers.get(0));
		
		//enrich question details
		surveyDetailsRequest.getSurveyDetails().getQuestionDetails()
			.forEach(question -> question.setAuditDetails(auditDetails));
		
	}



	public void enrichLookupDetails(SurveyDetailsRequest surveyDetailsRequest) {
		RequestInfo requestInfo = surveyDetailsRequest.getRequestInfo();
		String uuid = "";
		if(requestInfo.getUserInfo() != null ) {
			uuid = requestInfo.getUserInfo().getUuid();
		}
		AuditDetails auditDetails = USMUtil.getAuditDetails(uuid, false);
		//set audit details
		surveyDetailsRequest.getSurveyDetails().setAuditDetails(auditDetails);
	}
	
	

}
