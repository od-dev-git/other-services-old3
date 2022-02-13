/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.dsc.web.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.dsc.config.ApplicationProperties;
import org.egov.dsc.emas.ws.ds.DSAuthenticateWS;
import org.egov.dsc.emas.ws.ds.DSAuthenticateWSProxy;
import org.egov.dsc.model.CertificateRequest;
import org.egov.dsc.model.CertificateResponse;
import org.egov.dsc.model.CertificateResponsePojo;
import org.egov.dsc.model.DSCException;
import org.egov.dsc.model.DataSignRequest;
import org.egov.dsc.model.DataSignResponse;
import org.egov.dsc.model.DetailRequestPojo;
import org.egov.dsc.model.FileReq;
import org.egov.dsc.model.StorageResponse;
import org.egov.dsc.model.TokenInputResponse;
import org.egov.dsc.model.TokenRequest;
import org.egov.dsc.model.TokenResponse;
import org.egov.dsc.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import emh.Enum.ContentType;
import emh.Enum.Token_Status;
import emh.Enum.Token_Type;
import emh.Model.RequestData.ListCertificateRequest;
import emh.Model.RequestData.ListTokenRequest;
import emh.Model.RequestData.PKCSBulkPdfHashSignRequest;
import emh.Model.RequestData.PKCSSignRequest;
import emh.Model.RequestData.Request;
import emh.Model.ResponseData.BulkSignOutput;
import emh.Model.ResponseData.PKCSCertificate;
import emh.Model.ResponseData.ProviderToken;
import emh.Model.ResponseData.ResponseDataListPKCSCertificate;
import emh.Model.ResponseData.ResponseDataListProviderToken;
import emh.Model.ResponseData.ResponseDataPKCSBulkSign;
import emh.Model.ResponseData.ResponseDataPKCSSign;
import emh.emBridgeLib.EMBLException;
import emh.emBridgeLib.emBridge;
import emh.emBridgeLib.emBridgeSignerInput;

@RestController
@RequestMapping("/dsc")
public class DscController {

	@Autowired
	private ApplicationProperties applicationProperties;
    
	@Autowired
    private RestTemplate restTemplate;
	
	private String logPath=System.getProperty("user.dir")+"/DS/Log";
	private String tempPath=System.getProperty("user.dir")+"/"+"DS/Temp";
	private String licPath=System.getProperty("user.dir")+"/"+"DS/Lic";
	private String resCodePdfSign = null;
	
	@GetMapping(value="/_getCheck")
	public String test()
	{
		return applicationProperties.getEmasWsUrl();
	}
	
