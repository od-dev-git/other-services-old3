package org.egov.dx.web.models.TL;

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
@XStreamAlias("propertyDetails")
public class PropertyDetails {

	@XStreamAsAttribute
    @XStreamAlias("assessed")
	private String assessed;
	
	@XStreamAsAttribute
    @XStreamAlias("buid")
	private String buid;
	
	@XStreamAsAttribute
    @XStreamAlias("address")
	private String address;
}
