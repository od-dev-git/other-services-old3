package org.egov.report.web.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.Role;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OwnerInfo extends User{

	@JsonProperty("ownerInfoUuid")
	private String ownerInfoUuid;
	
	@NotNull
	@JsonProperty("mobileNumber")
	private String mobileNumber;

	@JsonProperty("gender")
	private String gender;

	@JsonProperty("fatherOrHusbandName")
	private String fatherOrHusbandName;

	@JsonProperty("correspondenceAddress")
	private String correspondenceAddress;

	@JsonProperty("isPrimaryOwner")
	private Boolean isPrimaryOwner;

	@JsonProperty("ownerShipPercentage")
	private Double ownerShipPercentage;

	@NotNull
	@JsonProperty("ownerType")
	private String ownerType;

	@JsonProperty("institutionId")
	private String institutionId;

	/**
	 * Populates Owner fields from the given User object
	 * 
	 * @param user User object obtained from user service
	 */
	public void addUserDetail(User user) {
		this.setUserName(user.getUserName());
		this.setSalutation(user.getSalutation());
		this.setName(user.getName());
		this.setGender(user.getGender());
		this.setMobileNumber(user.getMobileNumber());
		this.setEmailId(user.getEmailId());
		this.setAltContactNumber(user.getAltContactNumber());
		this.setPan(user.getPan());
		this.setAadhaarNumber(user.getAadhaarNumber());
		this.setPermanentAddress(user.getPermanentAddress());
		this.setPermanentCity(user.getPermanentCity());
		this.setPermanentPincode(user.getPermanentPincode());
		this.setCorrespondenceAddress(user.getCorrespondenceAddress());
		this.setCorrespondenceCity(user.getCorrespondenceCity());
		this.setCorrespondencePincode(user.getCorrespondencePincode());
		this.setActive(user.getActive());
		this.setLocale(user.getLocale());
		this.setType(user.getType());
		this.setAccountLocked(user.getAccountLocked());
		this.setRoles(user.getRoles());
		this.setFatherOrHusbandName(user.getFatherOrHusbandName());
		this.setBloodGroup(user.getBloodGroup());
		this.setIdentificationMark(user.getIdentificationMark());
		this.setPhoto(user.getPhoto());
		this.setTenantId(user.getTenantId());
	}

	@Builder
	public OwnerInfo(Long id, String uuid, String userName, String salutation, String name,
			String gender, String mobileNumber, String emailId, String altContactNumber, String pan,
			String aadhaarNumber, String permanentAddress, String permanentCity, String permanentPincode,
			String correspondenceCity, String correspondencePincode, String correspondenceAddress, Boolean active,
			String locale, String type, String signature, Boolean accountLocked,
			List<Role> roles, String fatherOrHusbandName, String bloodGroup, String identificationMark, String photo,
			String tenantId,
			String ownerInfoUuid, String mobileNumber2, String gender2, String fatherOrHusbandName2,
			String correspondenceAddress2, Boolean isPrimaryOwner, Double ownerShipPercentage, String ownerType,
			String institutionId) {
		super(id, uuid, userName, salutation, name, gender, mobileNumber, emailId, altContactNumber, pan,
				aadhaarNumber, permanentAddress, permanentCity, permanentPincode, correspondenceCity,
				correspondencePincode, correspondenceAddress, active, locale, type, signature,
				accountLocked, roles, fatherOrHusbandName, bloodGroup, identificationMark, photo, tenantId);
		this.ownerInfoUuid = ownerInfoUuid;
		this.mobileNumber = mobileNumber2;
		gender = gender2;
		fatherOrHusbandName = fatherOrHusbandName2;
		correspondenceAddress = correspondenceAddress2;
		this.isPrimaryOwner = isPrimaryOwner;
		this.ownerShipPercentage = ownerShipPercentage;
		this.ownerType = ownerType;
		this.institutionId = institutionId;
	}

}