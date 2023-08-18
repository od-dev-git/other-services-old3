package org.egov.usm.utility;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.usm.config.USMConfiguration;
import org.egov.usm.producer.Producer;
import org.egov.usm.repository.ServiceRequestRepository;
import org.egov.usm.web.model.Email;
import org.egov.usm.web.model.EmailRequest;
import org.egov.usm.web.model.SMSRequest;
import org.egov.usm.web.model.SurveyDetailsRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;




@Component
@Slf4j
public class NotificationUtil {

	private USMConfiguration config;

	private ServiceRequestRepository serviceRequestRepository;

	private Producer producer;

	@Autowired
	public NotificationUtil(USMConfiguration config, ServiceRequestRepository serviceRequestRepository, Producer producer) {
		this.config = config;
		this.serviceRequestRepository = serviceRequestRepository;
		this.producer = producer;
	}


	/**
	 * Extracts message for the specific code
	 * 
	 * @param notificationCode
	 *            The code for which message is required
	 * @param localizationMessage
	 *            The localization messages
	 * @return message for the specific code
	 */
	@SuppressWarnings("unchecked")
	public String getMessageTemplate(String notificationCode, String localizationMessage) {
		String path = "$..messages[?(@.code==\"{}\")].message";
		path = path.replace("{}", notificationCode);
		String message = null;
		try {
			Object messageObj = JsonPath.parse(localizationMessage).read(path);
			message = ((ArrayList<String>) messageObj).get(0);
		} catch (Exception e) {
			log.warn("Fetching from localization failed", e);
		}
		return message;
	}
	

	
	/**
	 * Returns the uri for the localization call
	 * 
	 * @param tenantId
	 *            TenantId of the propertyRequest
	 * @return The uri for localization search call
	 */
	public StringBuilder getUri(String tenantId, RequestInfo requestInfo) {

		if (config.getIsLocalizationStateLevel())
			tenantId = tenantId.split("\\.")[0];

		String locale = Constants.NOTIFICATION_LOCALE;
		if (!StringUtils.isEmpty(requestInfo.getMsgId()) && requestInfo.getMsgId().split("|").length >= 2)
			locale = requestInfo.getMsgId().split("\\|")[1];

		StringBuilder uri = new StringBuilder();
		uri.append(config.getLocalizationHost()).append(config.getLocalizationContextPath())
				.append(config.getLocalizationSearchEndpoint()).append("?").append("locale=").append(locale)
				.append("&tenantId=").append(tenantId).append("&module=").append(Constants.MODULE)
				.append("&codes=").append(StringUtils.join(Constants.NOTIFICATION_CODES,','));

		return uri;
	}

	
	
	/**
	 * Fetches messages from localization service
	 * 
	 * @param tenantId
	 *            tenantId of the MarriageRegistration
	 * @param requestInfo
	 *            The requestInfo of the request
	 * @return Localization messages for the module
	 */
	@SuppressWarnings("rawtypes")
	public String getLocalizationMessages(String tenantId, RequestInfo requestInfo) {
		LinkedHashMap responseMap = (LinkedHashMap) serviceRequestRepository.fetchResult(getUri(tenantId, requestInfo),
				requestInfo);
		String jsonString = new JSONObject(responseMap).toString();
		return jsonString;
	}

	
	/**
	 * Convert time to Local date time for Notification
	 * 
	 * @param scheduledTime
	 * @return Local date time format
	 */
	public String epochToDate(Long scheduledTime) {
		Calendar cal = new GregorianCalendar();
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
		cal.setTimeZone(timeZone);
		cal.setTimeInMillis(scheduledTime);
		
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String year = String.valueOf(cal.get(Calendar.YEAR));
		
		SimpleDateFormat time_format = new SimpleDateFormat("hh:mm aa");
		time_format.setTimeZone(timeZone);
		String timeComp = time_format.format(cal.getTime());
		StringBuilder date = new StringBuilder(day);
		date.append("/").append(month).append("/").append(year).append(" " + timeComp);

		return date.toString();
	}
	
	
	 
	/**
	 * Push SMS notification to kafka
	 * 
	 * @param smsRequestList
	 * @param isSMSEnabled
	 */
	public void sendSMS(List<SMSRequest> smsRequestList, boolean isSMSEnabled) {
		if (isSMSEnabled) {
			if (CollectionUtils.isEmpty(smsRequestList))
				log.info("Messages from localization couldn't be fetched!");
			for (SMSRequest smsRequest : smsRequestList) {
				producer.push(config.getSmsNotificationTopic(), smsRequest);
				log.info("MobileNumber: " + smsRequest.getMobileNumber() + " Messages: " + smsRequest.getMessage());
			}
		}
	}



	/**
	 * Creates sms request for the each owners
	 * 
	 * @param message
	 *            The message for the specific user
	 * @param mobileNumberToOwnerName
	 *            Map of mobileNumber to OwnerName
	 * @return List of SMSRequest
	 */
	public List<SMSRequest> createSMSRequest(String message, Map<String, String> mobileNumberToOwnerName) {
		List<SMSRequest> smsRequest = new LinkedList<>();
		for (Map.Entry<String, String> entryset : mobileNumberToOwnerName.entrySet()) {
			String customizedMsg = message.replace("<1>", entryset.getValue());
			customizedMsg = customizedMsg.replace(Constants.NOTIFICATION_OWNER_NAME_KEY, entryset.getValue());
			smsRequest.add(new SMSRequest(entryset.getKey(), customizedMsg));
		}
		return smsRequest;
	}



	/**
	 * Create mail request for specific user
	 * 
	 * @param message
	 * @param ownersEmailId
	 * @param request
	 * @return List<EmailRequest>
	 */
	public List<EmailRequest> createEmailRequest(String message, Map<String, String> ownersEmailId,
			SurveyDetailsRequest request) {
		
		Set<String> emailTo = ownersEmailId.keySet().stream().collect(Collectors.toSet());
		String subject = config.getEmailSubject().replaceAll(Constants.EMAIL_SUBJECT_ID_KEY, request.getSurveyDetails().getSurveyTickets().get(0).getTicketNo());
		
		List<EmailRequest> emailRequest = new LinkedList<>();
		Email email = Email.builder().body(message).emailTo(emailTo).subject(subject).build();
		emailRequest.add(EmailRequest.builder().email(email).requestInfo(request.getRequestInfo()).build());
		return emailRequest;
	}
	
	
	/**
	 * Push mail notification to kafka
	 * 
	 * @param emailRequestList
	 * @param isEmailEnabled
	 */
	public void sendEmail(List<EmailRequest> emailRequestList, boolean isEmailEnabled) {
		if (isEmailEnabled) {
			if (CollectionUtils.isEmpty(emailRequestList))
				log.info("Messages from localization couldn't be fetched!");
			for (EmailRequest emailRequest : emailRequestList) {
				producer.push(config.getEmailNotificationTopic(), emailRequest);
				log.info(" Email pushed to send: " + emailRequest.getEmail());
			}
		}
	}

}
