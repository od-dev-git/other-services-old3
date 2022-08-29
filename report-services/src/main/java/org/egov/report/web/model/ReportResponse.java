package org.egov.report.web.model;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.report.web.model.PropertyDetailsResponse;

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
	
	@JsonProperty("billSummaryResponses")
	private List<BillSummaryResponses> billSummaryResponses;
	
	@JsonProperty("waterNewConsumerMonthlyResponses")
	private List<WaterNewConsumerMonthlyResponse> waterNewConsumerMonthlyResponses;
	
	@JsonProperty("consumerBillHistoryResponse")
	private List<ConsumerBillHistoryResponse> consumerBillHistoryResponse;
	
	@JsonProperty("wsConsumerHistoryReport")
	private List<WSConsumerHistoryResponse> wsConsumerHistoryReport;
	
	@JsonProperty("propertyDetailsInfo")
	private List<PropertyDetailsResponse> propertyDetailsResponse;

	@JsonProperty("propertyWiseCollectionResponse")
	private List<PropertyWiseCollectionResponse> propertyWiseCollectionResponse;
	
	@JsonProperty("propertyWiseDemandResponse")
	private List<PropertyWiseDemandResponse> propertyWiseDemandResponse;
	
	@JsonProperty("ulbWiseTaxCollectionResponse")
	private List<ULBWiseTaxCollectionResponse> ulbWiseTaxCollectionResponse;

	@JsonProperty("taxCollectorWiseCollectionResponse")
	private List<TaxCollectorWiseCollectionResponse> taxCollectorWiseCollectionResponse;
	
	@JsonProperty("employeeWiseWSCollectionResponse")
	private List<EmployeeWiseWSCollectionResponse> employeeWiseWSCollectionResponse;
	
	@JsonProperty("monthWisePendingBillGenerationResponse")
	private List<MonthWisePendingBillGenerationResponse> monthWisePendingBillGenerationResponse;
	
	@JsonProperty("wsSchedulerBasedDemandsGenerationReponse")
	private List<WsSchedulerBasedDemandsGenerationReponse> wsSchedulerBasedDemandsGenerationReponse;
		
}
