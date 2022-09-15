package org.egov.dss.web.model;

import java.math.BigDecimal;

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
public class Plot {

	@JsonProperty("label")
	private String label;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("symbol")
	private String symbol;
	
	@JsonProperty("value")
	private BigDecimal value;
	
	@JsonProperty("strValue")
	private String strValue;
}
