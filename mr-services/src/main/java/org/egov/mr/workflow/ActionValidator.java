package org.egov.mr.workflow;

import static org.egov.mr.util.MRConstants.ACTION_APPLY;
import static org.egov.mr.util.MRConstants.ACTION_INITIATE;
import static org.egov.mr.util.MRConstants.STATUS_INITIATED;
import static org.egov.mr.util.MRConstants.businessService_MR;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.mr.util.MRConstants;
import org.egov.mr.web.models.Couple;
import org.egov.mr.web.models.MarriageRegistration;
import org.egov.mr.web.models.MarriageRegistrationRequest;
import org.egov.mr.web.models.workflow.BusinessService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class ActionValidator {



	private WorkflowService workflowService;

	@Autowired
	public ActionValidator( WorkflowService workflowService) {
		this.workflowService = workflowService;
	}




	/**
	 * Validates create request
	 * @param request The marriageRegistration Create request
	 */
	public void validateCreateRequest(MarriageRegistrationRequest request){
		Map<String, String> errorMap = new HashMap<>();
		Set<String> applicationTypes = new HashSet<>();
		request.getMarriageRegistrations().forEach(marriageRegistration -> {
			if(marriageRegistration.getApplicationType() != null ){
				applicationTypes.add(marriageRegistration.getApplicationType().toString());
			} 

			String businessService = marriageRegistration.getBusinessService();
			if (businessService == null)
				businessService = businessService_MR;

			switch(businessService)
			{
			case businessService_MR:


				if (ACTION_APPLY.equalsIgnoreCase(marriageRegistration.getAction())) {
					if (marriageRegistration.getApplicationDocuments() == null)
						errorMap.put("INVALID ACTION", "Action cannot be changed to APPLY. Application document are not provided");
				}
				if (!ACTION_APPLY.equalsIgnoreCase(marriageRegistration.getAction()) &&
						!ACTION_INITIATE.equalsIgnoreCase(marriageRegistration.getAction())) {
					errorMap.put("INVALID ACTION", "Action can only be APPLY or INITIATE during create");
				}
				break;


			}
		});
		//    validateRole(request);

		if(request.getMarriageRegistrations().size() > 1){
			if(applicationTypes.size() != 1){
				errorMap.put("INVALID APPLICATION TYPES", "Application Types should be identical for bulk requests");
			}
		}


		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}


	/**
	 * Validates the update request
	 * @param request The Marriage Registration update request
	 */
	public void validateUpdateRequest(MarriageRegistrationRequest request,BusinessService businessService){
		validateDocumentsForUpdate(request);
		validateAllMandatoryFiledsOnApply(request);
		// validateRole(request);
		// validateAction(request);
		validateIds(request,businessService);
	}


	/**
	 * Validates the applicationDocument
	 * @param request The Marriage Registration create or update request
	 */
	private void validateDocumentsForUpdate(MarriageRegistrationRequest request){
		Map<String,String> errorMap = new HashMap<>();
		request.getMarriageRegistrations().forEach(marriageRegistration -> {
			
			if(ACTION_APPLY.equalsIgnoreCase(marriageRegistration.getAction())){
				if(CollectionUtils.isEmpty(marriageRegistration.getApplicationDocuments()))
					errorMap.put("INVALID STATUS","Status cannot be APPLY when application document are not provided");
			}
		});

		if(!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}
	
	
	private void validateAllMandatoryFiledsOnApply(MarriageRegistrationRequest request){
		Map<String,String> errorMap = new HashMap<>();
		request.getMarriageRegistrations().forEach(marriageRegistration -> {
			
			if(ACTION_APPLY.equalsIgnoreCase(marriageRegistration.getAction())){
				if(CollectionUtils.isEmpty(marriageRegistration.getCoupleDetails()))
					errorMap.put("INVALID UPDATE","Couple Details are mandatory");
				
		        if(marriageRegistration.getApplicationType() != null && marriageRegistration.getApplicationType().toString().equals(MRConstants.APPLICATION_TYPE_NEW)){
		            validateWitnessDOB(marriageRegistration);
		        }
				
				
			}
		});

		if(!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

    private void validateWitnessDOB(MarriageRegistration marriageRegistration) {
        if (marriageRegistration == null) {
            log.error("Marriage Registration is null");
            throw new CustomException("INVALID_MARRIAGE_REGISTRATION", "Marriage Registration cannot be null");
        }

        List<Couple> coupleDetails = marriageRegistration.getCoupleDetails();
        if (coupleDetails == null || coupleDetails.isEmpty()) {
            log.error("Couple details are missing in Marriage Registration");
            throw new CustomException("INVALID_COUPLE_DETAILS", "Couple details cannot be null or empty");
        }

        for (Couple couple : coupleDetails) {
            if (couple.getBride() == null || couple.getBride().getWitness() == null) {
                log.error("Bride's witness details are missing");
                throw new CustomException("INVALID_WITNESS_DETAILS", "Bride's witness details cannot be null");
            }

            if (couple.getGroom() == null || couple.getGroom().getWitness() == null) {
                log.error("Groom's witness details are missing");
                throw new CustomException("INVALID_WITNESS_DETAILS", "Groom's witness details cannot be null");
            }

            Long bridesWitnessDOBMillis = couple.getBride().getWitness().getDateOfBirth();
            Long groomWitnessDOBMillis = couple.getGroom().getWitness().getDateOfBirth();

            if (bridesWitnessDOBMillis == null) {
                log.error("Bride's witness Date of Birth is missing");
                throw new CustomException("INVALID_DATE_OF_BIRTH", "Bride's witness Date of Birth cannot be null");
            }

            if (groomWitnessDOBMillis == null) {
                log.error("Groom's witness Date of Birth is missing");
                throw new CustomException("INVALID_DATE_OF_BIRTH", "Groom's witness Date of Birth cannot be null");
            }

            
         // Convert milliseconds to LocalDate
            LocalDate bridesWitnessDOB = Instant.ofEpochMilli(bridesWitnessDOBMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate groomWitnessDOB = Instant.ofEpochMilli(groomWitnessDOBMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate currentDate = LocalDate.now();

            // Check if witnesses are at least 18 years old
            if (Period.between(bridesWitnessDOB, currentDate).getYears() < 18) {
                log.error("Bride's witness is underage");
                throw new CustomException("INVALID_WITNESS_AGE", "Bride's witness must be at least 18 years old");
            }

            if (Period.between(groomWitnessDOB, currentDate).getYears() < 18) {
                log.error("Groom's witness is underage");
                throw new CustomException("INVALID_WITNESS_AGE", "Groom's witness must be at least 18 years old");
            }            
            
        }

        log.info("Witness Date of Birth validation completed successfully for marriage registration.");
    }








	/**
	 * Validates if the any new object is added in the request
	 * @param request The Marriage Registration update request
	 */
	private void validateIds(MarriageRegistrationRequest request,BusinessService businessService){
		Map<String,String> errorMap = new HashMap<>();
		request.getMarriageRegistrations().forEach(marriageRegistration -> {

			String namefBusinessService=marriageRegistration.getBusinessService();
			if((namefBusinessService==null) || (namefBusinessService.equals(businessService_MR))  && (!marriageRegistration.getStatus().equalsIgnoreCase(STATUS_INITIATED)))
			{
				if(!workflowService.isStateUpdatable(marriageRegistration.getStatus(), businessService)) {
					if (marriageRegistration.getId() == null)
						errorMap.put("INVALID UPDATE", "Id of marriageRegistration cannot be null");
					if(marriageRegistration.getMarriagePlace().getId()==null)
						errorMap.put("INVALID UPDATE", "Id of Marriage Place cannot be null");
					marriageRegistration.getCoupleDetails().forEach(couple -> {
						if(couple.getBride().getId()==null)
							errorMap.put("INVALID UPDATE", "Id of Bride cannot be null");

						if(couple.getBride().getAddress().getId()==null)
							errorMap.put("INVALID UPDATE", "Id of Bride Address cannot be null");
						
						if(couple.getBride().getGuardianDetails().getId()==null)
							errorMap.put("INVALID UPDATE", "Id of Bride Guardian Details cannot be null");
						
						if(couple.getBride().getWitness().getId()==null)
							errorMap.put("INVALID UPDATE", "Id of Couple Address cannot be null");

						if(couple.getGroom().getId()==null)
							errorMap.put("INVALID UPDATE", "Id of Groom cannot be null");

						if(couple.getGroom().getAddress().getId()==null)
							errorMap.put("INVALID UPDATE", "Id of Groom Address cannot be null");
						
						if(couple.getGroom().getGuardianDetails().getId()==null)
							errorMap.put("INVALID UPDATE", "Id of Groom Guardian Details cannot be null");
						
						if(couple.getGroom().getWitness().getId()==null)
							errorMap.put("INVALID UPDATE", "Id of Groom Witness cannot be null");
						
					});


					

					if(!CollectionUtils.isEmpty(marriageRegistration.getApplicationDocuments())){
						marriageRegistration.getApplicationDocuments().forEach(document -> {
							if(document.getId()==null)
								errorMap.put("INVALID UPDATE", "Id of applicationDocument cannot be null");
						});
					}
				}
			}
		});
		if(!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}





}
