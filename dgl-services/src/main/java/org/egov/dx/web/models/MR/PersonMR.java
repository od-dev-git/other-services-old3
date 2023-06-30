package org.egov.dx.web.models.MR;

import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.Address;
import org.egov.dx.web.models.Organization;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

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
	
	@XStreamAsAttribute
    @XStreamAlias("title")
    private String title;
    
	@XStreamAsAttribute
    @XStreamAlias("name")
    private String name;
    
	@XStreamAsAttribute
    @XStreamAlias("dob")
    private String dob;
    
	@XStreamAsAttribute
    @XStreamAlias("gender")
    private String gender;
    
	@XStreamAsAttribute
    @XStreamAlias("maritalStatus")
    private String maritalStatus;
    
	@XStreamAsAttribute
    @XStreamAlias("religion")
    private String religion;
    
    @XmlAttribute
    @XStreamAlias("Address")
    private Address address;
   
}
