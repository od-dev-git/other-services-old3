package org.egov.integration.model.revenue;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueNotificationSearchCriteria {

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("limit")
	private Integer limit;

	@JsonProperty("offset")
	private Integer offset;

}
