package org.egov.report.service;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.model.ExternalApiResponse;
import org.egov.report.model.Payment;
import org.egov.report.model.PaymentSearchCriteria;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.repository.builder.ServiceParamBuilder;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentService {
	
	@Autowired
	private ServiceRepository repository;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ServiceParamBuilder serviceParamBuilder;

	public List<Payment> getPayments(@Valid RequestInfo requestInfo, PaymentSearchCriteria paymentSearchCriteria) {
		List<Payment> payments = new ArrayList<>();
		if(!CollectionUtils.isEmpty(paymentSearchCriteria.getBusinessServices())) {
			for(String businessService : paymentSearchCriteria.getBusinessServices()) {
				StringBuilder paymentSearchUrlBuilder = new StringBuilder();
				serviceParamBuilder.createPaymentSearchQuery(paymentSearchUrlBuilder, paymentSearchCriteria, businessService);
				
				try {
					Object response = repository.fetchResult(paymentSearchUrlBuilder, RequestInfoWrapper.builder().requestInfo(requestInfo).build());
					payments.addAll(mapper.convertValue(response, ExternalApiResponse.class).getPayments());
				} catch (Exception ex) {
					log.error("External Service call error", ex);
					throw new CustomException("PAYMENT_FETCH_EXCEPTION", "Unable to fetch payment information");
				}
			}
		}
		
		return payments;
	}
	

}
