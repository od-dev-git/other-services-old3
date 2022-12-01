package org.egov.report.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.WSSearchCriteria;
import org.egov.report.repository.builder.ReportQueryBuilder;
import org.egov.report.repository.rowmapper.BillSummaryRowMapper;
import org.egov.report.repository.rowmapper.ConsumerBillHistoryRowMapper;
import org.egov.report.repository.rowmapper.ConsumerMasterRowMapper;
import org.egov.report.repository.rowmapper.EmployeeWiseWSCollectionRowMapper;
import org.egov.report.repository.rowmapper.SchedulerGeneratedDemandsRowMapper;
import org.egov.report.repository.rowmapper.WSConnectionsElegibleForDemandRowMapper;
import org.egov.report.repository.rowmapper.WaterConnectionRowMapper;
import org.egov.report.repository.rowmapper.WaterMonthlyDemandRowMapper;
import org.egov.report.repository.rowmapper.WaterMonthlyDemandsRowMapper;
import org.egov.report.repository.rowmapper.WaterNewConsumerMonthlyRowMapper;
import org.egov.report.service.UserService;
import org.egov.report.service.WaterService;
import org.egov.report.web.model.BillSummaryResponses;
import org.egov.report.web.model.ConsumerBillHistoryResponse;
import org.egov.report.web.model.ConsumerMasterWSReportResponse;
import org.egov.report.web.model.EmployeeWiseWSCollectionResponse;
import org.egov.report.web.model.ULBWiseWaterConnectionDetails;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.egov.report.web.model.WaterConnectionDetails;
import org.egov.report.web.model.WaterDemandResponse;
import org.egov.report.web.model.WaterNewConsumerMonthlyResponse;
import org.egov.report.web.model.WsSchedulerBasedDemandsGenerationReponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class WSReportRepository {
	
		@Autowired
		ReportQueryBuilder queryBuilder;
		
		@Autowired
		JdbcTemplate jdbcTemplate;
		
		@Autowired
		UserService userService;
		
		@Autowired
		private ReportServiceConfiguration configuration;
	
		@Autowired
		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
		public List<ConsumerMasterWSReportResponse> getComsumerMasterWSReport(RequestInfo requestInfo, WSReportSearchCriteria criteria){
		
			List<Object> preparedStatement = new ArrayList<>();
		
			String query = queryBuilder.getQueryForConsumerMasterWSReport(preparedStatement,criteria); 
		
			return jdbcTemplate.query(query, preparedStatement.toArray(), new ConsumerMasterRowMapper());
		
		}


	public List<BillSummaryResponses> getBillSummaryDetails(WSReportSearchCriteria criteria)
	{
		List<Object> preparedStmtList = new ArrayList<>();
		String query = queryBuilder.getBillSummaryDetailsQuery(criteria, preparedStmtList);
		log.info(" Prepared Statement " + preparedStmtList.toString());
		return jdbcTemplate.query(query,preparedStmtList.toArray(), new BillSummaryRowMapper());
		
	}
	
		public List<WaterNewConsumerMonthlyResponse> getWaterNewConsumerMonthlyReport(WSReportSearchCriteria criteria){
			
			List<Object> preparedStmtList = new ArrayList<>();
			
			String query = queryBuilder.getWaterNewConsumerQuery(criteria, preparedStmtList);

			return jdbcTemplate.query(query,preparedStmtList.toArray(), new WaterNewConsumerMonthlyRowMapper());
		}
		
		public List<ConsumerBillHistoryResponse> getConsumerBillHistoryReport(WSReportSearchCriteria criteria){
			
			List<Object> preparedStmtList = new ArrayList<>();
			
			String query = queryBuilder.getConsumerBillHistoryQuery(criteria, preparedStmtList);

			return jdbcTemplate.query(query,preparedStmtList.toArray(), new ConsumerBillHistoryRowMapper());
		}
		
		public Map<String, List<WaterDemandResponse>> getWaterMonthlyDemandReport(WSReportSearchCriteria criteria) {

			List<Object> preparedStmtList = new ArrayList<>();

			String query = queryBuilder.getWaterMonthlyDemandQuery(criteria, preparedStmtList);

			return jdbcTemplate.query(query,preparedStmtList.toArray(), new WaterMonthlyDemandRowMapper());

		}

		public Map<String, WaterConnectionDetails> getWaterMonthlyDemandConnection(WSReportSearchCriteria criteria) {

			List<Object> preparedStmtList = new ArrayList<>();

			String query = queryBuilder.getWaterMonthlyDemandConnectionQuery(criteria, preparedStmtList);

			return jdbcTemplate.query(query,preparedStmtList.toArray(), new WaterConnectionRowMapper());

		}


		public List<EmployeeWiseWSCollectionResponse> getEmployeeWiseCollectionReport(
				WSReportSearchCriteria searchCriteria) {
			
			List<Object> preparedStmtList = new ArrayList<>();

			String query = queryBuilder.getEmployeeWiseWSCollectionQuery(searchCriteria, preparedStmtList);

			return jdbcTemplate.query(query,preparedStmtList.toArray(), new EmployeeWiseWSCollectionRowMapper());
			
		}
		
		public List<ULBWiseWaterConnectionDetails> getNoOfWSDemandConnections(RequestInfo requestInfo,
				WSReportSearchCriteria searchCriteria) {

			List<Object> preparedStmtList = new ArrayList<>();
			String query = queryBuilder.getElegibleWSConnectionsQuery(searchCriteria, preparedStmtList);
			return jdbcTemplate.query(query,preparedStmtList.toArray(), new WSConnectionsElegibleForDemandRowMapper());
		}
		
//		public List<String> getDemands(WSReportSearchCriteria searchCriteria){
//			
//			List<Object> preparedStmtList = new ArrayList<>();
//			
//			String query =  queryBuilder.getDemandsQuery(searchCriteria, preparedStmtList);
//			
//			return jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);
//			
//		}
		
		public List<WsSchedulerBasedDemandsGenerationReponse> getSchedulerBasedWSDemands(RequestInfo requestInfo,
				WSReportSearchCriteria searchCriteria) {
            
			log.info("in report repository");
			List<Object> preparedStmtList = new ArrayList<>();

			log.info("going in query builder");
			String query = queryBuilder.getSchedulerGeneratedDemandQuery(searchCriteria, preparedStmtList);
			
			log.info(" Prepared Statement : " + preparedStmtList.toString());
			log.info("query: "+query);
			log.info("returned from query builder");
			return jdbcTemplate.query(query,preparedStmtList.toArray(), new SchedulerGeneratedDemandsRowMapper());
		}
		
		public Map<String, WaterConnectionDetails> getWaterConnections(WSReportSearchCriteria criteria) {

			List<Object> preparedStmtList = new ArrayList<>();

			String query = queryBuilder.getWaterConnectionQuery(criteria, preparedStmtList);

			return jdbcTemplate.query(query,preparedStmtList.toArray(), new WaterConnectionRowMapper());

		}


		public Long getConsumerMasterReportCount(WSReportSearchCriteria criteria) {
			
			List<Object> preparedStatement = new ArrayList<>();
			
			String query = queryBuilder.getConsumerMasterReportCount(preparedStatement,criteria); 
		
			return jdbcTemplate.queryForObject(query, preparedStatement.toArray(), Long.class);
			
		}


		public Long getSchedulerBasedWSDemandCount(RequestInfo requestInfo, WSReportSearchCriteria searchCriteria) {
			
			List<Object> preparedStatement = new ArrayList<>();
			
			String query = queryBuilder.getSchedulerBasedWSDemandCount(preparedStatement,searchCriteria); 
			log.info(" Prepared Statement : " + preparedStatement.toString());
			
			return jdbcTemplate.queryForObject(query, preparedStatement.toArray(), Long.class);
		}
		
		public Long getWaterConnectionsCount(WSReportSearchCriteria searchCriteria) {
			
			List<Object> preparedStmtList = new ArrayList<>();

			String query = queryBuilder.getWaterConnectionCountQuery(searchCriteria, preparedStmtList);

			return jdbcTemplate.queryForObject(query,preparedStmtList.toArray(), Long.class);
		}
		
		public Long getDemandsCount(WSReportSearchCriteria searchCriteria) {
			
			List<Object> preparedStmtList = new ArrayList<>();
						
			String query =  queryBuilder.getDemandsQueryCount(searchCriteria, preparedStmtList);
						
			return jdbcTemplate.queryForObject(query, preparedStmtList.toArray(), Long.class);
		}
		
		public List<String> getDemands(WSReportSearchCriteria searchCriteria) {

			List<Object> preparedStmtList = new ArrayList<>();

			String query = queryBuilder.getDemandsQuery(searchCriteria, preparedStmtList);

			return jdbcTemplate.queryForList(query, preparedStmtList.toArray(), String.class);

		}
		
		public Long getWaterConnectionCount(WSReportSearchCriteria searchCriteria) {
            List<Object> preparedStmtList = new ArrayList<>();

            String query = queryBuilder.getWaterConnectionsCountQuery(searchCriteria, preparedStmtList);

            return jdbcTemplate.queryForObject(query,preparedStmtList.toArray(),  Long.class);
        
        }
		
        public List<String> getWaterConnection(WSReportSearchCriteria searchCriteria) {
            List<Object> preparedStmtList = new ArrayList<>();

            String query = queryBuilder.getWaterConnectionsQuery(searchCriteria, preparedStmtList);

            return jdbcTemplate.queryForList(query,preparedStmtList.toArray(), String.class);
        }
		
		public Map<String, List<WaterDemandResponse>> getWaterMonthlyDemandReports(
                WSReportSearchCriteria searchCriteria ) {
		    Map<String, Object> preparedStmtList = new HashMap<>();
		    String query = queryBuilder.getWaterMonthlyDemandQuery2(searchCriteria, preparedStmtList );
		    return namedParameterJdbcTemplate.query(query,preparedStmtList, new WaterMonthlyDemandsRowMapper());
        }
		
        public Map<String, WaterConnectionDetails> getWaterConnectionDetails(WSReportSearchCriteria criteria) {
            List<Object> preparedStmtList = new ArrayList<>();
            String query = queryBuilder.getWaterConnectionDetailsQuery(criteria, preparedStmtList);
            return jdbcTemplate.query(query, preparedStmtList.toArray(), new WaterConnectionRowMapper());
        }
        
}
