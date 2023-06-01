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
public class BPA {

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("applicationNo")
	private String applicationNo = null;

	@JsonProperty("approvalNo")
	private String approvalNo = null;

	@JsonProperty("accountId")
	private String accountId = null;

	@JsonProperty("edcrNumber")
	private String edcrNumber = null;

	@JsonProperty("riskType")
	private String riskType = null;

	@JsonProperty("businessService")
	private String businessService = null;

	@JsonProperty("landId")
	private String landId = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("approvalDate")
	private Long approvalDate = null;

	@JsonProperty("applicationDate")
	private Long applicationDate = null;

	@JsonProperty("status")
	private String status = null;

	@JsonProperty("landInfo")
	private LandInfo landInfo = null;

	@JsonProperty("additionalDetails")
	private Object additionalDetails = null;

	@JsonProperty("reWorkHistory")
	private Object reWorkHistory = null;

	@JsonProperty("isRevisionApplication")
	private boolean isRevisionApplication = Boolean.FALSE;

	@JsonProperty("dscDetails")
	@Valid
	private List<DscDetails> dscDetails = null;

	public BPA id(String id) {
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

	public BPA applicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
		return this;
	}

	@Size(min = 1, max = 64)
	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public BPA approvalNo(String approvalNo) {
		this.approvalNo = approvalNo;
		return this;
	}

	@Size(min = 1, max = 64)
	public String getApprovalNo() {
		return approvalNo;
	}

	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}

	public BPA accountId(String accountId) {
		this.accountId = accountId;
		return this;
	}

	@Size(min = 1, max = 64)
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public BPA edcrNumber(String edcrNumber) {
		this.edcrNumber = edcrNumber;
		return this;
	}

	@Size(min = 1, max = 64)
	public String getEdcrNumber() {
		return edcrNumber;
	}

	public void setEdcrNumber(String edcrNumber) {
		this.edcrNumber = edcrNumber;
	}

	public BPA riskType(String riskType) {
		this.riskType = riskType;
		return this;
	}

	@Size(min = 1, max = 64)
	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	public BPA approvalDate(Long approvalDate) {
		this.approvalDate = approvalDate;
		return this;
	}

	public Long getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Long approvalDate) {
		this.approvalDate = approvalDate;
	}

	public BPA applicationDate(Long applicationDate) {
		this.applicationDate = applicationDate;
		return this;
	}

	public Long getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Long applicationDate) {
		this.applicationDate = applicationDate;
	}

	public BPA businessService(String businessService) {
		this.businessService = businessService;
		return this;
	}

	@Size(min = 1, max = 64)
	public String getBusinessService() {
		return businessService;
	}

	public void setBusinessService(String businessService) {
		this.businessService = businessService;
	}

	public BPA landId(String landId) {
		this.landId = landId;
		return this;
	}

	@Size(min = 1, max = 64)
	public String getLandId() {
		return landId;
	}

	public void setLandId(String landId) {
		this.landId = landId;
	}

	public BPA tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	@NotNull

	@Size(min = 2, max = 256)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public BPA status(String status) {
		this.status = status;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BPA landInfo(LandInfo landInfo) {
		this.landInfo = landInfo;
		return this;
	}

	@Valid
	public LandInfo getLandInfo() {
		return landInfo;
	}

	public void setLandInfo(LandInfo landInfo) {
		this.landInfo = landInfo;
	}

	public BPA additionalDetails(Object additionalDetails) {
		this.additionalDetails = additionalDetails;
		return this;
	}

	public BPA reWorkHistory(Object reWorkHistory) {
		this.reWorkHistory = reWorkHistory;
		return this;
	}

	public BPA isRevisionApplication(boolean isRevisionApplication) {
		this.isRevisionApplication = isRevisionApplication;
		return this;
	}

	public Object getAdditionalDetails() {
		return additionalDetails;
	}

	public void setAdditionalDetails(Object additionalDetails) {
		this.additionalDetails = additionalDetails;
	}

	public BPA addDscDetailsItem(DscDetails dscDetailsItem) {
		if (this.dscDetails == null) {
			this.dscDetails = new ArrayList<>();
		}
		if (!this.dscDetails.contains(dscDetailsItem))
			this.dscDetails.add(dscDetailsItem);
		return this;
	}

	public List<DscDetails> getDscDetails() {
		return dscDetails;
	}

	public void setDscDetails(List<DscDetails> dscDetails) {
		this.dscDetails = dscDetails;
	}

	public Object getReWorkHistory() {
		return reWorkHistory;
	}

	public void setReWorkHistory(Object reWorkHistory) {
		this.reWorkHistory = reWorkHistory;
	}

	public boolean getIsRevisionApplication() {
		return isRevisionApplication;
	}

	public void setIsRevisionApplication(boolean isRevisionApplication) {
		this.isRevisionApplication = isRevisionApplication;
	}

}
