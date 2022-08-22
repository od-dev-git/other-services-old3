package org.egov.report.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.egov.report.web.model.User;
import org.egov.report.web.model.UserResponse;
import org.egov.tracer.model.CustomException;
import org.egov.common.contract.request.RequestInfo;
import org.egov.report.web.model.IncentiveResponse;
import org.egov.report.web.model.PropertyDetailsResponse;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.Payment;
import org.egov.report.model.PaymentSearchCriteria;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.repository.PropertyDetailsReportRepository;
import org.egov.report.validator.PropertyReportValidator;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.egov.report.web.model.PropertyResponse;
import org.egov.report.web.model.TaxCollectorWiseCollectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.egov.report.repository.ServiceRepository;

import org.springframework.stereotype.Service;
import org.egov.report.model.Property;
import org.egov.report.model.PropertyConnectionRequest;
import org.egov.report.model.PropertySearchingCriteria;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PropertyService {
	
	@Autowired
	private PropertyReportValidator prValidator;

	@Autowired
	private PropertyDetailsReportRepository pdRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private ReportServiceConfiguration configuration;
	
	@Autowired
    private ServiceRepository repository;
	
	@Autowired
	private ObjectMapper mapper;

	public List<PropertyDetailsResponse> getPropertyDetails(RequestInfo requestInfo,
			PropertyDetailsSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub

		prValidator.validatePropertyDetailsSearchCriteria(searchCriteria);

		List<PropertyDetailsResponse> propDetlResponse = pdRepository.getPropertyDetails(searchCriteria);


		//Extracting user info from userService

		Set<String> uuIds = new HashSet<>();
		UserSearchCriteria usCriteria = new UserSearchCriteria();

				if(!CollectionUtils.isEmpty(propDetlResponse)) {
					propDetlResponse.forEach(res -> uuIds.add((res.getUserId())));
					
					usCriteria.setUuId(uuIds);

					 List<User> info = userService.getUserDetails(requestInfo, usCriteria);
					for(PropertyDetailsResponse res : propDetlResponse) {
						info.forEach(
								item -> {
									if(res.getUserId().equalsIgnoreCase(item.getUuid())) {
										res.setMobileNumber(item.getMobileNumber());
										res.setName(item.getName());
									}
								});
						}
					}

		return	propDetlResponse ;	
	}

	public List<TaxCollectorWiseCollectionResponse> gettaxCollectorWiseCollections(RequestInfo requestInfo,
			PropertyDetailsSearchCriteria searchCriteria) {

		prValidator.validatetcwcSearchCriteria(searchCriteria);
		
		Set<String> s = new HashSet<String>();
        s.add("PT");
			PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder()
					.businessServices(s)
					.tenantId(searchCriteria.getUlbName())
					.fromDate(searchCriteria.getStartDate())
					.toDate(searchCriteria.getEndDate()).build();
		
		List<Payment> payments = paymentService.getPayments(requestInfo, paymentSearchCriteria);
		List<TaxCollectorWiseCollectionResponse> tcwcResponse = new ArrayList<TaxCollectorWiseCollectionResponse>();
		
		for(Payment res : payments) {
			
			TaxCollectorWiseCollectionResponse tcwcr = new TaxCollectorWiseCollectionResponse();
							tcwcr.setAmountpaid(res.getTotalAmountPaid().toString());
							tcwcr.setPaymentMode(res.getPaymentMode().toString());
							tcwcr.setReceiptnumber(res.getPaymentDetails().get(0).getReceiptNumber());
							tcwcr.setConsumercode(res.getPaymentDetails().get(0).getBill().getConsumerCode());	
						    tcwcr.setPaymentdate(res.getTransactionDate().toString());
							tcwcr.setUserid(res.getAuditDetails().getCreatedBy());
							tcwcr.setTenantid(res.getTenantId());
							
							tcwcResponse.add(tcwcr);
						
			}

		List<Long> uids = new ArrayList<>();
		UserSearchCriteria usCriteria = new UserSearchCriteria();
		Set<String> Propertys = new HashSet<>();
		PropertySearchingCriteria pdsCriteria = new PropertySearchingCriteria();

		if(!CollectionUtils.isEmpty(tcwcResponse)) {
			tcwcResponse.forEach(res -> uids.add(Long.valueOf(res.getUserid())));
			usCriteria.setUserId(uids);
			tcwcResponse.forEach(res -> Propertys.add((res.getConsumercode())));
			pdsCriteria.setProperty(Propertys);
			

			List<Long> Uniqueids = uids.stream().distinct().collect(Collectors.toList());
			 List<User> info = userService.getUserDetails(requestInfo, Uniqueids);
			for(TaxCollectorWiseCollectionResponse res : tcwcResponse) {
				info.forEach(
						item -> {
							if(res.getUserid().equalsIgnoreCase(item.getId().toString())) {
								res.setMobilenumber(item.getMobileNumber());
								res.setName(item.getName());
								res.setEmployeeid(item.getUserName());
								res.setType(item.getType());
							}
						});
				}
			
			
			tcwcResponse.forEach(res -> uids.add(Long.valueOf(res.getUserid())));
			usCriteria.setUserId(uids);
			

			PropertySearchingCriteria propsCriteria = PropertySearchingCriteria.builder()
					.tenantid(searchCriteria.getUlbName())
					.property(Propertys).build();
			Set<String> Prop = Propertys.stream().distinct().collect(Collectors.toSet());
			
			 List<Property> propinfo = getProperty(requestInfo, propsCriteria);
			for(TaxCollectorWiseCollectionResponse res : tcwcResponse) {
				propinfo.forEach(
						item -> {
							if(res.getConsumercode().equalsIgnoreCase(item.getPropertyId())) {
								res.setOldpropertyid(item.getOldPropertyId());
							}
						});
				}
			
			
			
			}
		
		return tcwcResponse;
	}

	public List<Property> getProperty(RequestInfo requestInfo,
			PropertySearchingCriteria searchCriteria) {
		
        String propertyids = searchCriteria.getProperty().stream().collect(Collectors.joining(","));
		
		StringBuilder uri = new StringBuilder(configuration.getPtHost())
				.append(configuration.getPtSearchEndpoint()).append("?")
				.append("tenantId=").append(searchCriteria.getTenantid()).append("&")
				.append("propertyIds=").append(propertyids);
		
		List<Property> prop = new ArrayList<>();
		PropertyConnectionRequest request = PropertyConnectionRequest.builder().requestInfo(requestInfo).build();
		
		try {
			Object response = repository.fetchResult(uri, request);
			PropertyResponse propResponse = mapper.convertValue(response, PropertyResponse.class);
			prop.addAll(propResponse.getPropInfo());
			}catch(Exception ex) {
				log.error("External Service Call Erorr", ex);
				throw new CustomException("PROPERTY_FETCH_EXCEPTION", "Unable to fetch Property Information");
			}
		
		return	prop ;	
	}




}
