package org.egov.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.service.DemandService;
import org.egov.report.model.Demand;
import org.egov.report.model.DemandDetail;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.Payment;
import org.egov.report.model.PaymentSearchCriteria;
import org.egov.report.model.Property;
import org.egov.report.model.PropertyConnectionRequest;
import org.egov.report.model.PropertySearchingCriteria;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.model.enums.UserType;
import org.egov.report.repository.PropertyDetailsReportRepository;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.validator.PropertyReportValidator;
import org.egov.report.web.model.ConsumerMasterWSReportResponse;
import org.egov.report.web.model.DemandCriteria;
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

import org.springframework.util.StringUtils;
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
	
	@Autowired
    private DemandService demandService;
	
    public List<PropertyDetailsResponse> getPropertyDetails(RequestInfo requestInfo,
            PropertyDetailsSearchCriteria searchCriteria) {

        prValidator.validatePropertyDetailsSearchCriteria(searchCriteria);

        Long count = pdRepository.getPropertyDetailsCount(searchCriteria);
        Integer limit = configuration.getReportLimit();
        Integer offset = 0;
        
        List<PropertyDetailsResponse> response = new ArrayList();
        
        if (count > 0) {
            while (count > 0) {
                searchCriteria.setLimit(limit);
                searchCriteria.setOffset(offset);
                log.info("Feching Property Details ");
                List<PropertyDetailsResponse> propertyDetailResponse = pdRepository.getPropertyDetails(searchCriteria);
                log.info("Property Details Fetched");
                count = count - limit;
                offset += limit;

                if (!CollectionUtils.isEmpty(propertyDetailResponse)) {

                    // Extracting user info from userService
                    log.info("Fetching User Details ");
                    List<String> userIds = propertyDetailResponse.stream().map(propertyDetail -> propertyDetail.getUuid()).distinct()
                            .collect(Collectors.toList());
                    org.egov.report.user.UserSearchCriteria userSearchCriteria = org.egov.report.user.UserSearchCriteria.builder().uuid(userIds)
                            .active(true)
                            .type(UserType.CITIZEN)
                            .build();
                    List<org.egov.report.user.User> usersInfo = userService.searchUsers(userSearchCriteria,
                            requestInfo);
                    log.info("User Details Fetched ");
                    Map<String, org.egov.report.user.User> userMap = usersInfo.stream()
                            .collect(Collectors.toMap(org.egov.report.user.User::getUuid, Function.identity()));
                    log.info("Setting User Details ");
                    propertyDetailResponse.stream().forEach(propertyDetail -> {
                        org.egov.report.user.User user = userMap.get(propertyDetail.getUuid());
                        if (user != null) {
                            propertyDetail.setMobileNumber(user.getMobileNumber());
                            propertyDetail.setName(user.getName());
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
        List<TaxCollectorWiseCollectionResponse> taxCollectorWiseCollectionResponse =  payments.parallelStream().map(payment  ->{
            
            TaxCollectorWiseCollectionResponse taxCollectorWiseCollection = TaxCollectorWiseCollectionResponse
                  .builder()
                  .amountpaid(payment.getTotalAmountPaid().toString())
                  .paymentMode(payment.getPaymentMode().toString())
                  .receiptnumber(payment.getPaymentDetails().get(0).getReceiptNumber())
                  .consumercode(payment.getPaymentDetails().get(0).getBill().getConsumerCode())
                  .paymentdate(payment.getTransactionDate().toString())
                  .userid(payment.getAuditDetails().getCreatedBy())
                  .tenantid(payment.getTenantId())
                  .build();
            return taxCollectorWiseCollection;
        }).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(taxCollectorWiseCollectionResponse)) {
            List<Long> userIds = taxCollectorWiseCollectionResponse.stream().map(res ->Long.valueOf(res.getUserid())).distinct().collect(Collectors.toList());
            Set<String> Properties = taxCollectorWiseCollectionResponse.stream().map(res -> (res.getConsumercode())).collect(Collectors.toSet());
            
         // Extracting user info from userService
            log.info("Fetching User Details ");
            org.egov.report.user.UserSearchCriteria userSearchCriteria = org.egov.report.user.UserSearchCriteria
                    .builder()
                    .tenantId(searchCriteria.getUlbName())
                    .active(true)
                    .type(UserType.EMPLOYEE)
                    .id(userIds)
                    .build();
            List<org.egov.report.user.User> usersInfo = userService.searchUsers(userSearchCriteria,
                    requestInfo);
            log.info("User Details Fetched ");
            Map<Long, org.egov.report.user.User> userMap = usersInfo.stream()
                    .collect(Collectors.toMap(org.egov.report.user.User::getId, Function.identity()));
            log.info("Setting User Details ");
            taxCollectorWiseCollectionResponse.stream().forEach(collectionResponse -> {
                org.egov.report.user.User user = userMap.get(Long.valueOf(collectionResponse.getUserid()));
                if (user != null) {
                    collectionResponse.setMobilenumber(user.getMobileNumber());
                    collectionResponse.setName(user.getName());
                    collectionResponse.setEmployeeid(user.getUsername());
                    collectionResponse.setType(user.getType().toString());
                }
            });
            
            //getting Property Details
            log.info("Fetching Property Details ");
            PropertyDetailsSearchCriteria propertySearchingCriteria = PropertyDetailsSearchCriteria.builder()
                    .ulbName(searchCriteria.getUlbName()).propertyIds(Properties).build();
            HashMap<String , String> propertyMap = pdRepository.getOldPropertyIds(propertySearchingCriteria);
            log.info("Setting Property Details ");
            taxCollectorWiseCollectionResponse.parallelStream().forEach(taxCollectorWiseCollection ->{
                if(StringUtils.hasText(propertyMap.get(taxCollectorWiseCollection.getConsumercode()))){
                    String oldPropertyId = propertyMap.get(taxCollectorWiseCollection.getConsumercode());
                    taxCollectorWiseCollection.setOldpropertyid(oldPropertyId);
                }
            });

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
            while (count > 0) {
                searchCriteria.setLimit(limit);
                searchCriteria.setOffset(offset);
                Map<String, List<PropertyDemandResponse>> propertyDemandResponse = pdRepository.getPropertyDemandDetails(searchCriteria);
                count = count - limit;
                offset += limit;

                if (!CollectionUtils.isEmpty(propertyDemandResponse)) {
                    propertyDemandResponse.entrySet().parallelStream().forEach(object -> {
                        ULBWiseTaxCollectionResponse propertyInfo = new ULBWiseTaxCollectionResponse();
                        object.getValue().forEach(demands -> {
                            if (demands.getTaxperiodto() < System.currentTimeMillis()) {
                                propertyInfo.setTotalarreartaxamount(
                                        (propertyInfo.getTotalarreartaxamount()).add(demands.getTaxamount()));
                            } else {
                                propertyInfo
                                        .setTotaltaxamount((propertyInfo.getTotaltaxamount()).add(demands.getTaxamount()));
                            }
                            propertyInfo.setTotalcollectionamount(
                                    (propertyInfo.getTotalcollectionamount()).add(demands.getCollectionamount()));
                        });

                        BigDecimal dueAmountWithTotalArrear = propertyInfo.getDueamount()
                                .add(propertyInfo.getTotalarreartaxamount());
                        BigDecimal dueAmountWithCurrentYearDemand = dueAmountWithTotalArrear
                                .add(propertyInfo.getTotaltaxamount());
                        BigDecimal dueAmountFinal = dueAmountWithCurrentYearDemand
                                .subtract(propertyInfo.getTotalcollectionamount());

                        propertyInfo.setPropertyId(object.getKey());
                        propertyInfo.setDueamount(dueAmountFinal);
                        propertyInfo.setUlb(object.getValue().get(0).getTenantid());
                        propertyInfo.setOldpropertyid(object.getValue().get(0).getOldpropertyid());
                        propertyInfo.setWard(object.getValue().get(0).getWard());

                        if(propertyInfo == null) {
                            log.info("null object found " + propertyInfo.toString());
                        }
                        if (!Objects.isNull(propertyInfo) && StringUtils.hasText(propertyInfo.getUlb())) {
                            propertyResponse.add(propertyInfo);
                        }
                    });
                }
                
                
            }
        }
        return propertyResponse;
    }

    public List<PropertyWiseDemandResponse> getpropertyWiseDemandReport(RequestInfo requestInfo,
            PropertyDetailsSearchCriteria searchCriteria) {

        prValidator.validatePropertyDetailsSearchCriteria(searchCriteria);

        List<PropertyWiseDemandResponse> propertyWiseDemandResponse = new ArrayList<PropertyWiseDemandResponse>();

        Long count = pdRepository.getPropertyCount(searchCriteria);
        Integer limit = configuration.getReportConnectionsLimit();
        Integer offset = 0;

        if (count > 0) {

            Set<String> allProperties = (pdRepository.getPropertyIds(searchCriteria)).stream().distinct()
                    .collect(Collectors.toSet());

            while (count > 0) {

                searchCriteria.setLimit(limit);
                searchCriteria.setOffset(offset);
                Set<String> tempPropertiesListSet = allProperties.stream().skip(offset).limit(limit)
                        .collect(Collectors.toSet());
                count = count - limit;
                offset += limit;

                log.info("tempPropertiesListSet" + tempPropertiesListSet.toString());
                searchCriteria.setPropertyIds(tempPropertiesListSet);
                List<PropertyDetailsResponse> properties = pdRepository.getPropertyDetail(searchCriteria);// expand
                                                                                                          // result

                DemandCriteria demandCriteria = DemandCriteria.builder().tenantId(searchCriteria.getUlbName())
                        .consumerCode(tempPropertiesListSet).build();
                List<Demand> demands = demandService.getDemands(demandCriteria, requestInfo);
                log.info("demands" + demands.toString());

                List<PropertyWiseDemandResponse> tempResponseList = demands.parallelStream().map(row -> {

                    PropertyWiseDemandResponse tempResponse = PropertyWiseDemandResponse.builder()
                            .propertyId(row.getConsumerCode())
                            .taxperiodfrom(String.valueOf(row.getTaxPeriodFrom()))
                            .taxperiodto(String.valueOf(row.getTaxPeriodTo())).ulb(row.getTenantId())
                            .build();

                    List<DemandDetail> tempDemandDetail = row.getDemandDetails();
                    BigDecimal taxAmount = BigDecimal.ZERO;
                    BigDecimal collectionAmount = BigDecimal.ZERO;
                    tempResponse.setTaxamount(taxAmount.toString());
                    tempResponse.setCollectionamount(collectionAmount.toString());
                    tempDemandDetail.stream().forEach(demandUnit -> {
                        if (demandUnit.getTaxAmount() != null) {
                            tempResponse.setTaxamount(new BigDecimal(tempResponse.getTaxamount())
                                    .add(demandUnit.getTaxAmount()).toString());
                        }
                        if (demandUnit.getCollectionAmount() != null) {
                            tempResponse.setCollectionamount(new BigDecimal(tempResponse.getCollectionamount())
                                    .add(demandUnit.getCollectionAmount()).toString());
                        }
                    });
                    taxAmount = new BigDecimal(tempResponse.getTaxamount());
                    collectionAmount = new BigDecimal(tempResponse.getCollectionamount());
                    tempResponse.setDueamount(taxAmount.subtract(collectionAmount).toString());

                    return tempResponse;

                }).collect(Collectors.toList());

                log.info("tempResponseList enriched" + tempResponseList.toString());

                // setting properties data
                log.info("setting properties data ");
                final List<PropertyDetailsResponse> tempProperties = properties;
                tempResponseList.stream().forEach(item -> {
                    if (item == null) {
                        log.info(" null item found ");
                    }
                    if (item != null) {
                        List<String> uuids = new ArrayList<>();
                        tempProperties.stream().forEach(prop -> {
                            if (prop != null) {
                                if (prop.getPropertyId().equals(item.getPropertyId())) {
                                    if (StringUtils.hasText(prop.getOldPropertyId())) {
                                        item.setOldpropertyid(prop.getOldPropertyId());
                                    }
                                    if (StringUtils.hasText(prop.getWardNumber())) {
                                        item.setWard(prop.getWardNumber());
                                    }
                                    uuids.add(prop.getUuid());
                                }
                            }
                        });
                        item.setUuid(uuids);
                    }
                });

                log.info("setting user data");
                // Extracting user info from userService

                Set<String> userIds = properties.stream().map(item -> item.getUuid()).distinct()
                        .collect(Collectors.toSet());
                UserSearchCriteria usCriteria = UserSearchCriteria.builder().uuid(userIds)
                        .active(true)
                        .userType(UserSearchCriteria.CITIZEN)
                        .tenantId(searchCriteria.getUlbName())
                        .build();
                List<OwnerInfo> usersInfo = userService.getUserDetails(requestInfo, usCriteria);
                Map<String, User> userMap = usersInfo.stream()
                        .collect(Collectors.toMap(User::getUuid, Function.identity()));

                tempResponseList.stream().forEach(item -> {
                    if (item != null) {
                        item.getUuid().stream().forEach(uid -> {
                            if (uid != null) {
                                User user = userMap.get(uid);
                                if (user != null) {
                                    if (StringUtils.hasText(item.getMobilenumber())
                                            && StringUtils.hasText(user.getMobileNumber())) {
                                        item.setMobilenumber(item.getMobilenumber() + " , " + user.getMobileNumber());
                                    } else {
                                        if (StringUtils.hasText(user.getMobileNumber())) {
                                            item.setMobilenumber(user.getMobileNumber());
                                        }
                                    }
                                    if (StringUtils.hasText(item.getName()) && StringUtils.hasText(user.getName())) {
                                        item.setName(item.getName() + " , " + user.getName());
                                    } else {
                                        if (StringUtils.hasText(user.getName())) {
                                            item.setName(user.getName());
                                        }
                                    }

                                }
                            }
                        });
                    }
                });

                propertyWiseDemandResponse.addAll(tempResponseList);
                log.info("propertyWiseDemandResponse enriched");
            }

        }
        return propertyWiseDemandResponse;
    }

	public List<PropertyWiseCollectionResponse> getpropertyCollectionReport(RequestInfo requestInfo,
			PropertyDetailsSearchCriteria searchCriteria) {

		prValidator.validatePropertyWiseCollectionSearchCriteria(searchCriteria);
		
		List<PropertyWiseCollectionResponse> propertyWiseCollectionResponses = new ArrayList<PropertyWiseCollectionResponse>();

		// Search property by criteria
		Long count = pdRepository.getPropertiesDetailCount(searchCriteria);
        Integer limit = configuration.getReportLimit();
        Integer offset = 0;
        
        if (count > 0) {
            List<PropertyDetailsResponse> properties = new ArrayList<>();
            while (count > 0) {
                searchCriteria.setLimit(limit);
                searchCriteria.setOffset(offset);
                properties = pdRepository.getPropertiesDetail(searchCriteria);
                count = count - limit;
                offset += limit;
		
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
    }

}

        return propertyWiseCollectionResponses;
    }

}