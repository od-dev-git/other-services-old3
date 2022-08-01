package org.egov.report.repository.builder;

import java.util.stream.Collectors;

import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.PaymentSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class ServiceParamBuilder {
	
	@Autowired
	private ReportServiceConfiguration config;

	public void createPaymentSearchQuery(StringBuilder paymentSearchUrlBuilder,
			PaymentSearchCriteria paymentSearchCriteria, String businessService) {
		paymentSearchUrlBuilder = paymentSearchUrlBuilder.append(config.getCollectionHost())
				.append(config.getCollectionSearchEndPoint().replace("{module}", businessService))
				.append("?tenantId=").append(paymentSearchCriteria.getTenantId());
		
		if(paymentSearchCriteria.getFromDate()!=null && paymentSearchCriteria.getFromDate()!=0) {
			paymentSearchUrlBuilder.append("&fromDate=").append(paymentSearchCriteria.getFromDate());
		}
		
		if(paymentSearchCriteria.getToDate()!=null && paymentSearchCriteria.getToDate()!=0) {
			paymentSearchUrlBuilder.append("&toDate=").append(paymentSearchCriteria.getToDate());
		}
				
		if(!CollectionUtils.isEmpty(paymentSearchCriteria.getPaymentModes())) {
			paymentSearchUrlBuilder.append("&paymentModes=").append(paymentSearchCriteria.getPaymentModes().stream().collect(Collectors.joining(",")));
		}
		
		if(!CollectionUtils.isEmpty(paymentSearchCriteria.getIds())) {
			paymentSearchUrlBuilder.append("&ids=").append(paymentSearchCriteria.getPaymentModes().stream().collect(Collectors.joining(",")));
		}
	}

}
