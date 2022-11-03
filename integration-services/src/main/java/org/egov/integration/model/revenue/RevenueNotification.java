package org.egov.integration.model.revenue;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RevenueNotification {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("districtname")
	@NotBlank(message = "District Name cannot be Blank")
	private String districtname;
	
	@JsonProperty("tenantid")
	@NotBlank(message = "Tenant Id cannot be Blank")
	private String tenantid;
	
	@JsonProperty("revenuevillage")
	@NotBlank(message = "Revenue Village cannot be Blank")
	private String revenuevillage;
	
	@JsonProperty("plotno")
	@NotBlank(message = "Plot Number cannot be Blank")
	private String plotno;
	
	@JsonProperty("flatno")
	@NotBlank(message = "Flat Number cannot be Blank")
	private String flatno; 
	
	@JsonProperty("address")
	@NotBlank(message = "Address cannot be Blank")
	private String address;
	
	@JsonProperty("actiontaken")
	private Boolean actiontaken;
	
	@JsonProperty("action")
	private String action;
	
	@JsonProperty("additionaldetails")
	private Object additionaldetails;
	
	@JsonProperty("createdby")
	private String createdby;
	
	@JsonProperty("createdtime")
	private Long createdtime;
	
	@JsonProperty("lastmodifiedby")
	private String lastmodifiedby;
	
	@JsonProperty("lastmodifiedtime")
	private Long lastmodifiedtime;
	
	@JsonProperty("waterconsumerno")
	private String waterConsumerNo;
	
	@JsonProperty("propertyid")
	private String propertyId;
	
	@JsonProperty("currentowner")
	@NotBlank(message="Current Owner Details cannot be empty")
	List<@Valid RevenueOwner> currentOwner;
	
	@JsonProperty("newowner")
	@NotBlank(message="New Owner Details cannot be empty")
	List<@Valid RevenueOwner> newOwner;
	
}
