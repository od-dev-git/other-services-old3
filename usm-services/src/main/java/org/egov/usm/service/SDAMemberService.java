package org.egov.usm.service;

import java.util.List;

import javax.validation.Valid;

import org.egov.usm.repository.SDAMemberRepository;
import org.egov.usm.web.model.RequestInfoWrapper;
import org.egov.usm.web.model.SDAMember;
import org.egov.usm.web.model.SDAMembersRequest;
import org.egov.usm.web.model.SurveySearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SDAMemberService {
	
	private SDAMemberRepository repository;
	
	private MemberEnrichmentService enrichmentService;
	
	@Autowired
	public SDAMemberService(SDAMemberRepository repository, MemberEnrichmentService enrichmentService) {
		this.repository = repository;
		this.enrichmentService = enrichmentService;
	}

	
	/**
	 * Service for creating SDA Member
	 * 
	 * @param sdaMembersRequest
	 * @return SDAMember
	 */
	public SDAMember assignMember(@Valid SDAMembersRequest sdaMembersRequest) {
		
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
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public void deassignMember(@Valid SDAMembersRequest sdaMembersRequest) {
		// TODO Auto-generated method stub
		
	}

	public List<SDAMember> searchMembers(@Valid RequestInfoWrapper requestInfoWrapper,
			@Valid SurveySearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}
}
