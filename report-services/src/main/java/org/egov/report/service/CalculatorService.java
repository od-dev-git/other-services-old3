package org.egov.report.service;

import java.math.BigDecimal;
import java.util.Map;

import org.egov.report.model.IncentiveAnalysis;
import org.egov.report.util.ReportConstants;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
	
	public void calculateIncentives(String module, Map<String, IncentiveAnalysis> incentiveAnalysis) {
		
		switch (module) {
		case ReportConstants.WATER_MODULE:
			calculateWaterIncentive(incentiveAnalysis);
			break;
		case ReportConstants.PROPERTY_MODULE:
			calculatePropertyIncentive(incentiveAnalysis);
		default:
			break;
		}
	}

	private void calculatePropertyIncentive(Map<String, IncentiveAnalysis> incentiveAnalysis) {
		// TODO Auto-generated method stub
		
	}

	private void calculateWaterIncentive(Map<String, IncentiveAnalysis> incentiveAnalysis) {
		incentiveAnalysis.values().stream().forEach(incentive -> {
			incentive.setTotalIncentive(incentive.getTotalCollection().multiply(new BigDecimal(0.05)));
		});
	}

}
