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
@XStreamAlias("TradeLicenseCertificate")
public class TradeLicenseCertificate {
	
	@XmlAttribute
    @XStreamAlias("licenseNumber")
	private String licenseNumber;
	
	@XmlAttribute
    @XStreamAlias("applicationNumber")
	private String applicationNumber;
	
	@XmlAttribute
    @XStreamAlias("validFrom")
	private Long validFrom;
	
	@XmlAttribute
    @XStreamAlias("validTo")
	private Long validTo;
	
	@XmlAttribute
    @XStreamAlias("tradeName")
	private String tradeName;

	@XmlAttribute
    @XStreamAlias("tradeOwner")
	private String tradeOwner;
	
	@XmlAttribute
    @XStreamAlias("tradeAddress")
	private AddressTL tradeAddress;
	
	@XmlAttribute
    @XStreamAlias("tradeType")
	private String tradeType;
	
	@XmlAttribute
    @XStreamAlias("tradePurpose")
	private String tradePurpose;
	
	@XmlAttribute
    @XStreamAlias("tradeFee")
	private String tradeFee;
	
	@XmlAttribute
    @XStreamAlias("licenseIssueDate")
	private Long licenseIssueDate;

}
