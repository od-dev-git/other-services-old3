package org.egov.dx.web.models.TL;

import java.util.List;

import org.egov.dx.web.models.IssuedTo;
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
public class TLIssuedTo {
	
	@XStreamAlias("Organization")
	OrganizationTL organization;
	
	@XStreamAlias("Person")
	Person person;

}
