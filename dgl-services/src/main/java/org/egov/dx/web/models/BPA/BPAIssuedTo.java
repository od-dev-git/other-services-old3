package org.egov.dx.web.models.BPA;

import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.Organization;
import org.egov.dx.web.models.Person;
import org.egov.dx.web.models.MR.PersonMR;
import org.egov.dx.web.models.TL.TLIssuedTo;

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
@XStreamAlias("IssuedTo")
public class BPAIssuedTo {
	
	@XStreamAlias("Person")
	@XmlAttribute
    private PersonBPA person;

}
