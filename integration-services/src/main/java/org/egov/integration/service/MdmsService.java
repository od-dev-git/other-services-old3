package org.egov.integration.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.integration.config.IntegrationConfiguration;
import org.egov.integration.repository.ServiceRepository;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MdmsService {
	
	@Autowired
	private IntegrationConfiguration integrationConfiguration;

	@Autowired
    private ServiceRepository serviceRepository;
	
	public Object mDMSCall(RequestInfo requestInfo, String tenantId){
        MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo,tenantId);
        StringBuilder url = getMdmsSearchUrl();
        Object result = serviceRepository.fetchResult(url,mdmsCriteriaReq);
        return result;
    }


    /**
     * Creates MDMS request
     * @param requestInfo The RequestInfo of the Payment
     * @param tenantId The tenantId of the Payment
     * @return MDMSCriteria Request
     */
    private MdmsCriteriaReq getMDMSRequest(RequestInfo requestInfo, String tenantId) {

        // master details for Collection module
        List<MasterDetail> securityTokenMasterDetails = new ArrayList<>();

        securityTokenMasterDetails.add(MasterDetail.builder().name("SecurityToken").build());

        ModuleDetail dataSecurityModuleDtls = ModuleDetail.builder().masterDetails(securityTokenMasterDetails)
                .moduleName("DataSecurity").build();

        List<ModuleDetail> moduleDetails = new ArrayList<>();
        moduleDetails.add(dataSecurityModuleDtls);

        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId)
                .build();

        return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
    }


    /**
     * Creates and returns the url for mdms search endpoint
     *
     * @return MDMS Search URL
     */
    private StringBuilder getMdmsSearchUrl() {
        return new StringBuilder().append(integrationConfiguration.getMdmsHost()).append(integrationConfiguration.getMdmsEndpoint());
    }




}
