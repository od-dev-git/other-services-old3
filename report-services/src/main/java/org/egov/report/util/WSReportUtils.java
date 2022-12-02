package org.egov.report.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.egov.report.service.WaterService;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.BeanProperty.Bogus;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WSReportUtils {
	
	public Long addOneMonth(Long time) {
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT")); 
		cal.setTimeInMillis(time);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR, 11);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set( Calendar.AM_PM, Calendar.PM);
		return cal.getTimeInMillis();
	}
	
	public Long formatFromDate(Long fromDate) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(fromDate);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 1);
		return cal.getTimeInMillis();
	}
	
	public Long getFirstDayOfMonthYear(Long date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(date);
		cal.set(Calendar.DATE,1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis();	
	}
	
	   public Long getStartingTimeOfFirstDayOfMonthYear(Long date) {
	        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
	        cal.setTimeInMillis(date);
	        cal.set(Calendar.DATE,1);
	        cal.set(Calendar.HOUR, 0);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set( Calendar.AM_PM, Calendar.AM);
	        return cal.getTimeInMillis();   
	    }
	
	public String getConvertedDate(Long time) {
		
		if(time == 0 || time == null )
			return null;
		
		Date date = new Date(time);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		format.setTimeZone(TimeZone.getTimeZone("IST"));
		String formatted = format.format(date);
		return formatted;
	}
	
	public BigDecimal CalculateAmtAfterDueDate(BigDecimal taxAmt, BigDecimal collectedAmt, BigDecimal penaltyAmt,
			BigDecimal advanceAmt, BigDecimal arrears, BigDecimal arrearsCollectedAmt) {

		BigDecimal multiplyingFactor = new BigDecimal(1.05);
		BigDecimal amtAfterDueDate = ((taxAmt.subtract(collectedAmt)).multiply(multiplyingFactor)).add(advanceAmt)
				.add(penaltyAmt).add(arrears).subtract(arrearsCollectedAmt);

		if (amtAfterDueDate.compareTo(BigDecimal.ZERO) == 1)
			return amtAfterDueDate.setScale(0, RoundingMode.HALF_UP);
		else
			return BigDecimal.ZERO;
	}

	public BigDecimal CalculateAmtBeforeDueDate(BigDecimal taxAmt, BigDecimal collectedAmt, BigDecimal penaltyAmt,
			BigDecimal advanceAmt, BigDecimal arrears, BigDecimal rebateAmt, BigDecimal arrearsCollectedAmt) {

		BigDecimal multiplyingFactor = new BigDecimal(0.98);
		BigDecimal amtBeforeDueDate = ((taxAmt.subtract(collectedAmt)).multiply(multiplyingFactor)).add(advanceAmt)
				.add(rebateAmt).add(arrears).subtract(arrearsCollectedAmt);

		if (amtBeforeDueDate.compareTo(BigDecimal.ZERO) == 1)
			return amtBeforeDueDate.setScale(0, RoundingMode.HALF_UP);
		else
			return BigDecimal.ZERO;
	}

	public BigDecimal calculateTotalDue(BigDecimal taxAmt, BigDecimal collectedAmt, BigDecimal penaltyAmt,
			BigDecimal advanceAmt, BigDecimal arrears, BigDecimal arrearsCollectedAmt) {

		BigDecimal totalDue = taxAmt.add(penaltyAmt).add(advanceAmt).add(arrears).subtract(collectedAmt).subtract(arrearsCollectedAmt);

		if (totalDue.compareTo(BigDecimal.ZERO) == 1)
			return totalDue.setScale(2, BigDecimal.ROUND_CEILING);
		else
			return BigDecimal.ZERO;
	}

    public BigDecimal CalculateAmtAfterDueDateModified(BigDecimal taxAmt, BigDecimal collectedAmt,BigDecimal sewageTaxAmt, BigDecimal sewageCollectedAmt,
            BigDecimal penaltyAmt, BigDecimal advanceAmt, BigDecimal totalArrearAmt) {
        BigDecimal multiplyingFactor = new BigDecimal(1.05);
        BigDecimal amtAfterDueDate = BigDecimal.ZERO;
        if (penaltyAmt.compareTo(BigDecimal.ZERO) == 0) {
            amtAfterDueDate =((sewageTaxAmt.add(taxAmt))
                    .multiply(multiplyingFactor))
                    .add(advanceAmt)
                    .add(totalArrearAmt)
                    .subtract(sewageCollectedAmt)
                    .subtract(collectedAmt);    
        }else {
            amtAfterDueDate =(sewageTaxAmt.add(taxAmt))
                    .add(advanceAmt)
                    .add(penaltyAmt)
                    .add(totalArrearAmt)
                    .subtract(sewageCollectedAmt)
                    .subtract(collectedAmt);
        }
        
        if (amtAfterDueDate.compareTo(BigDecimal.ZERO) == 1)
            return amtAfterDueDate.setScale(0, RoundingMode.HALF_UP);
        else
            return BigDecimal.ZERO;
    }

    public BigDecimal CalculateAmtBeforeDueDateModified(BigDecimal taxAmt, BigDecimal collectedAmt,BigDecimal sewageTaxAmt, BigDecimal sewageCollectedAmt,
            BigDecimal penaltyAmt, BigDecimal advanceAmt, BigDecimal totalArrearAmt, BigDecimal rebateAmt) {
        BigDecimal multiplyingFactor = new BigDecimal(0.98);
        BigDecimal amtBeforeDueDate = BigDecimal.ZERO;
        
        if (rebateAmt.compareTo(BigDecimal.ZERO) == 0) {
            amtBeforeDueDate =((sewageTaxAmt.add(taxAmt))
                    .multiply(multiplyingFactor))
                    .add(advanceAmt)
                    .add(totalArrearAmt)
                    .subtract(sewageCollectedAmt)
                    .subtract(collectedAmt);    
        }else {
            amtBeforeDueDate =(sewageTaxAmt.add(taxAmt))
                    .add(advanceAmt)
                    .add(rebateAmt)
                    .add(totalArrearAmt)
                    .subtract(sewageCollectedAmt)
                    .subtract(collectedAmt); 
        }
        
        if (amtBeforeDueDate.compareTo(BigDecimal.ZERO) == 1)
            return amtBeforeDueDate.setScale(0, RoundingMode.HALF_UP);
        else
            return BigDecimal.ZERO;
    }

    public BigDecimal calculateTotalDueModified(BigDecimal taxAmt, BigDecimal collectedAmt,BigDecimal sewageTaxAmt, BigDecimal sewageCollectedAmt, BigDecimal specialRebateAmt,
            BigDecimal advanceAmt, BigDecimal totalArrearAmt) {//add special rebate  remove penalty
        BigDecimal totalDue = taxAmt.add(sewageTaxAmt)
                .add(specialRebateAmt)
                .add(advanceAmt)
                .add(totalArrearAmt)
                .subtract(collectedAmt)
                .subtract(sewageCollectedAmt);
        if (totalDue.compareTo(BigDecimal.ZERO) == 1)
            return totalDue.setScale(2, BigDecimal.ROUND_CEILING);
        else
            return BigDecimal.ZERO;
    }
}
