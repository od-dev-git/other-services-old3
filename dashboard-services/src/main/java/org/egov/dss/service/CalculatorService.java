package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.IncentiveAnalysis;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
	
	public static final BigDecimal Amt_15L = BigDecimal.valueOf(1500000);
	public static final BigDecimal Amt_30L = BigDecimal.valueOf(3000000);
	public static final BigDecimal Percentage2 = BigDecimal.valueOf(0.02);
	public static final BigDecimal Percentage2_5 = BigDecimal.valueOf(0.025);
	public static final BigDecimal Percentage3 = BigDecimal.valueOf(0.03);
	public static final BigDecimal Percentage4 = BigDecimal.valueOf(0.04);
	public static final BigDecimal Percentage5 = BigDecimal.valueOf(0.05);
	
	public void calculateIncentives(String module, Map<String, IncentiveAnalysis> incentiveAnalysis) {
		
		switch (module) {
		case DashboardConstants.WATER_MODULE:
			calculateWaterIncentive(incentiveAnalysis);
			break;
		case DashboardConstants.PROPERTY_MODULE:
			calculatePropertyIncentive(incentiveAnalysis);
		default:
			break;
		}
	}

	private void calculatePropertyIncentive(Map<String, IncentiveAnalysis> incentiveAnalysis) {
		incentiveAnalysis.values().stream().forEach(incentive -> {
			incentive.setTotalIncentiveOnArrear(calculatePTArrearCollectionIncentive(incentive));
			incentive.setTotalIncentiveOnCurrent(calculatePTCurrentCollectionIncentive(incentive));
			incentive.setTotalIncentive(incentive.getTotalIncentiveOnArrear().add(incentive.getTotalIncentiveOnCurrent()));
		});
		
	}

	private BigDecimal calculatePTCurrentCollectionIncentive(IncentiveAnalysis incentive) {
		BigDecimal currentIncentive = BigDecimal.ZERO;
		if(incentive.getCollectionTowardsCurrent().compareTo(Amt_15L) < 1 ) {
			currentIncentive = incentive.getCollectionTowardsCurrent().multiply(Percentage2).setScale(2, RoundingMode.UP);
		} else if(incentive.getCollectionTowardsCurrent().compareTo(Amt_15L) >= 1 
				&& incentive.getCollectionTowardsCurrent().compareTo(Amt_30L) < 1) {
			currentIncentive = Amt_15L.multiply(Percentage2).setScale(2, RoundingMode.UP);
			currentIncentive = currentIncentive.add((incentive.getCollectionTowardsCurrent().subtract(Amt_15L)).multiply(Percentage2_5).setScale(2, RoundingMode.UP));
		} else {
			currentIncentive = Amt_15L.multiply(Percentage2).setScale(2, RoundingMode.UP);
			currentIncentive = currentIncentive.add(Amt_15L.multiply(Percentage2_5).setScale(2, RoundingMode.UP));
			currentIncentive = currentIncentive.add((incentive.getCollectionTowardsCurrent().subtract(Amt_30L)).multiply(Percentage3).setScale(2, RoundingMode.UP));
		}
		return currentIncentive;
	}

	private BigDecimal calculatePTArrearCollectionIncentive(IncentiveAnalysis incentive) {
		BigDecimal arrearIncentive = BigDecimal.ZERO;
		if(incentive.getCollectionTowardsArrear().compareTo(Amt_15L) < 1 ) {
			arrearIncentive = incentive.getCollectionTowardsArrear().multiply(Percentage3).setScale(2, RoundingMode.UP);
		} else if(incentive.getCollectionTowardsArrear().compareTo(Amt_15L) >= 1 
				&& incentive.getCollectionTowardsArrear().compareTo(Amt_30L) < 1) {
			arrearIncentive = Amt_15L.multiply(Percentage3).setScale(2, RoundingMode.UP);
			arrearIncentive = arrearIncentive.add((incentive.getCollectionTowardsArrear().subtract(Amt_15L)).multiply(Percentage4).setScale(2, RoundingMode.UP));
		} else {
			arrearIncentive = Amt_15L.multiply(Percentage3).setScale(2, RoundingMode.UP);
			arrearIncentive = arrearIncentive.add(Amt_15L.multiply(Percentage4).setScale(2, RoundingMode.UP));
			arrearIncentive = arrearIncentive.add((incentive.getCollectionTowardsArrear().subtract(Amt_30L)).multiply(Percentage5).setScale(2, RoundingMode.UP));
		}
		return arrearIncentive;
	}

	private void calculateWaterIncentive(Map<String, IncentiveAnalysis> incentiveAnalysis) {
		incentiveAnalysis.values().stream().forEach(incentive -> {
			incentive.setTotalIncentive(incentive.getTotalCollection().multiply(Percentage5).setScale(2, RoundingMode.UP));
		});
	}

}
