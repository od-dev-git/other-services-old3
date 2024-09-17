package org.egov.report.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.service.DemandService;
import org.egov.report.repository.DemandRepository;
import org.egov.report.util.DemandEnrichmentUtil;
//import org.egov.report.repository.ServiceRequestRepository;
import org.egov.report.util.Util;
import org.egov.report.validator.DemandValidatorV1;
//import org.egov.demand.web.contract.User;
//import org.egov.demand.web.contract.UserResponse;
//import org.egov.demand.web.contract.UserSearchRequest;
import org.egov.report.repository.DemandRepository;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.ConsumerDemandAuditRequest;
import org.egov.report.model.Demand;
import org.egov.report.model.DemandDetailAudit;
import org.egov.report.model.DemandDetailAuditRequest;
import org.egov.report.model.DemandDetailAuditResponse;
import org.egov.report.model.UserSearchRequest;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.web.contract.User;
import org.egov.report.web.contract.UserResponse;
import org.egov.report.web.model.DemandCriteria;
import org.egov.tracer.model.CustomException;
//import org.egov.report.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DemandService {
    
    @Autowired
    private Util util;

    @Autowired
    private DemandValidatorV1 demandValidatorV1;
    
    @Autowired
    private ReportServiceConfiguration applicationProperties;
    
    @Autowired
    private ServiceRepository serviceRequestRepository;
    
    @Autowired
    private DemandRepository demandRepository;
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private DemandEnrichmentUtil demandEnrichmentUtil;
    
    @Autowired
    private UserService userService;
    

