package org.egov.dss.repository;

import static java.util.Collections.reverseOrder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.Bill;
import org.egov.dss.model.DemandSearchCriteria;
import org.egov.dss.model.Payment;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.model.TargetSearchCriteria;
import org.egov.dss.model.UrcSearchCriteria;
import org.egov.dss.model.UserSearchCriteria;
import org.egov.dss.model.UserSearchRequest;
import org.egov.dss.model.WaterSearchCriteria;
import org.egov.dss.repository.builder.URCQueryBuilder;
import org.egov.dss.repository.rowmapper.ChartRowMapper;
import org.egov.dss.repository.rowmapper.PaymentRowMapper;
import org.egov.dss.repository.rowmapper.TableChartRowMapper;
import org.egov.dss.repository.rowmapper.TenantWiseCollectionRowMapper;
import org.egov.dss.repository.rowmapper.URCRevenueRowMapper;
import org.egov.dss.repository.rowmapper.UrcTableChartRowMapper;
import org.egov.dss.repository.rowmapper.UserRowMapper;
import org.egov.dss.web.model.User;
import org.egov.dss.web.model.UserResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.egov.dss.repository.rowmapper.BillRowMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class URCRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private URCQueryBuilder urcQueryBuilder;
	
	@Autowired
	private ConfigurationLoader config;
	
	@Autowired
	private UserRowMapper userRowMapper;
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private BillRowMapper billRowMapper;
	
	public Object getTotalCollection(PaymentSearchCriteria criteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = urcQueryBuilder.getTotalCollection(criteria, preparedStatementValues);
		log.info("Query: " + query);
		log.info("Params: "+preparedStatementValues);
		List<BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(BigDecimal.class));
		return result.get(0);
	}
	
	public Integer getUlbsUnderUrc(UrcSearchCriteria criteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = urcQueryBuilder.getUlbsUnderUrc(criteria, preparedStatementValues);
		log.info("Query: " + query);
		log.info("Params: "+preparedStatementValues);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}
	
	public Integer jalSathiOnboarded(UrcSearchCriteria criteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = urcQueryBuilder.jalSathiOnboarded(criteria, preparedStatementValues);
		log.info("Query: " + query);
		log.info("Params: "+preparedStatementValues);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}
	
	public Object getUrcPropertiesPaid(PaymentSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = urcQueryBuilder.urcPropertiesPaid(criteria, preparedStatementValues);
        log.info("Query for URC Properties Paid : "+query);
        log.info("Params : "+preparedStatementValues);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}
	
	public Object activeJalsathi(PaymentSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = urcQueryBuilder.activeJalsathi(criteria, preparedStatementValues);
        log.info("Query for Active JalSathi : "+query);
        log.info("Params : "+preparedStatementValues);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}
	
	public Object propertyCoveredByJalsathi(PaymentSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = urcQueryBuilder.propertyCoveredByJalsathi(criteria, preparedStatementValues);
        log.info("Query for Proerty Cover By Jalsathi : "+query);
        log.info("Params : "+preparedStatementValues);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
	}
	
	public BigDecimal colletionByJalSathi(PaymentSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = urcQueryBuilder.collectionByJalsathi(criteria, preparedStatementValues);
        log.info("Query for Collection By Jalsathi : "+query);
        log.info("Params : "+preparedStatementValues);
        List<BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(BigDecimal.class));
        return result.get(0);
	}
	
	public LinkedHashMap<String, BigDecimal> getMonthWiseCollection(PaymentSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = urcQueryBuilder.getMonthWiseCollection(criteria, preparedStatementValues);
		log.info(" URC month wise Collection query : " + query);
		log.info(" preparedStatementValues : " + preparedStatementValues);
		LinkedHashMap<String, BigDecimal> result = (LinkedHashMap<String, BigDecimal>) namedParameterJdbcTemplate
				.query(query, preparedStatementValues, new URCRevenueRowMapper());
		return result;
	}
	
	public List<LinkedHashMap<String, Object>> getMonthWiseJalsathiCollection(PaymentSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = urcQueryBuilder.getMonthWiseJalsathiCollection(criteria, preparedStatementValues);
		log.info(" URC month wise JalSahti Collection query : " + query);
		log.info(" preparedStatementValues : " + preparedStatementValues);
		List<LinkedHashMap<String, Object>> result = (List<LinkedHashMap<String, Object>>) namedParameterJdbcTemplate
				.query(query, preparedStatementValues, new UrcTableChartRowMapper());
		return result;
	}
	
	public Object getTargetCollection(TargetSearchCriteria criteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = urcQueryBuilder.getTargetCollection(criteria, preparedStatementValues);
		log.info("query: " + query);
		log.info("preparedStatementValues: " + preparedStatementValues);
		List<BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(BigDecimal.class));
		return result.get(0);

	}
	
	public BigDecimal getCurrentDemand(DemandSearchCriteria criteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
	    String query = urcQueryBuilder.getCurrentDemand(criteria, preparedStatementValues);
		log.info("query: " + query);
		log.info("preparedStatementValues: " + preparedStatementValues);
		List<BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(BigDecimal.class));
		return result.get(0);

	}
	
	public BigDecimal getArrearDemand(DemandSearchCriteria criteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
	    String query = urcQueryBuilder.getArrearDemand(criteria, preparedStatementValues);
		log.info("query: " + query);
		log.info("preparedStatementValues: " + preparedStatementValues);
		List<BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(BigDecimal.class));
		return result.get(0);

	}
	
	public LinkedHashMap<String, BigDecimal> jalSathiWiseCollection(PaymentSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = urcQueryBuilder.jalSathiWiseCollection(criteria, preparedStatementValues);
		log.info(" Jal Sathi wise collection query : " + query);
		log.info(" preparedStatementValues : " + preparedStatementValues);
		LinkedHashMap<String, BigDecimal> result = (LinkedHashMap<String, BigDecimal>) namedParameterJdbcTemplate
				.query(query, preparedStatementValues, new URCRevenueRowMapper());
		return result;
	}
	
	public LinkedHashMap<String, BigDecimal> propertiesCoverByJalsathi(PaymentSearchCriteria criteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = urcQueryBuilder.propertiesCoverByJalsathi(criteria, preparedStatementValues);
		log.info(" Properties Cover By Jalsathi query : " + query);
		log.info(" preparedStatementValues : " + preparedStatementValues);
		LinkedHashMap<String, BigDecimal> result = (LinkedHashMap<String, BigDecimal>) namedParameterJdbcTemplate
				.query(query, preparedStatementValues, new URCRevenueRowMapper());
		return result;
	}
	
	public List<User> getEmployeeBaseTenant(List<Long> userIds) {
		List<Object> prepareStatement = new ArrayList<>();
		String query = urcQueryBuilder.getEmployeeBaseTenantQuery(userIds, prepareStatement);
		List<User> users = jdbcTemplate.query(query, prepareStatement.toArray(), userRowMapper);
		return users;
	}
	
	public List<User> getUserDetails(UserSearchCriteria userSearchCriteria, RequestInfo requestInfo) {

		StringBuilder uri = new StringBuilder(config.getUserHost()).append(config.getUserSearchEndpoint());

		UserSearchRequest request = generateUserSearchRequest(requestInfo, userSearchCriteria);
		log.info("User Service uri : " + uri);
		try {
			Object response = serviceRepository.fetchResult(uri, request);
			UserResponse userResponse = mapper.convertValue(response, UserResponse.class);
			return userResponse.getUserInfo();
		} catch (Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("USER_FETCH_EXCEPTION", "Unable to fetch User Information");
		}
	}
	
	private UserSearchRequest generateUserSearchRequest(RequestInfo requestInfo, UserSearchCriteria criteria) {

		UserSearchRequest request = UserSearchRequest.builder().requestInfo(requestInfo).active(Boolean.TRUE).build();

		if (StringUtils.hasText(criteria.getTenantId())) {
			if (StringUtils.hasText(criteria.getUserType())
					&& UserSearchCriteria.CITIZEN.equals(criteria.getUserType()))
				request.setTenantId(DashboardConstants.STATE_TENANT);
			else
				request.setTenantId(criteria.getTenantId());
		}

		if (!CollectionUtils.isEmpty(criteria.getUuid())) {
			request.setUuid(criteria.getUuid().stream().collect(Collectors.toSet()));
		}

		if (!CollectionUtils.isEmpty(criteria.getId())) {
			request.setId(criteria.getId().stream().collect(Collectors.toList()));
		}

		if (StringUtils.hasText(criteria.getUserType())) {
			request.setUserType(criteria.getUserType());
		}
		return request;
	}
	
	public Integer getTotalPropertiesCount(PropertySerarchCriteria propertySearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = urcQueryBuilder.getTotalPropertiesCountQuery(propertySearchCriteria, preparedStatementValues);
		log.info("query for total properties: "+ query);
		log.info("params : "+preparedStatementValues);
		List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
		return result.get(0);
	}
	
	public Object getActiveWaterConnectionCount(WaterSearchCriteria waterSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = urcQueryBuilder.getActiveConnectionCount(waterSearchCriteria, preparedStatementValues);
        log.info("query FOR get Active Water Connection Count : "+query);
        log.info("preparedStatementValues : "+preparedStatementValues);
        List<Integer> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(Integer.class));
        return result.get(0);
    }
	
	public List<Payment> fetchPayments(PaymentSearchCriteria paymentSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = urcQueryBuilder.getPaymentSearchQuery(paymentSearchCriteria, preparedStatementValues);
		log.info("Query: " + query);
		log.info("preparedStatementValues: " + preparedStatementValues);
		List<Payment> payments = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new PaymentRowMapper());
		if (!CollectionUtils.isEmpty(payments)) {
			Set<String> billIds = new HashSet<>();
			for (Payment payment : payments) {
				billIds.addAll(payment.getPaymentDetails().stream().map(detail -> detail.getBillId())
						.collect(Collectors.toSet()));
			}
			Map<String, org.egov.dss.model.Bill> billMap = getBills(billIds);
			for (Payment payment : payments) {
				payment.getPaymentDetails().forEach(detail -> {
					detail.setBill(billMap.get(detail.getBillId()));
				});
			}
			payments.sort(reverseOrder(Comparator.comparingLong(Payment::getTransactionDate)));
		}

		return payments;
	}

	private Map<String, Bill> getBills(Set<String> ids) {
		Map<String, Bill> mapOfIdAndBills = new HashMap<>();
		Map<String, Object> preparedStatementValues = new HashMap<>();
		preparedStatementValues.put("id", ids);
		String query = urcQueryBuilder.getBillQuery();
		List<Bill> bills = namedParameterJdbcTemplate.query(query, preparedStatementValues, billRowMapper);
		bills.forEach(bill -> {
			mapOfIdAndBills.put(bill.getId(), bill);
		});

		return mapOfIdAndBills;

	}
	
	public Long getPaymentsCount(@Valid RequestInfo requestInfo, PaymentSearchCriteria paymentSearchCriteria) {

		return getPaymentsCount(paymentSearchCriteria);

	}
	
	public Long getPaymentsCount(PaymentSearchCriteria paymentSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = urcQueryBuilder.getIdCountQuery(paymentSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        log.info("preparedStatementValues: "+preparedStatementValues);
		return namedParameterJdbcTemplate.queryForObject(query, preparedStatementValues, Long.class);
	}

}
