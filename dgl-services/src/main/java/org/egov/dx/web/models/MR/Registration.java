package org.egov.dx.web.models.MR;

import javax.xml.bind.annotation.XmlAttribute;

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
@EqualsAndHashCode
@XStreamAlias("registration")
public class Registration {
	
	@XmlAttribute
    @XStreamAlias("number")
    private String number;
	
	@XmlAttribute
    @XStreamAlias("date")
    private String date;

}
