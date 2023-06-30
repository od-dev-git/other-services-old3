package org.egov.dx.web.models.TL;

import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.Address;

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
@XStreamAlias("BusinessProfession")
public class BusinessProfession {
	
	@XStreamAsAttribute
    @XStreamAlias("name")
	private String name;
	
	@XStreamAsAttribute
    @XStreamAlias("licenseFee")
	private String licenseFee;
	
	@XStreamAsAttribute
    @XStreamAlias("sdFee")
	private String sdFee;
	
	@XStreamAsAttribute
    @XStreamAlias("scrunityFee")
	private String scrunityFee;
	
	@XStreamAsAttribute
    @XStreamAlias("penalty")
	private String penalty;
	
	@XStreamAsAttribute
    @XStreamAlias("challanNo")
	private String challanNo;
	
	@XStreamAsAttribute
    @XStreamAlias("year")
	private String year;

}
