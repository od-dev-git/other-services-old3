package org.egov.dss.model;

import java.math.BigDecimal;
import java.util.List;

import org.egov.dss.model.Bill.StatusEnum;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chart {

	  @JsonProperty("name")
	  private String name = null;

	  @JsonProperty("value")
	  private BigDecimal value  = null;

}