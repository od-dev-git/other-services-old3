package org.egov.dx.web.models.MR;

import javax.xml.bind.annotation.XmlAttribute;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@XStreamAlias("husband")
public class Husband {
	
	@XmlAttribute
    @XStreamAlias("name")
    private String name;
	
	@XmlAttribute
    @XStreamAlias("residingAt")
    private String residingAt;

}
