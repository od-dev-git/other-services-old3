package org.egov.mrcalculator.service;

import static org.egov.mrcalculator.utils.MRCalculatorConstants.businessService_MR;
import static org.egov.mr.util.MRConstants.MDMS_MARRIAGE_REGISTRATION_FEE_DETAILS;
import static org.egov.mr.util.MRConstants.MDMS_AFTER_30_DAYS_OF_MARRIAGE;
import static org.egov.mr.util.MRConstants.MDMS_WITHIN_30_DAYS_OF_MARRIAGE;
import static org.egov.mr.util.MRConstants.MDMS_AFTER_ONE_YEAR_OF_MARRIAGE;
import static org.egov.mr.util.MRConstants.MDMS_WITHIN_ONE_YEAR_OF_MARRIAGE;
import static org.egov.mr.util.MRConstants.MDMS_TATKAL_REGISTRATION_FEES;
import static org.egov.mr.util.MRConstants.MDMS_CHALLAN_FEE;
import static org.egov.mr.util.MRConstants.MDMS_REGISTRATION_FEE;
import static org.egov.mr.util.MRConstants.MDMS_DEVELOPMENT_FEE;
import static org.egov.mr.util.MRConstants.MDMS_REDCROSS_FEE;
import static org.egov.mr.util.MRConstants.MDMS_USER_FEE;
import static org.egov.mr.util.MRConstants.TENANT_ID;
import static org.egov.mr.util.MRConstants.MDMS_COST;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.producer.Producer;
import org.egov.mr.util.MarriageRegistrationUtil;
import org.egov.mr.web.models.MarriageRegistration;
import org.egov.mr.web.models.calculation.CalculationReq;
import org.egov.mr.web.models.calculation.CalculationRes;
import org.egov.mr.web.models.calculation.Category;
import org.egov.mr.web.models.calculation.TaxHeadEstimate;
import org.egov.mrcalculator.config.MRCalculatorConfigs;
import org.egov.mrcalculator.repository.BillingslabRepository;
import org.egov.mrcalculator.repository.builder.BillingslabQueryBuilder;
import org.egov.mrcalculator.utils.CalculationUtils;
import org.egov.mrcalculator.web.models.BillingSlab;
import org.egov.mrcalculator.web.models.BillingSlabSearchCriteria;
import org.egov.mrcalculator.web.models.Calculation;
import org.egov.mrcalculator.web.models.CalulationCriteria;
import org.egov.mrcalculator.web.models.EstimatesAndSlabs;
import org.egov.mrcalculator.web.models.FeeAndBillingSlabIds;
import org.egov.mrcalculator.web.models.enums.CalculationType;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.JsonPath;

import ch.qos.logback.core.net.SyslogOutputStream;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;


@Service
@Slf4j
public class CalculationService {


	@Autowired
	private BillingslabRepository repository;

	@Autowired
	private BillingslabQueryBuilder queryBuilder;

	@Autowired
	private MRCalculatorConfigs config;


	@Autowired
	private CalculationUtils utils;

	@Autowired
	private MarriageRegistrationUtil mrUtils;

	@Autowired
	private DemandService demandService;

	@Autowired
	private Producer producer;


	/**
	 * Calculates tax estimates and creates demand
	 * @param calculationReq The calculationCriteria request
	 * @return List of calculations for all applicationNumbers or marriageRegistrations in calculationReq
	 */
	public List<Calculation> calculate(CalculationReq calculationReq){
		String tenantId = calculationReq.getCalulationCriteria().get(0).getTenantId();
		List<Calculation> calculations = getCalculation(calculationReq.getRequestInfo(),
				calculationReq.getCalulationCriteria());
		demandService.generateDemand(calculationReq.getRequestInfo(),calculations,businessService_MR);
		CalculationRes calculationRes = CalculationRes.builder().calculations(calculations).build();
		//producer.push(config.getSaveTopic(),calculationRes);
		return calculations;
	}

