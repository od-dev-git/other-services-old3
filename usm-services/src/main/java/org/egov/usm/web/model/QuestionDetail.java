package org.egov.usm.web.model;

import org.egov.usm.model.enums.Status;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class QuestionDetail {

	@JsonProperty("id")
	private String id;

	@JsonProperty("surveyId")
	private String surveyId;

	@JsonProperty("questionStatement")
	private String questionStatement;

	@JsonProperty("category")
	private String category;

	@JsonProperty("options")
	private String options;

	@JsonProperty("type")
	private String type;

	@JsonIgnore
	private Status status;

	@JsonProperty("required")
	private Boolean required;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
