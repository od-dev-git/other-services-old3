package org.egov.integration.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.egov.integration.model.revenue.RevenueNotification;
import org.egov.integration.model.revenue.RevenueOwner;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ConsumerVerification {

	@JsonProperty("consumerNo")
	private String consumerNo;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("businessService")
	private String businessService;
	
	@JsonProperty("owner")
	private List<ConsumerVerificationOwner> consumerVerificationOwner;
	
	@JsonProperty("status")
	private String status;
	
}
