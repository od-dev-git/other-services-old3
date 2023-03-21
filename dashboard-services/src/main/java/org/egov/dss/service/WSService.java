package org.egov.dss.service;

import java.util.Arrays;
import java.util.List;

import org.egov.dss.constants.DashboardConstants;
import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.WaterSearchCriteria;
import org.egov.dss.repository.WSRepository;
import org.egov.dss.web.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class WSService {
	
	@Autowired
	private WSRepository wsRepository;
   
	public List<Data> totalActiveConnections(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = getWaterSearchCriteria(payloadDetails);
		criteria.setStatus(DashboardConstants.WS_CONNECTION_ACTIVATED);
		criteria.setExcludedTenantId(DashboardConstants.TESTING_TENANT);
		criteria.setIsOldApplication(Boolean.FALSE);
		Integer activeConnectionCount =  (Integer) wsRepository.getActiveWaterConnectionCount(criteria);
		return Arrays.asList(Data.builder().headerValue(activeConnectionCount).build());
	}
	
	private WaterSearchCriteria getWaterSearchCriteria(PayloadDetails payloadDetails) {
		WaterSearchCriteria criteria = new WaterSearchCriteria();

		if (StringUtils.hasText(payloadDetails.getModulelevel())) {
			criteria.setBusinessServices(Sets.newHashSet(payloadDetails.getModulelevel()));
		}
		
		if(StringUtils.hasText(payloadDetails.getTenantid())) {
			criteria.setTenantIds(Sets.newHashSet(payloadDetails.getTenantid()));
		}
		
		if(payloadDetails.getStartdate() != null && payloadDetails.getStartdate() != 0) {
			criteria.setFromDate(payloadDetails.getStartdate());
		}
		
		if(payloadDetails.getEnddate() != null && payloadDetails.getEnddate() != 0) {
			criteria.setToDate(payloadDetails.getEnddate());
		}
		
		return criteria;
	}

}
