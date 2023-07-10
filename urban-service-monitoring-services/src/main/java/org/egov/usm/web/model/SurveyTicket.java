package org.egov.usm.web.model;

import org.egov.usm.model.enums.TicketStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyTicket {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("ticketNo")
	private String ticketNo;
	
	@JsonProperty("surveyDetailId")
	private String surveyDetailId;
	
	@JsonProperty("questionCode")
	private String questionCode;
	
	@JsonProperty("id")
	private String ticketDescription;
	
	@JsonProperty("id")
	private TicketStatus status;
	
	@JsonProperty("ticketClosedTime")
	private String ticketClosedTime;
	
	@JsonProperty("additionalDetail")
	private String additionalDetail;
	
	@JsonProperty("auditDetail")
	private AuditDetails auditDetail;
	
}
