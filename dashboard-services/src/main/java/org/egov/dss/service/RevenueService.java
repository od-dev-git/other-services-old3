package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.activation.DataContentHandler;
import javax.xml.datatype.DatatypeConstants;

import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.BillAccountDetail;
import org.egov.dss.model.Chart;
import org.egov.dss.model.DemandSearchCriteria;
import org.egov.dss.model.FinancialYearWiseProperty;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.Payment;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.model.TargetSearchCriteria;
import org.egov.dss.model.UsageTypeResponse;
import org.egov.dss.model.enums.PaymentStatusEnum;
import org.egov.dss.repository.PaymentRepository;
import org.egov.dss.util.Constants;
import org.egov.dss.util.DashboardUtils;
import org.egov.dss.web.model.ChartCriteria;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.egov.dss.web.model.PlotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.com.lmax.disruptor.LiteBlockingWaitStrategy;

@Service
@Slf4j
public class RevenueService {
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private DashboardUtils dashboardUtils;
	
	private PaymentSearchCriteria getTotalCollectionPaymentSearchCriteria(PayloadDetails payloadDetails) {
		PaymentSearchCriteria criteria = new PaymentSearchCriteria();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.BS_HOME_REVENUE))				
				criteria.setBusinessServices(null);
			else if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_OBPS))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_REVENUE_ALL_BS));
			else if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_PT))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.PT_REVENUE_ALL_BS));
			else if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.BUSINESS_SERVICE_WS))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.WS_REVENUE_ALL_BS));
			else if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_REGULARIZATION))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.REGULARIZATION_REVENUE_ALL_BS));
			else
				criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
		}
		
		if(StringUtils.hasText(payloadDetails.getTenantid())) {
			criteria.setTenantIds(Sets.newHashSet(payloadDetails.getTenantid()));
		}
		
		if(payloadDetails.getStartdate() != null && payloadDetails.getStartdate() != 0) {
			criteria.setFromDate(payloadDetails.getStartdate());
		}
		
		if(payloadDetails.getEnddate() != null && payloadDetails.getEnddate() != 0) {
			criteria.setToDate(payloadDetails.getEnddate());
		}
		
		return criteria;
	}
	
	private DemandSearchCriteria getDemandSearchCriteria(PayloadDetails payloadDetails) {
		DemandSearchCriteria criteria = new DemandSearchCriteria();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			criteria.setBusinessService(payloadDetails.getModulelevel());
		}
		
		if(StringUtils.hasText(payloadDetails.getTenantid())) {
			criteria.setTenantId(payloadDetails.getTenantid());
		}
		
		if(StringUtils.hasText(payloadDetails.getTimeinterval())) {
			criteria.setFinancialYear(payloadDetails.getTimeinterval());
		}
		
		return criteria;
	}

	public List<Data> totalCollection(PayloadDetails payloadDetails) {
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		//List<Payment> payments = paymentRepository.getPayments(paymentSearchCriteria);
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
        return Arrays.asList(Data.builder().headerValue(totalCollection.setScale(2, RoundingMode.HALF_UP)).build());
	}
	
	public List<Data> currentCollection(PayloadDetails payloadDetails) {
        if(!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		setFromAndToDateInIST(paymentSearchCriteria);
		BigDecimal totalCollection = (BigDecimal) paymentRepository.getCurrentCollection(paymentSearchCriteria);
		if(totalCollection == null) {
			totalCollection = BigDecimal.ZERO;
		}
        return Arrays.asList(Data.builder().headerValue(totalCollection.setScale(2, RoundingMode.HALF_UP)).build());
        }
        
        return Arrays.asList(Data.builder().headerValue("NA").build());

	}

	private void setFromAndToDateInIST(PaymentSearchCriteria paymentSearchCriteria) {
		Long startTimeMillis = paymentSearchCriteria.getFromDate();
		
		ZonedDateTime startIST = Instant.ofEpochMilli(startTimeMillis).atZone(ZoneId.of("Asia/Kolkata"));		
		ZonedDateTime endIST = startIST.plusYears(1).minusNanos(1);
		
		Long endMillisIST = endIST.toInstant().toEpochMilli();
		log.info("Start TIme  : " + startTimeMillis.toString());
		log.info("End TIme  : " + endMillisIST.toString());
		
		paymentSearchCriteria.setToDate(endMillisIST);
	}
	
	public List<Data> arrearCollection(PayloadDetails payloadDetails) {
        if(!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		setFromAndToDateInIST(paymentSearchCriteria);
		BigDecimal totalCollection = (BigDecimal) paymentRepository.getArrearCollection(paymentSearchCriteria);
		if(totalCollection == null) {
			totalCollection = BigDecimal.ZERO;
		}
        return Arrays.asList(Data.builder().headerValue(totalCollection.setScale(2, RoundingMode.HALF_UP)).build());
        }
        return Arrays.asList(Data.builder().headerValue("NA").build());

	}

	public List<Data> todaysCollection(PayloadDetails payloadDetails) {
		Date date = new Date();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date .toInstant(), ZoneId.systemDefault());
	    ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		paymentSearchCriteria.setToDate(zdt.toEpochSecond());
		paymentSearchCriteria.setFromDate(zdt.minusDays(1).toEpochSecond());
		BigDecimal totalCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
        return Arrays.asList(Data.builder().headerValue(totalCollection.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> targetCollection(PayloadDetails payloadDetails) {
		String temp = payloadDetails.getTimeinterval();
		if(Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			payloadDetails.setTimeinterval(dashboardUtils.getCurrentFinancialYear());
		}
		TargetSearchCriteria targerSearchCriteria = getTargetSearchCriteria(payloadDetails);
		BigDecimal targetCollection = (BigDecimal) paymentRepository.getTtargetCollection(targerSearchCriteria);
		if(temp.equalsIgnoreCase(DashboardConstants.QUARTER)) {
			targetCollection = targetCollection.divide(new BigDecimal(4), 2, RoundingMode.HALF_UP);
		}else if(temp.equalsIgnoreCase(DashboardConstants.MONTH)) {
			targetCollection = targetCollection.divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);
		}
		else if(temp.equalsIgnoreCase(DashboardConstants.WEEK)) {
			targetCollection = targetCollection.divide(new BigDecimal(48), 2, RoundingMode.HALF_UP);
		}
		else if(temp.equalsIgnoreCase(DashboardConstants.DAY)) {
			targetCollection = targetCollection.divide(new BigDecimal(365), 2, RoundingMode.HALF_UP);
		}
		return Arrays.asList(Data.builder().headerValue(targetCollection.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> targetAchieved(PayloadDetails payloadDetails) {
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
		TargetSearchCriteria targerSearchCriteria = getTargetSearchCriteria(payloadDetails);
		BigDecimal targetCollection = (BigDecimal) targetCollection(payloadDetails).get(0).getHeaderValue();
		
		//BigDecimal targetAchieved = totalCollection.multiply(new BigDecimal(100)).divide(targetCollection);
		if(targetCollection.equals(BigDecimal.ZERO))
			targetCollection = BigDecimal.ONE;			
		BigDecimal targetAchieved = totalCollection.multiply(new BigDecimal(100)).divide(targetCollection, 2, RoundingMode.HALF_UP);
		return Arrays.asList(Data.builder().headerValue(targetAchieved.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> totalMutationFeeCollection(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setBusinessServices(Sets.newHashSet("PT.MUTATION"));
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
		return Arrays.asList(Data.builder().headerValue(totalCollection.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> cumulativeCollection(PayloadDetails payloadDetails) {
		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		if (!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			criteria.setFromDate(dashboardUtils.getStartDateGmt(String.valueOf(payloadDetails.getTimeinterval())));
		}
		List<Chart> cumulativeCollection = paymentRepository.getCumulativeCollection(criteria);
		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(cumulativeCollection, plots);
        
		BigDecimal total = cumulativeCollection.stream().map(usageCategory -> usageCategory.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);		 

		return Arrays.asList(Data.builder().headerName("Collections").headerSymbol("amount").headerValue(total).plots(plots).build());
	}

	public List<Data> topPerformingUlbs(PayloadDetails payloadDetails) {

		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		String tempTimeInterval = payloadDetails.getTimeinterval();
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		if(Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			payloadDetails.setTimeinterval(dashboardUtils.getCurrentFinancialYear());
		}
		TargetSearchCriteria targerSearchCriteria = getTargetSearchCriteria(payloadDetails);
		HashMap<String, BigDecimal> tenantWiseTarget = paymentRepository
				.getTenantWiseTargetCollection(targerSearchCriteria);
		if (DashboardConstants.TIME_INTERVAL.contains(tempTimeInterval)) {
			for (Map.Entry<String, BigDecimal> tenantTarget : tenantWiseTarget.entrySet()) {
				BigDecimal targetCollection = BigDecimal.ZERO;
				if (tempTimeInterval.equalsIgnoreCase(DashboardConstants.QUARTER))
					targetCollection = tenantTarget.getValue().divide(new BigDecimal(4), 2, RoundingMode.HALF_UP);
				if (tempTimeInterval.equalsIgnoreCase(DashboardConstants.MONTH))
					targetCollection = tenantTarget.getValue().divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);
				if (tempTimeInterval.equalsIgnoreCase(DashboardConstants.WEEK))
					targetCollection = tenantTarget.getValue().divide(new BigDecimal(48), 2, RoundingMode.HALF_UP);
				if (tempTimeInterval.equalsIgnoreCase(DashboardConstants.DAY))
					targetCollection = tenantTarget.getValue().divide(new BigDecimal(365), 2, RoundingMode.HALF_UP);
				tenantWiseTarget.put(tenantTarget.getKey(), targetCollection);
			}
		}

		HashMap<String, BigDecimal> tenantWisePercentage = new HashMap<>();

		tenantWiseCollection.forEach((key, value) -> {
			BigDecimal target = BigDecimal.ONE;
			if(tenantWiseTarget.containsKey(key))
				target = tenantWiseTarget.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(target, 2, RoundingMode.HALF_UP);
			tenantWisePercentage.put(key, percentage);
		});

		Map<String, BigDecimal> tenantWisePercentageSorted = tenantWisePercentage.entrySet().parallelStream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		List<Data> responseList = new ArrayList<>();
		
		int rank = 0;
		for(Map.Entry<String, BigDecimal> tenantWisePercent : tenantWisePercentageSorted.entrySet()) {
			rank++;
			List<Plot> plots = Arrays.asList(Plot.builder().name(tenantWisePercent.getKey()).value(tenantWisePercent.getValue()).symbol("percentage")
					.label("DSS_TARGET_ACHIEVED").build());
			responseList.add(Data.builder().plots(plots).headerValue(rank).build());
		}

		return responseList;

	}

	public List<Data> bottomPerformingUlbs(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		String tempTimeInterval = payloadDetails.getTimeinterval();
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		
		if(Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			payloadDetails.setTimeinterval(dashboardUtils.getCurrentFinancialYear());
		}

		TargetSearchCriteria targerSearchCriteria = getTargetSearchCriteria(payloadDetails);
		HashMap<String, BigDecimal> tenantWiseTarget = paymentRepository
				.getTenantWiseTargetCollection(targerSearchCriteria);
		
		if (DashboardConstants.TIME_INTERVAL.contains(tempTimeInterval)) {
			for (Map.Entry<String, BigDecimal> tenantTarget : tenantWiseTarget.entrySet()) {
				BigDecimal targetCollection = BigDecimal.ZERO;
				if (tempTimeInterval.equalsIgnoreCase(DashboardConstants.QUARTER))
					targetCollection = tenantTarget.getValue().divide(new BigDecimal(4), 2, RoundingMode.HALF_UP);
				if (tempTimeInterval.equalsIgnoreCase(DashboardConstants.MONTH))
					targetCollection = tenantTarget.getValue().divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);
				if (tempTimeInterval.equalsIgnoreCase(DashboardConstants.WEEK))
					targetCollection = tenantTarget.getValue().divide(new BigDecimal(48), 2, RoundingMode.HALF_UP);
				if (tempTimeInterval.equalsIgnoreCase(DashboardConstants.DAY))
					targetCollection = tenantTarget.getValue().divide(new BigDecimal(365), 2, RoundingMode.HALF_UP);
				tenantWiseTarget.put(tenantTarget.getKey(), targetCollection);
			}
		}

		HashMap<String, BigDecimal> tenantWisePercentage = new HashMap<>();

		tenantWiseCollection.forEach((key, value) -> {
			BigDecimal target = BigDecimal.ONE;
			if(tenantWiseTarget.containsKey(key))
				target = tenantWiseTarget.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(target, 2, RoundingMode.HALF_UP);
			tenantWisePercentage.put(key, percentage);
		});

		Map<String, BigDecimal> tenantWisePercentageSorted = tenantWisePercentage.entrySet().parallelStream()
				.sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		List<Data> responseList = new ArrayList<>();
		
		int rank = tenantWisePercentageSorted.size()+1;
		for(Map.Entry<String, BigDecimal> tenantWisePercent : tenantWisePercentageSorted.entrySet()) {
			rank--;
			List<Plot> plots = Arrays.asList(Plot.builder().name(tenantWisePercent.getKey()).value(tenantWisePercent.getValue()).symbol("percentage")
					.label("DSS_TARGET_ACHIEVED").build());
			responseList.add(Data.builder().plots(plots).headerValue(rank).build());
		}

		return responseList;
		
	}

	public List<Data> collectionByUsageType(PayloadDetails payloadDetails) {
		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		criteria.setPropertyStatus(DashboardConstants.STATUS_ACTIVE);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));

		List<Chart> collectionByUsageType = paymentRepository.getCollectionByUsageType(criteria);

		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(collectionByUsageType, plots);

		BigDecimal total = collectionByUsageType.stream().map(usageCategory -> usageCategory.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return Arrays.asList(
				Data.builder().headerName("DSS_PT_COLLECTION_BY_USAGE_TYPE").headerValue(total).plots(plots).build());
	}

	public List<Data> demandCollectionIndexDDRRevenue(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
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

		HashMap<String, BigDecimal> tenantWiseTransactions = new HashMap<>();
		tenantWisePayments.forEach((key,value) -> {
			BigDecimal noOfTransactions = BigDecimal.valueOf(value.size());
			tenantWiseTransactions.put(key, noOfTransactions);
		});

		HashMap<String, BigDecimal> tenantWiseAssessedProperties = new HashMap<>();
		tenantWisePayments.forEach((key,value) -> {
			BigDecimal noOfTransactions = BigDecimal.valueOf(value.stream().map(pay -> pay.getPaymentDetails()
					.get(0).getBill().getConsumerCode()).distinct().count());
			tenantWiseAssessedProperties.put(key, noOfTransactions);
		});

		List<Data> response = new ArrayList<>();

		tenantWiseAmountCollection.forEach((key, value) -> {

			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("Total Collection").value(value).build());

			plots.add(Plot.builder().name("Transactions").value(tenantWiseTransactions.get(key)).build());

			plots.add(Plot.builder().name("Assessed Properties").value(tenantWiseAssessedProperties.get(key)).build());

			response.add(Data.builder().headerName(key).plots(plots).build());
		});

		return response;
	}

	public List<Data> taxheadsBreakupDDRRevenue(PayloadDetails payloadDetails) {
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		List<Payment> payments = paymentRepository.getPayments(paymentSearchCriteria);
		Map<String, List<Payment>> taxheadsBreakupTenantWise = payments.parallelStream()
				.filter(pay -> pay.getPaymentStatus() != PaymentStatusEnum.CANCELLED)
				.filter(pay -> !pay.getTenantId().equalsIgnoreCase("od.testing"))
				.collect(Collectors.groupingBy(pay -> pay.getTenantId()));
		List<Data> taxHeadBreakupResponse = new ArrayList<>();
		int SerialNumber = 1;
		for (Map.Entry<String,List<Payment>> mapElement : taxheadsBreakupTenantWise.entrySet()) {
			
			String key = mapElement.getKey();
            List<Payment> value = mapElement.getValue();
            List<Plot> plots = new ArrayList();
            plots.add(Plot.builder().name("S.N.").label(String.valueOf(SerialNumber)).symbol("text").build());
            plots.add(Plot.builder().name("DDRs").label(key).symbol("text").build());
            List<BillAccountDetail> taxheads = value.stream().map(pay -> pay.getPaymentDetails().get(0).getBill().getBillDetails().get(0).getBillAccountDetails())
            		                      .flatMap(Collection::stream)
            		                      .collect(Collectors.toList());
        		
        		Map<String, BigDecimal > taxes = taxheads.parallelStream()
        				.collect(Collectors.groupingBy(BillAccountDetail::getTaxHeadCode,Collectors.reducing(BigDecimal.ZERO, BillAccountDetail::getAmount, BigDecimal::add)));

        		for (Map.Entry<String, BigDecimal > entry : taxes.entrySet()) {
 
    			plots.add(Plot.builder().name(entry.getKey()).value(entry.getValue()).symbol(key).build());

        		}
        		Data response = Data.builder().headerName(key).headerValue(SerialNumber).plots(plots).build();
        		SerialNumber++;
        		taxHeadBreakupResponse.add(response);
		}
		
        		return taxHeadBreakupResponse;
	}
	
	
	
	public List<Data> taxHeadsBreakupUsage(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		List<Payment> payments = paymentRepository.getPayments(paymentSearchCriteria);
		Map<String, List<Payment>> consumerCodeWisePayments = payments.parallelStream()
				.filter(pay -> pay.getPaymentStatus() != PaymentStatusEnum.CANCELLED)
				.filter(pay -> !pay.getTenantId().equalsIgnoreCase("od.testing"))
				.collect(Collectors.groupingBy(pay -> pay.getPaymentDetails().get(0).getBill().getConsumerCode()));
		
		 HashMap<String, List<BillAccountDetail>> consumerCodeWiseTaxesPaid = new HashMap<>();
			consumerCodeWisePayments.forEach((key,value) -> {
				List<BillAccountDetail> sum = value.stream().map(pay -> pay.getPaymentDetails().get(0).getBill().getBillDetails().get(0).getBillAccountDetails()).flatMap(Collection::stream)
	                      .collect(Collectors.toList());
				consumerCodeWiseTaxesPaid.put(key, sum);
			});
		
		
        List<UsageTypeResponse> usageTypeResponses = paymentRepository.getUsageTypes(paymentSearchCriteria);
        
        Map<String, List<BillAccountDetail>> second_Map = new HashMap<>();
        
        usageTypeResponses.parallelStream().forEach(item -> {
			if(consumerCodeWiseTaxesPaid.containsKey(item.getConsumerCode())) {
				if(second_Map.containsKey(item.getUsageCategory())){
					List<BillAccountDetail> addedBillAccountDetails = second_Map.get(item.getUsageCategory());
					addedBillAccountDetails.addAll(consumerCodeWiseTaxesPaid.get(item.getConsumerCode()));
					second_Map.put(item.getUsageCategory(),addedBillAccountDetails);
				}else {
					second_Map.put(item.getUsageCategory(),consumerCodeWiseTaxesPaid.get(item.getConsumerCode()));
				}

			}
		});

        List<Data> taxHeadBreakupResponse = new ArrayList<>();
        int SerialNumber = 1;
        
		for (Map.Entry<String,List<BillAccountDetail>> mapElement : second_Map.entrySet()) {
			
			String key = mapElement.getKey();
			List<BillAccountDetail> value = mapElement.getValue();
            List<Plot> plots = new ArrayList();
            plots.add(Plot.builder().name("S.N.").label(String.valueOf(SerialNumber)).symbol("text").build());
            plots.add(Plot.builder().name("Usage Type").label(key).symbol("text").build());
        		
        		Map<String, BigDecimal > taxes = value.parallelStream()
        				.collect(Collectors.groupingBy(BillAccountDetail::getTaxHeadCode,Collectors.reducing(BigDecimal.ZERO, BillAccountDetail::getAmount, BigDecimal::add)));

        		for (Map.Entry<String, BigDecimal > entry : taxes.entrySet()) {
 
    			plots.add(Plot.builder().name(entry.getKey()).value(entry.getValue()).symbol("number").build());

        		}
        		Data response = Data.builder().headerName(key).headerValue(SerialNumber).plots(plots).build();
        		SerialNumber++;
        		taxHeadBreakupResponse.add(response);
        		
		}
		
        		return taxHeadBreakupResponse;
	}
	
	public List<Data> demandCollectionIndexUsageRevenue(PayloadDetails payloadDetails) {

		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
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

		HashMap<String, BigDecimal> usageCategoryWiseTransactions = new HashMap<>();
		usageCategoryWisePayments.forEach((key,value) -> {
				BigDecimal noOfTransactions = BigDecimal.valueOf(value.size());
				usageCategoryWiseTransactions.put(key, noOfTransactions);
		});

		HashMap<String, BigDecimal> usageCategoryWiseAssessedProperties = new HashMap<>();
		usageCategoryWisePayments.forEach((key,value) -> {
				BigDecimal noOfAssessedProperties = BigDecimal.valueOf(value.stream().map(pay -> pay.getConsumerCode()).distinct().count());
				usageCategoryWiseAssessedProperties.put(key, noOfAssessedProperties);
		}); 

		List<Data> response = new ArrayList<>();

		usageCategoryWiseAmount.forEach((key, value) -> {

			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("Total Collection").value(value).build());

			plots.add(Plot.builder().name("Transactions").value(usageCategoryWiseTransactions.get(key)).build());

			plots.add(Plot.builder().name("Assessed Properties").value(usageCategoryWiseAssessedProperties.get(key)).build());

			response.add(Data.builder().headerName(key).headerName(key).plots(plots).build());
		});	
		return response;
	}
    
	private TargetSearchCriteria getTargetSearchCriteria(PayloadDetails payloadDetails) {
		TargetSearchCriteria criteria = new TargetSearchCriteria();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.BS_HOME_REVENUE))
				criteria.setBusinessServices(null);
			else
				criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
		}
		
		if(StringUtils.hasText(payloadDetails.getTenantid())) {
			criteria.setTenantIds(Sets.newHashSet(payloadDetails.getTenantid()));
		}
		
		if(payloadDetails.getStartdate() != null && payloadDetails.getStartdate() != 0) {
			criteria.setFromDate(payloadDetails.getStartdate());
		}
		
		if(payloadDetails.getEnddate() != null && payloadDetails.getEnddate() != 0) {
			criteria.setToDate(payloadDetails.getEnddate());
		}
		
		if(StringUtils.hasText(payloadDetails.getTimeinterval())) {
			criteria.setFinancialYear(payloadDetails.getTimeinterval());
		}
		
		return criteria;
	}

	public List<Data> digitalCollectionsByValue(PayloadDetails payloadDetails) {
		
		//Get Total Collection Here
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
		
		//Get Total Digital Collection Here
		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.ONLINE_PAYMENT,
				DashboardConstants.NEFT_PAYMENT, DashboardConstants.RTGS_PAYMENT, DashboardConstants.CARD_PAYMENT));
		BigDecimal digitalTotalCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
		
		if(totalCollection == BigDecimal.ZERO)
			totalCollection = BigDecimal.ONE;
		//Get Digital Collections by value using formula -> digitalCollectionAmount *100/totalCollectionAmount
		BigDecimal digitalCollectionByValue = digitalTotalCollection.multiply(new BigDecimal(100)).divide(totalCollection, 2, RoundingMode.HALF_UP);
		
		return Arrays.asList(Data.builder().headerValue(digitalCollectionByValue.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> digitalCollectionsByVolume(PayloadDetails payloadDetails) {
		//Get Total Transaction count
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		
		Long totalTransactionCount = (Long) paymentRepository.getTotalTransactionCount(paymentSearchCriteria);
		//Get Total Digital Transaction Count Here
				paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.ONLINE_PAYMENT,
						DashboardConstants.NEFT_PAYMENT, DashboardConstants.RTGS_PAYMENT, DashboardConstants.CARD_PAYMENT));
		
		Long digitalTransactionCount = (Long) paymentRepository.getTotalTransactionCount(paymentSearchCriteria);
		if(totalTransactionCount == 0L)
			totalTransactionCount = 1L;
		//Get Digital Transaction by volume using formula -> digitalCollectioncount *100/totalCollectionCount 
		Long digitalCollectionByVolume = (digitalTransactionCount * 100)/totalTransactionCount;
		return Arrays.asList(Data.builder().headerValue(digitalCollectionByVolume).build());
	}
	
	public List<Data> revenueGrowthRate(PayloadDetails payloadDetails) {
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		BigDecimal growthRate = new BigDecimal(100);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
		BigDecimal previousYearCollection = getPreviousYearCollection(payloadDetails);
		if (previousYearCollection.intValue() != 0) {
			growthRate = (totalCollection.divide(previousYearCollection, 2, RoundingMode.HALF_UP))
					.multiply(new BigDecimal(100));
		
		}

		return Arrays.asList(Data.builder().headerValue(growthRate).build());
	}
	
	public List<Data> revenueGrowthRateTable(PayloadDetails payloadDetails) {
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		
		List<Chart> collectionGrowthRate = paymentRepository.getCollectionGrowthRate(paymentSearchCriteria);
		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChartPercentage(collectionGrowthRate, plots);
		
        
		BigDecimal total = collectionGrowthRate.stream().map(usageCategory -> usageCategory.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);	

		return Arrays.asList(Data.builder().headerName("Collection Growth Rate").headerSymbol("amount").headerValue(total).plots(plots).build());
	}

	public BigDecimal getPreviousYearCollection(PayloadDetails payloadDetails) {
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
	/*	LocalDate fromDate = Instant.ofEpochMilli(payloadDetails.getStartdate()).atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate toDate = Instant.ofEpochMilli(payloadDetails.getEnddate()).atZone(ZoneId.systemDefault()).toLocalDate();
	    LocalDate previousYearFromDate = fromDate.minusYears(1);
	    LocalDate previousYearToDate = toDate.minusYears(1);
        Instant fromDateInstant =  previousYearFromDate.atStartOfDay(ZoneId.systemDefault()).toInstant(); */
        paymentSearchCriteria.setFromDate(Long.valueOf("1648751400000"));
		paymentSearchCriteria.setToDate(Long.valueOf("1680287399000"));
		BigDecimal previousYearCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
		return previousYearCollection;
	}
	
	public List<Data> topUlbsDigitalCollectionByValue(PayloadDetails payloadDetails) {

		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);

		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.ALL_DIGITAL_PAYMENT_MODE));
		HashMap<String, BigDecimal> tenantWiseDigitalCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);

		HashMap<String, BigDecimal> tenantWisePercentage = new HashMap<>();

		tenantWiseDigitalCollection.forEach((key, value) -> {
			BigDecimal collection = tenantWiseCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			tenantWisePercentage.put(key, percentage);
		});

		Map<String, BigDecimal> tenantWisePercentageSorted = tenantWisePercentage.entrySet().parallelStream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		List<Data> responseList = new ArrayList<>();

		int rank = 0;
		for (Map.Entry<String, BigDecimal> tenantWisePercent : tenantWisePercentageSorted.entrySet()) {
			rank++;
			List<Plot> plots = Arrays.asList(Plot.builder().name(tenantWisePercent.getKey())
					.value(tenantWisePercent.getValue()).symbol("percentage").label("DSS_COLLECTION_RATE").build());
			responseList.add(Data.builder().plots(plots).headerValue(rank).build());
		}

		return responseList;

	}
	
	public List<Data> bottomUlbsDigitalCollectionByValue(PayloadDetails payloadDetails) {

		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);

		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.ALL_DIGITAL_PAYMENT_MODE));
		HashMap<String, BigDecimal> tenantWiseDigitalCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);

		HashMap<String, BigDecimal> tenantWisePercentage = new HashMap<>();

		tenantWiseDigitalCollection.forEach((key, value) -> {
			BigDecimal collection = tenantWiseCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			tenantWisePercentage.put(key, percentage);
		});

		Map<String, BigDecimal> tenantWisePercentageSorted = tenantWisePercentage.entrySet().parallelStream()
				.sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		

		List<Data> responseList = new ArrayList<>();

		int rank = tenantWisePercentageSorted.size()+1;
		for (Map.Entry<String, BigDecimal> tenantWisePercent : tenantWisePercentageSorted.entrySet()) {
			rank--;
			List<Plot> plots = Arrays.asList(Plot.builder().name(tenantWisePercent.getKey())
					.value(tenantWisePercent.getValue()).symbol("percentage").label("DSS_COLLECTION_RATE").build());
			responseList.add(Data.builder().plots(plots).headerValue(rank).build());
		}

		return responseList;

	}
	
	public List<Data> topUlbsDigitalCollectionByVolume(PayloadDetails payloadDetails) {

		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseCollection = paymentRepository
				.getTenantWiseTransaction(paymentSearchCriteria);

		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.ALL_DIGITAL_PAYMENT_MODE));
		HashMap<String, BigDecimal> tenantWiseDigitalCollection = paymentRepository
				.getTenantWiseTransaction(paymentSearchCriteria);

		HashMap<String, BigDecimal> tenantWisePercentage = new HashMap<>();

		tenantWiseDigitalCollection.forEach((key, value) -> {
			BigDecimal collection = tenantWiseCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			tenantWisePercentage.put(key, percentage);
		});

		Map<String, BigDecimal> tenantWisePercentageSorted = tenantWisePercentage.entrySet().parallelStream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		List<Data> responseList = new ArrayList<>();

		int rank = 0;
		for (Map.Entry<String, BigDecimal> tenantWisePercent : tenantWisePercentageSorted.entrySet()) {
			rank++;
			List<Plot> plots = Arrays.asList(Plot.builder().name(tenantWisePercent.getKey())
					.value(tenantWisePercent.getValue()).symbol("percentage").label("DSS_COLLECTION_RATE").build());
			responseList.add(Data.builder().plots(plots).headerValue(rank).build());
		}

		return responseList;

	}
	
	public List<Data> bottomUlbsDigitalCollectionByVolume(PayloadDetails payloadDetails) {

		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseCollection = paymentRepository
				.getTenantWiseTransaction(paymentSearchCriteria);

		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.ALL_DIGITAL_PAYMENT_MODE));
		HashMap<String, BigDecimal> tenantWiseDigitalCollection = paymentRepository
				.getTenantWiseTransaction(paymentSearchCriteria);

		HashMap<String, BigDecimal> tenantWisePercentage = new HashMap<>();

		tenantWiseDigitalCollection.forEach((key, value) -> {
			BigDecimal collection = tenantWiseCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			tenantWisePercentage.put(key, percentage);
		});

		Map<String, BigDecimal> tenantWisePercentageSorted = tenantWisePercentage.entrySet().parallelStream()
				.sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		List<Data> responseList = new ArrayList<>();

		int rank = tenantWisePercentageSorted.size()+1;
		for (Map.Entry<String, BigDecimal> tenantWisePercent : tenantWisePercentageSorted.entrySet()) {
			rank--;
			List<Plot> plots = Arrays.asList(Plot.builder().name(tenantWisePercent.getKey())
					.value(tenantWisePercent.getValue()).symbol("percentage").label("DSS_COLLECTION_RATE").build());
			responseList.add(Data.builder().plots(plots).headerValue(rank).build());
		}

		return responseList;

	}

	private void extractDataForChart(List<Chart> items, List<Plot> plots) {
		items.stream().forEach(item ->{
			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("number").build());
		});
	}
	
	private void extractDataForChartPercentage(List<Chart> items, List<Plot> plots) {
		items.stream().forEach(item ->{
			plots.add(Plot.builder().name(item.getName()).value(item.getValue()).symbol("percentage").build());
		});
	}

	public List<Data> revenuePTTaxHeadsBreakup(PayloadDetails payloadDetails) {
		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		List<HashMap<String, Object>> ptTaxHeadsBreakup = paymentRepository.getptTaxHeadsBreakup(criteria);
		
			 List<Data> response = new ArrayList();
			 int serailNumber = 0 ;
			 for( HashMap<String, Object> ptTaxHeadRow : ptTaxHeadsBreakup) {
				 serailNumber++;
		            String tenantId = String.valueOf(ptTaxHeadRow.get("tenantid"));
		            String tenantIdStyled = tenantId.replace("od.", "");
		            tenantIdStyled = tenantIdStyled.substring(0, 1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
				 List<Plot> row = new ArrayList<>();
				row.add(Plot.builder().label(String.valueOf(serailNumber)).name("S.N.").symbol("text").build());
				row.add(Plot.builder().label(tenantIdStyled).name("DDRs").symbol("text").build());
				
				BigDecimal total = (new BigDecimal(String.valueOf(ptTaxHeadRow.get("holdingtaxcollection"))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("lighttaxcollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("watertaxcollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("servicetaxcollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("otherduescollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("drainagetaxcollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("latrinetaxcollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("penaltycollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("rebatecollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("interestcollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("parkingtaxcollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("ownershipexcemptioncollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("solidwasteuserchargecollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("usageexcemptioncollection")))))
						.add((new BigDecimal(String.valueOf(ptTaxHeadRow.get("advancecollection"))).abs()));
				
				row.add(Plot.builder().name("Holding Tax").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("holdingtaxcollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Light Tax").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("lighttaxcollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Water Tax").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("watertaxcollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Service Tax").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("servicetaxcollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Other Tax").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("otherduescollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Drainage Tax").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("drainagetaxcollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Latrine Tax").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("latrinetaxcollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Penalty").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("penaltycollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Rebate").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("rebatecollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Interest").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("interestcollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Parking Tax").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("parkingtaxcollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Ownership Exemption").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("ownershipexcemptioncollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Solid Waste User charges").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("solidwasteuserchargecollection")))).symbol("number").build());				
				row.add(Plot.builder().name("Usage Exemption").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("usageexcemptioncollection")))).symbol("number").build());	
				row.add(Plot.builder().name("Advance").value(new BigDecimal(String.valueOf(ptTaxHeadRow.get("advancecollection")))).symbol("number").build());
				row.add(Plot.builder().name("Total Amount").value(total).symbol("number").build());				

				 response.add(Data.builder().headerName(tenantIdStyled).headerValue(serailNumber).plots(row).insight(null).build());
			 }	
			 
				if (CollectionUtils.isEmpty(response)) {

					List<Plot> row = new ArrayList<>();
					row.add(Plot.builder().label(String.valueOf(serailNumber)).name("S.N.").symbol("text").build());
					row.add(Plot.builder().label(payloadDetails.getTenantid()).name("DDRs").symbol("text").build());

					row.add(Plot.builder().name("Holding Tax").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Light Tax").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Water Tax").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Service Tax").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Other Tax").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Drainage Tax").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Latrine Tax").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Penalty").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Rebate").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Interest").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Parking Tax").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Ownership Exemption").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Solid Waste User charges").value(BigDecimal.ZERO).symbol("number")
							.build());
					row.add(Plot.builder().name("Usage Exemption").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Advance").value(BigDecimal.ZERO).symbol("number").build());
					row.add(Plot.builder().name("Total Amount").value(BigDecimal.ZERO).symbol("number").build());

					response.add(Data.builder().headerName(payloadDetails.getTenantid()).headerValue(serailNumber)
							.plots(row).insight(null).build());

				}
			
		return response;
	}
	
	public List<Data> previousYearTargetAchieved(PayloadDetails payloadDetails) {
		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));

		BigDecimal previousYearCollection = getPreviousYearCollection(payloadDetails);
		
		TargetSearchCriteria targetCriteria = getTargetSearchCriteria(payloadDetails);
		
		BigDecimal previourYearTargetCollection = (BigDecimal) paymentRepository.getTtargetCollection(targetCriteria);
	
		if(previourYearTargetCollection.equals(null))
			previourYearTargetCollection = BigDecimal.ONE;
		
		List<Chart> chart = new ArrayList<Chart>();
		chart.add(Chart.builder().name(DashboardConstants.PREVIOUS_YEAR_TOTAL_COLLECTION).value(previousYearCollection)
				.build());
		chart.add(Chart.builder().name(DashboardConstants.PREVIOUS_YEAR_TARGET_COLLECTION).value(previourYearTargetCollection)
				.build());
		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(chart, plots);
		BigDecimal total = chart.stream().map(collection -> collection.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return Arrays.asList(
				Data.builder().headerName("DSS_PREVIOUS_TARGET_ACHIEVED").headerValue(total).plots(plots).build());
	}
	
	public List<Data> ptFinancialIndicatorsData(PayloadDetails payloadDetails) {

		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseAmountCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		HashMap<String, BigDecimal> tenantWiseTransactions = paymentRepository
				.getTenantWiseTransaction(paymentSearchCriteria);
		HashMap<String, BigDecimal> tenantWiseAssessedProperties = paymentRepository
				.getTenantPropertiesPaid(paymentSearchCriteria);
		List<Data> response = new ArrayList<>();
		int serialNumber = 1;

		for (HashMap.Entry<String, BigDecimal> tenantWiseCollection : tenantWiseAmountCollection.entrySet()) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(
					Plot.builder().name("ULBs").label(tenantWiseCollection.getKey().toString()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(tenantWiseCollection.getValue()).symbol("amount")
					.build());

			plots.add(
					Plot.builder().name("Transactions")
							.value(tenantWiseTransactions.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO
									: tenantWiseTransactions.get(tenantWiseCollection.getKey()))
							.symbol("number").build());

			plots.add(Plot.builder().name("Assessed Properties")
					.value(tenantWiseAssessedProperties.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO
							: tenantWiseAssessedProperties.get(tenantWiseCollection.getKey()))
					.symbol("number").build());

			response.add(Data.builder().headerName(tenantWiseCollection.getKey()).plots(plots).headerValue(serialNumber)
					.headerName(tenantWiseCollection.getKey()).build());

			serialNumber++;

		}
		
		if (CollectionUtils.isEmpty(response)) {

			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(BigDecimal.ZERO).symbol("amount").build());

			plots.add(Plot.builder().name("Transactions").value(BigDecimal.ZERO).symbol("number").build());

			plots.add(Plot.builder().name("Assessed Properties").value(BigDecimal.ZERO).symbol("number").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());
		}

		return response;
	}

	public List<Data> ptPaymentModeData(PayloadDetails payloadDetails) {

		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseAmountCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.CARD_PAYMENT));
		HashMap<String, BigDecimal> tenantWiseCardCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.ONLINE_PAYMENT));
		HashMap<String, BigDecimal> tenantWiseOnlineCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.CHEQUE_PAYMENT));
		HashMap<String, BigDecimal> tenantWiseChequeCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.CASH_PAYMENT));
		HashMap<String, BigDecimal> tenantWiseCashCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);

		HashMap<String, BigDecimal> tenantWiseCardPercentage = new HashMap<>();
		HashMap<String, BigDecimal> tenantWiseOnlinePercentage = new HashMap<>();
		HashMap<String, BigDecimal> tenantWiseChequePercentage = new HashMap<>();
		HashMap<String, BigDecimal> tenantWiseCashPercentage = new HashMap<>();

		tenantWiseCardCollection.forEach((key, value) -> {
			BigDecimal collection = tenantWiseAmountCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			tenantWiseCardPercentage.put(key, percentage);
		});
		tenantWiseOnlineCollection.forEach((key, value) -> {
			BigDecimal collection = tenantWiseAmountCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			tenantWiseOnlinePercentage.put(key, percentage);
		});
		tenantWiseChequeCollection.forEach((key, value) -> {
			BigDecimal collection = tenantWiseAmountCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			tenantWiseChequePercentage.put(key, percentage);
		});
		tenantWiseCashCollection.forEach((key, value) -> {
			BigDecimal collection = tenantWiseAmountCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			tenantWiseCashPercentage.put(key, percentage);
		});
		List<Data> response = new ArrayList<>();
		int serialNumber = 1;

		for (HashMap.Entry<String, BigDecimal> tenantWiseCollection : tenantWiseAmountCollection.entrySet()) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(tenantWiseCollection.getKey().toString()).symbol("text").build());

			plots.add(Plot.builder().name("Card Collection")
					.value(tenantWiseCardPercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : tenantWiseCardPercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			plots.add(Plot.builder().name("Online Collection")
					.value(tenantWiseOnlinePercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : tenantWiseOnlinePercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			plots.add(Plot.builder().name("Cheque Collection")
					.value(tenantWiseChequePercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : tenantWiseChequePercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			plots.add(Plot.builder().name("Cash Collection")
					.value(tenantWiseCashPercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : tenantWiseCashPercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			response.add(Data.builder().headerName(tenantWiseCollection.getKey()).plots(plots).headerValue(serialNumber).build());
			
			serialNumber++;

		}
		
		if (CollectionUtils.isEmpty(response)) {

			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Card Collection").value(BigDecimal.ZERO).symbol("percentage").build());

			plots.add(Plot.builder().name("Online Collection").value(BigDecimal.ZERO).symbol("percentage").build());

			plots.add(Plot.builder().name("Cheque Collection").value(BigDecimal.ZERO).symbol("percentage").build());

			plots.add(Plot.builder().name("Cash Collection").value(BigDecimal.ZERO).symbol("percentage").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());
		}

		return response;
	}

	public List<Data> getWsApplicationFeeCollection(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_WS_ONE_TIME_FEE,
				DashboardConstants.BUSINESS_SERVICE_SW_ONE_TIME_FEE));
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalApplicationFeeCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
        return Arrays.asList(Data.builder().headerValue(totalApplicationFeeCollection.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> getWsDemandFeeCollection(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setBusinessServices(
				Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_WS, DashboardConstants.BUSINESS_SERVICE_SW));
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalDemandFeeCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
        return Arrays.asList(Data.builder().headerValue(totalDemandFeeCollection.setScale(2, RoundingMode.HALF_UP)).build());
	}
	
	public List<Data> getWSCumulativeCollection(PayloadDetails payloadDetails) {

		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		if (!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			criteria.setFromDate(dashboardUtils.getStartDateGmt(String.valueOf(payloadDetails.getTimeinterval())));
		}
		List<Chart> cumulativeCollection = paymentRepository.getCumulativeCollection(criteria);
		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(cumulativeCollection, plots);

		BigDecimal total = cumulativeCollection.stream().map(usageCategory -> usageCategory.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return Arrays.asList(Data.builder().headerName("Water & Sewerage Collections").headerSymbol("amount")
				.headerValue(total).plots(plots).build());

	}
	
	public List<Data> wsCollectionByUsageType(PayloadDetails payloadDetails) {
		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_WS_ONE_TIME_FEE,
				DashboardConstants.BUSINESS_SERVICE_SW_ONE_TIME_FEE));
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		List<Chart> collectionByUsageType = paymentRepository.getWSCollectionByUsageType(criteria);

		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(collectionByUsageType, plots);

		BigDecimal total = collectionByUsageType.stream().map(usageCategory -> usageCategory.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return Arrays.asList(
				Data.builder().headerName("DSS_WS_APPLICATION_COLLECTION_BY_USAGE").headerValue(total).plots(plots).build());
	}

	public List<Data> wsCollectionByChannel(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		List<Chart> collectionByChannel = paymentRepository.getCollectionByChannel(criteria);
		
		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(collectionByChannel, plots);
		
		BigDecimal total = collectionByChannel.stream().map(paymentmode -> paymentmode.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return Arrays.asList(
				Data.builder().headerName("DSS_WS_COLLECTION_BY_CHANNEL").headerValue(total).plots(plots).build());
	}

	public List<Data> getWSTaxHeadsBreakup(PayloadDetails payloadDetails) {
		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		List<HashMap<String, Object>> wsTaxHeadsBreakup = paymentRepository.getWSTaxHeadsBreakup(criteria);

		List<Data> response = new ArrayList();
		int serailNumber = 0;
		for (HashMap<String, Object> wsTaxHeadRow : wsTaxHeadsBreakup) {
			serailNumber++;
			String tenantId = String.valueOf(wsTaxHeadRow.get("tenantid"));
			String tenantIdStyled = tenantId.replace("od.", "");
			tenantIdStyled = tenantIdStyled.substring(0, 1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
			List<Plot> row = new ArrayList<>();
			row.add(Plot.builder().label(String.valueOf(serailNumber)).name("S.N.").symbol("text").build());
			row.add(Plot.builder().label(tenantIdStyled).name("DDRs").symbol("text").build());

			BigDecimal total = (new BigDecimal(String.valueOf(wsTaxHeadRow.get("waterchargecollection"))))
					.add((new BigDecimal(String.valueOf(wsTaxHeadRow.get("seweragechargescollection")))))
					.add((new BigDecimal(String.valueOf(wsTaxHeadRow.get("sewerageadhocchargecollection")))))
					.add((new BigDecimal(String.valueOf(wsTaxHeadRow.get("penaltycollection")))))
					.add((new BigDecimal(String.valueOf(wsTaxHeadRow.get("rebatecollection")))))
					.add((new BigDecimal(String.valueOf(wsTaxHeadRow.get("interestcollection")))))
					.add((new BigDecimal(String.valueOf(wsTaxHeadRow.get("scrutinycollection")))))
					.add((new BigDecimal(String.valueOf(wsTaxHeadRow.get("securitycollection")))))
					.add((new BigDecimal(String.valueOf(wsTaxHeadRow.get("ownershipchangecollection")))))
					.add((new BigDecimal(String.valueOf(wsTaxHeadRow.get("labourcollection")))))
					.add((new BigDecimal(String.valueOf(wsTaxHeadRow.get("advancecollection")))));

			row.add(Plot.builder().name("Water Charges")
					.value(new BigDecimal(String.valueOf(wsTaxHeadRow.get("waterchargecollection")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Sewerage Charges")
					.value(new BigDecimal(String.valueOf(wsTaxHeadRow.get("seweragechargescollection")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Sewerage Adhoc Charges")
					.value(new BigDecimal(String.valueOf(wsTaxHeadRow.get("sewerageadhocchargecollection")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Penalty")
					.value(new BigDecimal(String.valueOf(wsTaxHeadRow.get("penaltycollection")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Rebate")
					.value(new BigDecimal(String.valueOf(wsTaxHeadRow.get("rebatecollection")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Interest")
					.value(new BigDecimal(String.valueOf(wsTaxHeadRow.get("interestcollection")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Scrutiny")
					.value(new BigDecimal(String.valueOf(wsTaxHeadRow.get("scrutinycollection")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Security")
					.value(new BigDecimal(String.valueOf(wsTaxHeadRow.get("securitycollection")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Ownership Change")
					.value(new BigDecimal(String.valueOf(wsTaxHeadRow.get("ownershipchangecollection")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Labour Fee")
					.value(new BigDecimal(String.valueOf(wsTaxHeadRow.get("labourcollection")))).symbol("number")
					.build());
			row.add(Plot.builder().name("Advance")
					.value(new BigDecimal(String.valueOf(wsTaxHeadRow.get("advancecollection")))).symbol("number")
					.build());

			response.add(Data.builder().headerName(tenantIdStyled).headerValue(serailNumber).plots(row).insight(null)
					.build());
		}
		
		if (CollectionUtils.isEmpty(response)) {

			List<Plot> row = new ArrayList<>();
			row.add(Plot.builder().label(String.valueOf(serailNumber)).name("S.N.").symbol("text").build());
			row.add(Plot.builder().label(payloadDetails.getTenantid()).name("DDRs").symbol("text").build());

			row.add(Plot.builder().name("Water Charges").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Sewerage Charges").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Sewerage Adhoc Charges").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Penalty").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Rebate").value(BigDecimal.ZERO).symbol("number").build());
			row.add(Plot.builder().name("Interest").value(BigDecimal.ZERO).symbol("number").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).headerValue(serailNumber).plots(row)
					.insight(null).build());
		}

		return response;
	}

	public List<Data> getWSKeyFinancialIndicators(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseAmountCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		HashMap<String, BigDecimal> tenantWiseTransactions = paymentRepository
				.getTenantWiseTransaction(paymentSearchCriteria);
		HashMap<String, BigDecimal> tenantWiseConnections = paymentRepository
				.getTenantPropertiesPaid(paymentSearchCriteria);
		
		List<Data> response = new ArrayList<>();
		int serialNumber = 1;

		for (HashMap.Entry<String, BigDecimal> tenantWiseCollection : tenantWiseAmountCollection.entrySet()) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(tenantWiseCollection.getKey().toString()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(tenantWiseCollection.getValue())
					.symbol("amount").build());

			plots.add(
					Plot.builder().name("Transactions")
							.value(tenantWiseTransactions.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO
									: tenantWiseTransactions.get(tenantWiseCollection.getKey()))
							.symbol("number").build());

			plots.add(
					Plot.builder().name("Total_Connections")
							.value(tenantWiseConnections.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO
									: tenantWiseConnections.get(tenantWiseCollection.getKey()))
							.symbol("number").build());

			response.add(Data.builder().headerName(tenantWiseCollection.getKey()).plots(plots)
					.headerValue(serialNumber).headerName(tenantWiseCollection.getKey().toString())
					.build());

			serialNumber++;
		}
		
		if (CollectionUtils.isEmpty(response)) {

			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(BigDecimal.ZERO).symbol("amount").build());

			plots.add(Plot.builder().name("Transactions").value(BigDecimal.ZERO).symbol("number").build());

			plots.add(Plot.builder().name("Total_Connections").value(BigDecimal.ZERO).symbol("number").build());

			response.add(Data.builder().plots(plots).headerValue(serialNumber).headerName(payloadDetails.getTenantid())
					.build());

		}

		return response;
	}

	public List<Data> bpaFeeCollection(PayloadDetails payloadDetails) {
		String visulizationCode = payloadDetails.getVisualizationcode();
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		
		if(Constants.VisualizationCodes.REVENUE_BPA_APP_FEE_COLLECTIONS.equalsIgnoreCase(visulizationCode))
			paymentSearchCriteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_BPA_APP_FEE));
		
		if(Constants.VisualizationCodes.REVENUE_BPA_SANC_FEE_COLLECTIONS.equalsIgnoreCase(visulizationCode))
			paymentSearchCriteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_BPA_SAN_FEE));
		
		if(Constants.VisualizationCodes.REVENUE_OC_APP_FEE_COLLECTIONS.equalsIgnoreCase(visulizationCode))
			paymentSearchCriteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_OC_APP_FEE));
		
		if(Constants.VisualizationCodes.REVENUE_OC_SANC_FEE_COLLECTIONS.equalsIgnoreCase(visulizationCode))
			paymentSearchCriteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_OC_SAN_FEE));
		
		BigDecimal totalCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
        return Arrays.asList(Data.builder().headerValue(totalCollection.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> getBPACumulativeCollection(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		if (!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			criteria.setFromDate(dashboardUtils.getStartDateGmt(String.valueOf(payloadDetails.getTimeinterval())));
		}
		List<Chart> cumulativeCollection = paymentRepository.getCumulativeCollection(criteria);
		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(cumulativeCollection, plots);
        
		BigDecimal total = cumulativeCollection.stream().map(usageCategory -> usageCategory.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);		 

		return Arrays.asList(Data.builder().headerName("DSS_BPA_TOTAL_CUMULATIVE_COLLECTION").headerSymbol("amount").headerValue(total).plots(plots).build());
	}
	
	public List<Data> bpaCollectionByChannel(PayloadDetails payloadDetails) {

		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		List<Chart> collectionByChannel = paymentRepository.getCollectionByChannel(criteria);

		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(collectionByChannel, plots);

		BigDecimal total = collectionByChannel.stream().map(paymentmode -> paymentmode.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return Arrays.asList(
				Data.builder().headerName("DSS_OBPS_COLLECTION_BY_PAYMENT_MODE").headerValue(total).plots(plots).build());
	}

	public List<Data> bpaCollectionReport(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseAmountCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		
		List<Data> response = new ArrayList<>();
		int serialNumber = 1;

		for (HashMap.Entry<String, BigDecimal> tenantWiseCollection : tenantWiseAmountCollection.entrySet()) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(tenantWiseCollection.getKey().toString()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(tenantWiseCollection.getValue())
					.symbol("amount").build());


			response.add(Data.builder().headerName(tenantWiseCollection.getKey()).plots(plots)
					.headerValue(serialNumber).headerName(tenantWiseCollection.getKey().toString())
					.build());

			serialNumber++;
		}
		
		if (CollectionUtils.isEmpty(response)) {

			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(BigDecimal.ZERO).symbol("amount").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());
		}

		return response;

	}

	public List<Data> tlCollectionsByLicenseType(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		
		List<Chart> licenseTypeWiseAmtCollected = paymentRepository
				.getTlCollectionsByLicenseType(paymentSearchCriteria);
		
		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(licenseTypeWiseAmtCollected, plots);

		BigDecimal total = licenseTypeWiseAmtCollected.stream().map(amount -> amount.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return Arrays.asList(
				Data.builder().headerName("DSS_TL_LICENSE_BY_TYPE").headerValue(total).plots(plots).build());
	}

	public List<Data> tlKeyFinancialIndicators(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		
		TargetSearchCriteria targetSearchCriteria = getTargetSearchCriteria(payloadDetails);
		
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseAmountCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		HashMap<String, BigDecimal> tenantWiseTransactions = paymentRepository
				.getTenantWiseTransaction(paymentSearchCriteria);
		HashMap<String, BigDecimal> tenantWiseLicensesIssued = paymentRepository
				.getTenantWiseLicensesIssued(paymentSearchCriteria);
		HashMap<String, BigDecimal> tenantWiseTargetCollection = paymentRepository
				.getTenantWiseTargetCollection(targetSearchCriteria);
		HashMap<String, BigDecimal> tenantWiseTargetAchieved = new HashMap<>();
		
		tenantWiseAmountCollection.forEach((key,value) -> {
			
			if(tenantWiseTargetCollection.containsKey(key)) {
				BigDecimal target = tenantWiseTargetCollection.get(key);
				BigDecimal targetAchieved = value.multiply(new BigDecimal(100)).divide(target,2,RoundingMode.HALF_UP);
				tenantWiseTargetAchieved.put(key, targetAchieved);
			}		
		});
		
		
		List<Data> response = new ArrayList<>();
		int serialNumber = 1;

		for (HashMap.Entry<String, BigDecimal> tenantWiseCollection : tenantWiseAmountCollection.entrySet()) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(
					Plot.builder().name("ULBs").label(tenantWiseCollection.getKey().toString()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(tenantWiseCollection.getValue()).symbol("amount")
					.build());

			plots.add(
					Plot.builder().name("Transactions")
							.value(tenantWiseTransactions.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO
									: tenantWiseTransactions.get(tenantWiseCollection.getKey()))
							.symbol("number").build());

			plots.add(Plot.builder().name("Total Licenses Issued")
					.value(tenantWiseLicensesIssued.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO
							: tenantWiseLicensesIssued.get(tenantWiseCollection.getKey()))
					.symbol("number").build());
			
			plots.add(Plot.builder().name("Target Collection")
					.value(tenantWiseTargetCollection.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO
							: tenantWiseTargetCollection.get(tenantWiseCollection.getKey()))
					.symbol("number").build());
			
			plots.add(Plot.builder().name("Target Achieved")
					.value(tenantWiseTargetAchieved.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO
							: tenantWiseTargetAchieved.get(tenantWiseCollection.getKey()))
					.symbol("percentage").build());

			response.add(Data.builder().headerName(tenantWiseCollection.getKey()).plots(plots).headerValue(serialNumber)
					.headerName(tenantWiseCollection.getKey()).build());

			serialNumber++;

		}
		if (CollectionUtils.isEmpty(response)) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(BigDecimal.ZERO).symbol("amount").build());

			plots.add(Plot.builder().name("Transactions").value(BigDecimal.ZERO).symbol("number").build());

			plots.add(Plot.builder().name("Total Licenses Issued").value(BigDecimal.ZERO).symbol("number").build());

			plots.add(Plot.builder().name("Target Collection").value(BigDecimal.ZERO).symbol("number").build());

			plots.add(Plot.builder().name("Target Achieved").value(BigDecimal.ZERO).symbol("percentage").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());
		}

		return response;
	}

	public List<Data> mrKeyFinancialIndicators(PayloadDetails payloadDetails) {
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);

		TargetSearchCriteria targetSearchCriteria = getTargetSearchCriteria(payloadDetails);

		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseAmountCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		HashMap<String, BigDecimal> tenantWiseTransactions = paymentRepository
				.getTenantWiseTransaction(paymentSearchCriteria);
		HashMap<String, BigDecimal> tenantWiseMrApplications = paymentRepository
				.getTenantWiseMrApplications(paymentSearchCriteria);

		List<Data> response = new ArrayList<>();
		int serialNumber = 1;

		for (HashMap.Entry<String, BigDecimal> tenantWiseCollection : tenantWiseAmountCollection.entrySet()) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(
					Plot.builder().name("ULBs").label(tenantWiseCollection.getKey().toString()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(tenantWiseCollection.getValue()).symbol("amount")
					.build());

			plots.add(
					Plot.builder().name("Transactions")
							.value(tenantWiseTransactions.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO
									: tenantWiseTransactions.get(tenantWiseCollection.getKey()))
							.symbol("number").build());

			plots.add(
					Plot.builder().name("Total Applications")
							.value(tenantWiseMrApplications.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO
									: tenantWiseMrApplications.get(tenantWiseCollection.getKey()))
							.symbol("number").build());


			response.add(Data.builder().headerName(tenantWiseCollection.getKey()).plots(plots).headerValue(serialNumber)
					.headerName(tenantWiseCollection.getKey()).build());

			serialNumber++;

		}
		
		if (CollectionUtils.isEmpty(response)) {

			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(BigDecimal.ZERO).symbol("amount").build());

			plots.add(Plot.builder().name("Transactions").value(BigDecimal.ZERO).symbol("number").build());

			plots.add(Plot.builder().name("Total Applications").value(BigDecimal.ZERO).symbol("number").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());

		}

		return response;
	}

	public List<Data> totalCollectionDeptWise(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		List<Chart> serviceWiseCollection = paymentRepository.getServiceWiseCollection(criteria);
		
		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(serviceWiseCollection, plots);
		
		BigDecimal total = serviceWiseCollection.stream().map(serviceType -> serviceType.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return Arrays.asList(
				Data.builder().headerName("DSS_TOTAL_CUMULATIVE_COLLECTION").headerValue(total).plots(plots).build());
	}
	
	public List<Data> totalDemand(PayloadDetails payloadDetails) {
		DemandSearchCriteria demandSearchCriteria = getDemandSearchCriteria(payloadDetails);
		demandSearchCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		BigDecimal totalDemand = BigDecimal.ZERO;
		BigDecimal currentDemand = BigDecimal.ZERO;
		BigDecimal arrearDemand = BigDecimal.ZERO;
		if (!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			currentDemand = (BigDecimal) currentDemand(payloadDetails).get(0).getHeaderValue();
			arrearDemand = (BigDecimal) arrearDemand(payloadDetails).get(0).getHeaderValue();
		}
		totalDemand = currentDemand.add(arrearDemand);
		return Arrays.asList(Data.builder().headerValue(totalDemand.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> currentDemand(PayloadDetails payloadDetails) {
		DemandSearchCriteria demandSearchCriteria = getDemandSearchCriteria(payloadDetails);
		demandSearchCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		BigDecimal totalDemand = BigDecimal.ZERO;
		if (!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			totalDemand = paymentRepository.getCurrentDemand(demandSearchCriteria);
		}
		return Arrays.asList(Data.builder().headerValue(totalDemand.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> arrearDemand(PayloadDetails payloadDetails) {
		DemandSearchCriteria demandSearchCriteria = getDemandSearchCriteria(payloadDetails);
		demandSearchCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		demandSearchCriteria.setIsArrearDemand(Boolean.TRUE);
		BigDecimal totalDemand = BigDecimal.ZERO;
		if (!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			totalDemand = paymentRepository.getArrearDemand(demandSearchCriteria);
		}
		return Arrays.asList(Data.builder().headerValue(totalDemand.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> previousYearCollection(PayloadDetails payloadDetails) {
        if(!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		setFromAndToDateInIST(paymentSearchCriteria); //Instead of Add we have to subtarct
		BigDecimal previousYearCollection = (BigDecimal) paymentRepository.getPreviousYearCollection(paymentSearchCriteria);
		if(previousYearCollection == null) {
			previousYearCollection = BigDecimal.ZERO;
		}
        return Arrays.asList(Data.builder().headerValue(previousYearCollection.setScale(2, RoundingMode.HALF_UP)).build());
        }
        return Arrays.asList(Data.builder().headerValue("NA").build());
	}
	
	public List<Data> tlCollectionsByLicenseTypeLine(PayloadDetails payloadDetails) {

		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		List<Chart> licenseTypeWiseAmtCollected = paymentRepository
				.getTlCollectionsByLicenseType(paymentSearchCriteria);

		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(licenseTypeWiseAmtCollected, plots);

		BigDecimal total = licenseTypeWiseAmtCollected.stream().map(amount -> amount.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return Arrays
				.asList(Data.builder().headerName("DSS_TL_LICENSE_BY_TYPE_LINE").headerValue(total).plots(plots).build());
	}
	
	public List<Data> topPerformingUlbsOverview(PayloadDetails payloadDetails) {

		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		List<Data> response = new ArrayList();
		HashMap<String, BigDecimal> tenantWiseCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		if (Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			payloadDetails.setTimeinterval(dashboardUtils.getCurrentFinancialYear());
		}
		int serialNumber = 1;
		
		Map<String, BigDecimal> tenantWiseSorted = tenantWiseCollection.entrySet().parallelStream()
		        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())) // Sort in descending order
		        .collect(Collectors.toMap(
		                Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		for (HashMap.Entry<String, BigDecimal> totalApplication : tenantWiseSorted.entrySet()) {
            List<Plot> plots = new ArrayList();
            plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

            plots.add(Plot.builder().name("Ulb").label(totalApplication.getKey().toString()).symbol("text")
                    .build());

			plots.add(Plot.builder().name("Rank")
					.value(BigDecimal.valueOf(serialNumber))
					.symbol("number").build());

			plots.add(Plot.builder().name("Total Collections")
					.value(totalApplication.getValue() == null ? BigDecimal.ZERO
							: totalApplication.getValue().setScale(0, BigDecimal.ROUND_UP))
					.symbol("amount").build());

			response.add(Data.builder().headerName(totalApplication.getKey()).plots(plots).headerValue(serialNumber)
					.build());

            serialNumber++;

        }
		
		if (CollectionUtils.isEmpty(response)) {
			serialNumber++;
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("Ulb").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Rank").value(BigDecimal.ZERO).symbol("number").build());

			plots.add(Plot.builder().name("Total Collections").value(BigDecimal.ZERO).symbol("amount").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());

		}

		return response;

	}
	
	public List<Data> bottomPerformingUlbsOverview(PayloadDetails payloadDetails) {

		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		List<Data> response = new ArrayList();
		HashMap<String, BigDecimal> tenantWiseCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		// Sort the HashMap in ascending order
		Map<String, BigDecimal> tenantWiseSorted = tenantWiseCollection.entrySet().parallelStream()
				.sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		int serialNumber = 1;
		for (HashMap.Entry<String, BigDecimal> totalApplication : tenantWiseSorted.entrySet()) {
            List<Plot> plots = new ArrayList();
            plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

            plots.add(Plot.builder().name("Ulb").label(totalApplication.getKey().toString()).symbol("text")
                    .build());

			plots.add(Plot.builder().name("Rank")
					.value(BigDecimal.valueOf(serialNumber))
					.symbol("number").build());

			plots.add(Plot.builder().name("Total Collections")
					.value(totalApplication.getValue() == null ? BigDecimal.ZERO
							: totalApplication.getValue().setScale(0, BigDecimal.ROUND_UP))
					.symbol("amount").build());

			response.add(Data.builder().headerName(totalApplication.getKey()).plots(plots).headerValue(serialNumber)
					.build());
           
            serialNumber++;

        }
		
		if (CollectionUtils.isEmpty(response)) {
			serialNumber++;
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("Ulb").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Rank").value(BigDecimal.ZERO).symbol("number").build());

			plots.add(Plot.builder().name("Total Collections").value(BigDecimal.ZERO).symbol("amount").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());

			}

		return response;

	}
	
	public List<Data> paymentModeCollection(PayloadDetails payloadDetails) {

		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		List<Chart> paymantModeWiseCollection = paymentRepository.getPaymentModeCollections(criteria);

		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(paymantModeWiseCollection, plots);

		BigDecimal total = paymantModeWiseCollection.stream().map(serviceType -> serviceType.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return Arrays.asList(
				Data.builder().headerName("Total Collections").headerValue(total).plots(plots).build());
	}
	
	public List<Data> cumulativeCollectionOverview(PayloadDetails payloadDetails) {
		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		if (!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			criteria.setFromDate(dashboardUtils.getStartDateGmt(String.valueOf(payloadDetails.getTimeinterval())));
		}
		List<Data> response = new ArrayList<>();

		// PT Collection
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.PT_REVENUE_ALL_BS));
		List<Chart> ptCollection = paymentRepository.getCumulativeCollection(criteria);
		List<Plot> ptCollectionPlots = new ArrayList<Plot>();
		extractDataForChart(ptCollection, ptCollectionPlots);
		BigDecimal ptTotalCollections = ptCollection.stream().map(usageCategory -> usageCategory.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Property Tax").headerSymbol("amount")
				.headerValue(ptTotalCollections).plots(ptCollectionPlots).build());

		// WS Collection
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.WS_REVENUE_ALL_BS));
		List<Chart> wsCollection = paymentRepository.getCumulativeCollection(criteria);
		List<Plot> wsCollectionPlots = new ArrayList<Plot>();
		extractDataForChart(wsCollection, wsCollectionPlots);
		BigDecimal wsTotalCollections = wsCollection.stream().map(usageCategory -> usageCategory.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Water & Sewerage").headerSymbol("amount")
				.headerValue(wsTotalCollections).plots(wsCollectionPlots).build());
		
		// BPA Collection
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.OBPS_REVENUE_ALL_BS));
		List<Chart> bpaCollection = paymentRepository.getCumulativeCollection(criteria);
		List<Plot> bpaCollectionPlots = new ArrayList<Plot>();
		extractDataForChart(bpaCollection, bpaCollectionPlots);
		BigDecimal bpaTotalCollections = bpaCollection.stream().map(usageCategory -> usageCategory.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("OBPS").headerSymbol("amount")
				.headerValue(bpaTotalCollections).plots(bpaCollectionPlots).build());
		
		// TL Collection
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_TL));
		List<Chart> tlCollection = paymentRepository.getCumulativeCollection(criteria);
		List<Plot> tlCollectionPlots = new ArrayList<Plot>();
		extractDataForChart(tlCollection, tlCollectionPlots);
		BigDecimal tlTotalCollections = tlCollection.stream().map(usageCategory -> usageCategory.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Trade License").headerSymbol("amount").headerValue(tlTotalCollections)
				.plots(tlCollectionPlots).build());
		

		// MR Collection
		criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_MR));
		List<Chart> mrCollection = paymentRepository.getCumulativeCollection(criteria);
		List<Plot> mrCollectionPlots = new ArrayList<Plot>();
		extractDataForChart(mrCollection, mrCollectionPlots);
		BigDecimal mrTotalCollections = mrCollection.stream().map(usageCategory -> usageCategory.getValue())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		response.add(Data.builder().headerName("Marriage Registration").headerSymbol("amount").headerValue(mrTotalCollections)
				.plots(mrCollectionPlots).build());

		return response;
	}

	public List<Data> regularizationFeeCollection(PayloadDetails payloadDetails) {
		String visulizationCode = payloadDetails.getVisualizationcode();
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		
		if(Constants.VisualizationCodes.REVENUE_REGULARIZATION_APP_FEE_COLLECTIONS.equalsIgnoreCase(visulizationCode))
			paymentSearchCriteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_REGULARIZATION_APP_FEE));
		
		if(Constants.VisualizationCodes.REVENUE_REGULARIZATION_SANC_FEE_COLLECTIONS.equalsIgnoreCase(visulizationCode))
			paymentSearchCriteria.setBusinessServices(Sets.newHashSet(DashboardConstants.BUSINESS_SERVICE_REGULARIZATION_SAN_FEE));
		
		BigDecimal totalCollection = (BigDecimal) paymentRepository.getTotalCollection(paymentSearchCriteria);
        return Arrays.asList(Data.builder().headerValue(totalCollection.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> getRegCumulativeCollection(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria criteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		if (!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			criteria.setFromDate(dashboardUtils.getStartDateGmt(String.valueOf(payloadDetails.getTimeinterval())));
		}
		List<Chart> cumulativeCollection = paymentRepository.getCumulativeCollection(criteria);
		List<Plot> plots = new ArrayList<Plot>();
		extractDataForChart(cumulativeCollection, plots);
        
		BigDecimal total = cumulativeCollection.stream().map(usageCategory -> usageCategory.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);		 

		return Arrays.asList(Data.builder().headerName("DSS_REGULARIZATION_TOTAL_CUMULATIVE_COLLECTION").headerSymbol("amount").headerValue(total).plots(plots).build());
	}

	public List<Data> regularizationCollectionReport(PayloadDetails payloadDetails) {
		
		PaymentSearchCriteria paymentSearchCriteria = getTotalCollectionPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		HashMap<String, BigDecimal> tenantWiseAmountCollection = paymentRepository
				.getTenantWiseCollection(paymentSearchCriteria);
		
		List<Data> response = new ArrayList<>();
		int serialNumber = 1;

		for (HashMap.Entry<String, BigDecimal> tenantWiseCollection : tenantWiseAmountCollection.entrySet()) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(tenantWiseCollection.getKey().toString()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(tenantWiseCollection.getValue())
					.symbol("amount").build());


			response.add(Data.builder().headerName(tenantWiseCollection.getKey()).plots(plots)
					.headerValue(serialNumber).headerName(tenantWiseCollection.getKey().toString())
					.build());

			serialNumber++;
		}
		
		if (CollectionUtils.isEmpty(response)) {

			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("ULBs").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Total Collection").value(BigDecimal.ZERO).symbol("amount").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());
		}

		return response;

	}

}
