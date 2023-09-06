package org.egov.usm.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SurveySearchCriteria {
	
	@JsonProperty("tenant")
	private String tenant;
	
	@JsonProperty("surveyId")
	private String surveyId;

	@JsonProperty("surveySubmittedId")
	private String surveySubmittedId;
	
	@JsonProperty("surveyNo")
	private String surveyNo;
	
	@JsonProperty("ticketId")
	private String ticketId;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("slumCode")
	private String slumCode;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("surveyDate")
	private Long surveyDate;
	
	@JsonProperty("createdBy")
	private String createdBy;
	
	@JsonProperty("isAdmin")
	private Boolean isAdmin;
}
