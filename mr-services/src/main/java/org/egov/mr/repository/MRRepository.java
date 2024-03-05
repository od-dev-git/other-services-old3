package org.egov.mr.repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.config.MRConfiguration;
import org.egov.mr.producer.Producer;
import org.egov.mr.repository.builder.MRQueryBuilder;
import org.egov.mr.repository.rowmapper.MRDigitalSignedCertificateRowMapper;
import org.egov.mr.repository.rowmapper.MRRowMapper;
import org.egov.mr.web.models.DscDetails;
import org.egov.mr.web.models.MarriageRegistration;
import org.egov.mr.web.models.MarriageRegistrationRequest;
import org.egov.mr.web.models.MarriageRegistrationSearchCriteria;
import org.egov.mr.workflow.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Repository
public class MRRepository {

    private JdbcTemplate jdbcTemplate;

    private MRQueryBuilder queryBuilder;

    private MRRowMapper rowMapper;
    
    private MRDigitalSignedCertificateRowMapper dscRowMapper;

    private Producer producer;

    private MRConfiguration config;

    private WorkflowService workflowService;


    @Autowired
    public MRRepository(JdbcTemplate jdbcTemplate, MRQueryBuilder queryBuilder, MRRowMapper rowMapper,
                        Producer producer, MRConfiguration config, WorkflowService workflowService,MRDigitalSignedCertificateRowMapper dscRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuilder = queryBuilder;
        this.rowMapper = rowMapper;
        this.producer = producer;
        this.config = config;
        this.workflowService = workflowService;
        this.dscRowMapper =dscRowMapper ;
    }



    public List<MarriageRegistration> getMarriageRegistartions(MarriageRegistrationSearchCriteria criteria) {
    	
        List<Object> preparedStmtList = new ArrayList<>();
        List<String> mrIdList = new ArrayList<>();
        
        if(StringUtils.isNotBlank(criteria.getMobileNumber())){
        	
        	List<Object> mobileNumberList = new ArrayList<>();
        	mobileNumberList.add(criteria.getMobileNumber());
        	String mrIdQuery = "select distinct T1.mr_id "
              		+ "from eg_mr_couple T1 JOIN eg_mr_address T2 on T1.id = T2.mr_couple_id where T2.contact = ? ";
            mrIdList =  jdbcTemplate.query(mrIdQuery, mobileNumberList.toArray(), new SingleColumnRowMapper<>(String.class));
            
            if(!mrIdList.isEmpty()) {
            	criteria.setMrIdList(mrIdList); 
            }
        }
        
        String query = queryBuilder.getMRSearchQuery(criteria, preparedStmtList);
        List<MarriageRegistration> registrations =  jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
        return registrations;
    }

    
    
    public List<DscDetails> getDscDetails(MarriageRegistrationSearchCriteria criteria) {
        List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getMRDscDetailsQuery(criteria, preparedStmtList);
        List<DscDetails> dscDetails =  jdbcTemplate.query(query, preparedStmtList.toArray(), dscRowMapper);
        return dscDetails;
    }

    public void save(MarriageRegistrationRequest marriageRegistrationRequest) {
        producer.push(config.getSaveTopic(), marriageRegistrationRequest);
    }
    /**
     * Pushes the update request to update topic or on workflow topic depending on the status
     *
     * @param marriageRegistrationRequest The update requuest
     */
    public void update(MarriageRegistrationRequest marriageRegistrationRequest,Map<String,Boolean> idToIsStateUpdatableMap) {
        RequestInfo requestInfo = marriageRegistrationRequest.getRequestInfo();
        List<MarriageRegistration> marriageRegistrations = marriageRegistrationRequest.getMarriageRegistrations();

        List<MarriageRegistration> registrationsForStatusUpdate = new LinkedList<>();
        List<MarriageRegistration> registrationsForUpdate = new LinkedList<>();


        for (MarriageRegistration registrations : marriageRegistrations) {
            if (idToIsStateUpdatableMap.get(registrations.getId())) {
                registrationsForUpdate.add(registrations);
                
            }
            else {
                registrationsForStatusUpdate.add(registrations);
            }
        }

        if (!CollectionUtils.isEmpty(registrationsForUpdate))
            producer.push(config.getUpdateTopic(), new MarriageRegistrationRequest(requestInfo, registrationsForUpdate));

        if (!CollectionUtils.isEmpty(registrationsForStatusUpdate))
            producer.push(config.getUpdateWorkflowTopic(), new MarriageRegistrationRequest(requestInfo, registrationsForStatusUpdate));


    }



	public void updateDscDetails(MarriageRegistrationRequest marriageRegistrationRequest) {
		
		RequestInfo requestInfo = marriageRegistrationRequest.getRequestInfo();
        List<MarriageRegistration> marriageRegistrations = marriageRegistrationRequest.getMarriageRegistrations();

       producer.push(config.getUpdateDscDetailsTopic(), new MarriageRegistrationRequest(requestInfo, marriageRegistrations));
		
	}
	



	private void sortChildObjectsById(List<MarriageRegistration> marriageRegistrations) {
        if(CollectionUtils.isEmpty(marriageRegistrations))
            return;
    }
	
	public List<String> fetchMarriageRegistrationIds(@Valid MarriageRegistrationSearchCriteria criteria) {
		List<Object> preparedStmtList = new ArrayList<>();
        preparedStmtList.add(criteria.getOffset());
        preparedStmtList.add(criteria.getLimit());

        return jdbcTemplate.query("select id from eg_mr_application ORDER BY createdtime offset " +
                        " ? " +
                        "limit ? ",
                preparedStmtList.toArray(),
                new SingleColumnRowMapper<>(String.class));
	}



	public List<MarriageRegistration> getMarriageRegistrationPlainSearch(
			MarriageRegistrationSearchCriteria idsCriteria) {

		List<Object> preparedStmtList = new ArrayList<>();
        String query = queryBuilder.getMRPlainSearchQuery(idsCriteria, preparedStmtList);
        log.info("Query: " + query);
        List<MarriageRegistration> marriageRegistrations =  jdbcTemplate.query(query, preparedStmtList.toArray(), rowMapper);
  //      sortChildObjectsById(marriageRegistrations);
        return marriageRegistrations;
	}


}
