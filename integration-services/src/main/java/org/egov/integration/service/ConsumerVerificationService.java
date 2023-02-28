package org.egov.integration.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.integration.model.ConsumerVerification;
import org.egov.integration.model.ConsumerVerificationOwner;
import org.egov.integration.model.ConsumerVerificationSearchCriteria;
import org.egov.integration.web.model.ConsumerVerificationResponse;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsumerVerificationService {
	
	public List<ConsumerVerification> search(ConsumerVerificationSearchCriteria searchCriteria) {

	ConsumerVerification response = ConsumerVerification.builder()
			.consumerNo(null)
			.consumerVerificationOwner(null)
			.tenantId(null)
			.status(null)
			.build(); 	
	ConsumerVerificationOwner ownerinfo = ConsumerVerificationOwner.builder()
		.ownerName("Amitabh").address("Rajori Garden , Lal Chowk").build();
    String businessService = searchCriteria.getBusinessService();
    
    switch(businessService){
	case "BPA":
		response = ConsumerVerification.builder().consumerNo("DCR72021ISSU0").businessService(businessService).consumerVerificationOwner(Arrays.asList(ownerinfo))
				.tenantId("Cuttack").status("INACTIVE").build();
		break;
    case "MR" :
    	response = ConsumerVerification.builder().consumerNo("MR/CTC/000011").businessService(businessService).consumerVerificationOwner(null)
		.tenantId("Cuttack").status("ACTIVE").build();
    	break;
    case "TL" :
    	ConsumerVerificationOwner ownerinfotl = ConsumerVerificationOwner.builder()
		.ownerName("Shahrukh").address("Shop No 68 ,Near Udyog Vihar ").build();
    	response = ConsumerVerification.builder().consumerNo("TL/CTC/000219").businessService(businessService).consumerVerificationOwner(Arrays.asList(ownerinfotl))
		.tenantId("Cuttack").status("INACTIVE").build();
    	break;
    	
    case "WS" :
    	response = ConsumerVerification.builder().consumerNo("WS/BMC/1908399").businessService(businessService).consumerVerificationOwner(null)
		.tenantId("Bhubaneswar").status("ACTIVE").build();
    	break;
    	
    case "PT" :
    	ConsumerVerificationOwner ownerinfotpt = ConsumerVerificationOwner.builder()
		.ownerName("Charan").address("29-d , Arya Nagar , Sundargarh Colony ").build();
    	response = ConsumerVerification.builder().consumerNo("PT-SNG-689867").businessService(businessService).consumerVerificationOwner(Arrays.asList(ownerinfotpt))
		.tenantId("Sundargarh").status("INACTIVE").build();
    	
    
    }

		return Arrays.asList(response);
	}

}
