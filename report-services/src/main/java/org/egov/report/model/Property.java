package org.egov.report.model;

import java.util.List;

import javax.validation.Valid;

import org.egov.report.model.Address;
import org.egov.report.web.model.OwnerInfo;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("propertyId")
	private String propertyId;
	
	@JsonProperty("tenantId")
	private String tenantid;
	
	@JsonProperty("surveyId")
	private String surveyid;
	
	@JsonProperty("accountId")
	private String accountid;
	
	@JsonProperty("oldPropertyId")
	private String oldPropertyId;
	
//	@JsonProperty("status")
//	private Object status;
	
	@JsonProperty("acknowledgementNumber")
	private String acknowledgementNumber;
	
	@JsonProperty("propertyType")
	private String propertyType;
	
	@JsonProperty("ownershipCategory")
	private String ownershipCategory;
	
	@JsonProperty("usageCategory")
	private String usageCategory;
	
//	@JsonProperty("creationreason")
//	private String creationreason;
//	
//	@JsonProperty("nooffloors")
//	private String nooffloors;
//	
//	@JsonProperty("landarea")
//	private String landarea;
//	
//	@JsonProperty("superbuiltarea")
//	private String superbuiltarea;
//	
//	@JsonProperty("linkedproperties")
//	private String linkedproperties;
//	
//	@JsonProperty("source")
//	private String source;
//	
//	@JsonProperty("channel")
//	private String channel;
	
//	@JsonProperty("createdby")
//	private String createdby;
//	
//	@JsonProperty("lastmodifiedby")
//	private String lastmodifiedby;
//	
//	@JsonProperty("createdtime")
//	private String createdtime;
//	
//	@JsonProperty("lastmodifiedtime")
//	private String lastmodifiedtime;
	
//	@JsonProperty("additionalDetails")
//	private Object additionalDetails;
	
	
	@JsonProperty("address")
	private Address address ;
	
	@JsonProperty("owners")
	private List<OwnerInfo> owners;
	
	
	
}