	/**
	 * Calculates tax estimates 
	 * @param calculationReq The calculationCriteria request
	 * @return List of calculations for all applicationNumbers or marriageRegistrations in calculationReq
	 */
	public List<Calculation> estimate(CalculationReq calculationReq){
		String tenantId = calculationReq.getCalulationCriteria().get(0).getTenantId();
		List<Calculation> calculations = getCalculation(calculationReq.getRequestInfo(),
				calculationReq.getCalulationCriteria());
		return calculations;
	}


	/***
	 * Calculates tax estimates
	 * @param requestInfo The requestInfo of the calculation request
	 * @param criterias list of CalculationCriteria containing the marriageRegistration or applicationNumber
	 * @return  List of calculations for all applicationNumbers or marriageRegistrations in criterias
	 */
	public List<Calculation> getCalculation(RequestInfo requestInfo, List<CalulationCriteria> criterias){
		List<Calculation> calculations = new LinkedList<>();
		for(CalulationCriteria criteria : criterias) {
			MarriageRegistration marriageRegistration;
			if (criteria.getMarriageRegistration()==null && criteria.getApplicationNumber() != null) {
				marriageRegistration = utils.getMarriageRegistration(requestInfo, criteria.getApplicationNumber(), criteria.getTenantId());
				criteria.setMarriageRegistration(marriageRegistration);
			}
			EstimatesAndSlabs estimatesAndSlabs = getTaxHeadEstimates(criteria,requestInfo);
			List<TaxHeadEstimate> taxHeadEstimates = estimatesAndSlabs.getEstimates();
			FeeAndBillingSlabIds feeAndBillingSlabIds = estimatesAndSlabs.getFeeAndBillingSlabIds();

			Calculation calculation = new Calculation();
			calculation.setMarriageRegistration(criteria.getMarriageRegistration());
			calculation.setTenantId(criteria.getTenantId());
			calculation.setTaxHeadEstimates(taxHeadEstimates);
			calculation.setBillingIds(feeAndBillingSlabIds);

			calculations.add(calculation);

		}
		return calculations;
	}


	/**
	 * Creates TacHeadEstimates
	 * @param calulationCriteria CalculationCriteria containing the marriageRegistration or applicationNumber
	 * @param requestInfo The requestInfo of the calculation request
	 * @return TaxHeadEstimates and the billingSlabs used to calculate it
	 */
	private EstimatesAndSlabs getTaxHeadEstimates(CalulationCriteria calulationCriteria, RequestInfo requestInfo){
		List<TaxHeadEstimate> estimates = new LinkedList<>();
		EstimatesAndSlabs  estimatesAndSlabs = getBaseTax(calulationCriteria,requestInfo);

		estimates.addAll(estimatesAndSlabs.getEstimates());

		estimatesAndSlabs.setEstimates(estimates);

		return estimatesAndSlabs;
	}


	/**
	 * Calculates base tax and cretaes its taxHeadEstimate
	 * @param calulationCriteria CalculationCriteria containing the marriageRegistration or applicationNumber
	 * @param requestInfo The requestInfo of the calculation request
	 * @return BaseTax taxHeadEstimate and billingSlabs used to calculate it
	 */
	private EstimatesAndSlabs getBaseTax(CalulationCriteria calulationCriteria, RequestInfo requestInfo){
		MarriageRegistration marriageRegistration = calulationCriteria.getMarriageRegistration();
		EstimatesAndSlabs estimatesAndSlabs = new EstimatesAndSlabs();
		BillingSlabSearchCriteria searchCriteria = new BillingSlabSearchCriteria();
		searchCriteria.setTenantId(marriageRegistration.getTenantId());


		String calculationType = config.getDefaultCalculationType();

		List<TaxHeadEstimate> estimateList = getBillingSlabIds(marriageRegistration,CalculationType
				.fromValue(calculationType),requestInfo);


		estimatesAndSlabs.setEstimates(estimateList);

		return estimatesAndSlabs;
	}




