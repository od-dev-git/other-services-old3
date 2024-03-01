package org.egov.report.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("txnId")
	private String txnId;
	
	@JsonProperty("txnAmount")
	private Long txnAmount;
	
	@JsonProperty("txnStatus")
	private String txnStatus;
	
	@JsonProperty("gateway")
	private String gateway;
	
	@JsonProperty("gatewayTxnId")
	private String gatewayTxnId;
	
	@JsonProperty("gatewayPaymentMode")
	private String gatewayPaymentMode;
	
	@JsonProperty("gatewayStatusCode")
	private String gatewayStatusCode;
	
	@JsonProperty("consumerCode")
	private String consumerCode;
	
	@JsonProperty("billId")
	private String billId;
	
	@JsonProperty("module")
	private String module;
	
}
