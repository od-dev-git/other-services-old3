package org.egov.dss.model;

import java.math.BigDecimal;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.util.Json;
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
public class PayloadDetails {
	
	@Size(max=64)
    @JsonProperty("id")
    private String id;
	
	@JsonProperty("visualizationCode")
	private String visualizationcode;
	
	@JsonProperty("moduleLevel")
	private String modulelevel;
	
	@JsonProperty("startDate")
	private Long startdate;
	
	@JsonProperty("endDate")
	private Long enddate;
	
	@JsonProperty("timeInterval")
	private String timeinterval;
	
	@JsonProperty("chartType")
	private String charttype;
	
	@JsonProperty("tenantId")
	private String tenantid;
	
	@JsonProperty("districtId")
	private String districtid;
	
	@JsonProperty("city")
	private String city;
	
	@JsonProperty("headerName")
	private String headername;
	
	@JsonProperty("valueType")
	private String valuetype;
	
	@JsonProperty("responseData")
	private Object responsedata = null;
	
	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime;

}
