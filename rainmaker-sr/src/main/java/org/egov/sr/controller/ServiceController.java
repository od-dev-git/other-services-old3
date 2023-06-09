package org.egov.sr.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.sr.contract.CountResponse;
import org.egov.sr.contract.RequestInfoWrapper;
import org.egov.sr.contract.ServiceReqSearchCriteria;
import org.egov.sr.contract.ServiceRequest;
import org.egov.sr.contract.ServiceResponse;
import org.egov.sr.model.Service;
import org.egov.sr.service.GrievanceService;
import org.egov.sr.utils.ResponseInfoFactory;
import org.egov.sr.validator.PGRRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/v1/requests/")
public class ServiceController {

	@Autowired
	private GrievanceService service;
	
	@Autowired
	private ResponseInfoFactory factory;

	@Autowired
	private PGRRequestValidator pgrRequestValidator;
	
	/**Dharamshala0
	 * enpoint to create service requests
	 * 
	 * @param ServiceReqRequest
	 * @author kaviyarasan1993
	 */
	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody @Valid ServiceRequest serviceRequest) {
		pgrRequestValidator.validateCreate(serviceRequest);
		ServiceResponse response = service.create(serviceRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * enpoint to update service requests
	 * 
	 * @param ServiceReqRequest
	 * @author kaviyarasan1993
	 */
	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody @Valid ServiceRequest serviceRequest) {
		pgrRequestValidator.validateUpdate(serviceRequest);
		ServiceResponse response = service.update(serviceRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	

	/**
	 * Controller endpoint to fetch service requests
	 * 
	 * @param requestInfoWrapper
	 * @param serviceReqSearchCriteria
	 * @return ResponseEntity<?>
	 * @author vishal
	 */
	@PostMapping("_searchOld")
	@ResponseBody
	private ResponseEntity<?> search(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid ServiceReqSearchCriteria serviceReqSearchCriteria) {
		pgrRequestValidator.validateSearch(serviceReqSearchCriteria, requestInfoWrapper.getRequestInfo());
		Object serviceReqResponse = service.getServiceRequestDetails(requestInfoWrapper.getRequestInfo(),
				serviceReqSearchCriteria);
		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);
	}
	
	@PostMapping("_search")
	@ResponseBody
	private ResponseEntity<?> searchFromDB(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid ServiceReqSearchCriteria serviceReqSearchCriteria) {
		
		ServiceResponse serviceReqResponse = service.searchFromDB(requestInfoWrapper.getRequestInfo(),
				serviceReqSearchCriteria);
		serviceReqResponse
				.setResponseInfo(factory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true));
		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);
	}
	
	/**
	 * Controller endpoint to fetch service requests irrespective of role
	 * 
	 * @param requestInfoWrapper
	 * @param serviceReqSearchCriteria
	 * @return ResponseEntity<?>
	 * @author vishal
	 */
	@PostMapping("_plainsearch")
	@ResponseBody
	private ResponseEntity<?> plainsearch(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid ServiceReqSearchCriteria serviceReqSearchCriteria) {
		Object serviceReqResponse = service.getServiceRequestDetailsForPlainSearch(requestInfoWrapper.getRequestInfo(),
				serviceReqSearchCriteria);
		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);
	}

	 /**
	 * Controller to fetch count of service requests based on a given criteria
	 * 
	 * @param requestInfoWrapper
	 * @param serviceReqSearchCriteria
	 * @return ResponseEntity<?>
	 * @author vishal
	 */
	@PostMapping("_countOld")
	@ResponseBody
	private ResponseEntity<?> count(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid ServiceReqSearchCriteria serviceReqSearchCriteria) {
		pgrRequestValidator.validateSearch(serviceReqSearchCriteria, requestInfoWrapper.getRequestInfo());
		Object countResponse = service.getCount(requestInfoWrapper.getRequestInfo(), serviceReqSearchCriteria);
		return new ResponseEntity<>(countResponse, HttpStatus.OK);
	}
	
	@PostMapping("_count")
	@ResponseBody
	private ResponseEntity<?> countFromDB(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid ServiceReqSearchCriteria serviceReqSearchCriteria) {
		
		Double count = service.getCountFromDB(requestInfoWrapper.getRequestInfo(), serviceReqSearchCriteria);
		CountResponse countResponse = CountResponse.builder().count(count)
				.responseInfo(factory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		
		return new ResponseEntity<>(countResponse, HttpStatus.OK);
	}
	
	
	/**
	 * Controller endpoint to fetch service requests
	 * 
	 * @param requestInfoWrapper
	 * @param serviceReqSearchCriteria
	 * @return ResponseEntity<?>
	 * 
	 */
	@PostMapping("escalate/_batch")
	@ResponseBody
	private ResponseEntity<?> batchEscalation(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid ServiceReqSearchCriteria serviceReqSearchCriteria) {
		pgrRequestValidator.validateEscalationSearch(serviceReqSearchCriteria, requestInfoWrapper.getRequestInfo());
		Object serviceReqResponse = service.getEscalationServiceRequestDetails(requestInfoWrapper.getRequestInfo(),
				serviceReqSearchCriteria);
		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);
	}
	
	/**
	 * Controller endpoint to fetch service requests for closure of complaints
	 * 
	 * @param requestInfoWrapper
	 * @param serviceReqSearchCriteria
	 * @return ResponseEntity<?>
	 * 
	 */
	@PostMapping("updateForClosure/_batch")
	@ResponseBody
	private ResponseEntity<?> updateForClosure(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid ServiceReqSearchCriteria serviceReqSearchCriteria) {
		pgrRequestValidator.validateEscalationSearch(serviceReqSearchCriteria, requestInfoWrapper.getRequestInfo());
		Object serviceReqResponse = service.getClosedComplaintsRequestDetails(requestInfoWrapper.getRequestInfo(),
				serviceReqSearchCriteria);
		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);
	}

}
