package org.egov.arc.repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.egov.arc.config.ArchivalConfig;
import org.egov.arc.model.Demand;
import org.egov.arc.model.DemandCriteria;
import org.egov.arc.model.DemandDetail;
import org.egov.arc.model.Demand.StatusEnum;
import org.egov.arc.repository.builder.ArchivalQueryBuilder;
import org.egov.arc.repository.rowmapper.DemandRowMapper;
import org.egov.arc.util.Util;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ArchivalRepository {

	@Autowired
	ArchivalQueryBuilder archivalQueryBuilder;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	DemandRowMapper demandRowMapper;

	@Autowired
	Util util;

	@Autowired
	ArchivalConfig config;

	public List<Demand> getDemands(DemandCriteria demandCriteria) {
		List<Object> preparedStatementValues = new ArrayList<>();
		String searchDemandQuery = archivalQueryBuilder.getDemandQuery(demandCriteria, preparedStatementValues);
		log.info("Demand Archival Query : " + searchDemandQuery);
		log.info("Params : " + String.valueOf(preparedStatementValues));
		return jdbcTemplate.query(searchDemandQuery, preparedStatementValues.toArray(), demandRowMapper);
	}

	public Long getDemandCount(DemandCriteria demandCriteria) {
		List<Object> preparedStatementValues = new ArrayList<>();
		String getDemndCountQuery = archivalQueryBuilder.getDemandCountQuery(demandCriteria, preparedStatementValues);
		log.info("Demand Count Query : " + getDemndCountQuery);
		log.info("Params : " + String.valueOf(preparedStatementValues));
		return jdbcTemplate.queryForObject(getDemndCountQuery, preparedStatementValues.toArray(), Long.class);
	}

	@Transactional
	public void insertArchiveDemands(Set<Demand> demands) {

		try {
			jdbcTemplate.batchUpdate(archivalQueryBuilder.INSERT_ARCHIVE_DEMANDS_QUERY,
					new InterruptibleBatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							Demand demand = demands.stream().collect(Collectors.toList()).get(i);
							ps.setString(1, demand.getId());
							ps.setString(2, demand.getConsumerCode());
							ps.setString(3, demand.getConsumerType());
							ps.setString(4, demand.getPayer().getUuid());
							ps.setLong(5, demand.getTaxPeriodFrom());
							ps.setLong(6, Long.valueOf(demand.getTaxPeriodTo()));
							ps.setString(7, demand.getAuditDetails().getCreatedBy());
							ps.setLong(8, demand.getAuditDetails().getCreatedTime());
							ps.setString(9, demand.getAuditDetails().getLastModifiedBy());
							ps.setLong(10, demand.getAuditDetails().getLastModifiedTime());
							ps.setString(11, demand.getTenantId());
							ps.setBigDecimal(12, demand.getMinimumAmountPayable());
							ps.setString(13, demand.getStatus().name().toString());
							ps.setObject(14, util.getPGObject(demand.getAdditionalDetails()));
							ps.setLong(15, demand.getBillExpiryTime());
							ps.setBoolean(16, demand.getIsPaymentCompleted());
							ps.setLong(17, demand.getFixedBillExpiryDate());
							ps.setLong(18, System.currentTimeMillis());

							Set<DemandDetail> demandDetails = demand.getDemandDetails();
							jdbcTemplate.batchUpdate(archivalQueryBuilder.INSERT_ARCHIVE_DEMAND_DETAILS_QUERY,
									new InterruptibleBatchPreparedStatementSetter() {
										@Override
										public void setValues(PreparedStatement ps, int j) throws SQLException {
											DemandDetail demandDetail = demandDetails.stream()
													.collect(Collectors.toList()).get(j);
											// Set values for DemandDetails table
											ps.setString(1, demandDetail.getId());
											ps.setString(2, demandDetail.getDemandId());
											ps.setString(3, demandDetail.getTaxHeadMasterCode());
											ps.setBigDecimal(4, demandDetail.getTaxAmount());
											ps.setBigDecimal(5, demandDetail.getCollectionAmount());
											ps.setString(6, demandDetail.getAuditDetails().getCreatedBy());
											ps.setLong(7, demandDetail.getAuditDetails().getCreatedTime());
											ps.setString(8, demandDetail.getAuditDetails().getLastModifiedBy());
											ps.setLong(9, demandDetail.getAuditDetails().getLastModifiedTime());
											ps.setString(10, demandDetail.getTenantId());
											ps.setObject(11, util.getPGObject(demandDetail.getAdditionalDetails()));
											ps.setLong(12, System.currentTimeMillis());

										}

										@Override
										public int getBatchSize() {
											// return (demandDetails.size()/config.getInsertBatchSize());
											 return demandDetails.size();
										}

										@Override
										public boolean isBatchExhausted(int i) {
											// TODO Auto-generated method stub
											return false;
										}

									});
						}

						@Override
						public int getBatchSize() {
						//	return (demands.size()/config.getInsertBatchSize());
						 return demands.size();
						}

						@Override
						public boolean isBatchExhausted(int i) {
							// TODO Auto-generated method stub
							return false;
						}

					});

			Set<String> archivedDemandIds = new HashSet<>();
			demands.stream().map(Demand::getId).forEach(archivedDemandIds::add);
			log.info("Archived Demand Ids : " + archivedDemandIds);

			if (!demands.isEmpty()) {
				Set<String> demandIds = new HashSet<>();
				demands.stream().map(Demand::getId).forEach(demandIds::add);
				Map<String, Object> paramMap = Collections.singletonMap("demandIds", demandIds);
				String deleteDemandSql = archivalQueryBuilder.DELETE_DEMAND_SQL_QUERY;
				String deleteDemandDetailsSql = archivalQueryBuilder.DELETE_DEMAND_DETAILS_SQL_QUERY;
				namedParameterJdbcTemplate.update(deleteDemandDetailsSql, paramMap);
				namedParameterJdbcTemplate.update(deleteDemandSql, paramMap);
				log.info("Deleted Demand ids : " + demandIds);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
