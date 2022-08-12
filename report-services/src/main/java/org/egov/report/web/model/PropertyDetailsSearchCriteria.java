package org.egov.report.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDetailsSearchCriteria {

	@JsonProperty("ulbName")
	private String ulbName;

	@JsonProperty("wardNo")
	private String wardNo;

}