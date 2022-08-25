package org.egov.report.web.model;

import java.math.BigDecimal;
import java.util.List;

import org.egov.report.web.model.EmployeeDateWiseWSCollectionResponse.EmployeeDateWiseWSCollectionResponseBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConsumerMasterWSReportResponse {
	
	@JsonProperty("ulb")
	private String ulb;
	
	@JsonProperty("wardNo")
	private String wardNo;
	
	@JsonProperty("connectionNo")
	private String connectionNo;
	
	@JsonProperty("oldConnectionNo")
	private String oldConnectionNo;
	
	@JsonProperty("connectionType")
	private String connectionType;
	
	@JsonProperty("connectionCategory")
	private String connectionCategory;
	
	@JsonProperty("usageCategory")
	private String usageCategory;
	
	@JsonProperty("connectionFacility")
	private String connectionFacility;
	
	@JsonProperty("userId")
	private String userId;
	
	@JsonProperty("userName")
	private String userName;
	
	@JsonProperty("userMobile")
	private String userMobile;
	
	@JsonProperty("userAddress")
	private String userAddress;
	

}
