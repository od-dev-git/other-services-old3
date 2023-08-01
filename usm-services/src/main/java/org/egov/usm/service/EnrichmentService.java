package org.egov.usm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.usm.config.USMConfiguration;
import org.egov.usm.model.enums.TicketStatus;
import org.egov.usm.utility.USMUtil;
import org.egov.usm.web.model.AuditDetails;
import org.egov.usm.web.model.QuestionLookup;
import org.egov.usm.web.model.SubmittedAnswer;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.egov.usm.web.model.SurveyTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		
		//Generate Survey number and set it
		List<String> surveyNumbers = usmUtil.getIdList(requestInfo, surveyDetailsRequest.getSurveyDetails().getTenantId(), config.getSurveyNoIdgenName(), config.getSurveyNoIdgenFormat(), 1 );
		
		surveyDetailsRequest.getSurveyDetails().setSurveyNo(surveyNumbers.get(0));
		surveyDetailsRequest.getSurveyDetails().setId(USMUtil.generateUUID());
		surveyDetailsRequest.getSurveyDetails().setSurveyTime(auditDetails.getCreatedTime());
		surveyDetailsRequest.getSurveyDetails().setAuditDetails(auditDetails);
		
		//enrich SubmittedAnswer details
		surveyDetailsRequest.getSurveyDetails().getSubmittedAnswers()
				.forEach(answer -> {
					answer.setId(USMUtil.generateUUID());
					answer.setSurveySubmittedId(surveyDetailsRequest.getSurveyDetails().getId());
					answer.setAuditDetails(auditDetails);
				});
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



	public List<SurveyTicket> enrichTickets(List<SubmittedAnswer> filterSubmittedAnswers,
			SurveyDetailsRequest surveyDetailsRequest) {
		List<SurveyTicket> surveyTickets = new ArrayList<>();

        RequestInfo requestInfo = surveyDetailsRequest.getRequestInfo();
        String uuid = requestInfo.getUserInfo().getUuid();
        AuditDetails auditDetails = USMUtil.getAuditDetails(uuid, true);

        // generate ticket number and set it
        filterSubmittedAnswers.forEach(answer -> {
            SurveyTicket surveyTicket = SurveyTicket.builder()
            								.id(USMUtil.generateUUID())
            								.tenantId(surveyDetailsRequest.getSurveyDetails().getTenantId())
            								.surveyAnswerId(answer.getId())
            								.questionId(answer.getQuestionId())
            								.ticketDescription(answer.getQuestionStatement())
            								.status(TicketStatus.OPEN)
            								.ticketCreatedTime(auditDetails.getCreatedTime())
            								.auditDetails(auditDetails)
            								.build();
            // Add the ticket into the List
            surveyTickets.add(surveyTicket);
        });

        List<String> ticketNumbers = usmUtil.getIdList(requestInfo, surveyDetailsRequest.getSurveyDetails().getTenantId(),
                config.getSurveyNoIdgenName(), config.getTicketNoIdgenFormat(), filterSubmittedAnswers.size());

        for (int i = 0; i < filterSubmittedAnswers.size(); i++) {
            surveyTickets.get(i).setTicketNo(ticketNumbers.get(i));
        }

        return surveyTickets;
	}

	

	public void enrichUpdateQuestionLookup(SurveyDetailsRequest surveyDetailsRequest, List<SurveyTicket> tickets) {
		List<QuestionLookup> questionLookupList = new ArrayList<>();
		RequestInfo requestInfo = surveyDetailsRequest.getRequestInfo();
		String uuid = requestInfo.getUserInfo().getUuid();
		AuditDetails auditDetails = USMUtil.getAuditDetails(uuid, false);
		
		tickets.forEach(ticket -> {
			QuestionLookup questionLookup = QuestionLookup.builder()
												.tenantId(surveyDetailsRequest.getSurveyDetails().getTenantId())
												.slumCode(surveyDetailsRequest.getSurveyDetails().getSlumCode())
												.questionId(ticket.getQuestionId())
												.hasOpenTicket(Boolean.TRUE)
												.ticketId(ticket.getId())
												.auditDetails(auditDetails)
												.build();
			questionLookupList.add(questionLookup);
		});
		
		//set UpdateQuestionLookup
		surveyDetailsRequest.getSurveyDetails().setUpdateQuestionLookup(questionLookupList);
		
	}


	public void enrichSaveQuestionLookup(SurveyDetailsRequest surveyDetailsRequest, List<String> questionIds) {
		
		List<QuestionLookup> questionLookupList = new ArrayList<>();
		RequestInfo requestInfo = surveyDetailsRequest.getRequestInfo();
		String uuid = requestInfo.getUserInfo().getUuid();
		AuditDetails auditDetails = USMUtil.getAuditDetails(uuid, true);
		
		List<SubmittedAnswer> addedAnswers =  surveyDetailsRequest.getSurveyDetails().getSubmittedAnswers().stream()
				.filter(answer -> questionIds.stream().noneMatch(questionId -> questionId.equals(answer.getQuestionId())))
				.collect(Collectors.toList());
		
		addedAnswers.forEach(answer -> {
			QuestionLookup questionLookup = QuestionLookup.builder()
												.id(USMUtil.generateUUID())
												.tenantId(surveyDetailsRequest.getSurveyDetails().getTenantId())
												.slumCode(surveyDetailsRequest.getSurveyDetails().getSlumCode())
												.questionId(answer.getQuestionId())
												.hasOpenTicket(Boolean.FALSE)
												.ticketId(null)
												.auditDetails(auditDetails)
												.build();
			questionLookupList.add(questionLookup);
		});
		
		//set UpdateQuestionLookup
		surveyDetailsRequest.getSurveyDetails().setSaveQuestionLookup(questionLookupList);
	}

	
	public void enrichSurveyUpdateRequest(@Valid SurveyDetailsRequest surveyDetailsRequest) {
		RequestInfo requestInfo = surveyDetailsRequest.getRequestInfo();
		String uuid = requestInfo.getUserInfo().getUuid();
		AuditDetails auditDetails = USMUtil.getAuditDetails(uuid, false);

		surveyDetailsRequest.getSurveyDetails().setSurveyTime(auditDetails.getLastModifiedTime());
		surveyDetailsRequest.getSurveyDetails().setAuditDetails(auditDetails);

		//enrich SubmittedAnswer details
		surveyDetailsRequest.getSurveyDetails().getSubmittedAnswers()
				.forEach(answer -> {
					answer.setSurveySubmittedId(surveyDetailsRequest.getSurveyDetails().getId());
					answer.setAuditDetails(auditDetails);
				});

	}

}
