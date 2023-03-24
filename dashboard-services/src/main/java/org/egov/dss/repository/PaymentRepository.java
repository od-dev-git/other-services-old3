package org.egov.dss.repository;

import static java.util.Collections.reverseOrder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.dss.model.Bill;
import org.egov.dss.model.Chart;
import org.egov.dss.model.Payment;
import org.egov.dss.model.PaymentSearchCriteria;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.model.TargetSearchCriteria;
import org.egov.dss.model.UsageTypeResponse;
import org.egov.dss.repository.builder.PaymentQueryBuilder;
import org.egov.dss.repository.rowmapper.BillRowMapper;
import org.egov.dss.repository.rowmapper.ChartRowMapper;
import org.egov.dss.repository.rowmapper.CollectionByUsageRowMapper;
import org.egov.dss.repository.rowmapper.PaymentRowMapper;
import org.egov.dss.repository.rowmapper.TenantWiseCollectionRowMapper;
import org.egov.dss.web.model.ChartCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PaymentRepository {
	
	@Autowired
	private PaymentQueryBuilder paymentQueryBuilder;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private PaymentRowMapper paymentRowMapper;
	
	@Autowired
	private BillRowMapper billRowMapper;

	public List<Payment> getPayments(PaymentSearchCriteria paymentSearchCriteria) {

        Map<String, Object> preparedStatementValues = new HashMap<>();

        List<String> ids = fetchPaymentIdsByCriteria(paymentSearchCriteria);

        if(CollectionUtils.isEmpty(ids))
            return new LinkedList<>();

        String query = paymentQueryBuilder.getPaymentSearchQuery(ids, preparedStatementValues);
        log.info("Query: " + query);
        log.info("preparedStatementValues: " + preparedStatementValues);
        List<Payment> payments = namedParameterJdbcTemplate.query(query, preparedStatementValues, paymentRowMapper);
        if (!CollectionUtils.isEmpty(payments)) {
            Set<String> billIds = new HashSet<>();
            for (Payment payment : payments) {
                billIds.addAll(payment.getPaymentDetails().stream().map(detail -> detail.getBillId()).collect(Collectors.toSet()));
            }
            Map<String, Bill> billMap = getBills(billIds);
            for (Payment payment : payments) {
                payment.getPaymentDetails().forEach(detail -> {
                    detail.setBill(billMap.get(detail.getBillId()));
                });
            }
            payments.sort(reverseOrder(Comparator.comparingLong(Payment::getTransactionDate)));
        }

        return payments;
		
	}
	
	public List<String> fetchPaymentIdsByCriteria(PaymentSearchCriteria paymentSearchCriteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = paymentQueryBuilder.getIdQuery(paymentSearchCriteria, preparedStatementValues);
        log.info("query: "+query);
        return namedParameterJdbcTemplate.query(query, preparedStatementValues, new SingleColumnRowMapper<>(String.class));
    }
	
	private Map<String, Bill> getBills(Set<String> ids){
    	Map<String, Bill> mapOfIdAndBills = new HashMap<>();
        Map<String, Object> preparedStatementValues = new HashMap<>();
        preparedStatementValues.put("id", ids);
        String query = paymentQueryBuilder.getBillQuery();
        List<Bill> bills = namedParameterJdbcTemplate.query(query, preparedStatementValues, billRowMapper);
        bills.forEach(bill -> {
        	mapOfIdAndBills.put(bill.getId(), bill);
        });
        
        return mapOfIdAndBills;

    }

	public List<UsageTypeResponse> getUsageTypes(PaymentSearchCriteria paymentSearchCriteria) {
		
		Map<String, Object> preparedStatementValues = new HashMap<>();
		
		String query = paymentQueryBuilder.getUsageTypeQuery(paymentSearchCriteria, preparedStatementValues);
		
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new CollectionByUsageRowMapper());
	}
	
	public Object getTtargetCollection(TargetSearchCriteria criteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = paymentQueryBuilder.getTargetCollection(criteria, preparedStatementValues);
		log.info("query: " + query);
		List<BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(BigDecimal.class));
		return result.get(0);

	}
	
	public Object getTotalCollection(PaymentSearchCriteria criteria) {
        Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = paymentQueryBuilder.getTotalCollection(criteria, preparedStatementValues);
		log.info("query: " + query);
		List<BigDecimal> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(BigDecimal.class));
		return result.get(0);

	}

	public Long getTotalTransactionCount(PaymentSearchCriteria paymentSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = paymentQueryBuilder.getTransactionsCount(paymentSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		List<Long> result = namedParameterJdbcTemplate.query(query, preparedStatementValues,
				new SingleColumnRowMapper<>(Long.class));
		return result.get(0);
	}

	public HashMap<String, BigDecimal> getTenantWiseCollection(PaymentSearchCriteria paymentSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = paymentQueryBuilder.getTenantWiseCollection(paymentSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues,new TenantWiseCollectionRowMapper());
	}

	public HashMap<String, BigDecimal> getTenantWiseTargetCollection(TargetSearchCriteria targerSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = paymentQueryBuilder.getTenantWiseTargetCollection(targerSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues, new TenantWiseCollectionRowMapper());
	}
	
	public HashMap<String, BigDecimal> getTenantWiseTransaction(PaymentSearchCriteria paymentSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = paymentQueryBuilder.getTenantWiseTransaction(paymentSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues,new TenantWiseCollectionRowMapper());
	}
	
	public List<Chart> getCumulativeCollection(PaymentSearchCriteria paymentSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = paymentQueryBuilder.getCumulativeCollection(paymentSearchCriteria, preparedStatementValues);
        log.info("query for Cumulative Collection : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}
	
	public List<Chart> getCollectionByUsageType(PaymentSearchCriteria paymentSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
        String query = paymentQueryBuilder.getCollectionByUsageTypeQuery(paymentSearchCriteria, preparedStatementValues);
        log.info("query for Usage Type Collection : "+query);
        List<Chart> result = namedParameterJdbcTemplate.query(query, preparedStatementValues, new ChartRowMapper());
        return result;
	}
	
	public HashMap<String, BigDecimal> getTenantWiseAssedProperties(PaymentSearchCriteria paymentSearchCriteria) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = paymentQueryBuilder.getTenantWiseAssedProperties(paymentSearchCriteria, preparedStatementValues);
		log.info("query: " + query);
		return namedParameterJdbcTemplate.query(query, preparedStatementValues,new TenantWiseCollectionRowMapper());
	}
	

}
