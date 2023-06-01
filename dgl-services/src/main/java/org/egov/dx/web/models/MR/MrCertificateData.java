package org.egov.dx.web.models.MR;

import org.egov.dx.web.models.CertificateForData;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XStreamAlias("CertificateData")
public class MrCertificateData {
	
	@XStreamAlias("Certificate")
    private CertificateForData certificate;
    
    @XStreamAlias("MarriageCertificate")
    private MarriageCertificate marriageCertificate;

}
