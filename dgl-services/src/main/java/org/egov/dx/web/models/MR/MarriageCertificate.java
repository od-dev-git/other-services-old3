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
@XStreamAlias("MarriageCertificate")
public class MarriageCertificate {
	
	@XmlAttribute
    @XStreamAlias("mrNumber")
	private String mrNumber;
	
	@XmlAttribute
    @XStreamAlias("applicationNumber")
	private String applicationNumber;
	
	@XmlAttribute
    @XStreamAlias("marriageDate")
	private Long marriageDate;
	
	@XmlAttribute
    @XStreamAlias("applicationDate")
	private Long applicationDate;
	
	@XmlAttribute
    @XStreamAlias("brideDOB")
	private Long brideDOB;

	@XmlAttribute
    @XStreamAlias("brideGroomDOB")
	private Long brideGroomDOB;
	
	@XmlAttribute
    @XStreamAlias("brideGuardianName")
	private String brideGuardianName;
	
	@XmlAttribute
    @XStreamAlias("brideGuardianAddress")
	private AddressMR brideGuardianAddress;
	
	@XmlAttribute
    @XStreamAlias("brideGroomGuardianName")
	private String brideGroomGuardianName;
	
	@XmlAttribute
    @XStreamAlias("brideGroomGuardianAddress")
	private AddressMR brideGroomGuardianAddress;
	
	@XmlAttribute
    @XStreamAlias("brideGroomWitnessName")
	private String brideGroomWitnessName;
	
	@XmlAttribute
    @XStreamAlias("brideGroomWitnessAddress")
	private String brideGroomWitnessAddress;
	
	@XmlAttribute
    @XStreamAlias("brideWitnessName")
	private String brideWitnessName;
	
	@XmlAttribute
    @XStreamAlias("brideWitnessAddress")
	private String brideWitnessAddress;
	
}
