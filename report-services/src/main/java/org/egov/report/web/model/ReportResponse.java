package org.egov.report.web.model;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
	
	@JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
	
	@JsonProperty("IncentiveInfo")
	private IncentiveResponse incentiveResponse;
	
	@JsonProperty("employeeDateWiseWSCollectionResponse")
	private List<EmployeeDateWiseWSCollectionResponse> employeeDateWiseWSCollectionResponse;
	
	@JsonProperty("ComsumerMasterWSReports")
	private List<ConsumerMasterWSReportResponse> consumerMasterWSReportResponses;

	@JsonProperty("consumerPaymentHistoryResponse")
	private List<ConsumerPaymentHistoryResponse> consumerPaymentHistoryResponse; 
	
	@JsonProperty("waterMonthlyDemandResponse")
	private List<WaterMonthlyDemandResponse> waterMonthlyDemandResponse;
}
