package org.egov.report.web.model;

import java.math.BigDecimal;

import org.egov.report.web.model.WaterConnectionDetails.WaterConnectionDetailsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaterDemandResponse {

	@JsonProperty("tenantid")
	private String tenantid;

	@JsonProperty("id")
	private String demandId;

	@JsonProperty("taxperiodfrom")
	private Long taxperiodfrom;

	@JsonProperty("taxperiodto")
	private Long taxperiodto;

	@JsonProperty("taxheadcode")
	private String taxheadcode;

	@JsonProperty("taxamount")
	private BigDecimal taxamount;

	@JsonProperty("collectionamount")
	private BigDecimal collectionamount;
	
}
