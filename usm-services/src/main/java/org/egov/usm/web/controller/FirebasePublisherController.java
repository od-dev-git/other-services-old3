package org.egov.usm.web.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.usm.utility.ResponseInfoFactory;
import org.egov.usm.web.model.MulticastMessageRepresentation;
import org.egov.usm.web.model.NotificationRequest;
import org.egov.usm.web.model.NotificationResponse;
import org.egov.usm.web.model.USMNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;

@RestController
@RequestMapping("/push/notification")
public class FirebasePublisherController {
	
	private final FirebaseMessaging firebaseMessaging;
	
	private final ResponseInfoFactory responseInfoFactory;
	
	@Autowired
    public FirebasePublisherController(FirebaseMessaging firebaseMessaging, ResponseInfoFactory responseInfoFactory) {
        this.firebaseMessaging = firebaseMessaging;
        this.responseInfoFactory = responseInfoFactory;
    }

	@PostMapping("/_topics")
	public ResponseEntity<NotificationResponse> postToTopic(@RequestBody NotificationRequest notificationRequest)
			throws FirebaseMessagingException {
		USMNotification notification = notificationRequest.getNotification();
		Message msg = Message.builder()
						.setTopic(notificationRequest.getNotification().getTopic())
						.putData("body", notificationRequest.getNotification().getMessage())
						.build();

		String id = firebaseMessaging.send(msg);
		notification.setId(id);
		NotificationResponse response =  NotificationResponse.builder()
				.notifications(Collections.singletonList(notification))
				.responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(notificationRequest.getRequestInfo(), true))
				.build();
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@PostMapping("/clients")
    public ResponseEntity<List<String>> postToClients(@RequestBody MulticastMessageRepresentation message) throws FirebaseMessagingException {
        
        MulticastMessage msg = MulticastMessage.builder()
          .addAllTokens(message.getRegistrationTokens())
          .putData("body", message.getData())
          .build();

        BatchResponse response = firebaseMessaging.sendMulticast(msg);
        
        List<String> ids = response.getResponses()
          .stream()
          .map(r->r.getMessageId())
          .collect(Collectors.toList());
        
        return ResponseEntity
          .status(HttpStatus.ACCEPTED)
          .body(ids);        
    }    
    
}
