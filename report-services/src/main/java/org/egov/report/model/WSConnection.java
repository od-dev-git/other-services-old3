package org.egov.report.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.egov.report.model.Payment.PaymentBuilder;
import org.egov.report.model.enums.InstrumentStatusEnum;
import org.egov.report.model.enums.PaymentModeEnum;
import org.egov.report.model.enums.PaymentStatusEnum;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class WSConnection {
	
	@JsonProperty("waterSource")
	private String waterSource;

	@JsonProperty("meterId")
	private String meterId;

	@JsonProperty("meterInstallationDate")
	private Long meterInstallationDate;
	
	@JsonProperty("connectionHolders")
	private List<LinkedHashMap<String, Object>> connectionHolders;
	
	@JsonProperty("additionalDetails")
	private LinkedHashMap<String, Object> additionalDetails;
	
	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("applicationStatus")
	private String applicationStatus;
	
	@JsonProperty("usageCategory")
	private String usageCategory;
	
	@JsonProperty("connectionNo")
	private String connectionNo;

}
