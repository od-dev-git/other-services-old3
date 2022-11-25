package org.egov.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.BillAccountDetail;
import org.egov.report.model.BillDetail;
import org.egov.report.model.Demand;
import org.egov.report.model.DemandDetail;
import org.egov.report.model.DemandDetails;
import org.egov.report.model.Payment;
import org.egov.report.model.PaymentSearchCriteria;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.model.WSConnection;
import org.egov.report.model.WSConnectionRequest;
import org.egov.report.model.WSSearchCriteria;
import org.egov.report.model.enums.UserType;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.repository.WSReportRepository;
import org.egov.report.util.PaymentUtil;
import org.egov.report.util.ReportConstants;
import org.egov.report.util.WSReportUtils;
import org.egov.report.validator.WSReportValidator;
import org.egov.report.web.model.BillSummaryResponses;
import org.egov.report.web.model.ConsumerBillHistoryResponse;
import org.egov.report.web.model.ConsumerMasterWSReportResponse;
import org.egov.report.web.model.ConsumerPaymentHistoryResponse;
import org.egov.report.web.model.DemandCriteria;
import org.egov.report.web.model.EmployeeDateWiseWSCollectionResponse;
import org.egov.report.web.model.EmployeeWiseWSCollectionResponse;
import org.egov.report.web.model.MonthWisePendingBillGenerationResponse;
import org.egov.report.web.model.OwnerInfo;
import org.egov.report.web.model.ULBWiseWaterConnectionDetails;
import org.egov.report.web.model.User;
import org.egov.report.web.model.WSConsumerHistoryResponse;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.egov.report.web.model.WaterConnectionDetailResponse;
import org.egov.report.web.model.WaterConnectionDetails;
import org.egov.report.web.model.WaterDemandResponse;
import org.egov.report.web.model.WaterMonthlyDemandResponse;
import org.egov.report.web.model.WaterNewConsumerMonthlyResponse;
import org.egov.report.web.model.WsSchedulerBasedDemandsGenerationReponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WaterService {
	
	@Autowired
	private WSReportValidator wsValidator;
	
	@Autowired
    private ServiceRepository repository;
	
	@Autowired
	private WSReportRepository wsRepository;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WSReportRepository reportRepository;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ReportServiceConfiguration configuration;
	
	@Autowired
	private PaymentUtil paymentUtil;
	
    @Autowired
    private WSReportUtils wsReportUtils;

    @Autowired
    private DemandService demandService;

	public List<EmployeeDateWiseWSCollectionResponse> employeeDateWiseWSCollection(RequestInfo requestInfo, WSReportSearchCriteria searchCriteria) {
		
		wsValidator.validateEmployeeDateWiseWSCollectionReport(searchCriteria);
		
		PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder()
				.businessServices(Stream.of("WS","WS.ONE_TIME_FEE").collect(Collectors.toSet()))
				.tenantId(searchCriteria.getTenantId())
				.fromDate(searchCriteria.getCollectionDate())
				.toDate(searchCriteria.getCollectionDate()).build();
		
		if(StringUtils.hasText(searchCriteria.getPaymentMode())) {
			paymentSearchCriteria.setPaymentModes(Stream.of(searchCriteria.getPaymentMode().split(",")).collect(Collectors.toSet()));
		}
		
		List<Payment> payments = paymentService.getPayments(requestInfo, paymentSearchCriteria);
		if(payments.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<EmployeeDateWiseWSCollectionResponse> response = payments.stream()
				.map(payment -> EmployeeDateWiseWSCollectionResponse.builder()
						.employeeId(payment.getAuditDetails().getCreatedBy())
						.tenantId(payment.getTenantId()).ulb(payment.getTenantId().split("\\.")[1].toUpperCase())
						.transactionDate(payment.getTransactionDate()).paymentMode(payment.getPaymentMode().toString())
						.consumerCode(payment.getPaymentDetails().get(0).getBill().getConsumerCode())
						.receiptNo(payment.getPaymentDetails().get(0).getReceiptNumber())
						.businessService(payment.getPaymentDetails().get(0).getBusinessService())
						.head("Water")
						.collectedAmount(payment.getTotalAmountPaid()).build())
				.collect(Collectors.toList());
		
		List<Long> userIds = response.stream().map(item -> Long.valueOf(item.getEmployeeId())).distinct().collect(Collectors.toList());
		List<OwnerInfo> usersInfo = userService.getUser(requestInfo, userIds);
		Map<Long, OwnerInfo> userMap = usersInfo.stream().collect(Collectors.toMap(OwnerInfo::getId, Function.identity()));
		
		response.stream().forEach(item -> {
			OwnerInfo user = userMap.get(Long.valueOf(item.getEmployeeId()));
			if(user!=null) {
				item.setEmployeeId(user.getUserName());
				item.setEmployeeName(user.getName());
			}
		});
		
		return response;
	
	}
	
	
public List<BillSummaryResponses> billSummary(RequestInfo requestInfo, WSReportSearchCriteria searchCriteria) {
		
	wsValidator.validateBillSummary(searchCriteria);
	 List<BillSummaryResponses> billSummResponses = new ArrayList<>();
	 List<BillSummaryResponses> response = wsRepository.getBillSummaryDetails(searchCriteria);
	 Map<Object,Long> billSummResponse = response.stream().collect(Collectors.groupingBy(e->Pair.of(e.getUlb(), e.getMonthYear()),Collectors.counting()));
	
	 billSummResponse.forEach((key, value)->{ 		
   	  
		 String ulb = key.toString().split(",")[0];
		 String monthYear = key.toString().split(",")[1];
         BillSummaryResponses res = new BillSummaryResponses();
         res.setCount(value.intValue());
         res.setUlb(ulb.substring(4,ulb.length()));
         res.setMonthYear(monthYear.substring(0, monthYear.length()-1));
         
         billSummResponses.add(res);
         });
	 return billSummResponses;
	}
	
    public List<ConsumerMasterWSReportResponse> consumerMasterWSReport(RequestInfo requestInfo,
            WSReportSearchCriteria criteria) {

        // validate the search criteria
        wsValidator.validateconsumerMasterWSReport(criteria);

        Long count = reportRepository.getConsumerMasterReportCount(criteria);
        Integer limit = configuration.getReportLimit();

        Integer offset = 0;
        List<ConsumerMasterWSReportResponse> consumerMasterResponse = new ArrayList();
        List<ConsumerMasterWSReportResponse> response = new ArrayList();
        if (count > 0) {
            while (count > 0) {
                criteria.setLimit(limit);
                criteria.setOffset(offset);
                response = reportRepository.getComsumerMasterWSReport(requestInfo, criteria);
                count = count - limit;
                offset += limit;

                // Extracting user info
				if (!CollectionUtils.isEmpty(response)) {
					List<String> userIds = response.stream().map(item -> item.getUserId()).distinct()
							.collect(Collectors.toList());

					org.egov.report.user.UserSearchCriteria userSearchCriteria = org.egov.report.user.UserSearchCriteria
							.builder().uuid(userIds).build();

					List<org.egov.report.user.User> usersInfo = userService.searchUsers(userSearchCriteria,
							requestInfo);
					Map<String, org.egov.report.user.User> userMap = usersInfo.stream()
							.collect(Collectors.toMap(org.egov.report.user.User::getUuid, Function.identity()));

					response.stream().forEach(item -> {
						org.egov.report.user.User user = userMap.get(item.getUserId());
						if (user != null) {
							item.setUserName(user.getName());
							item.setUserMobile(user.getMobileNumber());
							item.setUserAddress(user.getCorrespondenceAddress().getAddress());
						}
					});
				}
				consumerMasterResponse.addAll(response);
			}  
        }
        return consumerMasterResponse;
    }
	
	public List<ConsumerPaymentHistoryResponse> consumerPaymentHistory(RequestInfo requestInfo,WSReportSearchCriteria criteria){
		
		wsValidator.validateconsumerPaymentHistoryReport(criteria);
		
		WSSearchCriteria wsSearchCriteria = WSSearchCriteria.builder().consumerNo(criteria.getConsumerCode())
				.tenantId(criteria.getTenantId()).searchType("CONNECTION").build();
		
		List<WSConnection> connections = getWaterConnection(requestInfo, wsSearchCriteria);
		
		PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder()
				.businessServices(Stream.of("WS","WS.ONE_TIME_FEE").collect(Collectors.toSet()))
				.tenantId(criteria.getTenantId())
				.consumerCodes(Stream.of(criteria.getConsumerCode()).collect(Collectors.toSet())).build();
		
		List<Payment> payments = paymentService.getPayments(requestInfo, paymentSearchCriteria);
		if(payments.isEmpty()) {
			return Collections.emptyList();
		}
		
		
		List<ConsumerPaymentHistoryResponse> response = payments.stream()
				.map(payment -> ConsumerPaymentHistoryResponse.builder()
						.tenantId(payment.getTenantId()).ulb(payment.getTenantId().split("\\.")[1].toUpperCase())
						.consumerAddress(payment.getPayerAddress()).conumerName(payment.getPayerName())
						.employeeId(payment.getAuditDetails().getCreatedBy())
						.transactionDate(payment.getTransactionDate()).paymentMode(payment.getPaymentMode().toString())
						.consumerCode(payment.getPaymentDetails().get(0).getBill().getConsumerCode()).head("WATER")
						.monthYear(paymentUtil.getMonthYear(payment.getTransactionDate()))
						.paidAmount(payment.getTotalAmountPaid()).transactionId(payment.getTransactionNumber())
						.conumerName(connections.get(0).getConnectionHolders().get(0).get("name").toString())
						.consumerAddress(connections.get(0).getConnectionHolders().get(0).get("correspondenceAddress").toString())
						.ward(connections.get(0).getAdditionalDetails().get("ward").toString())
						.build())
				.collect(Collectors.toList());
		
		List<Long> userIds = response.stream().map(item -> Long.valueOf(item.getEmployeeId())).distinct().collect(Collectors.toList());
		List<OwnerInfo> usersInfo = userService.getUser(requestInfo, userIds);
		Map<Long, OwnerInfo> userMap = usersInfo.stream().collect(Collectors.toMap(OwnerInfo::getId, Function.identity()));
		
		response.stream().forEach(item -> {
			OwnerInfo user = userMap.get(Long.valueOf(item.getEmployeeId()));
			if(user!=null) {
				item.setEmployeeId(user.getUserName());
				item.setEmployeeName(user.getName());
			}
		});		
		
		return response;
	}
	
	public List<WSConnection> getWaterConnection(RequestInfo requestInfo, WSSearchCriteria criteria){
		
		StringBuilder uri = new StringBuilder(configuration.getWsHost())
				.append(configuration.getWsSearchEndpoint()).append("?")
				.append("tenantId="+criteria.getTenantId()).append("&")
				.append("searchType="+criteria.getSearchType()).append("&")
				.append("connectionNumber="+criteria.getConsumerNo());
		
		List<WSConnection> wsConnections = new ArrayList<>();
		WSConnectionRequest request = WSConnectionRequest.builder().requestInfo(requestInfo)
				.build();
			
		try {
		Object response = repository.fetchResult(uri, request);
		WaterConnectionDetailResponse res = mapper.convertValue(response, WaterConnectionDetailResponse.class);
		wsConnections.addAll(res.getConnections());
		}catch(Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("WS_CONNECTION FETCH ERROR", "Unable to fetch WS Connection Information");
		}
		
		return wsConnections;	
	}
	
	public List<WaterNewConsumerMonthlyResponse> waterNewConsumerMonthlyReport(RequestInfo requestInfo,WSReportSearchCriteria criteria){
		
		wsValidator.validateWaterNewConsumerMonthlyReport(criteria);
		
		List<WaterNewConsumerMonthlyResponse> response = reportRepository.getWaterNewConsumerMonthlyReport(criteria);
		
		if(!CollectionUtils.isEmpty(response)) {
		Set<String> userIds = response.stream().map(item -> item.getUserId()).distinct().collect(Collectors.toSet());
		UserSearchCriteria usCriteria = UserSearchCriteria.builder().uuid(userIds)
				.active(true)
				.userType(UserSearchCriteria.CITIZEN)
				.tenantId(criteria.getTenantId())
				.build();
		List<OwnerInfo> usersInfo = userService.getUserDetails(requestInfo, usCriteria);
		Map<String, User> userMap = usersInfo.stream().collect(Collectors.toMap(User::getUuid, Function.identity()));
		
		response.stream().forEach(item -> {
			User user = userMap.get(item.getUserId());
			if(user!=null) {
				item.setMobile(user.getMobileNumber());
				item.setUserName(user.getName());
				item.setUserAddress(user.getCorrespondenceAddress());
			}
		});	
		}
		
		return response;
	}
	
	public List<ConsumerBillHistoryResponse> consumerBillHistoryReport(RequestInfo requestInfo, WSReportSearchCriteria criteria){
		
		wsValidator.validateConsumerBillHistoryReport(criteria);
		
		return reportRepository.getConsumerBillHistoryReport(criteria);
		
	}
	
    public List<WaterMonthlyDemandResponse> waterMonthlyDemandReport(RequestInfo requestInfo,
            WSReportSearchCriteria searchCriteria) {

        wsValidator.validateWaterMonthlyDemandReport(searchCriteria);
        Long count = reportRepository.getWaterConnectionCount(searchCriteria);
        log.info("No of Water Connetcions : " + count.toString());
        Integer limit = configuration.getReportLimit();
        Integer offset = 0;
        
        List<WaterMonthlyDemandResponse> finalResponse = new ArrayList<>();
        
        if (count > 0) {
            while (count > 0) {
                searchCriteria.setLimit(limit);
                searchCriteria.setOffset(offset);
                log.info("Water Search Criteria : " + searchCriteria.toString());
                Set<String>  waterConnectionsSet = reportRepository.getWaterConnection(searchCriteria).stream().collect(Collectors.toSet());
                log.info("Water Connetcions : " + waterConnectionsSet.toString());

                Long taxperiodToDate = enrichToDate(searchCriteria.getToDate());
                searchCriteria.setToDate(taxperiodToDate);
                searchCriteria.setConsumerNumbers(waterConnectionsSet);
                
                // get Water Connection details here

                Map<String, WaterConnectionDetails> waterConnectionDetails = reportRepository.getWaterConnectionDetails(searchCriteria);//make a new query

                // get Demands
                DemandCriteria demandCriteria = DemandCriteria.builder().tenantId(searchCriteria.getTenantId())
                        .consumerCode(waterConnectionsSet).periodTo(taxperiodToDate).build();
                log.info(" Demands Criteria " + demandCriteria.toString());
                List<Demand> allDemands = demandService.getDemands(demandCriteria, requestInfo);
                log.info(" Demands Count fetched from DB " + allDemands.size());;
                Map<Object, List<Demand>> demandsGroupedByConnectionNo = allDemands.stream()
                        .collect(Collectors.groupingBy(Demand::getConsumerCode));
                log.info("No of demands Grouped By Connection No : " + demandsGroupedByConnectionNo.size());


                // get User details here
                Set<String> userIds = demandsGroupedByConnectionNo.entrySet().parallelStream()
                        .map(entry -> entry.getValue().get(0).getPayer().getUuid())
                        .distinct().collect(Collectors.toSet());
                UserSearchCriteria usCriteria = UserSearchCriteria.builder().uuid(userIds)
                        .active(true)
                        .userType(UserSearchCriteria.CITIZEN)
                        .tenantId(searchCriteria.getTenantId())//is it required
                        .build();

                List<OwnerInfo> usersInfo = userService.getUserDetails(requestInfo, usCriteria);
                Map<String, User> userMap = usersInfo.stream()
                        .collect(Collectors.toMap(User::getUuid, Function.identity()));



                if (!CollectionUtils.isEmpty(demandsGroupedByConnectionNo)) {//use Collect To List
                    List<WaterMonthlyDemandResponse> tempResponse = demandsGroupedByConnectionNo.entrySet().parallelStream().map(connection ->{
                        

                        WaterMonthlyDemandResponse responsePerConnection = new WaterMonthlyDemandResponse();
                        String connectionNo = String.valueOf(connection.getKey());
                        List<Demand> connectionDemands = connection.getValue();

                        if(connectionDemands != null) {

                            Collections.sort(connectionDemands,Comparator.comparing(e -> e.getTaxPeriodTo(),(s1,s2)->{
                                return s2.compareTo(s1);
                            }));

                            List<DemandDetail> currentDemandDetails = connectionDemands.get(0).getDemandDetails(); 
                            if(currentDemandDetails != null) {

                                currentDemandDetails.parallelStream().forEach(demandDetail -> {
                                    if (demandDetail.getTaxHeadMasterCode().equalsIgnoreCase("WS_CHARGE")) {
                                        responsePerConnection.setCurrentDemandAmt(responsePerConnection.getCurrentDemandAmt().add(demandDetail.getTaxAmount()));
                                        responsePerConnection.setCollectedAmt(responsePerConnection.getCollectedAmt().add(demandDetail.getCollectionAmount()));
                                    }
                                    if (demandDetail.getTaxHeadMasterCode().equalsIgnoreCase("SW_CHARGE")) {
                                        responsePerConnection.setSewageCurrentDemandAmount(responsePerConnection.getSewageCurrentDemandAmount().add(demandDetail.getTaxAmount()));
                                        responsePerConnection.setSewageCollectionAmount(responsePerConnection.getSewageCollectionAmount().add(demandDetail.getCollectionAmount()));
                                    }
                                    if (demandDetail.getTaxHeadMasterCode().equalsIgnoreCase("WS_TIME_REBATE") || demandDetail.getTaxHeadMasterCode().equalsIgnoreCase("WS_ANNUAL_PAYMENT_REBATE") || demandDetail.getTaxHeadMasterCode().equalsIgnoreCase("WS_SPECIAL_REBATE") || demandDetail.getTaxHeadMasterCode().equalsIgnoreCase("SW_SPECIAL_REBATE"))
                                        responsePerConnection.setRebateAmt((responsePerConnection.getRebateAmt().add(demandDetail.getTaxAmount())).subtract(demandDetail.getCollectionAmount()));
                                    if (demandDetail.getTaxHeadMasterCode().equalsIgnoreCase("WS_ADVANCE_CARRYFORWARD") || demandDetail.getTaxHeadMasterCode().equalsIgnoreCase("SW_ADVANCE_CARRYFORWARD"))
                                        responsePerConnection.setAdvanceAmt((responsePerConnection.getAdvanceAmt().add(demandDetail.getTaxAmount())).subtract(demandDetail.getCollectionAmount()));
                                    if (demandDetail.getTaxHeadMasterCode().equalsIgnoreCase("WS_TIME_PENALTY"))
                                        responsePerConnection.setPenaltyAmt((responsePerConnection.getPenaltyAmt().add(demandDetail.getTaxAmount())).subtract(demandDetail.getCollectionAmount()));
                                });
                            }
                            responsePerConnection.setTaxPriodFrom(wsReportUtils.getConvertedDate(connectionDemands.get(0).getTaxPeriodFrom()));
                            responsePerConnection.setTaxPeriodTo(wsReportUtils.getConvertedDate(connectionDemands.get(0).getTaxPeriodTo()));
                            String tenantId = connectionDemands.get(0).getTenantId();
                            String tenantIdStyled = tenantId.replace("od.", "");
                            tenantIdStyled = tenantIdStyled.substring(0,1).toUpperCase() + tenantIdStyled.substring(1).toLowerCase();
                            responsePerConnection.setUlb(tenantIdStyled);
                            responsePerConnection.setTenantId(tenantIdStyled);

                            BigDecimal totalArrearAmt = connectionDemands.stream().skip(1)
                                    .flatMap(item -> item.getDemandDetails().stream())
                                    .map(w -> w.getTaxAmount().subtract(w.getCollectionAmount()))
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);


                            BigDecimal taxAmt = responsePerConnection.getCurrentDemandAmt();
                            BigDecimal collectedAmt = responsePerConnection.getCollectedAmt();
                            BigDecimal penaltyAmt = responsePerConnection.getPenaltyAmt();
                            BigDecimal advanceAmt = responsePerConnection.getAdvanceAmt();
                            BigDecimal rebateAmt = responsePerConnection.getRebateAmt();


                            
                            responsePerConnection.setArrearAmt(totalArrearAmt);
                            responsePerConnection.setPayableWithPenaltyAmt((wsReportUtils.CalculateAmtAfterDueDateModified(taxAmt, collectedAmt,
                                    penaltyAmt, advanceAmt, totalArrearAmt)));
                            responsePerConnection.setPayableAfterRebateAmt(wsReportUtils.CalculateAmtBeforeDueDateModified(taxAmt, collectedAmt,
                                    penaltyAmt, advanceAmt, totalArrearAmt, rebateAmt));
                            responsePerConnection.setTotalDueAmt(wsReportUtils.calculateTotalDueModified(taxAmt, collectedAmt, penaltyAmt,
                                    advanceAmt, totalArrearAmt));
                            
                            
                            // set Water Connection details here
                            log.info("set Water Connection details here");
                            WaterConnectionDetails connectionDetails = waterConnectionDetails.get(connectionNo);
                            if(connectionDetails != null) {
                                responsePerConnection.setConnectionType(connectionDetails.getConnectiontype());
                                responsePerConnection.setWard(connectionDetails.getWard());
                                responsePerConnection.setConnectionNo(connectionNo); 
                                responsePerConnection.setOldconnectionno(connectionDetails.getOldconnectionno());
                            }

                            // set User details here
                            log.info("setting User Details");
                            User user = userMap.get(connectionDemands.get(0).getPayer().getUuid());
                            if(user!=null) {
                                responsePerConnection.setMobile(user.getMobileNumber());
                                responsePerConnection.setConnectionHolderName(user.getName());
                                responsePerConnection.setAddrss(user.getCorrespondenceAddress());
                            }
                            
                               
                        }
                        return responsePerConnection; 
                    }).collect(Collectors.toList());
                    finalResponse.addAll(tempResponse);
                }
               
                count = count - limit;
                offset += limit;
            }
        }

        return finalResponse;

    }

	public List<WSConsumerHistoryResponse> wsConsumerHistoryReport(RequestInfo requestInfo, WSReportSearchCriteria criteria) {
		
		List<WSConsumerHistoryResponse> response = new ArrayList<>();
		
		wsValidator.validateWsConsumerHistoryReport(criteria);
		
		Map<String, WaterConnectionDetails> responseConnection = reportRepository.getWaterMonthlyDemandConnection(criteria);
		
		if(!CollectionUtils.isEmpty(responseConnection)) {
		
			PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder()
					.businessServices(Stream.of("WS").collect(Collectors.toSet()))
					.tenantId(criteria.getTenantId())
					.fromDate(criteria.getFromDate())
					.toDate(criteria.getToDate())
					.consumerCodes(responseConnection.keySet()).build();
			
			List<Payment> payments = paymentService.getPayments(requestInfo, paymentSearchCriteria);
			
			Comparator<BillDetail> comparator = (obj1, obj2) -> obj2.getFromPeriod().compareTo(obj1.getFromPeriod());
			
			String consumerCode = responseConnection.keySet().stream().findFirst().get();
			
			payments.forEach(item -> 
			{
				WSConsumerHistoryResponse res = WSConsumerHistoryResponse.builder()
						.paymentMode(item.getPaymentMode().toString())
						.paymentDate(wsReportUtils.getConvertedDate(item.getTransactionDate()))
						.totalDue(item.getTotalDue())
						.collectionAmt(item.getTotalAmountPaid())
						.consumerNo(consumerCode)
						.connectionType(responseConnection.get(consumerCode).getConnectiontype())
						.oldConnectionNo(responseConnection.get(consumerCode).getOldconnectionno())
						.ulb(responseConnection.get(consumerCode).getTenantid().substring(3))
						.ward(responseConnection.get(consumerCode).getWard())
						.month(wsReportUtils.getConvertedDate(item.getTransactionDate()).substring(3,5))
						.build();
				
				item.getPaymentDetails().forEach(paymentDetail -> {
					
					res.setReceiptNo(paymentDetail.getReceiptNumber());
					
					List<BillDetail> billDetails = paymentDetail.getBill().getBillDetails();
					Collections.sort(billDetails ,comparator);
					
					Long fromDateCurrentDemand = billDetails.get(0).getFromPeriod();
					Long toDateCurrentDemand = billDetails.get(0).getToPeriod();
					
					billDetails.forEach(billDetail -> 
					{
						if(billDetail.getToPeriod() == toDateCurrentDemand && billDetail.getFromPeriod() == fromDateCurrentDemand) {
							
							List<BillAccountDetail> billAccountDetails = billDetail.getBillAccountDetails();
							billAccountDetails.forEach(billAccountDetail -> {
								
								if(billAccountDetail.getTaxHeadCode().equalsIgnoreCase("SW_ADVANCE_CARRYFORWARD")) {
									res.setAdvance(billAccountDetail.getAmount());
								}
								if(billAccountDetail.getTaxHeadCode().equalsIgnoreCase("WS_TIME_REBATE")) {
									res.setRebateAmt(billAccountDetail.getAmount());
								}
								if(billAccountDetail.getTaxHeadCode().equalsIgnoreCase("WS_TIME_PENALTY")) {
									res.setPenalty(billAccountDetail.getAmount());
								}
								if(billAccountDetail.getTaxHeadCode().equalsIgnoreCase("WS_CHARGE")) {
									res.setCurrentDemand(billAccountDetail.getAmount());
								}
								
							});
						}
						else {
							
							res.setArrear(billDetail.getAmount());
						}
					});
				});
				
				response.add(res);
			});
		}
		
		return response;
	}


	public List<EmployeeWiseWSCollectionResponse> employeeWiseWSCollection(RequestInfo requestInfo,
			WSReportSearchCriteria searchCriteria) {
		
		wsValidator.validateEmployeeWiseCollectionReport(searchCriteria);
		
		List<EmployeeWiseWSCollectionResponse> response = wsRepository.getEmployeeWiseCollectionReport(searchCriteria);
		
		if(!CollectionUtils.isEmpty(response)) {
		List<Long> userIds = response.stream().map(item -> Long.valueOf(item.getEmployeeId())).distinct().collect(Collectors.toList());
		List<OwnerInfo> usersInfo = userService.getUser(requestInfo, userIds);
		Map<Long, OwnerInfo> userMap = usersInfo.stream().collect(Collectors.toMap(OwnerInfo::getId, Function.identity()));
		
		response.stream().forEach(item -> {
			OwnerInfo user = userMap.get(Long.valueOf(item.getEmployeeId()));
			if(user!=null) {
				item.setEmployeeId(user.getUserName());
				item.setEmployeeName(user.getName());
			}
		});
		}
		
		return response;
	}
	
	public List<ULBWiseWaterConnectionDetails> getNoOfWSConnectionsElegibleForDemand(RequestInfo requestInfo,
			WSReportSearchCriteria searchCriteria) {

		wsValidator.validateWSConnectionElegibleForDemand(searchCriteria);

		List<ULBWiseWaterConnectionDetails> response = reportRepository.getNoOfWSDemandConnections(requestInfo,searchCriteria);


		return response;
	}


    public List<MonthWisePendingBillGenerationResponse> monthWisePendingBillGeneration(RequestInfo requestInfo,
            WSReportSearchCriteria searchCriteria) {

        
        wsValidator.validateMonthWisePendingBillGeneration(searchCriteria);
        searchCriteria.setConnectionType(ReportConstants.NON_METERED);

        Long count = reportRepository.getWaterConnectionsCount(searchCriteria);
        Integer limit = configuration.getReportLimit();
        Integer offset = 0;

        List<MonthWisePendingBillGenerationResponse> responseList = new ArrayList<>();
        Map<String, WaterConnectionDetails> connectionResponse = new HashMap<>();

        
        if (count > 0) {
            while (count > 0) {
                searchCriteria.setLimit(limit);
                searchCriteria.setOffset(offset);
                Map<String, WaterConnectionDetails> response = reportRepository.getWaterConnections(searchCriteria);
                connectionResponse.putAll(response);
                count = count - limit;
                offset += limit;
            }
        }

                if (!CollectionUtils.isEmpty(connectionResponse)) {

                    count = reportRepository.getDemandsCount(searchCriteria);
                    List<String> demandResponses = new ArrayList<>();

                    limit = configuration.getReportLimit();
                    offset = 0;
                    if (count > 0) {
                        while (count > 0) {
                            searchCriteria.setLimit(limit);
                            searchCriteria.setOffset(offset);
                            List<String> responses = reportRepository.getDemands(searchCriteria);
                            demandResponses.addAll(responses);
                            count = count - limit;
                            offset += limit;
                        }
                    }

                    responseList = connectionResponse.entrySet().parallelStream()
                            .filter(wcd -> !demandResponses.contains(wcd.getKey()))
                            .map(item -> MonthWisePendingBillGenerationResponse.builder()
                                    .consumerCode(item.getKey())
                                    .ulb(item.getValue().getTenantid().substring(3))
                                    .build())
                            .collect(Collectors.toList());
                }
        return responseList;
    }
	
	public List<WsSchedulerBasedDemandsGenerationReponse> getSchedulerBasedDemands(RequestInfo requestInfo,
			WSReportSearchCriteria searchCriteria) {
		log.info("inside water service");
		log.info("searchCriteria: "+searchCriteria.toString());
		log.info("validating");
		wsValidator.validateSchedulerDemandGeneration(searchCriteria);
		log.info("validated");
		log.info("entering into query");
		Long count = reportRepository.getSchedulerBasedWSDemandCount(requestInfo, searchCriteria);
		Integer limit = configuration.getReportLimit();

		Integer offset =  0;
		List<WsSchedulerBasedDemandsGenerationReponse> response = new ArrayList();
		if(count > 0) {
			while(count > 0) {	
			    searchCriteria.setLimit(limit);
			    searchCriteria.setOffset(offset);
				response.addAll(reportRepository.getSchedulerBasedWSDemands(requestInfo,searchCriteria));
				count = count - limit;
				offset += limit;
			}
		}
		log.info("back from query");
		log.info("response: "+response);
		
		return response;
	}
	
	   public Long enrichToDate(Long toDate) {
	        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
	        cal.setTimeInMillis(toDate);
	        cal.set(Calendar.HOUR, 11);
	        cal.set(Calendar.MINUTE, 59);
	        cal.set(Calendar.SECOND, 59);
	        cal.set( Calendar.AM_PM, Calendar.PM);
	        return cal.getTimeInMillis();
	    }
		
	
}