	/**
	 * @param  marriageRegistration for which fee has to be calculated
	 * @param calculationType Calculation logic to be used
	 * @return  Fee and billingSlab used to calculate it
	 */
	private List<TaxHeadEstimate> getBillingSlabIds(MarriageRegistration marriageRegistration, CalculationType calculationType , RequestInfo requestInfo){

		List<TaxHeadEstimate> estimateList = new ArrayList<>();

		if(marriageRegistration.getTenantId()!=null)
		{
			//    		   List<Object> preparedStmtList = new ArrayList<>();
			//    		   BillingSlabSearchCriteria searchCriteria = new BillingSlabSearchCriteria();
			//    		   searchCriteria.setTenantId(marriageRegistration.getTenantId());
			//
			//    		   // Call the Search
			//    		   String query = queryBuilder.getSearchQuery(searchCriteria, preparedStmtList);
			//    		   log.info("query "+query);
			//    		   log.info("preparedStmtList "+preparedStmtList.toString());
			//    		   List<BillingSlab> billingSlabs = repository.getDataFromDB(query, preparedStmtList);
			//
			//    		   if(billingSlabs.size()>1)
			//    			   throw new CustomException("BILLINGSLAB ERROR","Found multiple BillingSlabs for the given ULB");
			//    		   if(CollectionUtils.isEmpty(billingSlabs))
			//    		   {
			//    			   String[] tenantArray = marriageRegistration.getTenantId().split("\\.");
			//    			   String city = "" ;
			//    			   if(tenantArray.length>1)
			//    			   {
			//    				   city = tenantArray[1];
			//    			   }
			//    			   throw new CustomException("BILLINGSLAB ERROR","Found multiple BillingSlabs for the given ULB "+city+".");
			//    		   }
			//    		   System.out.println(" rate: "+billingSlabs.get(0).getRate());

			//    		   billingSlabIds.add(billingSlabs.get(0).getId());

			try {
				Object mdmsData = mrUtils.mDMSCall(marriageRegistration,requestInfo);

				List<LinkedHashMap<String,Object>> ulbWiseRateslist = JsonPath.read(mdmsData,MDMS_MARRIAGE_REGISTRATION_FEE_DETAILS);

				if(ulbWiseRateslist == null || ulbWiseRateslist.isEmpty())
				{
					throw new CustomException("BILLING ERROR","Billing rates Not Found  ");	
				}


				LinkedHashMap<String,Object> mdmsMap =  ulbWiseRateslist.get(0);

// Commenting the code for developmentFees,redcrossFees,userFees

				LinkedHashMap<String,Object> challanFeesMap  = (LinkedHashMap<String, Object>) mdmsMap.get(MDMS_CHALLAN_FEE);
				
				LinkedHashMap<String,Object> registrationFeesMap  = (LinkedHashMap<String, Object>) mdmsMap.get(MDMS_REGISTRATION_FEE);
				
				LinkedHashMap<String,Object> tatkalregistrationFeesMap  = (LinkedHashMap<String, Object>) mdmsMap.get(MDMS_TATKAL_REGISTRATION_FEES);
				
//				LinkedHashMap<String,Object> developmentFeesMap  = (LinkedHashMap<String, Object>) mdmsMap.get(MDMS_DEVELOPMENT_FEE);
				
//				LinkedHashMap<String,Object> redcrossFeesMap  = (LinkedHashMap<String, Object>) mdmsMap.get(MDMS_REDCROSS_FEE);
				
//				LinkedHashMap<String,Object> userFeesMap  = (LinkedHashMap<String, Object>) mdmsMap.get(MDMS_USER_FEE);



				if(challanFeesMap == null || challanFeesMap.isEmpty())
				{
					throw new CustomException("BILLING ERROR","Challan Fees Not Found  ");	
				}
				
				if(registrationFeesMap == null || registrationFeesMap.isEmpty())
				{
					throw new CustomException("BILLING ERROR","Registration Fee Not Found  ");	
				}
				
				if(tatkalregistrationFeesMap == null || tatkalregistrationFeesMap.isEmpty())
				{
					throw new CustomException("BILLING ERROR","Tatkal registration fee not configured.");	
				}
				
//				if(developmentFeesMap == null || developmentFeesMap.isEmpty())
//				{
//					throw new CustomException("BILLING ERROR","Development Fee Not Found  ");	
//				}
//				
//				if(redcrossFeesMap == null || redcrossFeesMap.isEmpty())
//				{
//					throw new CustomException("BILLING ERROR","Redcross Fee Not Found  ");	
//				}
//				
//				if(userFeesMap == null || userFeesMap.isEmpty())
//				{
//					throw new CustomException("BILLING ERROR","User Fee Not Found  ");	
//				}

				
					BigDecimal challanFee ;
					BigDecimal registrationFee ;
					BigDecimal tatkalRegistrationFees = BigDecimal.ZERO;
//					BigDecimal developmentFee ;
//					BigDecimal redcrossFee ;
//					BigDecimal userFee ;

					Long marriageDate = marriageRegistration.getMarriageDate();


					Calendar currentDate = new GregorianCalendar();
					TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
					currentDate.setTimeZone(timeZone);
					currentDate.set(Calendar.HOUR_OF_DAY, 23);
					currentDate.set(Calendar.MINUTE, 59);
					currentDate.set(Calendar.SECOND, 59);
					currentDate.set(Calendar.MILLISECOND, 0);

					Calendar fromDate = new GregorianCalendar(); 
					fromDate.setTimeInMillis(marriageDate);

					fromDate.add(Calendar.DAY_OF_MONTH, 30);

					Long calculatedDateAfter30Days = fromDate.getTimeInMillis();

					if(calculatedDateAfter30Days>=currentDate.getTimeInMillis())
					{
						challanFee = new BigDecimal(challanFeesMap.get(MDMS_WITHIN_30_DAYS_OF_MARRIAGE).toString());
					}else
						challanFee = new BigDecimal(challanFeesMap.get(MDMS_AFTER_30_DAYS_OF_MARRIAGE).toString());
                    
										
					Calendar fromDateForRegistration = new GregorianCalendar(); 
					
					fromDateForRegistration.setTimeInMillis(marriageDate);

					fromDateForRegistration.add(Calendar.YEAR, 1);

					Long calculatedDateAfterOneYear = fromDateForRegistration.getTimeInMillis();

					if(calculatedDateAfterOneYear>=currentDate.getTimeInMillis())
					{
						registrationFee = new BigDecimal(registrationFeesMap.get(MDMS_WITHIN_ONE_YEAR_OF_MARRIAGE).toString());
					}else
						registrationFee = new BigDecimal(registrationFeesMap.get(MDMS_AFTER_ONE_YEAR_OF_MARRIAGE).toString());
					
					if(marriageRegistration.getIsTatkalApplication() != null && marriageRegistration.getIsTatkalApplication() == Boolean.TRUE) {
						tatkalRegistrationFees = new BigDecimal(tatkalregistrationFeesMap.get(MDMS_COST).toString());
					}
						
//					developmentFee =  new BigDecimal(developmentFeesMap.get(MDMS_COST).toString());
//					
//					redcrossFee = new BigDecimal(redcrossFeesMap.get(MDMS_COST).toString());
//					
//					userFee = new BigDecimal(userFeesMap.get(MDMS_COST).toString());

					if(challanFee.compareTo(BigDecimal.ZERO)==-1)
						throw new CustomException("INVALID AMOUNT","Challan Fee amount is negative");

					if(registrationFee.compareTo(BigDecimal.ZERO)==-1)
						throw new CustomException("INVALID AMOUNT","Registration Fee amount is negative");
					
					if(tatkalRegistrationFees.compareTo(BigDecimal.ZERO)==-1)
						throw new CustomException("INVALID AMOUNT","Tatkal Registration Fee amount is negative");
					
//					if(developmentFee.compareTo(BigDecimal.ZERO)==-1)
//						throw new CustomException("INVALID AMOUNT","Development Fee amount is negative");
//					
//					if(redcrossFee.compareTo(BigDecimal.ZERO)==-1)
//						throw new CustomException("INVALID AMOUNT","Redcross Fee amount is negative");
//					
//					if(userFee.compareTo(BigDecimal.ZERO)==-1)
//						throw new CustomException("INVALID AMOUNT","User Fee amount is negative");

					
					
					TaxHeadEstimate challanFeeEstimate = new TaxHeadEstimate();

					challanFeeEstimate.setEstimateAmount(challanFee);
					challanFeeEstimate.setCategory(Category.FEE);

					challanFeeEstimate.setTaxHeadCode(config.getChallanFeeTaxHead());
					estimateList.add(challanFeeEstimate);
					
					
					boolean isPHC = false ;
					
					
					if((marriageRegistration.getCoupleDetails().get(0).getBride().getIsDivyang()!= null && marriageRegistration.getCoupleDetails().get(0).getBride().getIsDivyang() == true) 
							|| (marriageRegistration.getCoupleDetails().get(0).getGroom().getIsDivyang()!= null && marriageRegistration.getCoupleDetails().get(0).getGroom().getIsDivyang() == true))
					{
						isPHC = true ;
					}
					
					
					if(config.getPhcNoRegistrationFeesTenants()!= null && config.getPhcNoRegistrationFeesTenants().contains(marriageRegistration.getTenantId()) && isPHC)
					{
						log.info(" The bridge or groom is PHC so not adding Registration Fees ");
					}else
					{
						TaxHeadEstimate registrationFeeEstimate = new TaxHeadEstimate();

						registrationFeeEstimate.setEstimateAmount(registrationFee);
						registrationFeeEstimate.setCategory(Category.FEE);

						registrationFeeEstimate.setTaxHeadCode(config.getRegistrationFeeTaxHead());
						estimateList.add(registrationFeeEstimate);
						
					}
					
					if(marriageRegistration.getIsTatkalApplication() != null && marriageRegistration.getIsTatkalApplication() == Boolean.TRUE) {

						TaxHeadEstimate tatkalFeeEstimate = new TaxHeadEstimate();

						tatkalFeeEstimate.setEstimateAmount(tatkalRegistrationFees);
						tatkalFeeEstimate.setCategory(Category.FEE);

						tatkalFeeEstimate.setTaxHeadCode(config.getTatkalFeeTaxHead());
						estimateList.add(tatkalFeeEstimate);
					}
					
//					TaxHeadEstimate developmentFeeEstimate = new TaxHeadEstimate();
//
//					developmentFeeEstimate.setEstimateAmount(developmentFee);
//					developmentFeeEstimate.setCategory(Category.FEE);
//
//					developmentFeeEstimate.setTaxHeadCode(config.getDevelopmentFeeTaxHead());
//					estimateList.add(developmentFeeEstimate);
//					
//					TaxHeadEstimate redcrossFeeEstimate = new TaxHeadEstimate();
//
//					redcrossFeeEstimate.setEstimateAmount(redcrossFee);
//					redcrossFeeEstimate.setCategory(Category.FEE);
//
//					redcrossFeeEstimate.setTaxHeadCode(config.getRedcrossFeeTaxHead());
//					estimateList.add(redcrossFeeEstimate);
//					
//					TaxHeadEstimate userFeeEstimate = new TaxHeadEstimate();
//
//					userFeeEstimate.setEstimateAmount(userFee);
//					userFeeEstimate.setCategory(Category.FEE);
//
//					userFeeEstimate.setTaxHeadCode(config.getUserFeeTaxHead());
//					estimateList.add(userFeeEstimate);
					

				
				

			} catch (Exception e) {
				log.error(e.getMessage());
				throw new CustomException("BILLING ERROR","Error while fetching Billing rates ");	
			}






		}




		return estimateList;
	}




	/**
	 * Calculates total fee of by applying logic on list based on calculationType
	 * @param fees List of fee 
	 * @param calculationType Calculation logic to be used
	 * @return Total Fee
	 */
	private BigDecimal getTotalFee(List<BigDecimal> fees,CalculationType calculationType){
		BigDecimal totalFee = BigDecimal.ZERO;
		//Summation
		if(calculationType.equals(CalculationType.SUM))
			totalFee = fees.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

		//Average
		if(calculationType.equals(CalculationType.AVERAGE))
			totalFee = (fees.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
					.divide(new BigDecimal(fees.size()))).setScale(2,2);

		//Max
		if(calculationType.equals(CalculationType.MAX))
			totalFee = fees.stream().reduce(BigDecimal::max).get();

		//Min
		if(calculationType.equals(CalculationType.MIN))
			totalFee = fees.stream().reduce(BigDecimal::min).get();

		return totalFee;
	}





}
