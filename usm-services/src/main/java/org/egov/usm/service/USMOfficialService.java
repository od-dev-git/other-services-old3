package org.egov.usm.service;

import java.util.List;

import javax.validation.Valid;

import org.egov.usm.repository.USMOfficialRepository;
import org.egov.usm.web.model.USMOfficial;
import org.egov.usm.web.model.USMOfficialRequest;
import org.egov.usm.web.model.USMOfficialSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class USMOfficialService {

	private USMOfficialRepository repository;

	private OfficialEnrichmentService enrichmentService;

	@Autowired
	public USMOfficialService(USMOfficialRepository repository, OfficialEnrichmentService enrichmentService) {
		this.repository = repository;
		this.enrichmentService = enrichmentService;
	}

	/**
	 * Service layer for Creating officials
	 * 
	 * @param USMOfficialRequest
	 * @return created official
	 */

	public USMOfficial create(@Valid USMOfficialRequest usmOfficialRequest) {
		// Enrich official details
		enrichmentService.enrichUSMOfficialRequest(usmOfficialRequest);

		// assign the Employee as official
		repository.saveOfficial(usmOfficialRequest);

		return usmOfficialRequest.getUsmOffcial();

	}

	/**
	 * Service layer for searching official Requests
	 * 
	 * @param requestInfoWrapper
	 * @param searchCriteria
	 * @return UsmOfficialResponse with UsmOfficialRequest List
	 */

	public List<USMOfficial> searchOfficial(@Valid USMOfficialSearchCriteria searchCriteria) {
		log.info("search: " + searchCriteria.toString());
		List<USMOfficial> usmOfficials = repository.getOfficialRequests(searchCriteria);
		return usmOfficials;
	}

}
