package org.egov.report.web.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDemandResponse {
	
	@JsonProperty("consumercode")
	private String consumercode;
	
	@JsonProperty("oldpropertyid")
	private String oldpropertyid;
	
	@JsonProperty("uuid")
	private String uuid;
	
	@JsonProperty("ward")
	private String ward;

	@JsonProperty("id")
	private String id;

	@JsonProperty("payer")
	private String payer;

	@JsonProperty("createdby")
	private String createdby;
	
	@JsonProperty("taxperiodfrom")
	private Long taxperiodfrom;

	@JsonProperty("taxperiodto")
	private Long taxperiodto;
	
	@JsonProperty("tenantid")
	private String tenantid;

	@JsonProperty("status")
	private String status;
	
	@JsonProperty("taxamount")
	private BigDecimal taxamount;

	@JsonProperty("collectionamount")
	private BigDecimal collectionamount;

}
