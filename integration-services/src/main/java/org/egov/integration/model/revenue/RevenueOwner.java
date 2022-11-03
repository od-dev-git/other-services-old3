package org.egov.integration.model.revenue;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RevenueOwner {
	
	@JsonProperty("ownername")
	@NotBlank(message = "Owner Name cannot be null")
	private String ownerName;
	
	@JsonProperty("mobilenumber")
	@NotBlank(message = "Owner Mobile Number cannot be Blank")
	private String mobileNumber; 
	
}
