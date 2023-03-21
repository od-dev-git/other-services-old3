package org.egov.dss.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.egov.dss.model.PayloadDetails;
import org.egov.dss.model.PropertySerarchCriteria;
import org.egov.dss.repository.PTRepository;
import org.egov.dss.web.model.ChartCriteria;
import org.egov.dss.web.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

@Service
public class PTService {
	
	@Autowired
	private PTRepository ptRepository;

	public List<Data> totalProprties(PayloadDetails payloadDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> propertiesPaid(PayloadDetails payloadDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> propertiesAssessed(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = getPropertySearchCriteria(payloadDetails);
		Integer assessedPropertiesCount =  (Integer) ptRepository.getAssessedPropertiesCount(criteria);
		return Arrays.asList(Data.builder().headerValue(assessedPropertiesCount).build());
	}

	public List<Data> activeUlbs(PayloadDetails payloadDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> totalMutationProperties(PayloadDetails payloadDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> ptTotalApplications(PayloadDetails payloadDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> totalnoOfProperties(PayloadDetails payloadDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> cumulativePropertiesAssessed(PayloadDetails payloadDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> topPerformingUlbsCompletionRate(PayloadDetails payloadDetails) {
		// TODO Auto-generated method stub
		return null;
	}
    
	private PropertySerarchCriteria getPropertySearchCriteria(PayloadDetails payloadDetails) {
		PropertySerarchCriteria criteria = new PropertySerarchCriteria();

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
