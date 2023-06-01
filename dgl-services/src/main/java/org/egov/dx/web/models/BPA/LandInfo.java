package org.egov.dx.web.models.BPA;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Validated
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandInfo {

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("landUId")
	private String landUId = null;

	@JsonProperty("landUniqueRegNo")
	private String landUniqueRegNo = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("status")
	private Status status = null;

	@JsonProperty("address")
	private Address address = null;

	@JsonProperty("ownershipCategory")
	private String ownershipCategory = null;

	@JsonProperty("owners")
	@Valid
	private List<OwnerInfo> owners = new ArrayList<OwnerInfo>();

	@JsonProperty("additionalDetails")
	private Object additionalDetails = null;

	public LandInfo id(String id) {
		this.id = id;
		return this;
	}

	@Size(min = 1, max = 64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LandInfo landUId(String landUId) {
		this.landUId = landUId;
		return this;
	}

	@Size(min = 1, max = 64)
	public String getLandUId() {
		return landUId;
	}

	public void setLandUId(String landUId) {
		this.landUId = landUId;
	}

	public LandInfo landUniqueRegNo(String landUniqueRegNo) {
		this.landUniqueRegNo = landUniqueRegNo;
		return this;
	}

	@Size(min = 1, max = 64)
	public String getLandUniqueRegNo() {
		return landUniqueRegNo;
	}

	public void setLandUniqueRegNo(String landUniqueRegNo) {
		this.landUniqueRegNo = landUniqueRegNo;
	}

	public LandInfo tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * tenant id of the Property
	 * 
	 * @return tenantId
	 **/

	@NotNull

	@Size(min = 2, max = 256)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public LandInfo status(Status status) {
		this.status = status;
		return this;
	}

	@Valid
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LandInfo address(Address address) {
		this.address = address;
		return this;
	}

	@NotNull

	@Valid
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public LandInfo ownershipCategory(String ownershipCategory) {
		this.ownershipCategory = ownershipCategory;
		return this;
	}

	@Size(max = 64)
	public String getOwnershipCategory() {
		return ownershipCategory;
	}

	public void setOwnershipCategory(String ownershipCategory) {
		this.ownershipCategory = ownershipCategory;
	}

	public LandInfo owners(List<OwnerInfo> owners) {
		this.owners = owners;
		return this;
	}

	public LandInfo addOwnersItem(OwnerInfo ownersItem) {
		this.owners.add(ownersItem);
		return this;
	}

	@NotNull
	@Valid
	public List<OwnerInfo> getOwners() {
		return owners;
	}

	public void setOwners(List<OwnerInfo> owners) {
		this.owners = owners;
	}

	public LandInfo additionalDetails(Object additionalDetails) {
		this.additionalDetails = additionalDetails;
		return this;
	}

	public Object getAdditionalDetails() {
		return additionalDetails;
	}

	public void setAdditionalDetails(Object additionalDetails) {
		this.additionalDetails = additionalDetails;
	}
}
