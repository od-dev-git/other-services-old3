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
@XStreamAlias("pan")
public class Pan {
	
	@XStreamAsAttribute
    @XStreamAlias("number")
	private String number;
	
	@XStreamAsAttribute
    @XStreamAlias("name")
	private String name;

}
