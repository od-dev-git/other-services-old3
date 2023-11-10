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

	public BigDecimal calculateIncentives(String module, BigDecimal currentCollection, BigDecimal arrearCollection,
			BigDecimal totalCollection) {
		BigDecimal totalIncentive = BigDecimal.ZERO;
		if (module.equalsIgnoreCase(DashboardConstants.WATER_MODULE)) {
			totalIncentive = calculateWaterIncentive(totalCollection);
		} else if (module.equalsIgnoreCase(DashboardConstants.PROPERTY_MODULE)) {
			totalIncentive = calculatePropertyIncentive(currentCollection, arrearCollection);
		}
		return totalIncentive;
	}

	private BigDecimal calculatePropertyIncentive(BigDecimal currentCollection, BigDecimal arrearCollection) {
		    BigDecimal incentiveOnArrear = calculatePTArrearCollectionIncentive(arrearCollection);
		    BigDecimal incentiveOnCurrent = calculatePTCurrentCollectionIncentive(currentCollection);
		    BigDecimal totalPropertyIncentive = incentiveOnArrear.add(incentiveOnCurrent);	
		    return totalPropertyIncentive;
		
	}

	private BigDecimal calculatePTCurrentCollectionIncentive(BigDecimal currentColleciton) {
		BigDecimal currentIncentive = BigDecimal.ZERO;
		if(currentColleciton == null)
			currentColleciton = BigDecimal.ZERO;
		if (currentColleciton.compareTo(Amt_15L) < 1) {
			currentIncentive = currentColleciton.multiply(Percentage2).setScale(2, RoundingMode.UP);
		} else if (currentColleciton.compareTo(Amt_15L) >= 1 && currentColleciton.compareTo(Amt_30L) < 1) {
			currentIncentive = Amt_15L.multiply(Percentage2).setScale(2, RoundingMode.UP);
			currentIncentive = currentIncentive
					.add((currentColleciton.subtract(Amt_15L)).multiply(Percentage2_5).setScale(2, RoundingMode.UP));
		} else {
			currentIncentive = Amt_15L.multiply(Percentage2).setScale(2, RoundingMode.UP);
			currentIncentive = currentIncentive.add(Amt_15L.multiply(Percentage2_5).setScale(2, RoundingMode.UP));
			currentIncentive = currentIncentive
					.add((currentColleciton.subtract(Amt_30L)).multiply(Percentage3).setScale(2, RoundingMode.UP));
		}
		return currentIncentive;
	}

	private BigDecimal calculatePTArrearCollectionIncentive(BigDecimal arrearCollection) {
		BigDecimal arrearIncentive = BigDecimal.ZERO;
		if(arrearCollection == null)
			arrearCollection = BigDecimal.ZERO;
		if(arrearCollection.compareTo(Amt_15L) < 1 ) {
			arrearIncentive = arrearCollection.multiply(Percentage3).setScale(2, RoundingMode.UP);
		} else if(arrearCollection.compareTo(Amt_15L) >= 1 
				&& arrearCollection.compareTo(Amt_30L) < 1) {
			arrearIncentive = Amt_15L.multiply(Percentage3).setScale(2, RoundingMode.UP);
			arrearIncentive = arrearIncentive.add((arrearCollection.subtract(Amt_15L)).multiply(Percentage4).setScale(2, RoundingMode.UP));
		} else {
			arrearIncentive = Amt_15L.multiply(Percentage3).setScale(2, RoundingMode.UP);
			arrearIncentive = arrearIncentive.add(Amt_15L.multiply(Percentage4).setScale(2, RoundingMode.UP));
			arrearIncentive = arrearIncentive.add((arrearCollection.subtract(Amt_30L)).multiply(Percentage5).setScale(2, RoundingMode.UP));
		}
		return arrearIncentive;
	}

	private BigDecimal calculateWaterIncentive(BigDecimal totalCollection) {
		return totalCollection.multiply(Percentage5).setScale(2, RoundingMode.UP);
	}

}
