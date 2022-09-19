package org.egov.dss.model;

import java.math.BigDecimal;
import java.util.List;

import org.egov.dss.model.Bill.BillBuilder;
import org.egov.dss.model.Bill.StatusEnum;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageTypeResponse {

	@JsonProperty("usageCategory")
	private String usageCategory;
	
	@JsonProperty("consumerCode")
	private String consumerCode;
	
	@JsonProperty("amount")
	@Builder.Default
	private BigDecimal amount = BigDecimal.ZERO;
}
