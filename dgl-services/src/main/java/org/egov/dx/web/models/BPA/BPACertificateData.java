package org.egov.dx.web.models.BPA;

import org.egov.dx.web.models.CertificateForData;
import org.egov.dx.web.models.MR.MarriageCertificate;
import org.egov.dx.web.models.MR.MrCertificateData;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XStreamAlias("CertificateData")
public class BPACertificateData {
	
	@XStreamAlias("Certificate")
    private CertificateForData certificate;
    
    @XStreamAlias("BuildingPlan")
    private BuildingPlanCertificate buildingPlanCertificate;

}
