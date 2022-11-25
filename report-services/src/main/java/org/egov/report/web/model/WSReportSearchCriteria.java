package org.egov.report.web.model;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSReportSearchCriteria {
	
	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("module")
	private String module;
	
	@JsonProperty("fromDate")
	private Long fromDate;
	
	@JsonProperty("toDate")
	private Long toDate;
	
	@JsonProperty("collectionDate")
	private Long collectionDate;
	
	@JsonProperty("paymentMode")
	private String paymentMode;

	@JsonProperty("consumerNumber")
	private String consumerCode;

    @JsonProperty("consumerNumbers")
    private Set<String> consumerNumbers;

	@JsonProperty("connectionType")
	private String connectionType;
	
	@JsonProperty("ward")
	private String ward;

	@JsonProperty("monthYear")
	private Long monthYear;
	
	@JsonProperty("oldConnectionNo")
	private String oldConnectionNo;
	
	@JsonProperty("limit")
    private Integer limit ;
    
    @JsonProperty("offset")
    private Integer offset ;

}
