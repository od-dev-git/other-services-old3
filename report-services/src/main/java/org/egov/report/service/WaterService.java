package org.egov.report.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.model.Payment;
import org.egov.report.model.PaymentSearchCriteria;
import org.egov.report.validator.WSReportValidator;
import org.egov.report.web.model.EmployeeDateWiseWSCollectionResponse;
import org.egov.report.web.model.OwnerInfo;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaterService {
	
	@Autowired
	private WSReportValidator wsValidator;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private UserService userService;

	public List<EmployeeDateWiseWSCollectionResponse> employeeDateWiseWSCollection(RequestInfo requestInfo, WSReportSearchCriteria searchCriteria) {
		
		wsValidator.validateEmployeeDateWiseWSCollectionReport(searchCriteria);
		
		PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder()
				.businessServices(Stream.of("WS","WS.ONE_TIME_FEE").collect(Collectors.toSet()))
//				.businessServices("WS")
				.paymentModes(Stream.of(searchCriteria.getPaymentMode()).collect(Collectors.toSet()))
				.tenantId(searchCriteria.getTenantId())
				.fromDate(searchCriteria.getCollectionDate())
				.toDate(searchCriteria.getCollectionDate()).build();
		
		List<Payment> payments = paymentService.getPayments(requestInfo, paymentSearchCriteria);
		if(payments.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<EmployeeDateWiseWSCollectionResponse> response = payments.stream()
				.map(payment -> EmployeeDateWiseWSCollectionResponse.builder()
						.employeeId(payment.getAuditDetails().getCreatedBy())
						.tenantId(payment.getTenantId()).ulb(payment.getTenantId().split("\\.")[1].toUpperCase())
						.transactionDate(payment.getTransactionDate()).paymentMode(payment.getPaymentMode().toString())
						.consumerCode(payment.getPaymentDetails().get(0).getBill().getConsumerCode())
						.receiptNo(payment.getPaymentDetails().get(0).getReceiptNumber())
						.businessService(payment.getPaymentDetails().get(0).getBusinessService())
						.head("Water")
						.collectedAmount(payment.getTotalAmountPaid()).build())
				.collect(Collectors.toList());
		
		List<Long> userIds = response.stream().map(item -> Long.valueOf(item.getEmployeeId())).distinct().collect(Collectors.toList());
		List<OwnerInfo> usersInfo = userService.getUser(requestInfo, userIds);
		Map<Long, OwnerInfo> userMap = usersInfo.stream().collect(Collectors.toMap(OwnerInfo::getId, Function.identity()));
		
		response.stream().forEach(item -> {
			OwnerInfo user = userMap.get(Long.valueOf(item.getEmployeeId()));
			if(user!=null) {
				item.setEmployeeId(user.getUserName());
				item.setEmployeeName(user.getName());
			}
		});
		
		return response;
	}

}
