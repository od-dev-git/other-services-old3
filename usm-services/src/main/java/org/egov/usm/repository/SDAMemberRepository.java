package org.egov.usm.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.egov.usm.config.USMConfiguration;
import org.egov.usm.producer.Producer;
import org.egov.usm.repository.builder.SDAMemberQueryBuilder;
import org.egov.usm.repository.rowmapper.SDAMemberRowMapper;
import org.egov.usm.web.model.MemberSearchCriteria;
import org.egov.usm.web.model.SDAMember;
import org.egov.usm.web.model.SDAMembersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SDAMemberRepository {
	
	private JdbcTemplate jdbcTemplate;

    private SDAMemberQueryBuilder queryBuilder;

    private SDAMemberRowMapper rowMapper;

    private Producer producer;

    private USMConfiguration config;
    
	@Autowired
    public SDAMemberRepository(JdbcTemplate jdbcTemplate, SDAMemberQueryBuilder queryBuilder, Producer producer,
			USMConfiguration config) {
		this.jdbcTemplate = jdbcTemplate;
		this.queryBuilder = queryBuilder;
		this.producer = producer;
		this.config = config;
	}


	/**
     * Pushes the sdaMembersRequest to create kafka topic 
     * 
     * @param sdaMembersRequest
     */
	public void saveMember(@Valid SDAMembersRequest sdaMembersRequest) {
		log.info("Save request :", sdaMembersRequest.toString());
        producer.push(config.getCreateMemberTopic(), sdaMembersRequest);
	}


	/**
	 * Search members based on different criteria
	 * 
	 * @param searchCriteria
	 * @return List<SDAMember>
	 */
	public List<SDAMember> searchSDAMembers(MemberSearchCriteria searchCriteria) {
		log.info("Search Criteria :", searchCriteria.toString());
		List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getMemberSearchQuery(searchCriteria, preparedStmtList);
        List<SDAMember> sdaMembers =  jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
		
		if(sdaMembers.isEmpty())
			return Collections.emptyList();
		return sdaMembers;
	}


	/**
     * Pushes the sdaMembersRequest to update kafka topic 
     * 
     * @param sdaMembersRequest
     */
	public void updateMember(@Valid SDAMembersRequest sdaMembersRequest) {
		log.info("update request :", sdaMembersRequest.toString());
        producer.push(config.getUpdateMemberTopic(), sdaMembersRequest);
	}

}
