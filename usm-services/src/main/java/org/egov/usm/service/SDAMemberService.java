package org.egov.usm.service;

import java.util.List;

import javax.validation.Valid;

import org.egov.usm.repository.SDAMemberRepository;
import org.egov.usm.validator.SDAMemberValidator;
import org.egov.usm.web.model.MemberSearchCriteria;
import org.egov.usm.web.model.SDAMember;
import org.egov.usm.web.model.SDAMembersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SDAMemberService {
	
	private SDAMemberRepository repository;
	
	private MemberEnrichmentService enrichmentService;
	
	private SDAMemberValidator validator;
	
	@Autowired
	public SDAMemberService(SDAMemberRepository repository, MemberEnrichmentService enrichmentService, SDAMemberValidator validator) {
		this.repository = repository;
		this.enrichmentService = enrichmentService;
		this.validator = validator;
	}

	
	/**
	 * Service for creating SDA Member
	 * 
	 * @param sdaMembersRequest
	 * @return SDAMember
	 */
	public SDAMember assignMember(@Valid SDAMembersRequest sdaMembersRequest) {
		
		//Validate member already exist or not
		validator.isMemberAlreadyExists(sdaMembersRequest.getSdaMember());
		
		// Enrich SDA Member details
		enrichmentService.enrichSDAMembersRequest(sdaMembersRequest);
		
		//assign the citizen as SDA
		repository.saveMember(sdaMembersRequest);
		
		return sdaMembersRequest.getSdaMember();
	}

	
	/**
	 * Service Layer for reassign the SDA Member
	 * 
	 * @param sdaMembersRequest
	 * @return
	 */
	public SDAMember reassignMember(@Valid SDAMembersRequest sdaMembersRequest) {
		
		SDAMember existingSdaMember = validator.isMemberExists(sdaMembersRequest.getSdaMember());
		
		// Enrich SDA Member details
		enrichmentService.enrichReassignMembersRequest(sdaMembersRequest, existingSdaMember);

		// assign the citizen as SDA
		repository.updateMember(sdaMembersRequest);

		return sdaMembersRequest.getSdaMember();
	}
	
	
	/**
	 * Deassign a user from membership
	 * 
	 * @param sdaMembersRequest
	 */
	public void deassignMember(@Valid SDAMembersRequest sdaMembersRequest) {
		SDAMember existingSdaMember = validator.isMemberExists(sdaMembersRequest.getSdaMember());
		
		// Enrich SDA Member details
		enrichmentService.enrichDeassignMembersRequest(sdaMembersRequest, existingSdaMember);

		// assign the citizen as SDA
		repository.updateMember(sdaMembersRequest);
		
	}
	
	

	/**
	 * Search Members based on search criteria
	 * 
	 * @param searchCriteria
	 * @return List<SDAMember>
	 */
	public List<SDAMember> searchMembers(@Valid MemberSearchCriteria searchCriteria) {
		searchCriteria.setIsActive(Boolean.TRUE);
		return repository.searchSDAMembers(searchCriteria);
	}
}
