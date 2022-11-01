package org.egov.integration.model.revenue;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class RevenueNotification {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("districtName")
	private String districtName;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("revenueVillage")
	private String revenueVillage;
	
	@JsonProperty("plotNo")
	private String plotNo;
	
	@JsonProperty("flatNo")
	private String flatNo; 
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("currentOwnerName")
	private String currentOwnerName;
	
	@JsonProperty("currentOwnerMobileNumber")
	private String currentOwnerMobileNumber; 
	
	@JsonProperty("newOwnerName")
	private String newOwnerName;
	
	@JsonProperty("newOwnerMobileNumber")
	private String newOwnerMobileNumber; 
	
	@JsonProperty("actionTaken")
	private String actionTaken;
	
	@JsonProperty("action")
	private Boolean action;
	
	@JsonProperty("additionalDetails")
	private String additionalDetails;
	
	@JsonProperty("createdBy")
	private String createdBy;
	
	@JsonProperty("createdTime")
	private Long createdTime;
	
	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy;
	
	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime;
}
