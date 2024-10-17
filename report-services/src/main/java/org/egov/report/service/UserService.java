package org.egov.report.service;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.model.UserSearchRequest;
import org.egov.report.model.UtilityReportDetails;
import org.egov.report.model.UtilityReportSearchCriteria;
import org.egov.report.repository.DCBRepository;
import org.egov.report.repository.FileStoreRepository;
import org.egov.report.repository.ReportDao;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.repository.UserRepository;
import org.egov.report.util.EncryptionDecryptionUtil;
import org.egov.report.util.ReportConstants;
import org.egov.report.util.UserDetailsReportExcelGenerator;
import org.egov.report.web.model.OwnerInfo;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.report.web.model.User;
import org.egov.report.web.model.UserDetailResponse;
import org.egov.report.web.model.UtilityReportRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	private ServiceRepository repository;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ReportServiceConfiguration configuration;
	
	@Autowired
	private ReportDao reportDao;
	
	@Autowired
	private EncryptionDecryptionUtil encryptionDecryptionUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FileStoreRepository fileRepository;
	
	@Autowired
	private DCBRepository dcbRepository;
	
	@Autowired
	private EnrichmentService enrichmentService;
	
	@Autowired
	private FileStoreService fileStoreService;
	
	private UserSearchRequest getBaseUserSearchRequest(String tenantId, RequestInfo requestInfo) {
		return UserSearchRequest.builder().requestInfo(requestInfo).userType("EMPLOYEE").tenantId(tenantId).active(true)
				.build();
	}

	public List<OwnerInfo> getUser(RequestInfo requestInfo, List<Long> userIds) {
		List<User> users = reportDao.getEmployeeBaseTenant(userIds);
		Map<String, List<Long>> tenantWiseEmployeeMap = users.stream().collect(Collectors.groupingBy(User::getTenantId, Collectors.mapping(User::getId, Collectors.toList())));
		
		StringBuilder uri = new StringBuilder(configuration.getUserHost())
				.append(configuration.getUserSearchEndpoint());
		
		List<OwnerInfo> usersInfo = new ArrayList<>();
		
		for (String tenantId : tenantWiseEmployeeMap.keySet()) {
			UserSearchRequest userSearchRequest = getBaseUserSearchRequest(tenantId, requestInfo);
			userSearchRequest.setId(tenantWiseEmployeeMap.get(tenantId));
			try {
				Object response = repository.fetchResult(uri, userSearchRequest);
				UserDetailResponse userDetailResponse = mapper.convertValue(response, UserDetailResponse.class);
				usersInfo.addAll(userDetailResponse.getUser());
			} catch (Exception ex) {
				log.error("External Service call error", ex);
				throw new CustomException("USER_FETCH_EXCEPTION", "Unable to fetch user information");
			}
		}
		
		return usersInfo;
	}
	
	public List<OwnerInfo> getUserDetails(RequestInfo requestInfo, UserSearchCriteria userSearchCriteria){

		StringBuilder uri = new StringBuilder(configuration.getUserHost())
				.append(configuration.getUserSearchEndpoint());

		UserSearchRequest request = generateUserSearchRequest(requestInfo, userSearchCriteria);

		try {
			Object response = repository.fetchResult(uri, request);
			UserDetailResponse userDetailResponse = mapper.convertValue(response, UserDetailResponse.class);
			return userDetailResponse.getUser();
		}catch(Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("USER_FETCH_EXCEPTION", "Unable to fetch User Information");
		}
	}

	private UserSearchRequest generateUserSearchRequest(RequestInfo requestInfo, UserSearchCriteria criteria) {
		
		UserSearchRequest request = UserSearchRequest.builder().requestInfo(requestInfo).active(Boolean.TRUE).build();
		
		if(StringUtils.hasText(criteria.getTenantId())) {
			if(StringUtils.hasText(criteria.getUserType()) && UserSearchCriteria.CITIZEN.equals(criteria.getUserType()))
				request.setTenantId(ReportConstants.STATE_TENANT);
			else
				request.setTenantId(criteria.getTenantId());
		}
		
		if(!CollectionUtils.isEmpty(criteria.getUuid())) {
			request.setUuid(criteria.getUuid().stream().collect(Collectors.toSet()));
		}
		
		if(!CollectionUtils.isEmpty(criteria.getId())) {
			request.setId(criteria.getId().stream().collect(Collectors.toList()));
		}
		
		if(StringUtils.hasText(criteria.getUserType())) {
			request.setUserType(criteria.getUserType());
		}
		return request;
	}
	
	public List<org.egov.report.user.User> searchUsers(org.egov.report.user.UserSearchCriteria searchCriteria, RequestInfo requestInfo) {

		/*
		 * searchCriteria
		 * .setTenantId(getStateLevelTenantForCitizen(searchCriteria.getTenantId(),
		 * searchCriteria.getType()));
		 */

		searchCriteria = encryptionDecryptionUtil.encryptObject(searchCriteria, "UserSearchCriteria",
				org.egov.report.user.UserSearchCriteria.class);
		List<org.egov.report.user.User> list = userRepository.findAll(searchCriteria);

		list = encryptionDecryptionUtil.decryptObject(list, "UserList", org.egov.report.user.User.class, requestInfo);

		//setFileStoreUrlsByFileStoreIds(list);
		return list;
	}
	
	private void setFileStoreUrlsByFileStoreIds(List<org.egov.report.user.User> userList) {
		List<String> fileStoreIds = userList.parallelStream().filter(p -> p.getPhoto() != null).map(org.egov.report.user.User::getPhoto)
				.collect(Collectors.toList());
		if (!isEmpty(fileStoreIds)) {
			Map<String, String> fileStoreUrlList = null;
			try {
				fileStoreUrlList = fileRepository.getUrlByFileStoreId(userList.get(0).getTenantId(), fileStoreIds);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (fileStoreUrlList != null && !fileStoreUrlList.isEmpty()) {
				for (org.egov.report.user.User user : userList) {
					user.setPhoto(fileStoreUrlList.get(user.getPhoto()));
				}
			}
		}
	}

	public void generateUserDetailsReport(@Valid RequestInfoWrapper requestInfoWrapper,
			@Valid UserSearchCriteria userSearchCriteria) {

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		String reportType = ReportConstants.USERDETAILS_REPORT_STRING;

		List<String> tenantIds = userSearchCriteria.getTenantIds(); 

		validateUserDetailsReport(userSearchCriteria);

		if(CollectionUtils.isEmpty(tenantIds)) {
	        tenantIds = Arrays.asList(userSearchCriteria.getTenantId());
		}
		
		for (String tenantId : tenantIds) {
		
//		    String tenantId = "od";

			String financialYear = userSearchCriteria.getFinancialYear();

			List<UtilityReportDetails> reportList = dcbRepository.isReportExist(reportType, financialYear, tenantId);

			validateIfReportGenerationInProcess(reportList, tenantId);
			
			if(!reportList.isEmpty()) {
				UtilityReportDetails dcbReport = reportList.get(0);
				
				Long requestGap = configuration.getRequestGap();
				
				Long createdTime = dcbReport.getAuditDetails().getCreatedTime();
				
				Long currentTime = System.currentTimeMillis();
				
				if(currentTime < createdTime + requestGap) {
					log.info("Skipping... Report already present for tenantid: "+ tenantId);
					continue;
//		            return; // Use return to exit the method instead of continue

				}
				
			}
			reportList = new ArrayList<>();
			if (CollectionUtils.isEmpty(reportList)) {
				UtilityReportDetails reportDetails = enrichmentService.enrichSaveReport(requestInfo, reportType,
						financialYear, tenantId);
				dcbRepository.saveReportDetails(new UtilityReportRequest(requestInfo, reportDetails));
				reportList.add(reportDetails);
			}

			try {	
			
			List<Map<String, Object>> userDetailsList = userRepository.createUserDetailsReport(userSearchCriteria,tenantId);
			log.info("Total Users Fetched : " + userDetailsList.size());
			
	        Set<String> userIds = userDetailsList.stream()
	                .map(map -> (String) map.get("uuid"))
	                .filter(Objects::nonNull)
	                .collect(Collectors.toSet());
			
			
			// Path and filename for the excel file
			String fileName = generateFileName("UserDetails_Report", tenantId, financialYear);
			File temporaryfile = getTemporaryFile("UserDetails_Report", fileName);

			UserDetailsReportExcelGenerator generator = new UserDetailsReportExcelGenerator(userDetailsList);
	    	
	    	int limit = configuration.getReportLimit();
	    	int count = userDetailsList.size();
	        Map<String, User> userMap = new HashMap<>();

	        
	     // Set the limit for chunk size to 10,000 from configuration
	        int userSearchLimit = configuration.getUserServiceSearchLimit();

	        while (!userIds.isEmpty()) {
	            Set<String> chunk = userIds.stream()
	                    .limit(userSearchLimit)
	                    .collect(Collectors.toSet());

	            UserSearchCriteria usCriteria = UserSearchCriteria.builder()
	                    .uuid(chunk)
	                    .active(true)
	                    .userType(userSearchCriteria.getUserType())
	                    .tenantId(tenantId)
	                    .build();

	            try {
	                List<OwnerInfo> usersInfo = getUserDetails(requestInfo, usCriteria);

					userMap.putAll(usersInfo.stream().collect(Collectors.toMap(User::getUuid, Function.identity())));
	              
	                userIds.removeAll(chunk);
	            } catch (Exception e) {
	                System.err.println("Error fetching user details: " + e.getMessage());
	                e.printStackTrace();
	            }
	        }

	        System.out.println("Fetched user details for: " + userMap.size() + " users.");
	    
		
	        
	        AtomicInteger userCount = new AtomicInteger(0); // Using AtomicInteger for thread-safety if needed

	        userDetailsList.forEach(userDetail -> {
	            Object userId = userDetail.get("uuid"); // Assuming the map has a key 'userId' to match with userMap
	            if (userId != null && userMap.containsKey(userId.toString())) {
	                User user = userMap.get(userId.toString());

	                // Log the current user being processed along with the count
	                int currentCount = userCount.incrementAndGet();
	                log.info("Processing user {}: ID: {}, Name: {}, Mobile: {}", currentCount, userId, user.getName(), user.getMobileNumber());

	                userDetail.put("uuid", user.getName()); // Assuming User has a getName() method
	                userDetail.put("mobilenumber", user.getMobileNumber());
	            } else {
	                // Log when a user ID does not match any entry in userMap
	                log.warn("No matching user found for user ID: {}", userId);
	            }
	        });

            
			try {
				generator.generateExcelFile(temporaryfile);
			} catch (IOException e) {
				log.info("Error Occured while writing into excel file...");
				e.printStackTrace();
			}

			// push temporary file into file store
			log.info("uploading file to filestore...");
			Object filestoreDetails = fileStoreService.upload(temporaryfile, fileName, MediaType.MULTIPART_FORM_DATA_VALUE,
					"PT", "od");
			log.info("Uploaded the file to filestore with details: " + filestoreDetails.toString());

			// enrich and update Utility Report Details
			UtilityReportRequest reportDetailsRequest = enrichmentService.enrichUpdateReport(requestInfo, reportList,
					fileName, filestoreDetails);
			dcbRepository.updateReportDetails(reportDetailsRequest);

			// user info not decrypted and enrichment giving null pointer
			}catch (Exception e) {
				e.printStackTrace();
			}

		}	
		
	}
	
	private void validateIfReportGenerationInProcess(List<UtilityReportDetails> reportList, String tenantId) {
		if (!reportList.isEmpty()) {

			UtilityReportDetails dcbReport = reportList.get(0);

			if (dcbReport.getTenantId().equalsIgnoreCase(tenantId) && StringUtils.isEmpty(dcbReport.getFileStoreId())) {
				throw new CustomException("REPORT_GENERATION_INPROCESS",
						"Report Generation is currently in process. Kindly wait ..");
			}

		}		
	}

	//mobile number not null and distinct name, mobilenumber
	private void validateUserDetailsReport(@Valid UserSearchCriteria userSearchCriteria) {
	    // Check if userType is null or empty
	    if (StringUtils.isEmpty(userSearchCriteria.getUserType())) {
	        throw new CustomException("INVALID_REQUEST", "User Type is missing! Please provide User Type to proceed.");
	    }

	    // Check if active (Boolean) is null
	    if (userSearchCriteria.getActive() == null) {
	        throw new CustomException("INVALID_REQUEST", "Active status is missing! Please provide isActive status to proceed.");
	    }
	 // Validate tenantId and tenantIds
	    if (userSearchCriteria.getTenantId() == null && (userSearchCriteria.getTenantIds() == null || userSearchCriteria.getTenantIds().isEmpty())) {
	        throw new CustomException("INVALID_REQUEST", "At least one of tenantId or tenantIds must be provided.");
	    }

	    // Check if both tenantId and tenantIds are present
	    if (userSearchCriteria.getTenantId() != null && userSearchCriteria.getTenantIds() != null && !userSearchCriteria.getTenantIds().isEmpty()) {
	        log.info("Both tenantId and tenantIds are provided. Preference will be given to tenantId: " + userSearchCriteria.getTenantId());
	        // Set tenantIds to null if tenantId is provided
	        userSearchCriteria.setTenantIds(null);
	    }

	    // Optionally log if only tenantIds are provided
	    if (userSearchCriteria.getTenantIds() != null && !userSearchCriteria.getTenantIds().isEmpty() && userSearchCriteria.getTenantId() == null) {
	        log.info("Only tenantIds are provided: " + userSearchCriteria.getTenantIds());
	    }

	    
	    if (StringUtils.isEmpty(userSearchCriteria.getFinancialYear())) {
	        // Get the current year and set the fiscal year in the desired format
	        LocalDate currentDate = LocalDate.now();
	        int currentYear = currentDate.getYear();
	        int fiscalYearEnd;

	        // Assuming fiscal year ends on March 31, and current fiscal year is the next year if the current month is before April
	        if (currentDate.getMonthValue() >= 4) {
	            fiscalYearEnd = currentYear + 1;  // For April to December, current fiscal year ends next year
	        } else {
	            fiscalYearEnd = currentYear;  // For January to March, current fiscal year ends this year
	        }

	        // Set the financial year from 2021 to current fiscal year (e.g., "2021-24")
	        userSearchCriteria.setFinancialYear("2019-" + String.valueOf(fiscalYearEnd).substring(2));
	        log.info("Setting FY to 2019-" + String.valueOf(fiscalYearEnd).substring(2));
	    }
	    
	    if (userSearchCriteria.getCreateddate() != null && userSearchCriteria.getLastmodifieddate() != null) {
	        // Ensure the dates are non-negative and within a valid range
	        if (userSearchCriteria.getCreateddate() > 0 && userSearchCriteria.getLastmodifieddate() > 0) {


	        	Long createdDate = userSearchCriteria.getCreateddate();
	            Long lastModifiedDate = userSearchCriteria.getLastmodifieddate();

	            // Create the financial year range string with timestamps
	            String financialYearRange = createdDate + "_" + lastModifiedDate;
	            // Set the financial year range in userSearchCriteria
	            userSearchCriteria.setFinancialYear(financialYearRange);

	            // Log the financial year range
	            log.info("Financial Year set to: " + financialYearRange);

	            log.info("Both createddate and lastmodifieddate are provided: " 
	                     + "Created Date: " + userSearchCriteria.getCreateddate() 
	                     + ", Last Modified Date: " + userSearchCriteria.getLastmodifieddate()
	                     + ", FinancialYear: " + userSearchCriteria.getFinancialYear());
	        } else {
	            throw new IllegalArgumentException("Createddate and Lastmodifieddate must be positive.");
	        }
	    }
	}


		private String generateFileName(String fileInitialName, String tenantId, String financialYear) {
			String fileFormat = ".xlsx";
			String fileSeparator = "_";
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hhmmss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			String currentDateTime = dateFormat.format(new Date());
//			String ulbName = StringUtils.capitalize(tenantId.substring(3));
			
		    String ulbName;
		    if ("od".equals(tenantId)) {
		        ulbName = "od"; // Default name for 'od'
		    } else {
				ulbName = StringUtils.capitalize(tenantId.substring(3));
		    }
		    
			return fileInitialName + fileSeparator + ulbName + fileSeparator + financialYear + fileSeparator + currentDateTime + fileFormat;
		}
		
		private File getTemporaryFile(String reportType, String fileName) {
			//Temp file location
			File currentDirFile = new File(configuration.getReportTemporaryLocation());
			String currentPath = currentDirFile.getAbsolutePath();
			String absolutePath = currentPath + File.separator + reportType;
			log.info("Temporary storage Path : " + absolutePath);
			File directory = new File(absolutePath);
			if (directory.exists()) {
				try {
					FileUtils.deleteDirectory(directory);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			directory.mkdir();
			File temporaryfile = new File(absolutePath, fileName);
			
			if (!temporaryfile.exists()) {
				try {
					temporaryfile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return temporaryfile;
		}

		public Map<String, Object> getUserDetailsReport(RequestInfo requestInfo,
				@Valid UtilityReportSearchCriteria searchCriteria) {
			Map<String, Object> response = new HashMap<>();
			
			String reportType = ReportConstants.USERDETAILS_REPORT_STRING;

			String tenantId = searchCriteria.getTenant();
			
			String financialYear = searchCriteria.getFinancialYear();// what if its null

			List<UtilityReportDetails> reportList = dcbRepository.isReportExist(reportType, financialYear, tenantId);

			validateIfReportGenerationInProcess(reportList, tenantId);
			
			if(reportList.isEmpty()) {
				throw new CustomException("REPORT_NOT_GENERATED",
						"Report is not generated for mentioned criteria. Kindly generate the report first ..");
			}
			
			List<UtilityReportDetails> reportDetails = reportList.stream()
					.filter(item -> item.getTenantId().equalsIgnoreCase(tenantId)).collect(Collectors.toList());

			response.put("reportsDetails", reportDetails.get(0));
			
			return response;
		}


}
