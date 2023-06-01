package org.egov.dx.web.models.MR;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.dx.model.enums.InstrumentStatusEnum;
import org.egov.dx.model.enums.PaymentModeEnum;
import org.egov.dx.model.enums.PaymentStatusEnum;
import org.egov.dx.web.models.AuditDetails;
import org.egov.dx.web.models.DscDetails;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class MarriageRegistration {

	@Size(max = 64)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Size(max = 64)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@Size(max = 64)
	@JsonProperty("accountId")
	private String accountId = null;

	public enum ApplicationTypeEnum {
		NEW("NEW"),

		CORRECTION("CORRECTION");

		private String value;

		ApplicationTypeEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static ApplicationTypeEnum fromValue(String text) {
			for (ApplicationTypeEnum b : ApplicationTypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	// @NotNull(message = "applicationType is mandatory ")
	@JsonProperty("applicationType")
	private ApplicationTypeEnum applicationType = null;

	@JsonProperty("businessService")
	private String businessService = "MR";

	@JsonProperty("workflowCode")
	private String workflowCode = null;

	@Size(max = 64)
	@JsonProperty("mrNumber")
	private String mrNumber = null;

	@Size(max = 64)
	@JsonProperty("applicationNumber")
	private String applicationNumber;

	@JsonProperty("applicationDate")
	private Long applicationDate = null;

	@JsonProperty("marriageDate")
	private Long marriageDate = null;

	@JsonProperty("issuedDate")
	private Long issuedDate = null;

	@JsonProperty("isTatkalApplication")
	private Boolean isTatkalApplication;

	@JsonProperty("slaEndTime")
	private Long slaEndTime;

	@JsonProperty("additionalDetails")
	private Object additionalDetails = null;

	@NotNull
	@Size(max = 64)
	@JsonProperty("action")
	private String action = null;

	@Size(max = 64)
	@JsonProperty("status")
	private String status = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("dscDetails")
	@Valid
	private List<DscDetails> dscDetails = null;

	@JsonProperty("coupleDetails")
	@Valid
	private List<Couple> coupleDetails = null;

	public MarriageRegistration addCoupleDetailsItem(CoupleDetails coupleDetailItem) {
		if (this.coupleDetails == null) {
			this.coupleDetails = new ArrayList<>();
		}
		if (!coupleDetailItem.getIsGroom()) {
			if (this.coupleDetails.size() > 0) {
				if (this.coupleDetails.get(0).getBride() != null
						&& !this.coupleDetails.get(0).getBride().equals(coupleDetailItem))
					this.coupleDetails.get(0).setBride(coupleDetailItem);
				else if (this.coupleDetails.get(0).getBride() == null)
					this.coupleDetails.get(0).setBride(coupleDetailItem);
			} else {
				Couple couple = new Couple();
				couple.setBride(coupleDetailItem);
				this.coupleDetails.add(couple);
			}
		}
		if (coupleDetailItem.getIsGroom()) {
			if (this.coupleDetails.size() > 0) {
				if (this.coupleDetails.get(0).getGroom() != null
						&& !this.coupleDetails.get(0).getGroom().equals(coupleDetailItem))
					this.coupleDetails.get(0).setGroom(coupleDetailItem);
				else if (this.coupleDetails.get(0).getGroom() == null)
					this.coupleDetails.get(0).setGroom(coupleDetailItem);
			} else {
				Couple couple = new Couple();
				couple.setGroom(coupleDetailItem);
				this.coupleDetails.add(couple);
			}
		}

		return this;
	}

	public MarriageRegistration addDscDetailsItem(DscDetails dscDetailsItem) {
		if (this.dscDetails == null) {
			this.dscDetails = new ArrayList<>();
		}
		if (!this.dscDetails.contains(dscDetailsItem))
			this.dscDetails.add(dscDetailsItem);
		return this;
	}

}
