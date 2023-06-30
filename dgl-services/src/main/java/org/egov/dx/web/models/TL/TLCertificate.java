package org.egov.dx.web.models.TL;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.IssuedBy;
import org.egov.dx.web.models.IssuedTo;
import org.egov.dx.web.models.MR.MrCertificate;
import org.egov.dx.web.models.MR.MrCertificateData;

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
public class TLCertificate {
	
	@XStreamAlias("IssuedBy")
    private IssuedBy issuedBy;
	
	@XStreamAlias("IssuedTo")
    private TLIssuedTo issuedTo;
	
	@XStreamAlias("CertificateData")
    private TLCertificateData certificateData;
	
    @Size(max=64)
    @XmlAttribute
    @XStreamAlias("Signature")
    private String signature;

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
    @XStreamAlias("number")
    private String number;
    
    @XStreamAsAttribute
    @XStreamAlias("prevNumber")
    private String prevNumber;
    
    @XStreamAsAttribute
    @XStreamAlias("expiryDate")
    private String expiryDate;
    
    @XStreamAsAttribute
    @XStreamAlias("validFromDate")
    private String validFromDate;
    
    @XStreamAsAttribute
    @XStreamAlias("issuedAt")
    private String issuedAt;
    
    @XStreamAsAttribute
    @XStreamAlias("issueDate")
    private String issueDate;
    
    @XStreamAsAttribute
    @XStreamAlias("status")
    private String status;

}
