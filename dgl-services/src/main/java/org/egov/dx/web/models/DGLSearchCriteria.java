package org.egov.dx.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DGLSearchCriteria {
	
	private String consumerCode;
    
    private String maskedId;

}
