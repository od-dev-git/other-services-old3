package org.egov.dx.web.models.TL;

import org.egov.dx.web.models.CertificateForData;
import org.egov.dx.web.models.MR.MarriageCertificate;
import org.egov.dx.web.models.MR.MrCertificateData;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TLCertificateData {

	@XStreamAlias("Certificate")
    private CertificateForData certificate;
    
    @XStreamAlias("TLCertificate")
    private TradeLicenseCertificate tlCertificate;
	
}
