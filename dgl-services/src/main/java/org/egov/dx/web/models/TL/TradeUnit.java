package org.egov.dx.web.models.TL;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.dx.web.models.Address;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeUnit {
	
	@Size(max=64)
    @JsonProperty("id")
    private String id;

    @Size(max=64)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("active")
    private Boolean active;

    @Size(max=64)
    @JsonProperty("tradeType")
    private String tradeType = null;

    @Size(max=64)
    @JsonProperty("uom")
    private String uom = null;

    @Size(max=64)
    @JsonProperty("uomValue")
    private String uomValue = null;

}
