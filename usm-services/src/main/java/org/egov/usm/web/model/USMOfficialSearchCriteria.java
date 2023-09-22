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
public class USMOfficialSearchCriteria {

	@JsonProperty("id")
	private String id;

	@JsonProperty("tenantId")
	private String tenantId;

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
	
	@JsonProperty("ticketId")
	private String ticketId;
	
	

}
