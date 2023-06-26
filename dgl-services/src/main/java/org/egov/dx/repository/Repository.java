package org.egov.dx.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.dx.util.PTServiceDXConstants;
import org.egov.dx.web.models.DGLModel;
import org.egov.dx.web.models.DGLSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Repository
@Slf4j
public class Repository {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	//@Qualifier("secondaryMapper")
	private ObjectMapper mapper;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private DglQueryBuilder queryBuilder;
		
	/**
	 * Fetches results from external services through rest call.
	 * 
	 * @param request
	 * @param uri
	 * @return Object
	 */
	public Object fetchResult(StringBuilder uri, Object request) {
		
		Object response = null;
		log.info("URI: " + uri.toString());
		try {
			log.info(mapper.writeValueAsString(request));
			response = restTemplate.postForObject(uri.toString(), request, Map.class);
		} catch (ResourceAccessException e) {
			
			Map<String, String> map = new HashMap<>();
			map.put(PTServiceDXConstants.CONNECT_EXCEPTION_KEY, e.getMessage());
			throw new CustomException(map);
		}  catch (HttpClientErrorException e) {

			log.info("the error is : " + e.getResponseBodyAsString());
			throw new ServiceCallException(e.getResponseBodyAsString());
		}catch (Exception e) {

			log.error("Exception while fetching from searcher: ", e);
		}
		return response;
	}

	public DGLModel searchDataForDGL(DGLSearchCriteria criteria) {
		
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = queryBuilder.getDGLDataQuery(criteria, preparedStatementValues);
		log.info("query for fetching records: "+query);
		DGLModel dglResponse = namedParameterJdbcTemplate.query(query, preparedStatementValues, new DglRowMapper());
		if(StringUtils.isEmpty(dglResponse.getConsumerCode()))
			return null;
		return dglResponse;

	}
}
