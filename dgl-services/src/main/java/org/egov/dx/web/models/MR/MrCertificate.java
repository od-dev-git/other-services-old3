package org.egov.dx.web.models.MR;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.IssuedBy;
import org.egov.dx.web.models.IssuedTo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@XStreamAlias("Certificate")
public class MrCertificate {

	@XStreamAlias("IssuedBy")
    private IssuedBy issuedBy;
	
	@XStreamAlias("IssuedTo")
    private MRIssuedTo issuedTo;
	
	@XStreamAlias("CertificateData")
    private MrCertificateData certificateData;

	@XStreamAsAttribute
    @XStreamAlias("language")
    private String language;
    
	@XStreamAsAttribute
    @XStreamAlias("name")
    private String name;
    
	@XStreamAsAttribute
    @XStreamAlias("type")
    private String type;
    
	@XStreamAsAttribute
    @XStreamAlias("date")
    private String date;
    
	@XStreamAsAttribute
    @XStreamAlias("mobile")
    private String mobile;
    
	@XStreamAsAttribute
    @XStreamAlias("Application_no")
    private String applicationNo;
	
}
