package org.egov.dx.web.models.TL;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeLicense {

	@Size(max = 64)
	@JsonProperty("id")
	private String id = null;

	@NotNull
	@Size(max = 64)
	@JsonProperty("tenantId")
	private String tenantId = null;

	public enum LicenseTypeEnum {
		TEMPORARY("TEMPORARY"),

		PERMANENT("PERMANENT");

		private String value;

		LicenseTypeEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static LicenseTypeEnum fromValue(String text) {
			for (LicenseTypeEnum b : LicenseTypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	public enum ApplicationTypeEnum {
		NEW("NEW"),

		RENEWAL("CORRECTION"),

		CORRECTION("RENEWAL");

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

	@JsonProperty("businessService")
	private String businessService = "TL";

	@JsonProperty("licenseType")
	private LicenseTypeEnum licenseType = null;

	@JsonProperty("applicationType")
	private ApplicationTypeEnum applicationType = null;

	@JsonProperty("workflowCode")
	private String workflowCode = null;

	@Size(max = 64)
	@JsonProperty("licenseNumber")
	private String licenseNumber = null;

	@Size(max = 64)
	@JsonProperty("applicationNumber")
	private String applicationNumber;

	@Size(max = 64)
	@JsonProperty("paymentRefApplication")
	private String paymentRefApplication;

	@Size(max = 64)
	@JsonProperty("oldLicenseNumber")
	private String oldLicenseNumber = null;

	@Size(max = 256)
	@JsonProperty("propertyId")
	private String propertyId = null;

	@Size(max = 64)
	@JsonProperty("oldPropertyId")
	private String oldPropertyId = null;

	@Size(max = 64)
	@JsonProperty("accountId")
	private String accountId = null;

	@Size(max = 256)
	@JsonProperty("tradeName")
	private String tradeName = null;

	@JsonProperty("applicationDate")
	private Long applicationDate = null;

	@JsonProperty("commencementDate")
	private Long commencementDate = null;

	@JsonProperty("issuedDate")
	private Long issuedDate = null;

	@Size(max = 64)
	@JsonProperty("financialYear")
	private String financialYear = null;

	@JsonProperty("validFrom")
	private Long validFrom = null;

	@JsonProperty("validTo")
	private Long validTo = null;

	@NotNull
	@Size(max = 64)
	@JsonProperty("action")
	private String action = null;

	@JsonProperty("assignee")
	private List<String> assignee = null;

	@Size(max = 64)
	@JsonProperty("status")
	private String status = null;

	@Valid
	@NotNull
	@JsonProperty("tradeLicenseDetail")
	private TradeLicenseDetail tradeLicenseDetail = null;

	@Size(max = 128)
	private String comment;

	@JsonProperty("fileStoreId")
	private String fileStoreId = null;

}
