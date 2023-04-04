package org.egov.dss.service;

import java.util.Arrays;
import java.util.Calendar;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.enums.ChartType;
import org.egov.dss.repository.CommonRepository;
import org.egov.dss.util.DashboardUtils;
import org.egov.dss.web.model.ChartCriteria;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.Filter;
import org.egov.dss.web.model.RequestInfoWrapper;
import org.egov.dss.web.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashboardService {
	
	@Autowired
	private RedirectService redirectService;
	
	@Autowired
	private ValidatorService validator;
	
	@Autowired
	private ConfigurationLoader configurationLoader;
	
	@Autowired
	private CommonRepository commonRepository;
	
	@Autowired
	private DashboardUtils utils;
	
	public void processRequest(RequestInfoWrapper requestInfoWrapper) {
		Long schedulerStartTime = System.currentTimeMillis();
		ResponseData responseData = new ResponseData();
		requestEnrich(requestInfoWrapper.getChartCriteria());
		List<PayloadDetails> payloadList = getPayloadForScheduler(requestInfoWrapper.getChartCriteria());
		for (PayloadDetails payloadDetails : payloadList) {
			try {
				enrichPayload(payloadDetails);
				payloadDetails.setEnddate(requestInfoWrapper.getChartCriteria().getEndDate());
				requestInfoWrapper.setPayloadDetails(payloadDetails);
				responseData = serveRequest(requestInfoWrapper);
				payloadDetails.setResponsedata(responseData);
				payloadDetails.setLastModifiedTime(schedulerStartTime);
				if (responseData.getData() != null) {
					commonRepository.update(payloadDetails);
				}
			} catch (Exception e) {
				log.error("Unable to process for visualization code :" + payloadDetails.getVisualizationcode()
						+ " Module :" + payloadDetails.getModulelevel());
				e.printStackTrace();

			}

		}

	}
	
	public ResponseData serveRequest(RequestInfoWrapper requestInfoWrapper) {
		validator.validateRequest(requestInfoWrapper);
		ResponseData responseData = ResponseData.builder().build();
		List<Data> data = redirectService.redirect(requestInfoWrapper);
		responseData.setData(data);
		if(data != null && !data.isEmpty()) {
		setConfiguration(requestInfoWrapper, responseData);
		}
		return responseData;
	}

	private void setConfiguration(RequestInfoWrapper requestInfoWrapper, ResponseData responseData) {
		String internalChartId = requestInfoWrapper.getPayloadDetails().getVisualizationcode();
	/*	ObjectNode node = configurationLoader.get(Constants.ConfigurationFiles.CHART_API_CONFIG);
		ObjectNode chartNode = (ObjectNode) node.get(internalChartId);
		ChartType chartType = ChartType.fromValue(chartNode.get(Constants.JsonPaths.CHART_TYPE).asText());
		String valueType = chartNode.get(Constants.JsonPaths.VALUE_TYPE).asText();
		String chartName = chartNode.get(Constants.JsonPaths.CHART_NAME).asText();
		String drillChart = chartNode.get(Constants.JsonPaths.DRILL_CHART).asText();
	*/
		String chartType = requestInfoWrapper.getPayloadDetails().getCharttype();
		String chartName = requestInfoWrapper.getPayloadDetails().getHeadername();
		String valueType = requestInfoWrapper.getPayloadDetails().getValuetype();
				
		responseData.setChartType(chartType.toString());
		responseData.setVisualizationCode(internalChartId);
		//responseData.setDrillDownChartId(drillChart);
		if(chartType.equalsIgnoreCase(DashboardConstants.PERFORM)) {
			responseData.getData().stream().forEach(data -> data.setHeaderSymbol(valueType));
			responseData.getData().forEach(data -> data.setHeaderName(DashboardConstants.RANK));
		}else {
			if(!(chartType.toString().equalsIgnoreCase(ChartType.TABLE.toString()) || chartType.toString().equalsIgnoreCase(ChartType.XTABLE.toString()))) {
				responseData.getData().stream().forEach(data -> data.setHeaderSymbol(valueType));
			}
			if(!((chartType.toString()).equalsIgnoreCase(ChartType.TABLE.toString()) || (chartType.toString()).equalsIgnoreCase(ChartType.XTABLE.toString()) || (chartType.toString()).equalsIgnoreCase(ChartType.LINE.toString()))){
				responseData.getData().forEach(data -> data.setHeaderName(chartName));
			}
		}
		if(!(chartType.toString().equalsIgnoreCase(ChartType.TABLE.toString()) || chartType.toString().equalsIgnoreCase(ChartType.XTABLE.toString()))) {
			responseData.getData().forEach(data -> data.getPlots().forEach(plot -> plot.setSymbol(valueType)));
		}
		
		if((chartType.toString().equalsIgnoreCase(ChartType.TABLE.toString()) || chartType.toString().equalsIgnoreCase(ChartType.XTABLE.toString()))) {
			responseData.setDrillDownChartId(null);
			responseData.setFilter(Arrays.asList(Filter.builder().key(String.valueOf("tenantId")).column(String.valueOf("DDRs")).build()));
		}
		
		
	}
	
	public List<PayloadDetails> getPayloadForScheduler(ChartCriteria criteria){
	  List<PayloadDetails> payloadList = commonRepository.fetchSchedulerPayloads(criteria);
	   return payloadList;
	}
	
	public void requestEnrich(ChartCriteria criteria) {
		if (criteria == null || !StringUtils.hasText(criteria.getFinancialYear())) {
			criteria.setFinancialYear(utils.getCurrentFinancialYear());
		}
		criteria.setStartDate(utils.getStartDate(criteria.getFinancialYear()));
		criteria.setEndDate(utils.getEndDate(criteria.getFinancialYear()));

	}
	
	public void enrichPayload(PayloadDetails payloadDetails) {
		if (payloadDetails != null) {
			if (payloadDetails.getTimeinterval().equalsIgnoreCase(DashboardConstants.QUARTER)) {
				payloadDetails.setStartdate(utils.getStartDateOfQuarter());
			} else if (payloadDetails.getTimeinterval().equalsIgnoreCase(DashboardConstants.MONTH)) {
			    payloadDetails.setStartdate(utils.getStartDateOfMonth());
			}else if (payloadDetails.getTimeinterval().equalsIgnoreCase(DashboardConstants.WEEK)) {
			    payloadDetails.setStartdate(utils.getStartDateOfWeek());
			}
		}
    }
	

}




