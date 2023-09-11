package org.egov.usm.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class USMOfficial {

	@JsonProperty("id")
	private String id;

	@JsonProperty("tenant")
	private String tenant;

	@JsonProperty("ward")
	private String ward;

	@JsonProperty("slumcode")
	private String slumcode;

	@JsonProperty("category")
	private String category;

	@JsonProperty("role")
	private String role;

	@JsonProperty("assigned")
	private String assigned;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
