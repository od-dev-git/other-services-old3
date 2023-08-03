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
public class TradeLicenseDetail {

	@JsonProperty("id")
	@Size(max = 64)
	private String id;

	@JsonProperty("surveyNo")
	@Size(max = 64)
	private String surveyNo = null;

	@Size(max = 64)
	@JsonProperty("subOwnerShipCategory")
	private String subOwnerShipCategory = null;

	@Size(max = 64)
	@JsonProperty("structureType")
	private String structureType;

	@JsonProperty("operationalArea")
	private Double operationalArea;

	@JsonProperty("noOfEmployees")
	private Integer noOfEmployees;

	@JsonProperty("adhocExemption")
	private BigDecimal adhocExemption;

	@JsonProperty("adhocPenalty")
	private BigDecimal adhocPenalty;

	@Size(max = 1024)
	@JsonProperty("adhocExemptionReason")
	private String adhocExemptionReason;

	@Size(max = 1024)
	@JsonProperty("adhocPenaltyReason")
	private String adhocPenaltyReason;

	@NotNull
	@JsonProperty("owners")
	@Valid
	private List<OwnerInfo> owners = new ArrayList<>();
	
	@NotNull
    @JsonProperty("tradeUnits")
    @Valid
    private List<TradeUnit> tradeUnits = new ArrayList<>();

	/**
	 * License can be created from different channels
	 */
	public enum ChannelEnum {
		COUNTER("COUNTER"),

		CITIZEN("CITIZEN"),

		DATAENTRY("DATAENTRY");

		private String value;

		ChannelEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static ChannelEnum fromValue(String text) {
			for (ChannelEnum b : ChannelEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("channel")
	private ChannelEnum channel = null;

	@NotNull
	@Valid
	@JsonProperty("address")
	private AddressTL address = null;

	@JsonProperty("dscDetails")
	@Valid
	private List<DscDetails> dscDetails = null;

	@JsonProperty("additionalDetail")
	private JsonNode additionalDetail = null;

	public TradeLicenseDetail addOwnersItem(OwnerInfo ownersItem) {
		if (this.owners == null)
			this.owners = new ArrayList<>();
		if (!this.owners.contains(ownersItem))
			this.owners.add(ownersItem);
		return this;
	}

	public TradeLicenseDetail addDscDetailsItem(DscDetails dscDetailsItem) {
		if (this.dscDetails == null) {
			this.dscDetails = new ArrayList<>();
		}
		if (!this.dscDetails.contains(dscDetailsItem))
			this.dscDetails.add(dscDetailsItem);
		return this;
	}
	
	public TradeLicenseDetail addTradeUnitsItem(TradeUnit tradeUnitsItem) {
        if(this.tradeUnits==null)
            this.tradeUnits = new ArrayList<>();
        if(!this.tradeUnits.contains(tradeUnitsItem))
            this.tradeUnits.add(tradeUnitsItem);
        return this;
    }

}