//    @Value("${kafka.topic.ws.installment.update}")
//    private String wsInstallmentUpdateTopic;

    public List<Demand> getDemands(DemandCriteria demandCriteria, RequestInfo requestInfo) {

        demandValidatorV1.validateDemandCriteria(demandCriteria, requestInfo);

        UserSearchRequest userSearchRequest = null;
        List<User> payers = null;
        List<Demand> demands = null;
        
//        String userUri = applicationProperties.getUserServiceHostName()
//                .concat(applicationProperties.getUserServiceSearchPath());
        
//        String userUri = applicationProperties.getUserHost()
//                .concat(applicationProperties.getUserSearchEndpoint());
        
        /*
         * user type is CITIZEN by default because only citizen can have demand or payer can be null
         */
        String citizenTenantId = demandCriteria.getTenantId().split("\\.")[0];
        
        /*
         * If payer related data is provided first then user search has to be made first followed by demand search
         */
        if (demandCriteria.getEmail() != null || demandCriteria.getMobileNumber() != null) {
            
//            userSearchRequest = UserSearchRequest.builder().requestInfo(requestInfo)
//                    .tenantId(citizenTenantId).emailId(demandCriteria.getEmail())
//                    .mobileNumber(demandCriteria.getMobileNumber()).build();
//            
//            StringBuilder userUrl = new StringBuilder(userUri);
//            payers = mapper.convertValue(serviceRequestRepository.fetchResult(userUrl, userSearchRequest), UserResponse.class).getUser();
//            
//            if(CollectionUtils.isEmpty(payers))
//                return new ArrayList<>();
//            
//            Set<String> ownerIds = payers.stream().map(User::getUuid).collect(Collectors.toSet());
//            demandCriteria.setPayer(ownerIds);
            demands = demandRepository.getDemands(demandCriteria);
            
        } else {
            
            /*
             * If no payer related data given then search demand first then enrich payer(user) data
             */
            demands = demandRepository.getDemands(demandCriteria);
            if (!demands.isEmpty()) {

                Set<String> payerUuids = demands.stream().filter(demand -> null != demand.getPayer())
                        .map(demand -> demand.getPayer().getUuid()).collect(Collectors.toSet());

//                if (!CollectionUtils.isEmpty(payerUuids)) {
//
//                    userSearchRequest = UserSearchRequest.builder().requestInfo(requestInfo).uuid(payerUuids).build();
//
//                    StringBuilder userUrl = new StringBuilder(userUri);
//                    payers = mapper.convertValue(serviceRequestRepository.fetchResult(userUrl, userSearchRequest),
//                            UserResponse.class).getUser();
//                }
            }
        }
        
        if (!CollectionUtils.isEmpty(demands) && !CollectionUtils.isEmpty(payers))
            demands = demandEnrichmentUtil.enrichPayer(demands, payers);

        return demands;

    }
    
	public DemandDetailAuditResponse getDemandDetailAudit(ConsumerDemandAuditRequest request) {
		log.info("Starting getDemandDetailAudit with request: {}", request);

		// Validate request
		validateDemandAuditRequest(request);

		// Get the demand details from the repository
		List<DemandDetailAudit> demandDetails = demandRepository
				.findDemandDetails(request.getDemandDetailAuditRequest());
		log.info("Found {} demand details from repository", demandDetails.size());

		// Check if demand details are empty
		if (demandDetails.isEmpty()) {
			log.info("No demand details found for the provided request.");
			return DemandDetailAuditResponse.builder().totalRecords(0)
					.pageNumber(request.getDemandDetailAuditRequest().getPageNumber())
					.pageSize(request.getDemandDetailAuditRequest().getPageSize())
					.demandDetails(Collections.emptyList()).build();
		}

		// Collect distinct non-null UUIDs for both createdBy and lastModifiedBy
		List<String> allUuids = Stream
				.concat(demandDetails.stream().map(DemandDetailAudit::getDemandDetailCreatedby),
						demandDetails.stream().map(DemandDetailAudit::getDemandDetailLastmodifiedby))
				.filter(Objects::nonNull).distinct() // Ensure distinct elements
				.collect(Collectors.toList());

		log.info("Collected {} unique UUIDs for user details", allUuids.size());

		// Fetch user details
		Map<String, org.egov.report.user.User> userDetailResponse = getUserDetails(request.getRequestInfo(), allUuids);
		log.info("Fetched user details for UUIDs: {}", allUuids);

		// Enrich demand details with user information
		util.enrichOwnerDetailsInDemandDetailAudit(userDetailResponse, demandDetails);

		// Create and return the response
		DemandDetailAuditResponse response = DemandDetailAuditResponse.builder().totalRecords(demandDetails.size())
				.pageNumber(request.getDemandDetailAuditRequest().getPageNumber())
				.pageSize(request.getDemandDetailAuditRequest().getPageSize()).demandDetails(demandDetails).build();
		log.info("Returning response with total records: {}", response.getTotalRecords());

		return response;
	}

	private void validateDemandAuditRequest(ConsumerDemandAuditRequest request) {
		log.info("Validating DemandAuditRequest: {}", request);

		// Validate the main request
		if (request == null) {
			throw new CustomException("IllegalArgumentException", "DemandAuditRequest cannot be null.");
		}
		if (request.getRequestInfo() == null) {
			throw new CustomException("IllegalArgumentException", "RequestInfo cannot be null.");
		}

		// Validate the audit request object
		DemandDetailAuditRequest auditRequest = request.getDemandDetailAuditRequest();
		if (auditRequest == null) {
			throw new CustomException("IllegalArgumentException", "DemandDetailAuditRequest cannot be null.");
		}

		// Validate mandatory fields
		validateMandatoryFields(auditRequest);

		// Validate page size constraints
		if (auditRequest.getPageSize() > applicationProperties.getDefaultPageSizeMax()) {
			throw new CustomException("IllegalArgumentException",
					"PageSize cannot exceed " + applicationProperties.getDefaultPageSizeMax());
		}

		// Set default page number and size
		if (auditRequest.getPageNumber() <= 0) {
			auditRequest.setPageNumber(applicationProperties.getDefaultPageNumber());
			log.info("Setting default pageNumber to {}", applicationProperties.getDefaultPageNumber());
		}
		if (auditRequest.getPageSize() <= 0) {
			auditRequest.setPageSize(applicationProperties.getDefaultPageSize());
			log.info("Setting default pageSize to {}", applicationProperties.getDefaultPageSize());
		}

		// Validate tax period if provided
		validateTaxPeriod(auditRequest);
	}

	private void validateMandatoryFields(DemandDetailAuditRequest auditRequest) {
		if (auditRequest.getConsumercode() == null || auditRequest.getConsumercode().isEmpty()) {
			throw new CustomException("IllegalArgumentException",
					"ConsumerCode is mandatory and cannot be null or empty.");
		}

		if (auditRequest.getTenantId() == null || auditRequest.getTenantId().isEmpty()) {
			throw new CustomException("IllegalArgumentException", "TenantId is mandatory and cannot be null or empty.");
		}

		if (auditRequest.getDemandid() != null && auditRequest.getDemandid().isEmpty()) {
			throw new CustomException("IllegalArgumentException", "DemandId cannot be empty if provided.");
		}
	}

	private void validateTaxPeriod(DemandDetailAuditRequest auditRequest) {
		if (auditRequest.getTaxperiodfrom() != null && auditRequest.getTaxperiodto() != null) {
			if (auditRequest.getTaxperiodfrom() > auditRequest.getTaxperiodto()) {
				throw new CustomException("IllegalArgumentException",
						"Tax period from cannot be greater than tax period to.");

			}
		}

		if (auditRequest.getTaxperiodfrom() != null && auditRequest.getTaxperiodfrom() < 0) {
			throw new CustomException("IllegalArgumentException", "Tax period from cannot be a negative value.");

		}

		if (auditRequest.getTaxperiodto() != null && auditRequest.getTaxperiodto() < 0) {
			throw new CustomException("IllegalArgumentException", "Tax period to cannot be a negative value.");
		}
	}

	private Map<String, org.egov.report.user.User> getUserDetails(RequestInfo requestInfo, List<String> userIds) {
		// Validate input parameters
		if (requestInfo == null) {
			log.info("RequestInfo is null, cannot fetch user details");
			return Collections.emptyMap();
		}

		if (userIds == null || userIds.isEmpty()) {
			log.info("User IDs list is null or empty, returning empty user map");
			return Collections.emptyMap();
		}

		// Build search criteria
		org.egov.report.user.UserSearchCriteria userSearchCriteria = org.egov.report.user.UserSearchCriteria.builder()
				.uuid(userIds).build();

		List<org.egov.report.user.User> usersInfo = Collections.emptyList();
		try {
			// Fetch user details
			usersInfo = userService.searchUsers(userSearchCriteria, requestInfo);
			log.info("User details fetched successfully");
		} catch (Exception e) {
			log.error("Failed to fetch user details", e);
		}

		// Convert list to map
		Map<String, org.egov.report.user.User> userMap = usersInfo.stream().filter(Objects::nonNull) // Filter out null
																										// users
				.collect(Collectors.toMap(org.egov.report.user.User::getUuid, Function.identity(),
						(existing, replacement) -> existing));

		return userMap;
	}

}
