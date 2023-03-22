package org.egov.dss.service;

import java.util.Arrays;
import java.util.List;

import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.PgrSearchCriteria;
import org.egov.dss.repository.PGRRepository;
import org.egov.dss.web.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
	
	@Autowired
	private PGRService pgrService;
	
	@Autowired
	private PTService ptService;
	
	@Autowired
	private WSService wsService;
	
	@Autowired
	private TLService tlService;
	
	@Autowired
	private MRService mrService;
	
	@Autowired
	private BPAService bpaService;
	
	public List<Data> slaAchieved(PayloadDetails payloadDetails) {
		 payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
	     Double pgrSla =	(Double) pgrService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
	     Double ptSla =	(Double) ptService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
	     Double bpaSla =	(Double) bpaService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
	     Double wsSla =	(Double) wsService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
	     Double tlSla =	(Double) tlService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
	     Double mrSla =	(Double) mrService.slaAchieved(payloadDetails).get(0).getHeaderValue();
	     Double slaAchieved = (pgrSla+ptSla+bpaSla+wsSla+tlSla+mrSla)/6;
	     
		return Arrays.asList(Data.builder()
				.headerValue(slaAchieved).build());
	}
	
	public List<Data> totalApplication(PayloadDetails payloadDetails) {
		 payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PGR);
	     Integer pgrSla =	(Integer) pgrService.totalApplications(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_PT);
	     Integer ptSla =	(Integer) ptService.propertiesAssessed(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.MODULE_LEVEL_OBPS);
	     Integer bpaSla =	(Integer) bpaService.totalPermitIssued(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_WS);
	     Integer wsSla =	(Integer) wsService.totalActiveConnections(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_TL);
	     Integer tlSla =	(Integer) tlService.totalApplications(payloadDetails).get(0).getHeaderValue();
	     payloadDetails.setModulelevel(DashboardConstants.BUSINESS_SERVICE_MR);
	     Integer mrSla =	(Integer) mrService.totalApplications(payloadDetails).get(0).getHeaderValue();
	     Integer totalApplications = (pgrSla+ptSla+bpaSla+wsSla+tlSla+mrSla)/5;
	     
		return Arrays.asList(Data.builder()
				.headerValue(totalApplications).build());
	}

}
