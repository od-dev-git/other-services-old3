package org.egov.dx.service;
import static org.egov.dx.util.PTServiceDXConstants.DIGILOCKER_DOCTYPE;
import static org.egov.dx.util.PTServiceDXConstants.DIGILOCKER_ISSUER_ID;
import static org.egov.dx.util.PTServiceDXConstants.DIGILOCKER_ORIGIN_NOT_SUPPORTED;
import static org.egov.dx.util.PTServiceDXConstants.EXCEPTION_TEXT_VALIDATION;
import static org.egov.dx.util.PTServiceDXConstants.ORIGIN;
import static org.egov.dx.util.PTServiceDXConstants.DIGILOCKER_DOCTYPE_MR_CERT;
import static org.egov.dx.util.PTServiceDXConstants.DIGILOCKER_DOCTYPE_NOT_SUPPORTED;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.dx.producer.DGLProducer;
import org.egov.dx.repository.Repository;
import org.egov.dx.util.Configurations;
import org.egov.dx.util.PTServiceDXConstants;
import org.egov.dx.web.models.Address;
import org.egov.dx.web.models.Certificate;
import org.egov.dx.web.models.CertificateData;
import org.egov.dx.web.models.CertificateForData;
import org.egov.dx.web.models.DGLModel;
import org.egov.dx.web.models.DGLRequest;
import org.egov.dx.web.models.DGLSearchCriteria;
import org.egov.dx.web.models.DocDetailsResponse;
import org.egov.dx.web.models.FileStoreUrlResponse;
import org.egov.dx.web.models.IssuedBy;
import org.egov.dx.web.models.IssuedTo;
import org.egov.dx.web.models.Organization;
import org.egov.dx.web.models.Payment;
import org.egov.dx.web.models.PaymentForReceipt;
import org.egov.dx.web.models.PaymentRequest;
import org.egov.dx.web.models.PaymentSearchCriteria;
import org.egov.dx.web.models.Person;
import org.egov.dx.web.models.PropertyTaxReceipt;
import org.egov.dx.web.models.PullDocResponse;
import org.egov.dx.web.models.PullURIResponse;
import org.egov.dx.web.models.RequestInfoWrapper;
import org.egov.dx.web.models.ResponseStatus;
import org.egov.dx.web.models.SearchCriteria;
import org.egov.dx.web.models.BPA.AddressBPA;
import org.egov.dx.web.models.BPA.Area;
import org.egov.dx.web.models.BPA.BPA;
import org.egov.dx.web.models.BPA.BPACertificate;
import org.egov.dx.web.models.BPA.BPACertificateData;
import org.egov.dx.web.models.BPA.BPAIssuedTo;
import org.egov.dx.web.models.BPA.BPASearchCriteria;
import org.egov.dx.web.models.BPA.BuildingPlanCertificate;
import org.egov.dx.web.models.BPA.ConstructionPermission;
import org.egov.dx.web.models.BPA.PersonBPA;
import org.egov.dx.web.models.BPA.Photo;
import org.egov.dx.web.models.MR.Husband;
import org.egov.dx.web.models.MR.HusbandResiding;
import org.egov.dx.web.models.MR.MRIssuedTo;
import org.egov.dx.web.models.MR.MRSearchCriteria;
import org.egov.dx.web.models.MR.MarriageCertificate;
import org.egov.dx.web.models.MR.MarriagePlace;
import org.egov.dx.web.models.MR.MarriageRegistration;
import org.egov.dx.web.models.MR.MrCertificate;
import org.egov.dx.web.models.MR.MrCertificateData;
import org.egov.dx.web.models.MR.PersonMR;
import org.egov.dx.web.models.MR.Registration;
import org.egov.dx.web.models.MR.Ward;
import org.egov.dx.web.models.MR.Wife;
import org.egov.dx.web.models.MR.WifeResiding;
import org.egov.dx.web.models.TL.BusinessProfession;
import org.egov.dx.web.models.TL.DocDetailsIssuedTo;
import org.egov.dx.web.models.TL.DocDetailsPerson;
import org.egov.dx.web.models.TL.Licensee;
import org.egov.dx.web.models.TL.OrganizationTL;
import org.egov.dx.web.models.TL.Pan;
import org.egov.dx.web.models.TL.PropertyDetails;
import org.egov.dx.web.models.TL.TLCertificate;
import org.egov.dx.web.models.TL.TLCertificateData;
import org.egov.dx.web.models.TL.TLIssuedTo;
import org.egov.dx.web.models.TL.TradeLicense;
import org.egov.dx.web.models.TL.TradeLicenseCertificate;
import org.egov.dx.web.models.TL.TradeLicenseSearchCriteria;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataExchangeService {

	@Autowired
    private PaymentService paymentService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private Configurations configurations;
	
	@Autowired
	private MrService mrService;
	
	@Autowired
	private TLService tlService;
	
	@Autowired
	private BPAService bpaService;
	
	@Autowired 
	private Repository repository;
	
	@Autowired
	private DGLProducer producer; 
	
	
	public String searchPullURIRequest(SearchCriteria  searchCriteria) throws IOException {
		
		if(searchCriteria.getOrigin().equals(ORIGIN))
		{
			return searchForDigiLockerURIRequest(searchCriteria, false);
		}
		return DIGILOCKER_ORIGIN_NOT_SUPPORTED;
	}
	
	public String searchPullDocRequest(SearchCriteria  searchCriteria) throws IOException {
		
		if(searchCriteria.getOrigin().equals(ORIGIN))
		{
			return searchForDigiLockerDocRequest(searchCriteria);
		}
		
		return DIGILOCKER_ORIGIN_NOT_SUPPORTED;
	}
	

	public String searchForDigiLockerURIRequest(SearchCriteria searchCriteria, Boolean isRequestForDoc)
			throws IOException {

		RequestInfo request = new RequestInfo();
		request.setApiId("Rainmaker");
		request.setMsgId("1670564653696|en_IN");
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();

		User userInfo = User.builder().uuid(configurations.getAuthTokenVariable()).type("EMPLOYEE")
				.roles(Collections.emptyList()).id(0L).tenantId("od.".concat(searchCriteria.getCity())).build();

		request = new RequestInfo("", "", 0L, "", "", "", "", "", "", userInfo);
		//request.setAuthToken("0d6cf270-b444-46ff-8723-efc0dca0fe15");
		// request.setUserInfo(userResponse.getUser());
		requestInfoWrapper.setRequestInfo(request);
		PullURIResponse model = new PullURIResponse();
		PullDocResponse modelDoc = new PullDocResponse();
		XStream xstream = new XStream();
		xstream.addPermission(NoTypePermission.NONE); // forbid everything
		xstream.addPermission(NullPermission.NULL); // allow "null"
		xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xstream.addPermission(AnyTypePermission.ANY);

		
		if(isRequestForDoc) {
			String responseHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			if (PTServiceDXConstants.DIGILOCKER_DOCTYPE.equals(searchCriteria.getDocType())) {
				return DIGILOCKER_DOCTYPE_NOT_SUPPORTED;
				//processPTPullUriRequest(searchCriteria, requestInfoWrapper, model, xstream);
			} else if (PTServiceDXConstants.DIGILOCKER_DOCTYPE_MR_CERT.equals(searchCriteria.getDocType())) {
				processMRPullUriRequest(searchCriteria, requestInfoWrapper, modelDoc, xstream);
			} else if (PTServiceDXConstants.DIGILOCKER_DOCTYPE_TL_CERT.equals(searchCriteria.getDocType())) {
				processTLPullUriRequest(searchCriteria, requestInfoWrapper, modelDoc, xstream);
			} else if (PTServiceDXConstants.DIGILOCKER_DOCTYPE_BPA_CERT.equals(searchCriteria.getDocType())) {
				processBPAPullUriRequest(searchCriteria, requestInfoWrapper, modelDoc, xstream);
			} else {
				return DIGILOCKER_DOCTYPE_NOT_SUPPORTED;
			}
			xstream.processAnnotations(PullDocResponse.class);
			xstream.toXML(modelDoc);
			return responseHeader+xstream.toXML(modelDoc);
		}else {
			String responseHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			if (PTServiceDXConstants.DIGILOCKER_DOCTYPE.equals(searchCriteria.getDocType())) {
				return DIGILOCKER_DOCTYPE_NOT_SUPPORTED;
				//processPTPullUriRequest(searchCriteria, requestInfoWrapper, model, xstream);
			} else if (PTServiceDXConstants.DIGILOCKER_DOCTYPE_MR_CERT.equals(searchCriteria.getDocType())) {
				processMRPullUriRequest(searchCriteria, requestInfoWrapper, model, xstream);
			} else if (PTServiceDXConstants.DIGILOCKER_DOCTYPE_TL_CERT.equals(searchCriteria.getDocType())) {
				processTLPullUriRequest(searchCriteria, requestInfoWrapper, model, xstream);
			} else if (PTServiceDXConstants.DIGILOCKER_DOCTYPE_BPA_CERT.equals(searchCriteria.getDocType())) {
				processBPAPullUriRequest(searchCriteria, requestInfoWrapper, model, xstream);
			} else {
				return DIGILOCKER_DOCTYPE_NOT_SUPPORTED;
			}
			xstream.processAnnotations(PullURIResponse.class);
			xstream.toXML(model);
			return responseHeader +xstream.toXML(model);
		}
		
		

	}
	
	private void processBPAPullUriRequest(SearchCriteria searchCriteria, RequestInfoWrapper requestInfoWrapper,
			Object model, XStream xstream) throws IOException {
		BPASearchCriteria criteria = new BPASearchCriteria();
		criteria.setTenantId("od."+searchCriteria.getCity());
		criteria.setApprovalNo(searchCriteria.getApprovalNumber());
		generateBPALetter(searchCriteria, criteria, requestInfoWrapper, model, xstream);	
	}

	private void processTLPullUriRequest(SearchCriteria searchCriteria, RequestInfoWrapper requestInfoWrapper,
			Object model, XStream xstream) throws MalformedURLException, IOException {
		TradeLicenseSearchCriteria criteria = new TradeLicenseSearchCriteria();
		criteria.setTenantId("od."+searchCriteria.getCity());
		criteria.setLicenseNumbers(Arrays.asList(searchCriteria.getLicenseNumber()));
		generateTlLicense(searchCriteria, criteria, requestInfoWrapper, model, xstream);
	}

	private void processMRPullUriRequest(SearchCriteria searchCriteria, RequestInfoWrapper requestInfoWrapper,
			Object model, XStream xstream) throws IOException, MalformedURLException {
		MRSearchCriteria criteria = new MRSearchCriteria();
		criteria.setTenantId("od."+searchCriteria.getCity());
		criteria.setMrNumbers(Arrays.asList(searchCriteria.getMrNumber()));
		generateMRCertificate(searchCriteria, criteria, requestInfoWrapper, model, xstream);
	}

	private void processPTPullUriRequest(SearchCriteria searchCriteria, RequestInfoWrapper requestInfoWrapper,
			PullURIResponse model, XStream xstream) throws IOException, MalformedURLException {
		PaymentSearchCriteria criteria = new PaymentSearchCriteria();
		criteria.setTenantId("od."+searchCriteria.getCity());
		criteria.setConsumerCodes(Collections.singleton(searchCriteria.getPropertyId()));
		generatePTReceipt(searchCriteria, criteria, requestInfoWrapper, model, xstream);
	}

	private void generateMRCertificate(SearchCriteria searchCriteria, MRSearchCriteria criteria,
			RequestInfoWrapper requestInfoWrapper, Object model, XStream xstream)
			throws IOException, MalformedURLException {
		
		List<MarriageRegistration> registrations = mrService.getMarriageRegistrations(criteria, requestInfoWrapper);
	
		if((!registrations.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("TRUE")) && validateMrResponse(searchCriteria, registrations)
				|| !registrations.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("FALSE")) {
			
			log.info("Marriage Registrations Found and Validation Passed ! "+ String.valueOf(registrations));
			FileStoreUrlResponse filestoreid=null;
			String filestore=null;
			for (MarriageRegistration registration : registrations) {

				if (registration.getStatus().equalsIgnoreCase("APPROVED")) {
					if (registration.getDscDetails().get(0).getDocumentId() != null) {
						String tenantId = criteria.getTenantId();
						filestore = registration.getDscDetails().get(0).getDocumentId();
						filestoreid = paymentService
								.getFilestore(registration.getDscDetails().get(0).getDocumentId(), tenantId);
						break;
					}
				}

			}
			if (filestoreid != null && !filestoreid.getFilestoreIds().isEmpty()) {

				MrCertificate certificate = new MrCertificate();

				if (searchCriteria.getDocType().equals("RMCER")) {

					certificate = populateCertificateForMR(registrations.get(0));
					xstream.processAnnotations(MrCertificate.class);

				}
				
				createCeritificateFromFilestoreIdForMR(searchCriteria, model, xstream, certificate, filestoreid, filestore, registrations.get(0));
			
			}
		}	
		else
		{
			ResponseStatus responseStatus=new ResponseStatus();
		     responseStatus.setStatus("0");
		     DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		     LocalDateTime now = LocalDateTime.now();  
		     responseStatus.setTs(dtf.format(now));
		     responseStatus.setTxn(searchCriteria.getTxn());
		     
		 
		     DocDetailsResponse docDetailsResponse=new DocDetailsResponse();
		     DocDetailsIssuedTo issuedTo=new DocDetailsIssuedTo();
		     List<DocDetailsPerson> persons= new ArrayList<DocDetailsPerson>();
		     issuedTo.setPersons(persons);
		     docDetailsResponse.setURI(null);
		     docDetailsResponse.setIssuedTo(issuedTo);
		     //docDetailsResponse.setDataContent("");
		     docDetailsResponse.setDocContent("");
		     log.info(PTServiceDXConstants.EXCEPTION_TEXT_VALIDATION_MR);
		     if(model.getClass().equals(PullDocResponse.class)){
		    	 ((PullDocResponse) model).setDocDetails(docDetailsResponse);
		    	 ((PullDocResponse) model).setResponseStatus(responseStatus);
		     }else {
		    	 ((PullURIResponse) model).setDocDetails(docDetailsResponse);
		    	 ((PullURIResponse) model).setResponseStatus(responseStatus);
		     }
		     

		}

	}

	private void createCeritificateFromFilestoreIdForMR(SearchCriteria searchCriteria, Object model,
			XStream xstream, MrCertificate certificate, FileStoreUrlResponse filestoreid, String filestore,
			MarriageRegistration marriageRegistration)
			throws MalformedURLException, IOException {
		String tenantId = ("od."+searchCriteria.getCity());
		String pdfPath = filestoreid.getFilestoreIds().get(0).get("url");
		URL url1 = new URL(pdfPath);
		try {

			// Read the PDF from the URL and save to a local file
			InputStream is1 = url1.openStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[1024];

			while ((nRead = is1.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			
			buffer.flush();
			byte[] targetArray = buffer.toByteArray();
			String encodedString = Base64.getEncoder().encodeToString(targetArray);

			ResponseStatus responseStatus = new ResponseStatus();
			responseStatus.setStatus("1");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			responseStatus.setTs(dtf.format(now));
			responseStatus.setTxn(searchCriteria.getTxn());

			DocDetailsPerson person = new DocDetailsPerson();
			person.setName(marriageRegistration.getCoupleDetails().get(0).getGroom().getFirstName());
			person.setPhone("");
			person.setGender("Male");
			Long dob = marriageRegistration.getCoupleDetails().get(0).getGroom().getDateOfBirth();
			if(dob!= null) {
				LocalDate date = Instant.ofEpochMilli(dob).atZone(ZoneId.systemDefault()).toLocalDate();
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
				person.setDob(date.format(formatters));
			}else {
				person.setDob("dd-mm-yyyy");
			}
			
			List persons = new ArrayList<>();
			persons.add(person);
			
			if(model.getClass().equals(PullDocResponse.class)) {

				DocDetailsResponse docDetailsResponse = new DocDetailsResponse();
				docDetailsResponse
						.setDataContent(Base64.getEncoder().encodeToString(xstream.toXML(certificate).getBytes()));
				docDetailsResponse.setDocContent(encodedString);
				((PullDocResponse) model).setResponseStatus(responseStatus);
				((PullDocResponse) model).setDocDetails(docDetailsResponse);
			}else {
				
				DGLRequest dgl = populateDataAfterSuccessResponse(searchCriteria,filestore);
				String maskedId = dgl.getDglModel().getMaskedId();
				
				DocDetailsIssuedTo issuedTo = new DocDetailsIssuedTo();
				DocDetailsResponse docDetailsResponse = new DocDetailsResponse();
				issuedTo.setPersons(persons);
				docDetailsResponse.setURI(DIGILOCKER_ISSUER_ID.concat("-")
						.concat(DIGILOCKER_DOCTYPE_MR_CERT).concat("-").concat(maskedId));
				docDetailsResponse.setIssuedTo(issuedTo);

				docDetailsResponse
						.setDataContent(Base64.getEncoder().encodeToString(xstream.toXML(certificate).getBytes()));

				docDetailsResponse.setDocContent(encodedString);
				((PullURIResponse) model).setResponseStatus(responseStatus);
				((PullURIResponse) model).setDocDetails(docDetailsResponse);
			}

			
		} catch (NullPointerException npe) {
			log.error(npe.getMessage());
			log.info("Error Occured", npe.getMessage());
		}

	}

	private MrCertificate populateCertificateForMR(MarriageRegistration marriageRegistration) {
		MrCertificate certificate = new MrCertificate();
		certificate.setLanguage("99");
		certificate.setType("RMCER");
		certificate.setName("Marriage Registration Certificate");
		certificate.setApplicationNo(marriageRegistration.getApplicationNumber());
		certificate.setMobile("");
		Long dateIssued = marriageRegistration.getIssuedDate();
		if(dateIssued!= null) {
			LocalDate date = Instant.ofEpochMilli(dateIssued).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			certificate.setDate(date.format(formatters));
		}else {
			certificate.setDate("dd-mm-yyyy");
		}

		IssuedBy issuedBy = new IssuedBy();
		Organization organization = new Organization();
		organization.setName("Housing and Urban Development Department, Govt Of Odisha");
		organization.setTin("");
		organization.setCode("");
		organization.setUid("");
		organization.setType("SG");
		Address addressIssuedBy = new Address();
		addressIssuedBy.setType(null);
		addressIssuedBy.setLine1("3rd Floor, Kharvel Bhawan");
		addressIssuedBy.setLine2("West Wing, Room No 301");
		addressIssuedBy.setLandmark("");
		addressIssuedBy.setLocality("");
		addressIssuedBy.setDistrict("Bhubaneswar");
		addressIssuedBy.setPin("751001");
		addressIssuedBy.setState("Odisha");
		addressIssuedBy.setCountry("IN");
		organization.setAddress(addressIssuedBy);
		issuedBy.setOrganisation(organization);
		certificate.setIssuedBy(issuedBy);

		MRIssuedTo issuedTo = new MRIssuedTo();
		PersonMR personGroom = new PersonMR();
		personGroom.setTitle("Mr.");
		personGroom.setName(marriageRegistration.getCoupleDetails().get(0).getGroom().getFirstName());
		personGroom.setGender("Male");
		personGroom.setMaritalStatus("1");
		personGroom.setReligion("");
		Long dobGroom = marriageRegistration.getCoupleDetails().get(0).getGroom().getDateOfBirth();
		if(dobGroom!= null) {
			LocalDate date = Instant.ofEpochMilli(dobGroom).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			personGroom.setDob(date.format(formatters));
		}else {
			personGroom.setDob("dd-mm-yyyy");
		}
		
		Address addressGroom = new Address();
		addressGroom.setType(null);
		addressGroom.setHouse("");
		addressGroom.setLine1(marriageRegistration.getCoupleDetails().get(0).getGroom().getAddress().getAddressLine1());
		addressGroom.setLine2("");
		addressGroom.setLandmark("");
		addressGroom.setLocality("");
		addressGroom.setDistrict(marriageRegistration.getCoupleDetails().get(0).getGroom().getAddress().getDistrict());
		addressGroom.setPin(marriageRegistration.getCoupleDetails().get(0).getGroom().getAddress().getPinCode());
		addressGroom.setState(marriageRegistration.getCoupleDetails().get(0).getGroom().getAddress().getState());		
		addressGroom.setCountry(marriageRegistration.getCoupleDetails().get(0).getGroom().getAddress().getCountry());		
		
		personGroom.setAddress(addressGroom);
		
		
		PersonMR personBride = new PersonMR();
		personBride.setTitle("Ms.");
		personBride.setName(marriageRegistration.getCoupleDetails().get(0).getBride().getFirstName());
		personBride.setGender("Female");
		personBride.setMaritalStatus("1");
		personBride.setReligion("");
		Long dob = marriageRegistration.getCoupleDetails().get(0).getBride().getDateOfBirth();
		if(dob!= null) {
			LocalDate date = Instant.ofEpochMilli(dob).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			personBride.setDob(date.format(formatters));
		}else {
			personBride.setDob("dd-mm-yyyy");
		}
		
		Address addressBride = new Address();
		addressBride.setType(null);;
		addressBride.setLine1(marriageRegistration.getCoupleDetails().get(0).getBride().getAddress().getAddressLine1());
		addressBride.setLine2("");
		addressBride.setLandmark("");
		addressBride.setLocality("");
		addressBride.setHouse("");
		addressBride.setDistrict(marriageRegistration.getCoupleDetails().get(0).getBride().getAddress().getDistrict());
		addressBride.setPin(marriageRegistration.getCoupleDetails().get(0).getBride().getAddress().getPinCode());
		addressBride.setState(marriageRegistration.getCoupleDetails().get(0).getBride().getAddress().getState());		
		addressBride.setCountry(marriageRegistration.getCoupleDetails().get(0).getBride().getAddress().getCountry());
		
		personBride.setAddress(addressBride);
		
		issuedTo.setPersonBride(personBride);
		issuedTo.setPersonGroom(personGroom);
		certificate.setIssuedTo(issuedTo);
		

		MrCertificateData certificateData = new MrCertificateData();
		
		
		MarriageCertificate marriageCertificate = new MarriageCertificate();
		
		MarriagePlace marriagePlace = marriageRegistration.getMarriagePlace();
		
		marriageCertificate.setPlace(marriagePlace.getPlaceOfMarriage().concat("," + marriagePlace.getWard())
				.concat("," + marriagePlace.getPinCode()));		
		Long issuedDate = marriageRegistration.getIssuedDate();
		if(issuedDate!= null) {
			LocalDate date = Instant.ofEpochMilli(issuedDate).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			marriageCertificate.setSolemnizedDate(date.format(formatters));
		}else {
			marriageCertificate.setSolemnizedDate("dd-mm-yyyy");
		}
		
		Registration registration = new Registration();
		registration.setNumber(marriageRegistration.getMrNumber());
		if(issuedDate!= null) {
			LocalDate date = Instant.ofEpochMilli(issuedDate).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			registration.setDate(date.format(formatters));
		}else {
			registration.setDate("dd-mm-yyyy");
		}
		
		Ward ward = Ward.builder().name("").build();
		
		Husband husband = Husband.builder().name(personGroom.getName()).build();
		
		HusbandResiding husbandResiding = HusbandResiding.builder()
				.residingAt(
						addressGroom.getLocality() + "," + addressGroom.getState() + "," + addressGroom.getCountry())
				.build();
		
		Wife wife = Wife.builder().name(personBride.getName()).build();
		
		WifeResiding wifeResiding = WifeResiding.builder()
				.residingAt(
						addressBride.getLocality() + "," + addressBride.getState() + "," + addressBride.getCountry())
				.build();
		
		marriageCertificate.setHusband(husband);
		marriageCertificate.setWife(wife);
		marriageCertificate.setHusbandResiding(husbandResiding);
		marriageCertificate.setWifeResiding(wifeResiding);
		marriageCertificate.setWard(ward);
		marriageCertificate.setRegistration(registration);
		
		
		certificateData.setMarriageCertificate(marriageCertificate);
		certificate.setCertificateData(certificateData);
		return certificate;
	}

	private boolean validateMrResponse(SearchCriteria searchCriteria, List<MarriageRegistration> registrations) {
		// Implement if any Validations are needed
		return true;
	}

	private void generatePTReceipt(SearchCriteria searchCriteria, PaymentSearchCriteria criteria,
			RequestInfoWrapper requestInfoWrapper, PullURIResponse model, XStream xstream)
			throws IOException, MalformedURLException {
		List<Payment> payments = searchPayments(searchCriteria, criteria, requestInfoWrapper);

		if ((!payments.isEmpty() && configurations.getValidationFlag().toUpperCase().equals("TRUE")
				&& validateRequest(searchCriteria, payments.get(0)))
				|| (!payments.isEmpty() && configurations.getValidationFlag().toUpperCase().equals("FALSE"))) {
			log.info("Payment object is not null and validations passed!!!");
			String o = null;
			String filestore = null;
			if (payments.get(0).getFileStoreId() != null) {
				filestore = payments.get(0).getFileStoreId();
				o = paymentService.getFilestore(payments.get(0).getFileStoreId(), "od").toString();
			} else {
				List<Payment> latestPayment = new ArrayList<Payment>();
				latestPayment.add(payments.get(0));
				PaymentRequest paymentRequest = new PaymentRequest();
				paymentRequest.setPayments(latestPayment);
				// requestInfoWrapper.getRequestInfo().setMsgId("1674457280493|en_IN");
				paymentRequest.setRequestInfo(requestInfoWrapper.getRequestInfo());
				filestore = paymentService.createPDF(paymentRequest);
				o = paymentService.getFilestore(filestore, "od").toString();

			}
			if (o != null) {

				Certificate certificate = new Certificate();

				if (searchCriteria.getDocType().equals("PRTAX")) {

					certificate = populateCertificate(payments.get(0));
					xstream.processAnnotations(Certificate.class);

				}
				createCeritificateFromFilestoreId(searchCriteria, model, xstream, certificate, o, filestore);
			}
		}

		else {
			ResponseStatus responseStatus = new ResponseStatus();
			responseStatus.setStatus("0");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			responseStatus.setTs(dtf.format(now));
			responseStatus.setTxn(searchCriteria.getTxn());
			model.setResponseStatus(responseStatus);

			DocDetailsResponse docDetailsResponse = new DocDetailsResponse();
			IssuedTo issuedTo = new IssuedTo();
			List<Person> persons = new ArrayList<Person>();
			issuedTo.setPersons(persons);
			docDetailsResponse.setURI(null);
			//docDetailsResponse.setIssuedTo(issuedTo);
			// docDetailsResponse.setDataContent("");
			docDetailsResponse.setDocContent("");
			log.info(PTServiceDXConstants.EXCEPTION_TEXT_VALIDATION_MR);
			model.setDocDetails(docDetailsResponse);

		}
	}

	private void createCeritificateFromFilestoreId(SearchCriteria searchCriteria, PullURIResponse model,
			XStream xstream, Certificate certificate, String o, String filestore)
			throws MalformedURLException, IOException {

		String path = o.split("url=")[1];
		String pdfPath = path.substring(0, path.length() - 3);
		URL url1 = new URL(pdfPath);
		try {

			// Read the PDF from the URL and save to a local file
			InputStream is1 = url1.openStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[1024];

			while ((nRead = is1.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();
			byte[] targetArray = buffer.toByteArray();
			String encodedString = Base64.getEncoder().encodeToString(targetArray);

			ResponseStatus responseStatus = new ResponseStatus();
			responseStatus.setStatus("1");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			responseStatus.setTs(dtf.format(now));
			responseStatus.setTxn(searchCriteria.getTxn());
			model.setResponseStatus(responseStatus);

			DocDetailsResponse docDetailsResponse = new DocDetailsResponse();
			IssuedTo issuedTo = new IssuedTo();
			List<Person> persons = new ArrayList<Person>();
			issuedTo.setPersons(persons);
			docDetailsResponse
					.setURI(DIGILOCKER_ISSUER_ID.concat("-").concat(DIGILOCKER_DOCTYPE).concat("-").concat(filestore));
			//docDetailsResponse.setIssuedTo(issuedTo);

			docDetailsResponse
					.setDataContent(Base64.getEncoder().encodeToString(xstream.toXML(certificate).getBytes()));

			docDetailsResponse.setDocContent(encodedString);

			model.setDocDetails(docDetailsResponse);

		} catch (NullPointerException npe) {
			log.error(npe.getMessage());
			log.info("Error Occured", npe.getMessage());
		}
	}

	private List<Payment> searchPayments(SearchCriteria searchCriteria, PaymentSearchCriteria criteria,
			RequestInfoWrapper requestInfoWrapper) {
		List<Payment> payments = paymentService.getPayments(criteria, searchCriteria.getDocType(), requestInfoWrapper);
		log.info("Payments found are:---" + ((!payments.isEmpty() ? payments.size() : "No payments found")));
		log.info("Name to search is " + searchCriteria.getPayerName());
		log.info("Mobile to search is " + searchCriteria.getMobile());
		if (!payments.isEmpty()) {
			log.info("Name in latest payment is " + payments.get(0).getPayerName());
			log.info("Mobile in latest payment is " + payments.get(0).getMobileNumber());
		}
		return payments;
	}


	public String searchForDigiLockerDocRequest(SearchCriteria searchCriteria) throws IOException {

		PullDocResponse model = new PullDocResponse();

		String[] urlArray = searchCriteria.getURI().split("-");
		int len = urlArray.length;
		String filestoreId = "";
		String docType = urlArray[1];
		for (int i = 2; i < len; i++) {
			if (i == (len - 1))
				filestoreId = filestoreId.concat(urlArray[i]);
			else
				filestoreId = filestoreId.concat(urlArray[i]).concat("-");

		}
		
		DGLSearchCriteria criteria = DGLSearchCriteria.builder().maskedId(filestoreId).build();
		
		DGLModel dglModel = repository.searchDataForDGL(criteria); 
	
		String tenantId = "";
		String unMaskedId = "";
		FileStoreUrlResponse o = null;
		if (Objects.nonNull(dglModel)) {
			tenantId = dglModel.getTenantId();
			unMaskedId = dglModel.getFilestore();
			o = paymentService.getFilestore(unMaskedId, tenantId);
		}
		
		if (o != null && !o.getFilestoreIds().isEmpty()) {
			String pdfPath = o.getFilestoreIds().get(0).get("url");
			URL url1 = new URL(pdfPath);
			try {

				/*// Read the PDF from the URL and save to a local file
				InputStream is1 = url1.openStream();
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();

				int nRead;
				byte[] data = new byte[1024];

				while ((nRead = is1.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}

				buffer.flush();
				byte[] targetArray = buffer.toByteArray();
				// byte[] sourceBytes = IOUtils.toByteArray(is1)

				String encodedString = Base64.getEncoder().encodeToString(targetArray);

				// model.setResponseStatus("1");
				ResponseStatus responseStatus = new ResponseStatus();
				responseStatus.setStatus("1");
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				responseStatus.setTs(dtf.format(now));
				responseStatus.setTxn(searchCriteria.getTxn());
				model.setResponseStatus(responseStatus);

				DocDetailsResponse docDetailsResponse = new DocDetailsResponse();
				IssuedTo issuedTo = new IssuedTo();
				List<Person> persons = new ArrayList<Person>();
				issuedTo.setPersons(persons);
				docDetailsResponse.setURI(null);
				//docDetailsResponse.setIssuedTo(issuedTo);
				// docDetailsResponse.setDataContent(encodedString);
				docDetailsResponse.setDocContent(encodedString);

				model.setDocDetails(docDetailsResponse);*/

				// return targetArray.toString();
				
				SearchCriteria searchCriteriaForPullUri = SearchCriteria.builder()
						.city(tenantId.substring(3, tenantId.length())).txn(searchCriteria.getTxn()).docType(docType)
						.build();
				
				if (PTServiceDXConstants.DIGILOCKER_DOCTYPE_MR_CERT.equals(docType)) {
					searchCriteriaForPullUri.setMrNumber(dglModel.getConsumerCode());
				} else if (PTServiceDXConstants.DIGILOCKER_DOCTYPE_TL_CERT.equals(docType)) {
					searchCriteriaForPullUri.setLicenseNumber(dglModel.getConsumerCode());
				} else if (PTServiceDXConstants.DIGILOCKER_DOCTYPE_BPA_CERT.equals(docType)) {
					searchCriteriaForPullUri.setApprovalNumber(dglModel.getConsumerCode());;
				}
				return searchForDigiLockerURIRequest(searchCriteriaForPullUri, true);

			} catch (NullPointerException npe) {
				log.error(npe.getMessage());
				log.info("Error Occured", npe.getMessage());
			}
		} else {
			ResponseStatus responseStatus = new ResponseStatus();
			responseStatus.setStatus("0");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			responseStatus.setTs(dtf.format(now));
			responseStatus.setTxn(searchCriteria.getTxn());
			model.setResponseStatus(responseStatus);
			DocDetailsResponse docDetailsResponse = new DocDetailsResponse();
			log.info(EXCEPTION_TEXT_VALIDATION);
			IssuedTo issuedTo = new IssuedTo();
			List<Person> persons = new ArrayList<Person>();
			issuedTo.setPersons(persons);
			docDetailsResponse.setURI(null);
			//docDetailsResponse.setIssuedTo(issuedTo);
			docDetailsResponse.setDataContent("");
			docDetailsResponse.setDocContent("");

			model.setDocDetails(docDetailsResponse);

		}

		XStream xstream = new XStream();
		xstream.addPermission(NoTypePermission.NONE); // forbid everything
		xstream.addPermission(NullPermission.NULL); // allow "null"
		xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xstream.addPermission(AnyTypePermission.ANY);
		// xstream.processAnnotations(DocDetails.class);
		xstream.processAnnotations(PullDocResponse.class);
		xstream.toXML(model);

		return xstream.toXML(model);

	}

	Boolean validateRequest(SearchCriteria searchCriteria, Payment payment) {
		if (!searchCriteria.getPayerName().equals(payment.getPayerName()))
			return false;
		else if (!searchCriteria.getMobile().equals(payment.getMobileNumber()))
			return false;
		else
			return true;

	}
		
	Certificate populateCertificate(Payment payment) {
		Certificate certificate = new Certificate();
		certificate.setLanguage("99");
		certificate.setName("Property Tax Receipt");
		certificate.setType("PRTAX");
		certificate.setNumber("");
		certificate.setPrevNumber("");
		certificate.setExpiryDate("");
		certificate.setValidFromDate("");
		certificate.setIssuedAt("");
		certificate.setIssueDate("");
		certificate.setStatus("A");

		IssuedBy issuedBy = new IssuedBy();
		Organization organization = new Organization();
		organization.setName("");
		organization.setType("SG");
		Address address = new Address();
		address.setCountry("IN");
		organization.setAddress(address);
		issuedBy.setOrganisation(organization);
		certificate.setIssuedBy(issuedBy);

		IssuedTo issuedTo = new IssuedTo();
		Person person = new Person();
//		person.setAddress(address);
//		person.setPhoto("");
		certificate.setIssuedTo(issuedTo);

		CertificateData certificateData = new CertificateData();
		CertificateForData certificateForData = new CertificateForData();
		certificateData.setCertificate(certificateForData);

		PropertyTaxReceipt propertyTaxReceipt = new PropertyTaxReceipt();
		propertyTaxReceipt.setPaymentDate(payment.getPaymentDetails().get(0).getReceiptDate().toString());
		propertyTaxReceipt.setServicetype(payment.getPaymentDetails().get(0).getBusinessService());
		propertyTaxReceipt.setReceiptNo(payment.getPaymentDetails().get(0).getReceiptNumber());
		propertyTaxReceipt.setPropertyID(payment.getPaymentDetails().get(0).getBill().getConsumerCode());
		propertyTaxReceipt.setOwnerName(payment.getPayerName());
		propertyTaxReceipt.setOwnerContact(payment.getMobileNumber());
		propertyTaxReceipt.setPaymentstatus(payment.getPaymentStatus().toString());
		PaymentForReceipt paymentForReceipt = new PaymentForReceipt();
		paymentForReceipt.setPaymentMode(payment.getPaymentMode().toString());
		String billingPeriod = (payment.getPaymentDetails().get(0).getBill().getBillDetails().get(0).getFromPeriod()
				.toString()).concat("-")
				.concat(payment.getPaymentDetails().get(0).getBill().getBillDetails().get(0).getToPeriod().toString());
		paymentForReceipt.setBillingPeriod(billingPeriod);
		paymentForReceipt.setPropertyTaxAmount(payment.getTotalDue().toString());
		paymentForReceipt.setPaidAmount(payment.getTotalAmountPaid().toString());
		paymentForReceipt.setPendingAmount((payment.getTotalDue().subtract(payment.getTotalAmountPaid())).toString());
		paymentForReceipt.setExcessAmount("");
		paymentForReceipt.setTransactionID(payment.getTransactionNumber());
		paymentForReceipt.setG8ReceiptDate(payment.getPaymentDetails().get(0).getManualReceiptNumber());
		paymentForReceipt.setG8ReceiptNo(payment.getPaymentDetails().get(0).getManualReceiptDate().toString());

		propertyTaxReceipt.setPaymentForReceipt(paymentForReceipt);
		certificateData.setPropertyTaxReceipt(propertyTaxReceipt);
		certificate.setCertificateData(certificateData);
		return certificate;
	}
	
	private void generateTlLicense(SearchCriteria searchCriteria, TradeLicenseSearchCriteria criteria,
			RequestInfoWrapper requestInfoWrapper, Object model, XStream xstream)
			throws IOException, MalformedURLException {
		
		List<TradeLicense> licenses = tlService.getTradeLicenses(criteria, requestInfoWrapper);

		if((!licenses.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("TRUE")) && validateTLResponse(searchCriteria, licenses)
				|| !licenses.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("FALSE")) {
			
			log.info("Trade Licenses Found and Validation Passed ! " + String.valueOf(licenses));
			FileStoreUrlResponse filestoreid = null;
			String filestore = null;
			for (TradeLicense license : licenses) {
				if (license.getStatus().equalsIgnoreCase("APPROVED")) {
					if (license.getTradeLicenseDetail().getDscDetails().get(0).getDocumentId() != null) {
						String tenantId = criteria.getTenantId();
						filestore = license.getTradeLicenseDetail().getDscDetails().get(0).getDocumentId();
						filestoreid = paymentService
								.getFilestore(license.getTradeLicenseDetail().getDscDetails().get(0).getDocumentId(),
										tenantId);
						break;
					}
				}
			}
			
			if (filestoreid != null && !filestoreid.getFilestoreIds().isEmpty()) {

				TLCertificate certificate = new TLCertificate();

				if (searchCriteria.getDocType().equals("TDLCS")) {

					certificate = populateCertificateForTL(licenses.get(0), requestInfoWrapper);
					xstream.processAnnotations(TLCertificate.class);

				}
				
				createCeritificateFromFilestoreIdForTL(searchCriteria, model, xstream, certificate, filestoreid, filestore, licenses.get(0));
				
			}
		}	
		else
		{
			ResponseStatus responseStatus=new ResponseStatus();
		     responseStatus.setStatus("0");
		     DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		     LocalDateTime now = LocalDateTime.now();  
		     responseStatus.setTs(dtf.format(now));
		     responseStatus.setTxn(searchCriteria.getTxn());
		     
		 
		     DocDetailsResponse docDetailsResponse=new DocDetailsResponse();
		     DocDetailsIssuedTo issuedTo=new DocDetailsIssuedTo();
		     List<DocDetailsPerson> persons= new ArrayList<DocDetailsPerson>();
		     issuedTo.setPersons(persons);
		     docDetailsResponse.setURI(null);
		     docDetailsResponse.setIssuedTo(issuedTo);
		     //docDetailsResponse.setDataContent("");
		     docDetailsResponse.setDocContent("");
		     log.info(PTServiceDXConstants.EXCEPTION_TEXT_VALIDATION_TL);
		     
		     if(model.getClass().equals(PullDocResponse.class)){
		    	 ((PullDocResponse) model).setDocDetails(docDetailsResponse);
		    	 ((PullDocResponse) model).setResponseStatus(responseStatus);
		     }else {
		    	 ((PullURIResponse) model).setDocDetails(docDetailsResponse);
		    	 ((PullURIResponse) model).setResponseStatus(responseStatus);
		     }
		     

		}
	}

	private boolean validateTLResponse(SearchCriteria searchCriteria, List<TradeLicense> licenses) {
		// Add Validations for Trade License if needed
		return true;
	}

	private void createCeritificateFromFilestoreIdForTL(SearchCriteria searchCriteria, Object model,
			XStream xstream, TLCertificate certificate, FileStoreUrlResponse filestoreid, String filestore,
			TradeLicense tradeLicense)
			throws IOException, MalformedURLException {

		String tenantId = ("od." + searchCriteria.getCity());
		String pdfPath = filestoreid.getFilestoreIds().get(0).get("url");
		URL url1 = new URL(pdfPath);
		try {

			// Read the PDF from the URL and save to a local file
			InputStream is1 = url1.openStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[1024];

			while ((nRead = is1.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();
			byte[] targetArray = buffer.toByteArray();
			String encodedString = Base64.getEncoder().encodeToString(targetArray);

			ResponseStatus responseStatus = new ResponseStatus();
			responseStatus.setStatus("1");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			responseStatus.setTs(dtf.format(now));
			responseStatus.setTxn(searchCriteria.getTxn());

			
			DocDetailsPerson person = new DocDetailsPerson();
			person.setName(tradeLicense.getTradeLicenseDetail().getOwners().get(0).getName());
			person.setPhone(tradeLicense.getTradeLicenseDetail().getOwners().get(0).getMobileNumber());
			person.setGender(tradeLicense.getTradeLicenseDetail().getOwners().get(0).getGender());
			Long dob = tradeLicense.getTradeLicenseDetail().getOwners().get(0).getDob();
			if(dob!= null) {
				LocalDate date = Instant.ofEpochMilli(dob).atZone(ZoneId.systemDefault()).toLocalDate();
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
				person.setDob(date.format(formatters));
			}else {
				person.setDob("dd-mm-yyyy");
			}
			
			List persons = new ArrayList<>();
			persons.add(person);			
			if(model.getClass().equals(PullDocResponse.class)) {

				DocDetailsResponse docDetailsResponse = new DocDetailsResponse();
				docDetailsResponse
						.setDataContent(Base64.getEncoder().encodeToString(xstream.toXML(certificate).getBytes()));
				docDetailsResponse.setDocContent(encodedString);
				((PullDocResponse) model).setResponseStatus(responseStatus);
				((PullDocResponse) model).setDocDetails(docDetailsResponse);
			}else {
				
				DGLRequest dgl = populateDataAfterSuccessResponse(searchCriteria,filestore);
				String maskedId = dgl.getDglModel().getMaskedId();
				
				DocDetailsIssuedTo issuedTo = new DocDetailsIssuedTo();
				DocDetailsResponse docDetailsResponse = new DocDetailsResponse();
				issuedTo.setPersons(persons);
				docDetailsResponse.setURI(DIGILOCKER_ISSUER_ID.concat("-")
						.concat(PTServiceDXConstants.DIGILOCKER_DOCTYPE_TL_CERT).concat("-").concat(maskedId));
				docDetailsResponse.setIssuedTo(issuedTo);

				docDetailsResponse
						.setDataContent(Base64.getEncoder().encodeToString(xstream.toXML(certificate).getBytes()));

				docDetailsResponse.setDocContent(encodedString);
				((PullURIResponse) model).setResponseStatus(responseStatus);
				((PullURIResponse) model).setDocDetails(docDetailsResponse);
			}
		} catch (NullPointerException npe) {
			log.error(npe.getMessage());
			log.info("Error Occured", npe.getMessage());
		}

	}

	private TLCertificate populateCertificateForTL(TradeLicense tradeLicense, RequestInfoWrapper reqInfoWrapper) {
		
		RequestInfo requestInfo = reqInfoWrapper.getRequestInfo();
		String tenantId = tradeLicense.getTenantId();
		Object mdmsData = tlService.mDMSCall(requestInfo, tenantId, true);
		String district="";
		if (Objects.nonNull(mdmsData)) {
			List<Map<String, Object>> result = JsonPath.read(mdmsData, "$.MdmsRes.tenant.tenants");

			for (Map<String, Object> item : result) {
				if (item.get("code").equals(tenantId)) {
					Map<String, String> city = (Map<String, String>) item.get("city");
					district = city.get("districtName");
				}
			}
		}
		
		TLCertificate certificate = new TLCertificate();
		certificate.setLanguage("99");
		certificate.setPrevNumber("");
		certificate.setIssuedAt("");
		Long issueDate = tradeLicense.getIssuedDate();
		if(issueDate!= null) {
			LocalDate date = Instant.ofEpochMilli(issueDate).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			certificate.setIssueDate(date.format(formatters));
		}else {
			certificate.setIssueDate("dd-mm-yyyy");
		}
		certificate.setStatus("1");
		certificate.setName("Trade License Certificate");
		certificate.setType("TDLCS");
		certificate.setNumber(tradeLicense.getLicenseNumber());
		certificate.setPrevNumber("");
		Long expiryDate = tradeLicense.getValidTo();
		if(expiryDate!= null) {
			LocalDate date = Instant.ofEpochMilli(expiryDate).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			certificate.setExpiryDate(date.format(formatters));
		}else {
			certificate.setExpiryDate("dd-mm-yyyy");
		}
		Long validFrom = tradeLicense.getValidFrom();
		if(validFrom!= null) {
			LocalDate date = Instant.ofEpochMilli(validFrom).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			certificate.setValidFromDate(date.format(formatters));
		}else {
			certificate.setValidFromDate("dd-mm-yyyy");
		}

		IssuedBy issuedBy = new IssuedBy();
		Organization organization = new Organization();
		organization.setName("Housing and Urban Development Department, Govt Of Odisha");
		organization.setTin("");
		organization.setCode("");
		organization.setUid("");
		organization.setType("SG");
		Address addressIssuedBy = new Address();
		addressIssuedBy.setCountry("IN");
		addressIssuedBy.setType("");
		addressIssuedBy.setLine1("3rd Floor, Kharvel Bhawan");
		addressIssuedBy.setLine2("West Wing, Room No 301");
		addressIssuedBy.setLandmark("");
		addressIssuedBy.setLocality("");
		addressIssuedBy.setDistrict("Bhubaneswar");
		addressIssuedBy.setPin("751001");
		addressIssuedBy.setState("Odisha");
		addressIssuedBy.setCountry("IN");
		organization.setAddress(addressIssuedBy);
		issuedBy.setOrganisation(organization);
		certificate.setIssuedBy(issuedBy);
		
		String tradeType = "";
		Object mdmsDataForTradeType = tlService.mDMSCall(requestInfo, tenantId, false);
		if (Objects.nonNull(mdmsDataForTradeType)) {
			List<Map<String, Object>> result = JsonPath.read(mdmsDataForTradeType, "$.MdmsRes.TradeLicense.TradeType");

			for (Map<String, Object> item : result) {
				
				if (item.get("code")
						.equals(tradeLicense.getTradeLicenseDetail().getTradeUnits().get(0).getTradeType())) {
					
					tradeType = (String) item.get("name");
				}
			}

		}

		TLIssuedTo issuedTo = new TLIssuedTo();
		OrganizationTL organizationIssuesTo = new OrganizationTL();
		organizationIssuesTo.setName(tradeLicense.getTradeName());
		organizationIssuesTo.setTin("");
		organizationIssuesTo.setCode("");
		organizationIssuesTo.setUid("");
		organizationIssuesTo.setType(tradeType);
		Address addressIssuedTo = new Address();
		addressIssuedTo.setCountry("IN");
		String type = tradeLicense.getTradeLicenseDetail().getAddress().getType();
		addressIssuedTo.setType(StringUtils.isEmpty(type)?"":type);
		String line1 = tradeLicense.getTradeLicenseDetail().getAddress().getAddressLine1();
		addressIssuedTo.setLine1(StringUtils.isEmpty(line1)?"":line1);
		String line2 = tradeLicense.getTradeLicenseDetail().getAddress().getAddressLine2();
		addressIssuedTo.setLine2(StringUtils.isEmpty(line2)?"":line2);
		String landmark = tradeLicense.getTradeLicenseDetail().getAddress().getLandmark();
		addressIssuedTo.setLandmark(StringUtils.isEmpty(landmark)?"":landmark);
		String locality = tradeLicense.getTradeLicenseDetail().getAddress().getLocality().getName();
		addressIssuedTo.setLocality(StringUtils.isEmpty(locality)?"":locality);
		addressIssuedTo.setDistrict(district);
		String pin = tradeLicense.getTradeLicenseDetail().getAddress().getPincode();
		addressIssuedTo.setPin(StringUtils.isEmpty(pin)?"":pin);
		addressIssuedTo.setState("Odisha");
		organizationIssuesTo.setAddress(addressIssuedTo);
		Person person = new Person();
		person.setUid("");
		person.setName(tradeLicense.getTradeLicenseDetail().getOwners().get(0).getName());
		person.setPhone(tradeLicense.getTradeLicenseDetail().getOwners().get(0).getMobileNumber());
		String email = tradeLicense.getTradeLicenseDetail().getOwners().get(0).getEmailId();
		person.setEmail(email);
		if(StringUtils.isEmpty(email))
			person.setEmail("");
		person.setTitle(
				tradeLicense.getTradeLicenseDetail().getOwners().get(0).getGender().equalsIgnoreCase("MALE") ? "Mr."
						: "Ms.");
		Address addressPerson =  new Address();
		addressPerson.setCountry("IN");
		addressPerson.setType("");
		addressPerson.setLine1("");
		addressPerson.setLine2("");
		addressPerson.setLandmark("");
		addressPerson.setLocality(tradeLicense.getTradeLicenseDetail().getOwners().get(0).getPermanentAddress());
		addressPerson.setDistrict(district);
		addressPerson.setPin("");
		addressPerson.setState("Odisha");
		
		person.setAddress(addressPerson);
		issuedTo.setOrganization(organizationIssuesTo);
		issuedTo.setPerson(person);
		certificate.setIssuedTo(issuedTo);

		TLCertificateData certificateData = new TLCertificateData();
		CertificateForData certificateForData = new CertificateForData();
		certificateData.setCertificate(certificateForData);
		
		TradeLicenseCertificate tradeLicenseCertificate = new TradeLicenseCertificate();
		
		
		tradeLicenseCertificate.setMunicipalLicenceNo("");
		tradeLicenseCertificate.setNewLicenceNo(tradeLicense.getLicenseNumber());
		tradeLicenseCertificate.setUnitName(tradeLicense.getTradeName());
		if(expiryDate!= null) {
			LocalDate date = Instant.ofEpochMilli(expiryDate).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			tradeLicenseCertificate.setValidUpto(date.format(formatters));
		}else {
			tradeLicenseCertificate.setValidUpto("dd-mm-yyyy");
		}
		
		Pan pan = Pan.builder().name("").number("").build();
		
		PropertyDetails propertyDetails = PropertyDetails.builder().assessed("").buid("").address("").build();
		
		Licensee licensee = new Licensee();
		licensee.setName(tradeLicense.getTradeLicenseDetail().getOwners().get(0).getName());				
		licensee.setFatherHusbandName(tradeLicense.getTradeLicenseDetail().getOwners().get(0).getFatherOrHusbandName());
		licensee.setAddress(tradeLicense.getTradeLicenseDetail().getOwners().get(0).getPermanentAddress());		
		
		BusinessProfession businessProfession = BusinessProfession.builder().challanNo("").licenseFee("").name("")
				.penalty("").sdFee("").scrunityFee("").year("").build();	
		
		tradeLicenseCertificate.setAddress(new Address(addressIssuedTo));
		tradeLicenseCertificate.setPan(pan);
		tradeLicenseCertificate.setPropertyDetails(propertyDetails);		
		tradeLicenseCertificate.setLicensee(licensee);	
		tradeLicenseCertificate.setBusinessProfession(businessProfession);		
		
		certificateForData.setNumber(tradeLicense.getLicenseNumber());
		
		Long issuedDate = tradeLicense.getIssuedDate();
		if(issuedDate!= null) {
			LocalDate date = Instant.ofEpochMilli(issuedDate).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			certificateForData.setDate(date.format(formatters));
		}else {
			certificateForData.setDate("dd-mm-yyyy");
		}
		certificateForData.setPlace(tradeLicense.getTradeLicenseDetail().getAddress().getLocality().getName());
		
		certificateData.setCertificate(certificateForData);
		certificateData.setTlCertificate(tradeLicenseCertificate);
		
		certificate.setCertificateData(certificateData);
		certificate.setSignature("");
		return certificate;
	}
	
	private void generateBPALetter(SearchCriteria searchCriteria, BPASearchCriteria criteria,
			RequestInfoWrapper requestInfoWrapper, Object model, XStream xstream) throws IOException {
		
		List<BPA> applications = bpaService.getBPAPermitLetter(criteria, requestInfoWrapper);
		
		if((!applications.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("TRUE")) && validateBPAResponse(searchCriteria, applications)
				|| !applications.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("FALSE")) {
			
			log.info("BPA Applications Found and Validation Passed ! " + String.valueOf(applications));
			FileStoreUrlResponse filestoreid = null;
			String filestore = null;
			for (BPA application : applications) {
				if (application.getStatus().equalsIgnoreCase("APPROVED")) {
					if (application.getDscDetails().get(0).getDocumentId() != null) {
						String tenantId = criteria.getTenantId();
						filestore = application.getDscDetails().get(0).getDocumentId();
						filestoreid = paymentService
								.getFilestore(application.getDscDetails().get(0).getDocumentId(), tenantId);
						break;
					}
				}
			}
			
			if (filestoreid != null && !filestoreid.getFilestoreIds().isEmpty()) {

				BPACertificate certificate = new BPACertificate();

				if (searchCriteria.getDocType().equals("BPCER")) {

					certificate = populateCertificateForBPA(applications.get(0), requestInfoWrapper);
					xstream.processAnnotations(BPACertificate.class);

				}
				
				createCeritificateFromFilestoreIdForBPA(searchCriteria, model, xstream, certificate, filestoreid, filestore, applications.get(0));
			
			}
		}	
		else
		{
			ResponseStatus responseStatus=new ResponseStatus();
		     responseStatus.setStatus("0");
		     DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		     LocalDateTime now = LocalDateTime.now();  
		     responseStatus.setTs(dtf.format(now));
		     responseStatus.setTxn(searchCriteria.getTxn());
		 
		     DocDetailsResponse docDetailsResponse=new DocDetailsResponse();
		     DocDetailsIssuedTo issuedTo=new DocDetailsIssuedTo();
		     List<DocDetailsPerson> persons= new ArrayList<DocDetailsPerson>();
		     issuedTo.setPersons(persons);
		     docDetailsResponse.setURI(null);
		     docDetailsResponse.setIssuedTo(issuedTo);
		     //docDetailsResponse.setDataContent("");
		     docDetailsResponse.setDocContent("");
		     log.info(PTServiceDXConstants.EXCEPTION_TEXT_VALIDATION_BPA);
		     if(model.getClass().equals(PullDocResponse.class)){
		    	 ((PullDocResponse) model).setDocDetails(docDetailsResponse);
		    	 ((PullDocResponse) model).setResponseStatus(responseStatus);
		     }else {
		    	 ((PullURIResponse) model).setDocDetails(docDetailsResponse);
		    	 ((PullURIResponse) model).setResponseStatus(responseStatus);
		     }

		}
		
	}

	private boolean validateBPAResponse(SearchCriteria searchCriteria, List<BPA> applications) {
		// Implement BPA Validations here if any
		return true;
	}
	
	private BPACertificate populateCertificateForBPA(BPA bpa, RequestInfoWrapper reqInfoWrapper) {
		
		RequestInfo requestInfo = reqInfoWrapper.getRequestInfo();
		
		//call edcr here to get the required data
		LinkedHashMap edcrData = bpaService.getEDCRDetails(requestInfo, bpa);
		
		BPACertificate certificate = new BPACertificate();
		certificate.setLanguage("99");
		certificate.setName("Building Plan/ License");
		certificate.setType("BPCER");
		certificate.setNumber(bpa.getApprovalNo());
		certificate.setPrevNumber("");
		Long validFrom = bpa.getApprovalDate();
		if(validFrom!= null) {
			LocalDate date = Instant.ofEpochMilli(validFrom).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			certificate.setValidFromDate(date.format(formatters));
		}else {
			certificate.setValidFromDate("dd-mm-yyyy");
		}
		certificate.setIssuedAt(bpa.getTenantId().substring(3));
		if(validFrom!= null) {
			LocalDate date = Instant.ofEpochMilli(validFrom).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			certificate.setIssueDate(date.format(formatters));
		}else {
			certificate.setIssueDate("dd-mm-yyyy");
		}
		certificate.setStatus("A");
		
		Long validUpto = bpaService.getValidUptoDate(validFrom);
		if(validUpto!= null) {
			LocalDate date = Instant.ofEpochMilli(validUpto).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			certificate.setExpiryDate(date.format(formatters));
		}else {
			certificate.setExpiryDate("dd-mm-yyyy");
		}

		IssuedBy issuedBy = new IssuedBy();
		Organization organization = new Organization();
		organization.setName("Housing and Urban Development Department, Govt Of Odisha");
		organization.setTin("");
		organization.setCode("");
		organization.setUid("");
		organization.setType("SG");
		Address addressIssuedBy = new Address();
		addressIssuedBy.setType("");
		addressIssuedBy.setLine1("3rd Floor, Kharvel Bhawan");
		addressIssuedBy.setLine2("West Wing, Room No 301");
		addressIssuedBy.setLandmark("");
		addressIssuedBy.setLocality("");
		addressIssuedBy.setDistrict("Bhubaneswar");
		addressIssuedBy.setPin("751001");
		addressIssuedBy.setState("Odisha");
		addressIssuedBy.setCountry("IN");
		organization.setAddress(addressIssuedBy);
		issuedBy.setOrganisation(organization);
		certificate.setIssuedBy(issuedBy);

		BPAIssuedTo issuedTo = new BPAIssuedTo();
		PersonBPA person = new PersonBPA();
		
		person.setName(bpa.getLandInfo().getOwners().get(0).getName());
		person.setPhone(bpa.getLandInfo().getOwners().get(0).getMobileNumber());
		Long dob = bpa.getLandInfo().getOwners().get(0).getDob();
		if(dob!= null) {
			LocalDate date = Instant.ofEpochMilli(dob).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			person.setDob(date.format(formatters));
		}else {
			person.setDob("dd-mm-yyyy");
		}
		person.setGender(bpa.getLandInfo().getOwners().get(0).getGender());
		person.setTitle(person.getGender().equalsIgnoreCase("Male")?"Mr.":"Ms.");
		person.setPhone(bpa.getLandInfo().getOwners().get(0).getMobileNumber());
		person.setEmail(bpa.getLandInfo().getOwners().get(0).getEmailId());
		
		Address addressPerson = new Address();
		addressPerson.setType("");
		addressPerson.setLine1("");
		addressPerson.setLine2("");
		addressPerson.setHouse("");
		addressPerson.setLandmark("");
		addressPerson.setLocality("");	
		addressPerson.setDistrict(bpa.getLandInfo().getOwners().get(0).getPermanentCity().substring(3));
		addressPerson.setPin(bpa.getLandInfo().getOwners().get(0).getPermanentPincode());
		addressPerson.setState("Odisha");
		addressPerson.setCountry("India");
		
		person.setAddress(addressPerson);
		person.setPhoto(Photo.builder().format("").build());
		issuedTo.setPerson(person);
		certificate.setIssuedTo(issuedTo);

		BPACertificateData certificateData = new BPACertificateData();
		CertificateForData certificateForData = new CertificateForData();
		Long issuedDate = bpa.getApprovalDate();
		if(issuedDate!= null) {
			LocalDate date = Instant.ofEpochMilli(issuedDate).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			certificateForData.setDate(date.format(formatters));
		}else {
			certificateForData.setDate("dd-mm-yyyy");
		}
		certificateForData.setNumber(bpa.getApprovalNo());
		certificateForData.setPlace(bpa.getTenantId().substring(3));
		certificateData.setCertificate(certificateForData);
		
		BuildingPlanCertificate buildingPlanCertificate = new BuildingPlanCertificate();
		buildingPlanCertificate.setSiNo(bpa.getApprovalNo());
		buildingPlanCertificate.setApplicationNo(bpa.getApplicationNo());
		buildingPlanCertificate.setUnitName("");
		buildingPlanCertificate.setFeesPaid("");
		
		AddressBPA addressBPA = new AddressBPA();
		
		Area area = new Area();
		if(issuedDate!= null) {
			LocalDate date = Instant.ofEpochMilli(issuedDate).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			area.setApprovedDate(date.format(formatters));
		}else {
			area.setApprovedDate("dd-mm-yyyy");
		}
		
		if(validUpto!= null) {
			LocalDate date = Instant.ofEpochMilli(validUpto).atZone(ZoneId.systemDefault()).toLocalDate();
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
			buildingPlanCertificate.setValidUpto(date.format(formatters));
		}else {
			buildingPlanCertificate.setValidUpto("dd-mm-yyyy");
		}
		ConstructionPermission permission = new ConstructionPermission();
		
		
		if(!CollectionUtils.isEmpty(edcrData)) {
			String jsonString = new JSONObject(edcrData).toString();
			DocumentContext context = JsonPath.using(Configuration.defaultConfiguration()).parse(jsonString);
			
			List<String> totalFloorArea = context.read("edcrDetail.*.planDetail.virtualBuilding.totalFloorArea");
			List<String> totalBUA = context.read("edcrDetail.*.planDetail.virtualBuilding.totalBuitUpArea");
			List<Integer> floors = context.read("edcrDetail.*.planDetail.blocks[0].building.totalFloors");
			List<String> plotNo = context.read("edcrDetail.*.planDetail.planInformation.plotNo");
			List<String> khataNo = context.read("edcrDetail.*.planDetail.planInformation.khataNo");
			List<String> mauza = context.read("edcrDetail.*.planDetail.planInformation.mauza");
			List<String> district = context.read("edcrDetail.*.planDetail.planInformation.district");
			Integer floor = floors.get(0);
			
			permission.setName(bpa.getLandInfo().getOwners().get(0).getName());
			permission.setOthers("");
			permission.setSingle((Integer.compare(floor, 1)==0)?"Single":"Multi");
			area.setType("");
			
			area.setFloorArea(String.valueOf(totalFloorArea.get(0)));
			area.setApprovedCoveredArea(String.valueOf(totalBUA.get(0)));
			
			addressBPA.setType("");
			addressBPA.setPlotNo(plotNo.get(0));
			addressBPA.setKhataNo(khataNo.get(0));
			addressBPA.setMauza(mauza.get(0));
			addressBPA.setDistrict(district.get(0));

			
		}
		
		
		buildingPlanCertificate.setAddress(addressBPA);
		buildingPlanCertificate.setArea(area);
		buildingPlanCertificate.setConstructionPermission(permission);
		
		certificateData.setCertificate(certificateForData);
		certificateData.setBuildingPlanCertificate(buildingPlanCertificate);
		certificate.setCertificateData(certificateData);
		return certificate;
	}
	
	private void createCeritificateFromFilestoreIdForBPA(SearchCriteria searchCriteria, Object model,
			XStream xstream, BPACertificate certificate, FileStoreUrlResponse filestoreid, String filestore,
			BPA bpa)
			throws MalformedURLException, IOException {

		String tenantId = ("od." + searchCriteria.getCity());
		String pdfPath = filestoreid.getFilestoreIds().get(0).get("url");
		URL url1 = new URL(pdfPath);
		try {

			// Read the PDF from the URL and save to a local file
			InputStream is1 = url1.openStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[1024];

			while ((nRead = is1.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();
			byte[] targetArray = buffer.toByteArray();
			String encodedString = Base64.getEncoder().encodeToString(targetArray);

			ResponseStatus responseStatus = new ResponseStatus();
			responseStatus.setStatus("1");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			responseStatus.setTs(dtf.format(now));
			responseStatus.setTxn(searchCriteria.getTxn());
			
			DocDetailsPerson person = new DocDetailsPerson();
			person.setName(bpa.getLandInfo().getOwners().get(0).getName());
			person.setPhone(bpa.getLandInfo().getOwners().get(0).getMobileNumber());
			person.setGender(bpa.getLandInfo().getOwners().get(0).getGender());
			Long dob = bpa.getLandInfo().getOwners().get(0).getDob();
			if(dob!= null) {
				LocalDate date = Instant.ofEpochMilli(dob).atZone(ZoneId.systemDefault()).toLocalDate();
				DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
				person.setDob(date.format(formatters));
			}else {
				person.setDob("dd-mm-yyyy");
			}
			
			List persons = new ArrayList<>();
			persons.add(person);
			
			if(model.getClass().equals(PullDocResponse.class)) {

				DocDetailsResponse docDetailsResponse = new DocDetailsResponse();
				docDetailsResponse
						.setDataContent(Base64.getEncoder().encodeToString(xstream.toXML(certificate).getBytes()));
				docDetailsResponse.setDocContent(encodedString);
				((PullDocResponse) model).setResponseStatus(responseStatus);
				((PullDocResponse) model).setDocDetails(docDetailsResponse);
			}else {
				
				DGLRequest dgl = populateDataAfterSuccessResponse(searchCriteria,filestore);
				String maskedId = dgl.getDglModel().getMaskedId();
				
				DocDetailsIssuedTo issuedTo = new DocDetailsIssuedTo();
				DocDetailsResponse docDetailsResponse = new DocDetailsResponse();
				issuedTo.setPersons(persons);
				docDetailsResponse.setURI(DIGILOCKER_ISSUER_ID.concat("-")
						.concat(PTServiceDXConstants.DIGILOCKER_DOCTYPE_BPA_CERT).concat("-").concat(maskedId));
				docDetailsResponse.setIssuedTo(issuedTo);

				docDetailsResponse
						.setDataContent(Base64.getEncoder().encodeToString(xstream.toXML(certificate).getBytes()));

				docDetailsResponse.setDocContent(encodedString);
				((PullURIResponse) model).setResponseStatus(responseStatus);
				((PullURIResponse) model).setDocDetails(docDetailsResponse);
			}
			
		} catch (NullPointerException npe) {
			log.error(npe.getMessage());
			log.info("Error Occured", npe.getMessage());
		}

	}
	
	private DGLRequest populateDataAfterSuccessResponse(SearchCriteria searchCriteria, String filestore) {
		
		String docType = searchCriteria.getDocType();
		String tenantId = "od."+searchCriteria.getCity();
		String consumerCode = "";
		
		if(docType.equalsIgnoreCase(PTServiceDXConstants.DIGILOCKER_DOCTYPE_MR_CERT)) {
			consumerCode = searchCriteria.getMrNumber();
		}else if (docType.equalsIgnoreCase(PTServiceDXConstants.DIGILOCKER_DOCTYPE_TL_CERT)) {
			consumerCode = searchCriteria.getLicenseNumber();
		}else if (docType.equalsIgnoreCase(PTServiceDXConstants.DIGILOCKER_DOCTYPE_BPA_CERT)) {
			consumerCode = searchCriteria.getApprovalNumber();
		}
		
		String maskedId = getMaskedIdForFilestore();
		
		DGLSearchCriteria criteria = DGLSearchCriteria.builder().consumerCode(consumerCode).build();
		
		DGLModel dglResponse = repository.searchDataForDGL(criteria);
		
		DGLModel dglToPersist = DGLModel.builder().consumerCode(consumerCode).maskedId(maskedId).filestore(filestore)
				.tenantId(tenantId).build();
		
		DGLRequest request = DGLRequest.builder().dglModel(dglToPersist).build();
		
		if(Objects.nonNull(dglResponse)) {
			
			dglToPersist.setMaskedId(dglResponse.getMaskedId());
			producer.push(configurations.getUpdateTopic(), request);
			
		} else {
			producer.push(configurations.getSaveTopic(), request);
		}
		
		return request;
	}

	private String getMaskedIdForFilestore() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

}