    @RequestMapping(value = "/_getTokenInput", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TokenInputResponse> token(@RequestBody  TokenRequest tokenRequest) throws DSCException  {
    	emBridge bridge = null;
    	File logFile=new File(logPath);
    	File licFile=new File(licPath);
    	String errorCode="";
		try {
			if (!logFile.exists()) {
	            logFile.mkdirs();
			}
			if (!licFile.exists()) {
				licFile.mkdirs();
			}
	            	
			bridge = new emBridge(licPath+"/OdishaUrban.lic",logFile.getCanonicalPath());
		} catch (IOException e) {
			errorCode=errorCode+"DSC_ERR_01 ";
			e.printStackTrace();
			throw new DSCException("Error in accessing log file or lic file");
		}
    	ListTokenRequest listTokenRequest = new ListTokenRequest();
    	listTokenRequest.setTokenStatus(Token_Status.CONNECTED);
    	listTokenRequest.setTokenType(Token_Type.HARDWARE); 
    	Request data = null;
		try {
			data = bridge.encListToken(listTokenRequest);
		} catch (Exception e) {
			errorCode=errorCode+"DSC_ERR_02";
			e.printStackTrace();
			throw new DSCException("Error in getting r1 and r2 for token from embridge");
		}
		System.out.println("data.getErrorCode() :::"+data.getErrorCode());
    	System.out.println("Encrypted Data :"+data.getEncryptedData());
    	System.out.println("Encrypted Key ID :"+data.getEncryptionKeyID());	

    	DetailRequestPojo input=new DetailRequestPojo();
    	input.setEncryptedRequest(data.getEncryptedData());
    	input.setEncryptionKeyId(data.getEncryptionKeyID());
    	input.setErrorCode(errorCode);
        return getSuccessTokenInputResponse(input, tokenRequest.getRequestInfo());

    }
    
    @RequestMapping(value = "/_getTokens", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TokenResponse> tokens(@RequestBody  TokenRequest tokenRequest) throws DSCException  {
    	List<String> tokens=new ArrayList<String>();
    	emBridge bridge = null;
    	File logFile=new File(logPath);
    	File licFile=new File(licPath);
    	String errorCode="";
		try {
			if (!logFile.exists()) {
	            logFile.mkdirs();
			}
			if (!licFile.exists()) {
				licFile.mkdirs();
			}
	            	
			bridge = new emBridge(licPath+"/OdishaUrban.lic",logFile.getCanonicalPath());
		} catch (IOException e) {
			errorCode=errorCode+"DSC_ERR_01 ";
			e.printStackTrace();
			throw new DSCException("Error in accessing log file or lic file");
		}

    	ResponseDataListProviderToken responseDataListProviderToken = bridge.decListToken(tokenRequest.getResponseData()); 
    	List<ProviderToken> tokens1 = responseDataListProviderToken.getTokens();
    	for(ProviderToken token : tokens1) {	
    		System.out.println(token.getKeyStoreDisplayName());
    		tokens.add(token.getKeyStoreDisplayName());
    	}
    	
        return getSuccessTokenResponse(tokens, tokenRequest.getRequestInfo(),errorCode);

    }
    
    @RequestMapping(value = "/_getInputCertificate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TokenInputResponse> cert(@RequestBody CertificateRequest certificateRequest) throws DSCException  {
    	emBridge bridge = null;
    	File logFile=new File(logPath);
    	File licFile=new File(licPath);
    	String errorCode="";
		try {
			if (!logFile.exists()) {
	            logFile.mkdirs();
			}
			if (!licFile.exists()) {
				licFile.mkdirs();
			}
	            	
			bridge = new emBridge(licPath+"/OdishaUrban.lic",logFile.getCanonicalPath());
		} catch (IOException e) {
			errorCode=errorCode+"DSC_ERR_01 ";
			e.printStackTrace();
			throw new DSCException("Error in accessing log file or lic file");
		}
		ListCertificateRequest listCertRequest = new ListCertificateRequest();
		listCertRequest.setKeyStoreDisplayName(certificateRequest.getTokenDisplayName());//Microsoft Windows Store//ePass V 
    	Request data = null;
		try {
			data = bridge.encListCertificate(listCertRequest);
		} catch (Exception e) {
			errorCode=errorCode+"DSC_ERR_03 ";
			e.printStackTrace();
			throw new DSCException("Error in getting r1 and r2 for certificate from embridge");
		}

    	System.out.println("Encrypted Data :"+data.getEncryptedData());
    	System.out.println("Encrypted Key ID :"+data.getEncryptionKeyID());	

    	DetailRequestPojo input=new DetailRequestPojo();
    	input.setEncryptedRequest(data.getEncryptedData());
    	input.setEncryptionKeyId(data.getEncryptionKeyID());
    	input.setErrorCode(errorCode);
    	
        return getSuccessTokenInputResponse(input, certificateRequest.getRequestInfo());
    }
    
    @RequestMapping(value = "/_getCertificate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CertificateResponse> certa(@RequestBody  CertificateRequest certificateRequest) throws DSCException  {
    	emBridge bridge = null;
    	File logFile=new File(logPath);
    	File licFile=new File(licPath);
    	String errorCode="";
		try {
			if (!logFile.exists()) {
	            logFile.mkdirs();
			}
			if (!licFile.exists()) {
	            licFile.mkdirs();
			}
			bridge = new emBridge(licPath+"/OdishaUrban.lic",logFile.getCanonicalPath());
		} catch (IOException e) {
			errorCode=errorCode+"DSC_ERR_01 ";
			e.printStackTrace();
			throw new DSCException("Error in accessing log file or lic file");
		}
    	
    	 ResponseDataListPKCSCertificate responseDataListPKCSCertificate = null;
		try {
			responseDataListPKCSCertificate = bridge.decListCertificate(certificateRequest.getResponseData(),null);
		} catch (Exception e) {
			errorCode=errorCode+"DSC_ERR_04 ";
			e.printStackTrace();
			throw new DSCException("Error in decrypting the list certificate");
		} 
    	 List<PKCSCertificate> certificates = responseDataListPKCSCertificate.getCertificates();
    	 List<CertificateResponsePojo> certificatesList=new ArrayList<CertificateResponsePojo>();
    	 CertificateResponsePojo certificate=null;
    	 for(PKCSCertificate cert : certificates) {
    				System.out.println("value of keyId :" + cert.getKeyId());
    				System.out.println("value of common name :" + cert.getCommonName());
    				certificate=new CertificateResponsePojo();
    				certificate.setKeyId(cert.getKeyId());
    				certificate.setCommonName(cert.getCommonName());
    				certificate.setCertificateDate(cert.getCertificateData());
    				certificatesList.add(certificate);
    	 }
    	
        return getSuccessCertResponse(certificatesList, certificateRequest.getRequestInfo(),errorCode);

    }
    
    @RequestMapping(value = "/_dataSignInput", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TokenInputResponse> dataSignInput(@RequestBody DataSignRequest dataSignRequest) throws DSCException  {
    	emBridge bridge = null;
    	File logFile=new File(logPath);
    	File licFile=new File(licPath);
    	String errorCode="";
    	String randomNumber="";
		try {
			if (!logFile.exists()) {
	            logFile.mkdirs();
			}
			if (!licFile.exists()) {
	            licFile.mkdirs();
			}
			bridge = new emBridge(licPath+"/OdishaUrban.lic",logFile.getCanonicalPath());
		} catch (IOException e) {
			errorCode=errorCode+"DSC_ERR_01 ";
			e.printStackTrace();
			throw new DSCException("Error in accessing log file or lic file");
		}
    	
		PKCSSignRequest pKCSSignRequest = new PKCSSignRequest();
		pKCSSignRequest.setKeyStoreDisplayName(dataSignRequest.getTokenDisplayName());//token
		pKCSSignRequest.setKeyStorePassphrase(dataSignRequest.getKeyStorePassPhrase());
		pKCSSignRequest.setKeyId(dataSignRequest.getKeyId());
		
		pKCSSignRequest.setDataType(ContentType.TextPKCS7ATTACHED); 
		
		// emas random number execute
		try
		{
			randomNumber=populateRandom(dataSignRequest.getRequestInfo().getUserInfo().getId(),dataSignRequest.getChannelId());
		}catch (DSCException e) {
			errorCode=errorCode+"DSC_ERR_06 ";
			throw new DSCException("Error in populating random number from Emas");
		}
		
		pKCSSignRequest.setDataToSign(dataSignRequest.getRequestInfo().getUserInfo().getId()+"~"+randomNumber);  
		pKCSSignRequest.setTimeStamp("");
		Request data = null;
		try {
			data = bridge.encPKCSSign(pKCSSignRequest);
		} catch (Exception e) {
			errorCode=errorCode+"DSC_ERR_05 ";
			e.printStackTrace();
			throw new DSCException("Error in getting r1 and r2 for data sign from embridge");
		}
	    System.out.println("Encrypted Data :"+data.getEncryptedData());
		System.out.println("Encrypted Key ID :"+data.getEncryptionKeyID());	
		DetailRequestPojo input=new DetailRequestPojo();
		input.setEncryptedRequest(data.getEncryptedData());
		input.setEncryptionKeyId(data.getEncryptionKeyID());
    	input.setErrorCode(errorCode);
        return getSuccessTokenInputResponse(input, dataSignRequest.getRequestInfo());
        
    }
    
    @RequestMapping(value = "/_dataSign", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<DataSignResponse> dataSign(@RequestBody  DataSignRequest dataSignRequest) throws DSCException  {
    	emBridge bridge = null;
    	File logFile=new File(logPath);
    	File licFile=new File(licPath);
    	String errorCode="";
    	String repsonse="";
		try {
			if (!logFile.exists()) {
	            logFile.mkdirs();
			}
			if (!licFile.exists()) {
	            licFile.mkdirs();
			}
			bridge = new emBridge(licPath+"/OdishaUrban.lic",logFile.getCanonicalPath());
		} catch (IOException e) {
			errorCode=errorCode+"DSC_ERR_01 ";
			e.printStackTrace();
			throw new DSCException("Error in accessing log file or lic file");
		}
		ResponseDataPKCSSign responseDataPKCSSign = null;
		try {
			responseDataPKCSSign = bridge.decPKCSSign(dataSignRequest.getResponseData());
		} catch (EMBLException e) {
			errorCode=errorCode+"DSC_ERR_07 ";
			e.printStackTrace();
			throw new DSCException("Error in accessing log file or lic file");
		}
		//register
		try
		{
			repsonse=populateRegisterSoapCall(dataSignRequest.getRequestInfo().getUserInfo().getId(),dataSignRequest.getChannelId(),responseDataPKCSSign.getSignedText());
		}catch (DSCException e) {
			errorCode=errorCode+"DSC_ERR_08 ";
			throw new DSCException("Error in register api from Emas");
		}
		
        return getSuccessDataSignResponse(repsonse, null,dataSignRequest.getRequestInfo(),errorCode);
        
    }

	private String populateRegisterSoapCall(Long id,String channel,String signedText) throws DSCException {
		  String result="";
		  DSAuthenticateWS authenticateWS = new DSAuthenticateWSProxy(applicationProperties.getEmasWsUrl());
		  try {
			result = authenticateWS.register(id+"~"+channel,signedText,"registration","registration",true);
			System.out.println("result after registration ::::"+result);
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new DSCException("Error in register api from Emas");
		}
		  return result;
	}
	
	private String populateRandom(Long id,String channel) throws DSCException {
		  String result="";
		  DSAuthenticateWS authenticateWS =null;
		  try {
		   authenticateWS = new DSAuthenticateWSProxy(applicationProperties.getEmasWsUrl());
			result = authenticateWS.generateRandomNumber (id+"~"+channel);
			result=result.split("~")[1];
			  System.out.println("after result ::::"+result);
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new DSCException("Error in populating random number from Emas");
		}
		  return result;
		  }

	private ResponseEntity<TokenResponse> getSuccessTokenResponse(List<String> tokens, RequestInfo requestInfo,String errorCode) {
        final ResponseInfo responseInfo = ResponseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());

        TokenResponse tokenResponse = new TokenResponse(responseInfo, tokens,errorCode);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }
	
	private ResponseEntity<TokenInputResponse> getSuccessTokenInputResponse(DetailRequestPojo input, RequestInfo requestInfo) {
        final ResponseInfo responseInfo = ResponseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());

        TokenInputResponse tokenResponse = new TokenInputResponse(responseInfo, input);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }
	
	
	private ResponseEntity<CertificateResponse> getSuccessCertResponse(List<CertificateResponsePojo> certs, RequestInfo requestInfo, String errorCode) {
        final ResponseInfo responseInfo = ResponseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());

        CertificateResponse certificateResponse = new CertificateResponse(responseInfo, certs,errorCode);
        return new ResponseEntity<>(certificateResponse, HttpStatus.OK);
    }
	
	private ResponseEntity<DataSignResponse> getSuccessDataSignResponse(String responseString,String fileStoreId,  RequestInfo requestInfo, String errorCode) {
        final ResponseInfo responseInfo = ResponseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());

        DataSignResponse datasign = new DataSignResponse(responseInfo, responseString,fileStoreId,errorCode);
        return new ResponseEntity<>(datasign, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/_pdfSignInput", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TokenInputResponse> dpdfSignInput(@RequestBody DataSignRequest dataSignRequest) throws DSCException  {
    	emBridge bridge = null;
    	File logFile=new File(logPath);
    	File licFile=new File(licPath);
    	File tempFile=new File(tempPath);
    	String errorCode="";
    	String pdfStr = "";
		try {
			if (!logFile.exists()) {
	            logFile.mkdirs();
			}
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
			if (!licFile.exists()) {
				licFile.mkdirs();
			}
			bridge = new emBridge(licPath+"/OdishaUrban.lic",logFile.getCanonicalPath());
		} catch (IOException e) {
			errorCode=errorCode+"DSC_ERR_01 ";
			e.printStackTrace();
			throw new DSCException("Error in accessing log file or lic file");
		}
		List<emBridgeSignerInput> inputs = new ArrayList<>();
		try
		{
		pdfStr = getPdfBytes(dataSignRequest.getFileBytes(), dataSignRequest.getTenantId());
		}catch (DSCException e) {
			errorCode=errorCode+"DSC_ERR_12 ";
			e.printStackTrace();
			throw new DSCException("Error in extracting the pdf file from filestore");
		}
		//emBridgeSignerInput input = new emBridgeSignerInput(pdfStr,dataSignRequest.getFileName(),applicationProperties.getPdfProperty1(), applicationProperties.getPdfProperty2(),dataSignRequest.getRequestInfo().getUserInfo().getName(),true, PageTobeSigned.All, Coordinates.BottomMiddle, applicationProperties.getPdfProprty4(), false);
		emBridgeSignerInput input = new emBridgeSignerInput(pdfStr,dataSignRequest.getFileName(), applicationProperties.getPdfProperty1(), applicationProperties.getPdfProperty2(), dataSignRequest.getRequestInfo().getUserInfo().getName(), true, "All-215,32,325,72", applicationProperties.getPdfProprty4(), false);
	    inputs.add(input);
		PKCSBulkPdfHashSignRequest pKCSBulkPdfHashSignRequest = new PKCSBulkPdfHashSignRequest();
		pKCSBulkPdfHashSignRequest.setBulkInput(inputs);
		try {
			pKCSBulkPdfHashSignRequest.setTempFolder(tempFile.getCanonicalPath());
		} catch (IOException e) {
			errorCode=errorCode+"DSC_ERR_09 ";
			e.printStackTrace();
			throw new DSCException("Error in accessing the canonical path of temp file");
		}
		pKCSBulkPdfHashSignRequest.setKeyStoreDisplayName(dataSignRequest.getTokenDisplayName());
		pKCSBulkPdfHashSignRequest.setKeyStorePassphrase(dataSignRequest.getKeyStorePassPhrase());
		pKCSBulkPdfHashSignRequest.setKeyId(dataSignRequest.getKeyId());
		Request bulkPKCSSignRequest = null;
		try {
			bulkPKCSSignRequest = bridge.encPKCSBulkSign(pKCSBulkPdfHashSignRequest);
		} catch (Exception e) {
			errorCode=errorCode+"DSC_ERR_10 ";
			e.printStackTrace();
			throw new DSCException("Error in encrypting PKCSBulkSign for pdf sign in embridge");
		}
		String tempFilePath = bulkPKCSSignRequest.getTempFilePath();
		System.out.println("Encrypted Data :" + bulkPKCSSignRequest.getEncryptedData());
		System.out.println("Encrypted Key ID :" + bulkPKCSSignRequest.getEncryptionKeyID());
		System.out.println("tempFilePath :" + tempFilePath);//if contains .sig
    	if(!(tempFilePath != null && tempFilePath.contains(".sig")))
    	{
    		errorCode=errorCode+"DSC_ERR_11 ";
    		throw new DSCException("Error in accessing .sig file from temp filelocation");
    	}
		DetailRequestPojo pojo=new DetailRequestPojo();
    	pojo.setEncryptedRequest(bulkPKCSSignRequest.getEncryptedData());
    	pojo.setEncryptionKeyId(bulkPKCSSignRequest.getEncryptionKeyID());
    	pojo.setFileName(dataSignRequest.getFileName());
    	pojo.setTempFilePath(tempFilePath);
    	pojo.setErrorCode(errorCode);
        return getSuccessTokenInputResponse(pojo, dataSignRequest.getRequestInfo());
        
    }
    
    private String getPdfBytes(String fileStoreId, String tenantId) throws DSCException {
    		String pdfStr = null;
    		
    		InputStream unsignedFileStrm = null;
    		File unsignedFile  = fetchAsDigitPath(fileStoreId, tenantId).toFile();
			
			try {
				unsignedFileStrm = new FileInputStream(unsignedFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new DSCException(e.getMessage());
			}
			byte[] pdfBytes = new byte[(int) unsignedFile.length()];
			try {
				unsignedFileStrm.read(pdfBytes, 0, pdfBytes.length);
			} catch (IOException e) {
				throw new DSCException(e.getMessage());
			}
			try {
				unsignedFileStrm.close();
			} catch (IOException e) {
				throw new DSCException(e.getMessage());
			}
			pdfStr = Base64.encodeBase64String(pdfBytes);
			System.out.println("pdfStr :::"+pdfStr);
			System.out.println("pdfStr.length:::::"+pdfStr.length());
    	return pdfStr;
	}

	@RequestMapping(value = "/_pdfSign", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<DataSignResponse> pdfSign(@RequestBody  DataSignRequest dataSignRequest) throws DSCException  {
    	emBridge bridge = null;
    	File logFile=new File(logPath);
    	File licFile=new File(licPath);
    	String tempFilePath = dataSignRequest.getTempFilePath();
    	File tempFile=new File(tempPath);
    	String errorCode="";
		try {
			if (!logFile.exists()) {
	            logFile.mkdirs();
			}
			if (!licFile.exists()) {
	            licFile.mkdirs();
			}
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
	            	
			bridge = new emBridge(licPath+"/OdishaUrban.lic",logFile.getCanonicalPath());
		} catch (IOException e) {
			errorCode=errorCode+"DSC_ERR_01 ";
			e.printStackTrace();
			throw new DSCException("Error in accessing log file or lic file");
		}
		ResponseDataPKCSBulkSign apiResponse = bridge.decPKCSBulkSign(dataSignRequest.getResponseData(),tempFilePath);
		
		System.out.println("apiResponse.getErrorCode() - " +apiResponse.getErrorCode());
		String fileId = null;
		try {
			fileId = populateSignedPdfFileStoreId(apiResponse,tempFilePath,dataSignRequest.getFileName(),dataSignRequest.getRequestInfo().getUserInfo().getId(),dataSignRequest.getTenantId(),dataSignRequest.getModuleName(), dataSignRequest.getChannelId());
		} catch (DSCException e) {
			errorCode=errorCode+"DSC_ERR_13 ";
			e.printStackTrace();
			throw new DSCException("Error creating the signed pdf in filestore after pdf sign");
		}
		String result="";
		if(fileId != null && !fileId.isEmpty() && !fileId.equalsIgnoreCase("0"))
		{
			result="Success";
			System.out.println("fileID - "+fileId);
			System.out.println("result - "+result);
		}
		else
		{
			fileId=null;
			result=resCodePdfSign;
			System.out.println("fileID - "+fileId);
			System.out.println("result - "+result);
		}
    	
        return getSuccessDataSignResponse(result,fileId,dataSignRequest.getRequestInfo(),errorCode);
        
    }

	private String populateSignedPdfFileStoreId(ResponseDataPKCSBulkSign apiResponse, String serverTempPath,
			String fileName, Long userId,String tenantId, String moduleName, String channelId) throws DSCException  {
		String fileStoreId="0";
		File file =null;
		File tempFile = new File(serverTempPath);
		try {
			if(apiResponse != null) {
				resCodePdfSign = apiResponse.getErrorCode();
				for (BulkSignOutput doc : apiResponse.getBulkSignItems()) {
					System.out.println("doc.signedData::::: " + doc.getSignedData());
					if(!checkPdfAuthentication(String.valueOf(userId),doc.getSignedData(),channelId))
					{
						break;
					}
				
					byte[] signedDocBytes = Base64.decodeBase64(doc.getSignedData());
					String finalFilePath = tempPath+"\\"+fileName+"_signed.pdf";
					 file = new File(finalFilePath);
					OutputStream os = new FileOutputStream(file);
					os.write(signedDocBytes);
					os.close();
				}
			}
			if(file != null) {
				fileStoreId=store(new FileInputStream(file),file.getName(),"application/pdf",moduleName,true,tenantId);
				file.delete();
			}
			tempFile.delete();
		}
		catch(Exception e) {
			resCodePdfSign = apiResponse.getErrorCode();
			tempFile.delete();
			e.printStackTrace();
			throw new DSCException(e.getMessage());
		}
		return fileStoreId;
	}

	public String store(InputStream fileStream, String fileName, String mimeType, String moduleName,
            boolean closeStream,String tenantId) {
		String fileStoreId=null;
        try {          
            byte[] fileSize = fileName.getBytes();
            DiskFileItem fileItem = new DiskFileItem("file",mimeType, false, fileName, fileSize.length, null);
            OutputStream os = fileItem.getOutputStream();
            int ret = fileStream.read();
            while ( ret != -1 )
            {
                os.write(ret);
                ret = fileStream.read();
            }
            os.flush();
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            fileStoreId = storeFiles(Arrays.asList(multipartFile),
                    fileName,
                    mimeType, moduleName,false,tenantId);
        } catch (IOException e) {
        	resCodePdfSign = "Exe-FileStore";
        	e.printStackTrace();
        }
        return fileStoreId;
    }
	
	private String storeFiles(List<MultipartFile> files, String fileName, String mimeType, String moduleName,
            boolean deleteFile,String tenantId) {
			String fileStoreId=null;
        try {
            StorageResponse storageRes = getFileStorageService(files, moduleName,tenantId);

            List<FileReq> filesList = storageRes.getFiles();
            for (FileReq filesId : filesList) {
            	fileStoreId=filesId.getFileStoreId();
            }
        } catch (IOException e) {
        	resCodePdfSign = "Exe-FileStore";
        	e.printStackTrace();
        }
        return fileStoreId;
    }
	
	public StorageResponse getFileStorageService(final List<MultipartFile> files, String modulename,String tenantId)
	        throws IOException {
	    HttpHeaders headers = new HttpHeaders();
	    headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
	    map.add("module", modulename);
	    map.add("tenantId", tenantId);

	    ByteArrayResource contentsAsResource = null;
	    for (MultipartFile file : files) {
	        contentsAsResource = new ByteArrayResource(file.getBytes()) {
	            @Override
	            public String getFilename() {
	                return file.getOriginalFilename();
	            }
	        };
	    };
	    map.add("file", contentsAsResource);
	    //map.add("file", contentsAsResource);
	   
	    StringBuilder uri = new StringBuilder(applicationProperties.getEgovFileStoreSerHost())
	            .append(applicationProperties.getEgovFileStoreUploadFile());

	    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

	    return restTemplate.postForObject(uri.toString(), request, StorageResponse.class);
	}

	private boolean checkPdfAuthentication(String userId, String signedData, String channelId) throws DSCException {
		boolean result=false;
		DSAuthenticateWS authenticateWS = new DSAuthenticateWSProxy(applicationProperties.getEmasWsUrl());
		String authenticatePDF =null;
			String uniqueId = userId+"~"+channelId;
			try {
				authenticatePDF = authenticateWS.authenticatePDF(uniqueId, signedData , null, "authenticate");
			} catch (RemoteException e) {
				e.printStackTrace();
				throw new DSCException(e.getMessage());
			}
			System.out.println("authenticatePDF:: "+authenticatePDF);
		
			if(authenticatePDF != null && !authenticatePDF.isEmpty() && (authenticatePDF.contains("Success")|| authenticatePDF.contains("success")))
			{
				result=true;
			}
			else if(authenticatePDF != null && !authenticatePDF.isEmpty()){
				resCodePdfSign = authenticatePDF.split("^")[0];
			}
		return result;
	}

	private Path fetchAsDigitPath(String fileStoreId, String tenantId) throws DSCException {
        ResponseEntity<byte[]> responseEntity = fetchFilesFromDigitService(fileStoreId, tenantId);
        Path fileDirPath = Paths.get(fileStoreId);
        Path path = null;
        try {
            path = Files.write(fileDirPath, responseEntity.getBody());
        } catch (IOException e) {
            throw new DSCException("Error in extracting pdf from filestore");
        }
        return path;

    }
	
	public ResponseEntity<byte[]> fetchFilesFromDigitService(String fileStoreId, String tenantId) {
	    Map<String, String> request = new HashMap<String, String>();
	    request.put("tenantId", tenantId);
	    request.put("fileStoreId", fileStoreId);
	    
	    StringBuilder uri = new StringBuilder(applicationProperties.getEgovFileStoreSerHost())
	            .append(applicationProperties.getEgovFileStoreDownloadFile()).append("?");

	    if (tenantId != null && !tenantId.isEmpty()) {
	        uri.append("tenantId=").append(tenantId);
	    }
	    if (fileStoreId != null && !fileStoreId.isEmpty()) {
	        uri.append("&fileStoreId=").append(fileStoreId);
	    }
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
	    HttpEntity<String> entity = new HttpEntity<>(headers);
	    ResponseEntity<byte[]> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, byte[].class);
	    return response;
	}

}
