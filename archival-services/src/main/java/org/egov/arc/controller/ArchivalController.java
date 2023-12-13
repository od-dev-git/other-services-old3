package org.egov.arc.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.egov.arc.model.Demand;
import org.egov.arc.model.DemandCriteria;
import org.egov.arc.service.ArchivalService;
import org.egov.arc.web.contract.DemandResponse;
import org.egov.arc.web.contract.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArchivalController {

	@Autowired
	private ArchivalService archivalService;

	@PostMapping("_demandDataArchive")
	public ResponseEntity<?> demandArchival(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid DemandCriteria demandCriteria) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		Set<Demand> demands = archivalService.insertArchiveDemands(demandCriteria, requestInfo);
		DemandResponse response = DemandResponse.builder().demands(demands).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
