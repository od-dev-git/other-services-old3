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
@XStreamAlias("Licensee")
public class Licensee {
	
	@XStreamAsAttribute
    @XStreamAlias("name")
	private String name;
	
	@XStreamAsAttribute
    @XStreamAlias("fatherHusbandName")
	private String fatherHusbandName;

	@XStreamAsAttribute
    @XStreamAlias("address")
	private String address;
}
