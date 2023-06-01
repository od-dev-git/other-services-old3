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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.dx.util.Configurations;
import org.egov.dx.util.PTServiceDXConstants;
import org.egov.dx.web.models.Address;
import org.egov.dx.web.models.Certificate;
import org.egov.dx.web.models.CertificateData;
import org.egov.dx.web.models.CertificateForData;
import org.egov.dx.web.models.DocDetailsResponse;
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
import org.egov.dx.web.models.BPA.BPA;
import org.egov.dx.web.models.BPA.BPACertificate;
import org.egov.dx.web.models.BPA.BPACertificateData;
import org.egov.dx.web.models.BPA.BPASearchCriteria;
import org.egov.dx.web.models.BPA.BuildingPlanCertificate;
import org.egov.dx.web.models.MR.MRSearchCriteria;
import org.egov.dx.web.models.MR.MarriageCertificate;
import org.egov.dx.web.models.MR.MarriageRegistration;
import org.egov.dx.web.models.MR.MrCertificate;
import org.egov.dx.web.models.MR.MrCertificateData;
import org.egov.dx.web.models.TL.TLCertificate;
import org.egov.dx.web.models.TL.TLCertificateData;
import org.egov.dx.web.models.TL.TradeLicense;
import org.egov.dx.web.models.TL.TradeLicenseCertificate;
import org.egov.dx.web.models.TL.TradeLicenseSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public String searchPullURIRequest(SearchCriteria  searchCriteria) throws IOException {
		
		if(searchCriteria.getOrigin().equals(ORIGIN))
		{
			return searchForDigiLockerURIRequest(searchCriteria);
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
	

	public String searchForDigiLockerURIRequest(SearchCriteria searchCriteria) throws IOException {

		RequestInfo request = new RequestInfo();
		request.setApiId("Rainmaker");
		request.setMsgId("1670564653696|en_IN");
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();

		User userInfo = User.builder().uuid(configurations.getAuthTokenVariable()).type("EMPLOYEE")
				.roles(Collections.emptyList()).id(0L).tenantId("od.".concat(searchCriteria.getCity())).build();

		request = new RequestInfo("", "", 0L, "", "", "", "", "", "", userInfo);
		//request.setAuthToken("996c8153-352c-4769-b1de-3b70730dbd53");
		// request.setUserInfo(userResponse.getUser());
		requestInfoWrapper.setRequestInfo(request);
		PullURIResponse model = new PullURIResponse();
		XStream xstream = new XStream();
		xstream.addPermission(NoTypePermission.NONE); // forbid everything
		xstream.addPermission(NullPermission.NULL); // allow "null"
		xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xstream.addPermission(AnyTypePermission.ANY);

		if (PTServiceDXConstants.DIGILOCKER_DOCTYPE.equals(searchCriteria.getDocType())) {
			processPTPullUriRequest(searchCriteria, requestInfoWrapper, model, xstream);
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

		return xstream.toXML(model);

	}
	
	private void processBPAPullUriRequest(SearchCriteria searchCriteria, RequestInfoWrapper requestInfoWrapper,
			PullURIResponse model, XStream xstream) throws IOException {
		BPASearchCriteria criteria = new BPASearchCriteria();
		criteria.setTenantId("od."+searchCriteria.getCity());
		criteria.setApprovalNo(searchCriteria.getApprovalNumber());
		generateBPALetter(searchCriteria, criteria, requestInfoWrapper, model, xstream);	
	}

	private void processTLPullUriRequest(SearchCriteria searchCriteria, RequestInfoWrapper requestInfoWrapper,
			PullURIResponse model, XStream xstream) throws MalformedURLException, IOException {
		TradeLicenseSearchCriteria criteria = new TradeLicenseSearchCriteria();
		criteria.setTenantId("od."+searchCriteria.getCity());
		criteria.setLicenseNumbers(Arrays.asList(searchCriteria.getLicenseNumber()));
		generateTlLicense(searchCriteria, criteria, requestInfoWrapper, model, xstream);
	}

	private void processMRPullUriRequest(SearchCriteria searchCriteria, RequestInfoWrapper requestInfoWrapper,
			PullURIResponse model, XStream xstream) throws IOException, MalformedURLException {
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
			RequestInfoWrapper requestInfoWrapper, PullURIResponse model, XStream xstream)
			throws IOException, MalformedURLException {
		
		List<MarriageRegistration> registrations = mrService.getMarriageRegistrations(criteria, requestInfoWrapper);
	
		if((!registrations.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("TRUE")) && validateMrResponse(searchCriteria, registrations)
				|| !registrations.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("FALSE")) {
			
			log.info("Marriage Registrations Found and Validation Passed ! "+ String.valueOf(registrations));
			String filestoreid=null;
			String filestore=null;
			for (MarriageRegistration registration : registrations) {

				if (registration.getStatus().equalsIgnoreCase("APPROVED")) {
					if (registration.getDscDetails().get(0).getDocumentId() != null) {
						String tenantId = criteria.getTenantId();
						filestore = registration.getDscDetails().get(0).getDocumentId();
						filestoreid = paymentService
								.getFilestore(registration.getDscDetails().get(0).getDocumentId(), tenantId).toString();
						break;
					}
				}

			}
			if (filestoreid != null) {

				MrCertificate certificate = new MrCertificate();

				if (searchCriteria.getDocType().equals("RMCER")) {

					certificate = populateCertificateForMR(registrations.get(0));
					xstream.processAnnotations(MrCertificate.class);

				}
				
				createCeritificateFromFilestoreIdForMR(searchCriteria, model, xstream, certificate, filestoreid, filestore);
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
		     model.setResponseStatus(responseStatus);
		 
		     DocDetailsResponse docDetailsResponse=new DocDetailsResponse();
		     IssuedTo issuedTo=new IssuedTo();
		     List<Person> persons= new ArrayList<Person>();
		     issuedTo.setPersons(persons);
		     docDetailsResponse.setURI(null);
		     docDetailsResponse.setIssuedTo(issuedTo);
		     //docDetailsResponse.setDataContent("");
		     docDetailsResponse.setDocContent("");
		     log.info(PTServiceDXConstants.EXCEPTION_TEXT_VALIDATION_MR);
		     model.setDocDetails(docDetailsResponse);

		}

	}

	private void createCeritificateFromFilestoreIdForMR(SearchCriteria searchCriteria, PullURIResponse model,
			XStream xstream, MrCertificate certificate, String filestoreid, String filestore)
			throws MalformedURLException, IOException {
		String tenantId = ("od."+searchCriteria.getCity());
		String path = filestoreid.split("url=")[1];
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
			docDetailsResponse.setURI(tenantId.concat("-").concat(DIGILOCKER_ISSUER_ID).concat("-")
					.concat(DIGILOCKER_DOCTYPE_MR_CERT).concat("-").concat(filestore));
			docDetailsResponse.setIssuedTo(issuedTo);

			docDetailsResponse
					.setDataContent(Base64.getEncoder().encodeToString(xstream.toXML(certificate).getBytes()));

			docDetailsResponse.setDocContent(encodedString);

			model.setDocDetails(docDetailsResponse);
		} catch (NullPointerException npe) {
			log.error(npe.getMessage());
			log.info("Error Occured", npe.getMessage());
		}

	}

	private MrCertificate populateCertificateForMR(MarriageRegistration marriageRegistration) {
		MrCertificate certificate = new MrCertificate();
		certificate.setLanguage("99");
		certificate.setName("Marriage Certificate");
		certificate.setType("RMCER");
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
		person.setAddress(address);
		person.setPhoto("");
		certificate.setIssuedTo(issuedTo);

		MrCertificateData certificateData = new MrCertificateData();
		CertificateForData certificateForData = new CertificateForData();
		certificateData.setCertificate(certificateForData);
		
		MarriageCertificate marriageCertificate = new MarriageCertificate();
		marriageCertificate.builder().applicationDate(marriageRegistration.getApplicationDate())
				.marriageDate(marriageRegistration.getMarriageDate()).mrNumber(marriageRegistration.getMrNumber())
				.applicationNumber(marriageRegistration.getApplicationNumber())
				.brideDOB(marriageRegistration.getCoupleDetails().get(0).getBride().getDateOfBirth())
				.brideGroomDOB(marriageRegistration.getCoupleDetails().get(0).getGroom().getDateOfBirth())
				.brideGuardianName(marriageRegistration.getCoupleDetails().get(0).getBride().getFatherName())
				.brideGuardianAddress(marriageRegistration.getCoupleDetails().get(0).getBride().getAddress())
				.brideGroomGuardianName(marriageRegistration.getCoupleDetails().get(0).getGroom().getFatherName())
				.brideGroomGuardianAddress(marriageRegistration.getCoupleDetails().get(0).getGroom().getAddress())
				.brideWitnessName(marriageRegistration.getCoupleDetails().get(0).getBride().getWitness().getFirstName())
				.brideWitnessAddress(
						marriageRegistration.getCoupleDetails().get(0).getBride().getWitness().getAddress())
				.brideGroomWitnessName(
						marriageRegistration.getCoupleDetails().get(0).getGroom().getWitness().getFirstName())
				.brideGroomWitnessAddress(
						marriageRegistration.getCoupleDetails().get(0).getGroom().getWitness().getAddress())
				.build();
		
		certificateData.setCertificate(certificateForData);
		certificateData.setMarriageCertificate(marriageCertificate);
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
			docDetailsResponse.setIssuedTo(issuedTo);
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
			docDetailsResponse.setIssuedTo(issuedTo);

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
		String tenantId = urlArray[0];
		for (int i = 3; i < len; i++) {
			if (i == (len - 1))
				filestoreId = filestoreId.concat(urlArray[i]);
			else
				filestoreId = filestoreId.concat(urlArray[i]).concat("-");

		}
		String o = paymentService.getFilestore(filestoreId, tenantId).toString();

		if (o != null) {
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
				docDetailsResponse.setIssuedTo(issuedTo);
				// docDetailsResponse.setDataContent(encodedString);
				docDetailsResponse.setDocContent(encodedString);

				model.setDocDetails(docDetailsResponse);

				// return targetArray.toString();

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
			docDetailsResponse.setIssuedTo(issuedTo);
			// docDetailsResponse.setDataContent("");
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
		person.setAddress(address);
		person.setPhoto("");
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
			RequestInfoWrapper requestInfoWrapper, PullURIResponse model, XStream xstream)
			throws IOException, MalformedURLException {
		
		List<TradeLicense> licenses = tlService.getTradeLicenses(criteria, requestInfoWrapper);

		if((!licenses.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("TRUE")) && validateTLResponse(searchCriteria, licenses)
				|| !licenses.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("FALSE")) {
			
			log.info("Trade Licenses Found and Validation Passed ! " + String.valueOf(licenses));
			String filestoreid = null;
			String filestore = null;
			for (TradeLicense license : licenses) {
				if (license.getStatus().equalsIgnoreCase("APPROVED")) {
					if (license.getTradeLicenseDetail().getDscDetails().get(0).getDocumentId() != null) {
						String tenantId = criteria.getTenantId();
						filestore = license.getTradeLicenseDetail().getDscDetails().get(0).getDocumentId();
						filestoreid = paymentService
								.getFilestore(license.getTradeLicenseDetail().getDscDetails().get(0).getDocumentId(),
										tenantId)
								.toString();
						break;
					}
				}
			}
			
			if (filestoreid != null) {

				TLCertificate certificate = new TLCertificate();

				if (searchCriteria.getDocType().equals("TDLCS")) {

					certificate = populateCertificateForTL(licenses.get(0));
					xstream.processAnnotations(TLCertificate.class);

				}
				
				createCeritificateFromFilestoreIdForTL(searchCriteria, model, xstream, certificate, filestoreid, filestore);
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
		     model.setResponseStatus(responseStatus);
		 
		     DocDetailsResponse docDetailsResponse=new DocDetailsResponse();
		     IssuedTo issuedTo=new IssuedTo();
		     List<Person> persons= new ArrayList<Person>();
		     issuedTo.setPersons(persons);
		     docDetailsResponse.setURI(null);
		     docDetailsResponse.setIssuedTo(issuedTo);
		     //docDetailsResponse.setDataContent("");
		     docDetailsResponse.setDocContent("");
		     log.info(PTServiceDXConstants.EXCEPTION_TEXT_VALIDATION_TL);
		     model.setDocDetails(docDetailsResponse);

		}
	}

	private boolean validateTLResponse(SearchCriteria searchCriteria, List<TradeLicense> licenses) {
		// Add Validations for Trade License if needed
		return true;
	}

	private void createCeritificateFromFilestoreIdForTL(SearchCriteria searchCriteria, PullURIResponse model,
			XStream xstream, TLCertificate certificate, String filestoreid, String filestore)
			throws IOException, MalformedURLException {

		String tenantId = ("od." + searchCriteria.getCity());
		String path = filestoreid.split("url=")[1];
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
			docDetailsResponse.setURI(tenantId.concat("-").concat(DIGILOCKER_ISSUER_ID).concat("-")
					.concat(PTServiceDXConstants.DIGILOCKER_DOCTYPE_TL_CERT).concat("-").concat(filestore));
			docDetailsResponse.setIssuedTo(issuedTo);

			docDetailsResponse
					.setDataContent(Base64.getEncoder().encodeToString(xstream.toXML(certificate).getBytes()));

			docDetailsResponse.setDocContent(encodedString);

			model.setDocDetails(docDetailsResponse);
		} catch (NullPointerException npe) {
			log.error(npe.getMessage());
			log.info("Error Occured", npe.getMessage());
		}

	}

	private TLCertificate populateCertificateForTL(TradeLicense tradeLicense) {
		
		TLCertificate certificate = new TLCertificate();
		certificate.setLanguage("99");
		certificate.setName("Trade License");
		certificate.setType("TDLCS");
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
		person.setAddress(address);
		person.setPhoto("");
		certificate.setIssuedTo(issuedTo);

		TLCertificateData certificateData = new TLCertificateData();
		CertificateForData certificateForData = new CertificateForData();
		certificateData.setCertificate(certificateForData);
		
		TradeLicenseCertificate tradeLicenseCertificate = new TradeLicenseCertificate();
		tradeLicenseCertificate.builder().applicationNumber(tradeLicense.getApplicationNumber())
				.licenseNumber(tradeLicense.getLicenseNumber()).licenseIssueDate(tradeLicense.getIssuedDate())
				.validFrom(tradeLicense.getValidFrom()).validTo(tradeLicense.getValidTo())
				.tradeName(tradeLicense.getTradeName())
				.tradeOwner(tradeLicense.getTradeLicenseDetail().getOwners().get(0).getName())
				.tradeAddress(tradeLicense.getTradeLicenseDetail().getAddress())
				.build();
		
		certificateData.setCertificate(certificateForData);
		certificateData.setTlCertificate(tradeLicenseCertificate);
		return certificate;
	}
	
	private void generateBPALetter(SearchCriteria searchCriteria, BPASearchCriteria criteria,
			RequestInfoWrapper requestInfoWrapper, PullURIResponse model, XStream xstream) throws IOException {
		
		List<BPA> applications = bpaService.getBPAPermitLetter(criteria, requestInfoWrapper);
		
		if((!applications.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("TRUE")) && validateBPAResponse(searchCriteria, applications)
				|| !applications.isEmpty() && configurations.getValidationFlag().equalsIgnoreCase("FALSE")) {
			
			log.info("BPA Applications Found and Validation Passed ! " + String.valueOf(applications));
			String filestoreid = null;
			String filestore = null;
			for (BPA application : applications) {
				if (application.getStatus().equalsIgnoreCase("APPROVED")) {
					if (application.getDscDetails().get(0).getDocumentId() != null) {
						String tenantId = criteria.getTenantId();
						filestore = application.getDscDetails().get(0).getDocumentId();
						filestoreid = paymentService
								.getFilestore(application.getDscDetails().get(0).getDocumentId(), tenantId).toString();
						break;
					}
				}
			}
			
			if (filestoreid != null) {

				BPACertificate certificate = new BPACertificate();

				if (searchCriteria.getDocType().equals("BPCER")) {

					certificate = populateCertificateForBPA(applications.get(0));
					xstream.processAnnotations(BPACertificate.class);

				}
				
				createCeritificateFromFilestoreIdForBPA(searchCriteria, model, xstream, certificate, filestoreid, filestore);
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
		     model.setResponseStatus(responseStatus);
		 
		     DocDetailsResponse docDetailsResponse=new DocDetailsResponse();
		     IssuedTo issuedTo=new IssuedTo();
		     List<Person> persons= new ArrayList<Person>();
		     issuedTo.setPersons(persons);
		     docDetailsResponse.setURI(null);
		     docDetailsResponse.setIssuedTo(issuedTo);
		     //docDetailsResponse.setDataContent("");
		     docDetailsResponse.setDocContent("");
		     log.info(PTServiceDXConstants.EXCEPTION_TEXT_VALIDATION_BPA);
		     model.setDocDetails(docDetailsResponse);

		}
		
	}

	private boolean validateBPAResponse(SearchCriteria searchCriteria, List<BPA> applications) {
		// Implement BPA Validations here if any
		return true;
	}
	
	private BPACertificate populateCertificateForBPA(BPA bpa) {
		
		BPACertificate certificate = new BPACertificate();
		certificate.setLanguage("99");
		certificate.setName("Building Plan Permit Letter");
		certificate.setType("BPCER");
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
		person.setAddress(address);
		person.setPhoto("");
		certificate.setIssuedTo(issuedTo);

		BPACertificateData certificateData = new BPACertificateData();
		CertificateForData certificateForData = new CertificateForData();
		certificateData.setCertificate(certificateForData);
		
		BuildingPlanCertificate buildingPlanCertificate = new BuildingPlanCertificate();
		buildingPlanCertificate.builder().applicationNumber(bpa.getApplicationNo()).approvalDate(bpa.getApprovalDate())
				.approvalNumber(bpa.getApprovalNo()).ownerName(bpa.getLandInfo().getOwners().get(0).getName())
				.ownerAddress(bpa.getLandInfo().getAddress()).build();
		
		certificateData.setCertificate(certificateForData);
		certificateData.setBuildingPlanCertificate(buildingPlanCertificate);
		return certificate;
	}
	
	private void createCeritificateFromFilestoreIdForBPA(SearchCriteria searchCriteria, PullURIResponse model,
			XStream xstream, BPACertificate certificate, String filestoreid, String filestore)
			throws MalformedURLException, IOException {

		String tenantId = ("od." + searchCriteria.getCity());
		String path = filestoreid.split("url=")[1];
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
			docDetailsResponse.setURI(tenantId.concat("-").concat(DIGILOCKER_ISSUER_ID).concat("-")
					.concat(PTServiceDXConstants.DIGILOCKER_DOCTYPE_BPA_CERT).concat("-").concat(filestore));
			docDetailsResponse.setIssuedTo(issuedTo);

			docDetailsResponse
					.setDataContent(Base64.getEncoder().encodeToString(xstream.toXML(certificate).getBytes()));

			docDetailsResponse.setDocContent(encodedString);

			model.setDocDetails(docDetailsResponse);
		} catch (NullPointerException npe) {
			log.error(npe.getMessage());
			log.info("Error Occured", npe.getMessage());
		}

	}

}
