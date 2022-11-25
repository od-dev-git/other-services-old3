package org.egov.report.web.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaterMonthlyDemandResponse {
	
	private String tenantId;
	
	@JsonProperty("ulb")
	private String ulb;
	
	@JsonProperty("ward")
	private String ward;
	
	@JsonProperty("connectionNo")
	private String connectionNo;
	
	@JsonProperty("oldconnectionno")
    private String oldconnectionno;
	
	@JsonProperty("connectionType")
	private String connectionType;
	
	@JsonProperty("connectionHolderName")
	private String connectionHolderName;
	
	@JsonProperty("contactNo")
	private String mobile;
	
	@JsonProperty("address")
	private String addrss;
	
	@JsonProperty("demandPeriodFrom")
	private String taxPriodFrom;
	
	@JsonProperty("demandPriodTo")
	private String taxPeriodTo; 
	
	@JsonProperty("currentDemandAmount")
	@Default
	private BigDecimal currentDemandAmt = BigDecimal.ZERO;
	
	@JsonProperty("collectionAmount")
	@Default
	private BigDecimal collectedAmt = BigDecimal.ZERO;
	
	@JsonProperty("sewageCurrentDemandAmount")
    @Default
    private BigDecimal sewageCurrentDemandAmount = BigDecimal.ZERO;
    
    @JsonProperty("sewageCollectionAmount")
    @Default
    private BigDecimal sewageCollectionAmount = BigDecimal.ZERO;
	
	@JsonProperty("rebateAmount")
	@Default
	private BigDecimal rebateAmt = BigDecimal.ZERO;
	
	@JsonProperty("penaltyAmount")
	@Default
	private BigDecimal penaltyAmt = BigDecimal.ZERO;
	
	@JsonProperty("advanceAmount")
	@Default
	private BigDecimal advanceAmt = BigDecimal.ZERO;
	
	@JsonProperty("arrearAmt")
	@Default
	private BigDecimal arrearAmt = BigDecimal.ZERO;
	
	@JsonProperty("totalDueAmount")
	@Default
	private BigDecimal totalDueAmt = BigDecimal.ZERO;
	
	@JsonProperty("amountPayableAfterRebateAmount")
	@Default
	private BigDecimal payableAfterRebateAmt = BigDecimal.ZERO;
	
	@JsonProperty("amountPayableWithPenaltyAmount")
	@Default
	private BigDecimal payableWithPenaltyAmt = BigDecimal.ZERO;

}
