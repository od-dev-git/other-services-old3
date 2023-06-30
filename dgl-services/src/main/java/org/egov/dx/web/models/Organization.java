package org.egov.dx.web.models;

import javax.xml.bind.annotation.XmlAttribute;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@XStreamAlias("Organization")

public class Organization {
	
	@XStreamAsAttribute
    @XStreamAlias("name")
    private String name;
    
	@XStreamAsAttribute
    @XStreamAlias("code")
    private String code="";
    
	@XStreamAsAttribute
    @XStreamAlias("tin")
    private String tin="";
    
	@XStreamAsAttribute
    @XStreamAlias("uid")
    private String uid;
    
	@XStreamAsAttribute
    @XStreamAlias("type")
    private String type="SG";
	
	@XmlAttribute
    @XStreamAlias("Address")
    private Address address;
}
