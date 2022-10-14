package org.egov.report.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.model.Payment;
import org.egov.report.model.PaymentSearchCriteria;
import org.egov.report.model.enums.PaymentStatusEnum;
import org.egov.report.repository.PaymentRepository;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.repository.builder.ServiceParamBuilder;
import org.egov.report.util.PaymentUtil;
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
	private ObjectMapper mapper;
	
	@Autowired
	private ServiceParamBuilder serviceParamBuilder;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private PaymentUtil paymentUtil;

//	public List<Payment> getPayments(@Valid RequestInfo requestInfo, PaymentSearchCriteria paymentSearchCriteria) {
//		List<Payment> payments = new ArrayList<>();
//		if(!CollectionUtils.isEmpty(paymentSearchCriteria.getBusinessServices())) {
//			for(String businessService : paymentSearchCriteria.getBusinessServices()) {
//				StringBuilder paymentSearchUrlBuilder = new StringBuilder();
//				serviceParamBuilder.createPaymentSearchQuery(paymentSearchUrlBuilder, paymentSearchCriteria, businessService);
//				
//				try {
//					Object response = repository.fetchResult(paymentSearchUrlBuilder, RequestInfoWrapper.builder().requestInfo(requestInfo).build());
//					payments.addAll(mapper.convertValue(response, ExternalApiResponse.class).getPayments());
//				} catch (Exception ex) {
//					log.error("External Service call error", ex);
//					throw new CustomException("PAYMENT_FETCH_EXCEPTION", "Unable to fetch payment information");
//				}
//			}
//		}
//		
//		return payments;
//	}
	
	public List<Payment> getPayments(@Valid RequestInfo requestInfo, PaymentSearchCriteria paymentSearchCriteria) {
		if(paymentSearchCriteria.getFromDate() != null && paymentSearchCriteria.getToDate() != null) {
		paymentSearchCriteria.setFromDate(paymentUtil.enrichFormDate(paymentSearchCriteria.getFromDate()));
		paymentSearchCriteria.setToDate(paymentUtil.enrichToDate(paymentSearchCriteria.getToDate()));
		}
		
		List<Payment> payments = paymentRepository.fetchPayments(paymentSearchCriteria);
		payments = payments.stream().filter(payment -> payment.getPaymentStatus() != PaymentStatusEnum.CANCELLED).collect(Collectors.toList());
		return payments;
	}

	public Long getPaymentsCount(@Valid RequestInfo requestInfo, PaymentSearchCriteria paymentSearchCriteria) {
		
		return paymentRepository.getPaymentsCount(paymentSearchCriteria);
	
	}

}
