package org.egov.usm.web.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.usm.service.USMOfficialService;
import org.egov.usm.utility.ResponseInfoFactory;
import org.egov.usm.web.model.RequestInfoWrapper;
import org.egov.usm.web.model.USMOfficial;
import org.egov.usm.web.model.USMOfficialRequest;
import org.egov.usm.web.model.USMOfficialResponse;
import org.egov.usm.web.model.USMOfficialSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class USMOfficialController {

	private final ResponseInfoFactory responseInfoFactory;

	private final USMOfficialService usmOfficialService;

	@Autowired
	public USMOfficialController(USMOfficialService usmOfficialService, ResponseInfoFactory responseInfoFactory) {
		this.usmOfficialService = usmOfficialService;
		this.responseInfoFactory = responseInfoFactory;

	}

	/**
	 * Creates Survey Request for USM
	 * 
	 * @param surveyRequest
	 * @return SurveyResponse
	 */
	@PostMapping("/_assign")
	public ResponseEntity<USMOfficialResponse> create(@Valid @RequestBody USMOfficialRequest usmOfficialRequest) {

		USMOfficial usmOfficial = usmOfficialService.create(usmOfficialRequest);
		USMOfficialResponse response = USMOfficialResponse.builder().usmOffcials(Collections.singletonList(usmOfficial))
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(usmOfficialRequest.getRequestInfo(),
						true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/_search")
	public ResponseEntity<USMOfficialResponse> search(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute USMOfficialSearchCriteria searchCriteria) {
		List<USMOfficial> usmOfficial = usmOfficialService.searchOfficial(searchCriteria);
		USMOfficialResponse response = USMOfficialResponse.builder().usmOffcials(usmOfficial).responseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
