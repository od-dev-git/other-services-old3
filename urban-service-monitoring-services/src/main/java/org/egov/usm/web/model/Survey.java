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
public class Survey {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("surveyNo")
	private String surveyNo;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("slumCode")
	private String slumCode;
	
	@JsonProperty("surveyTime")
	private String surveyTime;
	
	@JsonProperty("additionalDetail")
	private String additionalDetail;
	
	@JsonProperty("auditDetail")
	private AuditDetails auditDetail;
	
	@JsonProperty("surveyDetail")
	private SurveyDetail surveyDetail;
	
}
