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

	@JsonProperty("ticketId")
	private String ticketId;

	@JsonProperty("surveyId")
	private String surveyId;

	@JsonProperty("ward")
	private String ward;

	@JsonProperty("category")
	private String category;

	@JsonProperty("slumCode")
	private String slumCode;

	@JsonProperty("questionCode")
	private String questionCode;

	@JsonProperty("status")
	private String status;

	@JsonProperty("isSatisfied")
	private Boolean isSatisfied;

	@JsonProperty("officialRole")
	private String officialRole;

	@JsonProperty("isNodalOfficer")
	private Boolean isNodalOfficer;

	@JsonProperty("isEscalateOfficer")
	private Boolean isEscalationOfficer;

	@JsonProperty("unAttended")
	private Boolean unAttended;

	@JsonProperty("createdBy")
	private String createdBy;
	
	@JsonProperty("assigned")
	private String assigned;

	@JsonProperty("tickDate")
	private Long ticketDate;

}
