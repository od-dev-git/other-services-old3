package org.egov.report.web.model;

import org.egov.report.web.model.WaterNewConsumerMonthlyResponse.WaterNewConsumerMonthlyResponseBuilder;

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
public class ConsumerBillHistoryResponse {
	
	@JsonProperty("ulb")
	private String ulb;
	
	@JsonProperty("oldConnectionNo")
	private String oldConnectionNo;
	
	@JsonProperty("monthYear")
	private Long monthYear;
	
	@JsonProperty("billNo")
	private String billNo;
	
	@JsonProperty("previousDue")
	private Long previousDue;
	
	@JsonProperty("adjustedAmt")
	private String adjustedAmt;
	
	@JsonProperty("previousPayment")
	private String previousPayment;
	
	@JsonProperty("rebateAvailed")
	private String rebateAvailed;
	
	@JsonProperty("fineLevied")
	private Long fineLevied;
	
	@JsonProperty("currentWSDemand")
	private String currentWSDemand;
	
	@JsonProperty("currentSWDemand")
	private String currentSWDemand;
	
	@JsonProperty("netPayment")
	private String netPayment;
	
	@JsonProperty("NPR")
	private String NPR;
	
	@JsonProperty("NPF")
	private String NPF;
	
	@JsonProperty("billDate")
	private String billDate;
	
	@JsonProperty("rebateDate")
	private String rebateDate;
	
	@JsonProperty("previousReading")
	private String previousReading;
	
	@JsonProperty("currentReading")
	private String currentReading;
	
	@JsonProperty("totalUnitsConsumed")
	private String totalUnitsConsumed;

}
