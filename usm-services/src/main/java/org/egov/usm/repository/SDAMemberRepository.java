package org.egov.usm.repository;

import javax.validation.Valid;

import org.egov.usm.config.USMConfiguration;
import org.egov.usm.producer.Producer;
import org.egov.usm.repository.builder.SDAMemberQueryBuilder;
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
}
