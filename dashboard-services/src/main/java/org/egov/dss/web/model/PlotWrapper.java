package org.egov.dss.web.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlotWrapper {

	@JsonProperty("plots")
	List<Plot> plots;
	
	@JsonProperty("insight")
	private String insight;
	
	@JsonProperty("headerSymbol")
	private String headerSymbol;
	
	@JsonProperty("headerValue")
	private Integer headerValue;
	
	@JsonProperty("headerName")
	private String headerName;
}
