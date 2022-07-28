package org.egov.report.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.model.BillDetail;
import org.egov.report.model.ExternalApiResponse;
import org.egov.report.model.IncentiveAnalysis;
import org.egov.report.model.Payment;
import org.egov.report.model.PaymentDetails;
import org.egov.report.validator.ReportValidator;
import org.egov.report.web.model.IncentiveReportCriteria;
import org.egov.report.web.model.IncentiveResponse;
import org.egov.report.web.model.OwnerInfo;
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
	
	@Autowired
	private UserService userService;
	
	public IncentiveResponse getIncentiveReport(@Valid RequestInfo requestInfo,
			IncentiveReportCriteria incentiveReportCriteria) {
		
		reportValidator.validateIncentiveCriteria(incentiveReportCriteria);
		ExternalApiResponse paymentApiResponse = paymentService.getPayments(requestInfo, incentiveReportCriteria);
		List<Payment> payments = paymentApiResponse.getPayments();
		if(payments.isEmpty()) {
			return IncentiveResponse.builder().incentiveAnalysis(Collections.EMPTY_LIST).build();
		}
		
		Map<String, IncentiveAnalysis> incentiveAnalysis = prepareCollectionReport(payments);
		
		calculatorService.calculateIncentives(incentiveReportCriteria.getModule(), incentiveAnalysis);
		
		List<Long> userIds = incentiveAnalysis.keySet().stream().map(key -> Long.valueOf(key)).collect(Collectors.toList());
		List<OwnerInfo> usersInfo =userService.getUser(requestInfo, userIds);
		enrichUserData(incentiveAnalysis, usersInfo);
		
		return IncentiveResponse.builder().incentiveAnalysis(incentiveAnalysis.values().stream().collect(Collectors.toList())).build();
	}

	

	private void enrichUserData(Map<String, IncentiveAnalysis> incentiveAnalysisMap,
			List<OwnerInfo> users) {
		for (OwnerInfo user : users) {
			IncentiveAnalysis incentiveAnalysis = incentiveAnalysisMap.get(user.getId().toString());
			incentiveAnalysis.setEmpName(user.getName());
			incentiveAnalysis.setEmpId(user.getUserName());
		}
	}

	private Map<String, IncentiveAnalysis> prepareCollectionReport(List<Payment> payments) {
		Map<String, IncentiveAnalysis> incentiveReport = new HashMap<>();
		
		for (Payment payment : payments) {
			IncentiveAnalysis incentive = incentiveReport.get(payment.getAuditDetails().getCreatedBy());
			if(incentive == null) {
				incentive = IncentiveAnalysis.builder().empId(payment.getAuditDetails().getCreatedBy()).build();
				incentiveReport.put(payment.getAuditDetails().getCreatedBy(), incentive);
			}
			incentive.setTotalNoOfTransaction(incentive.getTotalNoOfTransaction()+1);
			incentive.setTotalCollection(incentive.getTotalCollection().add(payment.getTotalAmountPaid()));
			
			BigDecimal arrearCollected = getColletdArrearAmount(payment);
			incentive.setCollectionTowardsArrear(incentive.getCollectionTowardsArrear().add(arrearCollected));
			incentive.setCollectionTowardsCurrent(incentive.getCollectionTowardsCurrent().add(payment.getTotalAmountPaid().subtract(arrearCollected)));
		}
		
		return incentiveReport;
	}



	private BigDecimal getColletdArrearAmount(Payment payment) {
		
		Comparator<BillDetail> billDetailComparator = (obj1, obj2) -> obj2.getFromPeriod().compareTo(obj1.getFromPeriod());
		BigDecimal arrearCollected = BigDecimal.ZERO;
		
		for (PaymentDetails pd : payment.getPaymentDetails()) {
			Collections.sort(pd.getBill().getBillDetails(), billDetailComparator);
			for(int i=0; i<pd.getBill().getBillDetails().size(); i++) {
				if(i != 0) {
					arrearCollected = arrearCollected.add(pd.getBill().getBillDetails().get(i).getAmountPaid());
				}
			}
		}
		
		return arrearCollected;
	}

}
