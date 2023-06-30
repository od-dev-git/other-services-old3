package org.egov.dx.web.models.MR;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.Address;
import org.egov.dx.web.models.Organization;
import org.egov.dx.web.models.Person;

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
public class MRIssuedTo {
	
	@XStreamAlias("Person")
	@XmlAttribute
    private PersonMR personGroom;

	@XStreamAlias("Person")
	@XmlAttribute
    private PersonMR personBride;
}
