package org.egov.dss.service;

import java.util.List;

import org.egov.dss.config.ConfigurationLoader;
import org.egov.dss.model.enums.ChartType;
import org.egov.dss.util.Constants;
import org.egov.dss.web.model.Data;
import org.egov.dss.web.model.RequestInfoWrapper;
import org.egov.dss.web.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class DashboardService {
	
	@Autowired
	private RedirectService redirectService;
	
	@Autowired
	private ValidatorService validator;
	
	@Autowired
	private ConfigurationLoader configurationLoader;
	
	public ResponseData serveRequest(RequestInfoWrapper requestInfoWrapper) {
		validator.validateRequest(requestInfoWrapper);
		
		ResponseData responseData = ResponseData.builder().build();
		List<Data> data = redirectService.redirect(requestInfoWrapper);
		responseData.setData(data);

		getConfiguration(requestInfoWrapper, responseData);
		
		return responseData;
	}

	private void getConfiguration(RequestInfoWrapper requestInfoWrapper, ResponseData responseData) {
		String internalChartId = requestInfoWrapper.getChartCriteria().getVisualizationCode();
		ObjectNode node = configurationLoader.get(Constants.ConfigurationFiles.CHART_API_CONFIG);
		ObjectNode chartNode = (ObjectNode) node.get(internalChartId);
		ChartType chartType = ChartType.fromValue(chartNode.get(Constants.JsonPaths.CHART_TYPE).asText());
		String valueType = chartNode.get(Constants.JsonPaths.VALUE_TYPE).asText();
		String chartName = chartNode.get(Constants.JsonPaths.CHART_NAME).asText();
		String drillChart = chartNode.get(Constants.JsonPaths.DRILL_CHART).asText();
		
		responseData.setChartType(chartType.toString());
		responseData.setVisualizationCode(internalChartId);
		responseData.setDrillDownChartId(drillChart);
		responseData.getData().stream().forEach(data -> data.setHeaderSymbol(valueType));
		if(!(chartType.toString()).equalsIgnoreCase(ChartType.TABLE.toString())){
			responseData.getData().forEach(data -> data.setHeaderName(chartName));
		}
		responseData.getData().forEach(data -> data.getPlots().forEach(plot -> plot.setSymbol(valueType)));
	}

}
