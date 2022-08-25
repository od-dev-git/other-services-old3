package org.egov.report.model;

import java.util.List;

import javax.validation.Valid;

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

	@JsonProperty("Id")
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
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("acknowledgementnumber")
	private String acknowledgementnumber;
	
	@JsonProperty("propertytype")
	private String propertytype;
	
	@JsonProperty("ownershipcategory")
	private String ownershipcategory;
	
	@JsonProperty("usagecategory")
	private String usagecategory;
	
	@JsonProperty("creationreason")
	private String creationreason;
	
	@JsonProperty("nooffloors")
	private String nooffloors;
	
	@JsonProperty("landarea")
	private String landarea;
	
	@JsonProperty("superbuiltarea")
	private String superbuiltarea;
	
	@JsonProperty("linkedproperties")
	private String linkedproperties;
	
	@JsonProperty("source")
	private String source;
	
	@JsonProperty("channel")
	private String channel;
	
	@JsonProperty("createdby")
	private String createdby;
	
	@JsonProperty("lastmodifiedby")
	private String lastmodifiedby;
	
	@JsonProperty("createdtime")
	private String createdtime;
	
	@JsonProperty("lastmodifiedtime")
	private String lastmodifiedtime;
	
	@JsonProperty("additionaldetails")
	private Object additionaldetails;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("owners")
	private List<OwnerInfo> owners;
	
	
	
}
