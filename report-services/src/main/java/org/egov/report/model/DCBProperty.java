package org.egov.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DCBProperty {
	
	private String propertyId;
	
	private String oldPropertyId;
	
	private String legacyHoldingNo;
	
	private String ward ;

}
