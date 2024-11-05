package org.egov.report.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.repository.SRRepository;
import org.egov.report.util.Util;
import org.egov.report.validator.SRValidator;
import org.egov.report.web.model.OwnerInfo;
import org.egov.report.web.model.SRReportSearchCriteria;
import org.egov.report.web.model.TicketDetails;
import org.egov.report.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SRService {
	
	@Autowired
	private SRValidator srValidator;
	
	@Autowired
	private SRRepository srRepository;
	
	@Autowired
	private Util utils;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ReportServiceConfiguration configuration;
	
	public List<TicketDetails> getTicketDetails(RequestInfo requestInfo,
			SRReportSearchCriteria srReportSearchCriteria) {
		
		srValidator.validateSRTicketDetail(srReportSearchCriteria);
		
		//fetch ticket details from DB
		List<TicketDetails> ticketDetails = srRepository.getTicketDetails(requestInfo, srReportSearchCriteria);
		
		Set<String> type = ticketDetails.stream().map(ticket -> ticket.getType()).collect(Collectors.toSet());
		
        fetchAndSetUsers(requestInfo, ticketDetails);
        
		//get Service Type Name from MDMS
		List<Map<String, Object>> serviceTypeMap = utils.getServiceTypefromMdms(requestInfo, "od");
		
		Map<String, String> serviceTypes = new HashMap<>();
		
		for (Map<String, Object> item : serviceTypeMap) {

			if (type.contains(item.get("code"))) {
				serviceTypes.put((String) item.get("code"), (String) item.get("name"));
			}
		}
		
		//set the name of service type instead of code in Response
		ticketDetails.stream().forEach(ticket -> {
            String serviceType = serviceTypes.get(ticket.getType());
            if (serviceType != null) {
            	ticket.setType(serviceType);
            }
        });
		
		return ticketDetails;
	}

	private void fetchAndSetUsers(RequestInfo requestInfo, List<TicketDetails> ticketDetails) {
		Set<String> userIds = ticketDetails.stream()
                .map(map -> (String) map.getRaisedby())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

		// Set the limit for chunk size to 10,000 from configuration
        int userSearchLimit = configuration.getUserServiceSearchLimit();
        Map<String, User> userMap = new HashMap<>();


        while (!userIds.isEmpty()) {
            // Limit the stream, convert to Long, make values distinct, and collect to List
            List<Long> chunk = userIds.stream()
                .limit(userSearchLimit)
                .map(id -> {
                    try {
                        return Long.valueOf(id); // Try to convert each String to Long
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid user ID: " + id);
                        return null; // Return null if parsing fails
                    }
                })
                .filter(Objects::nonNull) // Remove any null values from failed conversions
                .distinct() // Keep only distinct values
                .collect(Collectors.toList());

            // Exit the loop if chunk is empty to avoid infinite loop
            if (chunk.isEmpty()) {
                System.err.println("No valid user IDs left to process.");
                break;
            }

            UserSearchCriteria usCriteria = UserSearchCriteria.builder()
                .id(chunk)
                .active(true)
                .userType(UserSearchCriteria.EMPLOYEE)
                .build();

            try {
                List<OwnerInfo> usersInfo = userService.getUserDetails(requestInfo, usCriteria);

                userMap.putAll(usersInfo.stream()
                    .filter(Objects::nonNull) // Filter out any null users
                    .filter(user -> user.getId() != null) // Filter out users with null IDs
                    .collect(Collectors.toMap(
                        user -> String.valueOf(user.getId()), // Convert ID to String
                        Function.identity()
                    )));

                // Remove processed IDs from userIds
                userIds.removeAll(new HashSet<>(userIds));
            } catch (Exception e) {
                System.err.println("Error fetching user details: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Fetched user details for: " + userMap.size() + " users.");
        if (ticketDetails != null && userMap != null) { // Ensure ticketDetails and userMap are non-null
            ticketDetails.forEach(ticket -> {
                if (ticket != null) { // Check each ticket is non-null
                    String raisedBy = ticket.getRaisedby();
                    if (raisedBy != null && userMap.containsKey(raisedBy)) {
                        User user = userMap.get(raisedBy);
                        if (user != null && user.getName() != null) { // Check user and user name are non-null
                            ticket.setRaisedby(user.getName());
                        }
                    }
                }
            });
        }
	}
}
