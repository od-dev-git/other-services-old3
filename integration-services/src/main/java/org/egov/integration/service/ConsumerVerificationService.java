package org.egov.integration.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.integration.config.IntegrationConfiguration;
import org.egov.integration.model.Address;
import org.egov.integration.model.BPA;
import org.egov.integration.model.ConsumerVerification;
import org.egov.integration.model.ConsumerVerificationSearchCriteria;
import org.egov.integration.model.ConsumerVerificationServiceResponse;
import org.egov.integration.model.CoupleDetails;
import org.egov.integration.model.MarriageRegistration;
import org.egov.integration.model.OwnerInfo;
import org.egov.integration.model.Property;
import org.egov.integration.model.TradeLicense;
import org.egov.integration.model.VerificationOwner;
import org.egov.integration.web.model.BPAResponse;
import org.egov.integration.web.model.ConsumerVerificationResponse;
import org.egov.integration.web.model.MarriageRegistrationResponse;
import org.egov.integration.web.model.PropertyResponse;
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsumerVerificationService implements InitializingBean {

	private static final RequestInfo requestInfo;

	static {
		User userInfo = User.builder().uuid("TPA_VALIDATOR").type("SYSTEM").roles(Collections.emptyList()).id(0L)
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

		Role employeeRole = Role.builder().name("Employee").code("EMPLOYEE").tenantId("od.cuttack").build();
		
		User userInfo = User.builder().uuid(configuration.getConsumerVerificationUserUuid())
				.type(configuration.getConsumerVerificationUserType()).roles(Arrays.asList(employeeRole)).id(0L).build();

		requestInfo.setUserInfo(userInfo);
	}

	public ConsumerVerificationServiceResponse search(ConsumerVerificationSearchCriteria searchCriteria) {

		consumerVerificationValidator.validateSearch(searchCriteria);
		ConsumerVerificationServiceResponse response = ConsumerVerificationServiceResponse.builder().build();
		VerificationOwner ownerinfo = VerificationOwner.builder().name("Dummy Name").address("Dummy Address").build();

		String businessService = searchCriteria.getBusinessService();

		switch (businessService) {
		case "BPA":
			response = getBPAResponse(searchCriteria, new ConsumerVerificationServiceResponse());
			break;

		case "MR":
			response = getMRResponse(searchCriteria, new ConsumerVerificationServiceResponse());
			break;

		case "TL":
			response = getTLResponse(searchCriteria, new ConsumerVerificationServiceResponse());
			break;

		case "WS":
			response = getWSResponse(searchCriteria, new ConsumerVerificationServiceResponse());
			break;

		case "PT":
			response = getPTResponse(searchCriteria, new ConsumerVerificationServiceResponse());
			break;

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
				response.setAddress(item.getCorrespondenceAddress());
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
			response.setAddress(String.valueOf(address));
			
			List<VerificationOwner> owners = new ArrayList<>();
			connectionResponse.getTradeLicenseDetail().getOwners().stream().forEach(item -> {
				VerificationOwner owner = VerificationOwner.builder().name(item.getName())
						.address(item.getCorrespondenceAddress()).build();
				owners.add(owner);
			});
			response.setVerificationOwner(owners);

		}
	}

	private StringBuilder getAddress(Address address) {
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
			add.append("Ward No : " + address.getWard() + ", ");
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
		if (StringUtils.hasText(address.getPinCodeTL())) {
			add.append(address.getPinCodeTL() + ", ");
		}
		if (StringUtils.hasText(address.getDistrict())) {
			add.append(address.getDistrict() + ", ");
		}
		if (StringUtils.hasText(address.getState())) {
			add.append(address.getState() + ", ");
		}
		if (StringUtils.hasText(address.getCountry())) {
			add.append(address.getCountry() + ", ");
		}
		if (add.length() != 0) {
			add = add.delete(add.length() - 2, add.length());
		}
		return add;
	}

	private ConsumerVerificationServiceResponse getPTResponse(ConsumerVerificationSearchCriteria searchCriteria,
			ConsumerVerificationServiceResponse response) {

		List<Property> properties = getPTConnections(searchCriteria);
		Property connectionResponse = null;

		if (!CollectionUtils.isEmpty(properties)) {
			// filtering only APPROVED applications
			List<Property> connectionResponses = properties.parallelStream()
					.filter(property -> property.getStatus().toString().equalsIgnoreCase("ACTIVE"))
					.collect(Collectors.toList());

			// getting latest Approved application
			if (!CollectionUtils.isEmpty(connectionResponses)) {
				connectionResponse = connectionResponses.get(0);
			}
		}

		setPTResponseInfo(response, connectionResponse);

		return response;
	}

	private List<Property> getPTConnections(ConsumerVerificationSearchCriteria searchCriteria) {
		StringBuilder uri = new StringBuilder(configuration.getPtHost()).append(configuration.getPtSearchEndpoint())
				.append("?").append("tenantId=" + searchCriteria.getTenantId()).append("&")
				.append("propertyIds=" + searchCriteria.getConsumerNo());

		List<Property> properties = new ArrayList<>();
		RequestInfoWrapper requestWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		try {
			Object fetchResponse = repository.fetchResult(uri, requestWrapper);
			log.info("Property response: ", fetchResponse);

			PropertyResponse res = mapper.convertValue(fetchResponse, PropertyResponse.class);
			log.info("Property response: " + String.valueOf(res));
			properties.addAll(res.getProperties());
		} catch (Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("PROPERTY FETCH ERROR", "Unable to fetch Property Information");
		}
		return properties;
	}

	private void setPTResponseInfo(ConsumerVerificationServiceResponse response, Property connectionResponse) {
		if (connectionResponse != null) {
			response.setTenantId(connectionResponse.getTenantId());
			response.setConsumerNo(connectionResponse.getPropertyId());
			response.setStatus(String.valueOf(connectionResponse.getStatus()));

			StringBuilder address = getAddress(connectionResponse.getAddress());
			response.setAddress(String.valueOf(address));
			
			List<VerificationOwner> owners = new ArrayList<>();
			connectionResponse.getOwners().stream().forEach(item -> {
				VerificationOwner owner = VerificationOwner.builder().name(item.getName())
						.address(item.getCorrespondenceAddress()).build();
				owners.add(owner);
			});
			response.setVerificationOwner(owners);

		}
	}

	private ConsumerVerificationServiceResponse getMRResponse(ConsumerVerificationSearchCriteria searchCriteria,
			ConsumerVerificationServiceResponse response) {
		List<MarriageRegistration> mrRegistrations = getMRRegistrations(searchCriteria);
		MarriageRegistration mrRegistrationsResponse = null;

		if (!CollectionUtils.isEmpty(mrRegistrations)) {
			// filtering only APPROVED applications
			List<MarriageRegistration> mrRegistrationsResponses = mrRegistrations.parallelStream()
					.filter(tradeLicense -> tradeLicense.getStatus().equalsIgnoreCase("APPROVED"))
					.collect(Collectors.toList());

			// getting latest Approved application
			if (!CollectionUtils.isEmpty(mrRegistrationsResponses)) {
				Collections.sort(mrRegistrationsResponses,
						Comparator.comparing(e -> e.getApplicationDate(), (s1, s2) -> {
							return s2.compareTo(s1);
						}));
				mrRegistrationsResponse = mrRegistrationsResponses.get(0);
			}
		}

		setMRResponseInfo(response, mrRegistrationsResponse);

		return response;
	}

	private List<MarriageRegistration> getMRRegistrations(ConsumerVerificationSearchCriteria searchCriteria) {

		StringBuilder uri = new StringBuilder(configuration.getMrHost()).append(configuration.getMrSearchEndpoint())
				.append("?").append("tenantId=" + searchCriteria.getTenantId()).append("&").append("offset=0")
				.append("&").append("mrNumbers=" + searchCriteria.getConsumerNo());

		List<MarriageRegistration> mrRegistrations = new ArrayList<>();
		RequestInfoWrapper requestWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		try {
			Object fetchResponse = repository.fetchResult(uri, requestWrapper);
			log.info("Marriage Registration response: ", fetchResponse);

			MarriageRegistrationResponse res = mapper.convertValue(fetchResponse, MarriageRegistrationResponse.class);
			log.info("Marriage Registration response: " + String.valueOf(res));
			mrRegistrations.addAll(res.getMarriageRegistrations());
		} catch (Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("MARRIAGE_REGISTRATION FETCH ERROR",
					"Unable to fetch Marriage Registration Information");
		}
		return mrRegistrations;
	}

	private void setMRResponseInfo(ConsumerVerificationServiceResponse response,
			MarriageRegistration mrRegistrationsResponse) {
		if (mrRegistrationsResponse != null) {
			response.setTenantId(mrRegistrationsResponse.getTenantId());
			response.setConsumerNo(mrRegistrationsResponse.getMrNumber());
			response.setStatus(mrRegistrationsResponse.getStatus());
			response.setAddress(mrRegistrationsResponse.getMarriagePlace().getPlaceOfMarriage());
			List<VerificationOwner> owners = new ArrayList<>();
			mrRegistrationsResponse.getCoupleDetails().stream().forEach(couple -> {

				Address brideAddress = couple.getBride().getAddress();
				StringBuilder brideAdd = getAddress(brideAddress);

				Address groomAddress = couple.getGroom().getAddress();
				StringBuilder groomAdd = getAddress(groomAddress);

				VerificationOwner bride = VerificationOwner.builder().name(couple.getBride().getFirstName())
						.address(String.valueOf(brideAdd)).build();
				owners.add(bride);

				VerificationOwner groom = VerificationOwner.builder().name(couple.getGroom().getFirstName())
						.address(String.valueOf(groomAdd)).build();
				owners.add(groom);

			});
			response.setVerificationOwner(owners);

		}
	}

	private ConsumerVerificationServiceResponse getBPAResponse(ConsumerVerificationSearchCriteria searchCriteria,
			ConsumerVerificationServiceResponse response) {
		List<BPA> bpaRegistrations = getBPARegistrations(searchCriteria);
		BPA bpaRegistrationsResponse = null;

		if (!CollectionUtils.isEmpty(bpaRegistrations)) {
			bpaRegistrationsResponse = bpaRegistrations.get(0);
		}

		setBPAResponseInfo(response, bpaRegistrationsResponse);

		return response;
	}

	private void setBPAResponseInfo(ConsumerVerificationServiceResponse response, BPA bpaRegistrationsResponse) {
		if (bpaRegistrationsResponse != null) {
			response.setTenantId(bpaRegistrationsResponse.getTenantId());
			response.setConsumerNo(bpaRegistrationsResponse.getApplicationNo());
			response.setStatus(bpaRegistrationsResponse.getStatus());

			StringBuilder address = getAddress(bpaRegistrationsResponse.getLandInfo().getAddress());
			response.setAddress(String.valueOf(address));
			
			List<VerificationOwner> owners = new ArrayList<>();
			bpaRegistrationsResponse.getLandInfo().getOwners().stream().forEach(item -> {
				VerificationOwner owner = VerificationOwner.builder().name(item.getName())
						.address(String.valueOf(item.getCorrespondenceAddress())).build();
				owners.add(owner);
			});
			response.setVerificationOwner(owners);

		}
	}

	private List<BPA> getBPARegistrations(ConsumerVerificationSearchCriteria searchCriteria) {
		StringBuilder uri = new StringBuilder(configuration.getBpaHost()).append(configuration.getBpaSearchEndpoint())
				.append("?").append("offset=0").append("&").append("limit=-1").append("&")
				.append("tenantId=" + searchCriteria.getTenantId()).append("&")
				.append("applicationNo=" + searchCriteria.getConsumerNo());

		List<BPA> bpaRegistrations = new ArrayList<>();
		RequestInfoWrapper requestWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		try {
			Object fetchResponse = repository.fetchResult(uri, requestWrapper);
			log.info("BPA response: ", fetchResponse);

			BPAResponse res = mapper.convertValue(fetchResponse, BPAResponse.class);
			log.info("BPA response: " + String.valueOf(res));
			bpaRegistrations.addAll(res.getBPA());
		} catch (Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("BPA FETCH ERROR", "Unable to fetch BPA Information");
		}
		return bpaRegistrations;
	}

}
