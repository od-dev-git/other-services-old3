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
public class SDAMember {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("mobileNumber")
	private String mobileNumber;

	@JsonProperty("userId")
	private String userId;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("ward")
	private String ward;

	@JsonProperty("slumCode")
	private String slumCode;

	@JsonProperty("isActive")
	private boolean isActive;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
