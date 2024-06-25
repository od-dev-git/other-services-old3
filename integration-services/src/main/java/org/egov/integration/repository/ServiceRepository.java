package org.egov.integration.repository;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ServiceRepository {
	
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private RestTemplate restTemplate;
	
	public Object fetchResult(StringBuilder uri, Object request) {
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object response = null;
		StringBuilder str = new StringBuilder(this.getClass().getCanonicalName()).append(".fetchResult:")
				.append(System.lineSeparator());
		str.append("URI: ").append(uri.toString()).append(System.lineSeparator());
		try {
			str.append("Request: ").append(mapper.writeValueAsString(request)).append(System.lineSeparator());
			log.debug(str.toString());
			response = restTemplate.postForObject(uri.toString(), request, Map.class);
		} catch (HttpClientErrorException e) {
			log.error("External Service threw an Exception: ", e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		} catch (Exception e) {
			log.error("Exception while fetching from searcher: ", e);
		}
		return response;
	}
	
	public Object fetchResultString(StringBuilder uri, Object request) {
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object response = null;
		StringBuilder str = new StringBuilder(this.getClass().getCanonicalName()).append(".fetchResult:")
				.append(System.lineSeparator());
		str.append("URI: ").append(uri.toString()).append(System.lineSeparator());
		try {
			str.append("Request: ").append(mapper.writeValueAsString(request)).append(System.lineSeparator());
			log.debug(str.toString());
			response = restTemplate.postForObject(uri.toString(), request, String.class);
		} catch (HttpClientErrorException e) {
			log.error("External Service threw an Exception: ", e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		} catch (Exception e) {
			log.error("Exception while fetching from searcher: ", e);
		}
		return response;
	}
	
//	public String fetchResultObject(StringBuilder uri, String request) {
//
//
//		 HttpClient client = HttpClient.newHttpClient();
//	        try {
//				HttpRequest httpRequest = HttpRequest.newBuilder()
//				        .uri(new URI(uri.toString()))
//				        .header("Content-Type", "application/json")
//				        .POST(BodyPublishers.ofString("\"" + request + "\""))
//				        .build();
//				HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//				return response.body();
//
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//	        return null;
//
//	}

	public String fetchResultObject(StringBuilder uri, String request) {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(new URI(uri.toString()));
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setEntity(new StringEntity("\"" + request + "\""));

			CloseableHttpResponse response = client.execute(httpPost);
			String responseBody = EntityUtils.toString(response.getEntity());
			response.close();
			return responseBody;
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}



}
