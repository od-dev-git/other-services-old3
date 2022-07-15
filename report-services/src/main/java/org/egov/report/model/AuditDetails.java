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
public class AuditDetails {
	
	private String createdBy;
	
	private Long createdTime;
    
	private String lastModifiedBy;
    
	private Long lastModifiedTime;
}
