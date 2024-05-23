package org.egov.report.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.report.service.DemandService;
import org.egov.report.util.DCBReportExcelGenerator;
import org.egov.report.util.PTAssessmentsReportExcelGenerator;
import org.egov.report.util.PaymentUtil;
import org.egov.report.util.PaymetsReportExcelGenerator;
import org.egov.report.util.ReportConstants;
import org.egov.report.util.Util;
import org.egov.report.model.BillAccountDetail;
import org.egov.report.model.BillDetail;
import org.egov.report.model.Demand;
import org.egov.report.model.DemandDetail;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.Payment;
import org.egov.report.model.PaymentDetail;
import org.egov.report.model.PaymentSearchCriteria;
import org.egov.report.model.Property;
import org.egov.report.model.PropertyConnectionRequest;
import org.egov.report.model.PropertySearchingCriteria;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.model.UtilityReportDetails;
import org.egov.report.model.UtilityReportSearchCriteria;
import org.egov.report.model.enums.UserType;
import org.egov.report.repository.DCBRepository;
import org.egov.report.repository.PropertyDetailsReportRepository;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.validator.PropertyReportValidator;
import org.egov.report.web.model.ConsumerMasterWSReportResponse;
import org.egov.report.web.model.DemandCriteria;
import org.egov.report.web.model.OwnerInfo;
import org.egov.report.web.model.PTAssessmentSearchCriteria;
import org.egov.report.web.model.PropertyDemandResponse;
import org.egov.report.web.model.PropertyDetailsResponse;
import org.egov.report.web.model.PropertyDetailsSearchCriteria;
import org.egov.report.web.model.PropertyResponse;
import org.egov.report.web.model.PropertyWiseCollectionResponse;
import org.egov.report.web.model.PropertyWiseDemandResponse;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.report.web.model.TaxCollectorWiseCollectionResponse;
import org.egov.report.web.model.ULBWiseTaxCollectionResponse;
import org.egov.report.web.model.User;
import org.egov.report.web.model.UtilityReportRequest;
import org.egov.report.web.model.WaterConnectionDetails;
import org.egov.report.web.model.WaterMonthlyDemandResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
	
	@Autowired
	private PaymentUtil paymentUtil;
	
	@Autowired
	private DCBRepository dcbRepository;

	@Autowired
	private Util utils;
	
	@Autowired
	private FileStoreService fileStoreService;
	
	@Autowired
	private EnrichmentService enrichmentService;
	
	public static final List<String> TAXHEADS_NOT_ALLOWED_FOR_DEMAND_REPORT = Collections
			.unmodifiableList(Arrays.asList("PT_ADVANCE_CARRYFORWARD","PT_PENALTY","PT_INTEREST","PT_TIME_REBATE","PT_TIME_PENALTY"));
	
	public static final List<String> TAXHEADS_FOR_ADVANCE = Collections
	.unmodifiableList(Arrays.asList("PT_ADVANCE_CARRYFORWARD"));
	
	public static final String PAYMENTMODE_ONLINE = "ONLINE";

	public static final String PAYMENTMODE_OFFLINE = "OFFLINE";

	
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
                    Collection<PropertyDetailsResponse> getAllPropertyDetails = mergeUserDetails(propertyDetailResponse,userMap);
					 response.addAll(getAllPropertyDetails);
                }
            }
            
        }
        return response;
    }


    private Collection<PropertyDetailsResponse> mergeUserDetails(List<PropertyDetailsResponse> propertyDetailResponse,
			Map<String, org.egov.report.user.User> userMap) {
		Map<String, PropertyDetailsResponse> propertyResponseDetail = new HashMap<>();
		propertyDetailResponse.stream().forEach(propertyDetail -> {
			org.egov.report.user.User user = userMap.get(propertyDetail.getUuid());
        log.info("Adiing Use Details for : " + propertyDetail.getPropertyId());
			if (user != null) {
				if (propertyResponseDetail.get(propertyDetail.getPropertyId()) != null) {
					PropertyDetailsResponse propertyUser = propertyResponseDetail.get(propertyDetail.getPropertyId());
					propertyUser.setMobileNumber(propertyUser.getMobileNumber() + " , " + user.getMobileNumber());
					propertyUser.setName(propertyUser.getName() + " , " + user.getName());
					propertyUser.setGuardianName(propertyUser.getGuardianName() + " , " + user.getGuardian());
					propertyUser.setGuardianRelation(propertyUser.getGuardianRelation() + " , " + user.getGuardianrelation());
				} else {
					propertyDetail.setMobileNumber(user.getMobileNumber());
					propertyDetail.setName(user.getName());
					if(StringUtils.hasText(user.getGuardian())) {
						propertyDetail.setGuardianName(user.getGuardian());
					}
					if(StringUtils.hasText(user.getGuardianrelation())) {
						propertyDetail.setGuardianRelation(user.getGuardianrelation());
					}
					propertyResponseDetail.put(propertyDetail.getPropertyId(), propertyDetail);
				}

			}
		});
		return propertyResponseDetail.values();
	}


    public List<TaxCollectorWiseCollectionResponse> getTaxCollectorWiseCollections(RequestInfo requestInfo,
            PropertyDetailsSearchCriteria searchCriteria) {

        prValidator.validateTaxCollectorWiseCollectionSearchCriteria(searchCriteria);

        Set<String> businessService = new HashSet<String>();
        businessService.add("PT");
        PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder().businessServices(businessService)
                .tenantId(searchCriteria.getUlbName()).fromDate(searchCriteria.getStartDate())
                .toDate(searchCriteria.getEndDate()).build();

//		paymentSearchCriteria.setPaymentModes(Stream
//				.of("CASH", "OFFLINE_RTGS", "OFFLINE_NEFT", "POSTAL_ORDER", "CHEQUE","DD","CARD").collect(Collectors.toSet()));

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
            
			BigDecimal arrearCollected = getColletdArrearAmount(payment);
			taxCollectorWiseCollection.setArrearCollection(taxCollectorWiseCollection.getArrearCollection().add(arrearCollected));
			taxCollectorWiseCollection.setCurrentCollection(taxCollectorWiseCollection.getCurrentCollection().add(payment.getTotalAmountPaid().subtract(arrearCollected)));

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
                    .type(UserType.EMPLOYEE)
                    .id(userIds)
                    .build();
            List<org.egov.report.user.User> usersInfo = userService.searchUsers(userSearchCriteria,
                    requestInfo);
            log.info("User Details Fetched ");
            Map<Long, org.egov.report.user.User> userMap = usersInfo.stream()
                    .collect(Collectors.toMap(org.egov.report.user.User::getId, Function.identity()));
            
            //getting Property Details
            log.info("Fetching Property Details ");
            PropertyDetailsSearchCriteria propertySearchingCriteria = PropertyDetailsSearchCriteria.builder()
                    .ulbName(searchCriteria.getUlbName()).propertyIds(Properties).build();
            HashMap<String , PropertyDetailsResponse> propertyMap = pdRepository.getOldPropertyIds(propertySearchingCriteria);
            
            log.info("Setting User Details ");
            taxCollectorWiseCollectionResponse = taxCollectorWiseCollectionResponse.stream().peek(collectionResponse -> {
                org.egov.report.user.User user = userMap.get(Long.valueOf(collectionResponse.getUserid()));
                if (user != null) {
                    collectionResponse.setMobilenumber(user.getMobileNumber());
                    collectionResponse.setName(user.getName());
                    collectionResponse.setEmployeeid(user.getUsername());
                    collectionResponse.setType(user.getType().toString());
                    if(StringUtils.hasText(propertyMap.get(collectionResponse.getConsumercode()).getOldPropertyId())){
                        collectionResponse.setOldpropertyid(propertyMap.get(collectionResponse.getConsumercode()).getOldPropertyId());
                        collectionResponse.setDdnNo(propertyMap.get(collectionResponse.getConsumercode()).getDdnNo());
                        collectionResponse.setLegacyHoldingNo(propertyMap.get(collectionResponse.getConsumercode()).getLegacyHoldingNo());
                    }
                }
            }).filter(tcwcrUser -> StringUtils.hasText(tcwcrUser.getType())).collect(Collectors.toList());
            
//            //getting Property Details
//            log.info("Fetching Property Details ");
//            PropertyDetailsSearchCriteria propertySearchingCriteria = PropertyDetailsSearchCriteria.builder()
//                    .ulbName(searchCriteria.getUlbName()).propertyIds(Properties).build();
//            HashMap<String , String> propertyMap = pdRepository.getOldPropertyIds(propertySearchingCriteria);
//            log.info("Setting Property Details ");
//            taxCollectorWiseCollectionResponse.parallelStream().forEach(taxCollectorWiseCollection ->{
//                if(StringUtils.hasText(propertyMap.get(taxCollectorWiseCollection.getConsumercode()))){
//                    String oldPropertyId = propertyMap.get(taxCollectorWiseCollection.getConsumercode());
//                    taxCollectorWiseCollection.setOldpropertyid(oldPropertyId);
//                }
//            });

        }

        return taxCollectorWiseCollectionResponse;
    }
    
	private BigDecimal getColletdArrearAmount(Payment payment) {
		
		Comparator<BillDetail> billDetailComparator = (obj1, obj2) -> obj2.getFromPeriod().compareTo(obj1.getFromPeriod());
		BigDecimal arrearCollected = BigDecimal.ZERO;
		
		for (PaymentDetail pd : payment.getPaymentDetails()) {
			Collections.sort(pd.getBill().getBillDetails(), billDetailComparator);
			for(int i=0; i<pd.getBill().getBillDetails().size(); i++) {
				if(i != 0) {
					arrearCollected = arrearCollected.add(pd.getBill().getBillDetails().get(i).getAmountPaid());
				}
			}
		}
		
		return arrearCollected;
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

                log.info("tempPropertiesListSet" + tempPropertiesListSet.toString());
                searchCriteria.setPropertyIds(tempPropertiesListSet);
                List<PropertyDetailsResponse> properties = pdRepository.getPropertyDetail(searchCriteria);
                Map<String, List<PropertyDetailsResponse>> propertyMap = properties.stream()
                        .collect(Collectors.groupingBy(
                                PropertyDetailsResponse::getPropertyId,
                                Collectors.mapping(
                                        Function.identity(), Collectors.toList())));

                DemandCriteria demandCriteria = DemandCriteria
                        .builder()
                        .tenantId(searchCriteria.getUlbName())
                        .consumerCode(tempPropertiesListSet)
                        .build();
                List<Demand> demands = demandService.getDemands(demandCriteria, requestInfo);
                log.info("demands" + demands.toString());
                log.info(" Demands Count fetched from DB " + demands.size());
                ;
                Map<Object, List<Demand>> demandsGroupedByConnectionNo = demands.stream()
                        .collect(Collectors.groupingBy(Demand::getConsumerCode));
                log.info("No of demands Grouped By Connection No : " + demandsGroupedByConnectionNo.size());

                if (!CollectionUtils.isEmpty(demandsGroupedByConnectionNo)) {// use Collect To List
                    List<ULBWiseTaxCollectionResponse> tempResponse = demandsGroupedByConnectionNo.entrySet()
                            .parallelStream().map(connection -> {

                                ULBWiseTaxCollectionResponse responsePerConnection = new ULBWiseTaxCollectionResponse();
                                String connectionNo = String.valueOf(connection.getKey());
                                log.info(" Connection No :  " + connectionNo.toString());
                                List<Demand> connectionDemands = connection.getValue();

                                if (connectionDemands != null) {

                                    Collections.sort(connectionDemands,
                                            Comparator.comparing(e -> e.getTaxPeriodTo(), (s1, s2) -> {
                                                return s2.compareTo(s1);
                                            }));

                                    List<DemandDetail> currentDemandDetails = connectionDemands.get(0)
                                            .getDemandDetails();
                                    if (currentDemandDetails != null) {

                                        BigDecimal currentYearDemand = currentDemandDetails.stream()
                                                .map(w -> w.getTaxAmount())
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                                        responsePerConnection.setTotaltaxamount(currentYearDemand);
                                    }

                                    BigDecimal totalArrearDemandAmt = connectionDemands.stream().skip(1)
                                            .flatMap(item -> item.getDemandDetails().stream())
                                            .map(w -> w.getTaxAmount())
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                                    BigDecimal totalCollectionAmt = connectionDemands.stream()
                                            .flatMap(item -> item.getDemandDetails().stream())
                                            .map(w -> w.getCollectionAmount())
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                                    BigDecimal currentYearTaxAmt = responsePerConnection.getTotaltaxamount();
                                    BigDecimal totalDueAmt = currentYearTaxAmt.add(totalArrearDemandAmt)
                                            .subtract(totalCollectionAmt);

                                    responsePerConnection.setTotalarreartaxamount(totalArrearDemandAmt);
                                    responsePerConnection.setTotalcollectionamount(totalCollectionAmt);
                                    responsePerConnection.setDueamount(totalDueAmt);
                                    
                                    responsePerConnection.setPropertyId(connectionNo);
                                    String tenantId = connectionDemands.get(0).getTenantId();
                                    String tenantIdStyled = tenantId.replace("od.", "");
                                    tenantIdStyled = tenantIdStyled.substring(0, 1).toUpperCase()
                                            + tenantIdStyled.substring(1).toLowerCase();
                                    responsePerConnection.setUlb(tenantIdStyled);

                                    // setting properties data
                                    List<PropertyDetailsResponse> propertyDetail = propertyMap
                                            .get(responsePerConnection.getPropertyId());
                                    if (propertyDetail != null) {
                                        responsePerConnection
                                                .setOldpropertyid(propertyDetail.get(0).getOldPropertyId());
                                        responsePerConnection.setWard(propertyDetail.get(0).getWardNumber());
                                        responsePerConnection.setDdnNo(propertyDetail.get(0).getDdnNo());
                                        responsePerConnection.setLegacyHoldingNo(propertyDetail.get(0).getLegacyHoldingNo());
                                    }

                                }
                                return responsePerConnection;
                            }).collect(Collectors.toList());
                    propertyResponse.addAll(tempResponse);
                }
                
                count = count - limit;
                offset += limit;
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

                    String tenantIdStyled = row.getTenantId().replace("od.", "");
                    tenantIdStyled = tenantIdStyled.substring(0, 1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
                    
                    PropertyWiseDemandResponse tempResponse = PropertyWiseDemandResponse.builder()
                            .propertyId(row.getConsumerCode())
                            .taxperiodfrom(String.valueOf(row.getTaxPeriodFrom()))
                            .taxperiodto(String.valueOf(row.getTaxPeriodTo())).ulb(tenantIdStyled)
                            .build();

                    List<DemandDetail> tempDemandDetail = row.getDemandDetails();
                    BigDecimal taxAmount = BigDecimal.ZERO;
                    BigDecimal collectionAmount = BigDecimal.ZERO;
                    BigDecimal advanceTaxAmount = BigDecimal.ZERO;
                    BigDecimal advanceCollectionAmount = BigDecimal.ZERO;
                    tempResponse.setTaxamount(taxAmount.toString());
                    tempResponse.setCollectionamount(collectionAmount.toString());
                    tempResponse.setAdvanceDemandAmount(advanceTaxAmount.toString());
                    tempResponse.setAdvanceCollectedAmount(advanceCollectionAmount.toString());
                    tempDemandDetail.stream().forEach(demandUnit -> {
                    	
                    	
                    	
                    	if(TAXHEADS_FOR_ADVANCE.contains(demandUnit.getTaxHeadMasterCode())) {
                            if (demandUnit.getTaxAmount() != null) {
                                tempResponse.setAdvanceDemandAmount(new BigDecimal(tempResponse.getAdvanceDemandAmount())
                                        .add(demandUnit.getTaxAmount()).toString());
                            }
                            if (demandUnit.getCollectionAmount() != null) {
                                tempResponse.setAdvanceCollectedAmount(new BigDecimal(tempResponse.getAdvanceCollectedAmount())
                                        .add(demandUnit.getCollectionAmount()).toString());
                            }
                        	}
                    	
                    	
                    	
                    	if(!TAXHEADS_NOT_ALLOWED_FOR_DEMAND_REPORT.contains(demandUnit.getTaxHeadMasterCode())) {
                        if (demandUnit.getTaxAmount() != null) {
                            tempResponse.setTaxamount(new BigDecimal(tempResponse.getTaxamount())
                                    .add(demandUnit.getTaxAmount()).toString());
                        }
                        if (demandUnit.getCollectionAmount() != null) {
                            tempResponse.setCollectionamount(new BigDecimal(tempResponse.getCollectionamount())
                                    .add(demandUnit.getCollectionAmount()).toString());
                        }
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
                                    if(StringUtils.hasText(prop.getDdnNo())){
                                        item.setDdnNo(prop.getDdnNo());
                                    }
                                    if(StringUtils.hasText(prop.getLegacyHoldingNo())){
                                        item.setLegacyHoldingNo(prop.getLegacyHoldingNo());
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

        List<PropertyWiseCollectionResponse> propertyWiseCollectionResponses = new ArrayList<PropertyWiseCollectionResponse>();// take

        // Search property by criteria
        Long count = pdRepository.getPropertiesDetailCount(searchCriteria);
        Integer limit = configuration.getReportLimit();
        Integer offset = 0;

        if (count > 0) {
            while (count > 0) {
                searchCriteria.setLimit(limit);
                searchCriteria.setOffset(offset);
                log.info(" Fetching Property Details ");
                List<PropertyDetailsResponse> properties = pdRepository.getPropertiesDetail(searchCriteria);
                log.info("Property Details Fetched ");


                Map<String, List<PropertyDetailsResponse>> propertyMap = properties.stream()
                        .collect(Collectors.groupingBy(
                                PropertyDetailsResponse::getPropertyId,
                                Collectors.mapping(
                                        Function.identity(), Collectors.toList())));

                if (!CollectionUtils.isEmpty(properties)) {

                    // getting payments
                    Set<String> propertyIds = properties.stream().map(item -> item.getPropertyId())
                            .collect(Collectors.toSet());
                    Set<String> businessService = Stream.of("PT").collect(Collectors.toSet());
                    PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder()
                            .businessServices(businessService)
                            .tenantId(searchCriteria.getUlbName())
                            .consumerCodes(propertyIds)
                            .build();
                    List<Payment> payments = paymentService.getPayments(requestInfo, paymentSearchCriteria);

                    if (payments != null) {
                        List<PropertyWiseCollectionResponse> tempCollectionResponses = payments.parallelStream()
                                .map(payment -> {
                                    return enrichPayments(payment);
                                }).collect(Collectors.toList());

                        // setting property data
                        log.info("Setting Property Details ");
                        enrichPropertyDetail(propertyMap, tempCollectionResponses);

                        // Getting user data
                        log.info("Fetching User Details ");
                        List<String> userIds = properties.stream().map(property -> property.getUuid()).distinct()
                                .collect(Collectors.toList());
                        enrichUserDetails(requestInfo, tempCollectionResponses, userIds);

                        //setting Temporary Response to Final Response
                        propertyWiseCollectionResponses.addAll(tempCollectionResponses);

                    }

                }
                count = count - limit;
                offset += limit;
            }

        }

        return propertyWiseCollectionResponses;
    }

    private void enrichUserDetails(RequestInfo requestInfo,
            List<PropertyWiseCollectionResponse> tempCollectionResponses, List<String> userIds) {
        org.egov.report.user.UserSearchCriteria userSearchCriteria = org.egov.report.user.UserSearchCriteria
                .builder()
                .uuid(userIds)
                .active(true)
                .type(UserType.CITIZEN)
                .build();
        List<org.egov.report.user.User> usersInfo = userService.searchUsers(userSearchCriteria,
                requestInfo);
        log.info("User Details Fetched ");
        Map<String, org.egov.report.user.User> userMap = usersInfo.stream()
                .collect(Collectors.toMap(org.egov.report.user.User::getUuid, Function.identity()));
        log.info("Setting User Details ");
        
        tempCollectionResponses.stream().forEach(collectionResponse -> {
            if (collectionResponse != null) {
                collectionResponse.getUuid().parallelStream().forEach(uuid -> {
                    org.egov.report.user.User user = userMap.get(uuid);
                    if (user != null) {
                        if (StringUtils.hasText(collectionResponse.getMobilenumber())
                                && StringUtils.hasText(user.getMobileNumber())) {
                            collectionResponse.setMobilenumber(collectionResponse.getMobilenumber()
                                    + " , " + user.getMobileNumber());
                        } else {
                            if (StringUtils.hasText(user.getMobileNumber())) {
                                collectionResponse.setMobilenumber(user.getMobileNumber());
                            }
                        }

                        if (StringUtils.hasText(collectionResponse.getName())
                                && StringUtils.hasText(user.getName())) {
                            collectionResponse
                                    .setName(collectionResponse.getName() + " , " + user.getName());
                        } else {
                            if (StringUtils.hasText(user.getName())) {
                                collectionResponse.setName(user.getName());
                            }
                        }
                    }
                });
            }
        });
    }

    private PropertyWiseCollectionResponse enrichPayments(Payment payment) {
        PropertyWiseCollectionResponse propertyWiseCollectionResponse = new PropertyWiseCollectionResponse();

        if (payment != null) {

            BigDecimal due = payment.getTotalDue();
            BigDecimal amountPaid = payment.getTotalAmountPaid();
            BigDecimal currentDueInitial = BigDecimal.ZERO;
            BigDecimal currentDueWithTotalDue = currentDueInitial.add(due);
            BigDecimal finalCurrentDue = currentDueWithTotalDue.subtract(amountPaid);

            propertyWiseCollectionResponse.setConsumercode(
                    payment.getPaymentDetails().get(0).getBill().getConsumerCode());
            propertyWiseCollectionResponse.setDue(payment.getTotalDue().toString());
            propertyWiseCollectionResponse
                    .setAmountpaid(payment.getTotalAmountPaid().toString());
            propertyWiseCollectionResponse.setCurrentdue(finalCurrentDue.toString());
            propertyWiseCollectionResponse.setReceiptdate(
                    payment.getPaymentDetails().get(0).getReceiptDate().toString());
            propertyWiseCollectionResponse.setReceiptnumber(
                    payment.getPaymentDetails().get(0).getReceiptNumber());
            propertyWiseCollectionResponse.setPaymentMode(payment.getPaymentMode().name());
        }
        return propertyWiseCollectionResponse;
    }

    private void enrichPropertyDetail(Map<String, List<PropertyDetailsResponse>> propertyMap,
            List<PropertyWiseCollectionResponse> tempCollectionResponses) {
        tempCollectionResponses.parallelStream().forEach(response -> {
//                            PropertyDetailsResponse propertyDetail = propertyMap.get(response.getConsumercode());
            List<PropertyDetailsResponse> propertyDetail = propertyMap.get(response.getConsumercode());
            if (propertyDetail != null) {
                response.setOldpropertyid(propertyDetail.get(0).getOldPropertyId());
                response.setDdnNo(propertyDetail.get(0).getDdnNo());
                response.setLegacyHoldingNo(propertyDetail.get(0).getLegacyHoldingNo());
                response.setWard(propertyDetail.get(0).getWardNumber());
                List<String> uuids = propertyDetail.parallelStream()
                        .map(propertyUser -> propertyUser.getUuid()).collect(Collectors.toList());
                response.setUuid(uuids);// stream and set in list
            }
        });
    }


	public void generatePTAssessmentReport(@Valid RequestInfoWrapper requestInfoWrapper,
			@Valid PTAssessmentSearchCriteria ptAssessmentSearchCriteria) {

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		String reportType = ReportConstants.PTASSESSMENT_REPORT_STRING;

		prValidator.validatePTAssessmentReport(ptAssessmentSearchCriteria);

		List<String> tenantIds = ptAssessmentSearchCriteria.getTenantIds();

		for (String tenantId : tenantIds) {

			String financialYear = ptAssessmentSearchCriteria.getFinancialYear();

			List<UtilityReportDetails> reportList = new ArrayList<>();
			
//			if(prValidator.validateRecentReport(reportList, tenantId)) {
//				log.info("Skipping... Report already present for tenantid: "+ tenantId);
//				continue;
//			}
			
			if (CollectionUtils.isEmpty(reportList)) {
				UtilityReportDetails reportDetails = enrichmentService.enrichSaveReport(requestInfo, reportType,
						financialYear, tenantId);
				dcbRepository.saveReportDetails(new UtilityReportRequest(requestInfo, reportDetails));
				reportList.add(reportDetails);
			}

			try {	
			
			List<Map<String, Object>> assessmentsDetailsList = pdRepository.createPTAssessmentReport(ptAssessmentSearchCriteria,tenantId);
			log.info("Total Assessments Fetched : " + assessmentsDetailsList.size());
			
	        Set<String> userIds = assessmentsDetailsList.stream()
	                .map(map -> (String) map.get("name"))
	                .filter(Objects::nonNull)
	                .collect(Collectors.toSet());
			
			
			// Path and filename for the excel file
			String fileName = generateFileName("PTAssessment_Report", tenantId, financialYear);
			File temporaryfile = getTemporaryFile("PTAssessment_Report", fileName);

	    	PTAssessmentsReportExcelGenerator generator = new PTAssessmentsReportExcelGenerator(assessmentsDetailsList);
	    	
	    	int limit = configuration.getReportLimit();
	    	int count = assessmentsDetailsList.size();
	        Map<String, User> userMap = new HashMap<>();

            // get User details here
	        if (count > 0) {
	            while (count > 0) {
            UserSearchCriteria usCriteria = UserSearchCriteria.builder().uuid(userIds)
                    .active(true)
                    .userType(UserSearchCriteria.CITIZEN)
                    .tenantId(tenantId)//is it required
                    .build();

            List<OwnerInfo> usersInfo = userService.getUserDetails(requestInfo, usCriteria);
            // Update global userMap
            userMap.putAll(usersInfo.stream()
                    .collect(Collectors.toMap(User::getUuid, Function.identity())));

            count = count - limit;
        }
    }
            
	        assessmentsDetailsList.forEach(assessment -> {
	            Object userId = assessment.get("name"); // Assuming the map has a key 'userId' to match with userMap
	            if (userId != null && userMap.containsKey(userId.toString())) {
	                User user = userMap.get(userId.toString());
	                assessment.put("name", user.getName()); // Assuming User has a getName() method
	            }
	        });
            
			try {
				generator.generateExcelFile(temporaryfile);
			} catch (IOException e) {
				log.info("Error Occured while writing into excel file...");
				e.printStackTrace();
			}

			// push temporary file into file store
			log.info("uploading file to filestore...");
			Object filestoreDetails = fileStoreService.upload(temporaryfile, fileName, MediaType.MULTIPART_FORM_DATA_VALUE,
					"PT", "od");
			log.info("Uploaded the file to filestore with details: " + filestoreDetails.toString());

			// enrich and update Utility Report Details
			UtilityReportRequest reportDetailsRequest = enrichmentService.enrichUpdateReport(requestInfo, reportList,
					fileName, filestoreDetails);
			dcbRepository.updateReportDetails(reportDetailsRequest);

			// user info not decrypted and enrichment giving null pointer
			}catch (Exception e) {
				e.printStackTrace();
			}

			
	}
		
	}
		
		private String generateFileName(String fileInitialName, String tenantId, String financialYear) {
			String fileFormat = ".xlsx";
			String fileSeparator = "_";
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hhmmss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			String currentDateTime = dateFormat.format(new Date());
			String ulbName = StringUtils.capitalize(tenantId.substring(3));
			return fileInitialName + fileSeparator + ulbName + fileSeparator + financialYear + fileSeparator + currentDateTime + fileFormat;
		}
		
		private File getTemporaryFile(String reportType, String fileName) {
			//Temp file location
			File currentDirFile = new File(configuration.getReportTemporaryLocation());
			String currentPath = currentDirFile.getAbsolutePath();
			String absolutePath = currentPath + File.separator + reportType;
			log.info("Temporary storage Path : " + absolutePath);
			File directory = new File(absolutePath);
			if (directory.exists()) {
				try {
					FileUtils.deleteDirectory(directory);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			directory.mkdir();
			File temporaryfile = new File(absolutePath, fileName);
			
			if (!temporaryfile.exists()) {
				try {
					temporaryfile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return temporaryfile;
		}


		public Map<String, Object> getPTAssessmentsReporteport(RequestInfo requestInfo,
				@Valid UtilityReportSearchCriteria searchCriteria) {
			Map<String, Object> response = new HashMap<>();
			
			String reportType = ReportConstants.PTASSESSMENT_REPORT_STRING;

			String tenantId = searchCriteria.getTenant();
			
			String financialYear = searchCriteria.getFinancialYear();

			List<UtilityReportDetails> reportList = dcbRepository.isReportExist(reportType, financialYear, tenantId);

			prValidator.validateIfReportGenerationInProcess(reportList, tenantId);
			
			if(reportList.isEmpty()) {
				throw new CustomException("REPORT_NOT_GENERATED",
						"Report is not generated for mentioned criteria. Kindly generate the report first ..");
			}
			
			List<UtilityReportDetails> reportDetails = reportList.stream()
					.filter(item -> item.getTenantId().equalsIgnoreCase(tenantId)).collect(Collectors.toList());

			response.put("reportsDetails", reportDetails.get(0));
			
			return response;
		}

		
		

}