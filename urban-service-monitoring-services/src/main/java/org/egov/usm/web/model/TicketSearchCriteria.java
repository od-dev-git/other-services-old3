package org.egov.usm.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketSearchCriteria {

	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("ticketNo")
	private String ticketNo;
	
	@JsonProperty("surveyId")
	private String surveyId;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("slumCode")
	private String slumCode;
	
	@JsonProperty("questionCode")
	private String questionCode;
	
	@JsonProperty("status")
	private String status;
}
