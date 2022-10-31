package org.egov.integration.web.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.integration.model.revenue.RevenueNotification;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevenueIntegrationResponse {
	
	@JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;
	
	@JsonProperty("Notifications")
	private List<RevenueNotification> revenueNotification;

}
