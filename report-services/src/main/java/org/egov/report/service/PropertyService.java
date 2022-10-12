package org.egov.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.Payment;
import org.egov.report.model.PaymentSearchCriteria;
import org.egov.report.model.Property;
import org.egov.report.model.PropertyConnectionRequest;
import org.egov.report.model.PropertySearchingCriteria;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.repository.PropertyDetailsReportRepository;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.validator.PropertyReportValidator;
import org.egov.report.web.model.ConsumerMasterWSReportResponse;
import org.egov.report.web.model.OwnerInfo;
import org.egov.report.web.model.PropertyDemandResponse;
import org.egov.report.web.model.PropertyDetailsResponse;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.egov.report.web.model.PropertyResponse;
import org.egov.report.web.model.PropertyWiseCollectionResponse;
import org.egov.report.web.model.PropertyWiseDemandResponse;
import org.egov.report.web.model.TaxCollectorWiseCollectionResponse;
import org.egov.report.web.model.ULBWiseTaxCollectionResponse;
import org.egov.report.web.model.User;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

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

        prValidator.validatePropertyDetailsSearchCriteria(searchCriteria);

        Long count = pdRepository.getPropertyDetailsCount(searchCriteria);
        Integer limit = configuration.getReportLimit();
        Integer offset = 0;
        
        List<PropertyDetailsResponse> response = new ArrayList();
        
        if (count > 0) {
            List<PropertyDetailsResponse> propertyDetailResponse = new ArrayList();
            while (count > 0) {
                propertyDetailResponse = pdRepository.getPropertyDetails(searchCriteria, limit, offset);
                count = count - limit;
                offset += limit;

                if (!CollectionUtils.isEmpty(propertyDetailResponse)) {

                    // Extracting user info from userService

                    Set<String> userIds = propertyDetailResponse.stream().map(item -> item.getUuid()).distinct()
                            .collect(Collectors.toSet());
                    UserSearchCriteria usCriteria = UserSearchCriteria.builder().uuid(userIds)
                            .active(true)
                            .userType(UserSearchCriteria.CITIZEN)
                            .tenantId(searchCriteria.getUlbName())
                            .build();
                    List<OwnerInfo> usersInfo = userService.getUserDetails(requestInfo, usCriteria);
                    Map<String, User> userMap = usersInfo.stream()
                            .collect(Collectors.toMap(User::getUuid, Function.identity()));
                    propertyDetailResponse.stream().forEach(item -> {
                        User user = userMap.get(item.getUuid());
                        if (user != null) {
                            item.setMobileNumber(user.getMobileNumber());
                            item.setName(user.getName());
                        }
                    });
                }
                response.addAll(propertyDetailResponse);
            }
            
        }
        return response;
    }

    public List<TaxCollectorWiseCollectionResponse> getTaxCollectorWiseCollections(RequestInfo requestInfo,
            PropertyDetailsSearchCriteria searchCriteria) {

        prValidator.validateTaxCollectorWiseCollectionSearchCriteria(searchCriteria);

        Set<String> businessService = new HashSet<String>();
        businessService.add("PT");
        PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder().businessServices(businessService)
                .tenantId(searchCriteria.getUlbName()).fromDate(searchCriteria.getStartDate())
                .toDate(searchCriteria.getEndDate()).build();

        List<Payment> payments = paymentService.getPayments(requestInfo, paymentSearchCriteria);
        List<TaxCollectorWiseCollectionResponse> taxCollectorWiseCollectionResponse = new ArrayList<TaxCollectorWiseCollectionResponse>();
        List<TaxCollectorWiseCollectionResponse> response = new ArrayList<TaxCollectorWiseCollectionResponse>();

        for (Payment res : payments) {

            TaxCollectorWiseCollectionResponse taxCollectorWiseCollection = new TaxCollectorWiseCollectionResponse();
            taxCollectorWiseCollection.setAmountpaid(res.getTotalAmountPaid().toString());
            taxCollectorWiseCollection.setPaymentMode(res.getPaymentMode().toString());
            taxCollectorWiseCollection.setReceiptnumber(res.getPaymentDetails().get(0).getReceiptNumber());
            taxCollectorWiseCollection.setConsumercode(res.getPaymentDetails().get(0).getBill().getConsumerCode());
            taxCollectorWiseCollection.setPaymentdate(res.getTransactionDate().toString());
            taxCollectorWiseCollection.setUserid(res.getAuditDetails().getCreatedBy());
            taxCollectorWiseCollection.setTenantid(res.getTenantId());

            taxCollectorWiseCollectionResponse.add(taxCollectorWiseCollection);

        }

        List<Long> uids = new ArrayList<>();
        UserSearchCriteria usCriteria = new UserSearchCriteria();
        Set<String> Propertys = new HashSet<>();
        PropertySearchingCriteria pdsCriteria = new PropertySearchingCriteria();

        if (!CollectionUtils.isEmpty(taxCollectorWiseCollectionResponse)) {
            taxCollectorWiseCollectionResponse.forEach(res -> uids.add(Long.valueOf(res.getUserid())));
            usCriteria.setId(uids);
            taxCollectorWiseCollectionResponse.forEach(res -> Propertys.add((res.getConsumercode())));
            pdsCriteria.setProperty(Propertys);

            List<Long> ids = uids.stream().distinct().collect(Collectors.toList());
            UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder().tenantId(searchCriteria.getUlbName())
                    .userType(UserSearchCriteria.EMPLOYEE).active(true)
                    .id(ids).build();
            List<OwnerInfo> info = userService.getUserDetails(requestInfo, userSearchCriteria);
            for (TaxCollectorWiseCollectionResponse res : taxCollectorWiseCollectionResponse) {
                info.forEach(item -> {
                    if (res.getUserid().equalsIgnoreCase(item.getId().toString())) {
                        res.setMobilenumber(item.getMobileNumber());
                        res.setName(item.getName());
                        res.setEmployeeid(item.getUserName());
                        res.setType(item.getType());
                    }
                });
            }

            taxCollectorWiseCollectionResponse.forEach(res -> uids.add(Long.valueOf(res.getUserid())));
            usCriteria.setId(uids);

            PropertySearchingCriteria propsCriteria = PropertySearchingCriteria.builder()
                    .tenantid(searchCriteria.getUlbName()).property(Propertys).build();

            List<Property> propinfo = getProperty(requestInfo, propsCriteria);
            for (TaxCollectorWiseCollectionResponse res : taxCollectorWiseCollectionResponse) {
                propinfo.forEach(item -> {
                    if (res.getConsumercode().equalsIgnoreCase(item.getPropertyId())) {
                        res.setOldpropertyid(item.getOldPropertyId());
                    }
                });
            }

        }

        return taxCollectorWiseCollectionResponse;
    }

	public List<Property> getProperty(RequestInfo requestInfo, PropertySearchingCriteria searchCriteria) {

		String propertyids = searchCriteria.getProperty().stream().collect(Collectors.joining(","));

		StringBuilder uri = new StringBuilder(configuration.getPtHost()).append(configuration.getPtSearchEndpoint())
				.append("?").append("tenantId=").append(searchCriteria.getTenantid()).append("&").append("propertyIds=")
				.append(propertyids);

		List<Property> prop = new ArrayList<>();
		PropertyConnectionRequest request = PropertyConnectionRequest.builder().requestInfo(requestInfo).build();

		try {
			Object response = repository.fetchResult(uri, request);
			PropertyResponse propResponse = mapper.convertValue(response, PropertyResponse.class);
			prop.addAll(propResponse.getPropInfo());
		} catch (Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("PROPERTY_FETCH_EXCEPTION", "Unable to fetch Property Information");
		}

		return prop;
	}

	public List<ULBWiseTaxCollectionResponse> getulbWiseTaxCollections(RequestInfo requestInfo,
			PropertyDetailsSearchCriteria searchCriteria) {

		prValidator.validatePropertyDetailsSearchCriteria(searchCriteria);

		List<ULBWiseTaxCollectionResponse> propertyResponse = new ArrayList<ULBWiseTaxCollectionResponse>();

		Long count = pdRepository.getPropertyDemandDetailsCount(searchCriteria);
        Integer limit = configuration.getReportLimit();
        Integer offset = 0;
        
        if (count > 0) {
            Map<String, List<PropertyDemandResponse>> propertyDemandResponse = new HashMap<>();
            while (count > 0) {
                propertyDemandResponse = pdRepository.getPropertyDemandDetails(searchCriteria , limit , offset);
                count = count - limit;
                offset += limit;

		if (!CollectionUtils.isEmpty(propertyDemandResponse)) {

		//	propertyDemandResponse.forEach((key, value) -> {
		    propertyDemandResponse.entrySet().parallelStream().forEach(obj -> {
		        
				ULBWiseTaxCollectionResponse propertyInfo = new ULBWiseTaxCollectionResponse();

			//	value.forEach(item -> {
				obj.getValue().forEach(item -> {
					if (item.getTaxperiodto() < System.currentTimeMillis()) { 
						
						propertyInfo.setTotalarreartaxamount((propertyInfo.getTotalarreartaxamount()).add(item.getTaxamount()));
					}

					else {
						propertyInfo.setTotaltaxamount((propertyInfo.getTotaltaxamount()).add(item.getTaxamount()));
					}

					propertyInfo.setTotalcollectionamount((propertyInfo.getTotalcollectionamount()).add(item.getCollectionamount()));

				});

				BigDecimal dueAmountWithTotalArrear = propertyInfo.getDueamount().add(propertyInfo.getTotalarreartaxamount());
				BigDecimal dueAmountWithCurrentYearDemand = dueAmountWithTotalArrear.add(propertyInfo.getTotaltaxamount());
				BigDecimal dueAmountFinal = dueAmountWithCurrentYearDemand.subtract(propertyInfo.getTotalcollectionamount());

				propertyInfo.setPropertyId(obj.getKey());
				propertyInfo.setDueamount(dueAmountFinal);
				propertyInfo.setUlb(obj.getValue().get(0).getTenantid());
				propertyInfo.setOldpropertyid(obj.getValue().get(0).getOldpropertyid());
				propertyInfo.setWard(obj.getValue().get(0).getWard());

				propertyResponse.add(propertyInfo);
			});


        }
    }
}
		return propertyResponse;
	}

	public List<PropertyWiseDemandResponse> getpropertyWiseDemandReport(RequestInfo requestInfo,
			PropertyDetailsSearchCriteria searchCriteria) {

		prValidator.validatePropertyDetailsSearchCriteria(searchCriteria);

		List<PropertyWiseDemandResponse> propertyResponse = new ArrayList<PropertyWiseDemandResponse>();

		Map<String, List<PropertyDemandResponse>> propertyDemandResponse = pdRepository
				.getPropertyWiseDemandDetails(searchCriteria);

		if (!CollectionUtils.isEmpty(propertyDemandResponse)) {

			propertyDemandResponse.forEach((key, value) -> {
				value.forEach(item -> {
					PropertyWiseDemandResponse propertyInfo = new PropertyWiseDemandResponse();
					BigDecimal currentYearDemandAmount = BigDecimal.ZERO;
					BigDecimal totalCollectionAmount = BigDecimal.ZERO;
					BigDecimal dueAmount = BigDecimal.ZERO;

					if (item.getTaxamount() == null) {
						propertyInfo.setTaxamount(currentYearDemandAmount.toString());
					} else {
						currentYearDemandAmount = item.getTaxamount();
						propertyInfo.setTaxamount(currentYearDemandAmount.toString());
					}

					if (item.getCollectionamount() == null) {
						propertyInfo.setCollectionamount(totalCollectionAmount.toString());
					} else {
						totalCollectionAmount = item.getCollectionamount();
						propertyInfo.setCollectionamount(totalCollectionAmount.toString());
					}
					BigDecimal dueAmountWithCurrentYearDemandAmount = dueAmount.add(currentYearDemandAmount);
					BigDecimal dueAmountFinal = dueAmountWithCurrentYearDemandAmount.subtract(totalCollectionAmount);
					propertyInfo.setDueamount(dueAmountFinal.toString());
					propertyInfo.setPropertyId(key);
					propertyInfo.setTaxperiodfrom(item.getTaxperiodfrom().toString());
					propertyInfo.setTaxperiodto(item.getTaxperiodto().toString());
					propertyInfo.setUlb(item.getTenantid());
					propertyInfo.setUuid(item.getUuid());
					propertyInfo.setName(null);
					propertyInfo.setMobilenumber(null);
					propertyInfo.setOldpropertyid(item.getOldpropertyid());
					propertyInfo.setWard(item.getWard());

					propertyResponse.add(propertyInfo);

				});
			});


			// Extracting user info from userService

			Set<String> userIds = propertyResponse.stream().map(item -> item.getUuid()).distinct().collect(Collectors.toSet());
			UserSearchCriteria usCriteria = UserSearchCriteria.builder().uuid(userIds)
					.active(true)
					.userType(UserSearchCriteria.CITIZEN)
					.tenantId(searchCriteria.getUlbName())
					.build();
			List<OwnerInfo> usersInfo = userService.getUserDetails(requestInfo, usCriteria);
			Map<String, User> userMap = usersInfo.stream().collect(Collectors.toMap(User::getUuid, Function.identity()));

			propertyResponse.stream().forEach(item -> {
				User user = userMap.get(item.getUuid());
				if(user!=null) {
					item.setMobilenumber(user.getMobileNumber());
					item.setName(user.getName());
				}
			});
		}
		
		return propertyResponse;
	}

	public List<PropertyWiseCollectionResponse> getpropertyCollectionReport(RequestInfo requestInfo,
			PropertyDetailsSearchCriteria searchCriteria) {

		prValidator.validatePropertyWiseCollectionSearchCriteria(searchCriteria);
		
		List<PropertyWiseCollectionResponse> propertyWiseCollectionResponses = new ArrayList<PropertyWiseCollectionResponse>();

		// Search property by criteria
		List<PropertyDetailsResponse> properties = pdRepository.getPropertiesDetail(searchCriteria);
		
		if(!CollectionUtils.isEmpty(properties)) {
			
			
			//getting payments
			Set<String> propertyIds = properties.stream().map(item -> item.getPropertyId()).distinct().collect(Collectors.toSet());
			
			Set<String> businessService = new HashSet<String>();
			businessService.add("PT");
		
			PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder()
					                                      .businessServices(businessService)
					                                      .tenantId(searchCriteria.getUlbName())
					                                      .consumerCodes(propertyIds)
					                                      .build();
			
			
			List<Payment> payments = paymentService.getPayments(requestInfo, paymentSearchCriteria);

			for (Payment res : payments)  {
				
				PropertyWiseCollectionResponse propertyWiseCollectionResponse = new PropertyWiseCollectionResponse();

				propertyWiseCollectionResponse.setConsumercode(res.getPaymentDetails().get(0).getBill().getConsumerCode());
				propertyWiseCollectionResponse.setDue(res.getTotalDue().toString());
				propertyWiseCollectionResponse.setAmountpaid(res.getTotalAmountPaid().toString());
				BigDecimal due = res.getTotalDue();
				BigDecimal amountPaid = res.getTotalAmountPaid();
				BigDecimal currentDueInitial = BigDecimal.ZERO;
				BigDecimal currentDueWithTotalDue = currentDueInitial.add(due);
				BigDecimal finalCurrentDue = currentDueWithTotalDue.subtract(amountPaid);
				propertyWiseCollectionResponse.setCurrentdue(finalCurrentDue.toString());
				propertyWiseCollectionResponse.setReceiptdate(res.getPaymentDetails().get(0).getReceiptDate().toString());
				propertyWiseCollectionResponse.setReceiptnumber(res.getPaymentDetails().get(0).getReceiptNumber());
				propertyWiseCollectionResponse.setPaymentMode(res.getPaymentMode().name());

				propertyWiseCollectionResponses.add(propertyWiseCollectionResponse);
				
			}
			
			//setting property data
			
			for (PropertyWiseCollectionResponse res : propertyWiseCollectionResponses) {
				properties.forEach(item -> {
					if (res.getConsumercode().equalsIgnoreCase(item.getPropertyId())) {
						res.setOldpropertyid(item.getOldPropertyId());
						res.setWard(item.getWardNumber());
						res.setUuid(item.getUuid());
					}
				});
			}

				
			//Getting user data
			Set<String> userIds = properties.stream().map(item -> item.getUuid()).distinct().collect(Collectors.toSet());
			
			UserSearchCriteria usCriteria = UserSearchCriteria.builder().uuid(userIds)
					.active(true)
					.userType(UserSearchCriteria.CITIZEN)
					.tenantId(searchCriteria.getUlbName())
					.build();
			List<OwnerInfo> usersInfo = userService.getUserDetails(requestInfo, usCriteria);
			Map<String, User> userMap = usersInfo.stream().collect(Collectors.toMap(User::getUuid, Function.identity()));
			propertyWiseCollectionResponses.stream().forEach(item -> {
				User user = userMap.get(item.getUuid());
				if(user!=null) {
					item.setMobilenumber(user.getMobileNumber());
					item.setName(user.getName());
				}
			});
			



		}
		
		return propertyWiseCollectionResponses;
	}

}
