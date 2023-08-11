package org.egov.usm.validator;

import java.util.List;

import org.egov.tracer.model.CustomException;
import org.egov.usm.repository.SDAMemberRepository;
import org.egov.usm.web.model.MemberSearchCriteria;
import org.egov.usm.web.model.SDAMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
public class SDAMemberValidator {
	
	@Autowired
	private SDAMemberRepository repository;

	/**
	 * Throw error if member already exists
	 * 
	 * @param sdaMember
	 */
	public void isMemberAlreadyExists(SDAMember sdaMember) {

		MemberSearchCriteria searchCriteria = MemberSearchCriteria.builder()
        		.tenantId(sdaMember.getTenantId())
        		.ward(sdaMember.getWard())
        		.slumCode(sdaMember.getSlumCode())
                .build();
		
		List<SDAMember> sdaMembers = repository.searchSDAMembers(searchCriteria);
        if(!CollectionUtils.isEmpty(sdaMembers))
            throw new CustomException("EG_SDA_MEMBER_ALREADY_EXIST_ERR", "The survey entity provided in assign request already exist.");
	}
	

	/**
	 * return the existing member details
	 * 
	 * @param sdaMember
	 * @return existing SDAMember
	 */
	public SDAMember isMemberExists(SDAMember sdaMember) {
		if(ObjectUtils.isEmpty(sdaMember.getId()))
            throw new CustomException("EG_SY_UUID_NOT_PROVIDED_ERR", "Providing member id is mandatory for updating and deleting surveys");

		MemberSearchCriteria searchCriteria = MemberSearchCriteria.builder()
        		.id(sdaMember.getId())
                .build();
		
		List<SDAMember> sdaMembers = repository.searchSDAMembers(searchCriteria);
        if(CollectionUtils.isEmpty(sdaMembers))
            throw new CustomException("EG_SURVEY_DOES_NOT_EXIST_ERR", "The survey entity provided in update request does not exist.");

        return sdaMembers.get(0);
	}

}
