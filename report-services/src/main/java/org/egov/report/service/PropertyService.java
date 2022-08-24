package org.egov.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

		Set<String> uuIds = new HashSet<>();
		UserSearchCriteria usCriteria = UserSearchCriteria.builder().build();

		if (!CollectionUtils.isEmpty(propDetlResponse)) {
			propDetlResponse.forEach(res -> uuIds.add((res.getUserId())));

			usCriteria.setUuid(uuIds);

			List<OwnerInfo> info = userService.getUserDetails(requestInfo, usCriteria);
			for (PropertyDetailsResponse res : propDetlResponse) {
				info.forEach(item -> {
					if (res.getUserId().equalsIgnoreCase(item.getUuid())) {
						res.setMobileNumber(item.getMobileNumber());
						res.setName(item.getName());
					}
				});
			}
		}

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
			UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder()
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
//			Set<String> Prop = Propertys.stream().distinct().collect(Collectors.toSet());

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

		List<ULBWiseTaxCollectionResponse> propResponse = new ArrayList<ULBWiseTaxCollectionResponse>();

		Map<String, List<PropertyDemandResponse>> propDemResponse = pdRepository
				.getPropertyDemandDetails(searchCriteria);

		if (!CollectionUtils.isEmpty(propDemResponse)) {

			propDemResponse.forEach((key, value) -> {
				ULBWiseTaxCollectionResponse pr = new ULBWiseTaxCollectionResponse();
				BigDecimal currentYearDemandAmount = BigDecimal.ZERO;
				BigDecimal totalArrearDemandAmount = BigDecimal.ZERO;
				BigDecimal totalCollectionAmount = BigDecimal.ZERO;
				BigDecimal dueAmount = BigDecimal.ZERO;

				value.forEach(item -> {
					if (item.getTaxperiodto() < System.currentTimeMillis()) {
						totalArrearDemandAmount.add(item.getTaxamount());
					}

					else {
						currentYearDemandAmount.add(item.getTaxamount());
					}

					totalCollectionAmount.add(item.getCollectionamount());

				});

				BigDecimal dueAmountWithTotalArrear = dueAmount.add(totalArrearDemandAmount);
				BigDecimal dueAmountWithCurrentYearDemand = dueAmountWithTotalArrear.add(currentYearDemandAmount);
				BigDecimal dueAmountFinal = dueAmountWithCurrentYearDemand.subtract(totalCollectionAmount);

				pr.setPropertyId(key);
				pr.setTotaltaxamount(currentYearDemandAmount.toString());
				pr.setTotalarreartaxamount(totalArrearDemandAmount.toString());
				pr.setTotalcollectionamount(totalCollectionAmount.toString());
				pr.setDueamount(dueAmountFinal.toString());
				pr.setUlb(value.get(0).getTenantid());
				pr.setOldpropertyid(null);
				pr.setWard(null);

				propResponse.add(pr);
			});

			Set<String> Propertys = new HashSet<>();
//			 PropertySearchingCriteria pdsCriteria = new PropertySearchingCriteria();
			propResponse.forEach(res -> Propertys.add((res.getPropertyId())));

			PropertySearchingCriteria propsCriteria = PropertySearchingCriteria.builder()
					.tenantid(searchCriteria.getUlbName()).property(Propertys).build();

			List<Property> propinfo = getProperty(requestInfo, propsCriteria);
			for (ULBWiseTaxCollectionResponse res : propResponse) {
				propinfo.forEach(item -> {
					if (res.getPropertyId().equalsIgnoreCase(item.getPropertyId())) {
						res.setOldpropertyid(item.getOldPropertyId());
						res.setWard(item.getWard());
					}
				});
			}

			if (searchCriteria.getWardNo() != null) {
				for (int i = 0; i < propResponse.size(); i++) {

					if (propResponse.get(i).getWard() != searchCriteria.getWardNo()) {
						propResponse.remove(propResponse.get(i));
						i--;

					}

				}
			}

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

			if (searchCriteria.getPropertyId() != null) {
				propDemResponse.keySet().removeIf(k -> !(k.equals(searchCriteria.getPropertyId())));
			}

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
					pr.setName(null);
					pr.setMobilenumber(null);
					pr.setOldpropertyid(null);
					pr.setWard(null);

					propResponse.add(pr);

				});
			});

			Set<String> Propertys = new HashSet<>();
			propResponse.forEach(res -> Propertys.add((res.getPropertyId())));

			PropertySearchingCriteria propsCriteria = PropertySearchingCriteria.builder()
					.tenantid(searchCriteria.getUlbName()).property(Propertys).build();

			List<Property> propinfo = getProperty(requestInfo, propsCriteria);
			for (PropertyWiseDemandResponse res : propResponse) {
				propinfo.forEach(item -> {
					if (res.getPropertyId().equalsIgnoreCase(item.getPropertyId())) {
						res.setOldpropertyid(item.getOldPropertyId());
						res.setWard(item.getWard());
					}
				});
			}

		}

		List<PropertyDetailsResponse> propDetlResponse = pdRepository.getPropertyDetails(searchCriteria);

		// Extracting user info from userService

		Set<String> uuIds = new HashSet<>();
		UserSearchCriteria usCriteria = UserSearchCriteria.builder().build();

		if (!CollectionUtils.isEmpty(propDetlResponse)) {
			for (int i = 0; i < propDetlResponse.size(); i++) {
				boolean flag = false;
				for (int j = 0; j < propResponse.size(); j++) {
					if (propDetlResponse.get(i).getPropertyId() == propResponse.get(j).getPropertyId()) {
						flag = true;
						break;
					}

				}
				if (flag == false) {
					propDetlResponse.remove(i);
					i--;
				}
			}
			propDetlResponse.forEach(res -> uuIds.add((res.getUserId())));

			usCriteria.setUuid(uuIds);

			List<OwnerInfo> info = userService.getUserDetails(requestInfo, usCriteria);
			for (PropertyDetailsResponse res : propDetlResponse) {
				info.forEach(item -> {
					if (res.getUserId().equalsIgnoreCase(item.getUuid())) {
						res.setMobileNumber(item.getMobileNumber());
						res.setName(item.getName());
					}
				});
			}

			for (PropertyWiseDemandResponse res : propResponse) {
				propDetlResponse.forEach(item -> {
					if (res.getPropertyId().equalsIgnoreCase(item.getPropertyId())) {
						res.setMobilenumber(item.getMobileNumber());
						res.setName(item.getName());
					}
				});
			}

		}

		if (searchCriteria.getWardNo() != null) {
			for (int i = 0; i < propResponse.size(); i++) {
				if (propResponse.get(i).getWard() != null) {
					if (!propResponse.get(i).getWard().equals(searchCriteria.getWardNo())) {
						propResponse.remove(i);
						i--;
					}
				}

			}
		}
		if (searchCriteria.getOldPropertyId() != null) {
			for (int i = 0; i < propResponse.size(); i++) {
				if (propResponse.get(i).getOldpropertyid() != null) {
					if (!propResponse.get(i).getOldpropertyid().equals(searchCriteria.getOldPropertyId())) {
						propResponse.remove(i);
						i--;
					}
				}

			}
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
						res.setWard(item.getWard());
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
