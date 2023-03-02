package org.egov.integration.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.egov.integration.model.enums.Status;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {

	@JsonProperty("owners")
	@Valid
	private List<OwnerInfo> owners;

	@JsonProperty("propertyId")
	private String propertyId;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("status")
	private Status status;

	@JsonProperty("address")
	private Address address;

}
