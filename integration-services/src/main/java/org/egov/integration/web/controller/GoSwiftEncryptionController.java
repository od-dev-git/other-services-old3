package org.egov.integration.web.controller;

import org.egov.integration.service.GoSwiftEncryptionService;
import org.egov.integration.web.model.FeedbackCreationResponse;
import org.egov.integration.web.model.GoSwiftInputRequest;
import org.egov.integration.web.model.GoSwiftLoginRequest;
import org.egov.integration.web.model.GoSwiftLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goswift")
public class GoSwiftEncryptionController {

    @Autowired
    private GoSwiftEncryptionService encryptionService;

    @PostMapping("/_encrypt")
    public ResponseEntity<String> encrypt(@RequestBody GoSwiftInputRequest goSwiftInputRequest){
        String response= encryptionService.encrypt(goSwiftInputRequest.getGoSwiftInput());
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
    
    @PostMapping("/_login")
    public ResponseEntity<GoSwiftLoginResponse> decrypt(@RequestBody GoSwiftLoginRequest goSwiftLoginRequest){
        GoSwiftLoginResponse response= encryptionService.login(goSwiftLoginRequest.getCode());
        return new ResponseEntity<GoSwiftLoginResponse>(response, HttpStatus.OK);
    }
}
