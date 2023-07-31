package org.egov.usm.web.model;

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
public class QuestionLookup {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("slumCode")
	private String slumCode;
	
	@JsonProperty("questionId")
	private String questionId;
	
	@JsonProperty("hasOpenTicket")
    private Boolean hasOpenTicket;
	
	@JsonProperty("ticketId")
	private String ticketId;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
