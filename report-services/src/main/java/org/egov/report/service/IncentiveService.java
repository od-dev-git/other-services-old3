package org.egov.report.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.model.ExternalApiResponse;
import org.egov.report.model.IncentiveAnalysis;
import org.egov.report.model.Payment;
import org.egov.report.util.ReportConstants;
import org.egov.report.validator.ReportValidator;
import org.egov.report.web.model.IncentiveReportCriteria;
import org.egov.report.web.model.IncentiveResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncentiveService {
	
	@Autowired
	private ReportValidator reportValidator;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private CalculatorService calculatorService;

	public IncentiveResponse getIncentiveReport(@Valid RequestInfo requestInfo,
			IncentiveReportCriteria incentiveReportCriteria) {
		
		reportValidator.validateIncentiveCriteria(incentiveReportCriteria);
		ExternalApiResponse paymentApiResponse = paymentService.getPayments(requestInfo, incentiveReportCriteria);
		List<Payment> payments = paymentApiResponse.getPayments();
		
		Map<String, IncentiveAnalysis> incentiveAnalysis = prepareCollectionReport(payments);
		calculatorService.calculateIncentives(incentiveReportCriteria.getModule(), incentiveAnalysis);
		
		return IncentiveResponse.builder().incentiveAnalysis(incentiveAnalysis.values().stream().collect(Collectors.toList())).build();
	}

	

	private Map<String, IncentiveAnalysis> prepareCollectionReport(List<Payment> payments) {
		Map<String, IncentiveAnalysis> incentiveReport = new HashMap<>();
		
		for (Payment payment : payments) {
			IncentiveAnalysis incentive = incentiveReport.get(payment.getAuditDetails().getCreatedBy());
			if(incentive == null) {
				incentive = IncentiveAnalysis.builder().empId(payment.getAuditDetails().getCreatedBy()).build();
			}
			incentive.setTotalNoOfTransaction(incentive.getTotalNoOfTransaction()+1);
			incentive.setTotalCollection(incentive.getTotalCollection().add(payment.getTotalAmountPaid()));
		}
		
		return incentiveReport;
	}

}
