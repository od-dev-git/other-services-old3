package org.egov.dx.web.models.TL;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeLicenseSearchCriteria {

	 	@JsonProperty("tenantId")
	    private String tenantId;

	    @JsonProperty("status")
	    private String status;

	    @JsonProperty("applicationType")
	    private String applicationType;

	    @JsonProperty("ids")
	    private List<String> ids;

	    @JsonProperty("applicationNumber")
	    private String applicationNumber;

	    @JsonProperty("licenseNumbers")
	    private List<String> licenseNumbers;

	    @JsonProperty("oldLicenseNumber")
	    private String oldLicenseNumber;

	    @JsonProperty("mobileNumber")
	    private String mobileNumber;
	    
	    @JsonProperty("employeeUuid")
	    private String employeeUuid;

	    @JsonIgnore
	    private String accountId;


	    @JsonProperty("fromDate")
	    private Long fromDate = null;

	    @JsonProperty("toDate")
	    private Long toDate = null;

	    @JsonProperty("businessService")
	    private String businessService = null;

	    @JsonProperty("validTo")
	    private Long validTo = null;

	    @JsonProperty("offset")
	    private Integer offset;

	    @JsonProperty("limit")
	    private Integer limit;

	    @JsonProperty("councilForArchNumber")
	    private String councilForArchNumber;
	    
	    @JsonIgnore
	    private List<String> ownerIds;
	    
	    @JsonProperty("searchType")
	    private String searchType;


	    public boolean isEmpty() {
	        return (this.tenantId == null && this.status == null && this.applicationType == null && this.ids == null && this.applicationNumber == null
	                && this.licenseNumbers == null && this.oldLicenseNumber == null && this.mobileNumber == null &&
	                this.fromDate == null && this.toDate == null && this.ownerIds == null
	        );
	    }

	    public boolean tenantIdOnly() {
	        return (this.tenantId != null && this.status == null && this.applicationType == null && this.ids == null && this.applicationNumber == null
	                && this.licenseNumbers == null && this.oldLicenseNumber == null && this.mobileNumber == null &&
	                this.fromDate == null && this.toDate == null && this.ownerIds == null
	        );
	    }
	
}
