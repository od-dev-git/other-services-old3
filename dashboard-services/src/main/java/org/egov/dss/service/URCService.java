package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.BillDetail;
import org.egov.dss.model.DemandSearchCriteria;
import org.egov.dss.model.IncentiveAnalysis;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.Payment;
import org.egov.dss.model.PaymentDetail;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.model.TargetSearchCriteria;
import org.egov.dss.model.UrcSearchCriteria;
import org.egov.dss.model.UserSearchCriteria;
import org.egov.dss.model.WaterSearchCriteria;
import org.egov.dss.repository.PaymentRepository;
import org.egov.dss.repository.URCRepository;
import org.egov.dss.util.DashboardUtility;
import org.egov.dss.util.DashboardUtils;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.egov.dss.web.model.User;
import org.egov.dss.model.enums.PaymentStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class URCService {

	@Autowired
	private URCRepository urcRepository;

	@Autowired
	private DashboardUtils dashboardUtils;
	
	@Autowired
	CalculatorService calculatorService;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
		
	private PaymentSearchCriteria getPaymentSearchCriteria(PayloadDetails payloadDetails) {
		PaymentSearchCriteria criteria = new PaymentSearchCriteria();
		List<String> urcUlb = DashboardUtility.getSystemProperties().getUrculbs();
		
		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_URC))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.URC_REVENUE_ALL_BS));
			else if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_PT))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.PT_REVENUE_ALL_BS));
			else if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.BUSINESS_SERVICE_WS))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.WS_REVENUE_ALL_BS));
			else
				criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
		}

		if (StringUtils.hasText(payloadDetails.getTenantid())) {
			if (urcUlb.contains(payloadDetails.getTenantid())) {
				criteria.setTenantIds(Sets.newHashSet(payloadDetails.getTenantid()));
			} else {
				criteria.setTenantIds(Sets.newHashSet("od.odisha"));
			}
		} else {
			criteria.setTenantIds(Sets.newHashSet(urcUlb));
		}

		if (payloadDetails.getStartdate() != null && payloadDetails.getStartdate() != 0) {
			criteria.setFromDate(payloadDetails.getStartdate());
		}

		if (payloadDetails.getEnddate() != null && payloadDetails.getEnddate() != 0) {
			criteria.setToDate(payloadDetails.getEnddate());
		}

		return criteria;
	}

	private DemandSearchCriteria getDemandSearchCriteria(PayloadDetails payloadDetails) {
		DemandSearchCriteria criteria = new DemandSearchCriteria();
		List<String> urcUlb = DashboardUtility.getSystemProperties().getUrculbs();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_URC))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.URC_REVENUE_ALL_BS));
			else
				criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
		}

		if (StringUtils.hasText(payloadDetails.getTenantid())) {
			if (urcUlb.contains(payloadDetails.getTenantid())) {
				criteria.setTenantIds(Sets.newHashSet(payloadDetails.getTenantid()));
			} else {
				criteria.setTenantIds(Sets.newHashSet("od.odisha"));
			}
		} else {
			criteria.setTenantIds(Sets.newHashSet(urcUlb));
		}

		if (StringUtils.hasText(payloadDetails.getTimeinterval())) {
			criteria.setFinancialYear(payloadDetails.getTimeinterval());
		}

		return criteria;
	}
	
	public UrcSearchCriteria getUrcSearchCriteria(PayloadDetails payloadDetails) {
		UrcSearchCriteria criteria = new UrcSearchCriteria();
		List<String> urcUlb = DashboardUtility.getSystemProperties().getUrculbs();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
		}

		if (StringUtils.hasText(payloadDetails.getTenantid())) {
			if (urcUlb.contains(payloadDetails.getTenantid())) {
				criteria.setTenantIds(Sets.newHashSet(payloadDetails.getTenantid()));
			} else {
				criteria.setTenantIds(Sets.newHashSet("od.odisha"));
			}
		} else {
			criteria.setTenantIds(Sets.newHashSet(urcUlb));
		}

		if (payloadDetails.getStartdate() != null && payloadDetails.getStartdate() != 0) {
			criteria.setFromDate(payloadDetails.getStartdate());
		}

		if (payloadDetails.getEnddate() != null && payloadDetails.getEnddate() != 0) {
			criteria.setToDate(payloadDetails.getEnddate());
		}

		return criteria;
	}
	
	public PropertySerarchCriteria getPropertySearchCriteria(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = new PropertySerarchCriteria();
		List<String> urcUlb = DashboardUtility.getSystemProperties().getUrculbs();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
		}
		
		if (StringUtils.hasText(payloadDetails.getTenantid())) {
			if (urcUlb.contains(payloadDetails.getTenantid())) {
				criteria.setTenantIds(Sets.newHashSet(payloadDetails.getTenantid()));
			} else {
				criteria.setTenantIds(Sets.newHashSet("od.odisha"));
			}
		} else {
			criteria.setTenantIds(Sets.newHashSet(urcUlb));
		}
		
		if(payloadDetails.getStartdate() != null && payloadDetails.getStartdate() != 0) {
			criteria.setFromDate(payloadDetails.getStartdate());
		}
		
		if(payloadDetails.getEnddate() != null && payloadDetails.getEnddate() != 0) {
			criteria.setToDate(payloadDetails.getEnddate());
		}
		
		return criteria;
	}
	
	public WaterSearchCriteria getWaterSearchCriteria(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = new WaterSearchCriteria();
		List<String> urcUlb = DashboardUtility.getSystemProperties().getUrculbs();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
		}

		if (StringUtils.hasText(payloadDetails.getTenantid())) {
			if (urcUlb.contains(payloadDetails.getTenantid())) {
				criteria.setTenantIds(Sets.newHashSet(payloadDetails.getTenantid()));
			} else {
				criteria.setTenantIds(Sets.newHashSet("od.odisha"));
			}
		} else {
			criteria.setTenantIds(Sets.newHashSet(urcUlb));
		}

		if (payloadDetails.getStartdate() != null && payloadDetails.getStartdate() != 0) {
			criteria.setFromDate(payloadDetails.getStartdate());
		}

		if (payloadDetails.getEnddate() != null && payloadDetails.getEnddate() != 0) {
			criteria.setToDate(payloadDetails.getEnddate());
		}

		return criteria;
	}
	
	private TargetSearchCriteria getTargetSearchCriteria(PayloadDetails payloadDetails) {
		TargetSearchCriteria criteria = new TargetSearchCriteria();
		List<String> urcUlb = DashboardUtility.getSystemProperties().getUrculbs();
		
		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_URC))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.MODULE_LEVEL_PT,DashboardConstants.BUSINESS_SERVICE_WS));
			else
				criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
		}
		
		if (StringUtils.hasText(payloadDetails.getTenantid())) {
			if (urcUlb.contains(payloadDetails.getTenantid())) {
				criteria.setTenantIds(Sets.newHashSet(payloadDetails.getTenantid()));
			} else {
				criteria.setTenantIds(Sets.newHashSet("od.odisha"));
			}
		} else {
			criteria.setTenantIds(Sets.newHashSet(urcUlb));
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

	public List<Data> urcTotalCollection(PayloadDetails payloadDetails) {
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		BigDecimal previousTotalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		if(previousTotalCollection == BigDecimal.ZERO)
			previousTotalCollection = BigDecimal.ONE;
		BigDecimal changeInCollection = totalCollection.divide(previousTotalCollection, 2, BigDecimal.ROUND_HALF_UP)
				.subtract(BigDecimal.ONE).multiply(new BigDecimal(100));
		return Arrays.asList(Data.builder().headerValue(
				calculatePercentageValue(dashboardUtils.addDenominationForAmount(totalCollection), changeInCollection))
				.build());
	}
	
	public List<Data> ptTotalCollection(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		BigDecimal previousTotalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		if (previousTotalCollection.compareTo(BigDecimal.ZERO) == 0
				|| previousTotalCollection.compareTo(new BigDecimal("0.00")) == 0) {
			previousTotalCollection = BigDecimal.ONE;
		}
		BigDecimal changeInCollection = totalCollection.divide(previousTotalCollection, 2, BigDecimal.ROUND_HALF_UP)
				.subtract(BigDecimal.ONE).multiply(new BigDecimal(100));
		return Arrays.asList(Data.builder().headerValue(
				calculatePercentageValue(dashboardUtils.addDenominationForAmount(totalCollection), changeInCollection))
				.build());
	}
	
	private void postEnrichmentCollection(PayloadDetails payloadDetails, PaymentSearchCriteria paymentSearchCriteria) {
		if (!DashboardConstants.TIME_INTERVAL.contains(payloadDetails.getTimeinterval())) {
			paymentSearchCriteria.setFromDate(dashboardUtils.previousFYStartDate(payloadDetails.getTimeinterval()));
			paymentSearchCriteria.setToDate(dashboardUtils.previousFYEndDate(payloadDetails.getTimeinterval()));
		} else if (payloadDetails.getTimeinterval().equalsIgnoreCase(DashboardConstants.MONTH)) {
			paymentSearchCriteria.setFromDate(dashboardUtils.previousMonthStartDate());
			paymentSearchCriteria.setToDate(dashboardUtils.previousMonthEndDate());
		} else if (payloadDetails.getTimeinterval().equalsIgnoreCase(DashboardConstants.WEEK)) {
			paymentSearchCriteria.setFromDate(dashboardUtils.previousWeekStartDate());
			paymentSearchCriteria.setToDate(dashboardUtils.previousWeekEndDate());
		} else if (payloadDetails.getTimeinterval().equalsIgnoreCase(DashboardConstants.QUARTER)) {
			paymentSearchCriteria.setFromDate(dashboardUtils.previousQuarterStartDate());
			paymentSearchCriteria.setToDate(dashboardUtils.previousQuarterEndDate());
		}        
	}
	
	private String calculatePercentageValue(String amount, BigDecimal value) {
		return String.valueOf(amount+"("+value+"%)");
	}
	
	private String concatString(Integer amount, Integer value) {
		return String.valueOf(amount+"("+value+"%)");
	}

	public List<Data> wsTotalCollection(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
	    BigDecimal previousTotalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
	    if(previousTotalCollection == BigDecimal.ZERO)
			previousTotalCollection = BigDecimal.ONE;
		BigDecimal changeInCollection = totalCollection.divide(previousTotalCollection, 2, BigDecimal.ROUND_HALF_UP)
				.subtract(BigDecimal.ONE).multiply(new BigDecimal(100));
		return Arrays.asList(Data.builder().headerValue(
				calculatePercentageValue(dashboardUtils.addDenominationForAmount(totalCollection), changeInCollection))
				.build());
	}
	
	public List<Data> ulbsUnderUrc(PayloadDetails payloadDetails) {
		List<String> urcUlb = DashboardUtility.getSystemProperties().getUrculbs();
		return Arrays.asList(Data.builder().headerValue(urcUlb.size()).build());

	}
	
	public List<Data> jalsathiOnboarded(PayloadDetails payloadDetails) {
		UrcSearchCriteria urcSearchCriteria = getUrcSearchCriteria(payloadDetails);
		urcSearchCriteria.setFromDate(null);
		urcSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		urcSearchCriteria.setHrmsCode("SUJOG_JAL%");
		urcSearchCriteria.setIsActive(Boolean.TRUE);
		Integer jalSathiOnboarded = urcRepository.jalSathiOnboarded(urcSearchCriteria);
		return Arrays.asList(Data.builder().headerValue(jalSathiOnboarded).build());
	}
	
	public List<Data> totalPropertiesPaid(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PaymentSearchCriteria criteria = getPaymentSearchCriteria(payloadDetails);
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		Integer propertiesPaid = (Integer) urcRepository.getUrcPropertiesPaid(criteria);
        return Arrays.asList(Data.builder().headerValue(propertiesPaid).build());
	}
	
	public List<Data> totalWaterConsumerPaid(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		PaymentSearchCriteria criteria = getPaymentSearchCriteria(payloadDetails);
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		Integer waterConsumerPaid = (Integer) urcRepository.getUrcPropertiesPaid(criteria);
        return Arrays.asList(Data.builder().headerValue(waterConsumerPaid).build());
	}
	
	public List<Data> ptPaymentModeData(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		LinkedHashMap<String, BigDecimal> monthWiseTotalCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
				paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.CARD_PAYMENT));
		LinkedHashMap<String, BigDecimal> monthWiseCardCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.ONLINE_PAYMENT));
		LinkedHashMap<String, BigDecimal> monthWiseOnlineCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.CHEQUE_PAYMENT));
		LinkedHashMap<String, BigDecimal> monthWiseChequeCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.CASH_PAYMENT));
		LinkedHashMap<String, BigDecimal> monthWiseCashCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);

		LinkedHashMap<String, BigDecimal> monthWiseCardPercentage = new LinkedHashMap<>();
		LinkedHashMap<String, BigDecimal> monthWiseOnlinePercentage = new LinkedHashMap<>();
		LinkedHashMap<String, BigDecimal> monthWiseChequePercentage = new LinkedHashMap<>();
		LinkedHashMap<String, BigDecimal> monthWiseCashPercentage = new LinkedHashMap<>();

		monthWiseCardCollection.forEach((key, value) -> {
			BigDecimal collection = monthWiseTotalCollection.get(key) == BigDecimal.ZERO ? BigDecimal.ONE
					: monthWiseTotalCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			monthWiseCardPercentage.put(key, percentage);
		});
		monthWiseOnlineCollection.forEach((key, value) -> {
			BigDecimal collection = monthWiseTotalCollection.get(key) == BigDecimal.ZERO ? BigDecimal.ONE
					: monthWiseTotalCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			monthWiseOnlinePercentage.put(key, percentage);
		});
		monthWiseChequeCollection.forEach((key, value) -> {
			BigDecimal collection = monthWiseTotalCollection.get(key) == BigDecimal.ZERO ? BigDecimal.ONE
					: monthWiseTotalCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			monthWiseChequePercentage.put(key, percentage);
		});
		monthWiseCashCollection.forEach((key, value) -> {
			BigDecimal collection = monthWiseTotalCollection.get(key) == BigDecimal.ZERO ? BigDecimal.ONE
					: monthWiseTotalCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			monthWiseCashPercentage.put(key, percentage);
		});
		List<Data> response = new ArrayList<>();
		int serialNumber = 1;

		for (HashMap.Entry<String, BigDecimal> tenantWiseCollection : monthWiseTotalCollection.entrySet()) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("Months").label(tenantWiseCollection.getKey().toString()).symbol("text").build());

			plots.add(Plot.builder().name("Card Collection Amount")
					.value(monthWiseCardPercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : monthWiseCardPercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			plots.add(Plot.builder().name("Online Collection Amount")
					.value(monthWiseOnlinePercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : monthWiseOnlinePercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			plots.add(Plot.builder().name("Cheque Collection Amount")
					.value(monthWiseChequePercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : monthWiseChequePercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			plots.add(Plot.builder().name("Cash Collection Amount")
					.value(monthWiseCashPercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : monthWiseCashPercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			response.add(Data.builder().headerName(tenantWiseCollection.getKey()).plots(plots).headerValue(serialNumber).build());
			
			serialNumber++;

		}
		
		if (CollectionUtils.isEmpty(response)) {

			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("Months").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Card Collection Amount").value(BigDecimal.ZERO).symbol("percentage").build());

			plots.add(Plot.builder().name("Online Collection Amount").value(BigDecimal.ZERO).symbol("percentage").build());

			plots.add(Plot.builder().name("Cheque Collection Amount").value(BigDecimal.ZERO).symbol("percentage").build());

			plots.add(Plot.builder().name("Cash Collection Amount").value(BigDecimal.ZERO).symbol("percentage").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());
		}

		return response;
	}
	
	public List<Data> wsPaymentModeData(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);

		LinkedHashMap<String, BigDecimal> monthWiseTotalCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
				paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.CARD_PAYMENT));
		LinkedHashMap<String, BigDecimal> monthWiseCardCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.ONLINE_PAYMENT));
		LinkedHashMap<String, BigDecimal> monthWiseOnlineCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.CHEQUE_PAYMENT));
		LinkedHashMap<String, BigDecimal> monthWiseChequeCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setPaymentModes(Sets.newHashSet(DashboardConstants.CASH_PAYMENT));
		LinkedHashMap<String, BigDecimal> monthWiseCashCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);

		LinkedHashMap<String, BigDecimal> monthWiseCardPercentage = new LinkedHashMap<>();
		LinkedHashMap<String, BigDecimal> monthWiseOnlinePercentage = new LinkedHashMap<>();
		LinkedHashMap<String, BigDecimal> monthWiseChequePercentage = new LinkedHashMap<>();
		LinkedHashMap<String, BigDecimal> monthWiseCashPercentage = new LinkedHashMap<>();

		monthWiseCardCollection.forEach((key, value) -> {
			BigDecimal collection = monthWiseTotalCollection.get(key) == BigDecimal.ZERO ? BigDecimal.ONE
					: monthWiseTotalCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			monthWiseCardPercentage.put(key, percentage);
		});
		monthWiseOnlineCollection.forEach((key, value) -> {
			BigDecimal collection = monthWiseTotalCollection.get(key) == BigDecimal.ZERO ? BigDecimal.ONE
					: monthWiseTotalCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			monthWiseOnlinePercentage.put(key, percentage);
		});
		monthWiseChequeCollection.forEach((key, value) -> {
			BigDecimal collection = monthWiseTotalCollection.get(key) == BigDecimal.ZERO ? BigDecimal.ONE
					: monthWiseTotalCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			monthWiseChequePercentage.put(key, percentage);
		});
		monthWiseCashCollection.forEach((key, value) -> {
			BigDecimal collection = monthWiseTotalCollection.get(key) == BigDecimal.ZERO ? BigDecimal.ONE
					: monthWiseTotalCollection.get(key);
			BigDecimal percentage = value.multiply(new BigDecimal(100)).divide(collection, 2, RoundingMode.HALF_UP);
			monthWiseCashPercentage.put(key, percentage);
		});
		List<Data> response = new ArrayList<>();
		int serialNumber = 1;

		for (HashMap.Entry<String, BigDecimal> tenantWiseCollection : monthWiseTotalCollection.entrySet()) {
			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("Months").label(tenantWiseCollection.getKey().toString()).symbol("text").build());

			plots.add(Plot.builder().name("Card Collection Amount")
					.value(monthWiseCardPercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : monthWiseCardPercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			plots.add(Plot.builder().name("Online Collection Amount")
					.value(monthWiseOnlinePercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : monthWiseOnlinePercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			plots.add(Plot.builder().name("Cheque Collection Amount")
					.value(monthWiseChequePercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : monthWiseChequePercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			plots.add(Plot.builder().name("Cash Collection Amount")
					.value(monthWiseCashPercentage.get(tenantWiseCollection.getKey()) == null ? BigDecimal.ZERO : monthWiseCashPercentage.get(tenantWiseCollection.getKey())).symbol("percentage").build());

			response.add(Data.builder().headerName(tenantWiseCollection.getKey()).plots(plots).headerValue(serialNumber).build());
			
			serialNumber++;

		}
		
		if (CollectionUtils.isEmpty(response)) {

			List<Plot> plots = new ArrayList();
			plots.add(Plot.builder().name("S.N.").label(String.valueOf(serialNumber)).symbol("text").build());

			plots.add(Plot.builder().name("Months").label(payloadDetails.getTenantid()).symbol("text").build());

			plots.add(Plot.builder().name("Card Collection Amount").value(BigDecimal.ZERO).symbol("percentage").build());

			plots.add(Plot.builder().name("Online Collection Amount").value(BigDecimal.ZERO).symbol("percentage").build());

			plots.add(Plot.builder().name("Cheque Collection Amount").value(BigDecimal.ZERO).symbol("percentage").build());

			plots.add(Plot.builder().name("Cash Collection Amount").value(BigDecimal.ZERO).symbol("percentage").build());

			response.add(Data.builder().headerName(payloadDetails.getTenantid()).plots(plots).headerValue(serialNumber)
					.build());
		}

		return response;
	}	
	
	public List<Data> targetCollection(PayloadDetails payloadDetails) {
		String temp = payloadDetails.getTimeinterval();
		if(Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			payloadDetails.setTimeinterval(dashboardUtils.getCurrentFinancialYear());
		}
		TargetSearchCriteria targerSearchCriteria = getTargetSearchCriteria(payloadDetails);
		BigDecimal targetCollection = (BigDecimal) urcRepository.getTargetCollection(targerSearchCriteria);
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
	
	public List<Data> revenuePTTargetAchievement(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		List<Plot> plots = new ArrayList<Plot>();
		int serialNumber = 1;
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		BigDecimal targetCollection = (BigDecimal) targetCollection(payloadDetails).get(0).getHeaderValue();
		plots.add(Plot.builder().name(DashboardConstants.TARGET_COLLECTION).value(targetCollection)
				.label(String.valueOf(serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_COLLECTION).value(totalCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.PENDING_COLLECTION)
				.value(targetCollection.subtract(totalCollection)).label(String.valueOf(++serialNumber))
				.symbol("Amount").build());
		postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		BigDecimal previousTotalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		BigDecimal previousTargetCollection = (BigDecimal) targetCollection(payloadDetails).get(0).getHeaderValue();
		if (previousTargetCollection.compareTo(BigDecimal.ZERO) == 0
				|| previousTargetCollection.compareTo(new BigDecimal("0.00")) == 0) {
			previousTargetCollection = BigDecimal.ONE;
		}
		BigDecimal previousTargetAchieved = previousTotalCollection.divide(previousTargetCollection, 2, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(100));
		plots.add(Plot.builder().name(DashboardConstants.PREVIOUS_ACHIEVEMENT).value(previousTargetAchieved)
				.label(String.valueOf(++serialNumber)).symbol("Percentage").build());
		return Arrays.asList(
				Data.builder().headerName("DSS_URC_PT_TARGET_ACHIEVEMENT").plots(plots).build());
	}
	
	public BigDecimal totalDemand(PayloadDetails payloadDetails) {
		DemandSearchCriteria demandSearchCriteria = getDemandSearchCriteria(payloadDetails);
		demandSearchCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		BigDecimal totalDemand = BigDecimal.ZERO;
		BigDecimal currentDemand = BigDecimal.ZERO;
		BigDecimal arrearDemand = BigDecimal.ZERO;
		if (!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			currentDemand = (BigDecimal) currentDemand(payloadDetails).get(0).getHeaderValue();
			arrearDemand = (BigDecimal) arrearDemand(payloadDetails).get(0).getHeaderValue();
		}
		if (Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			payloadDetails.setTimeinterval(dashboardUtils.getCurrentFinancialYear());
			currentDemand = (BigDecimal) currentDemand(payloadDetails).get(0).getHeaderValue();
			arrearDemand = (BigDecimal) arrearDemand(payloadDetails).get(0).getHeaderValue();
		}

		totalDemand = currentDemand.add(arrearDemand);

		return totalDemand;
	}
	
	public List<Data> currentDemand(PayloadDetails payloadDetails) {
		DemandSearchCriteria demandSearchCriteria = getDemandSearchCriteria(payloadDetails);
		demandSearchCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		BigDecimal totalDemand = BigDecimal.ZERO;
		if (!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			totalDemand = urcRepository.getCurrentDemand(demandSearchCriteria);
		}
		return Arrays.asList(Data.builder().headerValue(totalDemand.setScale(2, RoundingMode.HALF_UP)).build());
	}

	public List<Data> arrearDemand(PayloadDetails payloadDetails) {
		DemandSearchCriteria demandSearchCriteria = getDemandSearchCriteria(payloadDetails);
		demandSearchCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		demandSearchCriteria.setIsArrearDemand(Boolean.TRUE);
		BigDecimal totalDemand = BigDecimal.ZERO;
		if (!Sets.newHashSet(DashboardConstants.TIME_INTERVAL).contains(payloadDetails.getTimeinterval())) {
			totalDemand = urcRepository.getArrearDemand(demandSearchCriteria);
		}
		return Arrays.asList(Data.builder().headerValue(totalDemand.setScale(2, RoundingMode.HALF_UP)).build());
	}
    
	public List<Data> ptDemandEfficiency(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);	
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		List<Plot> plots = new ArrayList<Plot>();
		int serialNumber = 1;
		BigDecimal totalDemand = totalDemand(payloadDetails);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		BigDecimal pendingCollection = totalDemand.subtract(totalCollection);
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_DEMAND).value(totalDemand)
				.label(String.valueOf(serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_COLLECTION).value(totalCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.PENDING_COLLECTION).value(pendingCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		BigDecimal previousTotalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		if (totalDemand.compareTo(BigDecimal.ZERO) == 0 || totalDemand.compareTo(new BigDecimal("0.00")) == 0) {
			totalDemand = BigDecimal.ONE;
		}
		payloadDetails.setTimeinterval(dashboardUtils.getPreviousFY());
		BigDecimal previousYearDemand = totalDemand(payloadDetails);
		if (previousYearDemand.compareTo(BigDecimal.ZERO) == 0
				|| previousYearDemand.compareTo(new BigDecimal("0.00")) == 0) {
			previousYearDemand = BigDecimal.ONE;
		}
		BigDecimal previousYearEfficiency = previousTotalCollection.divide(previousYearDemand, 2, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(100));
		plots.add(Plot.builder().name(DashboardConstants.PREVIOUS_EFFICIENCY).value(previousYearEfficiency)
				.label(String.valueOf(++serialNumber)).symbol("Percentage").build());
		return Arrays.asList(Data.builder().headerName("DSS_URC_PT_DEMAND_EFFICIENCY").plots(plots).build());
	}
	
	public List<Data> wsDemandEfficiency(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);	   
		paymentSearchCriteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		List<Plot> plots = new ArrayList<Plot>();
		int serialNumber = 1;
		BigDecimal totalDemand = totalDemand(payloadDetails);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		BigDecimal pendingCollection = totalDemand.subtract(totalCollection);
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_DEMAND).value(totalDemand)
				.label(String.valueOf(serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_COLLECTION).value(totalCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.PENDING_COLLECTION).value(pendingCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		BigDecimal previousTotalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		if (totalDemand.compareTo(BigDecimal.ZERO) == 0 || totalDemand.compareTo(new BigDecimal("0.00")) == 0) {
			totalDemand = BigDecimal.ONE;
		}
		payloadDetails.setTimeinterval(dashboardUtils.getPreviousFY());
		BigDecimal previousYearDemand = totalDemand(payloadDetails);
		if (previousYearDemand.compareTo(BigDecimal.ZERO) == 0
				|| previousYearDemand.compareTo(new BigDecimal("0.00")) == 0) {
			previousYearDemand = BigDecimal.ONE;
		}
		BigDecimal previousYearEfficiency = previousTotalCollection.divide(previousYearDemand, 2, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(100));
		plots.add(Plot.builder().name(DashboardConstants.PREVIOUS_EFFICIENCY_ACHIEVEMENT).value(previousYearEfficiency)
				.label(String.valueOf(++serialNumber)).symbol("Percentage").build());
		return Arrays.asList(Data.builder().headerName("DSS_URC_WS_DEMAND_EFFICIENCY").plots(plots).build());
	}
	
	public List<Data> topJalSathiPTCollection(PayloadDetails payloadDetails) {
		RequestInfo requestInfo = new RequestInfo();
		List<Data> response = new ArrayList<>();
		List<User> usersInfo = new ArrayList<User>();	
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);
		HashMap<String, BigDecimal> jalSathiWiseCollection = urcRepository
				.jalSathiWiseCollection(paymentSearchCriteria);
		Set<String> uuids = jalSathiWiseCollection.keySet();
		log.info("uuids : " + uuids);
		if(!CollectionUtils.isEmpty(uuids) && uuids != null) {
			UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder().uuid(uuids).build();
			usersInfo = urcRepository.getUserDetails(userSearchCriteria, requestInfo);		
			}	
		log.info("usersInfo : " + usersInfo);
		Map<String, String> jalSathiNameUuid = new HashMap<String, String>();
		usersInfo.forEach(user -> jalSathiNameUuid.put(user.getUuid(), user.getName()));
		Map<String, BigDecimal> resultMap = jalSathiNameUuid.entrySet().stream()
				.filter(entry -> jalSathiWiseCollection.containsKey(entry.getKey()))
				.collect(Collectors.toMap(entry -> entry.getValue().toString(), // Convert the key to String
						entry -> jalSathiWiseCollection.get(entry.getKey())));

		Map<String, BigDecimal> sortedMap = resultMap.entrySet().stream()
				.sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		resultMap.forEach((key, value) -> log.info(key + ": " + value));
		int rank = 0;
		for (Map.Entry<String, BigDecimal> tenantWisePercent : sortedMap.entrySet()) {
			rank++;
			List<Plot> plots = Arrays.asList(Plot.builder().label(String.valueOf(rank)).name(tenantWisePercent.getKey())
					.value(tenantWisePercent.getValue()).symbol("Amount").build());
			response.add(Data.builder().plots(plots).headerValue(rank).build());
		}
		
		if (CollectionUtils.isEmpty(response)) {
			List<Plot> plots = Arrays.asList(Plot.builder().label(String.valueOf("0")).name("N/A")
					.value(BigDecimal.ZERO).symbol("Amount").build());
			response.add(Data.builder().plots(plots).headerValue(rank).build());
		}
		
		return response;

	}
	
	public List<Data> topJalSathiWSCollection(PayloadDetails payloadDetails) {
		RequestInfo requestInfo = new RequestInfo();
		List<Data> response = new ArrayList<>();
		List<User> usersInfo = new ArrayList<User>();				
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);
		HashMap<String, BigDecimal> jalSathiWiseCollection = urcRepository
				.jalSathiWiseCollection(paymentSearchCriteria);
		Set<String> uuids = jalSathiWiseCollection.keySet();
		log.info("uuids : " + uuids);
		if(!CollectionUtils.isEmpty(uuids) && uuids != null) {
		UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder().uuid(uuids).build();
		usersInfo = urcRepository.getUserDetails(userSearchCriteria, requestInfo);		
		}	
		log.info("usersInfo : " + usersInfo);
		Map<String, String> jalSathiNameUuid = new HashMap<String, String>();
		usersInfo.forEach(user -> jalSathiNameUuid.put(user.getUuid(), user.getName()));
		Map<String, BigDecimal> resultMap = jalSathiNameUuid.entrySet().stream()
				.filter(entry -> jalSathiWiseCollection.containsKey(entry.getKey()))
				.collect(Collectors.toMap(entry -> entry.getValue().toString(), // Convert the key to String
						entry -> jalSathiWiseCollection.get(entry.getKey())));

		Map<String, BigDecimal> sortedMap = resultMap.entrySet().stream()
				.sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		resultMap.forEach((key, value) -> log.info(key + ": " + value));
		int rank = 0;
		for (Map.Entry<String, BigDecimal> tenantWisePercent : sortedMap.entrySet()) {
			rank++;
			List<Plot> plots = Arrays.asList(Plot.builder().label(String.valueOf(rank)).name(tenantWisePercent.getKey())
					.value(tenantWisePercent.getValue()).symbol("Amount").build());
			response.add(Data.builder().plots(plots).headerValue(rank).build());
		}
		
		if (CollectionUtils.isEmpty(response)) {
			List<Plot> plots = Arrays.asList(Plot.builder().label(String.valueOf("0")).name("N/A")
					.value(BigDecimal.ZERO).symbol("Amount").build());
			response.add(Data.builder().plots(plots).headerValue(rank).build());
		}
		
		return response;

	}
	
	public List<Data> servicePropertiesPaid(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		List<Plot> plots = new ArrayList<Plot>();
		int serialNumber = 1;
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		PropertySerarchCriteria propertySearchCriteria = getPropertySearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		propertySearchCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		propertySearchCriteria.setStatus(DashboardConstants.STATUS_ACTIVE);
		propertySearchCriteria.setFromDate(null);
		Integer propertiesPaid = (Integer) urcRepository.getUrcPropertiesPaid(paymentSearchCriteria);
		Integer totalProperties = urcRepository.getTotalPropertiesCount(propertySearchCriteria);
		Integer enrichTotalProperties = enrichProperties(payloadDetails, totalProperties);
		if (enrichTotalProperties == 0) {
			enrichTotalProperties = 1;
		}
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_PROPERTIES).value(new BigDecimal(enrichTotalProperties))
				.label(String.valueOf(serialNumber)).symbol("number").build());
		plots.add(Plot.builder().name(DashboardConstants.PROPERTIES_PAID).value(new BigDecimal(propertiesPaid))
				.label(String.valueOf(++serialNumber)).symbol("number").build());
		plots.add(Plot.builder().name(DashboardConstants.PROPERTIES_NOT_PAID)
				.value(new BigDecimal(enrichTotalProperties).subtract(new BigDecimal(propertiesPaid)))
				.label(String.valueOf(++serialNumber)).symbol("number").build());
		plots.add(Plot.builder().name(DashboardConstants.PROPERTIES_PAID)
				.value(new BigDecimal(propertiesPaid).multiply(new BigDecimal(100))
						.divide(new BigDecimal(enrichTotalProperties), RoundingMode.UP))
				.label(String.valueOf(++serialNumber)).symbol("number").build());

		return Arrays.asList(Data.builder().headerName("DSS_SERVICE_PROPERTIES_PAID").plots(plots).build());
	}
	
	public List<Data> serviceWaterConsumerPaid(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		List<Plot> plots = new ArrayList<Plot>();
		int serialNumber = 1;
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		WaterSearchCriteria waterSearchCriteria = getWaterSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		waterSearchCriteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		waterSearchCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		waterSearchCriteria.setIsOldApplication(Boolean.FALSE);
		waterSearchCriteria.setFromDate(null);
		Integer propertiesPaid = (Integer) urcRepository.getUrcPropertiesPaid(paymentSearchCriteria);
		Integer activeConnectionCount =  (Integer) urcRepository.getActiveWaterConnectionCount(waterSearchCriteria);
		Integer enrichTotalProperties = enrichProperties(payloadDetails, activeConnectionCount);
		if (enrichTotalProperties == 0) {
			enrichTotalProperties = 1;
		}
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_CONNECTIONS).value(new BigDecimal(enrichTotalProperties))
				.label(String.valueOf(serialNumber)).symbol("number").build());
		plots.add(Plot.builder().name(DashboardConstants.CONNECTIONS_PAID).value(new BigDecimal(propertiesPaid))
				.label(String.valueOf(++serialNumber)).symbol("number").build());
		plots.add(Plot.builder().name(DashboardConstants.CONNECTIONS_NOT_PAID)
				.value(new BigDecimal(enrichTotalProperties).subtract(new BigDecimal(propertiesPaid)))
				.label(String.valueOf(++serialNumber)).symbol("number").build());
		plots.add(Plot.builder().name(DashboardConstants.CONNECTIONS_PAID)
				.value(new BigDecimal(propertiesPaid).multiply(new BigDecimal(100))
						.divide(new BigDecimal(enrichTotalProperties), RoundingMode.UP))
				.label(String.valueOf(++serialNumber)).symbol("number").build());
		return Arrays.asList(Data.builder().headerName("DSS_SERVICE_WATER_CONSUMER_PAID").plots(plots).build());
	}

	private Integer enrichProperties(PayloadDetails payloadDetails, Integer totalProperties) {
		Integer finalTotalProperty = 0;
		if (payloadDetails.getTimeinterval().equals(DashboardConstants.DAY)) {
			finalTotalProperty = totalProperties / 365;
		} else if (payloadDetails.getTimeinterval().equals(DashboardConstants.WEEK)) {
			finalTotalProperty = totalProperties / 48;
		} else if (payloadDetails.getTimeinterval().equals(DashboardConstants.MONTH)) {
			finalTotalProperty = totalProperties / 12;
		} else if (payloadDetails.getTimeinterval().equals(DashboardConstants.QUARTER)) {
			finalTotalProperty = totalProperties / 4;
		} else
			finalTotalProperty = totalProperties;

		return finalTotalProperty;
	}
	
	public List<Data> propertiesCoverByJalsathi(PayloadDetails payloadDetails) {
		RequestInfo requestInfo = new RequestInfo();
		List<Data> response = new ArrayList<>();
		List<User> usersInfo = new ArrayList<User>();	
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);
		HashMap<String, BigDecimal> jalSathiWiseCollection = urcRepository
				.propertiesCoverByJalsathi(paymentSearchCriteria);
		Set<String> uuids = jalSathiWiseCollection.keySet();
		log.info("uuids : " + uuids);
		if(!CollectionUtils.isEmpty(uuids) && uuids != null) {
			UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder().uuid(uuids).build();
			usersInfo = urcRepository.getUserDetails(userSearchCriteria, requestInfo);		
			}	
		log.info("usersInfo : " + usersInfo);
		Map<String, String> jalSathiNameUuid = new HashMap<String, String>();
		usersInfo.forEach(user -> jalSathiNameUuid.put(user.getUuid(), user.getName()));
		Map<String, BigDecimal> resultMap = jalSathiNameUuid.entrySet().stream()
				.filter(entry -> jalSathiWiseCollection.containsKey(entry.getKey()))
				.collect(Collectors.toMap(entry -> entry.getValue().toString(), // Convert the key to String
						entry -> jalSathiWiseCollection.get(entry.getKey())));

		Map<String, BigDecimal> sortedMap = resultMap.entrySet().stream()
				.sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		resultMap.forEach((key, value) -> log.info(key + ": " + value));
		int rank = 0;
		for (Map.Entry<String, BigDecimal> tenantWisePercent : sortedMap.entrySet()) {
			rank++;
			List<Plot> plots = Arrays.asList(Plot.builder().label(String.valueOf(rank)).name(tenantWisePercent.getKey())
					.value(tenantWisePercent.getValue()).symbol("number").build());
			response.add(Data.builder().plots(plots).headerValue(rank).build());
		}
		
		if (CollectionUtils.isEmpty(response)) {
			List<Plot> plots = Arrays.asList(Plot.builder().label(String.valueOf("0")).name("N/A")
					.value(BigDecimal.ZERO).symbol("number").build());
			response.add(Data.builder().plots(plots).headerValue(rank).build());
		}
		
		return response;

	}
	
	public List<Data> topJalSathiUnifiedCollection(PayloadDetails payloadDetails) {
		RequestInfo requestInfo = new RequestInfo();
		List<Data> response = new ArrayList<>();
		List<User> usersInfo = new ArrayList<User>();	
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);
		HashMap<String, BigDecimal> jalSathiWiseCollection = urcRepository
				.jalSathiWiseCollection(paymentSearchCriteria);
		Set<String> uuids = jalSathiWiseCollection.keySet();
		log.info("uuids : " + uuids);
		if(!CollectionUtils.isEmpty(uuids) && uuids != null) {
			UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder().uuid(uuids).build();
			usersInfo = urcRepository.getUserDetails(userSearchCriteria, requestInfo);		
			}	
		log.info("usersInfo : " + usersInfo);
		Map<String, String> jalSathiNameUuid = new HashMap<String, String>();
		usersInfo.forEach(user -> jalSathiNameUuid.put(user.getUuid(), user.getName()));
		Map<String, BigDecimal> resultMap = jalSathiNameUuid.entrySet().stream()
				.filter(entry -> jalSathiWiseCollection.containsKey(entry.getKey()))
				.collect(Collectors.toMap(entry -> entry.getValue().toString(), // Convert the key to String
						entry -> jalSathiWiseCollection.get(entry.getKey())));

		Map<String, BigDecimal> sortedMap = resultMap.entrySet().stream()
				.sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		resultMap.forEach((key, value) -> log.info(key + ": " + value));
		int rank = 0;
		for (Map.Entry<String, BigDecimal> tenantWisePercent : sortedMap.entrySet()) {
			rank++;
			List<Plot> plots = Arrays.asList(Plot.builder().label(String.valueOf(rank)).name(tenantWisePercent.getKey())
					.value(tenantWisePercent.getValue()).symbol("Amount").build());
			response.add(Data.builder().plots(plots).headerValue(rank).build());
		}
		
		if (CollectionUtils.isEmpty(response)) {
			List<Plot> plots = Arrays.asList(Plot.builder().label(String.valueOf("0")).name("N/A")
					.value(BigDecimal.ZERO).symbol("Amount").build());
			response.add(Data.builder().plots(plots).headerValue(rank).build());
		}
		
		return response;

	}
	
	public List<Data> connectionPaid(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);	    
		List<Plot> plots = new ArrayList<Plot>();
		int serialNumber = 1;
		BigDecimal totalDemand = totalDemand(payloadDetails);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		BigDecimal pendingCollection = totalDemand.subtract(totalCollection);
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_DEMAND).value(totalDemand)
				.label(String.valueOf(serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_COLLECTION).value(totalCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.PENDING_COLLECTION).value(pendingCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		BigDecimal previousTotalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		if (totalDemand.compareTo(BigDecimal.ZERO) == 0 || totalDemand.compareTo(new BigDecimal("0.00")) == 0) {
			totalDemand = BigDecimal.ONE;
		}
		payloadDetails.setTimeinterval(dashboardUtils.getPreviousFY());
		BigDecimal previousYearDemand = totalDemand(payloadDetails);
		if (previousYearDemand.compareTo(BigDecimal.ZERO) == 0
				|| previousYearDemand.compareTo(new BigDecimal("0.00")) == 0) {
			previousYearDemand = BigDecimal.ONE;
		}
		BigDecimal previousYearEfficiency = previousTotalCollection.divide(previousYearDemand, 2, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(100));
		plots.add(Plot.builder().name(DashboardConstants.PREVIOUS_EFFICIENCY).value(previousYearEfficiency)
				.label(String.valueOf(++serialNumber)).symbol("Percentage").build());
		return Arrays.asList(Data.builder().headerName("DSS_URC_PT_DEMAND_EFFICIENCY").plots(plots).build());
	}
	
	public List<Data> monthwiseUnifiedCollection(PayloadDetails payloadDetails) {
		RequestInfo requestInfo = new RequestInfo();
		List<Data> response = new ArrayList<>();
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		LinkedHashMap<String, BigDecimal> monthWisePTCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
// 		Previous FY Collection
		BigDecimal previousFYPTCollection = getPreviousFYCollection(paymentSearchCriteria, payloadDetails);
		paymentSearchCriteria.setBusinessServices(Sets.newHashSet(DashboardConstants.WS_REVENUE_ALL_BS));
		LinkedHashMap<String, BigDecimal> monthWiseWSCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
		BigDecimal previousFYWSCollection = getPreviousFYCollection(paymentSearchCriteria, payloadDetails);
		if (previousFYWSCollection.compareTo(BigDecimal.ZERO) == 0
				|| previousFYWSCollection.compareTo(new BigDecimal("0.00")) == 0) {
			previousFYWSCollection = BigDecimal.ONE;
		}
		int serialNumber = 1;
		for (Map.Entry<String, BigDecimal> monthWiseCollection : monthWisePTCollection.entrySet()) {
			List<Plot> plots = new ArrayList<>();
			monthWiseCollection.getValue().divide(previousFYWSCollection, 2, BigDecimal.ROUND_HALF_UP)
					.subtract(BigDecimal.ONE);
			plots.add(Plot.builder().name(DashboardConstants.PROPERTY_TAX).value(monthWiseCollection.getValue())
					.symbol("Amount")
					.strValue(monthWiseCollection.getValue()
							.divide((previousFYPTCollection == BigDecimal.ZERO ? BigDecimal.ONE
									: previousFYPTCollection), 2, BigDecimal.ROUND_HALF_UP)
							.subtract(BigDecimal.ONE).toString())
					.build());
			plots.add(Plot.builder().name(DashboardConstants.WATER_SEWARAGE_CHARGES)
					.value(monthWiseWSCollection.get(monthWiseCollection.getKey())).symbol("Amount")
					.strValue(monthWiseCollection.getValue()
							.divide((previousFYWSCollection == BigDecimal.ZERO ? BigDecimal.ONE
									: previousFYWSCollection), 2, BigDecimal.ROUND_HALF_UP)
							.subtract(BigDecimal.ONE).toString())
					.build());
			plots.add(Plot.builder().name(DashboardConstants.TOTAL_COLLECTION)
					.value(monthWiseWSCollection.get(monthWiseCollection.getKey()).add(monthWiseCollection.getValue()))
					.symbol("Amount").build());
			response.add(Data.builder().headerName(monthWiseCollection.getKey()).plots(plots).headerValue(serialNumber)
					.headerSymbol("Amount").build());
			++serialNumber;
		}

		return response;

	}

	private BigDecimal getPreviousFYCollection(PaymentSearchCriteria paymentSearchCriteria,
			PayloadDetails payloadDetails) {
		PaymentSearchCriteria criteria = new PaymentSearchCriteria();
		String previousFy = dashboardUtils.getPreviousFY();
		criteria.setFromDate(dashboardUtils.previousFYStartDate(previousFy));
		criteria.setToDate(dashboardUtils.previousFYEndDate(previousFy));
		criteria.setTenantIds(paymentSearchCriteria.getTenantIds());
		criteria.setBusinessServices(paymentSearchCriteria.getBusinessServices());
		criteria.setExcludedTenant(paymentSearchCriteria.getExcludedTenant());
		criteria.setStatus(paymentSearchCriteria.getStatus());
		criteria.setStatusNotIn(paymentSearchCriteria.getStatusNotIn());
		BigDecimal previousTotalCollection = (BigDecimal) urcRepository.getTotalCollection(criteria);
		return previousTotalCollection;

	}
	
	public List<Data> urcDemandEfficiency(PayloadDetails payloadDetails) {
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);		
		List<Plot> plots = new ArrayList<Plot>();
		int serialNumber = 1;
		BigDecimal totalDemand = totalDemand(payloadDetails);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		BigDecimal pendingCollection = totalDemand.subtract(totalCollection);
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_DEMAND).value(totalDemand)
				.label(String.valueOf(serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_COLLECTION).value(totalCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.PENDING_COLLECTION).value(pendingCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		BigDecimal previousTotalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		if (totalDemand.compareTo(BigDecimal.ZERO) == 0 || totalDemand.compareTo(new BigDecimal("0.00")) == 0) {
			totalDemand = BigDecimal.ONE;
		}
		payloadDetails.setTimeinterval(dashboardUtils.getPreviousFY());
		BigDecimal previousYearDemand = totalDemand(payloadDetails);
		if (previousYearDemand.compareTo(BigDecimal.ZERO) == 0
				|| previousYearDemand.compareTo(new BigDecimal("0.00")) == 0) {
			previousYearDemand = BigDecimal.ONE;
		}
		BigDecimal previousYearEfficiency = previousTotalCollection.divide(previousYearDemand, 2, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(100));
		plots.add(Plot.builder().name(DashboardConstants.PREVIOUS_EFFICIENCY).value(previousYearEfficiency)
				.label(String.valueOf(++serialNumber)).symbol("Percentage").build());
		return Arrays.asList(Data.builder().headerName("DSS_URC_DEMAND_EFFICIENCY").plots(plots).build());
	}
	
	public List<Data> activeJalSathi(PayloadDetails payloadDetails) {
		PaymentSearchCriteria criteria = getPaymentSearchCriteria(payloadDetails);
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		criteria.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		criteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		criteria.setFromDate(dashboardUtils.getLastThirtyDayValue());
		criteria.setToDate(System.currentTimeMillis());		
		criteria.setIsJalSathi(Boolean.TRUE);
		Integer propertiesPaid = (Integer) urcRepository.activeJalsathi(criteria);
        return Arrays.asList(Data.builder().headerValue(propertiesPaid).build());
	}
	
	public List<Data> propertiesCoveredByJalsathi(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		PropertySerarchCriteria propertySearchCriteria = getPropertySearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);

		propertySearchCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		propertySearchCriteria.setStatus(DashboardConstants.STATUS_ACTIVE);
		propertySearchCriteria.setFromDate(null);
		Integer propertiesPaid = (Integer) urcRepository.propertyCoveredByJalsathi(paymentSearchCriteria);
		Integer totalProperties = urcRepository.getTotalPropertiesCount(propertySearchCriteria);
		if (totalProperties == 0)
			totalProperties = 1;
		BigDecimal propertyPaidRatio = BigDecimal.valueOf(propertiesPaid).multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(totalProperties), 2, BigDecimal.ROUND_HALF_UP);
		return Arrays.asList(Data.builder()
				.headerValue(calculatePercentageValue(String.valueOf(propertiesPaid), propertyPaidRatio)).build());
	}

	public List<Data> jalsathiPTCollection(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);
		BigDecimal totalJalSathiCollection = (BigDecimal) urcRepository.colletionByJalSathi(paymentSearchCriteria);
		// postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		if (totalCollection.compareTo(BigDecimal.ZERO) == 0 || totalCollection.compareTo(new BigDecimal("0.00")) == 0) {
			totalCollection = BigDecimal.ONE;
		}
		BigDecimal percent = totalJalSathiCollection.divide(totalCollection, 2, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(100));
		return Arrays.asList(Data.builder()
				.headerValue(
						calculatePercentageValue(dashboardUtils.addDenominationForAmount(totalJalSathiCollection), percent))
				.build());
	}
	
	public List<Data> waterConnectionCoveredByJalsathi(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		WaterSearchCriteria waterSearchCriteria = getWaterSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);

		waterSearchCriteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		waterSearchCriteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		waterSearchCriteria.setFromDate(null);
		Integer waterConsumerPaid = (Integer) urcRepository.propertyCoveredByJalsathi(paymentSearchCriteria);
		Integer totalWaterConsumer = (Integer) urcRepository.getActiveWaterConnectionCount(waterSearchCriteria);
		if (totalWaterConsumer == 0)
			totalWaterConsumer = 1;
		BigDecimal propertyPaidRatio = BigDecimal.valueOf(waterConsumerPaid).multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(totalWaterConsumer), 2, BigDecimal.ROUND_HALF_UP);
		return Arrays.asList(Data.builder()
				.headerValue(calculatePercentageValue(String.valueOf(waterConsumerPaid), propertyPaidRatio)).build());
	}
	
	public List<Data> jalsathiWSCollection(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);
		BigDecimal totalJalSathiCollection = (BigDecimal) urcRepository.colletionByJalSathi(paymentSearchCriteria);
		// postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		if (totalCollection.compareTo(BigDecimal.ZERO) == 0 || totalCollection.compareTo(new BigDecimal("0.00")) == 0) {
			totalCollection = BigDecimal.ONE;
		}
		BigDecimal percent = totalJalSathiCollection.divide(totalCollection, 2, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(100));
		return Arrays.asList(Data.builder()
				.headerValue(
						calculatePercentageValue(dashboardUtils.addDenominationForAmount(totalJalSathiCollection), percent))
				.build());
	}

	public List<Data> jalsathiTotalCollection(PayloadDetails payloadDetails) {
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);
		BigDecimal totalJalSathiCollection = (BigDecimal) urcRepository.colletionByJalSathi(paymentSearchCriteria);
		// postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		if (totalCollection.compareTo(BigDecimal.ZERO) == 0 || totalCollection.compareTo(new BigDecimal("0.00")) == 0) {
			totalCollection = BigDecimal.ONE;
		}
		BigDecimal percent = totalJalSathiCollection.divide(totalCollection, 2, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(100));
		return Arrays.asList(Data.builder()
				.headerValue(
						calculatePercentageValue(dashboardUtils.addDenominationForAmount(totalJalSathiCollection), percent))
				.build());
	}
	
	public List<Data> collectorWiseRevenue(PayloadDetails payloadDetails) {
		List<Plot> plots = new ArrayList<Plot>();
		int serialNumber = 1;
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		BigDecimal totalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);
		BigDecimal totalJalSathiCollection = (BigDecimal) urcRepository.colletionByJalSathi(paymentSearchCriteria);
		BigDecimal totalCollectionByOther = totalCollection.subtract(totalJalSathiCollection);
		// postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		if (totalCollection.compareTo(BigDecimal.ZERO) == 0 || totalCollection.compareTo(new BigDecimal("0.00")) == 0) {
			totalCollection = BigDecimal.ONE;
		}
		BigDecimal percent = totalJalSathiCollection.divide(totalCollection, 2, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(100));
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_COLLECTION).value(totalCollection)
				.label(String.valueOf(serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_COLLECTION_BY_JALSATHI).value(totalJalSathiCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_COLLECTION_BY_OTHERS).value(totalCollectionByOther)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.JALSATHI_COLLECTION_ACHIEVEMENT).value(percent)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());

		return Arrays.asList(Data.builder().headerName("DSS_URC_COLLECTOR_WISE_REVENUE").plots(plots).build());
	}  

 /*	
  * public List<Data> jalsathiPTIncentives(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		BigDecimal totalPtIncentive = BigDecimal.ZERO;
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		Map<String, IncentiveAnalysis> incentiveAnalysis = new HashMap<>();
		List<Payment> payments = urcRepository.fetchPayments(paymentSearchCriteria);
		System.out.println("payments : " + payments);
		prepareCollectionReport(payments, incentiveAnalysis);
		calculatorService.calculateIncentives(payloadDetails.getModulelevel(), incentiveAnalysis);
		log.info("incentiveAnalysis : " + incentiveAnalysis);
		for (Map.Entry<String, IncentiveAnalysis> incentive : incentiveAnalysis.entrySet()) {
			totalPtIncentive = totalPtIncentive.add(incentive.getValue().getTotalIncentive()).setScale(2,
					BigDecimal.ROUND_HALF_UP);
		}
		log.info("Total PT Incentive : " + totalPtIncentive);

		return Arrays.asList(Data.builder().headerValue(totalPtIncentive).headerName("DSS_JALSATHI_PT_INCENTIVES").build());
	}

	public List<Data> jalsathiWSIncentives(PayloadDetails payloadDetails) {
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		BigDecimal totalWSIncentive = BigDecimal.ZERO;
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		Map<String, IncentiveAnalysis> incentiveAnalysis = new HashMap<>();
		List<Payment> payments = urcRepository.fetchPayments(paymentSearchCriteria);
		prepareCollectionReport(payments, incentiveAnalysis);
		calculatorService.calculateIncentives(payloadDetails.getModulelevel(), incentiveAnalysis);
		log.info("incentiveAnalysis : " + incentiveAnalysis);
		for (Map.Entry<String, IncentiveAnalysis> incentive : incentiveAnalysis.entrySet()) {
			totalWSIncentive = totalWSIncentive.add(incentive.getValue().getTotalIncentive()).setScale(2,
					BigDecimal.ROUND_HALF_UP);
		}
		log.info("Total WS Incentive : " + totalWSIncentive);
		
		return Arrays.asList(Data.builder().headerValue(totalWSIncentive).headerName("DSS_JALSATHI_WS_INCENTIVES").build());
	}
	
	*/
	
	/*
	 * public List<Data> jalsathiPTIncentivesBkp(PayloadDetails payloadDetails) {
	 * payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
	 * PaymentSearchCriteria paymentSearchCriteria =
	 * getPaymentSearchCriteria(payloadDetails); paymentSearchCriteria
	 * .setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED,
	 * DashboardConstants.STATUS_DISHONOURED));
	 * paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
	 * setFromAndToDateInIST(paymentSearchCriteria); BigDecimal currentCollection =
	 * (BigDecimal) paymentRepository.getCurrentCollection(paymentSearchCriteria);
	 * BigDecimal arrearCollection = (BigDecimal)
	 * paymentRepository.getArrearCollection(paymentSearchCriteria); BigDecimal
	 * totalCollection = (BigDecimal)
	 * paymentRepository.getTotalCollection(paymentSearchCriteria);
	 * log.info("PT currentCollection: " + currentCollection + " arrearCollection: "
	 * + arrearCollection + " totalCollection: " + totalCollection); BigDecimal
	 * totalPropertyIncentive =
	 * calculatorService.calculateIncentives(payloadDetails.getModulelevel(),
	 * currentCollection, arrearCollection, totalCollection);
	 * log.info("Total PT Incentive : " + totalPropertyIncentive); return
	 * Arrays.asList(Data.builder().headerName("DSS_JALSATHI_PT_INCENTIVES")
	 * .headerValue(dashboardUtils.addDenominationForAmount(totalPropertyIncentive))
	 * .build()); }
	 */

	
	/*
	 * public List<Data> jalsathiWSIncentivesBkp(PayloadDetails payloadDetails) {
	 * payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
	 * PaymentSearchCriteria paymentSearchCriteria =
	 * getPaymentSearchCriteria(payloadDetails); paymentSearchCriteria
	 * .setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED,
	 * DashboardConstants.STATUS_DISHONOURED));
	 * paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
	 * setFromAndToDateInIST(paymentSearchCriteria); BigDecimal currentCollection =
	 * (BigDecimal) paymentRepository.getCurrentCollection(paymentSearchCriteria);
	 * BigDecimal arrearCollection = (BigDecimal)
	 * paymentRepository.getArrearCollection(paymentSearchCriteria); BigDecimal
	 * totalCollection = (BigDecimal)
	 * paymentRepository.getTotalCollection(paymentSearchCriteria);
	 * log.info("WS currentCollection: " + currentCollection + " arrearCollection: "
	 * + arrearCollection + " totalCollection: " + totalCollection); BigDecimal
	 * totalWSIncentive =
	 * calculatorService.calculateIncentives(payloadDetails.getModulelevel(),
	 * currentCollection, arrearCollection, totalCollection);
	 * log.info("Total WS Incentive : " + totalWSIncentive); return
	 * Arrays.asList(Data.builder().headerName("DSS_JALSATHI_WS_INCENTIVES")
	 * .headerValue(dashboardUtils.addDenominationForAmount(totalWSIncentive)).build
	 * ()); }
	 */
    
	private void prepareCollectionReport(List<Payment> payments, Map<String, IncentiveAnalysis> incentiveAnalysis) {

		for (Payment payment : payments) {
			IncentiveAnalysis incentive = incentiveAnalysis.get(payment.getAuditDetails().getCreatedBy());
			if (incentive == null) {
				incentive = IncentiveAnalysis.builder().empId(payment.getAuditDetails().getCreatedBy()).build();
				incentiveAnalysis.put(payment.getAuditDetails().getCreatedBy(), incentive);
			}
			incentive.setTotalNoOfTransaction(incentive.getTotalNoOfTransaction() + 1);
			incentive.setTotalCollection(incentive.getTotalCollection().add(payment.getTotalAmountPaid()));

			BigDecimal arrearCollected = getColletdArrearAmount(payment);
			incentive.setCollectionTowardsArrear(incentive.getCollectionTowardsArrear().add(arrearCollected));
			incentive.setCollectionTowardsCurrent(incentive.getCollectionTowardsCurrent()
					.add(payment.getTotalAmountPaid().subtract(arrearCollected)));
		}
	}

	private BigDecimal getColletdArrearAmount(Payment payment) {

		Comparator<BillDetail> billDetailComparator = (obj1, obj2) -> obj2.getFromPeriod()
				.compareTo(obj1.getFromPeriod());
		BigDecimal arrearCollected = BigDecimal.ZERO;

		for (PaymentDetail pd : payment.getPaymentDetails()) {
			Collections.sort(pd.getBill().getBillDetails(), billDetailComparator);
			for (int i = 0; i < pd.getBill().getBillDetails().size(); i++) {
				if (i != 0) {
					arrearCollected = arrearCollected.add(pd.getBill().getBillDetails().get(i).getAmountPaid());
				}
			}
		}

		return arrearCollected;
	}
	
	public List<Data> jalsathiContributionToPT(PayloadDetails payloadDetails) {
		List<Data> response = new ArrayList<>();
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		LinkedHashMap<String, BigDecimal> monthWiseTotalCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);
		List<LinkedHashMap<String, Object>> monthWiseJalsathiContribution = urcRepository
				.getMonthWiseJalsathiCollection(paymentSearchCriteria);
		int serailNumber = 0;
		for (HashMap<String, Object> jalsathiContribution : monthWiseJalsathiContribution) {
			serailNumber++;
			List<Plot> row = new ArrayList<>();
			String monthName = String.valueOf(jalsathiContribution.get("name"));

			row.add(Plot.builder().label(String.valueOf(serailNumber)).name("S.N.").symbol("text").build());

			row.add(Plot.builder().label(monthName).name("Months").symbol("text").build());

			BigDecimal activeJalSathi = new BigDecimal(String.valueOf(jalsathiContribution.get("activejalsathi")));

			if (activeJalSathi.compareTo(BigDecimal.ZERO) == 0)
				activeJalSathi = BigDecimal.ONE;

			BigDecimal totalPTCollection = monthWiseTotalCollection.get(monthName);
			if (totalPTCollection.compareTo(BigDecimal.ZERO) == 0)
				totalPTCollection = BigDecimal.ONE;
			row.add(Plot.builder().name("Active Jal Saathis").value(activeJalSathi).symbol("number").build());

			row.add(Plot.builder().name("Property Tax collected by Jal Saathis (Rs)")
					.value(new BigDecimal(String.valueOf(jalsathiContribution.get("value")))).symbol("Amount").build());

			row.add(Plot.builder().name("Average Property tax collected per Jal Saathi (Rs)")
					.value(new BigDecimal(String.valueOf(jalsathiContribution.get("value"))).divide(activeJalSathi, 2,
							BigDecimal.ROUND_HALF_UP))
					.symbol("Amount").build());

			row.add(Plot.builder().name("Share in total property tax collected")
					.value(new BigDecimal(String.valueOf(jalsathiContribution.get("value")))
							.divide(totalPTCollection, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)))
					.symbol("percentage").build());

			row.add(Plot.builder().name("Units Covered by Jal Saathis")
					.value(new BigDecimal(String.valueOf(jalsathiContribution.get("consumercode")))).symbol("number")
					.build());

			row.add(Plot.builder().name("Average units covered per Jal Saathi")
					.value(new BigDecimal(String.valueOf(jalsathiContribution.get("consumercode")))
							.divide(activeJalSathi, 2, BigDecimal.ROUND_HALF_UP))
					.symbol("number").build());

			response.add(
					Data.builder().headerName(monthName).headerValue(serailNumber).plots(row).insight(null).build());
		}
		return response;
	}
	
	public List<Data> jalsathiContributionToWS(PayloadDetails payloadDetails) {
		List<Data> response = new ArrayList<>();
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		paymentSearchCriteria
				.setStatus(Sets.newHashSet(DashboardConstants.STATUS_CANCELLED, DashboardConstants.STATUS_DISHONOURED));
		paymentSearchCriteria.setExcludedTenant(DashboardConstants.TESTING_TENANT);
		LinkedHashMap<String, BigDecimal> monthWiseTotalCollection = urcRepository
				.getMonthWiseCollection(paymentSearchCriteria);
		paymentSearchCriteria.setIsJalSathi(Boolean.TRUE);
		List<LinkedHashMap<String, Object>> monthWiseJalsathiContribution = urcRepository
				.getMonthWiseJalsathiCollection(paymentSearchCriteria);
		int serailNumber = 0;
		for (HashMap<String, Object> jalsathiContribution : monthWiseJalsathiContribution) {
			serailNumber++;
			List<Plot> row = new ArrayList<>();
			String monthName = String.valueOf(jalsathiContribution.get("name"));

			row.add(Plot.builder().label(String.valueOf(serailNumber)).name("S.N.").symbol("text").build());

			row.add(Plot.builder().label(monthName).name("Months").symbol("text").build());

			BigDecimal activeJalSathi = new BigDecimal(String.valueOf(jalsathiContribution.get("activejalsathi")));

			if (activeJalSathi.compareTo(BigDecimal.ZERO) == 0)
				activeJalSathi = BigDecimal.ONE;

			BigDecimal totalPTCollection = monthWiseTotalCollection.get(monthName);
			if (totalPTCollection.compareTo(BigDecimal.ZERO) == 0)
				totalPTCollection = BigDecimal.ONE;
			row.add(Plot.builder().name("Active Jal Saathis").value(activeJalSathi).symbol("number").build());

			row.add(Plot.builder().name("Water & Sewerage charges collected by Jal Saathis (Rs)")
					.value(new BigDecimal(String.valueOf(jalsathiContribution.get("value")))).symbol("Amount").build());

			row.add(Plot.builder().name("Average water & sewerage charges collected per Jal Saathi (Rs)")
					.value(new BigDecimal(String.valueOf(jalsathiContribution.get("value"))).divide(activeJalSathi, 2,
							BigDecimal.ROUND_HALF_UP))
					.symbol("Amount").build());

			row.add(Plot.builder().name("Share in total water & sewerage charge collections")
					.value(new BigDecimal(String.valueOf(jalsathiContribution.get("value")))
							.divide(totalPTCollection, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)))
					.symbol("percentage").build());

			row.add(Plot.builder().name("Units Covered by Jal Saathis")
					.value(new BigDecimal(String.valueOf(jalsathiContribution.get("consumercode")))).symbol("number")
					.build());

			row.add(Plot.builder().name("Average units covered per Jal Saathi")
					.value(new BigDecimal(String.valueOf(jalsathiContribution.get("consumercode")))
							.divide(activeJalSathi, 2, BigDecimal.ROUND_HALF_UP))
					.symbol("number").build());

			response.add(
					Data.builder().headerName(monthName).headerValue(serailNumber).plots(row).insight(null).build());
		}
		return response;
	}
	
	private void setFromAndToDateInIST(PaymentSearchCriteria paymentSearchCriteria) {
		Long startTimeMillis = paymentSearchCriteria.getFromDate();

		ZonedDateTime startIST = Instant.ofEpochMilli(startTimeMillis).atZone(ZoneId.of("Asia/Kolkata"));
		ZonedDateTime endIST = startIST.plusYears(1).minusNanos(1);
		Long endMillisIST = endIST.toInstant().toEpochMilli();

		paymentSearchCriteria.setToDate(endMillisIST);
	}

	public List<Data> jalsathiPTIncentives(PayloadDetails payloadDetails) {
		RequestInfo requestInfo = new RequestInfo();
		BigDecimal totalPtIncentives = BigDecimal.ZERO;
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		Long count = urcRepository.getPaymentsCount(requestInfo, paymentSearchCriteria);
		Integer limit = 10000;
		Integer offset = 0;
		Map<String, IncentiveAnalysis> incentiveAnalysis = new HashMap<>();
		if (count > 0) {
			while (count > 0) {
				paymentSearchCriteria.setOffset(offset);
				paymentSearchCriteria.setLimit(limit);
				List<Payment> tempPayments = getPayments(requestInfo, paymentSearchCriteria);
				prepareCollectionReport(tempPayments, incentiveAnalysis);
				count = count - limit;
				offset += limit;
			}
		}
		calculatorService.calculateIncentives(DashboardConstants.MODULE_LEVEL_PT, incentiveAnalysis);
		totalPtIncentives = incentiveAnalysis.values().stream().map(IncentiveAnalysis::getTotalIncentive)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		log.info("Total PT Incentives : "+totalPtIncentives+" for interval :"+payloadDetails.getTimeinterval());

		return Arrays
				.asList(Data.builder().headerName("DSS_JALSATHI_PT_INCENTIVES").headerValue(totalPtIncentives).build());
	}
	
	public List<Data> jalsathiWSIncentives(PayloadDetails payloadDetails) {
		RequestInfo requestInfo = new RequestInfo();
		BigDecimal totalWsIncentives = BigDecimal.ZERO;
		payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
		PaymentSearchCriteria paymentSearchCriteria = getPaymentSearchCriteria(payloadDetails);
		Long count = urcRepository.getPaymentsCount(requestInfo, paymentSearchCriteria);
		Integer limit = 10000;
		Integer offset = 0;
		Map<String, IncentiveAnalysis> incentiveAnalysis = new HashMap<>();
		if (count > 0) {
			while (count > 0) {
				paymentSearchCriteria.setOffset(offset);
				paymentSearchCriteria.setLimit(limit);
				List<Payment> tempPayments = getPayments(requestInfo, paymentSearchCriteria);
				prepareCollectionReport(tempPayments, incentiveAnalysis);
				count = count - limit;
				offset += limit;
			}
		}
		calculatorService.calculateIncentives(DashboardConstants.BUSINESS_SERVICE_WS, incentiveAnalysis);
		totalWsIncentives = incentiveAnalysis.values().stream().map(IncentiveAnalysis::getTotalIncentive)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		log.info("Total WS Incentives : "+totalWsIncentives+" for interval :"+payloadDetails.getTimeinterval());

		return Arrays
				.asList(Data.builder().headerName("DSS_JALSATHI_WS_INCENTIVES").headerValue(totalWsIncentives).build());
	}

	public List<Payment> getPayments(@Valid RequestInfo requestInfo, PaymentSearchCriteria paymentSearchCriteria) {
		if (paymentSearchCriteria.getFromDate() != null && paymentSearchCriteria.getToDate() != null) {
			paymentSearchCriteria.setFromDate(dashboardUtils.enrichFormDate(paymentSearchCriteria.getFromDate()));
			paymentSearchCriteria.setToDate(dashboardUtils.enrichToDate(paymentSearchCriteria.getToDate()));
		}

		List<Payment> payments = urcRepository.fetchPayments(paymentSearchCriteria);
		payments = payments.stream().filter(payment -> payment.getPaymentStatus() != PaymentStatusEnum.CANCELLED)
				.collect(Collectors.toList());
		return payments;
	}
	
}
