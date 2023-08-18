package org.egov.usm.service;

import java.util.List;

import javax.validation.Valid;

import org.egov.usm.repository.USMOfficialRepository;
import org.egov.usm.validator.USMOfficialValidator;
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

	private USMOfficialValidator validator;

	@Autowired
	public USMOfficialService(USMOfficialRepository repository, OfficialEnrichmentService enrichmentService,
			USMOfficialValidator validator) {
		this.repository = repository;
		this.enrichmentService = enrichmentService;
		this.validator = validator;
	}

	/**
	 * Service layer for Creating officials
	 * 
	 * @param USMOfficialRequest
	 * @return created official
	 */

	public USMOfficial create(@Valid USMOfficialRequest usmOfficialRequest) {

		validator.isOfficialAlreadyExists(usmOfficialRequest.getUsmOffcial());
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

	public List<USMOfficial> deassingOfficial(@Valid USMOfficialRequest usmOfficialRequest) {

		return null;
	}

	/**
	 * Deassign a official
	 * 
	 * @param usmOfficialRequest
	 */
	public void deassignOfficial(@Valid USMOfficialRequest usmOfficialRequest) {
		USMOfficial existingOfficialMember = validator.isOfficialExists(usmOfficialRequest.getUsmOffcial());

		// Enrich official Member details
		enrichmentService.enrichDeassignOfficialRequest(usmOfficialRequest, existingOfficialMember);

		// update the official and deassign
		repository.updateOfficial(usmOfficialRequest);

	}

	/**
	 * reassign a official
	 * 
	 * @param usmOfficialRequest
	 */

	public USMOfficial reassignOfficial(@Valid USMOfficialRequest usmOfficialRequest) {

		USMOfficial existingOfficial = validator.isOfficialExists(usmOfficialRequest.getUsmOffcial());

		// Enrich SDA Member details
		enrichmentService.enrichReassignMembersRequest(usmOfficialRequest, existingOfficial);

		// assign the citizen as SDA
		repository.updateOfficial(usmOfficialRequest);

		return usmOfficialRequest.getUsmOffcial();
	}

}
