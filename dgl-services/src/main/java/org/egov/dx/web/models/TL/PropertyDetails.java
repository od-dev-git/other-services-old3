package org.egov.dx.web.models.TL;

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
@XStreamAlias("propertyDetails")
public class PropertyDetails {

	@XmlAttribute
    @XStreamAlias("assessed")
	private String assessed;
	
	@XmlAttribute
    @XStreamAlias("buid")
	private String buid;
	
	@XmlAttribute
    @XStreamAlias("address")
	private String address;
}
