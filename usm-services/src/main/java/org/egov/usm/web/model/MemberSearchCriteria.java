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
public class MemberSearchCriteria {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("userId")
	private String userId;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("ward")
	private String ward;

	@JsonProperty("slumCode")
	private String slumCode;

	@JsonProperty("isActive")
	private Boolean isActive;

	@JsonProperty("surveyDate")
	private Long surveyDate;

	@JsonProperty("createdBy")
	private String createdBy;
	
	@JsonProperty("ticketId")
	private String ticketId;
}
