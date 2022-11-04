package org.egov.integration.model.revenue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
	private String districtName;
	
	@JsonProperty("tenantid")
	@NotBlank(message = "Tenant Id cannot be Blank")
	private String tenantId;
	
	@JsonProperty("revenuevillage")
	@NotBlank(message = "Revenue Village cannot be Blank")
	private String revenueVillage;
	
	@JsonProperty("plotno")
	@NotBlank(message = "Plot Number cannot be Blank")
	private String plotNo;
	
	@JsonProperty("flatno")
	@NotBlank(message = "Flat Number cannot be Blank")
	private String flatNo; 
	
	@JsonProperty("address")
	@NotBlank(message = "Address cannot be Blank")
	private String address;
	
	@JsonProperty("actiontaken")
	private Boolean actionTaken;
	
	@JsonProperty("action")
	private String action;
	
	@JsonProperty("additionaldetails")
	private Object additionalDetails;
	
	@JsonProperty("createdby")
	private String createdBy;
	
	@JsonProperty("createdtime")
	private Long createdTime;
	
	@JsonProperty("lastmodifiedby")
	private String lastModifiedBy;
	
	@JsonProperty("lastmodifiedtime")
	private Long lastModifiedTime;
	
	@JsonProperty("waterconsumerno")
	private String waterConsumerNo;
	
	@JsonProperty("propertyid")
	private String propertyId;
	
	@JsonProperty("currentowner")
	private List<@Valid RevenueOwner> currentOwner;
	
	@JsonProperty("newowner")
	private List<@Valid RevenueOwner> newOwner;
	
	public RevenueNotification addCurrentOwners(RevenueOwner owners) {
        if (this.currentOwner == null) {
        this.currentOwner = new ArrayList<>();
        }
        if(!this.currentOwner.contains(owners))
            this.currentOwner.add(owners);
        return this;
    }
	  
	  public RevenueNotification addNewOwners(RevenueOwner owners) {
        if (this.newOwner == null) {
        this.newOwner = new ArrayList<>();
        }
        if(!this.newOwner.contains(owners))
            this.newOwner.add(owners);
        return this;
    }
}
