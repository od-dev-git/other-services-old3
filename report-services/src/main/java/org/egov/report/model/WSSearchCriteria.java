package org.egov.report.model;

import java.util.List;
import java.util.Set;

import org.egov.report.model.PaymentSearchCriteria.PaymentSearchCriteriaBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WSSearchCriteria {

	private String consumerNo;
	
	private String oldConsumerNo;
	
	private String ownerMobileNo;
	
	private String tenantId;
	
	private String searchType;
}
