package org.egov.report.web.model;

import java.math.BigDecimal;

import org.egov.report.web.model.WaterMonthlyDemandResponse.WaterMonthlyDemandResponseBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaterNewConsumerMonthlyResponse {

	@JsonProperty("ulb")
	private String ulb;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("connectionNo")
	private String connectionNo;
	
	@JsonProperty("applicationNo")
	private String applicationNo;
	
	@JsonProperty("date")
	private Long date;
	
	@JsonProperty("userName")
	private String userName;
	
	@JsonProperty("userAddress")
	private String userAddress;
	
	@JsonProperty("mobile")
	private String mobile;
	
	@JsonProperty("sanctionDate")
	private Long sanctionDate;
	
	@JsonProperty("connectionType")
	private String connectionType;
	
	@JsonProperty("connectionFacility")
	private String connectionFacility;
	
	@JsonProperty("connectionCategory")
	private String connectionCategory;
	
	@JsonProperty("connectionPurpose")
	private String connectionPurpose;
	
}
