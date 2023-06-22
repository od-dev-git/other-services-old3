package org.egov.dx.web.models.MR;

import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.Address;
import org.egov.dx.web.models.Organization;

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
@XStreamAlias("Person")
public class PersonMR {
	
	@XmlAttribute
    @XStreamAlias("title")
    private String title;
    
    @XmlAttribute
    @XStreamAlias("name")
    private String name;
    
    @XmlAttribute
    @XStreamAlias("dob")
    private String dob;
    
    @XmlAttribute
    @XStreamAlias("gender")
    private String gender;
    
    @XmlAttribute
    @XStreamAlias("maritalStatus")
    private String maritalStatus;
    
    @XmlAttribute
    @XStreamAlias("religion")
    private String religion;
    
    @XmlAttribute
    @XStreamAlias("Address")
    private Address address;
   
}
