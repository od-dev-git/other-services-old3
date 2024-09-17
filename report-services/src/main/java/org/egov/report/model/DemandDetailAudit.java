package org.egov.report.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DemandDetailAudit {

	@JsonProperty("id")
	private String id;

	@JsonProperty("consumercode")
	private String consumercode;

	@JsonProperty("businessservice")
	private String businessservice;

	@JsonProperty("taxperiodfrom")
	private Long taxperiodfrom;

	@JsonProperty("taxperiodto")
	private Long taxperiodto;

	@JsonProperty("entryRank")
	private int entryRank;

	@JsonProperty("taxheadcode")
	private String taxheadcode;

	@JsonProperty("demandTaxamount")
	private BigDecimal demandTaxamount;

	@JsonProperty("cumulativeTaxamount")
	private BigDecimal cumulativeTaxamount;

	@JsonProperty("demandCollectionamount")
	private BigDecimal demandCollectionamount;

	@JsonProperty("cumulativeCollectionamount")
	private BigDecimal cumulativeCollectionamount;

	@JsonProperty("demandDetailCreatedby")
	private String demandDetailCreatedby;

	@JsonProperty("demandDetailCreatedtime")
	private Long demandDetailCreatedtime;

	@JsonProperty("demandDetailLastmodifiedby")
	private String demandDetailLastmodifiedby;

	@JsonProperty("demandDetailLastmodifiedtime")
	private Long demandDetailLastmodifiedtime;

}
