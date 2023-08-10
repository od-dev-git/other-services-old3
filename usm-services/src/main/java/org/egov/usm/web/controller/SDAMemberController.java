package org.egov.usm.web.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.usm.service.SDAMemberService;
import org.egov.usm.utility.ResponseInfoFactory;
import org.egov.usm.web.model.MemberSearchCriteria;
import org.egov.usm.web.model.RequestInfoWrapper;
import org.egov.usm.web.model.SDAMember;
import org.egov.usm.web.model.SDAMembersRequest;
import org.egov.usm.web.model.SDAMembersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slum")
public class SDAMemberController {

	private final SDAMemberService sdaMemberService;
    
    private final ResponseInfoFactory responseInfoFactory;

    @Autowired
    public SDAMemberController(SDAMemberService sdaMemberService, ResponseInfoFactory responseInfoFactory) {
        this.sdaMemberService = sdaMemberService;
        this.responseInfoFactory = responseInfoFactory;
    }
    
    /**
     * Assign SDA Member for USM
     * 
     * @param sdaMembersRequest
     * @return SDAMembersResponse
     */
	@PostMapping("/member/_assign")
	public ResponseEntity<SDAMembersResponse> assignMember(@Valid @RequestBody SDAMembersRequest sdaMembersRequest) {
		SDAMember sdaMember = sdaMemberService.assignMember(sdaMembersRequest);
		SDAMembersResponse response =  SDAMembersResponse.builder()
				.sdaMembers(Collections.singletonList(sdaMember))
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(sdaMembersRequest.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Reassign a Member
	 * @param sdaMembersRequest
	 * @return SDAMembersResponse
	 */
	@PostMapping("/member/_reassign")
	public ResponseEntity<SDAMembersResponse> reassignMember(@Valid @RequestBody SDAMembersRequest sdaMembersRequest) {
		SDAMember sdaMember = sdaMemberService.reassignMember(sdaMembersRequest);
		SDAMembersResponse response =  SDAMembersResponse.builder()
				.sdaMembers(Collections.singletonList(sdaMember))
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(sdaMembersRequest.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Deassisn member
	 * @param sdaMembersRequest
	 * @return responseInfo
	 */
	@PostMapping("/member/_deassign")
	public ResponseEntity<?> deassignMember(@Valid @RequestBody SDAMembersRequest sdaMembersRequest) {
		sdaMemberService.deassignMember(sdaMembersRequest);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(sdaMembersRequest.getRequestInfo(), true);
        return new ResponseEntity<>(responseInfo, HttpStatus.OK);
	}
	
	
	/**
	 * Search SDA Members
	 * @param searchCriteria
	 * @return SDAMembersResponse
	 */
	@PostMapping("/member/_search")
	public ResponseEntity<SDAMembersResponse> search(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
										@Valid @ModelAttribute MemberSearchCriteria searchCriteria) {
		List<SDAMember> sdaMembers = sdaMemberService.searchMembers(searchCriteria);
		
		SDAMembersResponse response =  SDAMembersResponse.builder()
				.sdaMembers(sdaMembers)
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
