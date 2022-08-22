package org.egov.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.egov.common.contract.request.RequestInfo;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.DemandDetails;
import org.egov.report.model.Payment;
import org.egov.report.model.PaymentSearchCriteria;
import org.egov.report.model.UserSearchCriteria;
import org.egov.report.model.UserSearchRequest;
import org.egov.report.model.WSConnection;
import org.egov.report.model.WSConnectionRequest;
import org.egov.report.model.WSSearchCriteria;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.repository.WSReportRepository;
import org.egov.report.util.PaymentUtil;
import org.egov.report.util.WSReportUtils;
import org.egov.report.validator.WSReportValidator;
import org.egov.report.web.model.BillSummaryResponses;
import org.egov.report.web.model.ConsumerBillHistoryResponse;
import org.egov.report.web.model.ConsumerMasterWSReportResponse;
import org.egov.report.web.model.ConsumerPaymentHistoryResponse;
import org.egov.report.web.model.EmployeeDateWiseWSCollectionResponse;
import org.egov.report.web.model.OwnerInfo;
import org.egov.report.web.model.User;
import org.egov.report.web.model.UserDetailResponse;
import org.egov.report.web.model.UserResponse;
import org.egov.report.web.model.WSReportSearchCriteria;
import org.egov.report.web.model.WaterConnectionDetailResponse;
import org.egov.report.web.model.WaterConnectionDetails;
import org.egov.report.web.model.WaterDemandResponse;
import org.egov.report.web.model.WaterMonthlyDemandResponse;
import org.egov.report.web.model.WaterNewConsumerMonthlyResponse;
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
	
	 
	    HashMap<String, Integer> hash = new LinkedHashMap<>();
		 wsValidator.validateBillSummary(searchCriteria);
		 List<BillSummaryResponses> response = wsRepository.getBillSummaryDetails(searchCriteria);
		 List<BillSummaryResponses> billSummResponse = new ArrayList<>();
		 
		 if(!response.isEmpty()) {
		 String myResponseMonth = response.get(0).getMonthYear();
		 for(BillSummaryResponses res: response) {
			 if(hash.containsKey(res.getUlb()))
			 {
				 Integer count = hash.get(res.getUlb());
				 hash.put(res.getUlb(),count+1);
			 }
			 else
			 {
			 hash.put(res.getUlb(), 1);
			 }
			
		 }
		 
		 
		 hash.forEach((key, value)->
		           {
			          BillSummaryResponses res = new BillSummaryResponses();
			          res.setCount(value);
			          res.setUlb(key);
			          res.setMonthYear(myResponseMonth);
			          
			          billSummResponse.add(res);
		           }
				 );
		 
		 }
		 return billSummResponse;
	}
	
	public List<ConsumerMasterWSReportResponse> consumerMasterWSReport(RequestInfo requestInfo, WSReportSearchCriteria criteria) {
		
		List<Long> userIds = new ArrayList<>();
		
		//validate the search criteria
		wsValidator.validateconsumerMasterWSReport(criteria);
		
		 
		List<ConsumerMasterWSReportResponse> response = reportRepository.getComsumerMasterWSReport(requestInfo,criteria);
		
		//Extracting user info from userService
		if(!CollectionUtils.isEmpty(response)) {
			response.forEach(res -> userIds.add(res.getUserId()));
			
			 List<User> info = userService.getUserDetails(requestInfo, userIds);
			
			for(ConsumerMasterWSReportResponse res : response) {
				
				info.forEach(
						item -> {
							if(res.getUserId() == item.getId()) {
								res.setUserMobile(item.getMobileNumber());
								res.setUserName(item.getName());
								res.setUserAddress(item.getCorrespondenceAddress());
								res.setUserId(null);
							}
						});
				}
			}
		return response;
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
		UserSearchCriteria usCriteria = UserSearchCriteria.builder().uuId(userIds).build();
		List<User> usersInfo = userService.getUserDetails(requestInfo, usCriteria);
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
	
	public List<WaterMonthlyDemandResponse> waterMonthlyDemandReport(RequestInfo requestInfo, WSReportSearchCriteria criteria) {

		List<WaterMonthlyDemandResponse> response = new ArrayList<>();

		wsValidator.validateWaterMonthlyDemandReport(criteria);

		//get demand details response here
		Map<String, List<WaterDemandResponse>> demandResponse = reportRepository.getWaterMonthlyDemandReport(criteria);

		if(!CollectionUtils.isEmpty(demandResponse)) {
		List<DemandDetails> demandDetails = new ArrayList<>();

		//calculate current demand details and arrears here
		demandResponse.forEach((key,value)->
					{
						DemandDetails details = new DemandDetails();
						details.setConsumerCode(key);

						Comparator<WaterDemandResponse> comparator = (obj1, obj2) -> obj2.getTaxperiodfrom().compareTo(obj1.getTaxperiodfrom());
						Collections.sort(value, comparator);

						Long fromDateCurrentDemand = value.get(0).getTaxperiodfrom();
						Long toDateCurrentDemand = value.get(0).getTaxperiodto();

						value.forEach(item -> 
						{	
							if(item.getTaxperiodto() >= fromDateCurrentDemand && item.getTaxperiodto() <= toDateCurrentDemand) {

								details.setDemandPeriodFrom(wsReportUtils.getConvertedDate(item.getTaxperiodfrom()));
								details.setDemandPeriodTo(wsReportUtils.getConvertedDate(item.getTaxperiodto()));
								if(item.getTaxheadcode().equalsIgnoreCase("WS_CHARGE")) {
									details.setTaxAmount(details.getTaxAmount().add(item.getTaxamount()));
									details.setCollectedAmt(details.getCollectedAmt().add(item.getCollectionamount()));
								}
								if(item.getTaxheadcode().equalsIgnoreCase("WS_TIME_REBATE"))
									details.setRebateAmount(details.getRebateAmount().add(item.getTaxamount()));
								if(item.getTaxheadcode().equalsIgnoreCase("WS_ADVANCE_CARRYFORWARD"))
									details.setAdvance(details.getAdvance().add(item.getTaxamount()));	
								if(item.getTaxheadcode().equalsIgnoreCase("WS_TIME_PENALTY"))
									details.setPenaltyAmount(details.getPenaltyAmount().add(item.getTaxamount()));	
							}
							else {
								details.setArrears(details.getArrears().add(item.getTaxamount()));
								details.setArrearsCollectedAmount(details.getArrearsCollectedAmount().add(item.getCollectionamount()));
							}
						});
						BigDecimal taxAmt = details.getTaxAmount();
						BigDecimal collectedAmt = details.getCollectedAmt();
						BigDecimal penaltyAmt = details.getPenaltyAmount();
						BigDecimal advanceAmt = details.getAdvance();
						BigDecimal arrears = details.getArrears();
						BigDecimal rebateAmt = details.getRebateAmount();
						BigDecimal arrearsCollectedAmt = details.getArrearsCollectedAmount();
						details.setAmountAfterDueDate(wsReportUtils.CalculateAmtAfterDueDate(taxAmt, collectedAmt, penaltyAmt, advanceAmt, arrears, arrearsCollectedAmt));
						details.setAmountBeforeDueDate(wsReportUtils.CalculateAmtBeforeDueDate(taxAmt, collectedAmt, penaltyAmt, advanceAmt, arrears, rebateAmt, arrearsCollectedAmt));
						details.setTotalDue(wsReportUtils.calculateTotalDue(taxAmt, collectedAmt, penaltyAmt, advanceAmt, arrears, arrearsCollectedAmt));
						demandDetails.add(details);
					});

		//get Water Connection details here
		Map<String, WaterConnectionDetails> responseConnection = reportRepository.getWaterMonthlyDemandConnection(criteria);

		Set<String> userIds = responseConnection.values().stream().map(item -> item.getUserid()).distinct().collect(Collectors.toSet());
		UserSearchCriteria usCriteria = UserSearchCriteria.builder().uuId(userIds).build();
		List<User> usersInfo = userService.getUserDetails(requestInfo, usCriteria);
		Map<String, User> userMap = usersInfo.stream().collect(Collectors.toMap(User::getUuid, Function.identity()));

		responseConnection.values().stream().forEach(item -> {
			User user = userMap.get(item.getUserid());
			if(user!=null) {
				item.setMobile(user.getMobileNumber());
				item.setUserName(user.getName());
				item.setUserAddress(user.getCorrespondenceAddress());
			}
		});

		//map water connection and demand details here
		demandDetails.forEach(item -> {

			WaterConnectionDetails details = responseConnection.get(item.getConsumerCode());
			if(details != null) {
				WaterMonthlyDemandResponse res = new WaterMonthlyDemandResponse();
				res.setAdvanceAmt(item.getAdvance());
				res.setArrearAmt(item.getArrears());
				res.setCollectedAmt(item.getCollectedAmt());
				res.setConnectionNo(item.getConsumerCode());
				res.setConnectionType(details.getConnectiontype());
				res.setCurrentDemandAmt(item.getTaxAmount());
				res.setPenaltyAmt(item.getPenaltyAmount());
				res.setRebateAmt(item.getRebateAmount());
				res.setTaxPeriodTo(item.getDemandPeriodTo());
				res.setTaxPriodFrom(item.getDemandPeriodFrom());
				res.setTenantId(details.getTenantid());
				res.setWard(details.getWard());
				res.setConnectionHolderName(details.getUserName());
				res.setMobile(details.getMobile());
				res.setAddrss(details.getUserAddress());
				res.setPayableAfterRebateAmt(item.getAmountBeforeDueDate());
				res.setPayableWithPenaltyAmt(item.getAmountAfterDueDate());
				res.setTotalDueAmt(item.getTotalDue());
				res.setUlb(details.getTenantid().substring(3));
				response.add(res);
			}
		});
		}
		return response;
	}
		
	
}
