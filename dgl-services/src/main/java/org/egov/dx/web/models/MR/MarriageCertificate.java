package org.egov.dx.web.models.MR;

import javax.xml.bind.annotation.XmlAttribute;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XStreamAlias("Marriage")
public class MarriageCertificate {
	
	@XmlAttribute
    @XStreamAlias("place")
	private String place;
	
	@XmlAttribute
    @XStreamAlias("solemnizedDate")
	private String solemnizedDate;
	
	@XmlAttribute
    @XStreamAlias("registration")
	private Registration registration;
	
	@XmlAttribute
    @XStreamAlias("ward")
	private Ward ward;
	
	@XmlAttribute
    @XStreamAlias("husband")
	private Husband husband;

	@XmlAttribute
    @XStreamAlias("wife")
	private Wife wife;
	
}
