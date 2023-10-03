package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.DemandSearchCriteria;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.TargetSearchCriteria;
import org.egov.dss.model.UrcSearchCriteria;
import org.egov.dss.repository.URCRepository;
import org.egov.dss.util.DashboardUtility;
import org.egov.dss.util.DashboardUtils;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Plot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class URCService {

	@Autowired
	private URCRepository urcRepository;

	@Autowired
	private DashboardUtils dashboardUtils;
	
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
			criteria.setBusinessService(payloadDetails.getModulelevel());
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
	
	private TargetSearchCriteria getTargetSearchCriteria(PayloadDetails payloadDetails) {
		TargetSearchCriteria criteria = new TargetSearchCriteria();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			if (payloadDetails.getModulelevel().equalsIgnoreCase(DashboardConstants.MODULE_LEVEL_URC))
				criteria.setBusinessServices(Sets.newHashSet(DashboardConstants.MODULE_LEVEL_PT,DashboardConstants.BUSINESS_SERVICE_WS));
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
				.subtract(BigDecimal.ONE);
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
		if(previousTotalCollection == BigDecimal.ZERO)
			previousTotalCollection = BigDecimal.ONE;
		BigDecimal changeInCollection = totalCollection.divide(previousTotalCollection, 2, BigDecimal.ROUND_HALF_UP)
				.subtract(BigDecimal.ONE);
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
				.subtract(BigDecimal.ONE);
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
		if (previousTotalCollection == BigDecimal.ZERO)
			previousTotalCollection = BigDecimal.ONE;
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
		PaymentSearchCriteria paymentSearchCriteria = new PaymentSearchCriteria();
		payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
		List<Plot> plots = new ArrayList<Plot>();
		int serialNumber = 1;
		BigDecimal totalDemand = totalDemand(payloadDetails);
		BigDecimal totalCollection = (BigDecimal) ptTotalCollection(payloadDetails);
		BigDecimal pendingCollection = totalDemand.subtract(totalCollection);
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_DEMAND).value(totalDemand)
				.label(String.valueOf(serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.TOTAL_COLLECTION).value(totalCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		plots.add(Plot.builder().name(DashboardConstants.PENDING_COLLECTION).value(pendingCollection)
				.label(String.valueOf(++serialNumber)).symbol("Amount").build());
		postEnrichmentCollection(payloadDetails, paymentSearchCriteria);
		BigDecimal previousTotalCollection = (BigDecimal) urcRepository.getTotalCollection(paymentSearchCriteria);
		if (previousTotalCollection == BigDecimal.ZERO)
			previousTotalCollection = BigDecimal.ONE;
		BigDecimal previousYearEfficiency = previousTotalCollection.divide(totalDemand, 2, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(100));
		plots.add(Plot.builder().name(DashboardConstants.PREVIOUS_EFFICIENCY).value(previousYearEfficiency)
				.label(String.valueOf(++serialNumber)).symbol("Percentage").build());
		return Arrays.asList(Data.builder().headerName("DSS_URC_PT_DEMAND_EFFICIENCY").plots(plots).build());
	}
    
	
}
