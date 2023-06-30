package org.egov.dx.web.models;

import javax.xml.bind.annotation.XmlAttribute;

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

public class CertificateForData {
	
	@XStreamAsAttribute
    @XStreamAlias("number")
    private String number;
    
	@XStreamAsAttribute
    @XStreamAlias("place")
    private String place;
	
	@XStreamAsAttribute
    @XStreamAlias("date")
    private String date;
    
}
