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
    
    @JsonProperty("surveyAnswerId")
    private String surveyAnswerId;
    
    @JsonProperty("questionId")
    private String questionId;
    
    @JsonProperty("ticketDescription")
    private String ticketDescription;
    
    @JsonProperty("status")
    private TicketStatus status;
    
    @JsonProperty("ticketCreatedTime")
    private Long ticketCreatedTime;
    
    @JsonProperty("ticketClosedTime")
    private Long ticketClosedTime;
    
    @JsonProperty("hasOpenTicket")
    private Boolean hasOpenTicket ;
    
    @JsonProperty("additionalDetail")
    private String additionalDetail;
    
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;
	
}
