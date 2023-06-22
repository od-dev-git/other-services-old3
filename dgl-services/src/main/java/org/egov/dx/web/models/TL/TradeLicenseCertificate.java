package org.egov.dx.web.models.TL;

import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.Address;
import org.egov.dx.web.models.MR.AddressMR;
import org.egov.dx.web.models.MR.MarriageCertificate;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XStreamAlias("TradeLicense")
public class TradeLicenseCertificate {
	
	@XmlAttribute
    @XStreamAlias("municipalLicenceNo")
	private String municipalLicenceNo;
	
	@XmlAttribute
    @XStreamAlias("newLicenceNo")
	private String newLicenceNo;
	
	@XmlAttribute
    @XStreamAlias("unitName")
	private String unitName;
	
	@XmlAttribute
    @XStreamAlias("validUpto")
	private String validUpto;
	
	@XmlAttribute
    @XStreamAlias("Address")
	private Address address;

	@XmlAttribute
    @XStreamAlias("pan")
	private Pan pan;
	
	@XmlAttribute
    @XStreamAlias("propertyDetails")
	private PropertyDetails propertyDetails;
	
	@XmlAttribute
    @XStreamAlias("Licensee")
	private Licensee licensee;
	
	@XmlAttribute
    @XStreamAlias("BusinessProfession")
	private BusinessProfession businessProfession;

}
