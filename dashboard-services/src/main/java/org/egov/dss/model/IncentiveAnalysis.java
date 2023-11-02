package org.egov.dss.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncentiveAnalysis {
	
	@JsonProperty("employeeName")
	private String empName;
	
	@JsonProperty("employeeId")
	private String empId;
	
	@JsonProperty("totalNoOfTransaction")
	@Builder.Default
	private Long totalNoOfTransaction = 0L;
	
	@JsonProperty("collectionTowardsArrear")
	@Builder.Default
	private BigDecimal collectionTowardsArrear = BigDecimal.ZERO;
	
	@JsonProperty("collectionTowardsCurrent")
	@Builder.Default
	private BigDecimal collectionTowardsCurrent = BigDecimal.ZERO;
	
	@JsonProperty("totalCollection")
	@Builder.Default
	private BigDecimal totalCollection = BigDecimal.ZERO;
	
	@JsonProperty("totalIncentiveOnArrear")
	@Builder.Default
	private BigDecimal totalIncentiveOnArrear = BigDecimal.ZERO;
	
	@JsonProperty("totalIncentiveOnCurrent")
	@Builder.Default
	private BigDecimal totalIncentiveOnCurrent = BigDecimal.ZERO;
	
	@JsonProperty("totalIncentive")
	@Builder.Default
	private BigDecimal totalIncentive = BigDecimal.ZERO;
	
	@JsonIgnore
	@Builder.Default
	private Boolean isJalsathiCollection = false;
	
}
