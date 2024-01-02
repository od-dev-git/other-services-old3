package com.tl.billing.slab;

public class BillingSlab {
	private String licenseType;
	private String uom;
	private String applicationType;
	private Float rate;
	private String tenantId;
	private Float fromUom;
	private Type type;
	private String tradeType;
	private Float toUom;
	
	
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
	public Float getRate() {
		return rate;
	}
	public void setRate(Float rate) {
		this.rate = rate;
	}
	public Float getFromUom() {
		return fromUom;
	}
	public void setFromUom(Float fromUom) {
		this.fromUom = fromUom;
	}
	public Float getToUom() {
		return toUom;
	}
	public void setToUom(Float toUom) {
		this.toUom = toUom;
	}
	@Override
	public String toString() {
		return "BillingSlab [licenseType=" + licenseType + ", uom=" + uom + ", applicationType=" + applicationType
				+ ", rate=" + rate + ", tenantId=" + tenantId + ", fromUom=" + fromUom + ", type=" + type
				+ ", tradeType=" + tradeType + ", toUom=" + toUom + "]";
	}
	
	
	
	

}
