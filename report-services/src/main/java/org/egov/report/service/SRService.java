package org.egov.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.repository.SRRepository;
import org.egov.report.util.Util;
import org.egov.report.validator.SRValidator;
import org.egov.report.web.model.SRReportSearchCriteria;
import org.egov.report.web.model.TicketDetails;
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
	
	public List<TicketDetails> getTicketDetails(RequestInfo requestInfo,
			SRReportSearchCriteria srReportSearchCriteria) {
		
		srValidator.validateSRTicketDetail(srReportSearchCriteria);
		
		//fetch ticket details from DB
		List<TicketDetails> ticketDetails = srRepository.getTicketDetails(requestInfo, srReportSearchCriteria);
		
		Set<String> type = ticketDetails.stream().map(ticket -> ticket.getType()).collect(Collectors.toSet());
		
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

}
