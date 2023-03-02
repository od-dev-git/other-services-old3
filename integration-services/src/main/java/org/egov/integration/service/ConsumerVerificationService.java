package org.egov.integration.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.integration.config.IntegrationConfiguration;
import org.egov.integration.model.Address;
import org.egov.integration.model.ConsumerVerification;
import org.egov.integration.model.ConsumerVerificationSearchCriteria;
import org.egov.integration.model.ConsumerVerificationServiceResponse;
import org.egov.integration.model.OwnerInfo;
import org.egov.integration.model.TradeLicense;
import org.egov.integration.model.VerificationOwner;
import org.egov.integration.web.model.ConsumerVerificationResponse;
import org.egov.integration.web.model.RequestInfoWrapper;
import org.egov.integration.web.model.TradeLicenseResponse;
import org.egov.integration.web.model.WaterConnectionDetailResponse;
import org.egov.integration.model.WSConnection;
import org.egov.integration.repository.ServiceRepository;
import org.egov.integration.validator.ConsumerVerificationValidator;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsumerVerificationService implements InitializingBean {

	private static final RequestInfo requestInfo;

	static {
		User userInfo = User.builder().uuid("DAILY_RECONC_JOB").type("SYSTEM").roles(Collections.emptyList()).id(0L)
				.build();

		requestInfo = new RequestInfo("", "", 0L, "", "", "", "", "", "", userInfo);
	}

	@Autowired
	private IntegrationConfiguration configuration;

	@Autowired
	private ServiceRepository repository;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ConsumerVerificationValidator consumerVerificationValidator;

	@Override
	public void afterPropertiesSet() throws Exception {

		User userInfo = User.builder().uuid(configuration.getConsumerVerificationUserUuid())
				.type(configuration.getConsumerVerificationUserType()).roles(Collections.emptyList()).id(0L).build();

		requestInfo.setUserInfo(userInfo);
	}

	public ConsumerVerificationServiceResponse search(ConsumerVerificationSearchCriteria searchCriteria) {

		consumerVerificationValidator.validateSearch(searchCriteria);
		ConsumerVerificationServiceResponse response = ConsumerVerificationServiceResponse.builder().build();
		VerificationOwner ownerinfo = VerificationOwner.builder().name("Dummy Name").address("Dummy Address").build();

		String businessService = searchCriteria.getBusinessService();

		switch (businessService) {
		case "BPA":
			response = ConsumerVerificationServiceResponse.builder().consumerNo("Dummy Consumer No")
					.businessService(businessService).verificationOwner(Arrays.asList(ownerinfo)).tenantId("Dummy City")
					.status("Dummy Status").build();
			break;
		case "MR":
			response = ConsumerVerificationServiceResponse.builder().consumerNo("Dummy Consumer No")
					.businessService(businessService).verificationOwner(Arrays.asList(ownerinfo)).tenantId("Dummy City")
					.status("Dummy Status").build();
			break;
		case "TL":
			response = getTLResponse(searchCriteria, new ConsumerVerificationServiceResponse());
			break;

		case "WS":
			response = getWSResponse(searchCriteria, new ConsumerVerificationServiceResponse());
			break;

		case "PT":
			VerificationOwner ownerinfotpt = VerificationOwner.builder().name("Dummy Name").address("Dummy Address")
					.build();
			response = ConsumerVerificationServiceResponse.builder().consumerNo("Dummy Consumer No")
					.businessService(businessService).verificationOwner(Arrays.asList(ownerinfotpt))
					.tenantId("Dummy City").status("Dummy Status").build();

		}

		return response;
	}

	private ConsumerVerificationServiceResponse getWSResponse(ConsumerVerificationSearchCriteria searchCriteria,
			ConsumerVerificationServiceResponse response) {
		List<WSConnection> wsConnections = getWaterConnections(searchCriteria);
		WSConnection connectionResponse = null;
		if (!CollectionUtils.isEmpty(wsConnections)) {
			connectionResponse = wsConnections.get(0);
		}

		setWSResponseInfo(response, connectionResponse);

		return response;

	}

	private List<WSConnection> getWaterConnections(ConsumerVerificationSearchCriteria searchCriteria) {
		StringBuilder uri = new StringBuilder(configuration.getWsHost()).append(configuration.getWsSearchEndpoint())
				.append("?").append("tenantId=" + searchCriteria.getTenantId()).append("&")
				.append("searchType=CONNECTION").append("&")
				.append("connectionNumber=" + searchCriteria.getConsumerNo());

		List<WSConnection> wsConnections = new ArrayList<>();
		RequestInfoWrapper requestWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		try {
			Object fetchResponse = repository.fetchResult(uri, requestWrapper);
			log.info("Water response: ", fetchResponse);

			WaterConnectionDetailResponse res = mapper.convertValue(fetchResponse, WaterConnectionDetailResponse.class);
			log.info("Water response: " + String.valueOf(res));
			wsConnections.addAll(res.getConnections());
		} catch (Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("WS_CONNECTION FETCH ERROR", "Unable to fetch WS Connection Information");
		}
		return wsConnections;
	}

	private void setWSResponseInfo(ConsumerVerificationServiceResponse response, WSConnection connectionResponse) {
		if (connectionResponse != null) {
			response.setTenantId(connectionResponse.getTenantId());
			response.setConsumerNo(connectionResponse.getConnectionNo());
			response.setStatus(connectionResponse.getApplicationStatus());
			List<VerificationOwner> owners = new ArrayList<>();

			connectionResponse.getConnectionHolders().stream().forEach(item -> {
				VerificationOwner owner = VerificationOwner.builder().name(item.getName())
						.address(item.getCorrespondenceAddress()).build();
				owners.add(owner);
			});
			response.setVerificationOwner(owners);

		}
	}

	private ConsumerVerificationServiceResponse getTLResponse(ConsumerVerificationSearchCriteria searchCriteria,
			ConsumerVerificationServiceResponse response) {

		List<TradeLicense> tlConnections = getTLConnections(searchCriteria);
		TradeLicense connectionResponse = null;

		if (!CollectionUtils.isEmpty(tlConnections)) {
			// filtering only APPROVED applications
			List<TradeLicense> connectionResponses = tlConnections.parallelStream()
					.filter(tradeLicense -> tradeLicense.getStatus().equalsIgnoreCase("APPROVED"))
					.collect(Collectors.toList());

			// getting latest Approved application
			if (!CollectionUtils.isEmpty(connectionResponses)) {
				Collections.sort(connectionResponses, Comparator.comparing(e -> e.getApplicationDate(), (s1, s2) -> {
					return s2.compareTo(s1);
				}));
				connectionResponse = connectionResponses.get(0);
			}
		}

		setTLResponseInfo(response, connectionResponse);
		int a = 5;

		return response;
	}

	private List<TradeLicense> getTLConnections(ConsumerVerificationSearchCriteria searchCriteria) {

		StringBuilder uri = new StringBuilder(configuration.getTlHost()).append(configuration.getTlSearchEndpoint())
				.append("?").append("tenantId=" + searchCriteria.getTenantId()).append("&").append("offset=0")
				.append("&").append("licenseNumbers=" + searchCriteria.getConsumerNo());

		List<TradeLicense> tlConnections = new ArrayList<>();
		RequestInfoWrapper requestWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		try {
			Object fetchResponse = repository.fetchResult(uri, requestWrapper);
			log.info("Trade License response: ", fetchResponse);

			TradeLicenseResponse res = mapper.convertValue(fetchResponse, TradeLicenseResponse.class);
			log.info("Trade License response: " + String.valueOf(res));
			tlConnections.addAll(res.getLicenses());
		} catch (Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("TL_CONNECTION FETCH ERROR", "Unable to fetch Trade License Information");
		}
		return tlConnections;
	}

	private void setTLResponseInfo(ConsumerVerificationServiceResponse response, TradeLicense connectionResponse) {
		if (connectionResponse != null) {
			response.setTenantId(connectionResponse.getTenantId());
			response.setConsumerNo(connectionResponse.getLicenseNumber());
			response.setStatus(connectionResponse.getStatus());

			StringBuilder address = getAddress(connectionResponse.getTradeLicenseDetail().getAddress());

			List<VerificationOwner> owners = new ArrayList<>();
			connectionResponse.getTradeLicenseDetail().getOwners().stream().forEach(item -> {
				VerificationOwner owner = VerificationOwner.builder().name(item.getName())
						.address(String.valueOf(address)).build();
				owners.add(owner);
			});
			response.setVerificationOwner(owners);

		}
	}

	private StringBuilder getAddress(@NotNull Address address) {
		StringBuilder add = new StringBuilder();
		if (StringUtils.hasText(address.getDoorNo())) {
			add.append(address.getDoorNo() + ", ");
		}
		if (StringUtils.hasText(address.getBuildingName())) {
			add.append(address.getBuildingName() + ", ");
		}
		if (StringUtils.hasText(address.getStreet())) {
			add.append(address.getStreet() + ", ");
		}
		if (StringUtils.hasText(address.getWard())) {
			add.append(address.getWard() + ", ");
		}
		if (StringUtils.hasText(address.getCity())) {

			String tenantId = address.getCity();
			String tenantIdStyled = tenantId.replace("od.", "");
			tenantIdStyled = tenantIdStyled.substring(0, 1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
			add.append(tenantIdStyled + ", ");
		}
		if (StringUtils.hasText(address.getPincode())) {
			add.append(address.getPincode() + ", ");
		}
		if (add.length() != 0) {
			add = add.delete(add.length() - 2, add.length());
		}
		return add;
	}

}
