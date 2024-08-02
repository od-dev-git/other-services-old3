package org.egov.report.web.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

import org.springframework.util.StringUtils;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TradeLicenseSearchCriteria {


    @JsonProperty("tenantId")
    private String tenantId;
    
    @JsonProperty("tenant")
    private String tenant;

    @JsonProperty("status")
    private String status;

    @JsonProperty("applicationType")
    private String applicationType;

    @JsonProperty("ids")
    private List<String> ids;
    
    @JsonProperty("applicationNumbersNotIn")
    private List<String> applicationNumbersNotIn;

    @JsonProperty("applicationNumber")
    private String applicationNumber;

    @JsonProperty("licenseNumbers")
    private List<String> licenseNumbers;
    
    @JsonProperty("applicationNumbers")
    private List<String> applicationNumbers;

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
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("licenseType")
    private String licenseType;


}