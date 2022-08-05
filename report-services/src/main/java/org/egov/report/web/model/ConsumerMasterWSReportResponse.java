package org.egov.report.web.model;

import java.math.BigDecimal;
import java.util.List;

import org.egov.report.web.model.EmployeeDateWiseWSCollectionResponse.EmployeeDateWiseWSCollectionResponseBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConsumerMasterWSReportResponse {
	
	private String ulb;
	
	private String wardNo;
	
	private String connectionNo;
	
	private String oldConnectionNo;
	
	private String connectionType;
	
	private String connectionCategory;
	
	private String usageCategory;
	
	private String connectionFacility;
	
	private Long userId;
	
	private String userName;
	
	private String userMobile;
	
	private String userAddress;
	

}
