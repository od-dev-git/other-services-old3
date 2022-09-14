package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.egov.dss.model.Payment;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.enums.PaymentStatusEnum;
import org.egov.dss.repository.PaymentRepository;
import org.egov.dss.web.model.ChartCriteria;
import org.egov.dss.web.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class RevenueService {
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	private PaymentSearchCriteria getPaymentSearchCriteria(ChartCriteria chartCriteria) {
		PaymentSearchCriteria criteria = new PaymentSearchCriteria();
		
		if(StringUtils.hasText(chartCriteria.getModuleLevel())) {
			criteria.setBusinessServices(Sets.newHashSet(chartCriteria.getModuleLevel()));
		}
		
		if(StringUtils.hasText(chartCriteria.getFilter().getTenantId())) {
			criteria.setTenantIds(Sets.newHashSet(chartCriteria.getFilter().getTenantId()));
		}
		
		if(chartCriteria.getFilter().getStartDate() != null && chartCriteria.getFilter().getStartDate() != 0) {
			criteria.setFromDate(chartCriteria.getFilter().getStartDate());
		}
		
		if(chartCriteria.getFilter().getEndDate() != null && chartCriteria.getFilter().getEndDate() != 0) {
			criteria.setToDate(chartCriteria.getFilter().getEndDate());
		}
		
		return criteria;
	}

	public List<Data> totalCollection(ChartCriteria chartCriteria) {
		
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(chartCriteria);
		List<Payment> payments = paymentRepository.getPayments(paymentSearchCriteria);
		BigDecimal totalCollection = payments.parallelStream()
				.filter(pay -> pay.getPaymentStatus() != PaymentStatusEnum.CANCELLED)
				.filter(pay -> !pay.getTenantId().equalsIgnoreCase("od.testing"))
				.map(pay -> pay.getTotalAmountPaid()).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return Arrays.asList(Data.builder().headerValue(totalCollection.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> todaysCollection(ChartCriteria chartCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> targetCollection(ChartCriteria chartCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> targetAchieved(ChartCriteria chartCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> totalMutationFeeCollection(ChartCriteria chartCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> cumulativeCollection(ChartCriteria chartCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> topPerformingUlbs(ChartCriteria chartCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> bottomPerformingUlbs(ChartCriteria chartCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> collectionByUsageType(ChartCriteria chartCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> demandCollectionIndexDDRRevenue(ChartCriteria chartCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> taxheadsBreakupDDRRevenue(ChartCriteria chartCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
