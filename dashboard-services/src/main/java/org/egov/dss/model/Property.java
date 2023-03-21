package org.egov.dss.model;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.dss.model.enums.InstrumentStatusEnum;
import org.egov.dss.model.enums.PaymentModeEnum;
import org.egov.dss.model.enums.PaymentStatusEnum;

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
public class Property {
	
	    @Size(max=64)
	    @JsonProperty("id")
	    private String id;

	   
	    @Size(max=64)
	    @JsonProperty("tenantId")
	    private String tenantId;

	    @JsonProperty("propertyId")
	    private String propertyId;

	    @JsonProperty("oldPropertyId")
	    private String oldPropertyId;

	    @JsonProperty("status")
	    private String status;

	    @JsonProperty("propertyType")
	    private String propertyType;

	    @JsonProperty("ownershipCategory")
	    private String ownershipCategory;

	    @JsonProperty("usageCategory")
	    private String usageCategory;

	    @Size(max=128)
	    @JsonProperty("instrumentNumber")
	    private String instrumentNumber;

	    @JsonProperty("instrumentStatus")
	    private InstrumentStatusEnum instrumentStatus;

	    @Size(max=64)
	    @JsonProperty("ifscCode")
	    private String ifscCode;

	    @JsonProperty("auditDetails")
	    private AuditDetails auditDetails;

	    @JsonProperty("additionalDetails")
	    private JsonNode additionalDetails;

	    @JsonProperty("paymentDetails")
	    @Valid
	    private List<PaymentDetail> paymentDetails;

	    @Size(max=128)
	    @NotNull
	    @JsonProperty("paidBy")
	    private String paidBy = null;

	    @Size(max=64)
	    @JsonProperty("mobileNumber")
	    private String mobileNumber = null;

	    @Size(max=128)
	    @JsonProperty("payerName")
	    private String payerName = null;

	    @Size(max=1024)
	    @JsonProperty("payerAddress")
	    private String payerAddress = null;

	    @Size(max=1024)
	    @JsonProperty("narration")
	    private String narration = null;
	    
	    @Size(max=64)
	    @JsonProperty("payerEmail")
	    private String payerEmail = null;

	    @Size(max=64)
	    @JsonProperty("payerId")
	    private String payerId = null;

	    @JsonProperty("paymentStatus")
	    private PaymentStatusEnum paymentStatus;

}
