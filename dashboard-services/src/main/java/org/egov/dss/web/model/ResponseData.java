package org.egov.dss.web.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseData {

	@JsonProperty("chartType")
	private String chartType;
	
	@JsonProperty("visualizationCode")
	private String visualizationCode;
	
	@JsonProperty("drillDownChartId")
	private String drillDownChartId;
	
	@JsonProperty("data")
	private List<Data> data;
}
