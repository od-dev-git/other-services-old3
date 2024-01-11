package com.tl.csv.trade.type;

import java.util.List;

public class ApplicationDocument{
    public String applicationType;
    public List<String> documentList;
    
	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	public List<String> getDocumentList() {
		return documentList;
	}
	public void setDocumentList(List<String> documentList) {
		this.documentList = documentList;
	}
    
    
}