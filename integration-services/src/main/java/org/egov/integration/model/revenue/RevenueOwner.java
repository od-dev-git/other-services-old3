package org.egov.integration.model.revenue;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevenueOwner {
	
	@JsonProperty("ownername")
	@NotBlank(message = "Owner Name cannot be null")
	private String ownerName;
	
	@JsonProperty("mobilenumber")
	@NotBlank(message = "Owner Mobile Number cannot be Blank")
	private String mobileNumber; 
	
	@JsonProperty("revenuenotificationid")
	private String revenueNotificationId;
	
	@JsonProperty("ownertype")
	private String ownerType;
	
	@JsonProperty("createdby")
	private String createdBy;
	
	@JsonProperty("createdtime")
	private Long createdTime;
	
	@JsonProperty("lastmodifiedby")
	private String lastModifiedBy;
	
	@JsonProperty("lastmodifiedtime")
	private Long lastModifiedTime;
	
}
