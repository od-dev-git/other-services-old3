package org.egov.dx.web.models.MR;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.IssuedBy;
import org.egov.dx.web.models.IssuedTo;

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
@EqualsAndHashCode
@XStreamAlias("Certificate")
public class MrCertificate {

	@XStreamAlias("IssuedBy")
    private IssuedBy issuedBy;
	
	@XStreamAlias("IssuedTo")
    private MRIssuedTo issuedTo;
	
	@XStreamAlias("CertificateData")
    private MrCertificateData certificateData;

    @XmlAttribute
    @XStreamAlias("language")
    private String language;
    
    @XmlAttribute
    @XStreamAlias("name")
    private String name;
    
    @XmlAttribute
    @XStreamAlias("type")
    private String type;
    
    @XmlAttribute
    @XStreamAlias("date")
    private String date;
    
    @XmlAttribute
    @XStreamAlias("mobile")
    private String mobile;
    
    @XmlAttribute
    @XStreamAlias("Application_no")
    private String applicationNo;
	
}
