package org.egov.dss.web.model;

import java.util.ArrayList;
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
public class Data {

	@JsonProperty("headerName")
	private String headerName;
	
	@JsonProperty("headerValue")
	private Object headerValue;
	
	@JsonProperty("headerSymbol")
	private Object headerSymbol;
	
	@JsonProperty("insight")
	private Object insight;
	
	@JsonProperty("plots")
	@Builder.Default
	private List<Plot> plots = new ArrayList<>();
}
