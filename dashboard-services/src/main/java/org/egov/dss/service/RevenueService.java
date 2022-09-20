package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import org.apache.catalina.mapper.Mapper;
import org.egov.dss.model.Payment;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.UsageTypeResponse;
import org.egov.dss.model.enums.PaymentStatusEnum;
import org.egov.dss.repository.PaymentRepository;
import org.egov.dss.util.DashboardUtils;
import org.egov.dss.web.model.ChartCriteria;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.google.common.collect.Sets;

@Service
public class RevenueService {
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private DashboardUtils dashboardUtils;
	
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
		
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(chartCriteria);
		List<Payment> payments = paymentRepository.getPayments(paymentSearchCriteria);
		BigDecimal todaysCollection = payments.parallelStream()
				.filter(pay -> pay.getPaymentStatus() != PaymentStatusEnum.CANCELLED)
				.filter(pay -> !pay.getTenantId().equalsIgnoreCase("od.testing"))
				.filter(pay -> pay.getTransactionDate() >= dashboardUtils.getStartingTime(System.currentTimeMillis()))
				.filter(pay -> pay.getTransactionDate() <= dashboardUtils.getEndingTime(System.currentTimeMillis()))
				.map(pay -> pay.getTotalAmountPaid()).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return Arrays.asList(Data.builder().headerValue(todaysCollection.setScale(2, RoundingMode.HALF_UP)).build());
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
		
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(chartCriteria);
		paymentSearchCriteria.setBusinessServices(Sets.newHashSet("PT.MUTATION"));
		List<Payment> payments = paymentRepository.getPayments(paymentSearchCriteria);
		BigDecimal totalCollection = payments.parallelStream()
				.filter(pay -> pay.getPaymentStatus() != PaymentStatusEnum.CANCELLED)
				.filter(pay -> !pay.getTenantId().equalsIgnoreCase("od.testing"))
				.filter(pay -> pay.getPaymentDetails().get(0).getBusinessService().equalsIgnoreCase("PT.MUTATION"))
				.map(pay -> pay.getTotalAmountPaid()).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return Arrays.asList(Data.builder().headerValue(totalCollection.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> cumulativeCollection(ChartCriteria chartCriteria) {
		
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(chartCriteria);
		List<Plot> cumulativeMonthCollections = new ArrayList<>();
		BigDecimal addedCumulativeCollection = BigDecimal.ZERO;
		
		
//		Calendar startDay = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//		startDay.setTimeInMillis(paymentSearchCriteria.getFromDate());
//		Calendar lastDay = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//		lastDay.setTimeInMillis(paymentSearchCriteria.getToDate());
//		Calendar endDay = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//		endDay.setTimeInMillis(startDay.getTimeInMillis());
//		endDay.add(Calendar.MONTH, 1);
//		endDay.add(Calendar.SECOND, -1);
		
		
		  Calendar startDay = dashboardUtils.getDayFromLong(paymentSearchCriteria.getFromDate());
		  Calendar lastDay = dashboardUtils.getDayFromLong(paymentSearchCriteria.getToDate());
		  Calendar endDay = dashboardUtils.addOneMonth(startDay.getTimeInMillis());
		
		for(int i = 0; startDay.getTimeInMillis() < lastDay.getTimeInMillis() ; i++) {
		
            paymentSearchCriteria.setFromDate(startDay.getTimeInMillis());
			paymentSearchCriteria.setToDate(endDay.getTimeInMillis());

			Plot cumulativeMonthCollection = new Plot();
			List<Payment> payments = paymentRepository.getPayments(paymentSearchCriteria);
			BigDecimal cumulativeCollection = payments.parallelStream()
					.filter(pay -> pay.getPaymentStatus() != PaymentStatusEnum.CANCELLED)
					.filter(pay -> !pay.getTenantId().equalsIgnoreCase("od.testing"))
					.map(pay -> pay.getTotalAmountPaid()).reduce(BigDecimal.ZERO, BigDecimal::add);	
			
			addedCumulativeCollection=addedCumulativeCollection.add(cumulativeCollection);	
			
			DateFormat dateFormat = new SimpleDateFormat("MMMM-yyyy");  
			String strDate = dateFormat.format(startDay.getTime());
			
			cumulativeMonthCollection.setName(strDate);
			cumulativeMonthCollection.setSymbol("amount");
			cumulativeMonthCollection.setValue(addedCumulativeCollection);
			
			cumulativeMonthCollections.add(cumulativeMonthCollection);

			startDay.add(Calendar.MONTH, 1);
			endDay = dashboardUtils.addOneMonth(startDay.getTimeInMillis());
			
		}
		
		return Arrays.asList(Data.builder().headerValue(addedCumulativeCollection.setScale(2, RoundingMode.HALF_UP)).plots(cumulativeMonthCollections).build());
	}

	public List<Data> topPerformingUlbs(ChartCriteria chartCriteria) {
		
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(chartCriteria);
		List<Payment> payments = paymentRepository.getPayments(paymentSearchCriteria);
		Map<String, List<Payment>> tenantWisePayments = payments.parallelStream()
				.filter(pay -> pay.getPaymentStatus() != PaymentStatusEnum.CANCELLED)
				.filter(pay -> !pay.getTenantId().equalsIgnoreCase("od.testing"))
				.collect(Collectors.groupingBy(Payment::getTenantId));
		
		HashMap<String, BigDecimal> tenantWiseAmountCollection = new HashMap<>();
		tenantWisePayments.forEach((key,value) -> {
			BigDecimal sum = value.stream().collect(Collectors.mapping(Payment::getTotalAmountPaid, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));
			tenantWiseAmountCollection.put(key, sum);
		});
		
		List<Data> responseList = new ArrayList<>();
		
		Map<String, BigDecimal> tenantWiseAmountCollectionSorted = tenantWiseAmountCollection.entrySet().parallelStream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		tenantWiseAmountCollectionSorted.forEach((key,value) -> {
			List<Plot> plots = Arrays.asList(Plot.builder().name(key).value(value).build());
			responseList.add(Data.builder().plots(plots).build());
		});
		
		return responseList;
	}

	public List<Data> bottomPerformingUlbs(ChartCriteria chartCriteria) {
		
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(chartCriteria);
		List<Payment> payments = paymentRepository.getPayments(paymentSearchCriteria);
		Map<String, List<Payment>> tenantWisePayments = payments.parallelStream()
				.filter(pay -> pay.getPaymentStatus() != PaymentStatusEnum.CANCELLED)
				.filter(pay -> !pay.getTenantId().equalsIgnoreCase("od.testing"))
				.collect(Collectors.groupingBy(Payment::getTenantId));
		
		HashMap<String, BigDecimal> tenantWiseAmountCollection = new HashMap<>();
		tenantWisePayments.forEach((key,value) -> {
			BigDecimal sum = value.stream().collect(Collectors.mapping(Payment::getTotalAmountPaid, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));
			tenantWiseAmountCollection.put(key, sum);
		});
		
		List<Data> responseList = new ArrayList<>();
		
		Map<String, BigDecimal> tenantWiseAmountCollectionSorted = tenantWiseAmountCollection.entrySet().parallelStream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		tenantWiseAmountCollectionSorted.forEach((key,value) -> {
			List<Plot> plots = Arrays.asList(Plot.builder().name(key).value(value).build());
			responseList.add(Data.builder().plots(plots).build());
		});
		
		return responseList;
		
	}

	public List<Data> collectionByUsageType(ChartCriteria chartCriteria) {
		
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(chartCriteria);
		List<Payment> payments = paymentRepository.getPayments(paymentSearchCriteria);
		Map<String, List<Payment>> consumerCodeWisePayments = payments.parallelStream()
				.filter(pay -> pay.getPaymentStatus() != PaymentStatusEnum.CANCELLED)
				.filter(pay -> !pay.getTenantId().equalsIgnoreCase("od.testing"))
				.collect(Collectors.groupingBy(pay -> pay.getPaymentDetails().get(0).getBill().getConsumerCode()));
		
		HashMap<String, BigDecimal> consumerCodeWiseAmountPaid = new HashMap<>();
		consumerCodeWisePayments.forEach((key,value) -> {
			BigDecimal sum = value.stream().collect(Collectors.mapping(Payment::getTotalAmountPaid, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));
			consumerCodeWiseAmountPaid.put(key, sum);
		});
			
		List<UsageTypeResponse> usageTypeResponses = paymentRepository.getUsageTypes(paymentSearchCriteria);
		
		usageTypeResponses.parallelStream().forEach(item -> {
			if(consumerCodeWiseAmountPaid.containsKey(item.getConsumerCode())) {
				item.setAmount(consumerCodeWiseAmountPaid.get(item.getConsumerCode()));
			}
		});
		
		 Map<String, List<UsageTypeResponse>> usageCategoryWisePayments= usageTypeResponses.parallelStream()
				 .collect(Collectors.groupingBy(UsageTypeResponse::getUsageCategory));
		 
		 HashMap<String, BigDecimal> usageCategoryWiseAmount = new HashMap<>();
		 usageCategoryWisePayments.forEach((key,value) -> {
				BigDecimal sum = value.stream().collect(Collectors.mapping(UsageTypeResponse::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));
				usageCategoryWiseAmount.put(key, sum);
			});
		 
		 List<Plot> plots = new ArrayList<>();
		 usageCategoryWiseAmount.forEach((key, value) -> {
	     	Plot plot = Plot.builder().name(key).value(value).build();
	     	plots.add(plot);
			});
		
		return Arrays.asList(Data.builder().plots(plots).build()) ;
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
