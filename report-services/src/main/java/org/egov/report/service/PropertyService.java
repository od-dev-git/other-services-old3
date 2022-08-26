package org.egov.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
		// TODO Auto-generated method stub

		prValidator.validatePropertyDetailsSearchCriteria(searchCriteria);

		List<PropertyDetailsResponse> propDetlResponse = pdRepository.getPropertyDetails(searchCriteria);

		// Extracting user info from userService

//		Set<String> uuIds = new HashSet<>();
//		UserSearchCriteria usCriteria = UserSearchCriteria.builder().tenantId(searchCriteria.getUlbName())
//				.active(true).userType(UserSearchCriteria.CITIZEN).build();
//
//		if (!CollectionUtils.isEmpty(propDetlResponse)) {
//			propDetlResponse.forEach(res -> uuIds.add((res.getUuid())));
//
//			usCriteria.setUuid(uuIds);
//
//			List<OwnerInfo> info = userService.getUserDetails(requestInfo, usCriteria);
//			for (PropertyDetailsResponse res : propDetlResponse) {
//				info.forEach(item -> {
//					if (res.getUuid().equalsIgnoreCase(item.getUuid())) {
//						res.setMobileNumber(item.getMobileNumber());
//						res.setName(item.getName());
//					}
//				});
//			}
//		}

		
		// Extracting user info from userService

				Set<String> userIds = propDetlResponse.stream().map(item -> item.getUuid()).distinct().collect(Collectors.toSet());
				UserSearchCriteria usCriteria = UserSearchCriteria.builder().uuid(userIds)
						.active(true)
						.userType(UserSearchCriteria.CITIZEN)
						.tenantId(searchCriteria.getUlbName())
						.build();
				List<OwnerInfo> usersInfo = userService.getUserDetails(requestInfo, usCriteria);
				Map<String, User> userMap = usersInfo.stream().collect(Collectors.toMap(User::getUuid, Function.identity()));
				propDetlResponse.stream().forEach(item -> {
					User user = userMap.get(item.getUuid());
					if(user!=null) {
						item.setMobileNumber(user.getMobileNumber());
						item.setName(user.getName());
					}
				});
			
		return propDetlResponse;
	}

	public List<TaxCollectorWiseCollectionResponse> getTaxCollectorWiseCollections(RequestInfo requestInfo,
			PropertyDetailsSearchCriteria searchCriteria) {

prValidator.validatetcwcSearchCriteria(searchCriteria);

		Set<String> s = new HashSet<String>();
		s.add("PT");
		PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder().businessServices(s)
				.tenantId(searchCriteria.getUlbName()).fromDate(searchCriteria.getStartDate())
				.toDate(searchCriteria.getEndDate()).build();

		List<Payment> payments = paymentService.getPayments(requestInfo, paymentSearchCriteria);
		List<TaxCollectorWiseCollectionResponse> tcwcResponse = new ArrayList<TaxCollectorWiseCollectionResponse>();

		for (Payment res : payments) {

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

		if (!CollectionUtils.isEmpty(tcwcResponse)) {
			tcwcResponse.forEach(res -> uids.add(Long.valueOf(res.getUserid())));
			usCriteria.setId(uids);
			tcwcResponse.forEach(res -> Propertys.add((res.getConsumercode())));
			pdsCriteria.setProperty(Propertys);

			List<Long> ids = uids.stream().distinct().collect(Collectors.toList());
			UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder().tenantId(searchCriteria.getUlbName())
					.userType(UserSearchCriteria.EMPLOYEE).active(true)
					.id(ids).build();
			List<OwnerInfo> info = userService.getUserDetails(requestInfo, userSearchCriteria);
			for (TaxCollectorWiseCollectionResponse res : tcwcResponse) {
				info.forEach(item -> {
					if (res.getUserid().equalsIgnoreCase(item.getId().toString())) {
						res.setMobilenumber(item.getMobileNumber());
						res.setName(item.getName());
						res.setEmployeeid(item.getUserName());
						res.setType(item.getType());
					}
				});
			}

			tcwcResponse.forEach(res -> uids.add(Long.valueOf(res.getUserid())));
			usCriteria.setId(uids);

			PropertySearchingCriteria propsCriteria = PropertySearchingCriteria.builder()
					.tenantid(searchCriteria.getUlbName()).property(Propertys).build();

			List<Property> propinfo = getProperty(requestInfo, propsCriteria);
			for (TaxCollectorWiseCollectionResponse res : tcwcResponse) {
				propinfo.forEach(item -> {
					if (res.getConsumercode().equalsIgnoreCase(item.getPropertyId())) {
						res.setOldpropertyid(item.getOldPropertyId());
					}
				});
			}

		}

		return tcwcResponse;
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

		log.info( "getting Property Demand Details");
		List<ULBWiseTaxCollectionResponse> propResponse = new ArrayList<ULBWiseTaxCollectionResponse>();

		
		Map<String, List<PropertyDemandResponse>> propDemResponse = pdRepository
				.getPropertyDemandDetails(searchCriteria);
		log.info( "got Property Demand Details");
		if (!CollectionUtils.isEmpty(propDemResponse)) {

			propDemResponse.forEach((key, value) -> {
				ULBWiseTaxCollectionResponse pr = new ULBWiseTaxCollectionResponse();

				value.forEach(item -> {
					if (item.getTaxperiodto() < System.currentTimeMillis()) { 
						
						pr.setTotalarreartaxamount((pr.getTotalarreartaxamount()).add(item.getTaxamount()));
					}

					else {
						pr.setTotaltaxamount((pr.getTotaltaxamount()).add(item.getTaxamount()));
					}

					pr.setTotalcollectionamount((pr.getTotalcollectionamount()).add(item.getCollectionamount()));

				});

				BigDecimal dueAmountWithTotalArrear = pr.getDueamount().add(pr.getTotalarreartaxamount());
				BigDecimal dueAmountWithCurrentYearDemand = dueAmountWithTotalArrear.add(pr.getTotaltaxamount());
				BigDecimal dueAmountFinal = dueAmountWithCurrentYearDemand.subtract(pr.getTotalcollectionamount());

				pr.setPropertyId(key);
				pr.setDueamount(dueAmountFinal);
				pr.setUlb(value.get(0).getTenantid());
				pr.setOldpropertyid(value.get(0).getOldpropertyid());
				pr.setWard(value.get(0).getWard());

				propResponse.add(pr);
			});


		}
		return propResponse;
	}

	public List<PropertyWiseDemandResponse> getpropertyWiseDemandReport(RequestInfo requestInfo,
			PropertyDetailsSearchCriteria searchCriteria) {

		prValidator.validatePropertyDetailsSearchCriteria(searchCriteria);

		List<PropertyWiseDemandResponse> propResponse = new ArrayList<PropertyWiseDemandResponse>();

		Map<String, List<PropertyDemandResponse>> propDemResponse = pdRepository
				.getPropertyWiseDemandDetails(searchCriteria);

		if (!CollectionUtils.isEmpty(propDemResponse)) {

			propDemResponse.forEach((key, value) -> {
				value.forEach(item -> {
					PropertyWiseDemandResponse pr = new PropertyWiseDemandResponse();
					BigDecimal currentYearDemandAmount = BigDecimal.ZERO;
					BigDecimal totalCollectionAmount = BigDecimal.ZERO;
					BigDecimal dueAmount = BigDecimal.ZERO;

					if (item.getTaxamount() == null) {
						pr.setTaxamount(currentYearDemandAmount.toString());
					} else {
						currentYearDemandAmount = item.getTaxamount();
						pr.setTaxamount(currentYearDemandAmount.toString());
					}

					if (item.getCollectionamount() == null) {
						pr.setCollectionamount(totalCollectionAmount.toString());
					} else {
						totalCollectionAmount = item.getCollectionamount();
						pr.setCollectionamount(totalCollectionAmount.toString());
					}
					BigDecimal dueAmountWithCurrentYearDemandAmount = dueAmount.add(currentYearDemandAmount);
					BigDecimal dueAmountFinal = dueAmountWithCurrentYearDemandAmount.subtract(totalCollectionAmount);
					pr.setDueamount(dueAmountFinal.toString());
					pr.setPropertyId(key);
					pr.setTaxperiodfrom(item.getTaxperiodfrom().toString());
					pr.setTaxperiodto(item.getTaxperiodto().toString());
					pr.setUlb(item.getTenantid());
					pr.setUuid(item.getUuid());
					pr.setName(null);
					pr.setMobilenumber(null);
					pr.setOldpropertyid(item.getOldpropertyid());
					pr.setWard(item.getWard());

					propResponse.add(pr);

				});
			});


			// Extracting user info from userService

			Set<String> userIds = propResponse.stream().map(item -> item.getUuid()).distinct().collect(Collectors.toSet());
			UserSearchCriteria usCriteria = UserSearchCriteria.builder().uuid(userIds)
					.active(true)
					.userType(UserSearchCriteria.CITIZEN)
					.tenantId(searchCriteria.getUlbName())
					.build();
			List<OwnerInfo> usersInfo = userService.getUserDetails(requestInfo, usCriteria);
			Map<String, User> userMap = usersInfo.stream().collect(Collectors.toMap(User::getUuid, Function.identity()));

			propResponse.stream().forEach(item -> {
				User user = userMap.get(item.getUuid());
				if(user!=null) {
					item.setMobilenumber(user.getMobileNumber());
					item.setName(user.getName());
				}
			});
		}
		
		return propResponse;
	}

	public List<PropertyWiseCollectionResponse> getpropertyCollectionReport(RequestInfo requestInfo,
			PropertyDetailsSearchCriteria searchCriteria) {

		prValidator.validatepwcSearchCriteria(searchCriteria);

		Set<String> s = new HashSet<String>();
		s.add("PT");
		Set<String> p = new HashSet<String>();
		if (searchCriteria.getPropertyId() == null) {
			p = null;
		} else {
			p.add(searchCriteria.getPropertyId());
		}
		PaymentSearchCriteria paymentSearchCriteria = PaymentSearchCriteria.builder().businessServices(s)
				.tenantId(searchCriteria.getUlbName()).consumerCodes(p).build();

		List<Payment> payments = paymentService.getPayments(requestInfo, paymentSearchCriteria);
		List<PropertyWiseCollectionResponse> pwcResponse = new ArrayList<PropertyWiseCollectionResponse>();

		for (Payment res : payments) {

			PropertyWiseCollectionResponse pwcr = new PropertyWiseCollectionResponse();

			pwcr.setConsumercode(res.getPaymentDetails().get(0).getBill().getConsumerCode());
			pwcr.setDue(res.getTotalDue().toString());
			pwcr.setAmountpaid(res.getTotalAmountPaid().toString());
			BigDecimal due = res.getTotalDue();
			BigDecimal amountPaid = res.getTotalAmountPaid();
			BigDecimal currentDueInitial = BigDecimal.ZERO;
			BigDecimal currentDueWithTotalDue = currentDueInitial.add(due);
			BigDecimal finalCurrentDue = currentDueWithTotalDue.subtract(amountPaid);
			pwcr.setCurrentdue(finalCurrentDue.toString());
			pwcr.setReceiptdate(res.getPaymentDetails().get(0).getReceiptDate().toString());
			pwcr.setReceiptnumber(res.getPaymentDetails().get(0).getReceiptNumber());
			pwcr.setPaymentMode(res.getPaymentMode().name());

			pwcResponse.add(pwcr);

		}

		Set<String> Propertys = new HashSet<>();
		PropertySearchingCriteria pdsCriteria = new PropertySearchingCriteria();

		if (!CollectionUtils.isEmpty(pwcResponse)) {
			pwcResponse.forEach(res -> Propertys.add((res.getConsumercode())));
			pdsCriteria.setProperty(Propertys);

			PropertySearchingCriteria propsCriteria = PropertySearchingCriteria.builder()
					.tenantid(searchCriteria.getUlbName()).property(Propertys).build();

			List<Property> propinfo = getProperty(requestInfo, propsCriteria);
			for (PropertyWiseCollectionResponse res : pwcResponse) {
				propinfo.forEach(item -> {
					if (res.getConsumercode().equalsIgnoreCase(item.getPropertyId())) {
						res.setOldpropertyid(item.getOldPropertyId());
						res.setWard(item.getAddress().getWard());
						res.setName(item.getOwners().get(0).getName());
						res.setMobilenumber(item.getOwners().get(0).getMobileNumber());

					}
				});
			}

		}

		if (searchCriteria.getWardNo() != null) {
			for (int i = 0; i < pwcResponse.size(); i++) {
				if (pwcResponse.get(i).getWard() != null) {
					if (!pwcResponse.get(i).getWard().equals(searchCriteria.getWardNo())) {
						pwcResponse.remove(i);
						i--;
					}
				}

			}
		}

		if (searchCriteria.getOldPropertyId() != null) {
			for (int i = 0; i < pwcResponse.size(); i++) {
				if (pwcResponse.get(i).getOldpropertyid() != null) {
					if (!pwcResponse.get(i).getOldpropertyid().equals(searchCriteria.getOldPropertyId())) {
						pwcResponse.remove(i);
						i--;
					}
				}

			}
		}

		return pwcResponse;
	}

}
