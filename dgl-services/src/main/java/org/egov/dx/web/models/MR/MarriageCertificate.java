package org.egov.dx.web.models.MR;

import javax.xml.bind.annotation.XmlAttribute;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

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
	
	@XStreamAsAttribute
    @XStreamAlias("place")
	private String place;
	
	@XStreamAsAttribute
    @XStreamAlias("solemnizedDate")
	private String solemnizedDate;
	
	@XmlAttribute
    @XStreamAlias("registration")
	private Registration registration;
	
	@XmlAttribute
    @XStreamAlias("ward")
	private Ward ward;
	
	@XmlAttribute
    @XStreamAlias("Husband")
	private Husband husband;

	@XmlAttribute
    @XStreamAlias("husband")
	private HusbandResiding husbandResiding;
	
	@XmlAttribute
    @XStreamAlias("wife")
	private Wife wife;

	@XmlAttribute
    @XStreamAlias("wife")
	private WifeResiding wifeResiding;
	
}
