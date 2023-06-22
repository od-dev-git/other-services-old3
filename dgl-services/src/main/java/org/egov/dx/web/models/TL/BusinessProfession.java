package org.egov.dx.web.models.TL;

import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.Address;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XStreamAlias("BusinessProfession")
public class BusinessProfession {
	
	@XmlAttribute
    @XStreamAlias("name")
	private String name;
	
	@XmlAttribute
    @XStreamAlias("licenseFee")
	private String licenseFee;
	
	@XmlAttribute
    @XStreamAlias("sdFee")
	private String sdFee;
	
	@XmlAttribute
    @XStreamAlias("scrunityFee")
	private String scrunityFee;
	
	@XmlAttribute
    @XStreamAlias("penalty")
	private String penalty;
	
	@XmlAttribute
    @XStreamAlias("challanNo")
	private String challanNo;
	
	@XmlAttribute
    @XStreamAlias("year")
	private String year;

}
