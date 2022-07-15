package org.egov.report.service;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.ExternalApiResponse;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.web.model.IncentiveReportCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentService {
	
	@Autowired
	private ServiceRepository repository;
	
	@Autowired
	private ReportServiceConfiguration config;
	
	@Autowired
	private ObjectMapper mapper;

	public ExternalApiResponse getPayments(@Valid RequestInfo requestInfo, IncentiveReportCriteria incentiveReportCriteria) {
		// https://sujog-dev.odisha.gov.in/collection-services/payments/WS/_search?tenantId=od.cuttack&fromDate=1622400707000&toDate=1654019507000
		StringBuilder paymentSearchUrlBuilder = new StringBuilder();
		paymentSearchUrlBuilder = paymentSearchUrlBuilder.append(config.getCollectionHost())
				.append(config.getCollectionSearchEndPoint().replace("{module}", incentiveReportCriteria.getModule()))
				.append("?tenantId=").append(incentiveReportCriteria.getTenantId())
				.append("&fromDate=").append(incentiveReportCriteria.getFromDate())
				.append("&toDate=").append(incentiveReportCriteria.getToDate());
		
		try {
			Object response = repository.fetchResult(paymentSearchUrlBuilder, requestInfo);
			return mapper.convertValue(response, ExternalApiResponse.class);
		} catch (Exception ex) {
			log.error("External Service call error", ex);
			throw new CustomException("PAYMENT_FETCH_EXCEPTION", "Unable to fetch payment information");
		}
	}

}
