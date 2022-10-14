package org.egov.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.BillDetail;
import org.egov.report.model.IncentiveAnalysis;
import org.egov.report.model.Payment;
import org.egov.report.model.PaymentDetail;
import org.egov.report.model.PaymentSearchCriteria;
import org.egov.report.util.ReportConstants;
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
	
	@Autowired
	private ReportServiceConfiguration configuration;
	
	public IncentiveResponse getIncentiveReport(@Valid RequestInfo requestInfo,
			IncentiveReportCriteria incentiveReportCriteria) {
		
		reportValidator.validateIncentiveCriteria(incentiveReportCriteria);
		
		
		PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder()
				.businessServices(Stream.of(incentiveReportCriteria.getModule()).collect(Collectors.toSet()))
				.tenantId(incentiveReportCriteria.getTenantId())
				.fromDate(incentiveReportCriteria.getFromDate())
				.toDate(incentiveReportCriteria.getToDate()).build();
		
		Long count = paymentService.getPaymentsCount(requestInfo, paymentSearchCriteria);
		
		
		Integer limit = configuration.getReportLimit();
		Integer offset = 0;
		Map<String, IncentiveAnalysis> incentiveAnalysis  = new HashMap<>();
		if(count > 0) {
			while(count > 0) {
				paymentSearchCriteria.setOffset(offset);
				paymentSearchCriteria.setLimit(limit);
				List<Payment> tempPayments = paymentService.getPayments(requestInfo, paymentSearchCriteria);
				prepareCollectionReport(tempPayments, incentiveAnalysis);
				count = count - limit;
				offset += limit;
			}
		}
		
		calculatorService.calculateIncentives(incentiveReportCriteria.getModule(), incentiveAnalysis);
		
		List<Long> userIds = incentiveAnalysis.keySet().stream().map(key -> Long.valueOf(key)).collect(Collectors.toList());
		List<OwnerInfo> usersInfo =userService.getUser(requestInfo, userIds);
		usersInfo = usersInfo.stream().filter(user -> user.getRoles().stream().map(role -> role.getCode()).collect(Collectors.toList()).contains(ReportConstants.ROLE_JALSATHI)).collect(Collectors.toList());
		enrichUserData(incentiveAnalysis, usersInfo);
		
		return IncentiveResponse.builder().incentiveAnalysis(incentiveAnalysis.values().stream()
				.filter(ia -> ia.getIsJalsathiCollection()).collect(Collectors.toList())).build();
	}

	

	private void enrichUserData(Map<String, IncentiveAnalysis> incentiveAnalysisMap,
			List<OwnerInfo> users) {
		for (OwnerInfo user : users) {
			IncentiveAnalysis incentiveAnalysis = incentiveAnalysisMap.get(user.getId().toString());
			incentiveAnalysis.setEmpName(user.getName());
			incentiveAnalysis.setEmpId(user.getUserName());
			incentiveAnalysis.setIsJalsathiCollection(Boolean.TRUE);
		}
	}

	private void prepareCollectionReport(List<Payment> payments, Map<String, IncentiveAnalysis> incentiveAnalysis) {
		
		
		for (Payment payment : payments) {
			IncentiveAnalysis incentive = incentiveAnalysis.get(payment.getAuditDetails().getCreatedBy());
			if(incentive == null) {
				incentive = IncentiveAnalysis.builder().empId(payment.getAuditDetails().getCreatedBy()).build();
				incentiveAnalysis.put(payment.getAuditDetails().getCreatedBy(), incentive);
			}
			incentive.setTotalNoOfTransaction(incentive.getTotalNoOfTransaction()+1);
			incentive.setTotalCollection(incentive.getTotalCollection().add(payment.getTotalAmountPaid()));
			
			BigDecimal arrearCollected = getColletdArrearAmount(payment);
			incentive.setCollectionTowardsArrear(incentive.getCollectionTowardsArrear().add(arrearCollected));
			incentive.setCollectionTowardsCurrent(incentive.getCollectionTowardsCurrent().add(payment.getTotalAmountPaid().subtract(arrearCollected)));
		}
	}



	private BigDecimal getColletdArrearAmount(Payment payment) {
		
		Comparator<BillDetail> billDetailComparator = (obj1, obj2) -> obj2.getFromPeriod().compareTo(obj1.getFromPeriod());
		BigDecimal arrearCollected = BigDecimal.ZERO;
		
		for (PaymentDetail pd : payment.getPaymentDetails()) {
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
